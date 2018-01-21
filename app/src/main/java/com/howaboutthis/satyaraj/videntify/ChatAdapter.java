package com.howaboutthis.satyaraj.videntify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int SELF = 100;
    private List<Message> messageArrayList;

    ChatAdapter(List<Message> messageArrayList) {
        this.messageArrayList=messageArrayList;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // WatBot message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_watson, parent, false);

        }
        return new ViewItem(itemView);
    }

    @Override
    public int getItemViewType(int position) {

        Message message = messageArrayList.get(position);

            if (message.getId() != null && message.getId().equals("1")) {
                return SELF;
            }

        return position;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Message message = messageArrayList.get(position);
        message.setMessage(message.getMessage());
            ((ViewItem) holder).message.setText(message.getMessage());
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

     private class ViewItem extends RecyclerView.ViewHolder {
        TextView message;

         ViewItem(View view) {
            super(view);
            message = view.findViewById(R.id.message);
        }
    }
}