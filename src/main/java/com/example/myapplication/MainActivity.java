package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText username,password;
    TextView newuser;
    Button register,login;
    ProgressBar progressBar;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        login=(Button)findViewById(R.id.login);
        findViewById(R.id.newuser).setOnClickListener(this);
        login.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser()!=null){

            startActivity(new Intent(this,userProfile.class));
            finish();
        }
    }

    public void Login(){
        String usrnm=username.getText().toString().trim();
        String pswrd=password.getText().toString().trim();

    if(usrnm.isEmpty()){
        username.setError("Email required");
        username.requestFocus();
        return;
    }
    if(pswrd.isEmpty()){
        password.setError("Email required");
        password.requestFocus();
        return;
    }

       progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(usrnm,pswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                      Intent intent=new Intent(getApplicationContext(),userProfile.class);
                      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears all the activities on top of it on the stock i.e. registeration and/or login
                      startActivity(intent);
                    finish();
                  }else{
                      Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                  }
            }
        });

}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newuser: startActivity(new Intent(this,Register.class));
                              finish();
                              break;
            case R.id.login:
                          Login();
                          break;
        }
    }
}
