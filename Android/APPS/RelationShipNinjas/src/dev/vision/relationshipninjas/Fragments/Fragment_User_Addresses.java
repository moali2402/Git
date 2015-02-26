package dev.vision.relationshipninjas.Fragments;

import java.util.HashSet;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.API.API;
import dev.vision.relationshipninjas.purchase.Address;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 class Fragment_User_Addresses extends ListFragment {
        private LayoutInflater lf;
		private HashSet<Address> addresses;
        

		@Override
        public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
        	lf = getLayoutInflater(savedInstanceState);
        	getListView().setDivider(getResources().getDrawable(android.R.color.transparent));
        	getListView().setDividerHeight(20);
        	addresses= API.user.getAddressesList();
        	setListAdapter(new AddressesAdapter());
        }
        
        public class AddressesAdapter extends BaseAdapter {

    		@Override
    		public int getCount() {
    			return addresses.size();
    		}

    		@Override
    		public Address getItem(int po) {
    			return (Address) addresses.toArray()[po];
    		}

    		@Override
    		public long getItemId(int po) {
    			return po;
    		}

    		@Override
    		public View getView(int p, View arg1, ViewGroup arg2) {
    			View v = lf.inflate(R.layout.user_addresses_item, null);
    			TextView t = (TextView) v.findViewById(R.id.textView1);
    			ImageView i = (ImageView) v.findViewById(R.id.imageView1);
    			Address add =getItem(p);
    			t.setText(add.getSpanned());
    			if(add.isDefaultbilling() || add.isDefaultshipping()){
    				i.setBackgroundResource(R.drawable.abc_list_selector_disabled_holo_dark);;
    				if(add.isDefaultbilling() && add.isDefaultshipping())
    					i.setImageResource(R.drawable.defaultshipping);
    			}else{
    				i.setImageResource(0);
    				i.setBackgroundResource(0);
    			}
    			return v;
    		}

    	}
    }