package com.example.notepad.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.MainActivity;
import com.example.notepad.utility.ImageBitmapString;
import com.example.notepad.utility.NoteDateTime;
import com.example.notepad.NotesViewById;
import com.example.notepad.R;
import com.example.notepad.model.Note;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteHolder>{

    private ArrayList<Note> notes = new ArrayList<>();

    private Context mContext;

    public NotesAdapter(ArrayList<Note> notes, Context mContext) {
        this.notes = notes;
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.note_cv_layout,parent,false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, final int position) {


        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    100);

            return;
        }

        if(notes!=null)
        {

            holder.txtTitle.setText(notes.get(position).getId()+"."+ notes.get(position).getTitle());

            holder.txtVersion.setText("v"+String.valueOf(notes.get(position).getVersion()));
            holder.txtNotes.setText(notes.get(position).getNoteText());
            holder.txtCreatedAt.setText("Created on "+NoteDateTime.dateFromLong(notes.get(position).getCreatedDateTime()));

            if (notes.get(position).getModifiedDateTime()!=0)
            {
                holder.txtModifiedAt.setText("Modified on "+NoteDateTime.dateFromLong(notes.get(position).getModifiedDateTime()));
            }
            else {

                holder.txtModifiedAt.setVisibility(View.GONE);
            }


            if(notes.get(position).getImageListParsedPath()!=null){

                holder.imageRCVframe.setVisibility(View.VISIBLE);
                for(int i = 0; i<notes.get(position).getImageListParsedPath().size();i++) {

                   Uri uri = Uri.parse(notes.get(position).getImageListParsedPath().get(i));
                   holder.uriArrayList.add(uri);
               }
            holder.recyclerViewImage.setHasFixedSize(true);
            holder.mainImageAdapter = new MainImageAdapter(holder.uriArrayList); //3edited holder.bitmapz
            holder.recyclerViewImage.setAdapter(holder.mainImageAdapter);//4
           }


        }

        holder.noteCardViewMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NotesViewById.class);
                intent.putExtra("ID",notes.get(position).getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
            return notes.size();
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle,txtNotes,txtCreatedAt,txtModifiedAt,txtVersion;
        CardView noteCardViewMain;
        RecyclerView recyclerViewImage;
        MainImageAdapter mainImageAdapter;
        FrameLayout imageRCVframe;
        ArrayList<Uri> uriArrayList;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNotes=itemView.findViewById(R.id.txtNotes);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            txtModifiedAt= itemView.findViewById(R.id.txtModifiedAt);
            txtVersion = itemView.findViewById(R.id.txtVersion);
            noteCardViewMain= itemView.findViewById(R.id.note_cardview_id);
            recyclerViewImage = itemView.findViewById(R.id.recyclerViewImage);
            imageRCVframe= itemView.findViewById(R.id.imageRCVframe);
            uriArrayList = new ArrayList<>();
            recyclerViewImage.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));

        }
    }



}
