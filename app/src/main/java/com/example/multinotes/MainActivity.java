package com.example.multinotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";

    private ArrayList<Note> noteList = new ArrayList<>();
    private RecyclerView recycler;
    private NoteAdapter noteAdapter;
    private int EDIT_NOTE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteList = myJSON.loadFile(getApplicationContext(),getString(R.string.notes_backup_file));

        recycler = findViewById(R.id.recycler);
        // Bridge between the recycler and our ViewHolder; connect the list to the adapter
        noteAdapter = new NoteAdapter(noteList, this);
        // Link the recycler to the adapter
        recycler.setAdapter(noteAdapter);
        // Our recycler needs a layout where to put our noteList
        recycler.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setTitle(getString(R.string.app_name) + "(" + Integer.toString(noteList.size()) + ")");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuAdd:
                Log.d(TAG, "onOptionsItemSelected: ADD");
                openNewNote();
                break;
            case R.id.menuInfo:
                Log.d(TAG, "onOptionsItemSelected: Info");
                openAboutActivity();
                break;
        }
        return true;
    }

    /*      Activities       */
    private void openAboutActivity() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    // OLD CODE position = noteList.size() ie index not yet in the list
    // NEW CODE position = 0: index where to insert latest Note
    // NEWEST: if no position is given it means it is a new note that has to be placed at pos 0
        // this way we solve the pb what if the user opens the note 0 which exists
    private void openNewNote()
    {
        Intent intent = new Intent(this, EditActivity.class);
        startActivityForResult(intent, EDIT_NOTE);
    }
    private void editNote(View v)
    {
        int pos = recycler.getChildLayoutPosition(v);
        Note noteToEdit = noteList.get(pos);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(Intent.ACTION_ATTACH_DATA, noteToEdit);
        intent.putExtra("NOTE_POSITION", pos); //MANDATORY:: see EditActivity onCreate
        startActivityForResult(intent, EDIT_NOTE);
    }

    @Override
    public void onClick(View v) {editNote(v);}

    @Override
    public boolean onLongClick(View v)
    {
        Log.d(TAG, "onLongClick: ");
        final int pos = recycler.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.icon1);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(pos);
                myJSON.saveFile(getApplicationContext(),getString(R.string.notes_backup_file), noteList);
                noteAdapter.notifyDataSetChanged();
                getSupportActionBar().setTitle(getString(R.string.app_name) + "(" + Integer.toString(noteList.size()) + ")");
            }
        });

        builder.setMessage("Do you want to delete this note?");
        builder.setTitle("Delete note");

        AlertDialog dialog = builder.create();
        dialog.show();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Note> newNoteList = myJSON.loadFile(getApplicationContext(),getString(R.string.notes_backup_file));
        noteList.clear();
        noteList.addAll(newNoteList);
        //for(Note note : newNoteList) noteList.add(note);

        noteAdapter.notifyDataSetChanged();
        getSupportActionBar().setTitle(getString(R.string.app_name) + "(" + Integer.toString(noteList.size()) + ")");
    }

}
