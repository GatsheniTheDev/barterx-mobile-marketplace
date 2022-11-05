package com.example.barterx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.barterx.model.Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UserRegistrationStep2 extends AppCompatActivity {

   private  EditText firstname;
   private EditText lastname;
   private EditText phone;
   private Button cont;
   private TextView canc;
   private FirebaseFirestore db;
   private FirebaseAuth mAuth;
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

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add user details to their profile
                startActivity(new Intent(UserRegistrationStep2.this, UserRegistrationPart3.class));
                FirebaseUser user = mAuth.getCurrentUser(); // get instance of currently logged in user

                Profile profile = new Profile(user.getUid(),
                      firstname.getText().toString(),lastname.getText().toString(),
                      user.getEmail().toString(),phone.getText().toString(),"");
                DocumentReference ref = db.collection("users").document(user.getEmail());
                ref.set(profile, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(),"profile info saved",Toast.LENGTH_LONG).show();
                                startActivity(new Intent(UserRegistrationStep2.this, UserRegistrationPart3.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext()," failed to save profile info",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do not add details to user profile
                // TODO: redirect user to the home page
                startActivity(new Intent(UserRegistrationStep2.this, Dashboard.class));
                //The code below needs to be removed
                //startActivity(new Intent(UserRegistrationStep2.this, Login.class));
            }
        });
    }
}