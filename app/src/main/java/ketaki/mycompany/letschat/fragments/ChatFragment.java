package ketaki.mycompany.letschat.fragments;

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

import java.util.ArrayList;
import java.util.List;

import ketaki.mycompany.letschat.Adapters.UserAdapter;
import ketaki.mycompany.letschat.Model.ChatList;
import ketaki.mycompany.letschat.Model.MyUsers;
import ketaki.mycompany.letschat.R;

public class ChatFragment extends Fragment {

    private UserAdapter userAdapter;
    private List<MyUsers> mUsers;

    FirebaseUser fUser;
    DatabaseReference reference;
    RecyclerView recyclerView;
    private List<ChatList> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat,
                container,
                false);

        recyclerView = view.findViewById(R.id.Recycler_View_chat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                userList.clear();

                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    ChatList chatList = snapshot.getValue(ChatList.class);
                    userList.add(chatList);
                }
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }

    private void chatList()
    {
        //Get all recent chats
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mUsers.clear();

                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    MyUsers user = snapshot.getValue(MyUsers.class);
                    for(ChatList chatList : userList)
                    {
                        if(user.getId().equals(chatList.getId()))
                        {
                            mUsers.add(user);
                        }
                    }
                }

                userAdapter = new UserAdapter(getContext(),mUsers);
                recyclerView.setAdapter(userAdapter);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}