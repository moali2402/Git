package com.dexafree.materialList.cards.internal;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.dexafree.materialList.cards.SelectiveCard;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.R;
import com.squareup.picasso.Picasso;

public abstract class BaseCheckedHexCardItemView<T extends SelectiveCard> extends BaseCardItemView<T> {
    public BaseCheckedHexCardItemView(Context context) {
        super(context);
    }

    public BaseCheckedHexCardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BaseCheckedHexCardItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void build(final T card) {
		super.build(card);

		// Left Button - Text
        final ImageView leftText = (ImageView) findViewById(R.id.left_text_button);
        leftText.setImageResource(card.getLeftButtonIcon());
        leftText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                card.getOnLeftButtonPressedListener().onButtonPressedListener(leftText, card);
            }
        });

        // Right Button - Text
        final ImageView rightText = (ImageView) findViewById(R.id.right_text_button);
        rightText.setImageResource(card.getRightButtonIcon());
        rightText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                card.getOnRightButtonPressedListener().onButtonPressedListener(rightText, card);
            }
        });

        // Title
        TextView title = (TextView) findViewById(R.id.titleTextView);
        title.setText(card.getTitle());
        if (card.getTitleColor() != -1) {
            title.setTextColor(card.getTitleColor());
        }

        // Description
        TextView description = (TextView) findViewById(R.id.descriptionTextView);
        description.setText(card.getDescription());
        if (card.getDescriptionColor() != -1) {
            description.setTextColor(card.getDescriptionColor());
        }

        // Image
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        if (imageView != null) {
            if(card.getUrlImage() == null || card.getUrlImage().isEmpty()) {
                imageView.setImageDrawable(card.getDrawable());
            } else {
                Picasso.with(getContext()).load(card.getUrlImage()).into(imageView);
            }
        }
        
        // Image
        ImageView isdefault = (ImageView) findViewById(R.id.isdefault);
        if (isdefault != null) {
            if(card.isDefault()) {
            	isdefault.setVisibility(View.VISIBLE);;
            } else {
            	isdefault.setVisibility(View.INVISIBLE);
            }
        }
        
        // CheckBox
        CheckBox isdefaultC = (CheckBox) findViewById(R.id.mark);
        if (isdefaultC != null && isdefaultC.getVisibility() == View.VISIBLE) {
            isdefaultC.setChecked(card.isMarked());
            isdefaultC.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					//card.getOnCheckedChangeListener().onCheckListener(isChecked, card);
					card.setMarked(isChecked);
				}
			});
        }

    }

}
