package com.example.jobmart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private EditText nm,mob,mail,pass;
    private TextView heading,subHead;
    private ProgressBar progressbar;
    private Button registration,reset;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mAuth = FirebaseAuth.getInstance();

        heading=(TextView)findViewById(R.id.heading);
        subHead=(TextView)findViewById(R.id.subHead);

        nm=(EditText)findViewById(R.id.nm);
        mob=(EditText)findViewById(R.id.mob);
        mail=(EditText)findViewById(R.id.mail);
        pass=(EditText)findViewById(R.id.pass);

        registration=(Button)findViewById(R.id.registration);
        reset=(Button)findViewById(R.id.reset);
        progressbar =(ProgressBar)findViewById(R.id.progressbar);
        registration.setOnClickListener(this);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    nm.setText(String.valueOf(""));
                    mob.setText(String.valueOf(""));
                    mail.setText(String.valueOf(""));
                    pass.setText(String.valueOf(""));


            }
        });

    }


    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.heading:
        startActivity(new Intent(this,MainActivity.class));
        break;
    case R.id.registration:
        registration();
        break;

     }
    }



    private void registration() {

        String name=nm.getText().toString().trim();
        String mobile=mob.getText().toString().trim();
        String email=mail.getText().toString().trim();
        String password=pass.getText().toString().trim();

        if (mobile.isEmpty()) {
            nm.setError("please enter vailid number");
            nm.requestFocus();
            return;

        }

        if(name.isEmpty()) {
            nm.setError("please enter full name");
            nm.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            nm.setError("please enter email");
            nm.requestFocus();
            return;
        }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mail.setError("please enter a vailid email");
                mail.requestFocus();
                return;
            }

            if (password.isEmpty()){
                pass.setError("password is required");
                mail.requestFocus();
                return;
            }
            if (password.length()<6){
                pass.setError("minimum password length 6 characters");
                pass.requestFocus();
                return;
            }


progressbar.setVisibility(View.VISIBLE);

mAuth.createUserWithEmailAndPassword(email,password)
        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    User user=new User(name,mobile,email,password);

                FirebaseDatabase.getInstance().getReference("User")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user). addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(MainActivity2.this, "user has register successfully", Toast.LENGTH_SHORT).show();
                                        progressbar.setVisibility(View.GONE);
                                    }
                                    else
                                        Toast.makeText(MainActivity2.this, "user failed to register try again", Toast.LENGTH_SHORT).show();
                                    progressbar.setVisibility(View.GONE);
                                }
                            });
                }
                else {
                    Toast.makeText(MainActivity2.this, "user failed to register try again", Toast.LENGTH_SHORT).show();
                    progressbar.setVisibility(View.GONE);
                }
            }
        });


    }


}
