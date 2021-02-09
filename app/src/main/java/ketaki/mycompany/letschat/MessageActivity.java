package ketaki.mycompany.letschat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ketaki.mycompany.letschat.Model.Users;

public class MessageActivity extends AppCompatActivity {

    TextView username;
    ImageView imageview;

    FirebaseUser fuser;
    DatabaseReference reference;
    Intent intent;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        imageview = findViewById(R.id.imageProfile);
        username = findViewById(R.id.username_profile);

        intent = getIntent();
        String userId = intent.getStringExtra("userID");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(userId!=null) {
            reference = FirebaseDatabase.getInstance().getReference("MyUsers").child(userId);

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Users user = snapshot.getValue(Users.class);
                    username.setText(user.getName());

                    if (user.getImageURL().equals("default")) {
                        imageview.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Glide.with(MessageActivity.this)
                                .load(user.getImageURL())
                                .into(imageview);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

}