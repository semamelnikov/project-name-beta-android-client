package com.example.aroundapplcation.view;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.aroundapplcation.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public Button button;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.btn_item_user_list);
    }
}
