package com.stampi.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stampi.chat.R;
import com.stampi.chat.model.Chat;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGTH = 1;
    private Context context;
    private List<Chat> list;
    private String imageUrl;
    FirebaseUser user;


    public MessageAdapter(Context context, List<Chat> list, String imageUrl) {
        this.context = context;
        this.list = list;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(v);
        } else {
            View v = LayoutInflater.from(context).inflate(R.layout.chat_item_rigth, parent, false);
            return new MessageAdapter.ViewHolder(v);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        final Chat chat = list.get(position);
        holder.show_message.setText(chat.getMessage());
        if (imageUrl.equals("default")) {
            holder.profile_name.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageUrl).into(holder.profile_name);
        }

        if (position == list.size() - 1) {
            if (chat.getIsseen()) {
                holder.txt_seen.setText("lu");
            } else {
                holder.txt_seen.setText("Envoyer");
            }
        } else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_name;
        private TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_name = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(user.getUid())) {
            return MSG_TYPE_RIGTH;
        } else {
            return MSG_TYPE_LEFT;
        }


    }
}
