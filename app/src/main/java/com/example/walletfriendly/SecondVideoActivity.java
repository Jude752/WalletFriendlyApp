package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class SecondVideoActivity extends AppCompatActivity {
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_video);

        // Set up the VideoView
        VideoView videoView = findViewById(R.id.video2);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video2);
        videoView.start();

        // Set up the MediaController
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set up the nextButton click listener
        nextButton = findViewById(R.id.btnNextVideo2);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the ThirdVideoActivity
                Intent intent = new Intent(SecondVideoActivity.this, ThirdVideoActivity.class);
                intent.putExtra("title", "How to write a budget plan");
                startActivity(intent);
            }
        });
    }
}
