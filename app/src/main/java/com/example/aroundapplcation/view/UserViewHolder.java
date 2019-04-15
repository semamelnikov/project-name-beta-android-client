package com.example.aroundapplcation.view;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aroundapplcation.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public TextView tvSurname;
    public TextView tvPhone;

    public UserViewHolder(@NonNull View itemView) {
        super(itemView);
        tvName = itemView.findViewById(R.id.tv_disp_name);
        tvSurname = itemView.findViewById(R.id.tv_disp_surname);
        tvPhone = itemView.findViewById(R.id.tv_disp_phone);
    }
}
