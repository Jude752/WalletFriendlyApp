package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddNoteActivity extends AppCompatActivity {
    private TextView tvRecord; // TextView for voice notes
    private TextView tvWrite; // TextView for written notes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Initialize and set click listener for voice notes TextView
        tvRecord = findViewById(R.id.tvVoice);
        tvRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start RecordNote activity
                Intent intent = new Intent(AddNoteActivity.this, RecordNoteActivity.class);
                startActivity(intent);
            }
        });

        // Initialize and set click listener for written notes TextView
        tvWrite = findViewById(R.id.tvWrite);
        tvWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start WriteNote activity
                Intent intent = new Intent(AddNoteActivity.this, WriteNoteActivity.class);
                startActivity(intent);
            }
        });
    }
}
