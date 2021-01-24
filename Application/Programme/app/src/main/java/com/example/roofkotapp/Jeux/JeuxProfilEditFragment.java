package com.example.roofkotapp.Jeux;

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

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.utils.ChangePhotoDialog;
import com.example.roofkotapp.utils.Init;
import com.example.roofkotapp.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class JeuxProfilEditFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener {

    private static final String TAG = "JeuxProfilEditFragment";

    private Jeux mjeux;
    private EditText nom, type, description;
    private CircleImageView image;
    private androidx.appcompat.widget.Toolbar toolbar;
    private String old_name;
    private String mSelectedImagePath;

    public JeuxProfilEditFragment() {
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jeux_edit_profil, container, false);
        this.nom = view.findViewById(R.id.fragment_jeux_edit_profil_nom);
        this.type = view.findViewById(R.id.fragment_jeux_edit_profil_type);
        this.description = view.findViewById(R.id.fragment_jeux_edit_profil_description);
        this.image = view.findViewById(R.id.fragment_jeux_edit_profil_image);
        this.toolbar = view.findViewById(R.id.valider_toolbar);

        // On définit le texte dans la toolbar via son id
        TextView text_toolbar = (TextView) view.findViewById(R.id.valider_toolbar_text);
        text_toolbar.setText("Modifier un jeu");

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        mjeux = getJeuxFromBundle();

        if (mjeux != null){
            init();
        }
        this.old_name = mjeux.getNom();


        ImageView valider = (ImageView)view.findViewById(R.id.valider_toolbar_image);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NotNullCheck(nom.getText().toString(), type.getText().toString(), description.getText().toString())){

                    JeuxDataBaseHelper dataBaseHelper = new JeuxDataBaseHelper(getActivity());

                    if( (old_name.equals(nom.getText().toString())) || (! dataBaseHelper.NameAlreadyUse(nom.getText().toString())) ){
                    // Si le nom n'a pas changé ou que le nouveau nom choisi n'est pas encore utilisé
                        mjeux.setNom(nom.getText().toString());
                        mjeux.setType(type.getText().toString());
                        mjeux.setDescription(description.getText().toString());
                        if (mSelectedImagePath != null){ mjeux.setImage(mSelectedImagePath); }

                        if (dataBaseHelper.UpDateJeux(old_name, mjeux)){
                            Toast.makeText(getActivity(),"Les changements ont été enregistré ! :)", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }else{
                            Toast.makeText(getActivity(),"Un problème est survenu !", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getActivity(),"Ce nom est déjà utilisé !", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        ImageView camera = (ImageView) view.findViewById(R.id.fragment_jeux_edit_profil_galerie);
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
                            dialog.setTargetFragment(JeuxProfilEditFragment.this, 0);
                            // Faut mettre la dernière ligne quand on navigue d'un fragment à un fragment (un fragment renvoie la bonne image au fragment)
                        }
                    }else{
                        ((JeuxScreen)getActivity()).verifyPermissions(permission);
                    }
                }
            }
        });

        return view;
    }

    private void init(){
        this.nom.setText(mjeux.getNom());
        this.type.setText(mjeux.getType());
        this.description.setText(mjeux.getDescription());
        UniversalImageLoader.setImage(mjeux.getImage(), image, null, "");

    }

    private Jeux getJeuxFromBundle() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.jeux));
        } else {
            return null;
        }
    }

    private boolean NotNullCheck(String nom, String type, String description){
        return (!nom.equals("")) && (!type.equals("")) && (!description.equals(""));
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
                    Toast.makeText(getActivity(), "Le jeu a bien éte supprimé ! :)", Toast.LENGTH_SHORT).show();
                    // On veut supprimer les données du joueur qui se trouvent encore dans le bundle !
                    this.getArguments().clear();
                    onResume();
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().getSupportFragmentManager().popBackStack();
                }else{
                    Toast.makeText(getActivity(), "Une erreur est survenue ! :(", Toast.LENGTH_SHORT).show();
                }

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
