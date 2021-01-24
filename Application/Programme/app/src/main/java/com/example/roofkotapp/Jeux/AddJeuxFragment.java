package com.example.roofkotapp.Jeux;

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

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.utils.ChangePhotoDialog;
import com.example.roofkotapp.utils.Init;
import com.example.roofkotapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddJeuxFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener{

    private EditText nom, type, description;
    private CircleImageView image;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String mSelectedImagePath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_add_jeux, container, false);
        this.nom = (EditText) view.findViewById(R.id.fragment_add_jeux_nom);
        this.type = (EditText) view.findViewById(R.id.fragment_add_jeux_type);
        this.description = (EditText) view.findViewById(R.id.fragment_add_jeux_description);
        this.image = (CircleImageView) view.findViewById(R.id.fragment_add_jeux_image);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);
        this.mSelectedImagePath = null;

        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setText("Nouveau jeu");

        UniversalImageLoader.setImage(null, image, null, "");


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        ImageView camera = (ImageView) view.findViewById(R.id.fragment_add_jeux_galerie);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Demande les différentes permissions pour accéder au stockage
                for(int i = 0; i < Init.PERMISSIONS.length; i++){
                    String[] permission = {Init.PERMISSIONS[i]};
                    if(((JeuxScreen)getActivity()).checkPermission(permission)){
                        if(i == Init.PERMISSIONS.length - 1){
                            ChangePhotoDialog dialog = new ChangePhotoDialog();
                            dialog.show(getFragmentManager(), getString(R.string.Change_photo_dialog));
                            dialog.setTargetFragment(AddJeuxFragment.this, 0);
                            // Faut mettre la dernière ligne quand on navigue d'un fragment à un fragment (un fragment renvoie la bonne image au fragment)
                        }
                    }else{
                        ((JeuxScreen)getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });

        // Sauvegarder les données
        ImageView valider = (ImageView)view.findViewById(R.id.valider_toolbar_image); // On garde les mêmes noms et les mêmes objets
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNullCheck(nom.getText().toString(), type.getText().toString(), description.getText().toString())){
                    JeuxDataBaseHelper jeuxDataBase = new JeuxDataBaseHelper(getActivity());

                    if (! jeuxDataBase.NameAlreadyUse(nom.getText().toString())){
                        Jeux jeux = new Jeux(nom.getText().toString(), description.getText().toString(),
                                type.getText().toString(), mSelectedImagePath);

                        if (jeuxDataBase.AddJeux(jeux)){
                            Toast.makeText(getActivity(), "Le jeu a été enregisté ! :)", Toast.LENGTH_LONG).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            Toast.makeText(getActivity(), "Erreur : un problème est survenu ! :(", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), "Un autre jeu a déjà le même nom !", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getActivity(), "Assurez-vous d'avoir remplis tout les champs !",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    // Fonction pour vérifier que le (pré)nom et catégorie ne sont pas null (retourne true si les champs sont remplis)
    private boolean NotNullCheck(String nom, String type, String description){
        if ((!nom.equals("")) && (!type.equals("")) && (!description.equals(""))){ return true;}
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
        }
        return super.onOptionsItemSelected(item);
    }

    // Retrouve l'image du Bundle (venant de ChangePhotoDialog)
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        if (bitmap != null){    // Tjs mieux de compresser les images, comme ça on diminue significativement la mémoire nécessaire
            ((JeuxScreen)getActivity()).compressBitmap(bitmap, 70);
            image.setImageBitmap(bitmap);
        }
    }

    @Override
    public void getImagePath(String path) {
        if (! (path.equals("")) ){
            path = path.replace(":/", "://");
            mSelectedImagePath = path;
            UniversalImageLoader.setImage(path, image, null, "");
        }
    }
}
