package com.example.demo;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.example.demo.Activity_Manage.StoreList.ViewHolder;
import com.example.demo.Quote_Activity.AttachmentAdapter;
import com.example.demo.Quote_Activity.QuoationListAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;


public class Fragment_Activity extends FragmentActivity implements OnPageChangeListener{

	 
	 Quote quote;
 	 User user;
	 private ArrayList<String> folders = new ArrayList<String>();
	 private ListView recall;
	 
	 private ViewPager mPager;
	 private ScreenSlidePagerAdapter mPagerAdapter;
	 private Fragment f1;
	 private Fragment_Quote f2;
	 private Fragment_Client f3;
	 private Fragment_Attachment f4;
	 private int last = 0;
	 ListView menu;
	 ImageView add;
	 
	 final String filesPath = Environment.getExternalStorageDirectory() + File.separator + "TheQuote" + File.separator+ "quotes" + File.separator ;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.v_activity_main);
       
        Bundle b =getIntent().getExtras();
        
        if(b!=null)
        	user = (User) b.getSerializable("user");
        
        setQuoteDetails();
     
	    menu = (ListView) findViewById(R.id.menu);
	    menu.setAdapter(new MenuAdapter());
	    menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
			
				if(last ==1 && f2!=null && f2.show == 0 ){
					AlertDialog.Builder ab = new AlertDialog.Builder(Fragment_Activity.this);
					ab.setMessage("Please select the Weekly term the client prefers");
					ab.setTitle("Warning!");
					ab.setNegativeButton("OK", null);
					AlertDialog a =ab.create();
					a.show();
				}else{
					mPager.setCurrentItem(position);
					last = position;
				}
				
				
				
			
			}
		});
     	f1 = new Fragment();
 		f2 = Fragment_Quote.newInstance();
 		f3 = Fragment_Client.newInstance();
 		f4 = Fragment_Attachment.newInstance();
 	
 		ArrayList<Fragment> ar = new ArrayList<Fragment>();
 		ar.add(f1);
 	    ar.add(f2);
 		ar.add(f3);
 		ar.add(f4);

     	mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setOffscreenPageLimit(5);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager(), ar);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(this);
        
	     recall = (ListView) findViewById(R.id.recall_list);

         add= (ImageView) findViewById(R.id.add_line_b);
	     
	     setLists();
	     
	     add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        			
				f2.addRow(new Row());
			}
		 });
	     
	     findViewById(R.id.total_on_off).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(f2.clicked)
		              f2.resetContractTotal();
					else{
		              f2.showContractTotal();		
					}
				}
		 });
	     
	     findViewById(R.id.save).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					saveApplication();				
				}
		});
	     
	     
	    findViewById(R.id.submit).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder ab = new AlertDialog.Builder(Fragment_Activity.this);
					ab.setMessage("By clicking continue, you confirm that you wish to submit this application?");
					ab.setTitle("Submit Application!");
					ab.setNegativeButton("Cancel", null).setPositiveButton("Continue", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							submitApplication();											
						}
					});
					AlertDialog a =ab.create();
					a.show();
				}
		});
	     
	     
	     findViewById(R.id.load).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showSearch();
				}
		 });
	     
	     findViewById(R.id.clear).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					recreateA();
					
				}

		 });
	     
	     findViewById(R.id.close_recall).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					 findViewById(R.id.recall_banner).setVisibility(View.GONE);
				}
		 });
	     
	     findViewById(R.id.logout).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					AlertDialog.Builder ab = new AlertDialog.Builder(Fragment_Activity.this);
					ab.setMessage("Confirm that you want to logout");
					ab.setTitle("Logout?");
					ab.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(Fragment_Activity.this, MainActivity.class));
							finish();
						}
					}).setNegativeButton("Cancel", null);
					AlertDialog a =ab.create();
					a.show();
				}
		 });
	     
	     recall.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					 retriveData(folders.get(position));
					 findViewById(R.id.recall_banner).setVisibility(View.GONE);

				}
			});
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
    	
    	ArrayList<Fragment> f;
        public ScreenSlidePagerAdapter(FragmentManager fm, ArrayList<Fragment> fr) {
            super(fm);
            f = fr;
        }

        @Override
        public Fragment getItem(int position) {
        	return f.get(position);
        }

        @Override
        public int getCount() {
            return f.size();
        }
    }
	
	 
	void showSearch(){
		 
		 findViewById(R.id.recall_banner).setVisibility(View.VISIBLE);
		 findViewById(R.id.recall_pr).setVisibility(View.VISIBLE);

		 new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				 findOldQuotes();
			}
		});
	 }
	 
	@SuppressWarnings("unchecked")
	void findOldQuotes(){
		 File dir = new File(filesPath+user.store.Storeid);
		 if(dir.exists()){
			 folders.clear();
			 for (File f : dir.listFiles()) {
				 if(f.isDirectory()){
					 String n = f.getName();
					 folders.add(n);
				 }
			 }
		 }
		 ((ArrayAdapter<String>)recall.getAdapter()).notifyDataSetChanged();
		 findViewById(R.id.recall_pr).setVisibility(View.GONE);
	 }
		 
	 private void submitApplication() {
		 quote.upload(this,f3.getView());
	 }
	 /*
	 void setupApp(){
		 setLists();
		 quote.cd.resetFields(this);	 
	 }*/
	 
	 private void setLists() {
		 recall.setAdapter(new ArrayAdapter<String>(this, R.layout.recal_text, folders));	     
	}

	void setQuoteDetails(){
		SetupNewQuote();
		showDetails();
	}
	
	private void SetupNewQuote() {
		quote = new Quote(user);			
	}

	void recreateA() {
		startActivity(getIntent());
		finish();					
	}
	
	private void showDetails() {
		((TextView) findViewById(R.id.salesrep)).setText(quote.user.storerep);
		((TextView) findViewById(R.id.storecode)).setText(quote.user.store.Storeid);
		((TextView) findViewById(R.id.date)).setText(quote.date);
		((TextView) findViewById(R.id.quote_id)).setText(quote.getQuoteId());
	}

	public void retriveData(String Quote_id){
		quote = new Quote(this, user, Quote_id);
		showDetails();
		f2.setQuoteClientFields();
		f3.setClientDetailsFields();
		
		//quote.cd.setQuoteClientFields(f2.getView());
		//quote.cd.setClientDetailsFields(f3.getView());
		
		quote.calculateData();
		f2.calculateRowsTotal(); 
		setRetrivedLists();
	}
	

	 private void setRetrivedLists() {
	     f4.setList();
	     f2.setList();
	}

	class MenuAdapter extends BaseAdapter{
	
		public MenuAdapter(){
		}
		
		@Override
		public int getCount() {
			return 4;
		}
	
		@Override
		public Drawable getItem(int position) {
			Drawable d = null;
			if(position == 0){
				d= getResources().getDrawable(R.drawable.home_all);
			}else if(position == 1){
				d= getResources().getDrawable(R.drawable.quote);
			}else if(position == 2){
				d= getResources().getDrawable(R.drawable.client_details);
			}else if(position == 3){
				d= getResources().getDrawable(R.drawable.attachment);
			}
			return d;
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			final ViewHolder holder;
			if(convertView == null){
				LayoutInflater lf = getLayoutInflater();
				holder = new ViewHolder();
				convertView = lf.inflate(R.layout.menu_item, null);
				holder.item = (ImageView) convertView.findViewById(R.id.menu_image);	
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			holder.item.setImageDrawable(getItem(position));
			
			if(mPager.getCurrentItem() == position)
				convertView.setBackgroundColor(Color.parseColor("#ff9900"));
			else
				convertView.setBackground(null);
			
			return convertView;
		}
		
		class ViewHolder{
			ImageView item;
	
		}
		
	}
	
	
	
	 private void saveApplication(){
		if(f3!=null && f3.getView()!=null)
			quote.save(this,f3.getView());
	 }
	

	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         Fragment fragment = (Fragment) f4;
	     if(fragment != null){
	            fragment.onActivityResult(requestCode, resultCode, data);
	     }
     }
	 

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int position) {
		
		if(last == 2){
			f3.getFields();
		}else if(last == 1){
			f2.getFieldsQ();
		}
		if(position == 1){
			//add.setVisibility(View.VISIBLE);	
			findViewById(R.id.total_on_off).setVisibility(View.VISIBLE);
		
			f2.setQuoteClientFields();
			f2.setHeight();
		}else{
			if(position == 2){
				f3.setClientDetailsFields();
			}
			//add.setVisibility(View.INVISIBLE);
			findViewById(R.id.total_on_off).setVisibility(View.INVISIBLE);
		
		}
		
		
		last = position;
		

		((MenuAdapter)menu.getAdapter()).notifyDataSetChanged();
		
	}

	 
}
