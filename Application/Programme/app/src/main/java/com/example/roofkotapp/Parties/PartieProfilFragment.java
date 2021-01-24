package com.example.roofkotapp.Parties;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.R;
import com.example.roofkotapp.ViewJoueurFragment;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.utils.JoueurListAdapter;

import java.util.ArrayList;
import java.util.List;

public class PartieProfilFragment extends Fragment {

    private static final String TAG = "PartieProfilFragment";

    private Button bouttou_jeu, boutton_joueurs;
    private TextView Choisir_jeu_text, Ajouter_Joueur_text;
    private ListView mjoueurslistview;
    private JoueurListAdapter adapter;
    private Spinner difficulte_spinner;

    private Jeux mjeu;
    private List<Joueur> mjoueurslist;

    private androidx.appcompat.widget.Toolbar toolbar;

    public PartieProfilFragment() {
        super();
        setArguments(new Bundle());
    }

    public interface ChoisirJeuListener{
        public void ChoisirJeu(int difficulte);
    }
    ChoisirJeuListener mChoisirJeu;

    public interface AjouterJoueursListener{
        public void AjouterJoueurs(int difficulte);
    }
    AjouterJoueursListener mAjouterJoueurs;

    public interface ValiderPartieListener{
        public void ValiderPartie(String difficulte);
    }
    ValiderPartieListener mValiderPartie;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_partie, container, false);
        this.bouttou_jeu = (Button) view.findViewById(R.id.fragment_profil_partie_jeu_boutton);
        this.boutton_joueurs = (Button) view.findViewById(R.id.fragment_profil_partie_joueurs_boutton);
        this.Choisir_jeu_text = (TextView) view.findViewById(R.id.fragment_profil_partie_jeu_text);
        this.Ajouter_Joueur_text = (TextView) view.findViewById(R.id.fragment_profil_partie_joueurs_text);
        this.mjoueurslistview = (ListView) view.findViewById(R.id.fragment_profil_partie_joueurs_listview);
        this.difficulte_spinner = (Spinner) view.findViewById(R.id.fragment_profil_partie_spinner);
        this.toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);

        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setText("Partie");

        difficulte_spinner.setSelection(getDifficulteFromBundle());

        this.bouttou_jeu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChoisirJeu.ChoisirJeu(difficulte_spinner.getSelectedItemPosition());
            }
        });

        this.mjeu = getJeuFromBundle();

        if (mjeu == null){
            this.bouttou_jeu.setText("Choisir");
            this.Choisir_jeu_text.setHint("Choisissez un jeu :");
        }else {
            this.bouttou_jeu.setText("Changer");
            this.Choisir_jeu_text.setTextColor(getResources().getColor(R.color.Black));
            this.Choisir_jeu_text.setText(mjeu.getNom());
        }


        this.boutton_joueurs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAjouterJoueurs.AjouterJoueurs(difficulte_spinner.getSelectedItemPosition());
            }
        });

        this.mjoueurslist = getJoueursFromBundle();
        if (mjoueurslist == null){
            this.Ajouter_Joueur_text.setHint("Choisissez les joueurs :");
            this.boutton_joueurs.setText("Ajouter");
        }else{
            String message = mjoueurslist.size() + " joueurs";
            this.Ajouter_Joueur_text.setTextColor(getResources().getColor(R.color.Black));
            this.Ajouter_Joueur_text.setText(message);
            this.boutton_joueurs.setText("Changer");

            adapter = new JoueurListAdapter(getActivity(), R.layout.activity_joueurview, mjoueurslist, "");
            mjoueurslistview.setAdapter(adapter);
        }

        TextView title_text = (TextView) view.findViewById(R.id.fragment_profil_partie_titre);
        if ((this.mjeu != null) && (this.mjoueurslist != null)){
            title_text.setTextColor(getResources().getColor(R.color.Valider));
        }else{
            title_text.setTextColor(getResources().getColor(R.color.Rouge));
        }
        title_text.setText("Nouvelle partie");


        ImageView valider = (ImageView) view.findViewById(R.id.valider_toolbar_image);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((mjeu != null) && (mjoueurslist != null)) {
                    mValiderPartie.ValiderPartie(difficulte_spinner.getSelectedItem().toString());
                } else {
                    Toast.makeText(getActivity(), "Veuillez choisir un jeu et des joueurs !", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mChoisirJeu = (ChoisirJeuListener) getActivity();
            mAjouterJoueurs = (AjouterJoueursListener) getActivity();
            mValiderPartie = (ValiderPartieListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    private Jeux getJeuFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.jeux));}
        else{return null;}
    }

    private int getDifficulteFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getInt("Difficulte");
        } else {
            return 0;
        }
    }

    private ArrayList<Joueur> getJoueursFromBundle(){
        Bundle bundle = this.getArguments();
        if (bundle != null){ return bundle.getParcelableArrayList(getString(R.string.Joueur));}
        else{return null;}
    }

}