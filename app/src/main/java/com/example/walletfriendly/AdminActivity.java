package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private Button toolButton;
    private Button newsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolButton = findViewById(R.id.btnTool);
        newsButton = findViewById(R.id.btnNews);

        toolButton.setOnClickListener(this);
        newsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnTool:
                // Open Tools Activity
                Intent toolsIntent = new Intent(AdminActivity.this, ToolsActivity.class);
                startActivity(toolsIntent);
                break;
            case R.id.btnNews:
                // Open News Activity
                Intent newsIntent = new Intent(AdminActivity.this, NewsActivity.class);
                startActivity(newsIntent);
                break;
        }
    }
}
