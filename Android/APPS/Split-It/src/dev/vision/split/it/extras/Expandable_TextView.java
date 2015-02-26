package dev.vision.split.it.extras;

import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView.BufferType;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
/**
 * User: Bazlur Rahman Rokon
 * Date: 9/7/13 - 3:33 AM
 */
public class Expandable_TextView extends GillsansTextView {
    

	private static final int DEFAULT_TRIM_LENGTH = 300;
    private static final String ELLIPSIS = ".....";

    private CharSequence originalText;
    private CharSequence trimmedText;
    private BufferType bufferType;
    private boolean trim = true;
    private int trimLength = DEFAULT_TRIM_LENGTH;

    public Expandable_TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.trimLength = DEFAULT_TRIM_LENGTH;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                trim = !trim;
                setText();
                //requestFocusFromTouch();
            }
        });
    }
    
    public Expandable_TextView(Context context) {
        this(context, null);
		this.trimLength = DEFAULT_TRIM_LENGTH;

    }

    public Expandable_TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0); 
		this.trimLength = DEFAULT_TRIM_LENGTH;

    }
   

    private void setText() {
        super.setText(getDisplayableText(), bufferType);
    }

    private CharSequence getDisplayableText() {
        return trim ? trimmedText : originalText;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        originalText = text;
        trimmedText = getTrimmedText(text);
        bufferType = type;
        setText();
    }

    private CharSequence getTrimmedText(CharSequence text) {
        if (originalText != null && originalText.length() > trimLength) {
        	
            return new SpannableStringBuilder(originalText, 0, trimLength).append(ELLIPSIS);
        } else {
            return originalText;
        }
    }

    public CharSequence getOriginalText() {
        return originalText;
    }

    public void setTrimLength(int trimLength) {
        this.trimLength = trimLength;
        trimmedText = getTrimmedText(originalText);
        setText();
    }

    public int getTrimLength() {
        return trimLength;
    }
}
