package com.example.chatapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.pojo.Message;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapters extends RecyclerView.Adapter<MessageAdapters.MessageViewHolder> {


    private List<Message> messages;



    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public MessageAdapters() {
        this.messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewAuth.setText(message.getAuthor());
        holder.textViewMess.setText(message.getMessage());


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuth;
        private TextView textViewMess;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuth = itemView.findViewById(R.id.textViewAuthor);
            textViewMess = itemView.findViewById(R.id.textViewMess);
        }
    }
}
