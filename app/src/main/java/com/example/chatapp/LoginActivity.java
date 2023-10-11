package com.example.chatapp;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {


    private EditText editTextEmail;
    private EditText editTextPass;
    private TextView textViewReg;
    private FirebaseAuth mAuth;

    private ImageView imageView;


    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    public void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == -1) {
            // Successfully signed in
            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
            String info = getInfoUser();
            //Toast.makeText(this, info, Toast.LENGTH_LONG).show();
            showMess(info);
            Log.i("MyBase", info);
            resSignInOk();
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            if (response != null) {
                showError("createUserWithEmail:failure" + response.getError().toString());
                Log.w("MyBase", "createUserWithEmail:failure", response.getError());
            } else {
                showError("Error");
            }
        }
    }

    public void signIn() {


        List<AuthUI.IdpConfig> providers = Arrays.asList(
                //  new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    public void showMess(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_LONG).show();
    }

    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    public String getInfoUser() {
        String result = "user is null!";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            result = user.getEmail();
            if (result != null && !result.isEmpty()) {
                return result;
            }
        }
        return result;
    }

    private void resSignInOk() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = findViewById(R.id.editTextLgEmail);
        editTextPass = findViewById(R.id.editTextLgPassw);
        mAuth = FirebaseAuth.getInstance();
        textViewReg = findViewById(R.id.textViewReg);
        imageView = findViewById(R.id.imageViewG);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        textViewReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onClickInput(View view) {
        String email = editTextEmail.getText().toString().trim();
        String passw = editTextPass.getText().toString().trim();
        if (!email.isEmpty() && !passw.isEmpty()) {
            loginUser(email, passw);
        }

    }

    public void loginUser(String email, String passw) {
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("MyBase", "signInWithEmail:success");
                            // FirebaseUser user = mAuth.getCurrentUser();
                            resSignInOk();
                            //  user_name = user.getEmail();
                        } else {
                            Log.w("MyBase", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}