package com.example.roofkotapp.Parties;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.DataBases.RegleDataBaseHelper;
import com.example.roofkotapp.Jeux.JeuxListAdapter;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.models.Regle;
import com.example.roofkotapp.utils.JoueurListAdapter;

import java.util.ArrayList;
import java.util.Random;

public class PartieViewFragment extends Fragment {

    private static final String TAG = "PartieViewFragment";
    private ListView JeuList, JoueurList;
    private TextView difficulte;
    private ArrayList<Regle> regles_list = null;

    JoueurListAdapter joueur_adapter;
    JeuxListAdapter jeu_adapter;

    public interface ShowJeuProfilListener{
        public void ShowJeuProfil(Jeux jeux);
    }
    ShowJeuProfilListener mshowJeuProfil;

    public interface ShowRegleJoueurListener{
        public void ShowRegleJoueur(Regle regle);
    }
    ShowRegleJoueurListener mshowRegleJoueur;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partieview, container, false);
        this.JeuList = (ListView) view.findViewById(R.id.fragment_partieview_jeu_list);
        this.JoueurList = (ListView) view.findViewById(R.id.fragment_partieview_joueur_list);
        this.difficulte = (TextView) view.findViewById(R.id.fragment_partieview_difficulte_choisie);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.simple_toolbar);
        TextView toolbar_text = (TextView) view.findViewById(R.id.simple_toolbar_text);
        toolbar_text.setText("Partie");

        Jeux jeu = getJeuFromBundle();
        if (jeu != null){
            ArrayList<Jeux> jeuxList = new ArrayList<>();
            jeuxList.add(jeu);
            jeu_adapter = new JeuxListAdapter(getActivity(), R.layout.activity_jeuxview, jeuxList, "");
            this.JeuList.setAdapter(jeu_adapter);
            this.JeuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Jeux jeu = (Jeux) parent.getItemAtPosition(position);
                    mshowJeuProfil.ShowJeuProfil(jeu);
                }
            });
        }

        String diff = getDifficulteFromBundle();
        if (diff != null){this.difficulte.setText(diff);}


        ArrayList<Joueur> joueurList = getJoueursFromBundle();
        if (regles_list == null){
            regles_list = getRegleList();
        }
        if (joueurList != null){
            joueur_adapter = new JoueurListAdapter(getActivity(), R.layout.activity_joueurview, joueurList, "");
            this.JoueurList.setAdapter(joueur_adapter);
            this.JoueurList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mshowRegleJoueur.ShowRegleJoueur(regles_list.get(position));
                }
            });
        }

        return view;
    }


    private Jeux getJeuFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.jeux));}
        else{
            QuitterPartieDialog dialog =new QuitterPartieDialog();

            return null;}
    }

    private String getDifficulteFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getString("Difficulte");
        } else {
            return null;
        }
    }

    private ArrayList<Joueur> getJoueursFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){ return bundle.getParcelableArrayList(getString(R.string.Joueur));}
        else{return null;}
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
           mshowJeuProfil = (ShowJeuProfilListener) getActivity();
           mshowRegleJoueur = (ShowRegleJoueurListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    private ArrayList<Regle> getRegleList(){
        RegleDataBaseHelper regleDataBase = new RegleDataBaseHelper(getActivity());
        Random random = new Random();
        int joueur_lenght = getJoueursFromBundle().size();

        ArrayList<Regle> rpse = new ArrayList<>();
        for (int i = 0; i < joueur_lenght; i++){
            Cursor curseur = regleDataBase.getRegleOfDifficulty(getDifficulteFromBundle());
            int aleatoire = random.nextInt(curseur.getCount());

            curseur.moveToFirst();
            for (int j = 0; j < aleatoire; j++){
                curseur.moveToNext();
            }
            Regle random_regle = new Regle(curseur.getString(0), curseur.getString(1),
                    curseur.getString(2));
            Log.d(TAG, "LA REGLE ALEATOIRE =>  " + random_regle.getNom());
            rpse.add(random_regle);
            curseur.close();
        }

        return rpse;
    }

}
