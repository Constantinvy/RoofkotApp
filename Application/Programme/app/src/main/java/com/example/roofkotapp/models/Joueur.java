package com.example.roofkotapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Joueur implements Parcelable {

    private String nom;
    private String prenom;
    private String cat;
    private String image; // On stocke le chemin de la photo

    public Joueur(String nom, String prenom, String cat, String image){
        this.nom = nom;
        this.prenom = prenom;
        this.cat = cat;
        this.image = image;
    }

    protected Joueur(Parcel in) {
        nom = in.readString();
        prenom = in.readString();
        cat = in.readString();
        image = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(prenom);
        dest.writeString(cat);
        dest.writeString(image);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Joueur> CREATOR = new Creator<Joueur>() {
        @Override
        public Joueur createFromParcel(Parcel in) {
            return new Joueur(in);
        }

        @Override
        public Joueur[] newArray(int size) {
            return new Joueur[size];
        }
    };

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public void setImage(String image){
        this.image = image;
    }

    public String getNom(){return this.nom;}

    public String getPrenom(){return this.prenom;}

    public String getCat(){return this.cat;}

    public String getImage(){return this.image;}

    @Override
    public String toString() {
        return "Joueur{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", cat='" + cat + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
