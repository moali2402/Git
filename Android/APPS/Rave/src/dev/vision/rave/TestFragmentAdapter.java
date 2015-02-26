package dev.vision.rave;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
public class TestFragmentAdapter extends FragmentPagerAdapter{
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
	  
	  @Override
	    public CharSequence getPageTitle(int position) {
		  if(position==0)
	        return "Social";
		  else return "Messaging";
	        	
	    }

}