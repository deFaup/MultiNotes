package com.example.multinotes;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class NoteViewHolder extends RecyclerView.ViewHolder
{
    public TextView noteTitle;
    public TextView noteDate;
    public TextView noteText;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        noteTitle = itemView.findViewById(R.id.noteTitle);
        noteDate = itemView.findViewById(R.id.noteDate);
        noteText = itemView.findViewById(R.id.noteText);
    }
}
