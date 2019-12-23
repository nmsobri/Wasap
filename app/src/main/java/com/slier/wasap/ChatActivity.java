package com.slier.wasap;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ListView chatListView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        this.setupButtonEvent();

        SharedPreferences prefs = ChatActivity.this.getSharedPreferences(LoginActivity.PREF_FILE, Context.MODE_PRIVATE);
        this.username = prefs.getString("username", "anonymous");

        this.database = FirebaseDatabase.getInstance();
        this.databaseReference = this.database.getReference().child("messages");
        this.chatListView = findViewById(R.id.chat_list);
    }

    @Override
    public void onStart() {
        super.onStart();
        ChatListAdapter chatListAdapter = new ChatListAdapter(ChatActivity.this, this.databaseReference, this.username);
        this.chatListView.setAdapter(chatListAdapter);
    }

    private void setupButtonEvent() {
        ImageButton sendButton = findViewById(R.id.button_send);

        sendButton.setOnClickListener(v -> {
            EditText textInput = findViewById(R.id.text_message);
            String userMessage = textInput.getText().toString();

            Message message = new Message(this.username, userMessage);

            this.databaseReference.push().setValue(message).addOnFailureListener(e -> ChatActivity.this.showErrorDialog(e.getMessage()));
            textInput.setText("");
        });
    }

    private void showErrorDialog(String s) {
        new AlertDialog.Builder(ChatActivity.this)
                .setTitle("Opps")
                .setMessage(s)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_info)
                .create()
                .show();
    }
}
