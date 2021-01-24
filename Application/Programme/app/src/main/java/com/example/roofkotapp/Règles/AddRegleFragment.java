package com.example.roofkotapp.Règles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.RegleDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;

public class AddRegleFragment extends Fragment {

    private static final String TAG = "AddRegleFragment";

    private androidx.appcompat.widget.Toolbar toolbar;
    private EditText nom, description;
    private Spinner difficulte;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_regle, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);
        nom = (EditText) view.findViewById(R.id.fragment_add_regle_nom);
        description = (EditText) view.findViewById(R.id.fragment_add_regle_description);
        difficulte = (Spinner) view.findViewById(R.id.fragment_add_regle_difficulte_spinner);

        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setText("Nouvelle règle");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        ImageView valider = (ImageView)view.findViewById(R.id.valider_toolbar_image); // On garde les mêmes noms et les mêmes objets
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNullCheck(nom.getText().toString(), description.getText().toString())){

                    RegleDataBaseHelper dataBaseHelper = new RegleDataBaseHelper(getActivity());
                    Regle regle = new Regle(nom.getText().toString(), difficulte.getSelectedItem().toString(), description.getText().toString());

                    if (dataBaseHelper.NameAlreadyUsed(nom.getText().toString())){
                        Toast.makeText(getActivity(), "Ce nom de règle est déjà utilisé ! Choisissez en un autre ", Toast.LENGTH_LONG).show();
                    }else {
                        if (dataBaseHelper.AddRegle(regle)) {
                            Toast.makeText(getActivity(), "La règle a bien été enregistrée ! :)", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "Erreur : un problème est survenu ! :(", Toast.LENGTH_LONG).show();
                        }
                    }
                }else{
                    Toast.makeText(getActivity(),"Assurez vous d'avoir complété toutes les informations !",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    // Fonction pour vérifier que le (pré)nom et catégorie ne sont pas null (retourne true si les champs sont remplis)
    private boolean NotNullCheck(String nom, String description){
        if ((!nom.equals("")) && (! description.equals(""))){ return true;}
        return false;
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
                Log.d(TAG, "onOptionsItemSelected: delete contact");
        }
        return super.onOptionsItemSelected(item);
    }
}
