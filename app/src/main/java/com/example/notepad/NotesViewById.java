package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notepad.adapters.NotesAdapter;
import com.example.notepad.adapters.NotesByIdAdapter;
import com.example.notepad.db.NotesDao;
import com.example.notepad.db.NotesDatabase;
import com.example.notepad.model.Note;

import java.util.ArrayList;
import java.util.List;

public class NotesViewById extends AppCompatActivity {
    private ArrayList<Note> notes;
    private RecyclerView recyclerViewNoteById;
    private NotesByIdAdapter adapter;
    private NotesDao dao;
    private int id;
    private TextView txtTitleBar;
    private static final String TAG = "NotesViewById";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view_by_id);

        dao = NotesDatabase.getInstance(this).notesDao();
        txtTitleBar = findViewById(R.id.txtTitleBar);
        recyclerViewNoteById = findViewById(R.id.recyclerviewNoteViewById);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadNotes();
    }
    private void loadNotes() {
        this.notes = new ArrayList<>();
        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        txtTitleBar.setText("List of Notes By ID : " + id);

        Log.d(TAG, "loadNotes: ID " + id);
        List<Note> list = dao.getNoteById(id);
        this.notes.addAll(list);
        recyclerViewNoteById.setLayoutManager(new LinearLayoutManager(this));
        this.adapter = new NotesByIdAdapter(notes, this);
        this.recyclerViewNoteById.setAdapter(adapter);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
