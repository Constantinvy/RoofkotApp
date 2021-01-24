package com.example.roofkotapp.Parties;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.roofkotapp.DataBases.JeuxDataBaseHelper;
import com.example.roofkotapp.Jeux.JeuxListAdapter;
import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PartieViewJeuxFragment extends Fragment {

    private static final String TAG = "PartieViewJeuxFragment";
    private PartieJeuxListAdapter adapter;
    private ListView jeuxlist;
    private androidx.appcompat.widget.Toolbar toolbar;

    public static List<Jeux> JeuxSelected = new ArrayList<>();
    public static ActionMode actionMode = null;


    public interface ValiderJeuOnClick{
        public void ValiderJeu(Jeux Selected_Jeu);
    }
    ValiderJeuOnClick mValiderJeu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: PartieViewJeuxFragment => créé");
        View view = inflater.inflate(R.layout.fragment_partie_jeuxview, container, false);
        toolbar = (androidx.appcompat.widget.Toolbar) view.findViewById(R.id.valider_toolbar);
        jeuxlist = (ListView) view.findViewById(R.id.fragment_partie_jeuxview_jeuxlist);


        AbsListView.MultiChoiceModeListener jeulistener = new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                actionMode = mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        };

        jeuxlist.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        jeuxlist.setMultiChoiceModeListener(jeulistener);


        TextView toolbar_text = (TextView) view.findViewById(R.id.valider_toolbar_text);
        toolbar_text.setText("Sélectionnez un jeu");

        PartieSetUpJeux();

        ImageView valider = (ImageView) view.findViewById(R.id.valider_toolbar_image);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (JeuxSelected.size() == 1){
                    getActivity().getSupportFragmentManager().popBackStack();
                    mValiderJeu.ValiderJeu(JeuxSelected.get(0));
                    JeuxSelected.clear();
                }else{
                    Toast.makeText(getActivity(), "Vous devez sélectionner un jeu !", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }


    private void PartieSetUpJeux(){
        final ArrayList<Jeux> arrayList_jeux = new ArrayList<>();
        JeuxDataBaseHelper db = new JeuxDataBaseHelper(getActivity());
        Cursor curseur = db.getAllJeux();

        if (! curseur.moveToNext()) {
            Toast.makeText(getActivity(), "Aucun jeu dans la base de données ! :(", Toast.LENGTH_SHORT).show();
            db.init();
            Toast.makeText(getActivity(), "Les jeux ont été chargés ! :)", Toast.LENGTH_SHORT).show();

        }else{
            arrayList_jeux.add(new Jeux(
                    curseur.getString(0), // Récupère le nom
                    curseur.getString(1),  // Récupère la description
                    curseur.getString(2), // Récupère le type
                    curseur.getString(3) // Récupère le chemin de l'image
            ));
            while (curseur.moveToNext()){
                arrayList_jeux.add(new Jeux(
                        curseur.getString(0), // Récupère le nom
                        curseur.getString(1),  // Récupère la description
                        curseur.getString(2), // Récupère le type
                        curseur.getString(3) // Récupère le chemin de l'image
                ));
            }
        }


        // On va afficher les joueurs dans l'odre
        Collections.sort(arrayList_jeux, new Comparator<Jeux>() {
            @Override
            public int compare(Jeux o1, Jeux o2) {
                return o1.getNom().compareToIgnoreCase(o2.getNom());
            }
        });

        adapter = new PartieJeuxListAdapter(getActivity(), R.layout.activity_partie_jeuxview, arrayList_jeux, "");
        jeuxlist.setAdapter(adapter);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mValiderJeu = (ValiderJeuOnClick) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach : ClassCastException : " + e.getMessage());
        }
    }
}