package com.example.barterx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.barterx.databinding.ActivityDashboardBinding;

public class Dashboard extends Drawer_Base {

    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        allocateActivityTitle("HOME");
    }
}