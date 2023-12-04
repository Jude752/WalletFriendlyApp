package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

public class FirstVideoActivity extends AppCompatActivity {
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_video);

        // Initialize and start the video playback
        VideoView videoView = findViewById(R.id.video1);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video1);
        videoView.start();

        // Set media controller for video playback controls
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Set onClickListener for the nextButton
        nextButton = findViewById(R.id.btnNextVideo);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to navigate to the SecondVideoActivity
                Intent intent = new Intent(FirstVideoActivity.this, SecondVideoActivity.class);
                intent.putExtra("title", "How to write a budget plan");
                startActivity(intent);
            }
        });
    }
}
