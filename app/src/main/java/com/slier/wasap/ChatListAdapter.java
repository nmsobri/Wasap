package com.slier.wasap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private final DatabaseReference databaseReference;
    private final String username;
    private ArrayList<Message> messages = new ArrayList<>();

    private static class ViewHolder {
        private TextView username;
        private TextView message;
    }

    private final Context activity;

    public ChatListAdapter(Activity activity, DatabaseReference databaseReference, String username) {
        this.activity = activity;
        this.databaseReference = databaseReference;
        this.username = username;

        this.databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatListAdapter.this.messages.add(dataSnapshot.getValue(Message.class));
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public int getCount() {
        return this.messages.size();
    }

    @Override
    public Message getItem(int position) {
        return this.messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.username = convertView.findViewById(R.id.username);
            viewHolder.message = convertView.findViewById(R.id.message);

            convertView.setTag(viewHolder);
        }

        Message message = this.getItem(position);
        viewHolder = (ViewHolder) convertView.getTag();

        if (this.username.equals(message.getUsername())) {
            convertView.setBackgroundResource(R.drawable.incoming);
            convertView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        } else {
            convertView.setBackgroundResource(R.drawable.outgoing);
            convertView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }

        viewHolder.username.setText(message.getUsername());
        viewHolder.message.setText(message.getMessage());

        return convertView;
    }
}
