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
import com.example.notepad.db.NotesDao;
import com.example.notepad.db.NotesDatabase;
import com.example.notepad.model.Note;
import com.example.notepad.utility.ImageBitmapString;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class ModifyNote extends AppCompatActivity {

    private static final String TAG = "ModifyNote";

    private EditText Title, etNote;
    private Button btAddImage, btModify;
    private NotesDao dao;
    Context mContext;
    ImageAdapter adapter;
    FrameLayout imageFragmenContainer;
    RecyclerView imageRecycleView;
    ArrayList<Bitmap> Bitmaps;
    ArrayList<String> newImageSources;//edited
    Note modifiedNote;
    Note note;
    Uri imageUri;
    ArrayList<Uri> imageUris=new ArrayList<>();;

    private int id;
    private String title;
    private String noteText;
    private long createdDateTime;
    private long modifiedDateTime;
    private int version;
    private ArrayList<String> getImageSources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note);

        Title = (EditText) findViewById(R.id.etTitle);
        Title.setSelection(Title.getText().length());

        btAddImage = findViewById(R.id.btAddImageFromGallery);
        etNote = findViewById(R.id.etNote);
        etNote.setSelection(etNote.getText().length());

        imageFragmenContainer = findViewById(R.id.imageFragmenContainer);
        imageRecycleView = findViewById(R.id.imageRecycleView);

        dao = NotesDatabase.getInstance(this).notesDao();

        getMakeNotes();

        btAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadImagesFromGallery();

            }
        });


        btModify = findViewById(R.id.buttonSave);
        btModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                modifyNote();
            }

        });

    }

    private void loadImagesFromGallery() {
        if (ActivityCompat.checkSelfPermission(ModifyNote.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ModifyNote.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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
        if (requestCode == 1 && resultCode == RESULT_OK) {

            imageFragmenContainer.setVisibility(View.VISIBLE);
            ClipData clipData = data.getClipData(); //clip data will be null if user select one item
            if (clipData != null) {

                for (int i = 0; i < clipData.getItemCount(); i++) {
                   imageUri = clipData.getItemAt(i).getUri();
                    try {

                       imageUris.add(imageUri);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
            else {
                imageUri = data.getData();
                try {
                    imageUris.add(imageUri);
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

    private void getMakeNotes() {

        getImageSources = new ArrayList<>();

        Intent intent = getIntent();
        id = intent.getExtras().getInt("ID");
        version = intent.getExtras().getInt("VERSION");
        note = dao.getLatestNotes(id,version);

        id = note.getId();
        title = note.getTitle();
        noteText = note.getNoteText();
        createdDateTime = note.getCreatedDateTime();
        getImageSources = note.getImageListParsedPath();

        Title.setText(title);
        etNote.setText(noteText);
        if (getImageSources != null) {
            imageFragmenContainer.setVisibility(View.VISIBLE);
            for (int i = 0; i < getImageSources.size(); i++) {
                Uri uri = Uri.parse(getImageSources.get(i));
                imageUris.add(uri);
            }
            imageRecycleView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            adapter = new ImageAdapter(imageUris);
            imageRecycleView.setAdapter(adapter);
        }

    }

    private void modifyNote() {

        String ModifiedTitle = Title.getText().toString();
        String ModifiedText = etNote.getText().toString();
        version = dao.getLatestVersion(id);
        if (ModifiedTitle.equals("")) {
            ModifiedTitle = "Untitled";
        }
        try {
            if(imageRecycleView.getAdapter().getItemCount()!=0) {
                Log.d(TAG, "imageRecycleView.getAdapter().getItemCount() : " + imageRecycleView.getAdapter().getItemCount());
                newImageSources = new ArrayList<>();
                for (int i = 0; i < imageRecycleView.getAdapter().getItemCount(); i++) {
                    String imageSource = imageUris.get(i).toString();
                    newImageSources.add(imageSource);
                    Log.d(TAG, "onSaveNewNote: imagesource " + newImageSources.get(i));
                }
            }
            else {
                newImageSources=null;
            }
        }
        catch (Exception e)
        {
            Log.d(TAG, "modifyNote: "+e.getMessage());
        }
        if (!ModifiedText.isEmpty()) {
            version++;
            modifiedDateTime = new Date().getTime();
            modifiedNote = new Note(id, ModifiedTitle, ModifiedText, createdDateTime,
                        modifiedDateTime, version, newImageSources);

            Log.d(TAG, "modifyNote: "+modifiedNote.toString());
           if(!noteText.equals(ModifiedText) || !title.equals(ModifiedTitle)) {
               try {
                   dao.insertNotes(modifiedNote);
                   Toast.makeText(this, "Modified Notes Saved!", Toast.LENGTH_SHORT).show();
                   finish();
               } catch (Exception e) {
                   Log.e("Exception ", e.getMessage());
               }
           }
           else{
                Toast.makeText(this, "No Changes Made..!! \nMake some change.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Empty Notes cannot be saved!! \n\t\tWrite something on notes.", Toast.LENGTH_SHORT).show();
        }

    }

}