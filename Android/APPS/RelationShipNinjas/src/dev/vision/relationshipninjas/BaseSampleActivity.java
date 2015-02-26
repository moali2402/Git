package dev.vision.relationshipninjas;

import java.util.Random;

import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.viewpagerindicator.PageIndicator;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseSampleActivity extends SlidingFragmentActivity {
    private static final Random RANDOM = new Random();
	protected ImageLoader imageLoader = ImageLoader.getInstance();

    protected TestFragmentAdapter mAdapter;
    protected ViewPager mPager;
    protected PageIndicator mIndicator;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

}
