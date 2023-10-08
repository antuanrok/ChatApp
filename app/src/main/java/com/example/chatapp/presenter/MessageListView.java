package com.example.chatapp.presenter;

import android.widget.Toast;

import com.example.chatapp.pojo.Message;

import java.util.List;

public interface MessageListView {

    void showData(List<Message> messages);
    void showError(String mess);

    void completeLoad(String text);
}
