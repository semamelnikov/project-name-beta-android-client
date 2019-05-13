package com.example.aroundapplcation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.aroundapplcation.R;
import com.example.aroundapplcation.contracts.SnoopersContract;
import com.example.aroundapplcation.model.BusinessCard;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class SnoopersAdapter extends RecyclerView.Adapter<SnoopersAdapter.SnoopersAdapterViewHolder> {

    private final List<BusinessCard> snoopers;
    private final SnoopersContract.Presenter presenter;
    private final Context context;

    public SnoopersAdapter(final List<BusinessCard> snoopers, final SnoopersContract.Presenter presenter,
                           final Context context) {
        this.snoopers = snoopers;
        this.presenter = presenter;
        this.context = context;
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

        final ImageView iconImageView = holder.businessCardIconImageView;
        final String iconUri = businessCard.getIconUri();
        if (iconUri != null && !"".equals(iconUri)) {
            Glide.with(context)
                    .load(iconUri)
                    .placeholder(R.drawable.person_default)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(300, 300)
                    .into(iconImageView);
        } else {
            iconImageView.setImageResource(R.drawable.person_default);
        }
    }

    @Override
    public int getItemCount() {
        return snoopers.size();
    }

    class SnoopersAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private TextView surnameTextView;
        private CircularImageView businessCardIconImageView;

        SnoopersAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            surnameTextView = itemView.findViewById(R.id.tv_surname);
            businessCardIconImageView = itemView.findViewById(R.id.business_card_icon);

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
