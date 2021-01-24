package com.example.roofkotapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JoueurDataBaseHelper;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.utils.JoueurListAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewJoueurFragment extends Fragment {

    private static final String TAG = "ViewJoueurFragment";

    private static final int STANDARD_APP_BAR = 0;
    private static final int SEARCH_APP_BAR = 1;
    private int mAppBarState;

    private AppBarLayout viewJoueurBar, searchBar;
    private JoueurListAdapter adapter;
    private ListView joueurslist;
    private EditText mSearchJoueur;

    public interface OnJoueurSelectedListener{
        public void OnJoueurSelected(Joueur joueur);
    }
    OnJoueurSelectedListener mJoueurListener;


    // Interface pour ajouter un nouveau joueur
    public interface OnAddJoueurListener{
        public void onAddJoueur();
    }
    OnAddJoueurListener mOnAddJoueur;

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joueurview, container, false);
        viewJoueurBar = (AppBarLayout) view.findViewById(R.id.joueur_toolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.joueur_search_toolbar);
        joueurslist = (ListView) view.findViewById(R.id.joueurs_list);
        mSearchJoueur = (EditText) view.findViewById(R.id.search_joueurs_text);

        // On définit les textes des toolbar
        mSearchJoueur.setHint("Trouver un joueur");
        TextView toolbar_text = (TextView) view.findViewById(R.id.joueurs_text);
        toolbar_text.setText("Joueur");

        setAppBarState(STANDARD_APP_BAR);

        setUpJoueurList();

        // Tout pour l'instant
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_joueur_boutton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add clicked");
                mOnAddJoueur.onAddJoueur();
            }
        });

        ImageView loupe = (ImageView)view.findViewById(R.id.joueur_toolbar_loupe);
        loupe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToolBarState();
            }
        });

        ImageView backarrow = (ImageView) view.findViewById(R.id.joueur_toolbar_backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleToolBarState();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{
            mJoueurListener = (OnJoueurSelectedListener) getActivity();
            mOnAddJoueur = (OnAddJoueurListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    private void setUpJoueurList(){
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

        adapter = new JoueurListAdapter(getActivity(), R.layout.activity_joueurview, joueurs, "");
        mSearchJoueur.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchJoueur.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        joueurslist.setAdapter(adapter);

        joueurslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mJoueurListener.OnJoueurSelected(joueurs.get(position));
            }
        });
    }

    private void toggleToolBarState(){
        if (mAppBarState == STANDARD_APP_BAR){setAppBarState(SEARCH_APP_BAR);}

        else{setAppBarState(STANDARD_APP_BAR);}
    }

    private void setAppBarState(int state){
        Log.d(TAG, "setAppBarState: changing app bar state to " + state);

        mAppBarState = state;
        if (mAppBarState == STANDARD_APP_BAR){
            searchBar.setVisibility(View.GONE);
            viewJoueurBar.setVisibility(View.VISIBLE);
            View view = getView();

            // ferme le clavier
            InputMethodManager ipm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try{
                ipm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }catch (NullPointerException e){
                Log.d(TAG, "setAppBarState: NullPointerException " + e.getMessage());
            }
        }
        else if (mAppBarState == SEARCH_APP_BAR){
            viewJoueurBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            // Ouvre le clavier
            InputMethodManager ipm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            ipm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APP_BAR);
    }
}
