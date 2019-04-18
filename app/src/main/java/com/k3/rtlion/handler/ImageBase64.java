package com.k3.rtlion.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class ImageBase64 {

    public Bitmap getImage(String base64img){
        try {
            byte[] decodedBytes = Base64.decode(base64img, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
