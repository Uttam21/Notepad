package com.example.notepad;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.notepad.adapters.NotesAdapter;
import com.example.notepad.db.NotesDao;
import com.example.notepad.db.NotesDatabase;
import com.example.notepad.model.Note;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMain;
    private TextView txtNotesView;
    private ArrayList<Note> notes;
    private NotesAdapter adapterMain;
    private NotesDao dao;
    private Note note;
    //private ArrayList<Note> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtNotesView = findViewById(R.id.txtNotesView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onAddNewNote();

            }
        });


        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);

            return;
        }
        //dao = NotesDatabase.getInstance(this).notesDao();

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onResume() {
        super.onResume();

        try {
            dao = NotesDatabase.getInstance(this).notesDao();
            if(dao.getLastId()>0)
            {
                loadNotes();
            }

        }
        catch (Exception e)
        {
            Log.e("aaaa", e.getMessage());

        }
    }


    private void loadNotes() {

            //this.list = new ArrayList<>(); // ****for latest version****
            this.notes = new ArrayList<>();
            for (int i = 1; i <= dao.getLastId(); i++) {
                    try {
                        int version = dao.getLatestVersion(i);
                        note = dao.getLatestNotes(i, version);
                        //list.add(note);
                        notes.add(note);

                    } catch (Exception e) {
                        Log.e("aaaa1234", e.getMessage());
                    }

            }
            //this.notes.addAll(list);
            Log.d("abcd", "loadNotes: " + notes.toString());
            txtNotesView.setText("Notes By Latest Version");
            recyclerViewMain = findViewById(R.id.recyclerviewMain);
            recyclerViewMain.setHasFixedSize(true);
            recyclerViewMain.setLayoutManager(new LinearLayoutManager(this));
            this.adapterMain = new NotesAdapter(notes,this);
            this.recyclerViewMain.setAdapter(adapterMain);

    }
    private void onAddNewNote() {
        Intent intent = new Intent(MainActivity.this,MakeNotes.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.delete_all_notes:
                if(dao.getLastId()>0){

                    dao.deleteAllNotes();
                    this.notes = new ArrayList<>();
                    this.adapterMain = new NotesAdapter(notes,this);
                    this.recyclerViewMain.setAdapter(adapterMain);
                    txtNotesView.setText("No Notes Available");
                    Toast.makeText(this,"All notes deleted",Toast.LENGTH_SHORT).show();
                    return true;
                }
            return false;

       default: return super.onOptionsItemSelected(item);
        }

    }
}
