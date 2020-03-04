package com.example.notepad.db;

import android.widget.LinearLayout;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.notepad.model.Note;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface NotesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertNotes(Note note);

    @Delete
    void delete(Note note);

    @Query("Delete FROM Note WHERE id = :noteID ")
    public void deleteNotesByID(int noteID);

    @Query("DELETE FROM Note")
    void deleteAllNotes();

    @Query("Delete FROM Note WHERE id = :noteID AND version = :noteVersion")
    public void deleteNotesByIdVersion(int noteID, int noteVersion);


    @Query("SELECT * FROM Note WHERE id = :noteID AND version = :noteVersion")
    public Note getLatestNotes(int noteID, int noteVersion);

    @Query("SELECT * FROM Note GROUP BY id,version")
    public List<Note> getLateNotes();


    @Query("SELECT * FROM Note WHERE id = :noteId order by version desc")
    public List<Note> getNoteById(int noteId);

    @Query("SELECT * FROM Note")
    public List<Note> getAllNotes();

    @Query("SELECT COUNT(version) FROM Note WHERE id = :noteID")
    public int getLatestVersion(int noteID);

    @Query("SELECT COUNT (DISTINCT id)FROM Note")
    public int getLastId();

    @Query("Select DISTINCT id From Note")
    public int distictId();


    @Query("SELECT imageListParsedPath FROM Note WHERE id = :noteId AND version = :noteVersion")
    public List<String> getImageByIdVersion(int noteId, int noteVersion);





}
