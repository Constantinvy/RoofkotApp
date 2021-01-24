package com.example.roofkotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.roofkotapp.models.Joueur;
import com.example.roofkotapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class JoueurScreen extends AppCompatActivity implements
        ViewJoueurFragment.OnJoueurSelectedListener,
        ViewJoueurFragment.OnAddJoueurListener,
        ProfilJoueurFragment.OnEditJoueurListener {

    private static final String TAG = "JoueurScreen";
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joueur);

        initImageLoader();

        init();
    }

    // Initialise le premier fragment (joueur)
    private void init(){
        ViewJoueurFragment fragment = new ViewJoueurFragment();

        // Permet de remplacer le contenu du fragment_container par ce fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(JoueurScreen.this, 1);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    @Override
    public void OnJoueurSelected(Joueur joueur) {
        Log.d(TAG, "OnJoueurSelected: Joueur selected from " + getString(R.string.View_Profils_Joueurs_Fragment)
        + " " + joueur.getNom());

        // On crée un nouveau fragment à travers lequel on veut naviguer.
        ProfilJoueurFragment fragment = new ProfilJoueurFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.Joueur), joueur);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Joueur_Fragment));
        transaction.commit();
    }

    @Override
    public void onEditJoueurSelected(Joueur joueur) {
        Log.d(TAG, "OnEditJoueurSelected: Joueur selected from " + getString(R.string.Profil_Joueur_Edit_Fragment)
                + " " + joueur.getNom());

        // On crée un nouveau fragment à travers lequel on veut naviguer.
        JoueurProfilEditFragment fragment = new JoueurProfilEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.Joueur), joueur);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Joueur_Edit_Fragment));
        transaction.commit();
    }

    /**
     * Generalized method for asking permission. Can pass any array of permissions
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(
                JoueurScreen.this,
                permissions,
                REQUEST_CODE
        );
    }
    /**
     * Compress a bitmap by the @param "quality"
     * Quality can be anywhere from 1-100 : 100 being the highest quality.
     * @param bitmap
     * @param quality
     * @return
     */
    public Bitmap compressBitmap(Bitmap bitmap, int quality){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        return bitmap;
    }
    /**
     * Checks to see if permission was granted for the passed parameters
     * ONLY ONE PERMISSION MAYT BE CHECKED AT A TIME
     * @param permission
     * @return
     */
    public boolean checkPermission(String[] permission){
        Log.d(TAG, "checkPermission: checking permissions for:" + permission[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(
                JoueurScreen.this,
                permission[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: \n Permissions was not granted for: " + permission[0]);
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: requestCode: " + requestCode);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case REQUEST_CODE:
                for(int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: " + permissions[i]);
                    }else{
                        break;
                    }
                }
                break;
        }
    }

    @Override
    public void onAddJoueur() {
        // On crée un nouveau fragment à travers lequel on veut naviguer.
        AddJoueurFragment fragment = new AddJoueurFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(getString(R.string.Add_Joueur));
        transaction.commit();
    }
}

/* Une autre manière de travailler avec les bouttons est d'inclure un "onClick="nom_de_methode" où la méthode prend
en argument une (View view). Ici le code se résumerait à Open_MenuScreen(View view)

 */
