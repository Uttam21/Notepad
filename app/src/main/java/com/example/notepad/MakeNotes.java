package com.example.notepad;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.notepad.adapters.ImageAdapter;
import com.example.notepad.adapters.NotesAdapter;
import com.example.notepad.db.NotesDao;
import com.example.notepad.db.NotesDatabase;
import com.example.notepad.model.Image;
import com.example.notepad.model.Note;
import com.example.notepad.utility.ImageBitmapString;
import com.example.notepad.utility.NoteDateTime;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MakeNotes extends AppCompatActivity {

    private static final String TAG = "MakeNotes";

    private EditText Title, etNote;
    private Button btAddImage, btSave;
    private NotesDao dao;
    Context mContext;
    ImageAdapter adapter;
    FrameLayout imageFragmenContainer;
    //private ArrayList<Image> imageArrayList = new ArrayList<>();
    ArrayList<Bitmap> bitmaps;
    RecyclerView imageRecycleView ;
    Uri imageUri;
    ArrayList<Uri> imageUris;

    Note note;
    ArrayList<String> imageSources;//edited


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_notes);

        Title = (EditText) findViewById(R.id.etTitle);
        btAddImage = findViewById(R.id.btAddImageFromGallery);
        etNote = findViewById(R.id.etNote);
        imageFragmenContainer = findViewById(R.id.imageFragmenContainer);
        dao = NotesDatabase.getInstance(this).notesDao();
        imageRecycleView = findViewById(R.id.imageRecycleView);

        btAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImagesFromGallery();

            }
        });


        btSave = findViewById(R.id.buttonSave);
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onSaveNewNote();
            }


        });


    }


    private void loadImagesFromGallery() {


        if (ActivityCompat.checkSelfPermission(MakeNotes.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MakeNotes.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);

            return;
        }

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK)
        {

            imageFragmenContainer.setVisibility(View.VISIBLE);
            imageUris=new ArrayList<>();
            bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData(); //clip data will be null if user select one item
            if (clipData != null)
            {
                for (int i = 0; i < clipData.getItemCount(); i++)
                {
                   imageUri = clipData.getItemAt(i).getUri();
                    try
                    {
                        imageUris.add(imageUri);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }

            }
            else {
                    imageUri = data.getData(); //one item
                    try {
                        imageUris.add(imageUri);
                        Log.d(TAG, "onActivityResult: uri "+imageUri.toString());
                    }
                    catch (Exception e)
                    {
                         e.printStackTrace();
                    }

            }
            imageRecycleView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            adapter = new ImageAdapter(imageUris);
            imageRecycleView.setAdapter(adapter);

        }

    }







    private void onSaveNewNote() {

        int id = dao.getLastId();
        int version=1;
        String text = etNote.getText().toString();
        String title = Title.getText().toString();

        if (title.equals("")) {
            title = "Untitled";
        }
        try {
                if(imageRecycleView.getAdapter().getItemCount()!=0)
                {
                    Log.d(TAG, "imageRecycleView.getAdapter().getItemCount() : " + imageRecycleView.getAdapter().getItemCount());
                    imageSources = new ArrayList<>();

                    for(int i=0;i<=imageRecycleView.getAdapter().getItemCount();i++)
                     {
                         String imageSource = imageUris.get(i).toString();
                         imageSources.add(imageSource);
                         Log.d(TAG, "onSaveNewNote: imagesource "+imageSources.get(i));
                     }
                }
                else {

                    imageSources=null;
                }
        }

        catch (Exception e)
        {
            Log.d(TAG, "modifyNote: "+e.getMessage());
        }

        if (!text.isEmpty()) {

            id++;
            long date = new Date().getTime();
            note = new Note(id, title, text, date, 0, version, imageSources);

            try{
                dao.insertNotes(note);
                Log.d(TAG, "onSaveNewNote: "+note.toString());
                Toast.makeText(this, "Notes Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
            catch (Exception e)
            {
                Log.e("Exception ", e.getMessage());

            }
        }
        else {
                 Toast.makeText(this, "Empty Notes cannot be saved!!\nWrite something on notes.", Toast.LENGTH_SHORT).show();
        }

    }


}
