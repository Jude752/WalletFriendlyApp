package com.example.walletfriendly;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

public class StoredImagesActivity extends AppCompatActivity {

    private LinearLayout imageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_images);

        imageContainer = findViewById(R.id.imageContainer);

        // Example code to add image views dynamically
        File[] imageFiles = getStoredImages();
        for (File imageFile : imageFiles) {
            addImageView(imageFile);
        }
    }

    private File[] getStoredImages() {
        // Logic to retrieve the stored image files
        // Replace this with your implementation
        File folder = new File(getExternalFilesDir(null) + "/Receipts");
        File[] files = folder.listFiles();

        // Sort the image files by name in ascending order
        if (files != null) {
            Arrays.sort(files, Comparator.comparing(File::getName));
        }

        return files;
    }

    private void addImageView(File imageFile) {
        ImageView imageView = new ImageView(this);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        imageView.setImageBitmap(bitmap);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePreview(imageFile);
            }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteConfirmationDialog(imageFile, imageView);
                return true;
            }
        });

        imageContainer.addView(imageView);
    }

    private void openImagePreview(File imageFile) {
        Intent intent = new Intent(this, ImagePreview.class);
        intent.putExtra("imagePath", imageFile.getAbsolutePath());
        startActivity(intent);
    }

    private void showDeleteConfirmationDialog(final File imageFile, final ImageView imageView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete this image?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteImage(imageFile);
                imageContainer.removeView(imageView);
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteImage(File imageFile) {
        if (imageFile.exists()) {
            if (imageFile.delete()) {
                // Image deleted successfully
                // You can perform any additional actions here, such as showing a toast message
            } else {
                // Failed to delete the image
                // You can handle the error condition here, such as showing an error message
            }
        } else {
            // Image file does not exist
            // You can handle the error condition here, such as showing an error message
        }
    }
}
