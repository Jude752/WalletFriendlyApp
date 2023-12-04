package com.example.walletfriendly;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class WriteNoteActivity extends AppCompatActivity {

    private EditText editTextNote;
    private Button buttonSave;
    private Button buttonCancel;
    private ListView listViewNotes;
    private ArrayList<String> notesList;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);

        // Initialize views
        editTextNote = findViewById(R.id.etNote);
        buttonSave = findViewById(R.id.btnSave);
        buttonCancel = findViewById(R.id.btnCancel);
        listViewNotes = findViewById(R.id.listViewNotes);

        // Initialize the notes ArrayList and the NoteAdapter
        notesList = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, notesList);
        listViewNotes.setAdapter(noteAdapter);

        // Set click listeners
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Saves the note entered by the user.
     */
    private void saveNote() {
        String noteContent = editTextNote.getText().toString();

        // Add the note to the ArrayList
        notesList.add(noteContent);

        // Notify the adapter that the data set has changed
        noteAdapter.notifyDataSetChanged();

        // Display a toast message to indicate the note has been saved
        Toast.makeText(this, "Note saved: " + noteContent, Toast.LENGTH_SHORT).show();

        // Clear the EditText
        editTextNote.setText("");
    }
}
