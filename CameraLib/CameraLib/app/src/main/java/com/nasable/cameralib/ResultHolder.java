package com.nasable.cameralib;

import android.support.annotation.Nullable;

/**
 * Created by noras on 10/4/18.
 */

public class ResultHolder {

    private static byte[] image;

    public static void setImage(@Nullable byte[] image) {
        ResultHolder.image = image;
    }

    @Nullable
    public static byte[] getImage() {
        return image;
    }
}
