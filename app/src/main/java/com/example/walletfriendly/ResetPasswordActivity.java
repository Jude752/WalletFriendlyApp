package com.example.walletfriendly;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailBox;
    private Button resetButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        // Initialize views
        emailBox = findViewById(R.id.emailBox);
        resetButton = findViewById(R.id.btnReset);
        cancelButton = findViewById(R.id.btnCancel);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPasswordResetEmail();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });
    }

    /**
     * Sends a password reset email to the provided email address.
     */
    private void sendPasswordResetEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        String email = emailBox.getText().toString().trim();

        if (!TextUtils.isEmpty(email)) {
            // Send password reset email
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "Password reset email sent. Check your emails.", Toast.LENGTH_SHORT).show();
                                navigateToMainActivity();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(ResetPasswordActivity.this, "Please enter your email.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Navigates back to the main activity.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(ResetPasswordActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
