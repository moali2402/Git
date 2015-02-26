/**
 * 
 */
package dev.vision.rave.views;

import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Mo
 *
 */
public class ColorFull_TextView extends TextView {
	
	private boolean left;

	/**
	 * @param context
	 */
	public ColorFull_TextView(Context context) {
		super(context);
		setBackgroundColor(DataHolder.APP_BACKGROUND);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ColorFull_TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrabuites(attrs);
		setBackgroundColor(DataHolder.APP_BACKGROUND);

	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ColorFull_TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getAttrabuites(attrs);
		setBackgroundColor(DataHolder.APP_BACKGROUND);

	}
	
	void getAttrabuites(AttributeSet attrs){
	    TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.Tab);
		left = a.getBoolean(R.styleable.Tab_left, true);
		a.recycle();
	}
	
	@SuppressLint("NewApi") 
	@Override
	public void setBackgroundColor(int color){

		RoundRectShape rc = new RoundRectShape(new float[] { left ? 30: 0, left ? 30: 0, left ? 0: 30, left ? 0: 30, 0, 0, 0, 0 }, null, null);
		ShapeDrawable selected = new ShapeDrawable(rc);
		selected.getPaint().setColor(color);

		ShapeDrawable unselected = new ShapeDrawable(rc);
		unselected.getPaint().setColor(Color.parseColor("#90ffffff"));

		StateListDrawable states = new StateListDrawable();
		states.addState(new int[] {android.R.attr.state_pressed},selected);
		states.addState(new int[] {android.R.attr.state_selected},selected);
		//ColorDrawable cd = new ColorDrawable(Color.parseColor("#90ffffff"));
		states.addState(new int[] {},unselected);
		
		if(Build.VERSION.SDK_INT > 15)
		super.setBackground(states);
		else
		super.setBackgroundDrawable(states);
	}

}
