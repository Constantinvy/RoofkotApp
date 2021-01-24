package com.example.roofkotapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Joueur;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

// Peut être employé pour tous les cas où on veut afficher une liste
// d'items.

public class JoueurListAdapter extends ArrayAdapter<Joueur> {

    private LayoutInflater mInflater;
    private List<Joueur> mjoueurs = null;
    private ArrayList<Joueur> arrayList; // Utilisé pour la recherche
    private int layoutResource;
    private Context mContext;
    private String mAppend; // Correspond au préfix de l'image qu'on veut avoir: web -> append = 'https://' , etc...

    public JoueurListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Joueur> joueurs, String append) {
        super(context, resource, joueurs);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mjoueurs = joueurs;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mjoueurs);
    }

    private static class ViewHolder{
        TextView prenom;
        TextView nom;
        TextView categorie;
        ImageView joueurImage;
        ProgressBar progressBar;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // ViewHolder Build Pattern Start
        final ViewHolder holder;

        if (convertView == null){
            convertView = mInflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder();

            //--------------------- Ce qu'on veut changer ------------------------------------
            holder.prenom = (TextView) convertView.findViewById(R.id.joueurview_prenom);
            holder.nom = (TextView) convertView.findViewById(R.id.joueurview_nom);
            holder.categorie = (TextView) convertView.findViewById(R.id.joueurview_categorie);
            holder.joueurImage = (ImageView) convertView.findViewById(R.id.joueurview_image);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.joueurview_progressbar);
            // -------------------------------------------------------------------------------

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //--------------------- Ce qu'on veut changer ------------------------------------
        String prenom_ = getItem(position).getPrenom();
        String nom = getItem(position).getNom();
        String imagepath = getItem(position).getImage();
        String cat_ = getItem(position).getCat();

        holder.prenom.setText(prenom_);
        holder.nom.setText(nom);
        holder.categorie.setText(cat_);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imagepath, holder.joueurImage, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                holder.progressBar.setVisibility(View.GONE);
            }
        });
        //-------------------------------------------------------------------------------

        return convertView;
    }

    // Fonction pour chercher les contacts et faire apparaître ceux avec le préfix écrit
    public void filter(String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        mjoueurs.clear();

        if (characterText == null){
            mjoueurs.addAll(arrayList);
        }else{
            mjoueurs.clear();
            for (Joueur joueur : arrayList){
                if (joueur.getPrenom().toLowerCase(Locale.getDefault()).contains(characterText)){
                    mjoueurs.add(joueur);
                }
            }
        }
        notifyDataSetChanged();
    }
}
