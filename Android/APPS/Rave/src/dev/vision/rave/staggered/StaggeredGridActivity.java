package dev.vision.rave.staggered;

import static dev.vision.rave.Constants.IMAGES;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import dev.vision.rave.R;

public class StaggeredGridActivity extends Activity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener {

    private static final String TAG = "StaggeredGridActivity";
    public static final String SAVED_DATA_KEY = "SAVED_DATA";

    private StaggeredGridView mGridView;
    private boolean mHasRequestedMore;
    private SampleAdapter mAdapter;

    String[] imageUrls;
    private ArrayList<String> mData;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgv);
        imageUrls = IMAGES;
        setTitle("SGV");
        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        LayoutInflater layoutInflater = getLayoutInflater();

        View header = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        View footer = layoutInflater.inflate(R.layout.list_item_header_footer, null);
        TextView txtHeaderTitle = (TextView) header.findViewById(R.id.txt_title);
        TextView txtFooterTitle =  (TextView) footer.findViewById(R.id.txt_title);
        txtHeaderTitle.setText("THE HEADER!");
        txtFooterTitle.setText("THE FOOTER!");

        mGridView.addHeaderView(header);
        mGridView.addFooterView(footer);
        mAdapter = new SampleAdapter(this, R.id.txt_line1);

        // do we have saved data?
        if (savedInstanceState != null) {
            mData = savedInstanceState.getStringArrayList(SAVED_DATA_KEY);
        }

        //if (mData == null) {
        //    mData = SampleData.generateSampleData();
        //}

        for (String data : imageUrls) {
            mAdapter.add(data);
        }

        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		swingBottomInAnimationAdapter.setAbsListView(mGridView);
		swingBottomInAnimationAdapter.setInitialDelayMillis(300);
		//gridView.setAdapter(swingBottomInAnimationAdapter);

        mGridView.setAdapter(swingBottomInAnimationAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(SAVED_DATA_KEY, mData);
    }

    @Override
    public void onScrollStateChanged(final AbsListView view, final int scrollState) {
        Log.d(TAG, "onScrollStateChanged:" + scrollState);
    }

    @Override
    public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {
        Log.d(TAG, "onScroll firstVisibleItem:" + firstVisibleItem +
                            " visibleItemCount:" + visibleItemCount +
                            " totalItemCount:" + totalItemCount);
        // our handling
        if (!mHasRequestedMore) {
            int lastInScreen = firstVisibleItem + visibleItemCount;
            if (lastInScreen >= totalItemCount) {
                Log.d(TAG, "onScroll lastInScreen - so load more");
                mHasRequestedMore = true;
                onLoadMoreItems();
            }
        }
    }

    private void onLoadMoreItems() {
        //final ArrayList<String> sampleData = SampleData.generateSampleData();
        for (String data : imageUrls) {
            mAdapter.add(data);
        }
        // stash all the data in our backing store
        ///mData.addAll(imageUrls);
        // notify the adapter that we can update now
        mAdapter.notifyDataSetChanged();
        mHasRequestedMore = false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Toast.makeText(this, "Item Clicked: " + position, Toast.LENGTH_SHORT).show();
    }
}