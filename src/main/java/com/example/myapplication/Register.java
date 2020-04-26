package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText password,email;
    TextView log,err;
    Button register;
    Users users;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    long maxID=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        register = (Button) findViewById(R.id.register);
        err = (TextView) findViewById(R.id.error);
        log = (TextView) findViewById(R.id.log);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Users");
        users=new Users();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        register.setOnClickListener(this);
        log.setOnClickListener(this);

        databaseReference.addValueEventListener(new ValueEventListener() {          //for auto incrementin the users
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxID=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    public void Register(){
        final String eml=email.getText().toString().trim();
        final String pswrd=password.getText().toString().trim();
        if(eml.isEmpty()){
            email.setError("Email required");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(eml).matches()){
            email.setError("Enter a valid email");
            email.requestFocus();
            return;
        }
        else if(pswrd.isEmpty()){
            password.setError("Password required");
            password.requestFocus();
            return;
        }
        else if(pswrd.length()<6){
            password.setError("minimum length of password should be 6");
            password.requestFocus();
            return;
        }

      progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(eml,pswrd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "registration successful", Toast.LENGTH_SHORT).show();
//                    databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
                    users.setPassword(pswrd);
                    users.setUsername(eml);
                    users.setProfilepic("");
                    users.setPics("");
//                    String childs=eml;
//                    databaseReference.push();
//                    Toast.makeText(Register.this, "String value of maxid+1 is"+maxID+1, Toast.LENGTH_SHORT).show();
                    databaseReference.child(String.valueOf(maxID+1)).setValue(users);
                }
                else{
                   // if(task.getException() instanceof FirebaseAuthUserCollisionException)
                   Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
//        Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.log: startActivity(new Intent(this,MainActivity.class));
                          finish();
                           break;
            case R.id.register: Register();
                  break;
        }
    }
}
