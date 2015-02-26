package dev.vision.relationshipninjas.Fragments;

import java.util.ArrayList;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Card;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 class Fragment_User_CC extends ListFragment {
        private LayoutInflater lf;
		private ArrayList<Card> ccs;

		@Override
        public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
        	lf = getLayoutInflater(savedInstanceState);
        	getListView().setDivider(getResources().getDrawable(android.R.color.transparent));
        	getListView().setDividerHeight(20);
        	ccs= API.user.getCreditCardList();
        	setListAdapter(new CCAdapter());
        }
        
        public class CCAdapter extends BaseAdapter {

    		@Override
    		public int getCount() {
    			return ccs.size();
    		}

    		@Override
    		public Card getItem(int p) {
    			return ccs.get(p);
    		}

    		@Override
    		public long getItemId(int p) {
    			return p;
    		}

    		@Override
    		public View getView(int p, View arg1, ViewGroup arg2) {
    			View v = lf.inflate(R.layout.user_cc_item, null);
    			TextView t = (TextView) v.findViewById(R.id.textView1);
    			ImageView i = (ImageView) v.findViewById(R.id.imageView1);
    			Card c =getItem(p);
    			t.setText(c.getSpanned());
    			if(c.isDefault()){
    				i.setBackgroundResource(R.drawable.abc_list_selector_disabled_holo_dark);;
					i.setImageResource(R.drawable.defaultcc);
    			}else{
    				i.setImageResource(0);
    				i.setBackgroundResource(0);
    			}
    			return v;
    		}

    	}
    }