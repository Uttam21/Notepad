package com.example.notepad.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notepad.R;
import com.example.notepad.model.Image;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private ArrayList<Uri> uriArrayList;
    private Context mContext;

    public ImageAdapter(ArrayList<Uri> uriArrayList) {

         this.uriArrayList = uriArrayList;
    }

    @NonNull
    @Override
    public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cv_layout,parent,false);
        return new ImageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImageHolder holder, int position) {
        holder.imageView.setImageURI(uriArrayList.get(position));
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.deleteButton.setVisibility(View.VISIBLE);
                holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.removeItem(holder.getAdapterPosition());
                    }
                });

                return false;
            }
        });


    }

    @Override
    public int getItemCount() {

        return uriArrayList.size();
    }


    public class ImageHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView,deleteButton;
        CardView cardView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgView_id);
            cardView = itemView.findViewById(R.id.img_cardview_id);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void removeItem(int position){
            uriArrayList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,  uriArrayList.size());
        }
    }

    
}
