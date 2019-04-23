package com.example.aroundapplcation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.BusinessCardsContract;
import com.example.aroundapplcation.model.AdvertiserBusinessCard;
import com.example.aroundapplcation.model.BusinessCard;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusinessCardsAdapter extends RecyclerView.Adapter<BusinessCardsAdapter.ViewHolder> {

    private final List<AdvertiserBusinessCard> advertiserBusinessCards;
    private final BusinessCardsContract.Presenter presenter;

    public BusinessCardsAdapter(final List<AdvertiserBusinessCard> advertiserBusinessCards,
                                final BusinessCardsContract.Presenter presenter) {
        this.advertiserBusinessCards = advertiserBusinessCards;
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View itemView = layoutInflater.inflate(R.layout.item_business_cards, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final BusinessCard businessCard = advertiserBusinessCards.get(position).getBusinessCard();

        final TextView nameTextView = holder.nameTextView;
        nameTextView.setText(businessCard.getName());

        final TextView surnameTextView = holder.surnameTextView;
        surnameTextView.setText(businessCard.getSurname());

        final TextView phoneTextView = holder.phoneTextView;
        phoneTextView.setText(businessCard.getPhone());
    }

    @Override
    public int getItemCount() {
        return advertiserBusinessCards.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private TextView surnameTextView;
        private TextView phoneTextView;

        ViewHolder(@NonNull View itemView) {
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
