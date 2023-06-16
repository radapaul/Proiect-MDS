package com.example.petfinderapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class OpenedCardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opened_card);

        String imageUrl = getIntent().getStringExtra("IMAGE_URL");
        String imageDescription = getIntent().getStringExtra("IMAGE_DESCRIPTION");

        ImageView zoomedImage = findViewById(R.id.zoomedImage);
        TextView txt = findViewById(R.id.viewdescription);

        txt.setText(imageDescription);
        Picasso.get().load(imageUrl).into(zoomedImage);
    }
}