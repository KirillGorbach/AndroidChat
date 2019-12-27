package com.example.chatprog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import android.text.format.DateFormat;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {

    Logger log = Logger.getLogger(MainActivity.class.getName());

    public static int SIGN_IN_CODE = 1;
    private RelativeLayout activity_main;
    private FirebaseListAdapter<Message> adapter;
    private Query query;
    private Button sendBtn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE){
            if(resultCode == RESULT_OK) {
                Snackbar.make(activity_main, "You are authorized!", Snackbar.LENGTH_SHORT).show();
                displayAllMessages();
            }else{
                Snackbar.make(activity_main, "You are not authorized!", Snackbar.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity_main = findViewById(R.id.activity_main);
        sendBtn = findViewById(R.id.send_button);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textField = findViewById(R.id.messageField);
                if(textField.getText().toString() == ""){return;}
                FirebaseDatabase.getInstance().getReference().push().setValue(
                        new Message(
                                FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                                textField.getText().toString()
                        )
                );
                textField.setText("");
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_CODE);
        }else{
            Snackbar.make(activity_main, "You are authorized!", Snackbar.LENGTH_SHORT).show();
            displayAllMessages();
        }

    }



    private void displayAllMessages() {
        ListView listOfMessages = findViewById(R.id.list_of_messages);
        FirebaseListOptions<Message> options =
                new FirebaseListOptions.Builder<Message>()
                        .setQuery(FirebaseDatabase.getInstance().getReference(), Message.class)
                        .setLayout(R.layout.list_item)
                        .build();

        adapter = new FirebaseListAdapter<Message>(options){
            @Override
            protected void populateView(@NonNull View v,Message model, int position) {
                TextView mess_user, mess_text, mess_time;
                mess_user = v.findViewById(R.id.message_user);
                mess_time = v.findViewById(R.id.message_time);
                mess_text = v.findViewById(R.id.message_text);

                mess_user.setText(model.getUserName());
                mess_text.setText(model.getTextMessage());
                mess_time.setText(DateFormat.format("dd-mm-yyyy HH:mm:ss", model.getMessageTime()));

            }
        };
        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
// @NonNull

//this, Message.class, R.layout.list_item, FirebaseDatabase.getInstance().getReference()
        /*query = FirebaseDatabase.getInstance().getReference().child("chats");
        FirebaseListOptions<Message> options =
                new FirebaseListOptions.Builder<Message>()
                        .setQuery(query, Message.class)
                        .setLayout(R.layout.list_item)
                        .build();*/