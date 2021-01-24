package com.example.roofkotapp.Parties;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JoueurDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Joueur;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PartieViewJoueursFragment extends Fragment {

    private static final String TAG = "PartieViewJoueursFragme";
    private PartieJoueursListAdapter adapter;
    private ListView joueurslist;
    private androidx.appcompat.widget.Toolbar toolbar;

    public static ArrayList<Joueur> JoueursSelected;
    public static ActionMode actionMode = null;


    public interface ValiderJoueursOnClick{
        public void ValiderJoueurs(ArrayList<Joueur> joueur_list );
    }
    ValiderJoueursOnClick mValiderJoueurs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: PartieViewJoueursFragment => créé");
        View view = inflater.inflate(R.layout.fragment_partie_joueursview, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);
        joueurslist = (ListView) view.findViewById(R.id.fragment_partie_joueursview_joueurlist);

        JoueursSelected = new ArrayList<>();
        /*
        JoueursSelected = getJoueurFromBundle(); // Afin de récuperer les joueurs déjà séléctionnés !
        if (JoueursSelected == null){
            Log.d(TAG, "onCreateView: JoueursSelected => empty");
            JoueursSelected = new ArrayList<>();}

         */

        AbsListView.MultiChoiceModeListener joueurslistener = new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
                actionMode = actionMode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(android.view.ActionMode mode) {

            }
        };

        joueurslist.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        joueurslist.setMultiChoiceModeListener(joueurslistener);


        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setTextSize(25);
        toolbar_text.setText("Sélectionnez les joueurs");

        PartieSetUpJoueurs();

        ImageView valider = (ImageView) view.findViewById(R.id.valider_toolbar_image);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JoueursSelected.size() >= 2){
                    mValiderJoueurs.ValiderJoueurs(JoueursSelected);
                }else{
                    Toast.makeText(getActivity(), "Veuillez choisir au moins deux joueurs !",Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }


    private void PartieSetUpJoueurs(){
        final ArrayList<Joueur> joueurs = new ArrayList<>();
        JoueurDataBaseHelper db = new JoueurDataBaseHelper(getActivity());
        Cursor curseur = db.getAllJoueur();

        if (! curseur.moveToNext()) {
            Toast.makeText(getActivity(), "Aucun joueur dans la base de données ! :(", Toast.LENGTH_LONG).show();
        }else{
            joueurs.add(new Joueur(
                    curseur.getString(1), // Récupère le nom
                    curseur.getString(2), // Récupère le prénom
                    curseur.getString(3), // Récupère la catégorie
                    curseur.getString(4) // Récupère le chemin de l'image
            ));
            while (curseur.moveToNext()){
                joueurs.add(new Joueur(
                        curseur.getString(1), // Récupère le nom
                        curseur.getString(2), // Récupère le prénom
                        curseur.getString(3), // Récupère la catégorie
                        curseur.getString(4) // Récupère le chemin de l'image
                ));
            }
        }
        // On itère parmis tous les joueurs pour récuper les infos


        // On va afficher les joueurs dans l'odre
        Collections.sort(joueurs, new Comparator<Joueur>() {
            @Override
            public int compare(Joueur o1, Joueur o2) {
                if (o1.getPrenom().equals(o2.getPrenom())){
                    return o1.getNom().compareToIgnoreCase(o2.getNom());
                }else{
                    return o1.getPrenom().compareToIgnoreCase(o2.getPrenom());
                }
            }
        });

        adapter = new PartieJoueursListAdapter(getActivity(), R.layout.activity_partie_joueursview, joueurs, "");
        joueurslist.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mValiderJoueurs = (ValiderJoueursOnClick) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach : ClassCastException : " + e.getMessage());
        }
    }

    /*
    private ArrayList<Joueur> getJoueurFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){ return bundle.getParcelableArrayList(getString(R.string.Joueur));}
        else{return null;}
    }*/
}
