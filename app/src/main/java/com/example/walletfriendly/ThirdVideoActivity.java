package com.example.walletfriendly;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class ThirdVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_video);

        VideoView videoView = findViewById(R.id.video3);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video3);
        videoView.start();

        // Create a media controller and attach it to the VideoView
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }
}
