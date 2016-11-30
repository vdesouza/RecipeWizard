package com.recipewizard.recipewizard;

import android.graphics.Bitmap;

/**
 * Created by Hank on 11/23/2016.
 */


// Just an object to hold the fields together.
public class MiniRecipe {

    private int id;
    private String name;
    private Bitmap img;
    private int usedIng;
    private int missIng;
    private int likes;


    public MiniRecipe(int id, String name, Bitmap img, int usedIng, int missIng, int likes) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.usedIng = usedIng;
        this.missIng = missIng;
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public int getUsedIng() {
        return usedIng;
    }

    public void setUsedIng(int usedIng) {
        this.usedIng = usedIng;
    }

    public int getMissIng() {
        return missIng;
    }

    public void setMissIng(int missIng) {
        this.missIng = missIng;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }
}
