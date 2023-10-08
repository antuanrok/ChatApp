package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.presenter.Authorizations;
import com.example.chatapp.presenter.PresenterAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements Authorizations {


    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewReg;

    private PresenterAuth presenterAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextLgEmail);
        editTextPass = findViewById(R.id.editTextLgPassw);
      //  mAuth = FirebaseAuth.getInstance();
        presenterAuth = new PresenterAuth(LoginActivity.this);
        textViewReg = findViewById(R.id.textViewReg);
        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickInput(View view) {
        String email= editTextEmail.getText().toString().trim();
        String passw= editTextPass.getText().toString().trim();
        if (!email.isEmpty() && !passw.isEmpty()) {
            presenterAuth.loginUser(email,passw);

        }

    }

    public void showActivity(boolean res) {
        if (res) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

        }
    }
}