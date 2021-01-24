package com.example.roofkotapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Regle  implements Parcelable {

    private String nom;
    private String description;
    private String difficulte;

    public Regle(String nom, String difficulte, String description){
        this.nom = nom;
        this.description = description;
        this.difficulte = difficulte;
    }

    protected Regle(Parcel in) {
        nom = in.readString();
        description = in.readString();
        difficulte = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(description);
        dest.writeString(difficulte);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Regle> CREATOR = new Creator<Regle>() {
        @Override
        public Regle createFromParcel(Parcel in) {
            return new Regle(in);
        }

        @Override
        public Regle[] newArray(int size) {
            return new Regle[size];
        }
    };

    public void setNom(String new_nom){
        this.nom = new_nom;
    }

    public void setDescription(String new_description){
        this.description = new_description;
    }

    public void setDifficulte(String new_difficulte){
        this.difficulte = new_difficulte;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription(){
        return this.description;
    }

    public String getDifficulte(){
        return this.difficulte;
    }




}

