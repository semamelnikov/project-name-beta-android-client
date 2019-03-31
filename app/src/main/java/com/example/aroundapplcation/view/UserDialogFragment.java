package com.example.aroundapplcation.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.aroundapplcation.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class UserDialogFragment extends DialogFragment {

    public static UserDialogFragment newInstance(String name, String surname, String phone) {
        UserDialogFragment dialogFragment = new UserDialogFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("surname", surname);
        args.putString("phone", phone);
        dialogFragment.setArguments(args);

        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_user, null);
        builder.setView(view)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        TextView name = view.findViewById(R.id.tv_user_name);
        name.setText(getArguments().getString("name", "Unknown"));
        TextView surname = view.findViewById(R.id.tv_user_surname);
        surname.setText(getArguments().getString("surname", "Unknown"));
        TextView phone = view.findViewById(R.id.tv_user_phone);
        phone.setText(getArguments().getString("phone", "Unknown"));
        return builder.create();
    }
}
