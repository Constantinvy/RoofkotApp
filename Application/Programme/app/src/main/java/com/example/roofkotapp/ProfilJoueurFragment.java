package com.example.roofkotapp;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JoueurDataBaseHelper;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilJoueurFragment extends Fragment {

    private static final String TAG = "ProfilJoueurFragment";
    private ImageView modifier;
    private Joueur mjoueur;
    private TextView prenom;
    private TextView nom;
    private TextView categorie;
    private CircleImageView mJoueurImage;

    private androidx.appcompat.widget.Toolbar toolbar;


    // Pour permettre de passer les informations pour éditer le profil
    public interface OnEditJoueurListener{
        public void onEditJoueurSelected(Joueur joueur);
    }

    OnEditJoueurListener mOnEditJoueurListener;

    // Permet d'éviter d'avoir des NullPointerException
    public ProfilJoueurFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joueur_profil, container, false);
        this.modifier = (ImageView) view.findViewById(R.id.joueur_profil_modifier);
        this.toolbar =  view.findViewById(R.id.joueur_toolbar_profil);
        this.mjoueur = getJoueurFromBundle();
        this.prenom = (TextView) view.findViewById(R.id.fragment_joueur_profil_prenom);
        this.nom = (TextView) view.findViewById(R.id.fragment_joueur_edit_profil_nom);
        this.categorie = (TextView) view.findViewById(R.id.fragment_joueur_profil_categorie);
        this.mJoueurImage = (CircleImageView) view.findViewById(R.id.fragment_joueur_profil_image);

        TextView text_toolbar = (TextView) view.findViewById(R.id.joueurs_profil_text);
        text_toolbar.setText(R.string.profil);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        init();

        // Pour modifier le profil
        this.modifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: modify button clicked");
                mOnEditJoueurListener.onEditJoueurSelected(mjoueur);
            }
        });

        return view;
    }


    private void init(){
        prenom.setText(mjoueur.getPrenom());
        nom.setText(mjoueur.getNom());
        categorie.setText(mjoueur.getCat());
        UniversalImageLoader.setImage(mjoueur.getImage(), mJoueurImage, null, "");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.joueur_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.joueur_menu_supprimer:
                JoueurDataBaseHelper dataBaseHelper = new JoueurDataBaseHelper(getActivity());
                Cursor curseur = dataBaseHelper.getJoueurId(mjoueur);

                int id = -1;
                while(curseur.moveToNext()){
                    id = curseur.getInt(0); // Récupère le JOUEUR_ID
                }
                if (id > -1) {   // Signifie qu'on a bien retrouvé le contact
                    if (dataBaseHelper.DeleteJoueur(id)){
                        Toast.makeText(getActivity(), "Le joueur a bien éte supprimé ! :)", Toast.LENGTH_LONG).show();

                        // On veut supprimer les données du joueur qui se trouvent encore dans le bundle !
                        this.getArguments().clear();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Une erreur est survenue ! :(", Toast.LENGTH_LONG).show();
                    }

                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrouve le joueur sélectionné depuis JoueurScreen
    private Joueur getJoueurFromBundle(){
        Log.d(TAG, "getJoueurFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.Joueur));}
        else{return null;}
    }

    // Pour prendre les informations pr éditer le profil
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnEditJoueurListener = (OnEditJoueurListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach : ClassCastException : " + e.getMessage());
        }
    }
}


/* Pour supprimer une activity de la stack: getActivity().getSupportFragmentManager().popBackStack()
 Voir contact fragment widget 2:40 pour les infos avec des backarrows */