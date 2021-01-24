package com.example.roofkotapp.utils;

import android.os.Bundle;
import android.os.TestLooperManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roofkotapp.R;

public class InformationScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        TextView texte = findViewById(R.id.information_texte);
        texte.setText(R.string.description);
    }
}
