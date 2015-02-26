package com.dexafree.materialList.cards.internal;

import android.content.Context;
import android.util.AttributeSet;

import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.HexImageCard;

public class HexBigImageCardItemView<T extends HexImageCard> extends BaseCheckedHexCardItemView<T>{
    public HexBigImageCardItemView(Context context) {
        super(context);
    }

    public HexBigImageCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HexBigImageCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
