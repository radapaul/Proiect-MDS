package com.example.petfinderapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
    private List<CardItem> cardItemList;

    public CardAdapter(List<CardItem> cardItemList) {
        this.cardItemList = cardItemList;
    }

    //view components within each item of the RecyclerView
    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView sectorView;
        TextView descriptionView;

        public CardViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            sectorView = itemView.findViewById(R.id.sector);
            descriptionView = itemView.findViewById(R.id.description);
        }
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardItem cardItem = cardItemList.get(position);
        //populate view components with the CardItem's data
        Picasso.get().load(cardItem.getImageUrl()).into(holder.imageView);
        holder.sectorView.setText("Sector: " + cardItem.getSector());
        holder.descriptionView.setText("Description: " + cardItem.getDescription());

        //open OpenedCardActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), OpenedCardActivity.class);
                intent.putExtra("IMAGE_URL", cardItem.getImageUrl());
                intent.putExtra("IMAGE_DESCRIPTION", cardItem.getDescription());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardItemList.size();
    }
}
