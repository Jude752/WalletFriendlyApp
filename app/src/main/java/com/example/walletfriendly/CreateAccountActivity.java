package com.example.walletfriendly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText rePasswordEditText;
    private Button signUpButton;

    private DatabaseHelper dbHelper;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        emailEditText = findViewById(R.id.email);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rePasswordEditText = findViewById(R.id.rePassword);
        signUpButton = findViewById(R.id.btnSignUp);

        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");

        if (userId != null) {
            // If userId is not null, it means we need to edit an existing user's information
            // Read data from the database for the specified user id
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            String[] projection = {
                    DatabaseContract.Users.COLUMN_NAME_EMAIL,
                    DatabaseContract.Users.COLUMN_NAME_USERNAME,
                    DatabaseContract.Users.COLUMN_NAME_PASSWORD
            };

            String selection = DatabaseContract.Users._ID + " = ?";
            String[] selectionArgs = { userId };

            Cursor cursor = db.query(
                    DatabaseContract.Users.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );

            if (cursor.moveToNext()) {
                // Extract email, username, and password from the cursor
                String emailText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_NAME_EMAIL));
                String usernameText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_NAME_USERNAME));
                String passwordText = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Users.COLUMN_NAME_PASSWORD));

                // Set the retrieved values to the respective EditText fields
                emailEditText.setText(emailText);
                usernameEditText.setText(usernameText);
                passwordEditText.setText(passwordText);
            }

            cursor.close();
            db.close();
        }

        // Add a TextWatcher to the usernameEditText to check for username availability
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String enteredUsername = s.toString().trim();

                if (!enteredUsername.isEmpty() && isUsernameExists(enteredUsername)) {
                    // Username already exists, show error message
                    usernameEditText.setError("Username is unavailable");
                } else {
                    // Username is available, clear error message
                    usernameEditText.setError(null);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered values from the EditText fields
                String enteredEmail = emailEditText.getText().toString().trim();
                String enteredUsername = usernameEditText.getText().toString().trim();
                String enteredPassword = passwordEditText.getText().toString().trim();
                String enteredRePassword = rePasswordEditText.getText().toString().trim();

                if (enteredEmail.isEmpty() || enteredUsername.isEmpty() || enteredPassword.isEmpty() || enteredRePassword.isEmpty()) {
                    // If any of the fields are empty, show a toast message to fill all fields
                    Toast.makeText(CreateAccountActivity.this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                } else if (!isEmailValid(enteredEmail)) {
                    // If the entered email is not valid, show a toast message for invalid email format
                    Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                } else if (!isPasswordValid(enteredPassword)) {
                    // If the entered password is not valid, show a toast message for invalid password format
                    Toast.makeText(CreateAccountActivity.this, "Password must contain at least 8 characters, including numbers, lowercase, and uppercase letters.", Toast.LENGTH_SHORT).show();
                } else if (!enteredPassword.equals(enteredRePassword)) {
                    // If the entered passwords do not match, show a toast message for password mismatch
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match. Please re-enter the same password.", Toast.LENGTH_SHORT).show();
                } else if (isUsernameExists(enteredUsername)) {
                    // If the entered username already exists, show a toast message for unavailable username
                    Toast.makeText(CreateAccountActivity.this, "Username is unavailable. Please choose a different username.", Toast.LENGTH_SHORT).show();
                } else if (isEmailExists(enteredEmail)) {
                    // If the entered email already exists, show a toast message for existing email address
                    Toast.makeText(CreateAccountActivity.this, "Email address is already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                } else {
                    // All validations passed, proceed to create or update the account

                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    if (userId == null) {
                        // Create a new user with Firebase Authentication
                        firebaseAuth.createUserWithEmailAndPassword(enteredEmail, enteredPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // User creation is successful, proceed to save additional user details in SQLite database

                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            String userId = firebaseUser.getUid();

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                                            ContentValues values = new ContentValues();
                                            values.put(DatabaseContract.Users.COLUMN_NAME_EMAIL, enteredEmail);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_USERNAME, enteredUsername);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_PASSWORD, enteredPassword);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_FIREBASE_USERID, userId);

                                            // Insert the values into the database
                                            long newRowId = db.insert(DatabaseContract.Users.TABLE_NAME, null, values);

                                            if (newRowId == -1) {
                                                // If the insertion fails, show a toast message for account creation error
                                                Toast.makeText(CreateAccountActivity.this, "Error while creating account. Please try again.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Account created successfully, show a toast message and navigate to the dashboard activity
                                                Toast.makeText(CreateAccountActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CreateAccountActivity.this, DashboardActivity.class);
                                                startActivity(intent);
                                            }

                                            db.close();
                                        } else {
                                            // User creation failed, show a toast message with the error
                                            Toast.makeText(CreateAccountActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        // Update an existing user's information

                        firebaseAuth.signInWithEmailAndPassword(enteredEmail, enteredPassword)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                            String userId = firebaseUser.getUid();

                                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                                            ContentValues values = new ContentValues();
                                            values.put(DatabaseContract.Users.COLUMN_NAME_EMAIL, enteredEmail);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_USERNAME, enteredUsername);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_PASSWORD, enteredPassword);
                                            values.put(DatabaseContract.Users.COLUMN_NAME_FIREBASE_USERID, userId);

                                            // Update the user's values in the database
                                            int rowsAffected = db.update(DatabaseContract.Users.TABLE_NAME, values,
                                                    DatabaseContract.Users._ID + " = ?", new String[]{userId});

                                            if (rowsAffected == 0) {
                                                // If the update fails, show a toast message for account update error
                                                Toast.makeText(CreateAccountActivity.this, "Error while updating account. Please try again.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Account updated successfully, show a toast message and navigate to the dashboard activity
                                                Toast.makeText(CreateAccountActivity.this, "Account updated successfully.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(CreateAccountActivity.this, DashboardActivity.class);
                                                startActivity(intent);
                                            }

                                            db.close();
                                        } else {
                                            // User authentication failed, show a toast message with the error
                                            Toast.makeText(CreateAccountActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        // Simple email validation using a regular expression
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    }

    private boolean isPasswordValid(String password) {
        // Password validation: At least 8 characters, including numbers, lowercase, and uppercase letters
        return password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}");
    }

    private boolean isUsernameExists(String username) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.Users._ID
        };

        String selection = DatabaseContract.Users.COLUMN_NAME_USERNAME + " = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.query(
                DatabaseContract.Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    private boolean isEmailExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseContract.Users._ID
        };

        String selection = DatabaseContract.Users.COLUMN_NAME_EMAIL + " = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.query(
                DatabaseContract.Users.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }
}
