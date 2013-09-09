
package com.buddy.sdk.unittests.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class Utils {
    public static byte[] getEncodedImageBytes(Drawable image) {
        byte[] bitmapByte = null;

        if (image != null) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) image);
            Bitmap bitmap = bitmapDrawable.getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);

            bitmapByte = Base64.encode(byteStream.toByteArray(), Base64.DEFAULT);
        }

        return bitmapByte;
    }
}
