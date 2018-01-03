package com.minosai.skindoc.chat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.minosai.skindoc.R;
import com.minosai.skindoc.chat.adapter.ChatAdapter;
import com.minosai.skindoc.chat.data.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String USER_NODE = "user_node";
    private static final String TAG  = "ChatActivity";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;

    List<Message> messages = new ArrayList<>();
    private String chatNode;

    RecyclerView recyclerView;
    ChatAdapter chatAdapter;

    TextView chatTextView;
    ImageView sendImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getSupportActionBar().setTitle(chatNode.split("-")[1]);

        initFirebase();
        initRecycler();
        initViews();
    }

    private void initViews() {
        chatTextView = (TextView) findViewById(R.id.txt_chat_text);
        sendImageView = (ImageView) findViewById(R.id.img_btn_chat_send);

        sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chatTextView.getText().toString().isEmpty()){
                    String key = databaseReference.push().getKey();
                    Message message = new Message(chatTextView.getText().toString(), chatNode.split("-")[0], key);
                    databaseReference.child(key).setValue(message);
                }
            }
        });
    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatAdapter(messages, this);
        recyclerView.setAdapter(chatAdapter);

        attachDatabaseReadListener();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        chatNode = getIntent().getStringExtra(USER_NODE);
        databaseReference = firebaseDatabase.getReference().child("chat").child(chatNode);
    }

    @Override
    protected void onPause() {
        super.onPause();
        detachDatabaseReadListener();
        messages.clear();
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        attachDatabaseReadListener();
    }

    private void detachDatabaseReadListener(){
        if(childEventListener!=null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    private void attachDatabaseReadListener(){
        if(childEventListener==null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(message);
                    chatAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            databaseReference.addChildEventListener(childEventListener);
        }
    }
}
