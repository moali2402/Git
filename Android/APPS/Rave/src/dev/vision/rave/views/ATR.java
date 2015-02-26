package dev.vision.rave.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class ATR extends TextView {

/*
 * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
 */
private static Typeface mTypeface;

public ATR(final Context context) {
    this(context, null);
}

public ATR(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
}

public ATR(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

     if (mTypeface == null) {
         mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/My Boyfriend's Handwriting.ttf");
     }
     setTypeface(mTypeface);
}

}