package com.example.roofkotapp.Parties;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class PartieJeuxListAdapter extends ArrayAdapter<Jeux> {

    private LayoutInflater mInflater;
    private List<Jeux> mjeux = null;
    private int layoutResource;
    private Context mContext;
    private String mAppend; // Correspond au préfix de l'image qu'on veut avoir: web -> append = 'https://' , etc...


    public PartieJeuxListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Jeux> jeux, String append) {
        super(context, resource, jeux);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        mAppend = append;
        this.mjeux = jeux;
    }

    private static class ViewHolder{
        TextView nom;
        TextView type;
        ImageView jeuxImage;
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
            holder.nom = (TextView) convertView.findViewById(R.id.activity_partie_jeuxview_nom);
            holder.type = (TextView) convertView.findViewById(R.id.activity_partie_jeuxview_type);
            holder.jeuxImage = (ImageView) convertView.findViewById(R.id.activity_partie_jeuxview_image);
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


        CheckBox checkBox = convertView.findViewById(R.id.activity_partie_jeuxview_checkbox);
        checkBox.setTag(position);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int position = (int) buttonView.getTag();

                if (PartieViewJeuxFragment.JeuxSelected.contains(mjeux.get(position))){
                    PartieViewJeuxFragment.JeuxSelected.remove(mjeux.get(position));

                }else{
                    PartieViewJeuxFragment.JeuxSelected.add(mjeux.get(position));
                }
                //PartieViewJeuxFragment.actionMode.setTitle(PartieViewJeuxFragment.JeuxSelected.size() + " jeux sélectionnés");
            }
        });

        return convertView;
    }
}
