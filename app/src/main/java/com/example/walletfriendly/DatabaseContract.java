package com.example.walletfriendly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DatabaseContract {

    // Make constructor private to prevent instantiation
    private DatabaseContract() {
    }

    // Define schema for users table
    public static class Users implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_FIREBASE_USERID = "firebase_userid";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_EMAIL + " TEXT," +
                        COLUMN_NAME_USERNAME + " TEXT," +
                        COLUMN_NAME_PASSWORD + " TEXT," +
                        COLUMN_NAME_FIREBASE_USERID + " TEXT)";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;

        public Users(int id, String fetchedUsername, String email, String password) {
        }
    }

    public static class LoginDetails implements BaseColumns {
        public static final String TABLE_NAME = "login_details";
        public static final String COLUMN_NAME_USERNAME = "username";
        public static final String COLUMN_NAME_PASSWORD = "password";

        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_USERNAME + " TEXT," +
                        COLUMN_NAME_PASSWORD + " TEXT)";

        public static final String DROP_TABLE =
                "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    // Define the DatabaseHelper class
    public static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "WalletFriendly.db";
        private static final int DATABASE_VERSION = 1;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(Users.CREATE_TABLE);
            db.execSQL(LoginDetails.CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(Users.DROP_TABLE);
            db.execSQL(LoginDetails.DROP_TABLE);
            onCreate(db);
        }
    }
}
