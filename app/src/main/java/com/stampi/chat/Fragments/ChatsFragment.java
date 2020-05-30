package com.stampi.chat.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.stampi.chat.R;
import com.stampi.chat.adapter.UserAdapter;
import com.stampi.chat.model.Chat;
import com.stampi.chat.model.User;

import java.util.ArrayList;
import java.util.List;


public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> list;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> listUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = v.findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        listUser = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listUser.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getSender().equals(fuser.getUid())) {
                        listUser.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(fuser.getUid())) {
                        listUser.add(chat.getSender());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return v;
    }


    private void readChats() {
        list = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            list.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                User user = snapshot.getValue(User.class);
                // affichage des user du chat
                for (String id : listUser) {
                    if (user.getId().equals(id)) {
                        if (list.size() != 0) {
                            List<User> users = new ArrayList<>(list);
                            for (User u : users) {
                                User user1 = u;
//                                 for(int i=0; i<list.size();i++){
                                if (!user.getId().equals(user1.getId())) {
                                    list.add(user);
                                }
                            }
                        } else {
                            list.add(user);
                        }
                    }
                }
            }
            adapter = new UserAdapter(getContext(), list, true);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
}
}
