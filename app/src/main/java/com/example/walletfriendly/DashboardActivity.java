package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {
    private TextView trackingTextView;
    private TextView goalsTextView;
    private TextView nearbyBusinessesTextView;
    private TextView tipsTextView;
    private Button settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find and set click listener for trackingTextView
        trackingTextView = findViewById(R.id.trackingTV);
        trackingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(TrackScreenActivity.class);
            }
        });

        // Find and set click listener for goalsTextView
        goalsTextView = findViewById(R.id.goalsTV);
        goalsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(SetGoalsActivity.class);
            }
        });

        // Find and set click listener for nearbyBusinessesTextView
        nearbyBusinessesTextView = findViewById(R.id.nearbyBusinessesTV);
        nearbyBusinessesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(MapScreen.class);
            }
        });

        // Find and set click listener for tipsTextView
        tipsTextView = findViewById(R.id.tipsTV);
        tipsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(FirstVideoActivity.class);
            }
        });

        // Find and set click listener for settingsButton
        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(SettingsActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            showToast("Settings button clicked");
            navigateTo(SettingsActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Helper method to navigate to the specified destination activity.
     *
     * @param destination The destination activity class to navigate to.
     */
    private void navigateTo(Class destination) {
        Intent intent = new Intent(DashboardActivity.this, destination);
        startActivity(intent);
    }

    /**
     * Helper method to show a short toast message.
     *
     * @param message The message to display in the toast.
     */
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
