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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
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

        initSocialMediaIcons(businessCard, holder);

        final ImageView iconImageView = holder.businessCardIconImageView;
        final String iconUri = businessCard.getIconUri();
        if (iconUri != null && !"".equals(iconUri)) {
            Glide.with(holder.itemView.getContext())
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
        return favorites.size();
    }

    private void initSocialMediaIcons(final BusinessCard businessCard, final FavoritesAdapterViewHolder holder) {
        final List<ImageView> mediaIcons = holder.mediaImageViews;
        if (businessCard.getVkId() != null && !businessCard.getVkId().isEmpty()) {
            mediaIcons.get(0).setImageResource(R.drawable.ic_bw_vk);
        }
        if (businessCard.getFacebookId() != null && !businessCard.getFacebookId().isEmpty()) {
            mediaIcons.get(1).setImageResource(R.drawable.ic_bw_facebook);
        }
        if (businessCard.getInstagramId() != null && !businessCard.getInstagramId().isEmpty()) {
            mediaIcons.get(2).setImageResource(R.drawable.ic_bw_instagram);
        }
        if (businessCard.getTwitterId() != null && !businessCard.getTwitterId().isEmpty()) {
            mediaIcons.get(3).setImageResource(R.drawable.ic_bw_twitter);
        }
    }

    class FavoritesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final static int MEDIA_VIEW_COUNT = 4;

        private TextView nameTextView;
        private TextView surnameTextView;
        private CircularImageView businessCardIconImageView;
        private List<ImageView> mediaImageViews;

        FavoritesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            mediaImageViews = new ArrayList<>();
            nameTextView = itemView.findViewById(R.id.tv_name);
            surnameTextView = itemView.findViewById(R.id.tv_surname);
            businessCardIconImageView = itemView.findViewById(R.id.business_card_icon);

            for (int i = 0; i < MEDIA_VIEW_COUNT; i++) {
                final String currentIconId = "iv_media_icon_" + i;
                final int iconId = itemView.getResources().getIdentifier(currentIconId, "id", context.getPackageName());
                final ImageView imageView = itemView.findViewById(iconId);
                mediaImageViews.add(imageView);
            }

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
