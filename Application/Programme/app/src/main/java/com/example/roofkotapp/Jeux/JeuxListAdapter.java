package com.example.roofkotapp.Jeux;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.example.roofkotapp.models.Jeux;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JeuxListAdapter extends ArrayAdapter<Jeux> {

    private LayoutInflater mInflater;
    private List<Jeux> mjeux = null;
    private ArrayList<Jeux> arrayList; // Utilisé pour la recherche
    private int layoutResource;
    private Context mContext;
    private String mAppend; // Correspond au préfix de l'image qu'on veut avoir: web -> append = 'https://' , etc...


    public JeuxListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Jeux> jeux, String append) {
        super(context, resource, jeux);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mjeux = jeux;
        arrayList = new ArrayList<>();
        this.arrayList.addAll(mjeux);
    }

    private static class ViewHolder{
        TextView nom;
        TextView type;
        ImageView jeuxImage;
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
            holder.nom = (TextView) convertView.findViewById(R.id.jeuxview_nom);
            holder.type = (TextView) convertView.findViewById(R.id.jeuxview_type);
            holder.jeuxImage = (ImageView) convertView.findViewById(R.id.jeuxview_image);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.jeuxview_progressbar);
            // -------------------------------------------------------------------------------

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        //--------------------- Ce qu'on veut changer ------------------------------------
        String nom_ = getItem(position).getNom();
        String type_ = getItem(position).getType();
        String imagepath = getItem(position).getImage();

        holder.nom.setText(nom_);
        holder.type.setText(type_);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(mAppend + imagepath, holder.jeuxImage, new ImageLoadingListener() {
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
        mjeux.clear();

        if (characterText == null){
            mjeux.addAll(arrayList);
        }else{
            mjeux.clear();
            for (Jeux jeux : arrayList){
                if (jeux.getNom().toLowerCase(Locale.getDefault()).contains(characterText)){
                    mjeux.add(jeux);
                }
            }
        }
        notifyDataSetChanged();
    }
}
