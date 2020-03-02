package com.example.multinotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>
{
    private static final String TAG = "NoteAdapter";
    private List<Note> noteList_;
    private MainActivity mainActivity_;

    NoteAdapter(List<Note> noteList, MainActivity mainActivity) {
        noteList_ = noteList;
        mainActivity_ = mainActivity;
    }

    // when you need to build a layout for the notes, this is executed
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Log.d(TAG, "onCreateViewHolder: ");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_entry, parent, false);

        itemView.setOnClickListener(mainActivity_);
        itemView.setOnLongClickListener(mainActivity_);

        return new NoteViewHolder(itemView);
    }

    // use the data from the list and put in inside the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: ");

        Note note = noteList_.get(position);

        holder.noteTitle.setText(note.getTitle_());
        holder.noteDate.setText(note.getLastUpdateTime_());
        holder.noteText.setText(note.getShortText());
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return noteList_.size();
    }
}
