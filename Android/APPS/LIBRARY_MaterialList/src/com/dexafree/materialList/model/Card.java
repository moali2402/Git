package com.dexafree.materialList.model;

/**
 * The Card is the base class of all Card Models.
 */
public abstract class Card {
    private boolean mDismissible;
	private boolean mDefault;

    public boolean isDismissible() {
        return mDismissible;
    }

    public void setDismissible(boolean canDismiss) {
        this.mDismissible = canDismiss;
    }
    
    public boolean isDefault() {
        return mDefault;
    }

    public void setDefault(boolean mDefault) {
        this.mDefault = mDefault;
    }

    public abstract int getLayout();
}
