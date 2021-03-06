package com.recipewizard.recipewizard;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.os.Parcel;
import android.widget.CheckBox;


/**
 * Created by Brian on 11/17/2016.
 */

class Ingredient_old implements Parcelable{

    private String name = new String ();
    private Bitmap picture;
    private boolean checked;
    private String category = new String();

    Ingredient_old(String name, Bitmap picture, boolean checked, String category){
        this.name = name;
        this.picture = picture;
        this.checked = checked;
        this.category = category;
    }
    Ingredient_old(Parcel parcel){
        this.name = parcel.readString();
        this.picture = (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader());
        this.checked = parcel.readByte() != 0;
        this.category = parcel.readString();
    }

    public String getName () {return name;}
    public void setName (String name) {this.name = name;}
    public Bitmap getPicture () {return picture;}
    public void setPicture (Bitmap picture) {this.picture = picture;}
    public boolean getCheckedStatus() {
        return checked;
    }
    public void setCheckedStatus(boolean checked) {
        this.checked = checked;
    }
    public String getCategory() {return category;}
    public void setCategory(String category) {this.category = category;}

    public String toString() {
        return name + "\n" + checked + "\n" + category + "\n";
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeValue(picture);
        dest.writeByte((byte) (checked ? 1 : 0));
        dest.writeString(category);
    }

    public static Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }
        @Override
        public Ingredient [] newArray(int size) {
            return new Ingredient  [size];
        }
    };
}
