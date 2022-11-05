package com.example.barterx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.barterx.databinding.ActivityListingsBinding;

public class Listings extends Drawer_Base {

    ActivityListingsBinding activityListingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityListingsBinding = ActivityListingsBinding.inflate(getLayoutInflater());
        setContentView(activityListingsBinding.getRoot());
        allocateActivityTitle("LISTINGS");
    }
}