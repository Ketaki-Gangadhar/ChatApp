package ketaki.mycompany.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button register_button;
    private EditText rEmail , rPassword, rName;
    private TextView rTextView;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_button = findViewById(R.id.registerButton);
        rEmail= findViewById(R.id.registerEmailId);
        rPassword = findViewById(R.id.registerPassword);
        rTextView = findViewById(R.id.registerTextView);
        rName = findViewById(R.id.registerName);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        rTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginActivity();
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String name = rName.getText().toString().trim();
                if(name.isEmpty()==true)
                {
                    rName.setError("Name is required !");
                    return;
                }

                if(email.isEmpty()==true)
                {
                    rEmail.setError("Email is required !");
                    return;
                }

                if(password.isEmpty()==true)
                {
                    rPassword.setError("Password is required !");
                    return;
                }
                if(password.length()<6)
                {
                    rPassword.setError("Password must have 6 or more characters");
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "You have registered successfully!", Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();
                            myRef = database.getInstance()
                                    .getReference("MyUsers")
                                    .child(userId);

                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("id",userId);
                            hashMap.put("username",name);
                            hashMap.put("imageURL","default");

                            myRef.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        startHomeActivity();
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterActivity.this, "Registration failed ! try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Failed to register :" +error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}