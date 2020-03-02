package com.example.multinotes;

import android.content.Context;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class myJSON {

    private static final String TAG = "myJSON";

    public static ArrayList<Note> loadFile(Context context, String file) {

        Log.d(TAG, "loadFile: Loading JSON File in an array of Note");
        ArrayList<Note> noteList = new ArrayList<>();

        try {
            InputStream is = context.openFileInput(file);

            //ObjectInputStream in = new ObjectInputStream(is);
            //JSONArray noteArray = (JSONArray)in.readObject();
            //is.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();

            System.out.println(" JSON loaded= " + sb.toString());
            JSONArray notes = new JSONArray(sb.toString());
            JSONArray noteTitles = notes.getJSONArray(0);
            JSONArray noteDates = notes.getJSONArray(1);
            JSONArray noteTexts = notes.getJSONArray(2);

            for (int i=0; i < noteTitles.length(); ++i)
            {
                Note newNote = new Note(noteTitles.getString(i),
                        noteTexts.getString(i),
                        noteDates.getString(i));

                noteList.add(newNote);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return noteList;
    }

    public static void saveFile(Context context, String file, ArrayList<Note> noteList)
    {
        Log.d(TAG, "saveFile: ");
        try {
            FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);

            JSONArray notes = new JSONArray();
            JSONArray noteTitles = new JSONArray();
            JSONArray noteDates = new JSONArray();
            JSONArray noteTexts = new JSONArray();

            for(Note note : noteList) {
                noteTitles.put(note.getTitle_());
                noteDates.put(note.getLastUpdateTime_());
                noteTexts.put(note.getText_());
            }
            notes.put(noteTitles);
            notes.put(noteDates);
            notes.put(noteTexts);

            fos.write(notes.toString().getBytes());
            fos.close();

            Log.d(TAG, "saveProduct: JSON:\n" + notes.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File Not Found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
