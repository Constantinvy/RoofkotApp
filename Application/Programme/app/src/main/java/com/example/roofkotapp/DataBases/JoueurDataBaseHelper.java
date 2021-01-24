package com.example.roofkotapp.DataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.roofkotapp.models.Joueur;

public class JoueurDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "ROOFKOT-JOUEURS";
    private static final String TABLE_NAME = "JOUEUR";
    private static final String COLUMN0 = "JOUEUR_ID";
    private static final String COLUMN1 = "NOM";
    private static final String COLUMN2 = "PRENOM";
    private static final String COLUMN3 = "CATEGORIE";
    private static final String COLUMN4 = "PHOTO";


    public JoueurDataBaseHelper(Context context){
        super(context, DATA_BASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_data_base = "CREATE TABLE " + TABLE_NAME +
                " ( " + COLUMN0 + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                COLUMN1 + " TEXT NOT NULL, " + COLUMN2 + " TEXT NOT NULL, " +
                COLUMN3 + " TEXT NOT NULL, " + COLUMN4 + " TEXT )";

        db.execSQL(create_data_base);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean AddJoueur(Joueur joueur){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, joueur.getNom());
        contentValues.put(COLUMN2, joueur.getPrenom());
        contentValues.put(COLUMN3, joueur.getCat());
        contentValues.put(COLUMN4, joueur.getImage());

        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1){return false;}
        return true;
    }

    public Cursor getAllJoueur(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean UpDateJoueur(Joueur joueur, int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN1, joueur.getNom());
        contentValues.put(COLUMN2, joueur.getPrenom());
        contentValues.put(COLUMN3, joueur.getCat());
        contentValues.put(COLUMN4, joueur.getImage());

        int result = db.update(TABLE_NAME, contentValues, COLUMN0 + " = ? ", new String[] {String.valueOf(id)});
        if (result != 1){return  false;}
        else{
            return true;
        }
    }

    // ATTENTION !!!! Pas oublier les '  ' pour les requêtes !!!!!
    public Cursor getJoueurId(Joueur joueur){
        SQLiteDatabase db = getWritableDatabase();
        String commande = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN1 + " = '" + joueur.getNom()
                + "' AND " + COLUMN2 + " = '" + joueur.getPrenom() +"'";

        return db.rawQuery(commande,null);
    }

    public boolean DeleteJoueur(int joueur_id){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN0 + " = ?", new String[] {String.valueOf(joueur_id)}) == 1;
    }
}