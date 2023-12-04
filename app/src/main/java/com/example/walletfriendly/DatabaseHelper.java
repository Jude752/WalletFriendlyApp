package com.example.walletfriendly;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database information
    private static final String DATABASE_NAME = "walletFriendly.db";
    private static final int DATABASE_VERSION = 8;

    // Users table
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FIREBASE_USERID = "firebase_userid";

    // Goals table
    public static final String TABLE_GOALS = "goals";
    private static final String COLUMN_GOAL_ID = "goal_id";
    private static final String COLUMN_FOOD_GOAL = "food_goal";
    private static final String COLUMN_RENTAL_GOAL = "rental_goal";
    private static final String COLUMN_BILLS_GOAL = "bills_goal";
    private static final String COLUMN_TRANSPORTATION_GOAL = "transportation_goal";
    private static final String COLUMN_OTHER_GOAL = "other_goal";
    public static final String COLUMN_TOTAL_GOAL = "total_goal";

    // Transactions table
    public static final String TABLE_TRANSACTIONS = "transactions";
    private static final String COLUMN_TRANSACTION_ID = "transaction_id";
    public static final String COLUMN_EXPENSES = "expenses";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_INCOME = "income";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_BUDGET = "budget";

    // Create statement for Users table
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_USERNAME + " TEXT," +
            COLUMN_EMAIL + " TEXT," +
            COLUMN_PASSWORD + " TEXT," +
            COLUMN_FIREBASE_USERID + " TEXT" + ")";

    // Create statement for Goals table
    private static final String CREATE_TABLE_GOALS = "CREATE TABLE " + TABLE_GOALS + "(" +
            COLUMN_GOAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_FOOD_GOAL + " REAL," +
            COLUMN_RENTAL_GOAL + " REAL," +
            COLUMN_BILLS_GOAL + " REAL," +
            COLUMN_TRANSPORTATION_GOAL + " REAL," +
            COLUMN_OTHER_GOAL + " REAL," +
            COLUMN_TOTAL_GOAL + " REAL" + ")";

    // Create statement for Transactions table
    private static final String CREATE_TABLE_TRANSACTIONS = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" +
            COLUMN_TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_EXPENSES + " REAL," +
            COLUMN_CATEGORY + " TEXT," +
            COLUMN_INCOME + " REAL," +
            COLUMN_BALANCE + " REAL," +
            COLUMN_BUDGET + " REAL" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_GOALS);
        db.execSQL(CREATE_TABLE_TRANSACTIONS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    /**
     * Check if the provided username and password belong to the admin.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the username and password match the admin's credentials, False otherwise.
     */
    public boolean checkAdmin(String username, String password) {
        String adminUsername = "admin";
        String adminPassword = "Admin123";
        return username.equals(adminUsername) && password.equals(adminPassword);
    }

    /**
     * Add a new user to the database.
     *
     * @param name           The name of the user.
     * @param email          The email of the user.
     * @param password       The password of the user.
     * @param username       The username of the user.
     * @param firebaseUserId The Firebase user ID associated with the user.
     * @return The ID of the newly inserted user.
     */
    public long addUser(String name, String email, String password, String username, String firebaseUserId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FIREBASE_USERID, firebaseUserId);

        long rowId = db.insert(TABLE_USERS, null, values);
        db.close();
        return rowId;
    }

    /**
     * Retrieve a user from the database based on the username.
     *
     * @param username The username of the user to retrieve.
     * @return The Users object representing the user, or null if not found.
     */
    public Users getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Users user = null;

        String[] projection = {COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int usernameIndex = cursor.getColumnIndex(COLUMN_USERNAME);
            int emailIndex = cursor.getColumnIndex(COLUMN_EMAIL);
            int passwordIndex = cursor.getColumnIndex(COLUMN_PASSWORD);

            int id = cursor.getInt(idIndex);
            String fetchedUsername = cursor.getString(usernameIndex);
            String email = cursor.getString(emailIndex);
            String password = cursor.getString(passwordIndex);

            user = new Users(id, fetchedUsername, email, password);
        }

        cursor.close();
        db.close();

        return user;
    }

    /**
     * Check if a user exists in the database with the provided username and password.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the user exists, False otherwise.
     */
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {COLUMN_USERNAME, COLUMN_PASSWORD};
        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(
                TABLE_USERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean userExists = (cursor.getCount() > 0);

        cursor.close();
        return userExists;
    }

    /**
     * Add a new goal to the database.
     *
     * @param foodGoal          The food goal amount.
     * @param rentalGoal        The rental goal amount.
     * @param billsGoal         The bills goal amount.
     * @param transportationGoal The transportation goal amount.
     * @param otherGoal         The other goal amount.
     * @param totalGoal         The total goal amount.
     * @return The ID of the newly inserted goal.
     */
    public long addGoal(double foodGoal, double rentalGoal, double billsGoal, double transportationGoal, double otherGoal, double totalGoal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_GOAL, foodGoal);
        values.put(COLUMN_RENTAL_GOAL, rentalGoal);
        values.put(COLUMN_BILLS_GOAL, billsGoal);
        values.put(COLUMN_TRANSPORTATION_GOAL, transportationGoal);
        values.put(COLUMN_OTHER_GOAL, otherGoal);
        values.put(COLUMN_TOTAL_GOAL, totalGoal);
        long rowId = db.insert(TABLE_GOALS, null, values);
        db.close();
        return rowId;
    }

    /**
     * Add a new transaction to the database.
     *
     * @param expenses The expenses amount.
     * @param category The category of the transaction.
     * @param income   The income amount.
     * @param balance  The balance amount.
     * @param budget   The budget amount.
     * @return The ID of the newly inserted transaction.
     */
    public long addTransaction(double expenses, String category, double income, double balance, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXPENSES, expenses);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_INCOME, income);
        values.put(COLUMN_BALANCE, balance);
        values.put(COLUMN_BUDGET, budget);
        long rowId = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return rowId;
    }
}
