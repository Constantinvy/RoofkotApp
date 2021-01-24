package com.example.roofkotapp.Règles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RegleListAdapter extends ArrayAdapter<Regle> {

    private LayoutInflater mInflater; // Permet d'instancier le layout de chaque éléments de la liste https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
    private List<Regle> regles_list; // Liste de toutes les règles
    private ArrayList<Regle> regle_arrayList; // Utilisé pour la recherche
    private int layoutResource;
    private Context mContext;


    public RegleListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Regle> regles) {
        super(context, resource, regles);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutResource = resource;
        this.mContext = context;
        this.regles_list = regles;
        regle_arrayList = new ArrayList<>();
        this.regle_arrayList.addAll(regles_list);
    }

    private static class ViewHolder{    // Petite classe qui reprend ce qu'il faut afficher pour chaque règle de la liste
        TextView nom;
        TextView difficulte;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final ViewHolder viewHolder;

        if (convertView == null){   // Si rien n'a encore été affiché
            convertView = mInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder();

            //--------------------- Ce qu'on veut afficher ------------------------------------
            viewHolder.nom = (TextView) convertView.findViewById(R.id.reglesview_nom);
            viewHolder.difficulte = (TextView) convertView.findViewById(R.id.reglesview_difficulté);
            // -------------------------------------------------------------------------------

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //-------------------------------- On affiche -----------------------------------
        String nom_ = getItem(position).getNom();
        String difficulte_ = getItem(position).getDifficulte();

        viewHolder.nom.setText(nom_);
        viewHolder.difficulte.setText(difficulte_);
        //-------------------------------------------------------------------------------

        return convertView;
    }

    // Fonction pour chercher les règles et faire apparaître ceux avec le préfix écrit
    public void filter(String characterText){
        characterText = characterText.toLowerCase(Locale.getDefault());
        regles_list.clear();

        if (characterText == null){
            regles_list.addAll(regle_arrayList);
        }else{
            regles_list.clear();
            for (Regle regle : regle_arrayList){
                if (regle.getNom().toLowerCase(Locale.getDefault()).contains(characterText)){
                    regles_list.add(regle);
                }
            }
        }
        notifyDataSetChanged();
    }
}
