package com.example.aroundapplcation.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aroundapplcation.R;
import com.example.aroundapplcation.model.User;
import com.example.aroundapplcation.services.NetworkService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private ArrayList<String> userIds;
    private Context context;

    public UserListAdapter(ArrayList<String> userIds, Context context) {
        this.userIds = userIds;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Button button = (Button) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_list, parent, false);
        return new UserViewHolder(button);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, int position) {
        holder.button.setText(userIds.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String accessToken =
                        context.getSharedPreferences(context.getString(R.string.aroUnd_preference_file_key), Context.MODE_PRIVATE)
                        .getString("accessToken", "unknown");
                NetworkService.getInstance()
                        .getApiInterface()
                        .getUser(accessToken, Integer.parseInt(((Button) v).getText().toString()))
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                                UserDialogFragment dialogFragment = UserDialogFragment.newInstance(
                                        response.body() != null ? response.body().getName() : "Unknown",
                                        response.body() != null ? response.body().getSurname() : "Unknown",
                                        response.body() != null ? response.body().getPhone() : "Unknown"
                                );
                                dialogFragment.show(((UserListActivity) context).getSupportFragmentManager(), "dialogFragment");
                            }

                            @Override
                            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                                Toast.makeText(context, "Network error...", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return userIds.size();
    }
}
