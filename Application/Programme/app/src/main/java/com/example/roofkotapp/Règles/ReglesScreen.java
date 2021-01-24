package com.example.roofkotapp.Règles;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.roofkotapp.R;
import com.example.roofkotapp.ViewJoueurFragment;
import com.example.roofkotapp.models.Regle;

public class ReglesScreen extends AppCompatActivity implements
    ViewReglesFragment.OnRegleSelectedListener,
    ViewReglesFragment.OnAddRegleListener,
    RegleProfilFragment.OnEditRegleListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regles);

        init();
    }


    // Initialise le premier fragment (règles)
    private void init(){
        ViewReglesFragment fragment = new ViewReglesFragment();

        // Permet de remplacer le contenu du fragment_container par ce fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_regles_container, fragment);
        transaction.commit();

    }

    @Override
    public void OnRegleSelected(Regle regle) {

        RegleProfilFragment fragment = new RegleProfilFragment(0);
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.regles), regle);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_regles_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Regle_Fragment));
        transaction.commit();

    }

    @Override
    public void onAddRegle() {

        AddRegleFragment fragment = new AddRegleFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_regles_container, fragment);
        transaction.addToBackStack(getString(R.string.Add_Regle));
        transaction.commit();
    }

    @Override
    public void onEditRegleSelected(Regle regle) {
        // On crée un nouveau fragment à travers lequel on veut naviguer.
        RegleProfilEditFragment fragment = new RegleProfilEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.regles), regle);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_regles_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Regle_Edit_Fragment));
        transaction.commit();
    }
}
