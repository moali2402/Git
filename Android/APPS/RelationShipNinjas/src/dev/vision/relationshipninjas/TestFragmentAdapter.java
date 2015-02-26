package dev.vision.relationshipninjas;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.IconPagerAdapter;

public class TestFragmentAdapter extends FragmentPagerAdapter {
	  private List<Fragment> fragments;


	  public TestFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
		  super(fm);
	      this.fragments = fragments;
	  }

	  @Override
	  public Fragment getItem(int position) {
		  return this.fragments.get(position);
	  }
	  
	  @Override
	  public void destroyItem (ViewGroup container, int position, Object object)
	  {
		  super.destroyItem(container, position, object);
	  }
	  
	  @Override
	  public int getCount() {
		  return this.fragments.size();
	  }

}