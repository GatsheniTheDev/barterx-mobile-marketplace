package com.example.barterx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if(user == null)
                {
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }else{
                    // TODO: show home page
                    // TODO: add customer details to global object so that i can be accessible by the profile page
                    //startActivity(new Intent(MainActivity.this, Dashboard.class));
                    startActivity(new Intent(MainActivity.this, Dashboard.class));
                    //startActivity(new Intent(MainActivity.this, MessagingHomeActivity.class));
                }
            }
        }, 3000);
    }
}