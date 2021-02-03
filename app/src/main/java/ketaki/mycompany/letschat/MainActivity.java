package ketaki.mycompany.letschat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {
    private Button login_button;
    private EditText lEmail , lPassword;
    private TextView lTextView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_button = findViewById(R.id.loginButton);
        lEmail= findViewById(R.id.loginEmailId);
        lPassword = findViewById(R.id.loginPassword);
        lTextView = findViewById(R.id.loginTextView);
        mAuth = FirebaseAuth.getInstance();
        lTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisterActivity();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();

                if(email.isEmpty()==true)
                {
                    lEmail.setError("Email is required !");
                    return;
                }

                if(password.isEmpty()==true)
                {
                    lPassword.setError("Password is required !");
                    return;
                }
                if(password.length()<6)
                {
                    lPassword.setError("Password must have 6 or more characters");
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "You have Logged In successfully!", Toast.LENGTH_SHORT).show();
                            startHomeActivity();
                        }
                        else
                        {
                            String error = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Failed to LogIn :" +error, Toast.LENGTH_SHORT).show();
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

    private void startRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
