package com.example.walletfriendly;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ToolsActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnUserManagement, btnAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        // Find buttons by their IDs
        btnUserManagement = findViewById(R.id.btnUserManagement);
        btnAnalytics = findViewById(R.id.btnAnalytics);

        // Set click listeners for the buttons
        btnUserManagement.setOnClickListener(this);
        btnAnalytics.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // Handle button clicks
        switch (view.getId()) {
            case R.id.btnUserManagement:
                // Perform user management actions
                Toast.makeText(this, "User management functionality for admins", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnAnalytics:
                // Perform analytics actions
                Toast.makeText(this, "Analytics functionality for admins", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
