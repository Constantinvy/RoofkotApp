package com.example.roofkotapp;

import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JoueurDataBaseHelper;
import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.utils.ChangePhotoDialog;
import com.example.roofkotapp.utils.Init;
import com.example.roofkotapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class JoueurProfilEditFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener{

    private static final String TAG = "JoueurProfilEditFragmen";

    private Joueur mjoueur;
    private EditText prenom;
    private EditText nom;
    private EditText categorie;
    private CircleImageView mJoueurImage;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String mSelectedImagePath;       // Toutes ces variables viennent du layout fragment_joueur_edit_profil

    public JoueurProfilEditFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_joueur_edit_profil, container, false);
        prenom = (EditText) view.findViewById(R.id.fragment_joueur_edit_profil_prenom);
        nom = (EditText) view.findViewById(R.id.fragment_joueur_edit_profil_nom);
        categorie = (EditText) view.findViewById(R.id.fragment_joueur_edit_profil_categorie);
        mJoueurImage = (CircleImageView) view.findViewById(R.id.fragment_joueur_edit_profil_image);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.fragment_joueur_edit_profil_toolbar);
        mSelectedImagePath = null;

        // On définit le texte dans la toolbar via son id
        TextView text_toolbar = (TextView) view.findViewById(R.id.joueurs_edit_profil_text);
        text_toolbar.setText(R.string.Profil);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        // On récupère le joueur depuis le bundle
        mjoueur = getJoueurFromBundle();

        if (mjoueur != null){
            init();
        }

        ImageView valider = (ImageView)view.findViewById(R.id.joueurs_edit_profil_valider);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Changes save");
                if (NotNullCheck(prenom.getText().toString(), nom.getText().toString(), categorie.getText().toString())){

                    JoueurDataBaseHelper dataBaseHelper = new JoueurDataBaseHelper(getActivity());
                    Cursor curseur = dataBaseHelper.getJoueurId(mjoueur);

                    int id = -1;
                    while(curseur.moveToNext()){
                        id = curseur.getInt(0); // Récupère le JOUEUR_ID
                    }
                    if (id > -1){
                        if (mSelectedImagePath != null){
                            mjoueur.setImage(mSelectedImagePath);
                        }
                        mjoueur.setCat(categorie.getText().toString());
                        mjoueur.setNom(nom.getText().toString());
                        mjoueur.setPrenom(prenom.getText().toString());

                        if (dataBaseHelper.UpDateJoueur(mjoueur, id)) {
                            Toast.makeText(getActivity(), "Modifications enregistrées ! :)", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                    else{
                        Log.d(TAG, "ID  = " + String.valueOf(id));
                        Toast.makeText(getActivity(), "Un problème est survenu avec la base de données ! :/",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        ImageView camera = (ImageView) view.findViewById(R.id.fragment_joueur_edit_profil_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Demande les différentes permissions pour accéder au stockage
                for(int i = 0; i < Init.PERMISSIONS.length; i++){
                    String[] permission = {Init.PERMISSIONS[i]};
                    if(((JoueurScreen)getActivity()).checkPermission(permission)){
                        if(i == Init.PERMISSIONS.length - 1){
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getFragmentManager(), getString(R.string.Change_photo_dialog));
                            dialog.setTargetFragment(JoueurProfilEditFragment.this, 0);
                            // Faut mettre la dernière ligne quand on navigue d'un fragment à un fragment (un fragment renvoie la bonne image au fragment)
                        }
                    }else{
                        ((JoueurScreen)getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });

        return view;
    }

    private boolean NotNullCheck(String prenom, String nom, String cat){
        return (!prenom.equals("")) && (!nom.equals("")) && (!cat.equals(""));
    }

    private void init(){
        prenom.setText(mjoueur.getPrenom());
        nom.setText(mjoueur.getNom());
        categorie.setText(mjoueur.getCat());
        UniversalImageLoader.setImage(mjoueur.getImage(), mJoueurImage, null, "");
    }

    private Joueur getJoueurFromBundle(){
        Log.d(TAG, "getJoueurFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){return bundle.getParcelable(getString(R.string.Joueur));}
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
                JoueurDataBaseHelper dataBaseHelper = new JoueurDataBaseHelper(getActivity());
                Cursor curseur = dataBaseHelper.getJoueurId(mjoueur);

                int id = -1;
                while(curseur.moveToNext()){
                    id = curseur.getInt(0); // Récupère le JOUEUR_ID
                }
                if (id > -1) {   // Signifie qu'on a bien retrouvé le contact
                    if (dataBaseHelper.DeleteJoueur(id)){
                        Toast.makeText(getActivity(), "Le joueur a bien éte supprimé ! :)", Toast.LENGTH_LONG).show();

                        // On veut supprimer les données du joueur qui se trouvent encore dans le bundle !
                        this.getArguments().clear();
                        onResume();
                        getActivity().getSupportFragmentManager().popBackStack();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Une erreur est survenue ! :(", Toast.LENGTH_LONG).show();
                    }

                }
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrouve l'image du Bundle (venant de ChangePhotoDialog)
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if (bitmap != null){    // Tjs mieux de compresser les images, comme ça on diminue significativement la mémoire nécessaire
            ((JoueurScreen)getActivity()).compressBitmap(bitmap, 70);
            mJoueurImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImagePath(String path) {
        if (! (path.equals("")) ){
            path = path.replace(":/", "://");
            mSelectedImagePath = path;
            UniversalImageLoader.setImage(path, mJoueurImage, null, "");
        }
    }
}