package ketaki.mycompany.letschat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ketaki.mycompany.letschat.Adapters.MessageAdapter;
import ketaki.mycompany.letschat.Model.Chat;
import ketaki.mycompany.letschat.Model.MyUsers;

public class MessageActivityy extends AppCompatActivity {

    TextView username;
    ImageView imageView;

    RecyclerView recyclerView;
    EditText msg_editText;
    ImageButton sendButton;
    String userId;
    FirebaseUser fUser;
    DatabaseReference reference;
    Intent intent;

    MessageAdapter messageAdapter;
    List<Chat> mChat;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityy_message);

        imageView = findViewById(R.id.user_imageView);
        username = findViewById(R.id.user_name);

        sendButton= findViewById(R.id.btnSend);
        msg_editText = findViewById(R.id.textSend);

        recyclerView = findViewById(R.id.recyclerView_msg);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        intent = getIntent();
         userId = intent.getStringExtra("userId");
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                MyUsers user = datasnapshot.getValue(MyUsers.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default"))
                {
                    imageView.setImageResource(R.drawable.person);
                }
                else
                {
                    Glide.with(MessageActivityy.this).load(user.getImageURL()).into(imageView);
                }
                readMessage(fUser.getUid(),userId,user.getImageURL());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = msg_editText.getText().toString();

                if(msg=="")
                {
                    Toast.makeText(MessageActivityy.this, "Empty msg can not be sent", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    sendMessage(fUser.getUid(),userId, msg);
                }

                msg_editText.setText("");

            }
        });

    }

    private  void sendMessage(String sender, String receiver, String msg)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object>hashMap = new HashMap<>();

        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",msg);

        reference.child("Chats").push().setValue(hashMap);


        //Adding user to chat fragment

        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fUser.getUid())
                .child(userId);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    chatRef.child("id").setValue(userId);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void readMessage (String myid, String userid, String imageUrl)
    {
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren())
                {
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(myid) && chat.getSender().equals(userid)
                    || chat.getReceiver().equals(userid) && chat.getSender().equals(myid))
                    {
                      mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivityy.this, mChat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}