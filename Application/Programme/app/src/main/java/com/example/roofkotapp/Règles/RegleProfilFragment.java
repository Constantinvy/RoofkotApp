package com.example.roofkotapp.Règles;

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

import com.example.roofkotapp.DataBases.RegleDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;

public class RegleProfilFragment extends Fragment {

    private static final String TAG = "RegleProfilFragment";
    private TextView toolbar_text, regle_name, regle_difficulte, regle_description;
    private ImageView modifier;
    private Regle mregle;
    private int cacher;

    private androidx.appcompat.widget.Toolbar toolbar, simple_toolbar;

    // Pour permettre de passer les informations pour éditer le profil
    public interface OnEditRegleListener {
        public void onEditRegleSelected(Regle regle);
    }
    OnEditRegleListener mOnEditRegleListener;

    // Permet d'éviter d'avoir des NullPointerException
    public RegleProfilFragment(int hide){
        super();
        setArguments(new Bundle());
        cacher = hide;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_regle_profil, container, false);
        this.toolbar_text = (TextView) view.findViewById(R.id.joueurs_profil_text);
        this.regle_name = (TextView) view.findViewById(R.id.fragment_regle_profil_nom);
        this.regle_difficulte = (TextView) view.findViewById(R.id.fragment_regle_profil_difficulte);
        this.regle_description = (TextView) view.findViewById(R.id.fragment_regle_profil_description);
        this.modifier = (ImageView) view.findViewById(R.id.joueur_profil_modifier);
        this.toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.joueur_toolbar_profil);
        this.simple_toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.simple_toolbar);

        this.mregle = getRegleFromBundle();

        this.toolbar_text.setText("Détails");
        TextView simple_toolbar_text = (TextView) view.findViewById(R.id.simple_toolbar_text);
        simple_toolbar_text.setText("Détails");

        if (cacher == 0){

            this.toolbar.setVisibility(View.VISIBLE);
            this.simple_toolbar.setVisibility(View.GONE);

            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            setHasOptionsMenu(true);

            // Pour modifier le profil
            this.modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnEditRegleListener.onEditRegleSelected(mregle);
                }
            });
        }else{
            this.toolbar.setVisibility(View.GONE);
            this.simple_toolbar.setVisibility(View.VISIBLE);
        }

        init();

        return view;
    }

    private void init(){
        this.regle_name.setText(mregle.getNom());
        this.regle_difficulte.setText(mregle.getDifficulte());
        this.regle_description.setText(mregle.getDescription());
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
                RegleDataBaseHelper dataBaseHelper = new RegleDataBaseHelper(getActivity());
                if (dataBaseHelper.DeleteRegle(mregle.getNom()) ){    // La règle a bien été supprimée
                    Toast.makeText(getActivity(), "La règle a bien été supprimée ! :)", Toast.LENGTH_LONG).show();
                    this.getArguments().clear();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(),"Une erreur est survenue ! :(", Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrouve le joueur sélectionné depuis JoueurScreen
    private Regle getRegleFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.regles));}
        else{return null;}
    }

    // Pour prendre les informations pr éditer le profil
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnEditRegleListener = (OnEditRegleListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach : ClassCastException : " + e.getMessage());
        }
    }
}
