package com.example.chatapp.presenter;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.chatapp.MainActivity;
import com.example.chatapp.RegisterActivity;
import com.example.chatapp.pojo.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;

public class Presenter {

    private List<Message> messages;

    private FirebaseFirestore db;


    private String DB_NAME = "messages";





    public List<Message> getMessages() {
        return messages;
    }

    private MessageListView view;

    public Presenter(MessageListView view) {
        this.messages = messages;
        this.view = view;
        messages = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection(DB_NAME).orderBy("date").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    messages.clear();
                    messages = value.toObjects(Message.class);
                    view.showData(messages);
                }
            }
        });
    }


    public void sendMessage(String auth, String mess, long date) {
        db.collection(DB_NAME)
                .add(new Message(auth, mess,date))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        view.completeLoad("DocumentSnapshot added with ID: " + documentReference.getId());
                        Log.i("MyBase", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.showError("Error adding document" + e.toString());
                        Log.i("MyBase","Error adding document" + e.toString());
                    }
                });

    }

}
