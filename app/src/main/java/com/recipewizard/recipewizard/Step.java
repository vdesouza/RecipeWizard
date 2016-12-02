package com.recipewizard.recipewizard;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brian on 12/1/2016.
 */

public class Step implements Parcelable{
    private String direction;
    private List<Ingredient> ingredients = new ArrayList<>();

    Step(String direction, List<Ingredient> ingredients){
        this.direction = direction;
        this.ingredients = ingredients;
    }
    Step(Parcel parcel){
        this.direction = parcel.readString();
        this.ingredients = parcel.createTypedArrayList(Ingredient.CREATOR);
    }

    public String getDirection () {return direction;}
    public void setDirection(String direction) {this.direction = direction;}
    public List<Ingredient> getIngredients () {return ingredients;}
    public void setIngredients (ArrayList<Ingredient> ingredients) {this.ingredients = ingredients;}




    public String toString() {
        return direction + "." + TextUtils.join(".", ingredients);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(direction);
        dest.writeTypedList(ingredients);
    }

    public static Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }
        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
