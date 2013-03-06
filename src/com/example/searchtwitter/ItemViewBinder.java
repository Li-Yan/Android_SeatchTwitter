package com.example.searchtwitter;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter.ViewBinder;

public class ItemViewBinder  implements ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if((view instanceof ImageView) && (data instanceof Bitmap))
        {
            ImageView iv = (ImageView) view;
            Bitmap bmp = (Bitmap) data;
            iv.setImageBitmap(bmp);
            return true;
        }
        return false;
    }
}
