package com.example.notepad.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.notepad.model.Note;
import com.example.notepad.utility.ImageBitmapString;

@Database(entities = {Note.class},
            version = 1,exportSchema = false)

@TypeConverters({ImageBitmapString.class})

public abstract class NotesDatabase extends RoomDatabase {

    public abstract NotesDao notesDao();

    public static final String DATABASE_NAME = "NotesDb";

    public static NotesDatabase instance;



    public static NotesDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context,NotesDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries().build();
        }

        return instance;
    }



}
