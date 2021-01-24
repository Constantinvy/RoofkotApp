package com.example.roofkotapp.Parties;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.AbsListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.roofkotapp.Jeux.ProfilJeuxFragment;
import com.example.roofkotapp.R;
import com.example.roofkotapp.Règles.RegleProfilFragment;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.models.Regle;
import com.example.roofkotapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PartieScreen extends AppCompatActivity implements
    PartieProfilFragment.ChoisirJeuListener,
    PartieViewJeuxFragment.ValiderJeuOnClick,
    PartieProfilFragment.AjouterJoueursListener,
    PartieViewJoueursFragment.ValiderJoueursOnClick,
    PartieProfilFragment.ValiderPartieListener,
    PartieViewFragment.ShowJeuProfilListener,
    PartieViewFragment.ShowRegleJoueurListener{


    private Jeux jeu = null;
    private int difficulte = 0;
    private ArrayList<Joueur> joueurs = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partie);

        init();

        initImageLoader();
    }

    private void init(){
        PartieProfilFragment partieProfil = new PartieProfilFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieProfil);
        //transaction.addToBackStack(getString(R.string.menu));
        transaction.commit();
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(PartieScreen.this, 1);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    public void PutInBundle(Jeux jeu, int diff, ArrayList<Joueur> joueurs, PartieProfilFragment partieProfil){
        partieProfil.getArguments().clear();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.jeux), jeu);
        args.putInt("Difficulte", diff);
        args.putParcelableArrayList(getString(R.string.Joueur), joueurs);
        partieProfil.setArguments(args);
    }

    @Override
    public void ChoisirJeu(int diff) {
        PartieViewJeuxFragment partieViewJeuxFragment = new PartieViewJeuxFragment();
        difficulte = diff;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieViewJeuxFragment);
        transaction.addToBackStack(getString(R.string.Partie_Profil_Fragment));
        transaction.commit();
    }


    @Override
    public void ValiderJeu(Jeux Selected_Jeu) {
        PartieProfilFragment partieProfil = new PartieProfilFragment();
        jeu = Selected_Jeu;
        PutInBundle(jeu, difficulte, joueurs,partieProfil);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieProfil);
        transaction.commit();
    }


    @Override
    public void AjouterJoueurs(int diff) {
        PartieViewJoueursFragment partieViewJoueurs = new PartieViewJoueursFragment();
        difficulte = diff;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieViewJoueurs);
        transaction.addToBackStack(getString(R.string.Partie_Profil_Fragment));
        transaction.commit();
    }

    @Override
    public void ValiderJoueurs(ArrayList<Joueur> joueur_list) {
        PartieProfilFragment partieProfil = new PartieProfilFragment();
        joueurs = joueur_list;
        PutInBundle(jeu, difficulte, joueurs, partieProfil);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieProfil);
        getSupportFragmentManager().popBackStack();
        transaction.commit();
    }

    @Override
    public void ValiderPartie(String difficulte) {
        PartieViewFragment partieView = new PartieViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.jeux), jeu);
        args.putParcelableArrayList(getString(R.string.Joueur), joueurs);
        args.putString("Difficulte", difficulte);
        partieView.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, partieView);
        transaction.commit();

    }

    @Override
    public void ShowJeuProfil(Jeux jeu) {
        ProfilJeuxFragment profilJeu = new ProfilJeuxFragment(1);
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.jeux), jeu);
        profilJeu.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, profilJeu);
        transaction.addToBackStack(getString(R.string.Resumer_Partie));
        transaction.commit();

    }

    @Override
    public void ShowRegleJoueur(Regle regle) {
        RegleProfilFragment regleProfil = new RegleProfilFragment(1);
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.regles), regle);
        regleProfil.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_partie_container, regleProfil);
        transaction.addToBackStack(getString(R.string.Resumer_Partie));
        transaction.commit();
    }
}
