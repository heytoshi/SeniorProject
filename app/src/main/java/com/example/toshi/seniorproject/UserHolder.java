package com.example.toshi.seniorproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserHolder extends RecyclerView.ViewHolder {
    View v;

    public UserHolder(View itemView) {
        super(itemView);
        v = itemView;
    }

    public void setNameHelper(String name) {
        TextView userName = v.findViewById(R.id.other_user_name);
        userName.setText(name);
    }
    public void setStatusHelper(String name) {
        TextView userName = v.findViewById(R.id.other_user_status);
        userName.setText(name);
    }
    public void setImageHelper(String name) {
        CircleImageView userName = v.findViewById(R.id.other_user_image);
        Picasso.get().load(name).into(userName);
    }

}