package com.example.roofkotapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.roofkotapp.R;

import java.io.File;

public class ChangePhotoDialog extends DialogFragment {

    private static final String TAG = "ChangePhotoDialog";
    private TextView nouvelle_photo, choisir_photo, annuler ;

    public interface OnPhotoReceivedListener{
        public void getBitmapImage(Bitmap bitmap);
        public void getImagePath(String path);
    }
    OnPhotoReceivedListener mOnPhotoReceived;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto,container, false);
        //this.nouvelle_photo = (TextView) view.findViewById(R.id.dialog_changephoto_nouvelle);
        this.choisir_photo = (TextView) view.findViewById(R.id.dialog_changephoto_galerie);
        this.annuler = (TextView) view.findViewById(R.id.dialog_changephoto_annuler);

        // Pour ouvrir l'appareil photo
        /*this.nouvelle_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Init.CAMERA_REQUEST_CODE);

            }
        });
        */

        // Choisir une photo dans la galerie
        this.choisir_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galerieIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galerieIntent.setType("image/*");
                startActivityForResult(galerieIntent,Init.PICKFILE_REQUEST_CODE);

            }
        });

        // Ne pas changer de photo
        this.annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnPhotoReceived = (OnPhotoReceivedListener) getTargetFragment();   // Permet de savoir à quel fragment envoyer l'information
            // On emploie pas getActivity() car, ici, on va d'un fragment à un fragment (et pas d'un fragment à une activty à un fragment)
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ChangePhotoDialog ClassCastException " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == Init.CAMERA_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)) {
            // Resultat de quand on prend une nouvelle photo avec la caméra
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            // On envoie le Bitmap et le fragment à l'interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }

        if ((requestCode == Init.PICKFILE_REQUEST_CODE) && (resultCode == Activity.RESULT_OK)){
            Uri selectedImageURI = data.getData();
            File file = new File(selectedImageURI.toString());

            // On envoie la photo de la galerie et le fragment à l'interface (comme c'est le même fragment que pour l'appareil photo on doit rien changer niveau fragmenttarget)
            mOnPhotoReceived.getImagePath(file.getPath());
            getDialog().dismiss();

        }
    }
}