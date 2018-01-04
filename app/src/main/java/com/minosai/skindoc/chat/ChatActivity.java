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
import com.minosai.skindoc.user.utils.UserDataStore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String USER_NODE = "user_node";
    private static final String TAG  = "ChatActivity";
    public static final String RECIEVER = "reciever";

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

        chatNode = getIntent().getStringExtra(USER_NODE);

        getSupportActionBar().setTitle(getIntent().getStringExtra(RECIEVER));

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
                    String sender = UserDataStore.getInstance().getUser(getApplicationContext()).getUser();
                    String msg = chatTextView.getText().toString();
                    String time = new SimpleDateFormat("h:mm a").format(new Date());
                    Message message = new Message(msg, sender, time, key);
                    databaseReference.child(key).setValue(message);
                    chatTextView.setText("");
                }
            }
        });
    }

    private void initRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatAdapter(messages, this);
        recyclerView.setAdapter(chatAdapter);

        attachDatabaseReadListener();
    }

    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
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
//                    Message message = dataSnapshot.getValue(Message.class);
                    messages.add(dataSnapshot.getValue(Message.class));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView.smoothScrollToPosition(messages.size() - 1);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    messages.remove(dataSnapshot.getValue(Message.class));
                    chatAdapter.notifyDataSetChanged();
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
