package com.example.roofkotapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Jeux implements Parcelable {

    private String nom;
    private String description;
    private String type;    // Type du jeux : jeux de cartes, jeux de dés, etc..
    private String image;

    public Jeux(String nom, String description, String type, String image) {
        this.nom = nom;
        this.description = description;
        this.type = type;
        this.image = image;
    }

    protected Jeux(Parcel in) {
        nom = in.readString();
        description = in.readString();
        type = in.readString();
        image = in.readString();
    }

    public static final Creator<Jeux> CREATOR = new Creator<Jeux>() {
        @Override
        public Jeux createFromParcel(Parcel in) {
            return new Jeux(in);
        }

        @Override
        public Jeux[] newArray(int size) {
            return new Jeux[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nom);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(image);
    }


    public String getNom() {
        return nom;
    }

    public String getDescription(){
        return description;
    }

    public String getType(){return type;}

    public String getImage() {
        return image;
    }

    public void setNom(String new_name){
        this.nom = new_name;
    }

    public void setDescription(String new_description){
        this.description = new_description;
    }

    public void setType(String new_type){ this.type = new_type;}

    public  void setImage(String new_image){
        this.image = new_image;
    }
}