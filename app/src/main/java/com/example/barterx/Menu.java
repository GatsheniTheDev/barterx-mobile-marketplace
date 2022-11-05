package com.example.barterx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.barterx.model.Profile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Menu extends AppCompatActivity {
    private TextView txtEamil;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ImageView img;
    private FusedLocationProviderClient location;
    private Location geolocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        txtEamil = findViewById(R.id.email);
        img = findViewById(R.id.image);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        location = LocationServices.getFusedLocationProviderClient(Menu.this);
        FirebaseUser user = mAuth.getCurrentUser();

        txtEamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });

        getProfileById(user.getEmail());
        img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               getLocation();
           }
       });


    }

    //get user profile info
    private void getProfileById(String collectionName){
        DocumentReference documentReference = db.collection("users").document(collectionName);
        documentReference.get().addOnCompleteListener( new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                      Profile user = document.toObject(Profile.class);
                        txtEamil.setText("Logged in As: "+ user.getFirstname());
                        Glide.with(getApplicationContext()).load(user.getImageUrl()).into(img);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.getMessage();
                Log.d("Tag",e.getMessage());
            }
        });
    }

    //this login to be moved from here
    private void getLocation(){
        if(ActivityCompat.checkSelfPermission(Menu.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Menu.this, Manifest.permission.ACCESS_COARSE_LOCATION) ==PackageManager.PERMISSION_GRANTED){

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)|| locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                location.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){
                            geolocation = task.getResult();
                            Log.d("Location","Lat--> " +geolocation.getLatitude()+ "  Long   ---> " + geolocation.getLongitude());
                        }
                    }
                });
        }
    }
    }
}