package com.example.chatapp.adapters;

import android.content.Context;
import android.preference.PreferenceManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.pojo.Message;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MessageAdapters extends RecyclerView.Adapter<MessageAdapters.MessageViewHolder> {


    private static final int TYPE_MY_MESSAGE = 0;
    private static final int TYPE_MY_OTHER_MESSAGE = 1;
    private Context context;

    private List<Message> messages;


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public MessageAdapters(Context ctx) {

        this.messages = new ArrayList<>();
        this.context = ctx;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int lay = 0;
        if (viewType == TYPE_MY_MESSAGE) {
            lay = R.layout.activity_item_message_my;
        } else {
            lay = R.layout.activity_item_message;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(lay, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String name_author = message.getAuthor();
        if (name_author != null && name_author.equals(PreferenceManager.getDefaultSharedPreferences(context).getString("author", "no name"))) {
            return TYPE_MY_MESSAGE;
        } else {
            return TYPE_MY_OTHER_MESSAGE;
        }
        //return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.textViewAuth.setText(message.getAuthor());
        String textOfMessage = message.getMessage();
        String urlToImage = message.getImg_url();
        if (textOfMessage != null && !textOfMessage.isEmpty()) {
            holder.textViewMess.setText(message.getMessage());
            holder.imageViewImg.setVisibility(View.GONE);
            holder.textViewMess.setVisibility(View.VISIBLE);
        }
        if (urlToImage != null && !urlToImage.isEmpty()) {
            holder.imageViewImg.setVisibility(View.VISIBLE);
            Picasso.get().load(urlToImage).into(holder.imageViewImg);
            holder.textViewMess.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewAuth;
        private TextView textViewMess;

        private ImageView imageViewImg;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAuth = itemView.findViewById(R.id.textViewAuthor);
            textViewMess = itemView.findViewById(R.id.textViewMess);
            imageViewImg = itemView.findViewById(R.id.imageViewImage);
        }
    }
}
