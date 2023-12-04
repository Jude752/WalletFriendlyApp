package com.example.walletfriendly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> notesList;

    public NoteAdapter(Context context, ArrayList<String> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int position) {
        return notesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            // Inflate the list item layout if it's null
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item_note, parent, false);
        }

        // Get the note at the current position
        String note = notesList.get(position);

        // Set the note text in the TextView
        TextView textViewNote = convertView.findViewById(R.id.textViewNote);
        textViewNote.setText(note);

        return convertView;
    }
}
