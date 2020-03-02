package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class EditActivity extends AppCompatActivity
{
    public static final String TAG = "EditActivity";
    private EditText noteTitle;
    private EditText noteText;
    private int position;
    private boolean textHasChanged;
    private boolean newNote;
    private String titleOrigine;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = findViewById(R.id.noteTitle);
        noteText = findViewById(R.id.noteText);
        textHasChanged = false;

        noteText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {textHasChanged = true;}
        });

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.ACTION_ATTACH_DATA))
        {
            Note note_ = (Note) intent.getSerializableExtra(Intent.ACTION_ATTACH_DATA);
            noteTitle.setText(note_.getTitle_());
            noteText.setText(note_.getText_());
            position = intent.getIntExtra("NOTE_POSITION",0);
            newNote = false;
        }
        else{
            noteTitle.setText(getString(R.string.note_default_title));
            noteText.setText(getString(R.string.note_default_text));
            position = 0;
            newNote = true;
        }

        // Monitor changes in title and text
        titleOrigine = noteTitle.getText().toString();
        textHasChanged = false;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        Log.d(TAG, "onOptionsItemSelected: Save note then go back to Main");
        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
        // on Pause is called after
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        save();
    }

    @Override
    public void onBackPressed()
    {
        Log.d(TAG, "onBackPressed: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon1);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                save();
            }
        });
        builder.setMessage("Do you want to save your note before closing ?");
        builder.setTitle("Notes unsaved");
        AlertDialog dialog = builder.create();
        dialog.show();

        Intent data = new Intent();
        setResult(RESULT_OK, data);
        finish();
        super.onBackPressed();
    }

    /* Save the note in JSON file if changed or new note */
    private void save()
    {
        Log.d(TAG, "save: saving note to JSON");

        if(noteTitle.getText().toString().isEmpty()) {
            return;
        }
        boolean titleHasChanged = titleOrigine.compareTo(noteTitle.getText().toString()) != 0;

        if (newNote)
        {
            ArrayList<Note> noteList = myJSON.loadFile(getApplicationContext(),getString(R.string.notes_backup_file));
            Note note = new Note(noteTitle.getText().toString(), noteText.getText().toString());
            noteList.add(0, note);
            myJSON.saveFile(getApplicationContext(),getString(R.string.notes_backup_file), noteList);
            newNote = false;
            position = 0;
        }
        else if (textHasChanged || titleHasChanged)
        {
            ArrayList<Note> noteList = myJSON.loadFile(getApplicationContext(),getString(R.string.notes_backup_file));
            noteList.remove(position);
            Note note = new Note(noteTitle.getText().toString(), noteText.getText().toString());
            noteList.add(0, note);
            myJSON.saveFile(getApplicationContext(), getString(R.string.notes_backup_file), noteList);
            position = 0;
        }
        textHasChanged = false;
    }


}
