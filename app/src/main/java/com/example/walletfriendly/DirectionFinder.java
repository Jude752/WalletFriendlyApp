package com.example.walletfriendly;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class DirectionFinder {

    private Context context;

    public DirectionFinder(Context context) {
        this.context = context;
    }

    public void execute(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Parse the JSON response to get the directions data
                        JSONObject directionsData = response.getJSONObject("directions");
                        // ...

                        // Use the directions data to update the UI
                        // ...
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle errors
                }
        );

        // Add the request to the queue
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
