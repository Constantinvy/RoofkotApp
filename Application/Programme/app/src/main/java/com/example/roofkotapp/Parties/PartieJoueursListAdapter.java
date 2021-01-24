package com.example.roofkotapp.Parties;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Joueur;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

public class PartieJoueursListAdapter extends ArrayAdapter<Joueur> {

        private LayoutInflater mInflater;
        private List<Joueur> mjoueurs = null;
        private int layoutResource;
        private Context mContext;
        private String mAppend; // Correspond au préfix de l'image qu'on veut avoir: web -> append = 'https://' , etc...


        public PartieJoueursListAdapter(@NonNull Context context, int resource, @NonNull List<Joueur> joueurs, String append) {
            super(context, resource, joueurs);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutResource = resource;
            this.mContext = context;
            mAppend = append;
            this.mjoueurs = joueurs;
        }

        private static class ViewHolder{
            TextView prenom;
            TextView nom;
            TextView categorie;
            ImageView image;
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
                holder.prenom = (TextView) convertView.findViewById(R.id.activity_partie_joueursview_prenom);
                holder.nom = (TextView) convertView.findViewById(R.id.activity_partie_joueursview_nom);
                holder.categorie = (TextView) convertView.findViewById(R.id.activity_partie_joueursview_categorie);
                holder.image = (ImageView) convertView.findViewById(R.id.activity_partie_joueursview_image);
                // -------------------------------------------------------------------------------

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            //--------------------- Ce qu'on veut changer ------------------------------------
            String prenom_ = getItem(position).getPrenom();
            String nom_ = getItem(position).getNom();
            String categorie_ = getItem(position).getCat();
            String imagepath = getItem(position).getImage();

            holder.prenom.setText(prenom_);
            holder.nom.setText(nom_);
            holder.categorie.setText(categorie_);

            ImageLoader imageLoader = ImageLoader.getInstance();

            imageLoader.displayImage(mAppend + imagepath, holder.image, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {
                }
            });
            //-------------------------------------------------------------------------------


            CheckBox checkBox = convertView.findViewById(R.id.activity_partie_joueursview_checkbox);
            checkBox.setTag(position);

            /*for (Joueur joueur : mjoueurs){
                if (PartieViewJoueursFragment.JoueursSelected.contains(joueur)){
                    Log.d("Checked", "getView: le joueur doit être checké !!");
                    checkBox.setChecked(true);
                }
            }
             */

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = (int) buttonView.getTag();

                    if (PartieViewJoueursFragment.JoueursSelected.contains(mjoueurs.get(position))){
                        PartieViewJoueursFragment.JoueursSelected.remove(mjoueurs.get(position));

                    }else{
                        PartieViewJoueursFragment.JoueursSelected.add(mjoueurs.get(position));
                    }
                }
            });

            return convertView;
        }
}