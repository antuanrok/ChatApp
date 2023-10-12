package com.example.chatapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chatapp.adapters.MessageAdapters;
import com.example.chatapp.pojo.Message;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMess;
    private MessageAdapters adapter;

    private EditText editTextMess;

    private ImageView imageViewSendMess;
    private ImageView imageViewSendImg;

    private String author;


    private List<Message> messages;

    private FirebaseFirestore db;


    private String DB_NAME = "messages";

    private FirebaseStorage storage;

    private StorageReference storageReference;
    private UploadTask uploadTask;

    private String img_url;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_sign_out) {
            signOut(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    onImgResult(result);
                }
            }
    );

    public static String getLastBitFromUrl(final String url){
        // return url.replaceFirst("[^?]*/(.*?)(?:\\?.*)","$1);" <-- incorrect
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    public void onImgResult(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent tmp = result.getData();
            if (tmp != null) {
                Uri uri = tmp.getData();
                if (uri != null) {
                   String t_url = getLastBitFromUrl(uri.getLastPathSegment());
                    StorageReference referenceToImages = storageReference.child("images/" + t_url);
                    referenceToImages.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Error process get url image", Toast.LENGTH_SHORT).show();
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return referenceToImages.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                Toast.makeText(MainActivity.this, "Get url image successful", Toast.LENGTH_SHORT).show();
                                Log.i("MyBase",downloadUri.toString());
                            } else {
                                Toast.makeText(MainActivity.this, "Error get url image", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        } else {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextMess = findViewById(R.id.editTextMessage);
        imageViewSendMess = findViewById(R.id.imageViewSendMess);
        imageViewSendImg = findViewById(R.id.imageViewAddImg);
        recyclerViewMess = findViewById(R.id.recyclerViewChat);
        adapter = new MessageAdapters();
        recyclerViewMess.setLayoutManager(new LinearLayoutManager(this));
        adapter.setMessages(new ArrayList<>());
        recyclerViewMess.setAdapter(adapter);
        author = "No Name";
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        //StorageReference referenceToImages = storageReference.child("images");

        imageViewSendImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //startActivityForResult(intent, );
                signInLauncher.launch(intent);
            }
        });
        imageViewSendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepMessToSend();
            }
        });
        if (getCurUser()) {
            Toast.makeText(this, "Logged", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            author = user.getEmail();
        } else {
            signOut(this);
        }

        messages = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

    }

    public boolean getCurUser() {
        boolean result_reg = false;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            result_reg = true;
        }
        return result_reg;
    }


    public void signOut(Context ctx) {
        AuthUI.getInstance()
                .signOut(ctx)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            signIn();
                        }
                    }
                });
    }


    public void signIn() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        db.collection(DB_NAME).orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    messages.clear();
                    messages = value.toObjects(Message.class);
                    showData(messages);
                }
            }
        });

    }

    private void prepMessToSend() {
        String mess = editTextMess.getText().toString().trim();
        long date = System.currentTimeMillis();
        if (!mess.isEmpty()) {
            sendMessage(author, mess, date, img_url);
            recyclerViewMess.scrollToPosition(adapter.getItemCount() - 1);
        }
    }


    public void showData(List<Message> messages) {
        adapter.setMessages(messages);
    }


    public void showError(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }


    public void completeLoad(String text) {
        editTextMess.setText("");
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    public void sendMessage(String auth, String mess, long date, String img_url) {
        db.collection(DB_NAME)
                .add(new Message(auth, mess, date, img_url))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        completeLoad("DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.i("MyBase", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showError("Error adding document" + e.toString());
                        Log.i("MyBase", "Error adding document" + e.toString());
                    }
                });

    }
}