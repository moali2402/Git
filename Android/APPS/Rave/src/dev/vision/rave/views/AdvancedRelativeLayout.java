/**
 * 
 */
package dev.vision.rave.views;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * @author Mo
 *
 */
public class AdvancedRelativeLayout extends RelativeLayout {

	private boolean invert;
	
	public AdvancedRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.text);
		set(a);
	}

	public AdvancedRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.text);
		set(a);
	}

	public AdvancedRelativeLayout(Context context) {
		super(context);
	}
	

	private void set(TypedArray a) {
		invert = a.getBoolean(R.styleable.text_invert, false);
		a.recycle();
		reColor();
	}
	
	public void reColor(){
		ColorDrawable cd;
		if(invert){
			cd= new ColorDrawable(DataHolder.Text_Colour);
		}else{
			cd= new ColorDrawable(DataHolder.APP_BACKGROUND);
		}
		cd.setAlpha(220);	
		setBackgroundColor(cd.getColor());

	}

}
