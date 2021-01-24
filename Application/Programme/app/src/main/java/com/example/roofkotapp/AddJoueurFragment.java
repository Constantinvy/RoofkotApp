package com.example.roofkotapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddJoueurFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener{

    private static final String TAG = "Add_Joueur";

    //private Joueur mjoueur;
    private EditText prenom;
    private EditText nom;
    private EditText categorie;
    private CircleImageView mJoueurImage;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String mSelectedImagePath;       // Toutes ces variables viennent du layout fragment_joueur_edit_profil


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_joueur, container, false);
        this.prenom = (EditText) view.findViewById(R.id.fragment_add_joueur_prenom);
        this.nom = (EditText) view.findViewById(R.id.fragment_add_joueur_nom);
        this.categorie = (EditText) view.findViewById(R.id.fragment_add_joueur_categorie);
        this.mJoueurImage = (CircleImageView) view.findViewById(R.id.fragment_add_joueur_image);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.fragment_joueur_edit_profil_toolbar);
        this.mSelectedImagePath = null;

        // Remplace la fonction init(). On applique l'image par défaut en demandant qqch d'impossible
        UniversalImageLoader.setImage(null, mJoueurImage, null, "");

        // On définit le texte dans la toolbar via son id
        TextView text_toolbar = (TextView) view.findViewById(R.id.joueurs_edit_profil_text);
        text_toolbar.setText(R.string.Nouveau_joueur);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        ImageView camera = (ImageView) view.findViewById(R.id.fragment_add_joueur_camera);
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
                            dialog.setTargetFragment(AddJoueurFragment.this, 0);
                            // Faut mettre la dernière ligne quand on navigue d'un fragment à un fragment (un fragment renvoie la bonne image au fragment)
                        }
                    }else{
                        ((JoueurScreen)getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });


        // Sauvegarder les données
        ImageView valider = (ImageView)view.findViewById(R.id.joueurs_edit_profil_valider); // On garde les mêmes noms et les mêmes objets
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNullCheck(prenom.getText().toString(), nom.getText().toString(), categorie.getText().toString())){

                    JoueurDataBaseHelper joueurDataBase = new JoueurDataBaseHelper(getActivity());
                    Joueur joueur = new Joueur(nom.getText().toString(), prenom.getText().toString(),
                            categorie.getText().toString(), mSelectedImagePath);

                    Log.d(TAG, "L URL DE L IMAGE => " + mSelectedImagePath);


                    if (joueurDataBase.AddJoueur(joueur)){
                        Toast.makeText(getActivity(), "Le joueur a été enregisté ! :)", Toast.LENGTH_LONG).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }else{
                        Toast.makeText(getActivity(), "Erreur : un problème est survenu ! :(", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });
        return view;
    }


    // Fonction pour vérifier que le (pré)nom et catégorie ne sont pas null (retourne true si les champs sont remplis)
    private boolean NotNullCheck(String prenom, String nom, String cat){
        if ((!prenom.equals("")) && (!nom.equals("")) && (!cat.equals(""))){ return true;}
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

    // Retrouve l'image du Bundle (venant de ChangePhotoDialog)
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if (bitmap != null){    // Tjs mieux de compresser les images, comme ça on diminue significativement la mémoire nécessaire
            ((JoueurScreen)getActivity()).compressBitmap(bitmap, 70);
            mJoueurImage.setImageBitmap(bitmap);
            Log.d(TAG, "getBitmapImage:  IMAGEPATH =>" + mJoueurImage.toString());
        }
    }

    @Override
    public void getImagePath(String path) {
        Log.d(TAG, "getImagePath:  IMAGEPATH => " + path);
        if (! (path.equals("")) ){
            path = path.replace(":/", "://");
            mSelectedImagePath = path;
            UniversalImageLoader.setImage(path, mJoueurImage, null, "");
        }
    }
}
