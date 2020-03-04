package com.example.notepad.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Image {

    private ArrayList<String> Bitmaps;


    public Image(ArrayList<String> bitmaps) {
        Bitmaps = bitmaps;
    }

    public ArrayList<String> getBitmaps() {
        return Bitmaps;
    }
}
