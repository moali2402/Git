package dev.vision.split.it.extras;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class BoldGillsansTextView extends TextView {

/*
 * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
 */
private static Typeface mTypeface;

public BoldGillsansTextView(final Context context) {
    this(context, null);
}

public BoldGillsansTextView(final Context context, final AttributeSet attrs) {
    this(context, attrs, 0);
}

public BoldGillsansTextView(final Context context, final AttributeSet attrs, final int defStyle) {
    super(context, attrs, defStyle);

     if (mTypeface == null) {
         mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/gillsan_bold.ttf");
     }
     setTypeface(mTypeface);
}

}