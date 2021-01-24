package com.example.roofkotapp.Jeux;

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

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilJeuxFragment extends Fragment {

    private static final String TAG = "ProfilJeuxFragment";

    private TextView nom, type, description;
    private CircleImageView image;
    private Jeux mjeux;
    private int modifier;

    private androidx.appcompat.widget.Toolbar toolbar, simple_toolbar;

    // Pour permettre de passer les informations pour éditer le profil
    public interface OnEditJeuxListener {
        public void onEditJoueurSelected(Jeux jeux);
    }
    OnEditJeuxListener mOnEditJeuxListener;

    // Permet d'éviter d'avoir des NullPointerException
    public ProfilJeuxFragment(int modif) {
        super();
        setArguments(new Bundle());
        this.modifier = modif;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jeux_profil, container, false);
        this.nom = (TextView) view.findViewById(R.id.fragment_jeux_profil_nom);
        this.type = (TextView) view.findViewById(R.id.fragment_jeux_profil_type);
        this.description = (TextView) view.findViewById(R.id.fragment_jeux_profil_description);
        this.image = (CircleImageView) view.findViewById(R.id.fragment_jeux_profil_image);
        this.mjeux = getJeuxFromBundle();
        this.toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.joueur_toolbar_profil);
        this.simple_toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.simple_toolbar);


        TextView text_toolbar = (TextView) view.findViewById(R.id.simple_toolbar_text);
        text_toolbar.setText("Jeu");
        TextView text_toolbar1 = (TextView) view.findViewById(R.id.joueurs_profil_text);
        text_toolbar1.setText("Jeu");

        // On change la toolbar en fonction de si on voit le jeux depuis ViewJeuFragment ou depuis PartieViewFragment
        if (this.modifier != 0){

            this.toolbar.setVisibility(View.INVISIBLE);
            this.simple_toolbar.setVisibility(View.VISIBLE);

        } else{

            this.toolbar.setVisibility(View.VISIBLE);
            this.simple_toolbar.setVisibility(View.INVISIBLE);

            ImageView modifier = (ImageView) view.findViewById(R.id.joueur_profil_modifier);
            modifier.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: modify button clicked");
                    mOnEditJeuxListener.onEditJoueurSelected(mjeux);
                }
            });

            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            setHasOptionsMenu(true);
        }

        init();

        return view;
    }

    private void init(){
        this.nom.setText(mjeux.getNom());
        this.description.setText(mjeux.getDescription());
        this.type.setText(mjeux.getType());
        UniversalImageLoader.setImage(mjeux.getImage(), image, null, "");
    }

    private Jeux getJeuxFromBundle(){
        Log.d(TAG, "getJeuxFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.jeux));}
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
                JeuxDataBaseHelper dataBaseHelper = new JeuxDataBaseHelper(getActivity());
                if (dataBaseHelper.DeleteJeux(mjeux.getNom())){
                    Toast.makeText(getActivity(), "Le jeux a bien éte supprimé ! :)", Toast.LENGTH_LONG).show();
                    this.getArguments().clear();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(),"Une erreur est survenue ! :(", Toast.LENGTH_LONG).show();
                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Pour prendre les informations pr éditer le profil
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnEditJeuxListener = (OnEditJeuxListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach : ClassCastException : " + e.getMessage());
        }
    }
}