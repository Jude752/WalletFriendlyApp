package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TrackScreenActivity extends AppCompatActivity {
    private Button upload;
    private Button notes;
    private Button transactions;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_screen);

        // Find the upload button and set its click listener
        upload = findViewById(R.id.upload);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the ImageUploader activity
                Intent intent = new Intent(TrackScreenActivity.this, ImageUploader.class);
                startActivity(intent);
            }
        });

        // Find the notes button and set its click listener
        notes = findViewById(R.id.btnAddNote);
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the AddNoteActivity
                Intent intent = new Intent(TrackScreenActivity.this, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        // Find the transactions button and set its click listener
        transactions = findViewById(R.id.btnTransactions);
        transactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the ExpensesAndIncomeActivity
                Intent intent = new Intent(TrackScreenActivity.this, ExpensesAndIncomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
