package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView resetPasswordTextView, createAccountTextView;
    private DatabaseHelper databaseHelper;

    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);
        resetPasswordTextView = findViewById(R.id.resetPassword);
        createAccountTextView = findViewById(R.id.createAccount);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Add a TextWatcher to the password field to check the password length
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No implementation needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No implementation needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check password length and display error message if necessary
                String password = s.toString().trim();
                if (password.length() < MIN_PASSWORD_LENGTH) {
                    passwordEditText.setError("Password must be at least " + MIN_PASSWORD_LENGTH + " characters long");
                } else {
                    passwordEditText.setError(null);
                }
            }
        });

        // Set click listener for the login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the username and password from the input fields
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Check if it's an admin login
                if (isAdminLogin(username, password)) {
                    // Perform admin login operation
                    if (databaseHelper.checkAdmin(username, password)) {
                        // Admin login successful, start the admin activity
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        return; // Exit the method to prevent further execution
                    }
                }

                // Perform regular user login operation
                if (databaseHelper.checkUser(username, password)) {
                    // User login successful, start the next activity
                    Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    // Invalid credentials
                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set click listener for the reset password text view
        resetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the reset password activity
                Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the create account text view
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the create account activity
                Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean isAdminLogin(String username, String password) {
        // Check if the username and password match admin credentials
        // You can customize this logic based on your admin credentials storage
        String adminUsername = "admin";
        String adminPassword = "Admin123";
        return username.equals(adminUsername) && password.equals(adminPassword);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database helper on activity destroy
        databaseHelper.close();
    }
}
