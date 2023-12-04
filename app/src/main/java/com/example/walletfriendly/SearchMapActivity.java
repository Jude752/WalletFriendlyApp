package com.example.walletfriendly;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private FrameLayout map;
    private GoogleMap gMap;
    private Location currentLocation;
    private Marker marker;
    private FusedLocationProviderClient fusedClient;
    private static final int REQUEST_CODE = 101;
    private SearchView searchView;
    private Button directionButton;
    private Polyline routePolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_map);

        map = findViewById(R.id.map);
        searchView = findViewById(R.id.searchView);
        directionButton = findViewById(R.id.btnDirection);
        searchView.clearFocus();

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        getLocation();

        directionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDirections();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String loc = searchView.getQuery().toString();
                if (loc == null || loc.isEmpty()) {
                    Toast.makeText(SearchMapActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(SearchMapActivity.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocationName(loc, 1);
                        if (!addressList.isEmpty()) {
                            LatLng latLng = new LatLng(addressList.get(0).getLatitude(), addressList.get(0).getLongitude());
                            if (marker != null) {
                                marker.setPosition(latLng);
                                marker.setTitle(loc);
                            } else {
                                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(loc);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                marker = gMap.addMarker(markerOptions);
                            }

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            gMap.animateCamera(cameraUpdate);
                        } else {
                            Toast.makeText(SearchMapActivity.this, "Location Not Found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedClient.getLastLocation();

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;

                    SupportMapFragment supportMapFragment =
                            (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(SearchMapActivity.this);
                }
            }
        });
    }

    private void getDirections() {
        String destination = searchView.getQuery().toString();
        if (destination == null || destination.isEmpty()) {
            Toast.makeText(SearchMapActivity.this, "Destination not found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentLocation == null) {
            Toast.makeText(SearchMapActivity.this, "Current location not found", Toast.LENGTH_SHORT).show();
            return;
        }

        // Build the URL for the API request
        String origin = currentLocation.getLatitude() + "," + currentLocation.getLongitude();
        String apiKey = "YOUR_API_KEY"; // Replace with your own API key
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin +
                "&destination=" + destination +
                "&key=" + apiKey;

        // Use the DirectionFinderAsyncTask class to make the API request and handle the response
        new DirectionFinderAsyncTask().execute(url);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;
        LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("My Current Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DirectionFinderAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            // Execute the HTTP request to fetch the directions data
            // and return the JSON response as a string
            String directionsJson = fetchDirectionsData(urls[0]);

            return directionsJson;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            if (jsonData != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    JSONArray routesArray = jsonObject.getJSONArray("routes");
                    if (routesArray.length() > 0) {
                        JSONObject routeObject = routesArray.getJSONObject(0);
                        JSONObject polylineObject = routeObject.getJSONObject("overview_polyline");
                        String encodedPolyline = polylineObject.getString("points");

                        List<LatLng> points = decodePolyline(encodedPolyline);
                        if (points.size() > 0) {
                            if (routePolyline != null) {
                                routePolyline.remove();
                            }

                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .addAll(points)
                                    .width(10)
                                    .color(Color.BLUE)
                                    .geodesic(true);
                            routePolyline = gMap.addPolyline(polylineOptions);

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            for (LatLng point : points) {
                                builder.include(point);
                            }
                            LatLngBounds bounds = builder.build();
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                            gMap.animateCamera(cameraUpdate);
                        } else {
                            Toast.makeText(SearchMapActivity.this, "No directions found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchMapActivity.this, "No routes found", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(SearchMapActivity.this, "Error fetching directions", Toast.LENGTH_SHORT).show();
            }
        }

        private String fetchDirectionsData(String requestUrl) {
            try {
                URL url = new URL(requestUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                bufferedReader.close();
                inputStream.close();

                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        private List<LatLng> decodePolyline(String encodedPolyline) {
            List<LatLng> points = new ArrayList<>();
            int index = 0, len = encodedPolyline.length();
            int lat = 0, lng = 0;

            while (index < len) {
                int b, shift = 0, result = 0;
                do {
                    b = encodedPolyline.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = encodedPolyline.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lng += dlng;

                double latLngValue = lat * 1e-5;
                double lngValue = lng * 1e-5;
                LatLng point = new LatLng(latLngValue, lngValue);
                points.add(point);
            }
            return points;
        }
    }
}
