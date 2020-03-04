package com.example.notepad.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.ModifyNote;
import com.example.notepad.utility.NoteDateTime;
import com.example.notepad.R;
import com.example.notepad.model.Note;

import java.util.ArrayList;

public class NotesByIdAdapter extends RecyclerView.Adapter<NotesByIdAdapter.NoteViewHolder>{

    private ArrayList<Note> notes;
    private Context mContext;

    public NotesByIdAdapter(ArrayList<Note> notes, Context mContext) {
        this.notes = notes;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.note_cv_layout,parent,false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder,final int position) {


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
                holder.recyclerViewImage1.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
                holder.recyclerViewImage1.setHasFixedSize(true);
                holder.mainImageAdapter = new MainImageAdapter(holder.uriArrayList); //3edited holder.bitmapz
                holder.recyclerViewImage1.setAdapter(holder.mainImageAdapter);
            }
        }

        holder.cardViewNoteViewById.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ModifyNote.class);
                    intent.putExtra("ID",notes.get(position).getId());
                    intent.putExtra("VERSION",notes.get(position).getVersion());
                    mContext.startActivity(intent);
                }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public Note getNoteAt(int position)
    {
        return notes.get(position);

    }
    public class NoteViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTitle,txtNotes,txtCreatedAt,txtModifiedAt,txtVersion;
        CardView cardViewNoteViewById;
        RecyclerView recyclerViewImage1;
        MainImageAdapter mainImageAdapter;
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        FrameLayout imageRCVframe;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNotes=itemView.findViewById(R.id.txtNotes);
            txtCreatedAt = itemView.findViewById(R.id.txtCreatedAt);
            txtModifiedAt= itemView.findViewById(R.id.txtModifiedAt);
            txtVersion = itemView.findViewById(R.id.txtVersion);
            cardViewNoteViewById= itemView.findViewById(R.id.note_cardview_id);
            recyclerViewImage1 = itemView.findViewById(R.id.recyclerViewImage);
            imageRCVframe= itemView.findViewById(R.id.imageRCVframe);

            txtNotes.setMaxLines(Integer.MAX_VALUE);
            txtNotes.setEllipsize(null);

        }
    }

}
