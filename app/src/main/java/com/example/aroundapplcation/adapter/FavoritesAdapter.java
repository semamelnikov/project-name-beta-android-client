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
import com.example.aroundapplcation.contracts.FavoritesContract;
import com.example.aroundapplcation.model.BusinessCard;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesAdapterViewHolder> {
    private final List<BusinessCard> favorites;
    private final FavoritesContract.Presenter presenter;
    private final Context context;

    public FavoritesAdapter(final List<BusinessCard> favorites, final FavoritesContract.Presenter presenter,
                            final Context context) {
        this.favorites = favorites;
        this.presenter = presenter;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesAdapter.FavoritesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater layoutInflater = LayoutInflater.from(context);

        final View itemView = layoutInflater.inflate(R.layout.item_business_cards, parent, false);

        return new FavoritesAdapter.FavoritesAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesAdapter.FavoritesAdapterViewHolder holder, int position) {
        final BusinessCard businessCard = favorites.get(position);

        final TextView nameTextView = holder.nameTextView;
        nameTextView.setText(businessCard.getName());

        final TextView surnameTextView = holder.surnameTextView;
        surnameTextView.setText(businessCard.getSurname());

        final TextView phoneTextView = holder.phoneTextView;
        phoneTextView.setText(businessCard.getPhone());

        final ImageView iconImageView = holder.businessCardIconImageView;
        final String iconUri = businessCard.getIconUri();
        if (iconUri != null && !"".equals(iconUri))
            Glide.with(context)
                    .load(iconUri)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(300, 300)
                    .into(iconImageView);
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameTextView;
        private TextView surnameTextView;
        private TextView phoneTextView;
        private ImageView businessCardIconImageView;

        FavoritesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            surnameTextView = itemView.findViewById(R.id.tv_surname);
            phoneTextView = itemView.findViewById(R.id.tv_phone);
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
