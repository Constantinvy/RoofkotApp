package com.example.roofkotapp.Jeux;

import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;
import com.example.roofkotapp.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;

public class JeuxScreen extends AppCompatActivity implements
        ViewJeuxFragment.OnAddJeuxListener,
        ViewJeuxFragment.OnJeuxSelectedListener,
        ProfilJeuxFragment.OnEditJeuxListener{


    private static final int REQUEST_CODE = 1;
    private static final String TAG = "JeuxScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeux);

        initImageLoader();

        init();

    }

    // Initialise le premier fragment (joueur)
    private void init(){
        ViewJeuxFragment fragment = new ViewJeuxFragment();

        // Permet de remplacer le contenu du fragment_container par ce fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_jeux_container, fragment);
        transaction.commit();

    }

    @Override
    public void OnJeuxSelected(Jeux jeu) {
        ProfilJeuxFragment fragment = new ProfilJeuxFragment(0);
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.jeux), jeu);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_jeux_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Jeux_Fragment));
        transaction.commit();
    }

    @Override
    public void onAddJeux() {
        AddJeuxFragment fragment = new AddJeuxFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_jeux_container, fragment);
        transaction.addToBackStack(getString(R.string.Add_Jeux));
        transaction.commit();
    }

    @Override
    public void onEditJoueurSelected(Jeux jeux) {
        JeuxProfilEditFragment fragment = new JeuxProfilEditFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.jeux), jeux);
        fragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_jeux_container, fragment);
        transaction.addToBackStack(getString(R.string.Profil_Jeux_Edit_Fragment));
        transaction.commit();
    }

    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(JeuxScreen.this, 2);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }


    /**
     * Generalized method for asking permission. Can pass any array of permissions
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: asking user for permissions.");
        ActivityCompat.requestPermissions(
                JeuxScreen.this,
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
                JeuxScreen.this,
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


}
