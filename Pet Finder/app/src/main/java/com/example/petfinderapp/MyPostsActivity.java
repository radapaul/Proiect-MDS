package com.example.petfinderapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPostsActivity extends AppCompatActivity {
    //used to get only own posts
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_posts);

        userEmail = getIntent().getStringExtra("email");

        List<CardItem> myCardItems = new ArrayList<>();
        RecyclerView myPostsRecyclerView = findViewById(R.id.myPostsRecyclerView);
        MyPostsAdapter myAdapter = new MyPostsAdapter(myCardItems);
        myPostsRecyclerView.setAdapter(myAdapter);
        myPostsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.133/PetFinder/read_my_posts.php?email=" + userEmail;

        //GET request to fetch the user's posts
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String imageUrl = jsonObject.getString("path");
                                String sector = jsonObject.getString("sector");
                                String description = jsonObject.getString("description");
                                String picassopath = "http://192.168.1.133/PetFinder/" + imageUrl;
                                myCardItems.add(new CardItem(picassopath, sector, description));
                            }
                            myAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyPostsActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });
        //adds the request to the queue to be executed
        queue.add(stringRequest);
    }
}

