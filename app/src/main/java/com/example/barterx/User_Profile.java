package com.example.barterx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.barterx.databinding.ActivityUserProfileBinding;

public class User_Profile extends Drawer_Base {

    ActivityUserProfileBinding activityUserProfileBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserProfileBinding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(activityUserProfileBinding.getRoot());
        allocateActivityTitle("PROFILE");
    }
}