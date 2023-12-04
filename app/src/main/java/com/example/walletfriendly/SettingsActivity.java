package com.example.walletfriendly;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREF_NAME = "MyPrefs";
    private static final String PREF_REMEMBER_ME = "rememberMe";

    private SharedPreferences sharedPreferences;
    private CheckBox rememberMe;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        rememberMe = findViewById(R.id.rememberMeCheckBox);
        logoutButton = findViewById(R.id.btnLogout);

        // Initialize shared preferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Set the initial state of the remember me checkbox based on saved preference
        boolean rememberMeState = sharedPreferences.getBoolean(PREF_REMEMBER_ME, false);
        rememberMe.setChecked(rememberMeState);

        // Set click listener for the remember me checkbox
        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the state of the checkbox to shared preferences
                boolean isChecked = rememberMe.isChecked();
                sharedPreferences.edit().putBoolean(PREF_REMEMBER_ME, isChecked).apply();

                if (isChecked) {
                    redirectToDashboard();
                }
            }
        });

        // Set click listener for the logout button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the shared preferences
                sharedPreferences.edit().clear().apply();

                // Clear all activities and start the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void redirectToDashboard() {
        // Redirect to the dashboard activity
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);

        // Finish the current activity to prevent going back to settings
        finish();
    }
}
