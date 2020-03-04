package com.example.notepad.adapters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.model.Image;
import com.example.notepad.model.Note;

import java.util.ArrayList;

public class MainImageAdapter extends RecyclerView.Adapter<MainImageAdapter.MainImageViewHolder> {
    private ArrayList<Uri> uriArrayList;

    public MainImageAdapter(ArrayList<Uri> uriArrayList) {
        this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public MainImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cv_layout,parent,false);
        return new MainImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainImageViewHolder holder, int position) {

        holder.imageView.setImageURI(uriArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return uriArrayList.size();
    }

    public class MainImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        CardView cardView;
        private ArrayList<Note> notes;//edited

        public MainImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgView_id);
            cardView = itemView.findViewById(R.id.img_cardview_id);
        }

    }
}
