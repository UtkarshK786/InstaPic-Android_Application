package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
//import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import kotlin.Metadata;

import static android.graphics.BitmapFactory.*;
import static java.lang.String.valueOf;

public class myProfile extends AppCompatActivity implements View.OnClickListener {

    CircularImageView circularImageView;
    Uri uriProfileImage;
    ProgressBar progressBar;
    TextView email;
    String usrnm;
    Users users;
//    metaData mdata;
    DatabaseReference firebaseDatabase;
    FirebaseStorage firebaseStorage,fb;
    FirebaseUser firebaseAuth;
    long name;
    int img;
//    ImageView imageView2;
    ImageView sharing;
    Button buttonshareUpload,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        circularImageView=findViewById(R.id.circularImageView);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        email=(TextView)findViewById(R.id.email);
        circularImageView.setOnClickListener(this);
        firebaseAuth=FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance().getReference("Users");
        users=new Users();
//        imageView2= findViewById(R.id.imageView2);
        firebaseStorage=FirebaseStorage.getInstance();
        usrnm=firebaseAuth.getEmail();
        email.setText(usrnm);
        button2=(Button)findViewById(R.id.button2);
//        mdata=new metaData();
        sharing=(ImageView)findViewById(R.id.sharing);
        buttonshareUpload=(Button)findViewById(R.id.buttonshareUpload);
        buttonshareUpload.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        Picasso.with(context).load(usrnm+"/profile/"+".jpg").into(circularImageView);
       StorageReference storageReference=firebaseStorage.getReferenceFromUrl("gs://instapic-c4f83.appspot.com/"+firebaseAuth.getEmail()+"/profile").child(".jpg");
       try {
           final File file=File.createTempFile("image","jpg");
           storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
               @Override
               public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                   Bitmap bitmap= decodeFile(file.getAbsolutePath());
                   circularImageView.setImageBitmap(bitmap);
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                      circularImageView.setImageResource(R.drawable.profile);
               }
           });
       } catch (IOException e) {
           e.printStackTrace();
       }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.circularImageView: img=1;
                                         showImageChooser();
                                         break;
            case R.id.buttonshareUpload: img=2;
                                         showImageChooser();
                                         break;
            case R.id.button2:  Intent intent=new Intent(myProfile.this,Main2Activity.class);
                               intent.putExtra("key",usrnm);
                               startActivity(intent);
                               break;
        }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uriProfileImage=data.getData();
            Log.i("value of img",String.valueOf(img));
           if(img==1)
            {
                Log.i("ds","coming till activity");
                circularImageView.setImageURI(uriProfileImage);
                uploadImage();
            }
            else if(img==2){
                sharing.setImageURI(uriProfileImage);
                uploadAndShare();
            }
        }
    }

    private String getExtension(Uri uri){
        ContentResolver cr=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }




    private void uploadImage(){
        progressBar.setVisibility(View.VISIBLE);
        circularImageView.setEnabled(false);
        final StorageReference storageReference= FirebaseStorage.getInstance().getReference(usrnm+"/profile/"+".jpg");
        if(uriProfileImage!=null){
            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(myProfile.this, "Profile Successfully updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(myProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        circularImageView.setEnabled(true);
    }
    private void uploadAndShare(){
        name=System.currentTimeMillis();
        circularImageView.setEnabled(false);
        final StorageReference storageReference= FirebaseStorage.getInstance().getReference(usrnm+"/images/"+name+"/.jpg");
        if(uriProfileImage!=null){
            storageReference.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final DatabaseReference fdatabase=FirebaseDatabase.getInstance().getReference().child("Users");
                 Query query=FirebaseDatabase.getInstance().getReference().child("Users");
                    fdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("Yes","We are in");
                            if(dataSnapshot.exists()){
                                  String str="";
                                  for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                                      str=snapshot.child("username").getValue().toString();
                                       Log.i("sn",str);
                                           if(str.equals(firebaseAuth.getEmail())) {
                                               Log.i("user", snapshot.toString());
                                               Log.i("Key", snapshot.getKey());
                                               final String key = snapshot.getKey();
                                               try {
                                                   final File file=File.createTempFile("image","jpg");
                                                   storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                                       @Override
                                                       public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                                         fdatabase.child(key).child("pics").push().setValue("gs://instapic-c4f83.appspot.com/"+usrnm+"/images/"+name);


                                                       }
                                                   }).addOnFailureListener(new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(@NonNull Exception e) {
                                                           circularImageView.setImageResource(R.drawable.profile);
                                                       }
                                                   });
                                               } catch (IOException e) {
                                                   e.printStackTrace();
                                               }                                       break;
                                           }
                                  }
                              }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(myProfile.this, "Image successfully uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(myProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        circularImageView.setEnabled(true);
    }

}
