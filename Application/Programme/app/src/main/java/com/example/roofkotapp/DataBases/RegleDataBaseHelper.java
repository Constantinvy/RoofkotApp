package com.example.roofkotapp.DataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Regle;

public class RegleDataBaseHelper extends SQLiteOpenHelper {

    private static String DATA_BASE_NAME = "ROOFKOT-REGLES";
    private static String TABLE_NAME = "REGLES";
    private static String COLUMN0 = "NOM";
    private static String COLUMN1 = "DIFFICULTE";
    private static String COLUMN2 = "DESCRIPTION";

    public RegleDataBaseHelper(Context context){ super(context, DATA_BASE_NAME, null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String commande = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN0 + " TEXT NOT NULL UNIQUE PRIMARY KEY, " +
                COLUMN1 + " TEXT NOT NULL, " + COLUMN2 + " TEXT NOT NULL )";

        db.execSQL(commande);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getRegle(String nom){
        SQLiteDatabase db = this.getWritableDatabase();
        String commande = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN0 + " = '" + nom + "'";

        return db.rawQuery(commande, null);
    }

    public Cursor getAllRegles(){
        SQLiteDatabase db = this.getWritableDatabase();
        String commande = "SELECT * FROM " + TABLE_NAME;

        return db.rawQuery(commande, null);
    }

    public boolean AddRegle(Regle regle){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN0, regle.getNom());
        contentValues.put(COLUMN1, regle.getDifficulte());
        contentValues.put(COLUMN2, regle.getDescription());

        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public boolean UpDateRegle(Regle new_regle, String old_name){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN0, new_regle.getNom());
        contentValues.put(COLUMN1, new_regle.getDifficulte());
        contentValues.put(COLUMN2, new_regle.getDescription());

        return db.update(TABLE_NAME, contentValues, COLUMN0 + " = ? ", new String[] {old_name}) == 1;
    }   // Retourne le nombre de lignes qui ont été mises à jour


    public boolean DeleteRegle(String nom){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN0 + " = ?", new String[] {nom}) == 1;
    }

    public boolean NameAlreadyUsed(String nom){
        Cursor curseur = getRegle(nom);
        try {
            if (curseur.getCount() == 1) {
                curseur.moveToFirst();
                return curseur.getString(0).equals(nom);
            }
            return false;
        }finally {
            if (curseur != null){curseur.close();}
        }
    }

    public Cursor getRegleOfDifficulty(String difficulte){
        SQLiteDatabase db = this.getWritableDatabase();
        String commande = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN1 + " = '" + difficulte + "'";

        return db.rawQuery(commande, null);
    }

    public void init() {
        String normale = "Normale";
        String intermediaire = "Intermédiare";
        String pas_la = "Pas là pour enfiler des perles";
        String roofkot = "Roofkot";

        // ================================================================ \\
        // ------------------------- Normale ------------------------------ \\
        // ================================================================ \\
        Regle inventer_1 = new Regle("Inventer 1", normale, "Le joueur peut inventer une règle (qui doit concerner tous les joueurs). " +
                "Chaque fois que cette règle est enfreinte par un joueur, il doit boire une gorgée.\n \n");
        AddRegle(inventer_1);

        Regle une_moins = new Regle("Une de moins", normale, "Au cours de la partie, le joueur peut refuser une de ses gorgées.\n \n");
        AddRegle(une_moins);

        Regle une_plus = new Regle("Une de plus", normale, "Au cours de la partie, le joueur peut donner une gorgée à boire à un autre joueur.\n \n");
        AddRegle(une_plus);

        Regle defi = new Regle("Le défi", normale,"Une fois au cours de la partie, le joueur peut en choisir deux autres et leur donner une de ses gorgées. "+
                "Choisissez leur un petit défi (bras de fer, culture générale, etc); le perdant devra boire.\n \n");
        AddRegle(defi);

        Regle pouce = new Regle("Le pouce", normale, "Une fois lors de la partie, le joueur dépose discrètement son pouce sur le bord de la table. "+
                "le dernier joueur à mettre le sien boit trois gorgées. Attention : le joueur doit prévenirt expliquer la règle en début de partie !");
        AddRegle(pouce);

        Regle caractere = new Regle("Le caractère physique", normale, "Choisissez un caractère physique, tous les joueurs le possédant, doivent boire deux gorgées.\n \n");
        AddRegle(caractere);

        Regle quitte_double = new Regle("Quitte ou double", normale, "Une fois au cours de la partie, le joueur peut en mettre en garde un autre en lui disant " +
                "Quitte ou double. Dès lors, la prochaine fois que le joueur joue (ou lors de la prochaine manche, en fonction du jeu) il devra boire le double de la quantité si il perd "+
                "et si il réussit, il ne devra rien boire.\n \n");
        AddRegle(quitte_double);

        Regle he_non = new Regle("Hé non !", normale, "Une seule fois au cours de la partie, le joueur peut refuser ses gorgées (peut importe le nombre) en disant au joueur qui veut le " +
                "faire boire 'Hé non'.\n \n");
        AddRegle(he_non);

        Regle annuler = new Regle("Annuler", normale, "Une fois lors de la partie, le joueur peut annuler une règle d'un autre joueur. "+
                "Il peut également distribuer deux gorgées à n'importe quel moment de la partie.");
        AddRegle(annuler);

        Regle intouchable= new Regle("Intouchable", normale, "Une fois lors de la partie, vous pouvez rendre 'intouchable' un ami lors d'une manche. Ne le dites pas aux autres joueurs mais écrivez le "+
                "(sur votre téléphone, papier, etc...) AVANT que la manche ne commence ! Toutes les gorgées que l'on donnera à votre ami n'auront aucun effet sur lui ! Après la manche, vous DEVEZ dire aux autres joueurs que vous avez utilisé cette règle, même si votre ami n'a pas reçu de gorgée !\n \n");
        AddRegle(intouchable);

        // ================================================================== \\
        // ----------------------- Intermédiaire ---------------------------- \\
        // ================================================================== \\

        Regle inventer_2 = new Regle("Inventer 2", intermediaire, "Le joueur peut inventer une règle (qui doit concerner tous les joueurs). " +
                "Chaque fois que cette règle est enfreinte par un joueur, il doit boire une gorgée.\n \n");
        AddRegle(inventer_2);


        Regle miroir_1 = new Regle("Miroir 1", intermediaire, "Une fois au cours de la partie, le joueur peut donner toutes les gorgées qu'il doit boire (sans les accumuler !) à un des joueurs qui tente de le faire boire.\n \n");
        AddRegle(miroir_1);

        Regle prochain_1 = new Regle("Pour le prochain 1", intermediaire, "Une fois au cours de la partie, le joueur peut donner l'ensemble de ses gorgées (sans les accumuler !) " +
                "au joueur qui perdra la prochaine manche (lui compris).\n \n");
        AddRegle(prochain_1);

        Regle annuler2 = new Regle("Annuler 2", intermediaire, "Une fois lors de la partie, le joueur peut annuler une règle qui le concerne uniquement lui ! "+
                "Il peut également donner un affond à n'importe quel moment de la partie.");
        AddRegle(annuler2);

        Regle deux = new Regle("Deux de plus !", intermediaire, "Une fois lors de la partie, le joueur peut distribuer deux affonds aux autres joueurs !\n \n");
        AddRegle(deux);

        Regle coup_pouce = new Regle("Coup de pouce", intermediaire, "Une fois lors de la partie, vous pouvez aider un ami en transferant un de ses affonds à un autre joueur. "+
                "C'est le moment de vous faire des amis !\n \n");
        AddRegle(coup_pouce);


        // ================================================================== \\
        // ---------------- Pas là pour enfiler des perles ------------------ \\
        // ================================================================== \\

        Regle inventer_3 = new Regle("Inventer 3", pas_la, "Le joueur peut inventer une règle (qui doit concerner tous les joueurs). " +
                "Chaque fois que cette règle est enfreinte par un joueur, il doit boire une gorgée.\n \n");
        AddRegle(inventer_3);

        Regle double_2 = new Regle("Le double", pas_la, "Deux fois lors de la partie, le joueur peut doubler le nombre de gorgée que doit boire un joueur.\n \n");
        AddRegle(double_2);

        Regle miroir_2 = new Regle("Miroir 2", pas_la, "Duex fois lors de la partie, le joueur peut donner toutes les gorgées qu'il doit boire (sans les accumuler !) à un autre joueur.\n \n");
        AddRegle(miroir_2);

        Regle prochain2 = new Regle("Pour le prochain 2", pas_la, "Une fois au cours de la partie, le joueur peut donner le double des gorgées qu'il doit boire (sans les accumuler !) au joueur à perdre la prochain manche, "+
                "lui non compris !\n \n");
        AddRegle(prochain2);

        Regle suite1 = new Regle ("La suite 1",pas_la,"À n'importe quel moment, une fois pendant la partie, le joueur peut donner ses gorgées à un autre joueur. "+
                "Pour savoir qui va boire, il va devoir choisir un thème (exemple: voiture, marque, sport, etc). Tous les autres participants devront donner un nom en rapport avec ce "+
                "thème. Le premier joueur à se tromper, à dire un mot qui a déjà été donné ou qui ne sait plus rien dire, perd et boit les gorgées.\n \n");
        AddRegle(suite1);

        Regle annuler3 = new Regle("Annuler 3", pas_la, "Une fois lors de la partie, le joueur peut annuler une règle d'un autre joueur ! "+
                "Il peut également distribuer deux affonds à n'importe quel moment de la partie.");
        AddRegle(annuler3);

        Regle passe_triple = new Regle("Ca passe ou ça triple", pas_la, "Une fois lors de la partie, le joueur peut en prévenir un autre en lui disant "+
                "'ça passe ou ça triple'. Dans ce cas, si le joueur perd la prochaine manche de la partie, il prendra trois fois le nombre de ses gorgées. Si il réussit, les joueurs sont quitte... pour l'instant. \n \n");
        AddRegle(passe_triple);

        Regle forgeron = new Regle("Le forgeron", pas_la, "Cette règle permet au joueur d'en choisir un autre qui devra boire "+
                "le double de toutes les gorgées qu'il reçoit dans les cinq prochaines minutes. Soit le temps de bien se faire enclumer !");
        AddRegle(forgeron);

        Regle fromule1 = new Regle("La formule 1", pas_la, "Une fois au cours de la partie, choisissez votre formule 1, c'est-à-dire un joueur que vous jugez bon en affond. Choisissez ensuite un autre joueur. Ce deuxième joueur doit battre "+
                "votre formule 1 en affond. Tant qu'elle n'y parvient pas, elle doit recommencer (avec un maximum de trois fois) Si le joueur ne parvient pas à battre votre formule 1, il doit reprendre un affond. \n \n");
        AddRegle(fromule1);

        Regle intouchable2 = new Regle("Intouchable 2", pas_la, "Une fois lors de la partie, vous pouvez rendre un de vos amis 'intouchable' pour deux manches. Vous "+
                "devez l'écrire (sur votre téléphone, papier, etc) AVANT le début des manches. Toutes les gorgées que l'on donnera à votre ami n'auront aucun effet sur lui ! Après la manche, vous DEVEZ "+
                "dire aux autres joueurs que vous avez utilisé cette règle, même si votre ami n'a pas reçu de gorgées !\n" +
                "Le joueur peut également distribuer deux affonds. \n \n ");
        AddRegle(intouchable2);

        // ===================================================================== \\
        // --------------------------- Roofkot --------------------------------- \\
        // ===================================================================== \\

        Regle inventer_4 = new Regle("Inventer 4", roofkot, "Le joueur peut inventer une règle (qui doit concerner tous les joueurs). " +
                "Chaque fois que cette règle est enfreinte par un joueur, il doit boire une gorgée.\n \n");
        AddRegle(inventer_4);

        Regle prochain3 = new Regle("Le prochain 3", roofkot, "Deux fois au cours de la partie, le joueur peut donner le double des gorgées qu'il doit boire (sans les accumuler !) au joueur à perdre la prochain manche, \n" +
                "lui non compris !\n \n");
        AddRegle(prochain3);

        Regle triple = new Regle("Le triple", roofkot, "Une fois au cours de la partie, le joueur peut tripler le nombre de gorgée que doit boire un joueur.\n \n");
        AddRegle(triple);

        Regle miroir3 = new Regle("Miroir 3", roofkot, "Le premier joueur à vous faire boire sera en miroir avec vous jusqu'à la fin de la partie.\n \n");
        AddRegle(miroir3);

        Regle suite2 = new Regle ("La suite 2",roofkot,"À n'importe quel moment, une fois pendant la partie, le joueur peut donner ses gorgées à un autre joueur. "+
                "Pour savoir qui va boire, il va devoir choisir un thème (exemple: voiture, marque, sport, etc). Tous les autres participants devront donner un nom en rapport avec ce "+
                "thème. À chaque nom cité, le nombre de gorgée augmente d'un ! Le premier joueur à se tromper, à dire un mot qui a déjà été donné ou qui ne sait plus rien dire, perd "+
                "et boit les gorgées. Donc si Constantin avait 7 gorgées à boire, joue cette règle (il choissit le thème du sport) et 10 noms en rapport avec le sport sont donnés, "+
                "le perdant aura 7 + 10 gorgées à boire ! \n \n");
        AddRegle(suite2);

        Regle amoureux = new Regle("Les amoureux", roofkot, "Au début de la partie, le joueur peut désigner deux amoureux. Dès lors, chaque fois qu'un amoureux doit boire, "+
                "son âme soeur doit boire la même quantité !");
        AddRegle(amoureux);

        Regle annuler4 = new Regle("Annuler 4", roofkot, "Une fois lors de la partie, le joueur peut annuler une règle d'un autre joueur ! "+
                "Il peut également distribuer trois affonds à n'importe quel moment de la partie.");
        AddRegle(annuler4);

        Regle quitte_quadruple = new Regle("Ca passe ou ça quadruple", roofkot, "Une fois lors de la partie, le joueur peut en prévenir un autre en lui disant "+
                "'ça passe ou ça quadruple'. Dans ce cas, si le joueur perd la prochaine manche de la partie, il prendra quatre fois le nombre de ses gorgées. Si il réussit, les joueurs sont quitte... pour l'instant. \n \n");
        AddRegle(quitte_quadruple);

        Regle expert = new Regle("Les experts", roofkot, "Le joueur va énoncer autant de thème que de participant. Pour chaque thème, les autres joueurs devront alors voter pour l'expert selon eux de ce domaine (parmis "+
                "les joueurs). Chaque fois qu'une personne est élue experte, elle doit prendre un affond.");
        AddRegle(expert);

        Regle intouchable3 = new Regle("Intouchable 3", pas_la, "Une fois lors de la partie, vous pouvez rendre un de vos amis 'intouchable' pour deux manches. Vous "+
                "devez l'écrire (sur votre téléphone, papier, etc) AVANT le début des manches. Toutes les gorgées que l'on donnera à votre ami n'auront aucun effet sur lui ! Après la manche, vous DEVEZ "+
                "dire aux autres joueurs que vous avez utilisé cette règle, même si votre ami n'a pas reçu de gorgées !\n" +
                "Le joueur peut également distribuer trois affonds. \n \n ");
        AddRegle(intouchable3);

    }
}
