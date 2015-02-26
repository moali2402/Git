package dev.vision.split.it;

import java.util.List;

import dev.vision.split.it.extras.IconPagerAdapter;
import dev.vision.split.it.fragments.TitleFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class FragmentAdapter extends FragmentPagerAdapter{
	  private List<TitleFragment> fragments;

	  public FragmentAdapter(FragmentManager fm, List<TitleFragment> fragments) {
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
		  return fragments.get(position).getTitle();
	  }

	 
}