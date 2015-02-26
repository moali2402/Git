package com.kmshack.newsstand;

import java.util.ArrayList;
import java.util.zip.Inflater;

import com.nvanbenschoten.motion.ParallaxImageView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SampleListFragment extends ScrollTabHolderFragment implements OnScrollListener {
    private TiltScrollController mTiltScrollController;

	private static final String ARG_POSITION = "position";

	private ListView mListView;
	private ArrayList<String> mListItems;

	private int mPosition;

	private SensorManager mSensorManager;

	private int mSrollState;

	private ParallaxImageView im;

	public static Fragment newInstance(int position) {
		SampleListFragment f = new SampleListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPosition = getArguments().getInt(ARG_POSITION);

		mListItems = new ArrayList<String>();

		for (int i = 1; i <= 100; i++) {
			mListItems.add(i + ". item - currnet page: " + (mPosition + 1));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, null);

		mListView = (ListView) v.findViewById(R.id.listView);

		View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, null, false);
		placeHolderView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, container.getMeasuredHeight()));
		mListView.addHeaderView(placeHolderView);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mListView.setOnScrollListener(this);
		mListView.setAdapter(new Adapter(getActivity(), R.layout.list_image,R.id.displayName, mListItems));
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        //mTiltScrollController = new TiltScrollController(mSensorManager, new TiltScrollListener());
        //mTiltScrollController.requestAllSensors();
	}

	
	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}

		mListView.setSelectionFromTop(1, scrollHeight);

	}
	
	
	class Adapter extends BaseAdapter{
		int lay;
		int res;
		ArrayList<String> items;
		public Adapter(Context con, int layot , int resid, ArrayList<String> mListItems){
			lay = layot;
			res = resid;
			items = new ArrayList<String>(mListItems);
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return items.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			LayoutInflater inf = LayoutInflater.from(getActivity());
			View v = inf.inflate(lay, null);
			TextView tx = (TextView) v.findViewById(res);
			tx.setText((String) getItem(arg0));
			//ParallaxImageView im = (ParallaxImageView) v.findViewById(R.id.image);
			//im.unregisterSensorManager();
			return v;
		}
		
		
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
		
		for (int i=0; i < visibleItemCount; i++) {
	           
	          
			 View listItem = view.getChildAt(i);
	            if (listItem == null)
	                break;
		
			ParallaxImageView im = (ParallaxImageView) listItem.findViewById(R.id.image);
			  if (im == null)
	                break;
           

			im.unregisterSensorManager();
			
		}
		
			
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		mSrollState = scrollState;
		if(mSrollState == SCROLL_STATE_IDLE){
			int firstVis = mListView.getFirstVisiblePosition(); 
			int lastVis = mListView.getLastVisiblePosition();
	
			/* This is the "conversion" from flat list positions to ViewGroup child positions */
			int count = lastVis - firstVis; 
	
	 
			          
			View listItem = mListView.getChildAt(count - ((int)count/2));
	        if (listItem == null)
	            return;
	
	        
	        im = (ParallaxImageView) listItem.findViewById(R.id.image);
		    
			if (im == null)
	            return;
		    im.setForwardTiltOffset(.35f);
		
			im.registerSensorManager();
		}
	}
	
	
	
	public void scrollTextUp(float mCurrentPosition) {
        mListView.smoothScrollBy(50, 5);
    }

    public void scrollTextBottom(float mCurrentPosition) {
    	mListView.smoothScrollBy(-50, 5);
    }


    @Override
	public void onPause() {
        //mTiltScrollController.killAllSensors();
        super.onPause();
    }

	
	
	public class TiltScrollListener implements ScrollListener {

        
		@Override
		public void onTiltUp(float mCurrentPosition) {
            scrollTextUp(mCurrentPosition);
			
		}

		@Override
		public void onTiltDown(float mCurrentPosition) {
            scrollTextBottom(mCurrentPosition);
			
		}
    }

	class TiltScrollController implements SensorEventListener {

	    private SensorManager mSensorManager;
	    private Sensor mAccelSensor;
	    private Sensor mMagnetSensor;
	    private float mCurrentPosition;
	    private boolean isCurrentPositionSet = false;
	    private int count = 0;
	    private ScrollListener mListener;

	    float[] mMagnetValues      = new float[3];
	    float[] mAccelValues       = new float[3];
	    float[] mOrientationValues = new float[3];
	    float[] mRotationMatrix    = new float[9];

	    public TiltScrollController(SensorManager sensorManager, ScrollListener listener) {
	        mSensorManager = sensorManager;
	        mListener = listener;
	        mAccelSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	        mMagnetSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	    }

	    @Override
	    public void onSensorChanged(SensorEvent event) {
	        switch (event.sensor.getType()) {
	            case Sensor.TYPE_ACCELEROMETER:
	                System.arraycopy(event.values, 0, mAccelValues, 0, 3);
	                break;

	            case Sensor.TYPE_MAGNETIC_FIELD:
	                System.arraycopy(event.values, 0, mMagnetValues, 0, 3);
	                break;
	        }
	        SensorManager.getRotationMatrix(mRotationMatrix, null, mAccelValues, mMagnetValues);
	        SensorManager.getOrientation(mRotationMatrix, mOrientationValues);
	        if (!isCurrentPositionSet) {
	            mCurrentPosition = mOrientationValues[1];
	            count += 1;
	            if(count == 2) {
	                isCurrentPositionSet = true;
	            }
	        }
	        if(mCurrentPosition + 0.25 < mOrientationValues[1]) {
	            //Scroll to Top
	            mListener.onTiltUp(mCurrentPosition);
	        }
	        else if (mCurrentPosition - 0.25 > mOrientationValues[1]) {
	            //Scroll to Bottom
	            mListener.onTiltDown(mCurrentPosition);
	        }
	    };

	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

	    public void requestAllSensors() {
	        mSensorManager.registerListener(this, mAccelSensor, SensorManager.SENSOR_DELAY_NORMAL);
	        mSensorManager.registerListener(this, mMagnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }

	    public void killAllSensors() {
	        mSensorManager.unregisterListener(this, mAccelSensor);
	        mSensorManager.unregisterListener(this, mMagnetSensor);
	    }

	    
	}
	
	public interface ScrollListener {
        public void onTiltUp(float mCurrentPosition);
        public void onTiltDown(float mCurrentPosition);
 }
	 
}