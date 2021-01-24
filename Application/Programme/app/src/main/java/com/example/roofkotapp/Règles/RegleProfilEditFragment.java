package com.example.roofkotapp.Règles;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.RegleDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;

public class RegleProfilEditFragment extends Fragment {

    private EditText new_name, description;
    private androidx.appcompat.widget.Toolbar toolbar;
    private Spinner difficulte;
    private Regle mRegle;
    private String old_name;

    public RegleProfilEditFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_regle, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);
        new_name = (EditText) view.findViewById(R.id.fragment_add_regle_nom);
        description = (EditText) view.findViewById(R.id.fragment_add_regle_description);
        difficulte = (Spinner) view.findViewById(R.id.fragment_add_regle_difficulte_spinner);

        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setText("Editer la règle");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mRegle = getRegleFromBundle();
        if (mRegle != null){
            init(); // On affiche toutes les informations
        }
        old_name = mRegle.getNom();

        ImageView valider = (ImageView)view.findViewById(R.id.valider_toolbar_image); // On garde les mêmes noms et les mêmes objets
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNullCheck(new_name.getText().toString(), description.getText().toString())) {

                    RegleDataBaseHelper dataBaseHelper = new RegleDataBaseHelper(getActivity());

                    if ((old_name.equals(new_name.getText().toString())) || (!dataBaseHelper.NameAlreadyUsed(new_name.getText().toString()) ) ){
                        /*
                        On vérifie que, si on a un nouveau nom différent de l'ancien, il ne correspond pas à un nom déjà utilisé !
                        Sans ce if l'edit se crash si on attribue un nom déjà utilisé (la condition unique de la BDD n'est plus respéctée !)
                         */
                        mRegle.setNom(new_name.getText().toString());
                        mRegle.setDifficulte(difficulte.getSelectedItem().toString());
                        mRegle.setDescription(description.getText().toString());

                        if (dataBaseHelper.UpDateRegle(mRegle, old_name)) {
                            Toast.makeText(getActivity(), "Modifications enregistées ! :)", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getActivity(), "Erreur : un problème est survenu ! :(", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Ce nom de règle est déjà utilisé ! Choisissez en un autre ", Toast.LENGTH_LONG).show();
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

    private void init(){
        new_name.setText(mRegle.getNom());
        description.setText(mRegle.getDescription());

        //Setting the selected device to the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.Difficulté, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulte.setAdapter(adapter);
        int position = adapter.getPosition(mRegle.getDifficulte());
        difficulte.setSelection(position);
    }

    private Regle getRegleFromBundle(){
        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.regles));}
        else{return null;}
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
                if (dataBaseHelper.DeleteRegle(mRegle.getNom())){
                    Toast.makeText(getActivity(), "La règle a bien éte supprimée ! :)", Toast.LENGTH_LONG).show();

                    // On veut supprimer les données du joueur qui se trouvent encore dans le bundle !
                    this.getArguments().clear();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(), "Une erreur est survenue ! :(", Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

}
