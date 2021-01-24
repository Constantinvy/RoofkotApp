package com.example.roofkotapp.Parties;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.roofkotapp.R;

public class QuitterPartieDialog extends DialogFragment {

    private TextView OK, ANNULER;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);
        this.OK = (TextView) view.findViewById(R.id.dialog_quitter_partie_ok);
        this.ANNULER = (TextView) view.findViewById(R.id.dialog_quitter_partie_annuler);

        this.OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        this.ANNULER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }
}
