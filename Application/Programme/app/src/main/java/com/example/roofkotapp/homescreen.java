package com.example.roofkotapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.Parties.PartieScreen;

public class homescreen  extends AppCompatActivity {

    private Button menu;
    private Button new_partie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        this.menu = findViewById(R.id.home_screen_menu);
        this.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_MenuScreen(); // On appelle la méthode qui va permettre de lancer la classe qui gère le menu
            }
        });

        this.new_partie = findViewById(R.id.home_screen_partie);
        this.new_partie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_PartieScreen();
            }
        });
    }

    private void Open_MenuScreen(){
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    private void Open_PartieScreen(){
        Intent intent = new Intent(this, PartieScreen.class);
        startActivity(intent);
    }
}
