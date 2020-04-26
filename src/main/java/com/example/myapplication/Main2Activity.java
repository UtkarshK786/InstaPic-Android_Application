package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {


        private RecyclerView mRecyclerView;
    private imageAdapter imageAdapter;
    private DatabaseReference databaseReference;
    FirebaseUser firebaseAuth;
    FirebaseStorage storageReference;
   String name;
        private List<String> mUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_feed);

        mRecyclerView=findViewById(R.id.recylerView);
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance();
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUsers=new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference fdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = FirebaseDatabase.getInstance().getReference().child("Users");
        Intent intent=getIntent();
        name=intent.getStringExtra("key");
        fdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String str = "";
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        str = snapshot.child("username").getValue().toString();
                        Log.i("sn", str);
                        if (str.equals(name)) {
                            Log.i("user", snapshot.toString());
                            Log.i("Key", snapshot.getKey());
                            String key = snapshot.getKey();
//                                                                     fdatabase.child(key).child("pics").child(String.valueOf(System.currentTimeMillis())).setValue(storageReference.toString());
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                if(snapshot1.getKey().equals("pics")){
                                    for(DataSnapshot snapshot2:snapshot1.getChildren()){
                                        Log.i("Url",snapshot2.getValue().toString());
                                        mUsers.add(snapshot2.getValue().toString());
                                    }
                        imageAdapter = new imageAdapter(Main2Activity.this, mUsers);
                        mRecyclerView.setAdapter(imageAdapter);
                                    break;
                                }                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Main2Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
