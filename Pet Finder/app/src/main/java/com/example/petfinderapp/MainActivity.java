package com.example.petfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    List<CardItem> AllCardItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String username = getIntent().getStringExtra("username");
//        TextView usernameTextView = findViewById(R.id.usernameTextView);
//        usernameTextView.setText(username);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        String fullname = getIntent().getStringExtra("fullname");
        String email = getIntent().getStringExtra("email");
        View headerView = navigationView.getHeaderView(0);
        TextView fullnameTextView = headerView.findViewById(R.id.textViewFullname);
        TextView emailTextView = headerView.findViewById(R.id.textViewEmail);
        fullnameTextView.setText(fullname);
        emailTextView.setText(email);



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        //ADD POST BUTTON
        Button myButton = findViewById(R.id.myButton);
        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPostActivity.class);
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });





//recyclerview+adapter
        List<CardItem> cardItems = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CardAdapter adapter = new CardAdapter(cardItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//data from server
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.1.133/PetFinder/read.php";

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
                                String picassopath="http://192.168.1.133/PetFinder/"+imageUrl;
                                cardItems.add(new CardItem(picassopath, sector, description));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(stringRequest);
        AllCardItems=cardItems;

        Button filterButton = findViewById(R.id.filterButton);
        final CharSequence[] sectors = new CharSequence[]{"All","1", "2", "3", "4", "5", "6"};
        final String[] effectivelyFinalSector = new String[1];
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Filter by Sector")
                        .setItems(sectors, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                effectivelyFinalSector[0] = sectors[which].toString();
                                filterButton.setText("Filter: " + effectivelyFinalSector[0]);
                                filterPosts(effectivelyFinalSector[0]);
                            }
                        });
                builder.create().show();
            }
        });

    }



    private void filterPosts(String sector) {
        List<CardItem> filteredCardItems = new ArrayList<>();

        if(Objects.equals(sector, "All"))
            filteredCardItems = new ArrayList<>(AllCardItems);
        else
            for (CardItem item : AllCardItems) {
                if (item.getSector().equals(sector)) {
                    filteredCardItems.add(item);
                }
            }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        CardAdapter adapter = new CardAdapter(filteredCardItems);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        switch(item.getItemId()){
            case R.id.nav_logout: {
                Toast.makeText(getApplicationContext(), "Log out successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
                break;
            }
            case R.id.nav_dark:
                int grayColor = ContextCompat.getColor(getApplicationContext(), R.color.gray);
                int whiteColor = ContextCompat.getColor(getApplicationContext(), R.color.white);
                Drawable drawerBackground = drawer.getBackground();
                if (drawerBackground instanceof ColorDrawable) {
                    int currentColor = ((ColorDrawable) drawerBackground).getColor();

                    if (currentColor == grayColor) {
                        drawer.setBackgroundColor(whiteColor);
                        Toast.makeText(getApplicationContext(),"Dark mode disabled", Toast.LENGTH_SHORT).show();
                    } else {
                        drawer.setBackgroundColor(grayColor);
                        Toast.makeText(getApplicationContext(),"Dark mode enabled", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.nav_myposts:
                Intent intent = new Intent(MainActivity.this, MyPostsActivity.class);
                intent.putExtra("email", getIntent().getStringExtra("email"));
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
//        item.setChecked(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}