package com.minosai.skindoc.chat.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minosai.skindoc.R;
import com.minosai.skindoc.chat.data.Message;
import com.minosai.skindoc.user.utils.UserDataStore;

import java.util.List;

/**
 * Created by minos.ai on 30/12/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<Message> messages;
    Context context;

    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatAdapter.ViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        String currentUser = UserDataStore.getInstance().getUser(context).getUser();

        if(currentMessage.getSender().equals(currentUser)) {
            holder.msgRight.setText(currentMessage.getMessage());
            holder.cardRight.setVisibility(View.VISIBLE);
            holder.cardLeft.setVisibility(View.GONE);
        } else {
            holder.msgLeft.setText(currentMessage.getMessage());
            holder.cardLeft.setVisibility(View.VISIBLE);
            holder.cardRight.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardLeft, cardRight;
        TextView msgLeft, msgRight, timeLeft, timeRight, date;

        public ViewHolder(View itemView) {
            super(itemView);

            cardLeft = (CardView) itemView.findViewById(R.id.card_chat_left);
            cardRight = (CardView) itemView.findViewById(R.id.card_chat_right);

            msgLeft = (TextView) itemView.findViewById(R.id.txt_chat_left_msg);
            msgRight = (TextView) itemView.findViewById(R.id.txt_chat_right_msg);
            timeLeft = (TextView) itemView.findViewById(R.id.txt_chat_left_time);
            timeRight = (TextView) itemView.findViewById(R.id.txt_chat_right_time);

            date = (TextView) itemView.findViewById(R.id.txt_chatitem_date);
        }
    }
}
