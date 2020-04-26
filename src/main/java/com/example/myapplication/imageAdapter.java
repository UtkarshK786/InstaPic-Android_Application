package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.graphics.BitmapFactory.decodeFile;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.imageViewHolder> {

    private Context mContext;
    private List<String> mUsers;
    FirebaseStorage firebaseStorage;

    public imageAdapter(Context context,List<String> strings){
        mContext=context;
        mUsers=strings;
    }
    @NonNull
    @Override
    public imageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new imageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull imageViewHolder holder, int position) {
        String uploadCurrent = mUsers.get(position);
        Log.i("see", uploadCurrent);
        firebaseStorage = FirebaseStorage.getInstance();
        setImage(holder, uploadCurrent);
//        Bitmap bitms=StringToBitMap(uploadCurrent);
//        Uri uri=getImageUri(mContext,bitms);
//            Picasso.get().load.placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

    }
    public void setImage(final imageViewHolder holder, String uploadCurrent){
        StorageReference storageReference = firebaseStorage.getReferenceFromUrl(uploadCurrent).child(".jpg");
        try {
            final File file = File.createTempFile("image", "jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = decodeFile(file.getAbsolutePath());
                    holder.imageView.setImageBitmap(bitmap);
//            Picasso.get().load(bitmap).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    holder.imageView.setImageResource(R.drawable.profile);
                }
            });
//           holder.imageView.setImageBitmap(bitms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    public Bitmap StringToBitMap(String encodedString){
//        try{
//            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        }
//        catch(Exception e){
//            e.getMessage();
//            return null;
//        }
//    }
//
//    private Uri getImageUri(Context context, Bitmap inImage) {
//        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
//        return Uri.parse(path);
//    }
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class imageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public imageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
        }
    }
}
