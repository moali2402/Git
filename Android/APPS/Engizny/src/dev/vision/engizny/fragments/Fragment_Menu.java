package dev.vision.engizny.fragments;

import java.util.ArrayList;

import dev.vision.engizny.CustomAnimation;
import dev.vision.engizny.R;
import dev.vision.engizny.R.layout;
import dev.vision.engizny.adapters.Menu_Adapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_Menu extends ListFragment {
	Menu_Adapter ma;
	ArrayList<BaseFragment> al = new ArrayList<BaseFragment>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.back_menu, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getFragments();
		
		ma = new Menu_Adapter(getActivity(), al);
		 
		setListAdapter(ma);
		/*
		Fragment newContent = al.get(0);
		 
		 if (newContent != null)
			switchFragment(newContent);
			*/
	}
	
	private void getFragments() {
		
		Fragment_Engizny ef = Fragment_Engizny.newInstance();
		Fragment_SuperMarket smf = Fragment_SuperMarket.newInstance();
		
		Fragment_Maintenance mf = Fragment_Maintenance.newInstance();
		Fragment_Restaurants rf = Fragment_Restaurants.newInstance();
		Fragment_Pharmacies pf = Fragment_Pharmacies.newInstance();
		Fragment_Laundry lf = Fragment_Laundry.newInstance();
		Fragment_LIMO lif = Fragment_LIMO.newInstance();
		Fragment_Market maf = Fragment_Market.newInstance();
		Fragment_Vallet vf = Fragment_Vallet.newInstance();

		Fragment_eShopping esf = Fragment_eShopping.newInstance();
		Fragment_OtherServices osf = Fragment_OtherServices.newInstance();

		al.clear();
		
		al.add(ef);
		al.add(smf);

		al.add(mf);
		al.add(rf);
		al.add(pf);
		al.add(lf);
		al.add(lif);
		al.add(maf);
		al.add(vf);


		al.add(esf);
		al.add(osf);

	}

	@Override
	public void onListItemClick(ListView lv, View v, int pos, long id) {
		 ma.setSelecteIndex(pos , v);
		 ((Menu_Adapter)lv.getAdapter()).notifyDataSetChanged();
		 Fragment newContent = al.get(pos);
		 
		 if (newContent != null)
			switchFragment(newContent);
	}
	
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		Activity ac = getActivity();
		if (ac == null)
			return;

		if (ac instanceof CustomAnimation) {
			CustomAnimation ra = (CustomAnimation) ac;
			ra.switchContent(fragment);
		}
	}	
	
}
