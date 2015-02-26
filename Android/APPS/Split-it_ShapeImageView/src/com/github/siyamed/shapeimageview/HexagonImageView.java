package com.github.siyamed.shapeimageview;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.shader.ShaderHelper;
import com.github.siyamed.shapeimageview.shader.SvgShader;

public class HexagonImageView extends ShaderImageView {

    public HexagonImageView(Context context) {
    	
        super(context);
    }

    public HexagonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexagonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public ShaderHelper createImageViewHelper() {
    	this.setDrawingCacheEnabled(true);
        return new SvgShader(R.raw.imgview_hexagon);
    }
    
    
    public void setOverlayEnabled(boolean b){
    	setOverlay(b);
    }
    
    
    public android.graphics.Bitmap get(){
    	   return this.getDrawingCache();
    }
    
    public ShaderHelper getPathH(){
    	return getPathHelper();
    }

}
