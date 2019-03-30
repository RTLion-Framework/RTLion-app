package com.k3.rtlion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class ImageBase64 {

    public Bitmap getImage(String base64img){
        byte[] decodedString = Base64.decode(base64img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(base64img, 0, base64img.length);
    }
}
