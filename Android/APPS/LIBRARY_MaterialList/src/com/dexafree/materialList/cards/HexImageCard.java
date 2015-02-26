package com.dexafree.materialList.cards;

import android.content.Context;
import com.dexafree.materialList.R;

public class HexImageCard extends SelectiveCard {
    public HexImageCard(final Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.material_hexa_image_card_layout;
    }
}
