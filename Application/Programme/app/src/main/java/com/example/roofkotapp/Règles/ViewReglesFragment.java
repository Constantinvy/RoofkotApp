package com.example.roofkotapp.Règles;

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

import com.example.roofkotapp.DataBases.RegleDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewReglesFragment extends Fragment {

    private static final String TAG = "ViewReglesFragment";

    private static final int STANDARD_APP_BAR = 0;
    private static final int SEARCH_APP_BAR = 1;
    private int mAppBarState;

    private AppBarLayout viewJoueurBar, searchBar;
    private RegleListAdapter adapter;
    private ListView regle_list;
    private EditText SearchText;


    // -- INTERFACES --

    // Pour afficher la règle sélécionnée
    public interface OnRegleSelectedListener{
        public void OnRegleSelected(Regle regle);
    }
    OnRegleSelectedListener mRegleListener;

    // Pour ajouter une règle
    public interface OnAddRegleListener {
        public void onAddRegle();
    }
    OnAddRegleListener mOnAddRegle;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reglesview, container, false);
        regle_list = (ListView) view.findViewById(R.id.regles_liste);
        viewJoueurBar = (AppBarLayout) view.findViewById(R.id.joueur_toolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.joueur_search_toolbar);
        SearchText = (EditText) view.findViewById(R.id.search_joueurs_text);

        TextView toolbar_text = (TextView) view.findViewById(R.id.joueurs_text);
        toolbar_text.setText(R.string.regles);

        SearchText.setHint("Trouver une règle");

        
        setAppBarState(STANDARD_APP_BAR);

        setUpRegleList();



        // - WIDGET -

        FloatingActionButton add_regles = (FloatingActionButton) view.findViewById(R.id.add_regles_button);
        add_regles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add clicked");
                mOnAddRegle.onAddRegle();
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


    private void setUpRegleList(){
        final ArrayList<Regle> regles = new ArrayList<>();
        RegleDataBaseHelper dataBaseHelper = new RegleDataBaseHelper(getActivity());
        Cursor curseur = dataBaseHelper.getAllRegles();

        if (! curseur.moveToNext()) {
            Toast.makeText(getActivity(), "Aucune règle dans la base de données ! :(", Toast.LENGTH_LONG).show();
            dataBaseHelper.init();
            Toast.makeText(getActivity(), "Les règles ont été chargées ! :)", Toast.LENGTH_SHORT).show();
        }else{
            // Pour bien afficher la 1e règle
            regles.add(new Regle(
                    curseur.getString(0), // Récupère le nom
                    curseur.getString(1), // Récupère la difficulté
                    curseur.getString(2)    // Récupère la description
            ));
            while (curseur.moveToNext()){
                regles.add(new Regle(
                        curseur.getString(0), // Récupère le nom
                        curseur.getString(1), // Récupère la difficulté
                        curseur.getString(2)    // Récupère la description
                ));
            }
        }


        Collections.sort(regles, new Comparator<Regle>() {
            @Override
            public int compare(Regle o1, Regle o2) {
                if (o1.getDifficulte().equals(o2.getDifficulte())){
                    return o1.getNom().compareToIgnoreCase(o2.getNom()); }

                if (o1.getDifficulte().equals("Normale")){
                    return -1; }

                if (o2.getDifficulte().equals("Normale")){
                    return 1; }

                else{
                    return o1.getDifficulte().compareToIgnoreCase(o2.getDifficulte());
                }
            }
        });

        adapter = new RegleListAdapter(getActivity(), R.layout.activity_reglesview,regles);
        SearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = SearchText.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        regle_list.setAdapter(adapter);
        regle_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mRegleListener.OnRegleSelected(regles.get(position));
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mRegleListener = (OnRegleSelectedListener) getActivity();
            mOnAddRegle = (OnAddRegleListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach - ViewReglesFragment: ClassCastException : " + e.getMessage());
        }
    }
}
