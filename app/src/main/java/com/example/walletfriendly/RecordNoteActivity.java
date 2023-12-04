package com.example.walletfriendly;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class RecordNoteActivity extends AppCompatActivity {

    // Initializing all variables..
    private TextView startTv, stopTv, playTv, stopPlayTv, statusTv;

    // Creating a variable for MediaRecorder object class.
    private MediaRecorder mediaRecorder;

    // Creating a variable for MediaPlayer class.
    private MediaPlayer mediaPlayer;

    // String variable for storing the file name.
    private static final String FILE_NAME = "AudioRecording.3gp";

    // Constant for storing audio permission.
    private static final int REQUEST_AUDIO_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_note);

        // Initialize all variables with their layout items.
        statusTv = findViewById(R.id.tvStatus);
        startTv = findViewById(R.id.btnRecord);
        stopTv = findViewById(R.id.btnStop);
        playTv = findViewById(R.id.btnPlay);
        stopPlayTv = findViewById(R.id.btnStopPlay);
        stopTv.setBackgroundColor(getResources().getColor(R.color.black));
        playTv.setBackgroundColor(getResources().getColor(R.color.black));
        stopPlayTv.setBackgroundColor(getResources().getColor(R.color.black));

        startTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start recording method will start the recording of audio.
                startRecording();
            }
        });

        stopTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pause recording method will pause the recording of audio.
                pauseRecording();
            }
        });

        playTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Play audio method will play the recorded audio.
                playAudio();
            }
        });

        stopPlayTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pause play method will pause the play of audio.
                pausePlaying();
            }
        });
    }

    private void startRecording() {
        // Check permission method is used to check if the user has granted permission to record and store the audio.
        if (checkPermissions()) {
            // Set background color method will change the background color of TextView.
            stopTv.setBackgroundColor(getResources().getColor(R.color.black));
            startTv.setBackgroundColor(getResources().getColor(R.color.theme));
            playTv.setBackgroundColor(getResources().getColor(R.color.black));
            stopPlayTv.setBackgroundColor(getResources().getColor(R.color.black));

            // Initialize the file name with the path of the recorded audio file.
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILE_NAME;

            // Initialize the MediaRecorder class.
            mediaRecorder = new MediaRecorder();

            // Set the audio source as MIC.
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);

            // Set the output format as THREE_GPP.
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

            // Set the audio encoder.
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            // Set the output file location.
            mediaRecorder.setOutputFile(filePath);

            try {
                // Prepare the MediaRecorder.
                mediaRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }

            // Start the audio recording.
            mediaRecorder.start();
            statusTv.setText("Recording Started");
        } else {
            // If audio recording permissions are not granted by the user, request the permissions.
            requestPermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // This method is called when the user grants the permission for audio recording.
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private boolean checkPermissions() {
        // This method is used to check permissions.
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        // This method is used to request the permission for audio recording and storage.
        ActivityCompat.requestPermissions(RecordNoteActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    public void playAudio() {
        stopTv.setBackgroundColor(getResources().getColor(R.color.black));
        startTv.setBackgroundColor(getResources().getColor(R.color.black));
        playTv.setBackgroundColor(getResources().getColor(R.color.theme));
        stopPlayTv.setBackgroundColor(getResources().getColor(R.color.black));

        // Play the recorded audio using MediaPlayer class.
        mediaPlayer = new MediaPlayer();
        try {
            // Set the data source which is the file name.
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FILE_NAME);

            // Prepare the MediaPlayer.
            mediaPlayer.prepare();

            // Start playing the audio.
            mediaPlayer.start();
            statusTv.setText("Recording Started Playing");
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
        stopTv.setBackgroundColor(getResources().getColor(R.color.theme));
        startTv.setBackgroundColor(getResources().getColor(R.color.black));
        playTv.setBackgroundColor(getResources().getColor(R.color.black));
        stopPlayTv.setBackgroundColor(getResources().getColor(R.color.black));

        // Stop the audio recording.
        mediaRecorder.stop();

        // Release the MediaRecorder.
        mediaRecorder.release();
        mediaRecorder = null;
        statusTv.setText("Recording Stopped");
    }

    public void pausePlaying() {
        // Release the MediaPlayer and pause the playing of recorded audio.
        mediaPlayer.release();
        mediaPlayer = null;
        stopTv.setBackgroundColor(getResources().getColor(R.color.black));
        startTv.setBackgroundColor(getResources().getColor(R.color.black));
        playTv.setBackgroundColor(getResources().getColor(R.color.black));
        stopPlayTv.setBackgroundColor(getResources().getColor(R.color.theme));
        statusTv.setText("Recording Play Stopped");
    }
}
