package com.example.aroundapplcation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.SnoopersContract;
import com.example.aroundapplcation.model.BusinessCard;

import java.util.List;

public class SnoopersAdapter extends RecyclerView.Adapter<SnoopersAdapter.SnoopersAdapterViewHolder> {

    private final List<BusinessCard> snoopers;
    private final SnoopersContract.Presenter presenter;

    public SnoopersAdapter(final List<BusinessCard> snoopers, final SnoopersContract.Presenter presenter) {
        this.snoopers = snoopers;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public SnoopersAdapter.SnoopersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View itemView = layoutInflater.inflate(R.layout.item_business_cards, parent, false);

        return new SnoopersAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SnoopersAdapter.SnoopersAdapterViewHolder holder, int position) {
        final BusinessCard businessCard = snoopers.get(position);

        final TextView nameTextView = holder.nameTextView;
        nameTextView.setText(businessCard.getName());

        final TextView surnameTextView = holder.surnameTextView;
        surnameTextView.setText(businessCard.getSurname());

        final TextView phoneTextView = holder.phoneTextView;
        phoneTextView.setText(businessCard.getPhone());
    }

    @Override
    public int getItemCount() {
        return snoopers.size();
    }

    class SnoopersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private TextView surnameTextView;
        private TextView phoneTextView;

        SnoopersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            surnameTextView = itemView.findViewById(R.id.tv_surname);
            phoneTextView = itemView.findViewById(R.id.tv_phone);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                presenter.loadBusinessCardScreen(position);
            }
        }
    }
}
