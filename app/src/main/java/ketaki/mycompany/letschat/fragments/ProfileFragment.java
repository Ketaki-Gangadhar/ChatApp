package ketaki.mycompany.letschat.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ketaki.mycompany.letschat.MessageActivityy;
import ketaki.mycompany.letschat.Model.MyUsers;
import ketaki.mycompany.letschat.R;


public class ProfileFragment extends Fragment {

    TextView userName;
    ImageView profileImage;

    DatabaseReference reference;
    FirebaseUser fUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view = inflater.inflate(R.layout.fragment_profile,container,false);

      userName = view.findViewById(R.id.profile_Username_User);
      profileImage = view.findViewById(R.id.profile_Image_User);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("MyUsers")
        .child(fUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                MyUsers user = snapshot.getValue(MyUsers.class);
                userName.setText(user.getUsername());
                if(user.getImageURL().equals("default"))
                {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                }
                else
                {
                    Glide.with(getContext()).load(user.getImageURL()).into(profileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}