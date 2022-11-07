package com.example.barterx;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterx.model.Profile;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationStep2 extends AppCompatActivity {
    private FusedLocationProviderClient location;
    private EditText firstname;
    private EditText lastname;
    private EditText phone;
    private Button cont;
    private TextView canc;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private double lat, lon;
    private  Profile profile;
    //private Map<String, Profile> profile = new HashMap<>();// map to store collection key value pair for user profile info.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration_step2);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firstname = findViewById(R.id.txtFirstName);
        lastname = findViewById(R.id.txtLastName);
        phone = findViewById(R.id.txtPhone);
        cont = findViewById(R.id.btnContinue);
        canc = findViewById(R.id.cancel);
        location = LocationServices.getFusedLocationProviderClient(this);
        requestPermission();
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser(); // get instance of currently logged in user
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            lat = task.getResult().getLatitude();
                            lon = task.getResult().getLongitude();
                            profile = new Profile(user.getUid(),
                                    firstname.getText().toString(), lastname.getText().toString(),
                                    user.getEmail().toString(),
                                    phone.getText().toString(),
                                    "", lat, lon);

                            DocumentReference ref = db.collection("users").document(user.getEmail());
                            ref.set(profile, SetOptions.merge())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(getApplicationContext(), "profile info saved", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(UserRegistrationStep2.this, UserRegistrationPart3.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), " failed to save profile info", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    }
                });
            }
        });

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do not add details to user profile
                // TODO: redirect user to the home page
                // TODO : edit 5
                //startActivity(new Intent(UserRegistrationStep2.this, Dashboard.class));
            }
        });
    }


    private void requestPermission() {
        ActivityResultLauncher<String[]> locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {
                            Boolean fineLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_FINE_LOCATION, false);
                            Boolean coarseLocationGranted = result.getOrDefault(
                                    Manifest.permission.ACCESS_COARSE_LOCATION, false);
                            if (fineLocationGranted != null && fineLocationGranted) {
                                // Precise location access granted.
                            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                                // Only approximate location access granted.
                            } else {
                                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                            }
                        }
                );
        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }
}