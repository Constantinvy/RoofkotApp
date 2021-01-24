package com.example.roofkotapp.Jeux;

import android.content.Context;
import android.database.Cursor;
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

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.models.Joueur;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewJeuxFragment extends Fragment {

    private static final String TAG = "ViewJeuxFragment";

    private static final int STANDARD_APP_BAR = 0;
    private static final int SEARCH_APP_BAR = 1;
    private int mAppBarState;

    private AppBarLayout viewJoueurBar, searchBar;
    private JeuxListAdapter adapter;
    private ListView jeuxlist;
    private EditText mSearchJoueur;

    public interface OnJeuxSelectedListener{
        public void OnJeuxSelected(Jeux jeu);
    }
    OnJeuxSelectedListener mJeuxListener;


    // Interface pour ajouter un nouveau joueur
    public interface OnAddJeuxListener{
        public void onAddJeux();
    }
    OnAddJeuxListener mOnAddJeux;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jeuxview, container, false);
        viewJoueurBar = (AppBarLayout) view.findViewById(R.id.joueur_toolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.joueur_search_toolbar);
        jeuxlist = (ListView) view.findViewById(R.id.jeux_list);
        mSearchJoueur = (EditText) view.findViewById(R.id.search_joueurs_text);

        // On définit les textes des toolbar
        mSearchJoueur.setHint("Trouver un jeu");
        TextView toolbar_text = (TextView) view.findViewById(R.id.joueurs_text);
        toolbar_text.setText("Jeux");

        /*
        JeuxDataBaseHelper db = new JeuxDataBaseHelper(getActivity());
        db.DeleteAll();
        */

        setAppBarState(STANDARD_APP_BAR);

        setUpJeuxList();

        // Tout pour l'instant
        FloatingActionButton add = (FloatingActionButton) view.findViewById(R.id.add_jeux_boutton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add clicked");
                mOnAddJeux.onAddJeux();
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
            mJeuxListener = (OnJeuxSelectedListener) getActivity();
            mOnAddJeux = (OnAddJeuxListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage());
        }
    }

    private void setUpJeuxList(){
        final ArrayList<Jeux> arrayList_jeux = new ArrayList<>();
        JeuxDataBaseHelper db = new JeuxDataBaseHelper(getActivity());
        Cursor curseur = db.getAllJeux();

        if (! curseur.moveToNext()) {
            Toast.makeText(getActivity(), "Aucun jeu dans la base de données ! :(", Toast.LENGTH_SHORT).show();
            db.init();
            Toast.makeText(getActivity(), "Les jeux ont été chargés ! :(", Toast.LENGTH_SHORT).show();

        }else{
            arrayList_jeux.add(new Jeux(
                    curseur.getString(0), // Récupère le nom
                    curseur.getString(1),  // Récupère la description
                    curseur.getString(2), // Récupère le type
                    curseur.getString(3) // Récupère le chemin de l'image
            ));
            while (curseur.moveToNext()){
                arrayList_jeux.add(new Jeux(
                        curseur.getString(0), // Récupère le nom
                        curseur.getString(1),  // Récupère la description
                        curseur.getString(2), // Récupère le type
                        curseur.getString(3) // Récupère le chemin de l'image
                ));
            }
        }

        Collections.sort(arrayList_jeux, new Comparator<Jeux>() {
            @Override
            public int compare(Jeux o1, Jeux o2) {
                return o1.getNom().compareToIgnoreCase(o2.getNom());
            }
        });

        adapter = new JeuxListAdapter(getActivity(), R.layout.activity_jeuxview, arrayList_jeux, "");
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

        jeuxlist.setAdapter(adapter);

        jeuxlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mJeuxListener.OnJeuxSelected(arrayList_jeux.get(position));
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
