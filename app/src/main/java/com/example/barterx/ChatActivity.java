package com.example.barterx;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.barterx.adapter.ChatAdapter;
import com.example.barterx.adapter.MessagingChatAdapter;
import com.example.barterx.databinding.ActivityChatBinding;
import com.example.barterx.model.Message;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {
    private DatabaseReference databaseReferenceSender, databaseReferenceReceiver;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ChatAdapter chatAdapter;
    private ActivityChatBinding binding;

    private  String senderRoom, receiverRoom;
    private String receiverId;
    private boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        chatAdapter = new ChatAdapter(this);
        binding.chatRecyclerView.setAdapter(chatAdapter);
        binding.chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            check= extras.getBoolean("chatIdCheck");
        }
        receiverId = getIntent().getStringExtra("id");
        if(check){
            receiverId = getIntent().getStringExtra("chatId");
        }
        senderRoom = user.getUid()+receiverId;
        receiverRoom = receiverId+user.getUid();
        databaseReferenceSender = FirebaseDatabase.getInstance().getReference("chats").child(senderRoom);
        databaseReferenceReceiver = FirebaseDatabase.getInstance().getReference("chats").child(receiverRoom);
        binding.floatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        receiveMessages();
    }

    private void sendMessage(){
        String msg = binding.messageBox.getEditText().getText().toString();
        String msgId = UUID.randomUUID().toString();
        String timestamp = new SimpleDateFormat("dd-MM-yy HH:mm a").format(Calendar.getInstance().getTime());
        if(msg.trim().length() >0){
            Message chat = new Message(msgId,user.getUid(),msg,timestamp);
            chatAdapter.add(chat);
            databaseReferenceSender.child(msgId).setValue(chat);
            databaseReferenceReceiver.child(msgId).setValue(chat);
            binding.messageBox.getEditText().setText("");
        }

    }

    private void receiveMessages(){
        databaseReferenceSender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatAdapter.clear();
                for (DataSnapshot snap:snapshot.getChildren()){
                    Message message = snap.getValue(Message.class);
                    chatAdapter.add(message);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}