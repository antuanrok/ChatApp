package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.adapters.MessageAdapters;
import com.example.chatapp.pojo.Message;
import com.example.chatapp.presenter.Authorizations;
import com.example.chatapp.presenter.MessageListView;
import com.example.chatapp.presenter.Presenter;
import com.example.chatapp.presenter.PresenterAuth;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MessageListView, Authorizations {

    private RecyclerView recyclerViewMess;
    private MessageAdapters adapter;
    private Presenter presenter;

    private EditText editTextMess;

    private ImageView imageViewSendMess;
    private String author;

   // private FirebaseAuth mAuth;

    private PresenterAuth presenterAuth;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_sign_out) {
            //mAuth.signOut();
            presenterAuth.signOut();
            getOut();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMess = findViewById(R.id.editTextMessage);
        imageViewSendMess = findViewById(R.id.imageViewSendMess);
        recyclerViewMess = findViewById(R.id.recyclerViewChat);
        //mAuth = FirebaseAuth.getInstance();
        presenterAuth = new PresenterAuth(this);
        adapter = new MessageAdapters();
        recyclerViewMess.setLayoutManager(new LinearLayoutManager(this));
        presenter = new Presenter(this);
        adapter.setMessages(new ArrayList<>());
        recyclerViewMess.setAdapter(adapter);
        author = "Andrey";
        imageViewSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepMessToSend();
            }
        });
        if (presenterAuth.getCurUser()) {
            Toast.makeText(this, "Logged", Toast.LENGTH_SHORT).show();
        } else {
            getOut();
        }
    }

    private void prepMessToSend() {
        String mess = editTextMess.getText().toString().trim();
        //long date = Calendar.getInstance().getTimeInMillis();
        long date = System.currentTimeMillis();
        if (!mess.isEmpty()) {
            presenter.sendMessage(author, mess, date);
            recyclerViewMess.scrollToPosition(adapter.getItemCount() - 1);
            //  adapter.setMessages(presenter.getMessages());

        }
    }

    @Override
    public void showData(List<Message> messages) {
        adapter.setMessages(messages);
    }

    @Override
    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void completeLoad(String text) {
        editTextMess.setText("");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private void getOut() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void showActivity(boolean res) {

    }
}