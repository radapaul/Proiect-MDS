package com.example.petfinderapp;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPostsAdapter extends RecyclerView.Adapter<MyPostsAdapter.MyPostsViewHolder> {

    private final List<CardItem> myCardItems;

    public MyPostsAdapter(List<CardItem> myCardItems) {
        this.myCardItems = myCardItems;
    }

    //ViewHolder class for the RecyclerView
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

    //populate the RecyclerView item with data
    @Override
    public void onBindViewHolder(@NonNull MyPostsViewHolder holder, int position) {
        CardItem cardItem = myCardItems.get(position);
        //Picasso to load the image from the URL into the ImageView
        Picasso.get().load(cardItem.getImageUrl()).into(holder.imageView);
        System.out.println("ImageUrl: " + cardItem.getImageUrl()); // Print the image URL for debugging purposes
        holder.sectorTextView.setText("Sector: " + cardItem.getSector());
        holder.descriptionTextView.setText("Description: " + cardItem.getDescription());

        //refresh button , post data to current date in the database , posts older than 7 days get deleted
        holder.refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.133/PetFinder/refresh.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(v.getContext(), "Post refreshed", Toast.LENGTH_SHORT).show();
                                Log.d("Response", response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Error.Response", String.valueOf(error));
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        String imageUrl = cardItem.getImageUrl();
                        String imagePathInDB = imageUrl.substring(imageUrl.indexOf("images/"));
                        params.put("image_path", imagePathInDB);
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                requestQueue.add(stringRequest);
            }
        });

        //delete post
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this post?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int position = holder.getBindingAdapterPosition();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.133/PetFinder/delete.php",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                if (response.trim().equals("Success")) {
                                                    Toast.makeText(v.getContext(), "Post deleted", Toast.LENGTH_SHORT).show();
                                                    myCardItems.remove(position);
                                                    notifyDataSetChanged();
                                                } else {
                                                    Toast.makeText(v.getContext(), "Failed to delete post", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        }) {
                                    @Override
                                    protected Map<String, String> getParams() {
                                        Map<String, String> params = new HashMap<>();
                                        String imageUrl = cardItem.getImageUrl();
                                        String imagePathInDB = imageUrl.substring(imageUrl.indexOf("images/"));
                                        params.put("image_path", imagePathInDB);
                                        return params;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                                requestQueue.add(stringRequest);
                            }
                        })
                        //cancel delete
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

        //edit post description
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.dialog_edit_description, null);
                builder.setView(view);
                final EditText descriptionEditText = view.findViewById(R.id.descriptionEditText);
                descriptionEditText.setText(cardItem.getDescription());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newDescription = descriptionEditText.getText().toString();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.133/PetFinder/update.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.trim().equals("Success")) {
                                            Toast.makeText(v.getContext(), "Description updated", Toast.LENGTH_SHORT).show();
                                            cardItem.setDescription(newDescription);
                                            notifyItemChanged(holder.getBindingAdapterPosition());
                                        } else {
                                            Toast.makeText(v.getContext(), "Failed to update description", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(v.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<>();
                                params.put("description", newDescription);
                                String imageUrl = cardItem.getImageUrl();
                                String imagePathInDB = imageUrl.substring(imageUrl.indexOf("images/"));
                                params.put("image_path", imagePathInDB);
                                return params;
                            }
                        };
                        RequestQueue requestQueue = Volley.newRequestQueue(v.getContext());
                        requestQueue.add(stringRequest);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myCardItems.size();
    }
}
