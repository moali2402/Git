package com.dexafree.materialList.cards;

import android.content.Context;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.dexafree.materialList.events.BusProvider;

/**
 * The ExtendedCard set two Buttons (right and left).
 */
public abstract class SelectiveCard extends SimpleCard {
    protected String leftButtonText;
    protected String rightButtonText;

    protected int leftButtonImage;
    protected int rightButtonImage;
    protected int mRightButtonTextColor = -1;
    protected OnButtonPressListener onLeftButtonPressedListener;
    protected OnButtonPressListener onRightButtonPressedListener;
    protected OnCheckListener onCheckedChangeListener;

    protected boolean dividerVisible = false;
    protected boolean fullWidthDivider = false;
	private boolean isMarked;

    public SelectiveCard(final Context context) {
        super(context);
    }

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public void setLeftButtonText(int leftButtonTextId) {
        setLeftButtonText(getString(leftButtonTextId));
    }

    public void setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
        BusProvider.dataSetChanged();
    }

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(int rightButtonTextId) {
        setRightButtonText(getString(rightButtonTextId));
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
        BusProvider.dataSetChanged();
    }
    
    
    public int getLeftButtonIcon() {
        return leftButtonImage;
    }

    public void setLeftButtonIcon(int leftButtonImage) {
        this.leftButtonImage=leftButtonImage;
        BusProvider.dataSetChanged();
    }

    
    public int getRightButtonIcon() {
        return rightButtonImage;
    }

    public void setRightButtonIcon(int rightButtonImage) {
        this.rightButtonImage = rightButtonImage;
        BusProvider.dataSetChanged();
    }

    public int getRightButtonTextColor() {
        return mRightButtonTextColor;
    }

    public void setRightButtonTextColor(int color) {
        this.mRightButtonTextColor = color;
        BusProvider.dataSetChanged();
    }

    public void setRightButtonTextColorRes(int colorId) {
        setRightButtonTextColor(getResources().getColor(colorId));
    }

    public boolean isDividerVisible() {
        return dividerVisible;
    }

    public boolean isFullWidthDivider() {
        return fullWidthDivider;
    }

    public void setFullWidthDivider(boolean fullWidthDivider) {
        this.fullWidthDivider = fullWidthDivider;
        BusProvider.dataSetChanged();
    }

    public void setDividerVisible(boolean visible) {
        this.dividerVisible = visible;
        BusProvider.dataSetChanged();
    }

    public OnButtonPressListener getOnLeftButtonPressedListener() {
        return onLeftButtonPressedListener;
    }

    public void setOnLeftButtonPressedListener(OnButtonPressListener onLeftButtonPressedListener) {
        this.onLeftButtonPressedListener = onLeftButtonPressedListener;
    }

    public OnButtonPressListener getOnRightButtonPressedListener() {
        return onRightButtonPressedListener;
    }

    public void setOnRightButtonPressedListener(OnButtonPressListener onRightButtonPressedListener) {
        this.onRightButtonPressedListener = onRightButtonPressedListener;
    }
    
    public OnCheckListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
    
    public boolean isMarked(){
		return isMarked;
    }
    
    public void setMarked(boolean b){
		isMarked = b;
    }
}
