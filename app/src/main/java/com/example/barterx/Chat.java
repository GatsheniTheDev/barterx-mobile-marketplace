package com.example.barterx;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.barterx.databinding.ActivityChatBinding;

public class Chat extends Drawer_Base {

    ActivityChatBinding activityChatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChatBinding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(activityChatBinding.getRoot());
        allocateActivityTitle("MESSAGES");
    }
}