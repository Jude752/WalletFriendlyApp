package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapScreen extends AppCompatActivity {
    private Button find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_screen);

        // Initialize the "Find" button
        find = findViewById(R.id.btnFind);

        // Set click listener for the "Find" button
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to navigate to the SearchMap activity
                Intent intent = new Intent(MapScreen.this, SearchMapActivity.class);
                startActivity(intent);
            }
        });
    }
}
