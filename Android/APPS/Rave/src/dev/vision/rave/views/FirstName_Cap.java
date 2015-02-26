package dev.vision.rave.views;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.TextView;

public class FirstName_Cap extends TextView {


	private boolean invert;
	private boolean cap;

	public FirstName_Cap(Context context) {
		super(context);
	}

	public FirstName_Cap(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.text);
		set(a);
	}

	public FirstName_Cap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.text);
		set(a);
	}
	
	private void set(TypedArray a) {
		invert = a.getBoolean(R.styleable.text_invert, false);
		cap = a.getBoolean(R.styleable.text_cap, false);
		reColor();
		a.recycle();
	}

	@Override
	public void setText(CharSequence text, BufferType bf) {
		reColor();
		String tx = text.toString();
		if(cap && text.length()>0 && tx.contains(" ")){
			String[] n = tx.split(" ",2);
			Spanned s = Html.fromHtml("<b>"+n[0]+"</b> "+ n[1]);
			super.setText(s, bf);
		}else{
			super.setText(text, bf);
		}
	}

	private void reColor() {
		if(invert){
			setTextColor(DataHolder.Text_Colour);
		}		
	}

}
