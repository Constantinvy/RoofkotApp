package com.example.roofkotapp.DataBases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.roofkotapp.R;
import com.example.roofkotapp.models.Jeux;

public class JeuxDataBaseHelper extends SQLiteOpenHelper {

    private static final String DATA_BASE_NAME = "ROOFKOT-JEUX-Test";
    private static final String TABLE_NAME = "JEUX";
    private static final String COLUMN0 = "NOM";
    private static final String COLUMN1 = "DESCRIPTION";
    private static final String COLUMN2 = "TYPE";
    private static final String COLUMN3 = "IMAGE";


    public JeuxDataBaseHelper(Context context){
        super(context, DATA_BASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_data_base = "CREATE TABLE " + TABLE_NAME +
                " ( " + COLUMN0 + " TEXT NOT NULL UNIQUE PRIMARY KEY, " +
                COLUMN1 + " TEXT NOT NULL, " + COLUMN2 + " TEXT NOT NULL, " +
                COLUMN3 + " TEXT )";

        db.execSQL(create_data_base);

        //init();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean AddJeux(Jeux jeu){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN0, jeu.getNom());
        contentValues.put(COLUMN1, jeu.getDescription());
        contentValues.put(COLUMN2, jeu.getType());
        contentValues.put(COLUMN3, jeu.getImage());

        return db.insert(TABLE_NAME,null, contentValues) != -1;
    }

    public Cursor getJeux(String nom){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN0 + " = ?", new String[] {nom});
    }

    public Cursor getAllJeux(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean UpDateJeux(String nom, Jeux jeux){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN0, jeux.getNom());
        contentValues.put(COLUMN1, jeux.getDescription());
        contentValues.put(COLUMN2, jeux.getType());
        contentValues.put(COLUMN3, jeux.getImage());

        return db.update(TABLE_NAME, contentValues, COLUMN0 + " = ? ", new String[] {nom}) == 1;
    }

    public boolean DeleteJeux(String nom){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN0 + " = ?", new String[] {nom}) == 1;
    }

    public void DeleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF  EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean NameAlreadyUse(String nom){
        Cursor curseur = getJeux(nom);
        try{
            if (curseur.getCount() == 1){
                curseur.moveToFirst();
                return curseur.getString(0).equals(nom);
            }
            return false;
        }finally {
            if (curseur != null){curseur.close(); }
        }
    }

    public void init(){
        Jeux pyramide_roofkot = new Jeux("Pyramide Roofkot", "Disposez les cartes en forme de pyramide, face cachée." +
                " Ensuite (et pour toutes les manches), chaque joueur" +
                " doit avoir 3 cartes en main. La partie commence quand on retourne la première carte. La couleur de la carte retournée " +
                " (coeur, trèfle, carreau ou pic) est l'atout de cette manche. Pour jouer, les participants doivent déposer" +
                " leur carte de la même couleur que l'atout ou une carte identique à la DERNIÈRE ayant été posée. Quand il la dépose," +
                " le joueur doit dire le nom du joueur qu'il attaque. Chaque fois qu'une carte est déposée, tout le monde compte à haute" +
                " voix jusque 5. N'importe quel joueur peut joueur avant d'être arrivé à 5. Lorsque plus personne ne sait jouer, le dernier" +
                " joueur à avoir été attaqué perd la manche. Il faut ensuite compter le nombre de cartes jouées. Vous devez additioner à" +
                " ce nombre l'étage de la pyramide. Enfin, ce nombre doit être divisé par le palier (le palier est un nombre choisi en début de partie," +
                " plus il est bas, plus vous allez boire). Le nombre final (arrondi à l'unité du bas) que vous obtenez" +
                " est le nombre d'afond que le perdant doit boire. \n" +
                " ATTENTION: lorsqu'un joueur n'a plus de carte, on ne peut pas l'attaquer. Si un joueur se trompe et dit son nom, il" +
                " doit automatiquement boire un afond et la manche continue !!\n" +
                " EXEMPLE : Henri, Lépold et Édouard commencent la partie. La première carte retournée est le 5 de coeur. " +
                " L'atout de la manche est donc coeur. Henri dépose son roi de coeur et attaque Léopold. Les joueurs comptent: 1.., 2..," +
                " 3.. Ensuite Édouard dépose son roi de pic et attaque Henri. Henri dépose son 3 de coeur et attaque Édouard. Édouard dépose son " +
                " 3 de carreau et son 3 de pic et attaque Léopold (à ce moment Édouard n'a plus de carte, on ne peut donc plus l'attaquer !). Ils comptent jusque 5" +"" +
                " - la partie est finie. Six cartes ont été jouées, ils sont" +
                " au 1e étage de la pyramide donc 6+1 = 7. Comme ils sont 3 joueurs, ils ont choisi un palier à 5. Donc 7/5 = 1,4. Léopold doit boire un affond.\n \n",
                "Jeu de cartes","https://cdn4.iconfinder.com/data/icons/celiac-disease-signs-and-symptoms/191/celiac-disease-005-512.png");
        AddJeux(pyramide_roofkot);

        Jeux pyramide = new Jeux("Pyramide", "Disposez les cartes en forme de pyramide. Distribuez ensuite" +
                " toutes les cartes restantes aux joueurs (ou un nombre que vous décidez-vous même). Chaque joueur peut regarder" +
                " ses cartes avant que la partie commence. Il doit les mémoriser puis les aligne devant lui face retournée (souvenez-vous" +
                " où se trouve chacune de vos cartes). Chaque étage de la pyramide représente un nombre de gorgée: les cartes à la base représente une gorgée," +
                " les cartes de la 2e rangée deux gorgées, etc ... La partie commence quand la première carte est retournée. Chaque joueur peut en attaquer un autre." +
                " Le joueur attaqué doit alors dire si le joueur qui l'attaque ment ou pas. S'il choisit de le croire, il boit la quantité initiale (cfr étage de la pyramide)." +
                " Dans le cas contraire, l'attaquant devra montrer sa carte pour prouver qu'il possède bien une carte identique à celle retournée. Si le joueur n'a pas menti, " +
                " le joueur attaqué doit boire le double de la quantité initiale. Et enfin, si le joueur a effectivement bluffé, c'est lui qui doit boire le double de la quantité initiale.\n" +
                "ATTENTION : quand un joueur montre une de ses cartes pour prouver qu'il ne ment pas, il n'a pas le droit de se tromper de carte. Sans quoi " +
                "c'est lui qui devra boire (même s'il possède la bonne carte). Il est donc primordial de bien se souvenir de la position de ses cartes !\n" +
                " Une fois que toutes les cartes de la pyramide ont été retournées, chaque joueur doit dire quelles cartes il possède et à quelle place chacune se trouve." +
                " À chaque erreur, il boit un affond !\n \n"
                ,"Jeu de carte","https://upload.wikimedia.org/wikipedia/commons/thumb/1/18/All_Gizah_Pyramids-2.jpg/300px-All_Gizah_Pyramids-2.jpg");

        AddJeux(pyramide);

        Jeux beerpong = new Jeux("Beerpong", "Remplissez des verres et disposez les en forme de pyramide (comme sur la photo) aux extrémités d'une table. " +
                "Formez deux équipes. Le but du jeu est de lancer la balle dans les verres de l'équipe adverse. Lorsqu'un joueur y parvient, laissez le verre à sa place " +
                "et redonnez la balle à ce même joueur. Tant qu'il parvient à rentrer la balle dans vos verres, il peut continuer à jouer. Quand il rate, le dernier joueur " +
                "de votre équipe à avoir joué doit affoner tous vos verres dans lesquels la balle est tombée. La première équipe qui n'a plus de verre a perdu et doit boire les verres restants de l'équipe adverse. \n"+
                "ATTENTION : Si le joueur lance trois fois d'affilé la balle dans le même verre, la partie est finie et son équipe gagne ! Si le joueur lance deux fois la balle dans le même verre (et qu'il rate sa troisième chance " +
                "ou tire dans un autre verre), l'équipe doit boire deux verres se situant sur la même ligne.\n \n",

        "Jeu de balle", "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f1/Beer_pong_table.jpg/220px-Beer_pong_table.jpg");

        AddJeux(beerpong);

        Jeux cercles_infernaux = new Jeux("Les cercles infernaux", "Formez un premier grand cercle de carte, face cachée. Ensuite, " +
                "formez à l'intérieur un deuxième cerlce de carte. Enfin, déposez les cartes restantes au centre de ces deux cercles. "+
                "Le but du jeu est de deviner la couleur (coeur, trèfle, carreau ou pic) de chaque carte. Un joueur commence en piochant une "+
                "carte (celle qu'il veut). Si il se trompe il doit boire une gorgée si la carte appartenait au cercle extérieur, deux gorgées "+
                "si elle appartenait au cercle intérieur et enfin un affond si la carte se trouvait au centre des deux cercles. Si le joueur trouve "+
                "la couleur de la carte, c'est à son voisin de gauche de continuer. Chaque fois qu'un joueur trouve la couleur de la carte, "+
                "les gorgées qu'il aurait du boire, sont ajoutées à la 'pile de gorgées'. Le premier joueur à perdre doit boire toute cette pile.\n" +
                "Exemple : Constantin pioche une carte au centre des cerlces et trouve sa couleur. C'est donc à son voisin Léopold de jouer. Il pioche une carte "+
                "du cerlce intérieur. Si il se trompe, il devra boire un affond et deux gorgées (carte de Constantin + la sienne). Si il trouve la couleur "+
                "de la carte, c'est au tour de son voisin Henri de jouer. Si Henri se trompe, il devra, en plus des gorgées de sa carte, boire un "+
                "affond et deux gorgées !\n \n","Jeu de cartes","https://upload.wikimedia.org/wikipedia/commons/thumb/8/82/Carte_da_gioco1.jpg/1200px-Carte_da_gioco1.jpg");
        AddJeux(cercles_infernaux);


        Jeux bon_chemin = new Jeux("Le bon chemin", "Ce jeu se joue normalement à deux mais peut être modifié pour jouer à plus. Disposez cinq lignes de "+
                "cinq cartes, face cachée. Le but du jeu est de choisir pour chaque ligne une carte sans que celle-ci ne contiennent de figure (roi, dame, valet) ou toutes "+
                "autes cartes définies en début de partie (par exemple aucun nombre (im)paire). La difficulté du jeu dépendant bien entendu du nombre de cartes 'interdites'. "+
                "Quand le joueur se trompe, il doit boire une gorgée si la carte se trouvait sur la première ligne, deux gorgées si elle se trouvait sur la deuxième ligne, etc... "+
                "Si la carte se trouvait sur la quatrième ligne, il doit boire un demi-affond et un affond si elle se trouvait sur la dernière ligne. Après chaque erreur, le joueur "+
                "recommence à la première ligne et les anciennes cartes sont remplacées afin de toujours avoir un carré de 25 cartes. Quand le joueur trouve une carte valide sur "+
                "la dernière rangée, il peut donner un affond. Si il trouve le 'bon chemin' du premier coup, il peut donner deux affonds !\n \n",
                "Jeu de cartes","https://download.vikidia.org/vikidia/fr/images/f/f6/Euchre.jpg");
        AddJeux(bon_chemin);

        Jeux beerpong_allemand = new Jeux("Beerpong allemand", "Déposez trois bières (idéalement des bouteilles) aux deux extrémités d'une table et un verre au centre. "+
                "Formez deux équipes. Le but du jeux est de finir ses bières avant l'équipe adverse. Le premier joueur commence en lançant la balle sur les bières de l'équipe " +
                "adverse. Si celui-ci touche une des bouteilles, il a le droit de boire sa bière tant que les joueurs de l'équipe adverse n'a pas déposé la balle dans le verre au "+
                "centre de la table. Dès que la balle est dans le verre, il doit arrêter de boire. C'est ensuite au tour de l'équipe adverse de jouer. Si la balle ne touche pas de bouteille, "+
                "le lanceur ne peut pas boire et c'est au tour de l'autre équipe de jouer.", "Jeu de balle", "https://upload.wikimedia.org/wikipedia/en/thumb/b/ba/Flag_of_Germany.svg/1200px-Flag_of_Germany.svg.png");
        AddJeux(beerpong_allemand);

        Jeux autoroute = new Jeux ("Autoroute","Ce jeu se joue normalement à deux. Un joueur aligne cinq cartes face visible. Pour chaque carte, l'autre joueur doit prédire "+
                "si la prochaine carte du tas sera plus grande, moins grande ou égale (il doit alors dire 'poteau') à celle visible. Si le jouer a fait le bon choix, il passe à la carte suivante. Sinon, il boit une gorgée "+
                "si il s'est trompé pour la première carte, deux gorgées pour la deuxième, etc... Il doit boire un demi affond pour la quatrième carte et un affond pour la cinquièmre. À chaque erreur, "+
                "le joueur recommence à la première carte. Si jamais il prédit que la carte sera supérieur/inférieur à la carte visible et que celle-ci est identique, il doit automatiquement boire "+
                "un affond. Chaque carte 'prédite' remplace l'ancienne. Quand le joueur prédit correctement les cinq cartes, changez les rôles.\n" +
                "Exemple : Henri commence par aligner cinq cartes visibles. La première carte est le 3 de pic. Édouard, pas très futé, prédit que la prochaine carte sera plus haute. " +
                "Pas de bol pour lui, c'est un 8 de coeur. Édouard boit une gorgée et recommence (cette fois ci la première carte est le 8 de coeur !). Il prédit que la carte sera plus haute. "+
                "Bien vu ! C'est une dame de pic. Il peut passer à la deuxième carte...\n \n", "Jeu de carte",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d7/A83_aux_Essarts_%28vue_2%2C_13_septembre_2015%2C_%C3%89duarel%29.jpg/1200px-A83_aux_Essarts_%28vue_2%2C_13_septembre_2015%2C_%C3%89duarel%29.jpg");
        AddJeux(autoroute);

        Jeux dealer = new Jeux ("Dealer", "Un premier joueur commence avec le paquet de carte en main. Il demande à son voisin de gauche de prédire la prochaine carte. Celui-ci dit un nombre. "+
                "Le joueur avec le paquet lui dit alors si la carte est au dessus/en dessous. Le joueur a alors une deuxième chance pour trouver la carte. Si il la trouve du "+
                "premier coup, le joueur avec le paquet doit boire un affond, si il la trouve du deuxième coup, le joueur avec le paquet boit un demi affond. Si jamais le joueur ne trouve pas la carte, il "+
                "doit boire la différence entre sa deuxième proposition et la carte (donc si son deuxième choix était un 10 et que la carte est un 8, il boit deux gorgées). Alignez toutes les cartes les unes à "+
                "côté des autres par ordre de grandeur afin de savoir quelles cartes sont déjà sorties. Quand les quatre cartes d'une même valeur sont sorties, tout le monde doit mettre son pouce sur le bord de la table. Le denier joueur "+
                "à mettre son pouce boit un affond. Après trois erreurs d'affilées, le joueur avec le paquet de carte le donne au dernier à s'être trompé (ou en fonction des "+
                "variantes à son voisin de gauche, à la personne qu'il veut, etc...).", "Jeu de carte", "https://upload.wikimedia.org/wikipedia/commons/0/0a/Deck_of_cards_used_in_the_game_piquet.jpg");
        AddJeux(dealer);

        Jeux citron = new Jeux ("Citron", "Un joueur a le paquet de carte, c'est le 'maitre du jeu'. Pour chaque joueur, il lui demande de prédire si la carte sera rouge ou noire. À chaque fois le maitre du jeu donne la carte "+
                "au joueur. En cas de bonne réponse, le joueur peut donner une gorgée à n'importe quel participant. Sinon il en boit une. Une fois que tous les joueurs ont une carte, le maitre du jeu demande à chaque joueur de prédire si la prochaine "+
                "carte sera plus grande ou plus petite que celle qu'il a déjà. En cas de bonne réponse, il peut donner deux gorgées. Sinon il en prend deux (sauf si la carte est identique à la première. Il doit alors prendre un affond !). Ensuite, "+
                "le maitre de jeu demande à nouveau à tous les joueurs de prédire si la carte se trouve à l'intérieur de leur deux premières ou à l'extérieure. En cas de bonne/mauvaise réponse le joueur donne/prend trois gorgées. "+
                "Enfin, le maitre du jeu fait un dernier tour où les joueurs doivent prédire la couleur (pic, coeur, carreau ou trèfle) de la carte. En cas de bonne/mauvaise réponse le joueur donne/prend quatre gorgées. "+
                "Chaque joueur a donc quatre cartes devant lui. Le maitre du jeu, dispose en deux rangées les cartes restantes (ou un nombre au choix) au milieu de la table. Une rangée de carte sera les cartes 'donner' et l'autre "+
                "sera les cartes 'prendre'. Pour chacune des rangées, attribuez un nombre de gorgée pour chaque carte (par exemple une, deux, trois gorgées et affond). Retournez une à une les cartes des différentes rangées (en alternant) "+
                "Si vous possédez dans les quatre cartes devant vous une carte qui se situe dans la rangée prendre/donner vous devez prendre/donner le nombre de gorgée choisi.\n \n", "Jeu de carte",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f6/Citron.jpg/1200px-Citron.jpg");
        AddJeux(citron);

        Jeux donner_prendre = new Jeux("Donner et prendre", "Pour ce jeu, vous avez besoin de deux dés (idéalement de couleur distincte). Un dé sera le dé 'prendre' et l'autre sera le dé 'donner'. Chaque participant lance les dés. "+
                "Le nombre de gorgée à prendre et à attribuer sont donnés par les résultats des dés. Si vous ne comprenez pas les règles de ce jeu, vous n'êtes vraiment pas le couteau le plus aiguisé du tiroir !\n \n"
                , "Jeu de dés", "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Dices1-1.png/220px-Dices1-1.png");
        AddJeux(donner_prendre);

        Jeux bataille = new Jeux("Bataille", "Distribuez toutes les cartes du paquet aux joueurs. Ensuite chaque joueur tire la première de ses cartes. Celui avec la carte la plus haute gagne. Tous les perdants doivent boire une gorgée. "+
                "La partie se finit quand un joueur possède toutes les cartes.\n \n", "Jeu de carte", "https://upload.wikimedia.org/wikipedia/commons/a/af/Theodoor_Rombouts_-_Joueurs_de_cartes.jpg");
        AddJeux(bataille);
    }

}
