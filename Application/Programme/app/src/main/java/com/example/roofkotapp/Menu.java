package com.example.roofkotapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.Jeux.JeuxScreen;
import com.example.roofkotapp.Parties.PartieScreen;
import com.example.roofkotapp.Règles.ReglesScreen;
import com.example.roofkotapp.utils.InformationScreen;

public class Menu extends AppCompatActivity {

    private Button joueurs;
    private Button regles;
    private Button jeux;
    private Button partie;
    private Button information;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.joueurs = findViewById(R.id.boutton_menu_joueurs);
        this.joueurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_JoueurScreen();
            }
        });

        this.regles = findViewById(R.id.boutton_menu_regles);
        this.regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_ReglesScreen();
            }
        });

        this.jeux = findViewById(R.id.boutton_menu_jeux);
        this.jeux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_JeuxScreen();
            }
        });

        this.partie = findViewById(R.id.boutton_menu_partie);
        this.partie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_PartieScreen();
            }
        });

        this.information = findViewById(R.id.boutton_menu_information);
        this.information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open_InformationScreen();
            }
        });

    }

    public void Open_JoueurScreen(){
        Intent intent = new Intent(this, JoueurScreen.class);
        startActivity(intent);
    }

    public void Open_ReglesScreen(){
        Intent intent = new Intent(this, ReglesScreen.class);
        startActivity(intent);
    }

    public void Open_JeuxScreen(){
        Intent intent = new Intent(this, JeuxScreen.class);
        startActivity(intent);
    }

    public void Open_PartieScreen(){
        Intent intent = new Intent(this, PartieScreen.class);
        startActivity(intent);
    }

    public void Open_InformationScreen(){
        Intent intent = new Intent(this, InformationScreen.class);
        startActivity(intent);
    }
}
