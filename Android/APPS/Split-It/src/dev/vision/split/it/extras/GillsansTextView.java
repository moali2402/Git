package dev.vision.split.it.extras;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class GillsansTextView extends TextView {

	/*
	 * Caches typefaces based on their file path and name, so that they don't have to be created every time when they are referenced.
	 */
	private static Typeface mTypeface;
	
	public GillsansTextView(final Context context) {
	    this(context, null);
	}
	
	public GillsansTextView(final Context context, final AttributeSet attrs) {
	    this(context, attrs, 0);
	}
	
	public GillsansTextView(final Context context, final AttributeSet attrs, final int defStyle) {
	    super(context, attrs, defStyle);
	
	     if (mTypeface == null) {
	         mTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/gillsans.ttf");
	     }
	     setTypeface(mTypeface);
	}
	
	
}