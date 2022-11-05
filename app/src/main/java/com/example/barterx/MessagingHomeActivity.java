package com.example.barterx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.barterx.adapter.MessagingUserAdapter;
import com.example.barterx.databinding.ActivityMessagingHomeBinding;
import com.example.barterx.model.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MessagingHomeActivity extends AppCompatActivity {
    private ActivityMessagingHomeBinding binding;
    private FirebaseFirestore databaseReference;
    private MessagingUserAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessagingHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        adapter = new MessagingUserAdapter(this);
        binding.userRecyclerView.setAdapter(adapter);
        binding.userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        getUserProfile();
    }


    private void getUserProfile(){
      databaseReference.collection("users").get()
              .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                  @Override
                  public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                      if (!queryDocumentSnapshots.isEmpty()) {
                          List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                          for (DocumentSnapshot snapshot : documents) {
                              Profile profile  = snapshot.toObject(Profile.class);
                              if(!profile.getId().equals(user.getUid())){
                                  adapter.add(profile);
                              }
                          }
                      } else {
                          // if the snapshot is empty we are displaying a toast message.
                          Toast.makeText(getApplicationContext(), "No data found in Database", Toast.LENGTH_SHORT).show();
                      }
                  }
              });
    }
}