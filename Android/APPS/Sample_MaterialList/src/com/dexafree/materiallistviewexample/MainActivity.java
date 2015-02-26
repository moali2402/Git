package com.dexafree.materiallistviewexample;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.controller.OnDismissCallback;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.cards.SimpleCard;
import com.dexafree.materialList.cards.BasicButtonsCard;
import com.dexafree.materialList.cards.BasicImageButtonsCard;
import com.dexafree.materialList.cards.BasicListCard;
import com.dexafree.materialList.cards.BigImageButtonsCard;
import com.dexafree.materialList.cards.BigImageCard;
import com.dexafree.materialList.cards.SmallImageCard;
import com.dexafree.materialList.cards.WelcomeCard;
import com.dexafree.materialList.cards.HexImageCard;
import com.dexafree.materialList.view.IMaterialView;
import com.dexafree.materialList.view.MaterialListView;


public class MainActivity extends ActionBarActivity {
    private Context mContext;
    private IMaterialView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mListView = (IMaterialView) findViewById(R.id.material_listview);
        mListView.setCardAnimation(MaterialListView.CardAnimation.SWING_BOTTOM_IN);

        fillArray();

        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(Card card, int position) {
                //Toast.makeText(mContext, "CARD NUMBER "+position+" dismissed", //Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fillArray() {
        for (int i = 0; i < 35; i++) {
            Card card = getRandomCard(i);
            mListView.add(card);
        }
    }

    private Card getRandomCard(final int position) {
        String title = "My Profile " + (position + 1);
        String description = "Status";

        int type = position % 1;

        SimpleCard card;
        Drawable icon;

        switch (type) {

            
            case 0:
            	card = new HexImageCard(this);
                card.setDescription(description);
                card.setTitleColor(Color.BLACK);
                card.setTitle(title);
                //card.setDrawable(R.drawable.photo);
                card.setDismissible(true);
                card.setDrawable("https://assets-cdn.github.com/images/modules/logos_page/GitHub-Mark.png");
                return card;

            default:
                card = new BigImageButtonsCard(this);
                card.setDescription(description);
                card.setTitle(title);
                card.setDrawable(R.drawable.photo);
                ((BigImageButtonsCard) card).setLeftButtonText("ADD CARD");
                ((BigImageButtonsCard) card).setRightButtonText("RIGHT BUTTON");

                if (position % 2 == 0) {
                    ((BigImageButtonsCard) card).setDividerVisible(true);
                }

                ((BigImageButtonsCard) card).setOnLeftButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Log.d("ADDING", "CARD");

                        mListView.add(generateNewCard());
                        Toast.makeText(mContext, "Added new card", Toast.LENGTH_SHORT).show();
                    }
                });

                ((BigImageButtonsCard) card).setOnRightButtonPressedListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {
                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                    }
                });
                card.setDismissible(true);


                return card;

        }

    }

    private Card generateNewCard() {
        SimpleCard card = new BasicImageButtonsCard(this);
        card.setDrawable(R.drawable.dog);
        card.setTitle("I'm new");
        card.setDescription("I've been generated on runtime!");

        return card;
    }
}
