package com.example.chatapp.presenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.chatapp.LoginActivity;
import com.example.chatapp.MainActivity;
import com.example.chatapp.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PresenterAuth {

    private FirebaseAuth mAuth;
    private boolean result_reg;
    private Authorizations view;


    public PresenterAuth(Authorizations view) {
        mAuth = FirebaseAuth.getInstance();
        this.view = view;
    }

    public void registerNewUser(String email, String passw) {
        mAuth.createUserWithEmailAndPassword(email, passw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean res;
                        if (task.isSuccessful()) {
                            Log.d("MyBase", "createUserWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            //startActivity(intent);
                            res = true;
                        } else {
                            Log.w("MyBase", "createUserWithEmail:failure", task.getException());
                            //Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            res = false;
                        }
                        view.showActivity(res);
                    }
                });
    }

    public void loginUser(String email, String passw) {
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        boolean res;
                        if (task.isSuccessful()) {
                            Log.d("MyBase", "signInWithEmail:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            //startActivity(intent);
                            res = true;
                        } else {
                            Log.w("MyBase", "signInWithEmail:failure", task.getException());
                            //  Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            res = false;
                        }
                        view.showActivity(res);
                    }
                });
    }

    public boolean getCurUser() {
        result_reg = false;
        if (mAuth.getCurrentUser() != null) {
            result_reg = true;
        }
        return result_reg;
    }

    public void signOut() {
        mAuth.signOut();
    }

}
