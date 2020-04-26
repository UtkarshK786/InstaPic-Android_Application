package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeFile;

public class homeAdapter extends RecyclerView.Adapter<homeAdapter.homeAdapterViewHolder> {
 private ArrayList<HomeView> mhomeViews;
 private OnItemClickListener mListener;
//   final private  Context mContext;

    FirebaseStorage firebaseStorage;
public interface OnItemClickListener{
    void onItemClick(String name);
}

public void setOnItemClickListener(OnItemClickListener listener){
         mListener=listener;
    }
    @NonNull
    @Override
    public homeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_home,parent,false);
       homeAdapterViewHolder holder=new homeAdapterViewHolder(view,mListener);
       return holder;

    }

    public homeAdapter(Context context,ArrayList<HomeView> homeViews){
             mhomeViews=homeViews;
//             mContext=context;

    }
    @Override
    public void onBindViewHolder(@NonNull final homeAdapterViewHolder holder, int position) {
            HomeView homeView=mhomeViews.get(position);
            String img=homeView.getImageResource();
            String name=homeView.getText();
            holder.text.setText(name);
//            holder.imageView.setImageResource(R.drawable.common_full_open_on_phone);
        firebaseStorage = FirebaseStorage.getInstance();
            Log.i("Image ka url",img);

//            holder.imageView.setImageResource(homeView.getImageResource().);
        final StorageReference storageReference = firebaseStorage.getReferenceFromUrl("gs://instapic-c4f83.appspot.com/h@gmail.com/profile").child(".jpg");
        try {
            final File file = File.createTempFile("image", "jpg");
            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = decodeFile(file.getAbsolutePath());
//                    holder.imageView.setImageBitmap(bitmap);
//            Picasso.get().load(bitmap).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.imageView);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                    holder.imageView.setImageResource(R.drawable.profile);
                }
            });
//           holder.imageView.setImageBitmap(bitms);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        return mhomeViews.size();
    }

    public static class homeAdapterViewHolder extends RecyclerView.ViewHolder{
        public CircularImageView imageView;
        private TextView text;
        public homeAdapterViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
            text=itemView.findViewById(R.id.textView3);
//            imageView.setImageResource(R.drawable.common_full_open_on_phone);
//            imageView.setImageResource(ContextCompat.getDrawable(mContext,R.drawable.common_full_open_on_phone));
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                        if(listener!=null){
                            listener.onItemClick(text.getText().toString());
                        }
                 }
             });

        }
    }
}
