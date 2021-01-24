package com.example.roofkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class prehomescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_home_screen);

        // Les actions que l'on veut faire à la création
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // Démarrer une page
                Intent intent = new Intent(getApplicationContext(), homescreen.class);
                // le "homescreen" est la classe qui sera lue après celle-ci !!!!
                startActivity(intent);
                finish();
            }
        };
        // On lance ces actions après un certain délais
        new Handler().postDelayed(runnable, 2000);
    }
}
