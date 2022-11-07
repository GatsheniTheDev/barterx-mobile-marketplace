package com.example.barterx;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.barterx.algorithms.HaversineDistanceAlgorithm;
import com.example.barterx.databinding.ActivityDashboardBinding;
import com.example.barterx.model.ListingDto;
import com.example.barterx.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Dashboard extends Drawer_Base {

    ActivityDashboardBinding activityDashboardBinding;
    private FirebaseFirestore database;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Profile profile = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("HOME");
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user == null ){
            startActivity(new Intent(Dashboard.this,Login.class));
        }
        database.collection("listing").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                     if(task.isSuccessful()){
                         QuerySnapshot snapshot = task.getResult();
                         List<ListingDto> list = new ArrayList<>();

                         if(user!= null) {

                             Task<DocumentSnapshot> reference = database.collection("users").document(user.getEmail())
                                     .get()
                                     .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                         @Override
                                         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                             if (task.isSuccessful()) {
                                                profile = task.getResult().toObject(Profile.class);
                                                 for (DocumentSnapshot snap : snapshot.getDocuments()) {
                                                     ListingDto dto = snap.toObject(ListingDto.class);
                                                           list.add(dto);
                                                 }
                                             }
                                             HaversineDistanceAlgorithm distanceAlgorithm = new HaversineDistanceAlgorithm(list, profile);
                                             Log.d("TagTest", distanceAlgorithm.FindNearestUsers(100).stream().filter(o-> !o.getMerchantId().equals(user.getUid())).collect(Collectors.toList()).toString());
                                         }
                                     });
                         }
                     }
                    }
                });


    }
}