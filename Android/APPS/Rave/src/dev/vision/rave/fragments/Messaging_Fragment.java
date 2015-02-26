package dev.vision.rave.fragments;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import dev.vision.rave.Constants;
import dev.vision.rave.DataHolder;
import dev.vision.rave.QuickReturnType;
import dev.vision.rave.R;
import dev.vision.rave.listeners.QuickReturnListViewOnScrollListener;
import dev.vision.rave.messaging.Chat_Activity;
import dev.vision.rave.user.Status;
import dev.vision.rave.views.AutoResizeTextView;
import dev.vision.rave.views.ContactStatusIcon;
import dev.vision.rave.views.HexContactStatusIcon;
import dev.vision.rave.views.InScrollableListView;
import dev.vision.rave.views.SquareText;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class Messaging_Fragment extends ListFragment implements OnItemClickListener {

	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String LAYOUT_ID = "Layout_ID";
	 private View v;
	 private LayoutInflater inflater;
	 private int mQuickReturnHeight;

	 private static final int STATE_ONSCREEN = 0;
	 private static final int STATE_OFFSCREEN = 1;
	 private static final int STATE_RETURNING = 2;
	 private int mState = STATE_ONSCREEN;
	 private int mScrollY;
	 private int mMinRawY = 0;

	 private String[] imageUrls;

	 private TranslateAnimation anim;
     private DisplayImageOptions options;
     private OnScrollListener osl;
     private InScrollableListView mListView;
	 private RelativeLayout mQuickReturnView;
     ImageLoader imageLoader = ImageLoader.getInstance();
     
	 public static final Messaging_Fragment newInstance(String message, int layid)

	 {

	   Messaging_Fragment f = new Messaging_Fragment();

	   Bundle bdl = new Bundle(1);

	   bdl.putString(EXTRA_MESSAGE, message);

	   bdl.putInt(LAYOUT_ID, layid);
	   
	   f.setArguments(bdl);
	   return f;

	 }

	 public class OnPauseScrollListener extends PauseOnScrollListener  {
			private int mScrollState;
			private boolean visible = true;
			QuickReturnListViewOnScrollListener sc;
			public OnPauseScrollListener(QuickReturnListViewOnScrollListener scrollListener, ImageLoader imageLoader,
					boolean pauseOnScroll, boolean pauseOnFling) {
				super(imageLoader, pauseOnScroll, pauseOnFling);
				sc = scrollListener;
			}

			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
				sc.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
					/*			
				if(firstVisibleItem > 3 && !visible){
					translate(-trans);
					visible= true;
				}else if(firstVisibleItem < 4 && visible){
					translate(0);
					visible=false;
				}
				*/
				
				
				 
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
		       super.onScrollStateChanged(view, scrollState);
				mScrollState = scrollState;

				if(mScrollState != OnScrollListener.SCROLL_STATE_IDLE)
				imageLoader.pause();
			}
		
		
	}

	 @SuppressLint("NewApi") 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,

	   Bundle savedInstanceState) {
		 this.inflater=inflater;
		 imageUrls = Constants.IMAGES;
	     int layoutId = getArguments().getInt(LAYOUT_ID);
		 View v = inflater.inflate(layoutId, container, false);
	     mQuickReturnView = (RelativeLayout) v.findViewById(R.id.messaging_sticky);

		 return v;
	 }
	 
	 @SuppressLint("NewApi") 
	 @Override
	 public void onViewCreated(View view, Bundle savedInstanceState) {
		 super.onViewCreated(view, savedInstanceState);
		 v=view;
	 }
	 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) 
    {
        super.onActivityCreated(savedInstanceState);
		mListView = (InScrollableListView) getListView();
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.color.ProfileBack)
			.showImageForEmptyUri(R.color.ProfileBack)
			.showImageOnFail(R.color.ProfileBack)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		
		
		int footerHeight = getActivity().getResources().getDimensionPixelSize(R.dimen.footer_height);
		final QuickReturnListViewOnScrollListener qr = new QuickReturnListViewOnScrollListener(QuickReturnType.FOOTER, null, 0, mQuickReturnView, footerHeight);

		
		if(mListView.getAdapter()==null){
			View mHeader = inflater.inflate(R.layout.messaging_header, null);

			mListView.addFooterView(mHeader);

			setListAdapter(new MessagingAdapter());
			
	        mListView.setOnScrollListener(new OnPauseScrollListener(qr, imageLoader, true, true));
	    
		}

		
		
		
		

		

		mListView.setCacheColorHint(Color.BLACK);
		mListView.setVerticalFadingEdgeEnabled(false);
		//mListView.setFadingEdgeLength(5);
		mListView.setOnItemClickListener(this);
	}
    
    @Override
    public void onResume(){
    	super.onResume();
    }
    
    private void setColours() {
		//v.findViewById(R.id.background).setBackgroundColor(DataHolder.APP_BACKGROUND);
		//v.findViewById(R.id.actionbar).setBackgroundColor(DataHolder.APP_BACKGROUND);
		
		//mQuickReturnView.setBackgroundColor(DataHolder.Text_Colour);
		   v.findViewById(R.id.messaging_sticky).setBackgroundColor(DataHolder.Text_Colour);
		   v.findViewById(R.id.messaging_sticky).getBackground().setAlpha(200);

		if(DataHolder.Text_Colour != Color.WHITE){
		   ((ImageView) v. findViewById(R.id.imageView1)).getDrawable().setColorFilter(null);
		   ((ImageView) v. findViewById(R.id.imageView2)).getDrawable().setColorFilter(null);
		}else{
		   ((ImageView) v.findViewById(R.id.imageView1)).getDrawable().setColorFilter(DataHolder.colorFilter_Negative);
		   ((ImageView) v.findViewById(R.id.imageView2)).getDrawable().setColorFilter(DataHolder.colorFilter_Negative);

		}
	}
    
    class MessagingAdapter extends BaseAdapter {
    	protected ImageLoader imageLoader = ImageLoader.getInstance();

		@Override
		public int getCount() {
			return 10;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@SuppressLint("NewApi") @Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.message_row, parent, false);
				holder = new ViewHolder();
				assert view != null;
				holder.contactIcon = (HexContactStatusIcon) view.findViewById(R.id.contactIcon);
				holder.userName = (TextView) view.findViewById(R.id.userName);
				holder.message_preview = (TextView) view.findViewById(R.id.message_preview);
				holder.notification = (SquareText) view.findViewById(R.id.notification);
				holder.time = (AutoResizeTextView) view.findViewById(R.id.time);

				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			int not= 0;
			
			//colours
			holder.userName.setTextColor(DataHolder.Text_Colour);
			holder.time.setTextColor(DataHolder.Text_Colour);
			if(DataHolder.Text_Colour == Color.BLACK)
				holder.time.getCompoundDrawables()[0].setColorFilter(DataHolder.colorFilter_Negative);
			else
				holder.time.getCompoundDrawables()[0].setColorFilter(null);
				
			holder.message_preview.setTextColor(DataHolder.Text_Colour);
			holder.message_preview.setAlpha((float) 0.7);
			
			//holder.userName.setText(user.getDisplay_Name());

			if(not>0){
				holder.notification.setVisibility(View.VISIBLE);
				holder.notification.setText(not<9? String.valueOf(not): "9+");
			}else{
				holder.notification.setVisibility(View.GONE);

			}
			//imageLoader.displayImage(imageUrls[position], holder.contactIcon, options);
			if(position==5)
				holder.contactIcon.ChangeStatus(Status.ONLINE);
			else
				holder.contactIcon.ChangeStatus(Status.Match("awaY"));
	
			return view;
		}

		class ViewHolder {
			public SquareText notification;
			public AutoResizeTextView time;
			TextView message_preview;
			HexContactStatusIcon contactIcon;
			TextView userName;

		}	
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		/*
		Chat_Fragment newFragment = new Chat_Fragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
       
		// Replace whatever is in the fragment_container view with this fragment,
		// and add the transaction to the back stack

		transaction.replace(R.id.background, newFragment);
        transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
		*/
		
		Intent i = new Intent(getActivity(),Chat_Activity.class);
		startActivity(i);
	}
    }
	 

