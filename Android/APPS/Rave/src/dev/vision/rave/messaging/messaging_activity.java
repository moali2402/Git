package dev.vision.rave.messaging;

import static dev.vision.rave.Constants.IMAGES;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.ColorPicker.OnColorChangedListener;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import dev.vision.rave.Abc;
import dev.vision.rave.DataHolder;
import dev.vision.rave.R;
import dev.vision.rave.user.Status;
import dev.vision.rave.views.AutoResizeTextView;
import dev.vision.rave.views.HexContactStatusIcon;
import dev.vision.rave.views.InScrollableListView;
import dev.vision.rave.views.SquareText;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.view.Window;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class messaging_activity extends Abc implements OnItemClickListener {
	
	public Uri uri;
	String[] imageUrls;
	DisplayImageOptions options;
	Bundle savedInstanceState;
	float ShadeHright;
	
	//private QuickReturnListView mListView;
	private View mHeader;
	private RelativeLayout mQuickReturnView;

	private int mQuickReturnHeight;
	
	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;
	Handler h;
	public boolean done;
    int viewH;
    InScrollableListView mListView;
	private ColorPicker picker;
	private SVBar svBar;
	private OpacityBar opacityBar;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.savedInstanceState = savedInstanceState;
		setContentView(R.layout.messaging);
		ShadeHright =(getResources().getDimension(R.dimen.shadeHeight));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .build();
		imageLoader.init(config);
		
		imageUrls = IMAGES;
		mQuickReturnView = (RelativeLayout) findViewById(R.id.sticky);
		mListView = (InScrollableListView) findViewById(android.R.id.list);
		DataHolder.APP_BACKGROUND = getResources().getColor(R.color.ProfileBack);
		DataHolder.Text_Colour = DataHolder.getContrastColor(DataHolder.APP_BACKGROUND);
		
		setColours();
		
		LayoutInflater inflater = getLayoutInflater();		
		mHeader = inflater.inflate(R.layout.messaging_header, null);
		options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.color.ProfileBack)
			.showImageForEmptyUri(R.color.ProfileBack)
			.showImageOnFail(R.color.ProfileBack)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		mListView.addHeaderView(mHeader);

		mListView.setAdapter(new MessagingAdapter());

		mListView.getViewTreeObserver().addOnGlobalLayoutListener(
			new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					mQuickReturnHeight = mQuickReturnView.getMeasuredHeight();
					//mPlaceHolder.getLayoutParams().height = mQuickReturnHeight;
					mListView.computeScrollY();
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				mScrollY = 0;
				int translationY = 0;

				if (mListView.scrollYIsComputed()) {
					mScrollY = mListView.getComputedScrollY();
				}

				int rawY = mScrollY;

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY >= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:

					translationY = (rawY - mMinRawY) + mQuickReturnHeight;

					System.out.println(translationY);
					if (translationY < 0) {
						translationY = 0;
						mMinRawY = rawY + mQuickReturnHeight;
					}

					if (rawY == 0) {
						mState = STATE_ONSCREEN;
						translationY = 0;
					}

					if (translationY > mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY,
							translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
		mListView.setCacheColorHint(Color.BLACK);
		mListView.setVerticalFadingEdgeEnabled(true);
		mListView.setFadingEdgeLength(5);

	}


	private void setColours() {
		findViewById(R.id.background).setBackgroundColor(DataHolder.APP_BACKGROUND);
		findViewById(R.id.actionbar).setBackgroundColor(DataHolder.APP_BACKGROUND);
		
		mQuickReturnView.setBackgroundColor(DataHolder.Text_Colour);
		
		if(DataHolder.Text_Colour == Color.WHITE){
		   findViewById(R.id.view1).getBackground().setColorFilter(DataHolder.colorFilter_Negative);
		   ((ImageView)findViewById(R.id.imageView1)).getDrawable().setColorFilter(null);
		}else{
		   findViewById(R.id.view1).getBackground().setColorFilter(null);
		   ((ImageView)findViewById(R.id.imageView1)).getDrawable().setColorFilter(DataHolder.colorFilter_Negative);
		}
	}
	
	@Override
	public void onResume(){
		super.onResume();
		showColorPickerDialog();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if(data!=null)
		  uri = data.getData();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	public class MessagingAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return imageUrls.length;
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
				view = getLayoutInflater().inflate(R.layout.message_row, parent, false);
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
			int not= 20;
			
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
			imageLoader.displayImage(imageUrls[position], holder.contactIcon, options);
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
	
	private void showColorPickerDialog()
	{
		AlertDialog.Builder colorDialogBuilder = new AlertDialog.Builder(
		this);
		LayoutInflater inflater = LayoutInflater.from(this);
		View dialogview = inflater.inflate(R.layout.color_picker, null);
		picker = (ColorPicker) dialogview.findViewById(R.id.picker);
		svBar = (SVBar) dialogview.findViewById(R.id.svbar);
		opacityBar = (OpacityBar) dialogview.findViewById(R.id.opacitybar);
		picker.addSVBar(svBar);
		picker.addOpacityBar(opacityBar);
		picker.setColor(DataHolder.APP_BACKGROUND);
        picker.setOldCenterColor(picker.getColor());

		picker.setOnColorChangedListener(new OnColorChangedListener() 
		{
	
	        @Override
	        public void onColorChanged(int color) {
                //picker.setOldCenterColor(picker.getColor());
                mListView.setBackgroundColor(picker.getColor());

	        }
	    });
	    colorDialogBuilder.setTitle("Choose Color");
	    colorDialogBuilder.setView(dialogview);
	    colorDialogBuilder.setPositiveButton("OK",
	            new DialogInterface.OnClickListener() {
	                @SuppressLint("NewApi") @Override
	                public void onClick(DialogInterface dialog, int which) {
	                    Log.d("l", "Color :" + picker.getColor());
	                    DataHolder.ChangeColors(picker.getColor());
	                    setColours();
	                    mListView.setBackground(null);

	                }
	            });
	    colorDialogBuilder.setNegativeButton("Cancel",
	            new DialogInterface.OnClickListener() {
	
	                @Override
	                public void onClick(DialogInterface dialog, int which) {
	                	/*int wantedPosition = 10; // Whatever position you're looking for
	                	int firstPosition = mListView.getFirstVisiblePosition() - mListView.getHeaderViewsCount(); // This is the same as child #0
	                	int wantedChild = wantedPosition - firstPosition;
	                	// Say, first visible position is 8, you want position 10, wantedChild will now be 2
	                	// So that means your view is child #2 in the ViewGroup:
	                	if (wantedChild < 0 || wantedChild >= mListView.getChildCount()) {
	                	  return;
	                	}
	                	// Could also check if wantedPosition is between listView.getFirstVisiblePosition() and listView.getLastVisiblePosition() instead.
	                	View wantedView = mListView.getChildAt(wantedChild);
	                	((RelativeLayout)findViewById(R.id.newotification)).addView(wantedView);
	                	*/
	                	dialog.cancel();
	                }
	            });
	    AlertDialog colorPickerDialog = colorDialogBuilder.create();
	    colorPickerDialog.show();
	}


	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					//FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		
	}
	

}
