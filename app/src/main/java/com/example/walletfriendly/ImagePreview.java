package com.example.walletfriendly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ImagePreview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        // Find the ImageView in the layout
        ImageView imageView = findViewById(R.id.imageViewPreview);

        // Retrieve the image path from the intent extras
        String imagePath = getIntent().getStringExtra("imagePath");

        // Load and display the image using the image path
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
    }
}
