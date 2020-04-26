package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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



public class userProfile extends AppCompatActivity  {

    private DatabaseReference databaseReference;
    FirebaseUser firebaseAuth;
    FirebaseStorage storageReference;
   private RecyclerView mRecyclerView;
   private homeAdapter mAdapter;
   private RecyclerView.LayoutManager mLayoutManager;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final ArrayList<HomeView> homeViews=new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance();
        mRecyclerView=findViewById(R.id.recylerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter=new homeAdapter();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference fdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Query query = FirebaseDatabase.getInstance().getReference().child("Users");
        fdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        str = snapshot.child("username").getValue().toString();
                        homeViews.add(new HomeView("gs://instapic-c4f83.appspot.com/"+str+"/profile",str));
                                    mAdapter = new homeAdapter(userProfile.this,homeViews);
                                    mRecyclerView.setAdapter(mAdapter);
                                    mAdapter.setOnItemClickListener(new homeAdapter.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(String name) {
                                            Intent intent=new Intent(userProfile.this,Main2Activity.class);
                                            intent.putExtra("key",name);
                                            startActivity(intent);

                                        }
                                    });

                                }
                            }

                        }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//
//        startActivity(new Intent(this,wall_feed.class));
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                Toast.makeText(this, "You are now logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,MainActivity.class));
                  FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(this,Main2Activity.class));

                return true;
            case R.id.profile:
                startActivity(new Intent(this,myProfile.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
