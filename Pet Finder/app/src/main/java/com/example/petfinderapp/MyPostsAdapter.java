package com.example.petfinderapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostsViewHolder> {

    private final List<CardItem> myCardItems;

    public MyPostsAdapter(List<CardItem> myCardItems) {
        this.myCardItems = myCardItems;
    }

    static class MyPostsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView sectorTextView, descriptionTextView;
        Button refreshButton, deleteButton, editButton;

        public MyPostsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myImageView);
            sectorTextView = itemView.findViewById(R.id.mySectorTextView);
            descriptionTextView = itemView.findViewById(R.id.myDescriptionTextView);
            refreshButton = itemView.findViewById(R.id.refreshButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

    @NonNull
    @Override
    public MyPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item, parent, false);
        return new MyPostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position) {
        CardItem cardItem = myCardItems.get(position);
        // Use Picasso to load the image from the URL into the ImageView
        Picasso.get().load(cardItem.getImageUrl()).into(holder.imageView);
        // Set the sector and description
        holder.sectorTextView.setText("Sector: " + cardItem.getSector());
        holder.descriptionTextView.setText("Description: " + cardItem.getDescription());

        // Set onClick listeners for each of the buttons
        holder.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle refresh button click
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle delete button click
            }
        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle edit button click
            }
        });
    }


    @Override
    public int getItemCount() {
        return myCardItems.size();
    }

}
