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


public class Quote_Activity extends ActionBarActivity{

	 TabHost tabHost;
	 ListView lv;
	 Quote quote;
	 private GridView gv;
	 private Uri outputFileUri;
     final String filesPath = Environment.getExternalStorageDirectory() + File.separator + "TheQuote" + File.separator+ "quotes" + File.separator ;
	 private File root;
	 User user;
     private int show = 0;

	 private boolean clicked = true;
	private Spinner terms;
	private ArrayList<String> folders = new ArrayList<String>();
	private ListView recall;

	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
       
        Bundle b =getIntent().getExtras();
        
        if(b!=null)
        	user = (User) b.getSerializable("user");
        
        setQuoteDetails();
     
        tabHost = (TabHost)findViewById(R.id.tabhost);
	    tabHost.setup();
	    
	     // Set the Tab name and Activity
	     // that will be opened when particular Tab will be selected
	     TabSpec home = tabHost.newTabSpec("Home");
	     home.setIndicator("Home");
	     home.setContent(R.id.home);
	     
	     final TabSpec tab1 = tabHost.newTabSpec("Quote");
	     tab1.setIndicator("Quote");
	     tab1.setContent(R.id.tab1);
	
	     final TabSpec tab2 = tabHost.newTabSpec("Client Details");
	     tab2.setIndicator("Client Details");
	     tab2.setContent(R.id.tab2);
	     
	     final TabSpec tab3 = tabHost.newTabSpec("Attachments");
	     tab3.setIndicator("Attachments");
	     tab3.setContent(R.id.tab3);
	     
	     /** Add the tabs  to the TabHost to display. */
	     tabHost.addTab(home);
	     tabHost.addTab(tab1);
	     tabHost.addTab(tab2);
	     tabHost.addTab(tab3);
	     
	     final ListView menu = (ListView) findViewById(R.id.menu);
	     menu.setAdapter(new MenuAdapter());
	     menu.setOnItemClickListener(new OnItemClickListener() {

			private int last = 0;

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				//if( last==0 || (last != position && (( last == 3 && quote.attachments.size() > 0) || (last == 2 && quote.cd.isFilled(Quote_Activity.this)) || (last == 1 && quote.isCalulated()))) ){					
					tabHost.setCurrentTab(position);
					if(last == 2){
						//quote.cd.getFields(Quote_Activity.this); undo
					}else if(last == 1){
						//quote.cd.getFieldsQ(Quote_Activity.this); undo
					}
					if(position == 2){
						//quote.cd.setClientDetailsFields(Quote_Activity.this); undo
					}else if(position == 1){
						//quote.cd.setQuoteClientFields(Quote_Activity.this); undo
					}
					last = position;

					((MenuAdapter)menu.getAdapter()).notifyDataSetChanged();

				/*}else{
					AlertDialog.Builder ab = new AlertDialog.Builder(Quote_Activity.this);
					ab.setMessage("Please make sure you fill all the required fields, Otherwise data will be lost");
					ab.setTitle("Reset Quote!");
					ab.setNegativeButton("Stay On this Page", null).setPositiveButton("Reset and Change Page", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							setQuoteDetails();
							
							setupApp();
							
							tabHost.setCurrentTab(position);	
							last = position;
							
							((MenuAdapter)menu.getAdapter()).notifyDataSetChanged();
							

						}
					});
					AlertDialog a =ab.create();
					a.show();
				}
				*/
			}
		  });
	
	     /*
	     for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
	         View v = tabHost.getTabWidget().getChildAt(i);
	         v.setBackgroundResource(R.drawable.indicator);
	      }
	      */
	     
	     recall = (ListView) findViewById(R.id.recall_list);

	     lv = (ListView) findViewById(R.id.listView1);
	     gv = (GridView) findViewById(R.id.gridView1);
	     
	     setLists();
	     
	     final ImageView add= (ImageView) findViewById(R.id.add_line_b);
	     
	     
	     add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
	        	 quote.add(new Row());
	
				((QuoationListAdapter)lv.getAdapter()).notifyDataSetChanged();
				Utility.setListViewHeightBasedOnChildren(lv);
	
			}
		 });
	     
	     OnTabChangeListener otc= new OnTabChangeListener() {
				
				@Override
				public void onTabChanged(String tabId) {
					if(tab1.getTag() == tabId){
					    Utility.setListViewHeightBasedOnChildren(lv);
						add.setVisibility(View.VISIBLE);	
						findViewById(R.id.total_on_off).setVisibility(View.VISIBLE);
					}else{
						add.setVisibility(View.INVISIBLE);
						findViewById(R.id.total_on_off).setVisibility(View.INVISIBLE);
					}
				}
	      };
	     tabHost.setOnTabChangedListener(otc);
	     
	     otc.onTabChanged(home.getTag());
	     
	     findViewById(R.id.gallery).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			     openGallery();				
			}
		 });
	     
	     findViewById(R.id.total_on_off).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(clicked)
		              resetContractTotal();
					else{
		              showContractTotal();		
					}
				}
		 });
	     
	     
	     
	     findViewById(R.id.camera).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openImageIntent();				
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
					submitApplication();				
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
					recreate();
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
					AlertDialog.Builder ab = new AlertDialog.Builder(Quote_Activity.this);
					ab.setMessage("Confirm that you want to logout");
					ab.setTitle("Logout?");
					ab.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(Quote_Activity.this, MainActivity.class));
							finish();
						}
					}).setNegativeButton("Cancel", null);
					AlertDialog a =ab.create();
					a.show();
				}
		 });
	     
    	terms = (Spinner) findViewById(R.id.terms);
    	terms.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				show= position;
				((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
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
	 
	 void findOldQuotes(){
		 File dir = new File(filesPath+user.store.Storeid);
		 folders.clear();
		 for (File f : dir.listFiles()) {
			 if(f.isDirectory()){
				 String n = f.getName();
				 folders.add(n);
			 }
		 }
		 ((ArrayAdapter<String>)recall.getAdapter()).notifyDataSetChanged();
		 findViewById(R.id.recall_pr).setVisibility(View.GONE);

	 }
	
	 public void showDatePickerDialog(View v) {
		    DialogFragment newFragment = new DatePickerFragment(v);
		    newFragment.show(getSupportFragmentManager(), "datePicker");
	}
	 
	 private void submitApplication() {
		 quote.upload(this);
	 }
	 /*
	 void setupApp(){
		 setLists();
		 quote.cd.resetFields(this);	 
	 }*/
	 
	 private void setLists() {
		 recall.setAdapter(new ArrayAdapter<String>(this, R.layout.recal_text, folders));
	     gv.setAdapter(new AttachmentAdapter(quote.attachments));
	     gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				

				intent.setDataAndType(Uri.fromFile(new File(quote.attachments.get(position))), "image/*");
				startActivity(intent);				
			}
		});
	     
	     
    	 quote.add(new Row());

    	 /*
		 for( int x = 3; x > 0; x--){
	    	 quote.add(new Row());
	     }
	     */
    	 
	     lv.setAdapter(new QuoationListAdapter(quote.rows));
	     //Utility.setListViewHeightBasedOnChildren(lv);
	}
	 
	 

	void setQuoteDetails(){
		SetupNewQuote();
		showDetails();
	}
	
	private void SetupNewQuote() {
		quote = new Quote(user);			
	}

	public void retriveData(String Quote_id){
		quote = new Quote(this, user, Quote_id);
		showDetails();
		quote.cd.setClientDetailsFields(this);
		quote.cd.setQuoteClientFields(this);
		quote.calculateData();
		calculateRowsTotal();
		setRetrivedLists();
	}


	 private void setRetrivedLists() {
	     gv.setAdapter(new AttachmentAdapter(quote.attachments));
	     gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				

				intent.setDataAndType(Uri.fromFile(new File(quote.attachments.get(position))), "image/*");
				startActivity(intent);				
			}
		});
	     
	     lv.setAdapter(new QuoationListAdapter(quote.rows));
	     Utility.setListViewHeightBasedOnChildren(lv);
	}



	private void showDetails() {
		((TextView) findViewById(R.id.salesrep)).setText(quote.user.storerep);
		((TextView) findViewById(R.id.storecode)).setText(quote.user.store.Storeid);
		((TextView) findViewById(R.id.date)).setText(quote.date);
		((TextView) findViewById(R.id.quote_id)).setText(quote.getQuoteId());
	}

	

	class QuoationListAdapter extends BaseAdapter{
		private ArrayList<Row> rows;

	
		public QuoationListAdapter(ArrayList<Row> rows){
			this.rows = rows;
		}
		
		@Override
		public int getCount() {
			return rows.size();
		}
	
		@Override
		public Row getItem(int position) {
			return rows.get(position);
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
				convertView = lf.inflate(R.layout.llist_item_l, null);
				holder.item = (EditText) convertView.findViewById(R.id.item);
				holder.price = (EditText) convertView.findViewById(R.id.price);	
				holder.disc = (EditText) convertView.findViewById(R.id.disc);
				holder.six_mth = (TextView) convertView.findViewById(R.id.m_6);	
				holder.one_yr = (TextView) convertView.findViewById(R.id.y_1);	
				holder.eighten_mth = (TextView) convertView.findViewById(R.id.m_18);	
				holder.two_yr = (TextView) convertView.findViewById(R.id.yr_2);	
				holder.three_yr = (TextView) convertView.findViewById(R.id.yr_3);	
				holder.remove = (ImageView) convertView.findViewById(R.id.remove_row);	

				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
	  	  	Row r= rows.get(position);
	
			holder.item.setTag(position); 
			holder.price.setTag(position); 
			holder.remove.setTag(position); 

			holder.item.setText(r.item); 
			holder.price.setText(String.format("%.2f", r.i.price)); 
			holder.disc.setText(""+r.i.discount); 

			if(show == 1|| show == 0 )
				holder.six_mth.setText(r.six_mth);
			else
				holder.six_mth.setText("");

			if(show == 2  || show == 0)
				holder.one_yr.setText(r.one_yr);
			else
				holder.one_yr.setText("");

			if(show == 3  || show == 0)
				holder.eighten_mth.setText(r.eighten_mth);
			else
				holder.eighten_mth.setText("");

			if(show == 4  || show == 0)
				holder.two_yr.setText(r.two_yr);
			else
				holder.two_yr.setText("");

			if(show == 5  || show == 0)
				holder.three_yr.setText(r.three_yr);
			else
				holder.three_yr.setText("");

            
			holder.price.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
				 	holder.price.setText("");
				 	return false;
				}
			});
			
			holder.disc.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
				 	holder.disc.setText("");
				 	return false;
				}
			});
			
			holder.item.addTextChangedListener(new TextWatcher() {
	
	             @Override
	            public void onTextChanged(CharSequence s, int start, int before,
	                    int count) {
	                      final int pos = (Integer) holder.item.getTag();
	                      final EditText name = (EditText) holder.item;
	                	  Row r= rows.get(pos);
	
	                      if(name.getText().toString().length()>0){
	                    	  r.item = name.getText().toString() ;
	                      }else{
	                    	  r.item = "";
	                      }
	                      quote.set(pos,r);
	
	            }
	
	             @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	                // TODO Auto-generated method stub
	            }
	
	             @Override
	            public void afterTextChanged(Editable s) {
	             
	            }
	
	         });
			
			 holder.price.addTextChangedListener(new TextWatcher() {
				
	             @Override
	            public void onTextChanged(CharSequence s, int start, int before,
	                    int count) {
	                      
	            }
	
	             @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	                // TODO Auto-generated method stub
	            }
	
	             @Override
	            public void afterTextChanged(Editable s) {
	            	 final int pos = (Integer) holder.price.getTag();
                     final TextView six_m = (TextView) holder.six_mth;
                     final TextView one_yr = (TextView) holder.one_yr;
                     final TextView eighten_mth = (TextView) holder.eighten_mth;
                     final TextView two_yr = (TextView) holder.two_yr;
                     final TextView three_yr = (TextView) holder.three_yr;

               	     Row r= rows.get(pos);
               	  
                     if(holder.price.getText().toString().length()>0){
                   	   r.setPrice(Float.parseFloat(holder.price.getText().toString()));
                     }else{
                       r.setPrice(0);
                     }
                 	 r.calculate();
                 	 
                     setTextForCalc(r, pos, six_m, one_yr, eighten_mth, two_yr, three_yr );

                     
                    
	            }
	
	         });
			
			holder.disc.addTextChangedListener(new TextWatcher() {
				
	             @Override
	            public void onTextChanged(CharSequence s, int start, int before,
	                    int count) {
	                      
	            }
	
	             @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	                // TODO Auto-generated method stub
	            }
	
	             @Override
	            public void afterTextChanged(Editable s) {
	            	 final int pos = (Integer) holder.price.getTag();
                    final TextView six_m = (TextView) holder.six_mth;
                    final TextView one_yr = (TextView) holder.one_yr;
                    final TextView eighten_mth = (TextView) holder.eighten_mth;
                    final TextView two_yr = (TextView) holder.two_yr;
                    final TextView three_yr = (TextView) holder.three_yr;

              	    Row r= rows.get(pos);
              	  
                    if(holder.disc.getText().toString().length()>0){
                  	  r.setDisc(Integer.parseInt(holder.disc.getText().toString()));
                    }else{
                      r.setDisc(0);
	                }
                    if(holder.price.getText().toString().length()>0)
	              	 r.calculate();
                    
                    setTextForCalc(r, pos, six_m, one_yr, eighten_mth, two_yr, three_yr );
	            }
	
	         });
			
			holder.remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	            	final int pos = (Integer) holder.remove.getTag();

					rows.remove(pos);	
					quote.calculateData();
					calculateRowsTotal();				     

					((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
					Utility.setListViewHeightBasedOnChildren(lv);

				}
			});
			
			return convertView;
		}
		
		private void setTextForCalc(Row r, int pos, TextView six_m, TextView one_yr, TextView eighten_mth, TextView two_yr, TextView three_yr) {
			/*
			 six_m.setText(r.six_mth);
             one_yr.setText(r.one_yr);
             eighten_mth.setText(r.eighten_mth);
             two_yr.setText(r.two_yr);
             three_yr.setText(r.three_yr);
            */
            if(show == 1|| show == 0 )
            	 six_m.setText(r.six_mth);
 			else
 				six_m.setText("");

 			if(show == 2  || show == 0)
 				one_yr.setText(r.one_yr);
 			else
 				one_yr.setText("");

 			if(show == 3  || show == 0)
 				eighten_mth.setText(r.eighten_mth);
 			else
 				eighten_mth.setText("");

 			if(show == 4  || show == 0)
 				two_yr.setText(r.two_yr);
 			else
 				two_yr.setText("");

 			if(show == 5  || show == 0)
 				three_yr.setText(r.three_yr);
 			else
 				three_yr.setText("");

             
             
             quote.set(pos,r);
             
     		 quote.calculateData();
     		 
 			 calculateRowsTotal();	

     		 

		}
		
		

		class ViewHolder{
			EditText disc;
			EditText item;
			EditText price;
			TextView six_mth;
			TextView one_yr;
			TextView eighten_mth;
			TextView two_yr;
			TextView three_yr;
			ImageView remove;
		}
		
	}
	


	public void resetRowsTotal() {
		((TextView) findViewById(R.id.t_price)).setText("");
		
		((TextView) findViewById(R.id.wkTotal1)).setText("");
		((TextView) findViewById(R.id.wkTotal2)).setText("");
		((TextView) findViewById(R.id.wkTotal3)).setText("");
		((TextView) findViewById(R.id.wkTotal4)).setText("");
		((TextView) findViewById(R.id.wkTotal5)).setText("");
	}
	
	void resetContractTotal(){
		clicked= false;

		((TextView) findViewById(R.id.Total1)).setText("");
		((TextView) findViewById(R.id.Total2)).setText("");
		((TextView) findViewById(R.id.Total3)).setText("");
		((TextView) findViewById(R.id.Total4)).setText("");
		((TextView) findViewById(R.id.Total5)).setText("");
	}
		
	public void calculateRowsTotal() {
		
		
		((TextView) findViewById(R.id.t_price)).setText(String.format("%.2f", quote.total_price));
		
		if(show == 1|| show == 0 )
        	((TextView) findViewById(R.id.wkTotal1)).setText(String.format("%.2f",quote.payable_1));
		else
		((TextView) findViewById(R.id.wkTotal1)).setText("");

		if(show == 2  || show == 0)
			((TextView) findViewById(R.id.wkTotal2)).setText(String.format("%.2f",quote.payable_2));
		else
			((TextView) findViewById(R.id.wkTotal2)).setText("");

		if(show == 3  || show == 0)
			((TextView) findViewById(R.id.wkTotal3)).setText(String.format("%.2f",quote.payable_3));
		else
			((TextView) findViewById(R.id.wkTotal3)).setText("");

		if(show == 4  || show == 0)
			((TextView) findViewById(R.id.wkTotal4)).setText(String.format("%.2f",quote.payable_4));
		else
			((TextView) findViewById(R.id.wkTotal4)).setText("");

		if(show == 5  || show == 0)
			((TextView) findViewById(R.id.wkTotal5)).setText(String.format("%.2f",quote.payable_5));
		else
			((TextView) findViewById(R.id.wkTotal5)).setText("");

		/*
		((TextView) findViewById(R.id.wkTotal1)).setText(String.format("%.2f",quote.payable_1));
		((TextView) findViewById(R.id.wkTotal2)).setText(String.format("%.2f",quote.payable_2));
		((TextView) findViewById(R.id.wkTotal3)).setText(String.format("%.2f",quote.payable_3));
		((TextView) findViewById(R.id.wkTotal4)).setText(String.format("%.2f",quote.payable_4));
		((TextView) findViewById(R.id.wkTotal5)).setText(String.format("%.2f",quote.payable_5));
		*/
		if(clicked)
			showContractTotal();
    	 else
    		resetContractTotal();
	}
	
	private void showContractTotal() {
		clicked = true;

		if(show == 1|| show == 0 )
        	((TextView) findViewById(R.id.Total1)).setText(String.format("%.2f",quote.total_payable_1));
		else
			((TextView) findViewById(R.id.Total1)).setText("");

		if(show == 2  || show == 0)
			((TextView) findViewById(R.id.Total2)).setText(String.format("%.2f",quote.total_payable_2));
		else
			((TextView) findViewById(R.id.Total2)).setText("");

		if(show == 3  || show == 0)
			((TextView) findViewById(R.id.Total3)).setText(String.format("%.2f",quote.total_payable_3));
		else
			((TextView) findViewById(R.id.Total3)).setText("");

		if(show == 4  || show == 0)
			((TextView) findViewById(R.id.Total4)).setText(String.format("%.2f",quote.total_payable_4));
		else
			((TextView) findViewById(R.id.Total4)).setText("");

		if(show == 5  || show == 0)
			((TextView) findViewById(R.id.Total5)).setText(String.format("%.2f",quote.total_payable_5));
		else
			((TextView) findViewById(R.id.Total5)).setText("");


		/*
		((TextView) findViewById(R.id.Total1)).setText(String.format("%.2f",quote.total_payable_1));
		((TextView) findViewById(R.id.Total2)).setText(String.format("%.2f",quote.total_payable_2));
		((TextView) findViewById(R.id.Total3)).setText(String.format("%.2f",quote.total_payable_3));
		((TextView) findViewById(R.id.Total4)).setText(String.format("%.2f",quote.total_payable_4));
		((TextView) findViewById(R.id.Total5)).setText(String.format("%.2f",quote.total_payable_5));		
		*/
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
			if(tabHost.getCurrentTab() == position)
				convertView.setBackgroundColor(Color.parseColor("#ff9900"));
			else
				convertView.setBackground(null);

			return convertView;
		}
		
		class ViewHolder{
			ImageView item;
	
		}
		
	}
	
	class AttachmentAdapter extends BaseAdapter{
		ArrayList<String> attachments;

		public AttachmentAdapter(ArrayList<String> attachments){
			
			this.attachments = attachments;

		}
		
		@Override
		public int getCount() {
			return attachments.size();
		}
	
		@Override
		public String getItem(int position) {
			
			return attachments.get(position);
		}
	
		@Override
		public long getItemId(int position) {
			return position;
		}
		
		
	
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder v;
			if(convertView == null){
				v= new ViewHolder();
				convertView = getLayoutInflater().inflate(R.layout.grid_item, null);
				v.im = (ImageView) convertView.findViewById(R.id.grid_image);
				v.rm = (ImageView) convertView.findViewById(R.id.im_rm);

				convertView.setTag(v);
			}else{
				v = (ViewHolder) convertView.getTag();
			}
            Bitmap bm = decodeSampledBitmapFromUri(getItem(position), 125, 125);

            v.im.setImageBitmap(bm);
            
            v.rm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(new File(getItem(position)).delete()){
						try{
							getContentResolver() .delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
						          MediaStore.Images.Media.DATA
						              + "='"
						              + getItem(position)
						              + "'", null);
						  } catch (Exception e) {
						      e.printStackTrace();
						  }
						quote.removeAttachment(position);
						notifyDataSetChanged();
					}
				}
			});
			//((ImageView)convertView).setLayoutParams(new AbsListView.LayoutParams(300, 300));;
			return convertView;
		}
		
		class ViewHolder{
			ImageView im;
			ImageView rm;
		}
		
		
	}
	
	
	 private void saveApplication(){
		quote.save(this);
	 }
	
	 void openGallery(){
		Intent i = new Intent(
		Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		
		startActivityForResult(i, 1);
	 }
	
	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(resultCode == RESULT_OK){
		        if (requestCode == 1 && null != data) {
		            Uri selectedImage = data.getData();
		            if(selectedImage != null){
			            String[] filePathColumn = { MediaStore.Images.Media.DATA };
			 
			            Cursor cursor = getContentResolver().query(selectedImage,
			                    filePathColumn, null, null, null);
			            cursor.moveToFirst();
			 
			            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			            String picturePath = cursor.getString(columnIndex);
			            cursor.close();
			             
			            //Bitmap b = BitmapFactory.decodeFile(picturePath);
			            quote.attach(picturePath);
			            ((AttachmentAdapter)gv.getAdapter()).notifyDataSetChanged();
		        	}
		        }else if(requestCode == 212)
		        {
		            final boolean isCamera;
		            if(data == null){
		                isCamera = true;
		            }
		            else
		            {
		                final String action = data.getAction();
		                if(action == null)
		                {
		                    isCamera = false;
		                }
		                else
		                {
		                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		                }
		            }
	
		            Uri selectedImageUri;
		            if(isCamera)
		            {
		                selectedImageUri = outputFileUri;
		               // Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
		               // Bitmap bm = BitmapFactory.decodeFile(selectedImageUri.getPath());
		                quote.attach(selectedImageUri.getPath());
			            ((AttachmentAdapter) gv.getAdapter()).notifyDataSetChanged();
		            }
		            
		       }
     
         }
     }
	 
	 private void openImageIntent() {
		    // Determine Uri of camera image to save.
	        root = new File(filesPath + quote.user.store.Storeid + File.separator + quote.getQuoteId() + File.separator);
	        root.mkdirs();


		    final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
		    final File sdImageMainDirectory = new File(root, fname);
		    outputFileUri = Uri.fromFile(sdImageMainDirectory);

		    // Camera.
		    final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);


		    startActivityForResult(captureIntent, 212);
		}

	 
	 public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth, int reqHeight) {

	        Bitmap bm = null;
	        // First decode with inJustDecodeBounds=true to check dimensions
	        final BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(path, options);

	        // Calculate inSampleSize
	        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	        //  Decode bitmap with inSampleSize set
	        options.inJustDecodeBounds = false;
	        bm = BitmapFactory.decodeFile(path, options); 

	        return bm;   
	    }

	    public int calculateInSampleSize(

	        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	        // Raw height and width of image
	        final int height = options.outHeight;
	        final int width = options.outWidth;
	        int inSampleSize = 1;

	        if (height > reqHeight || width > reqWidth) {
	            if (width > height) {
	                inSampleSize = Math.round((float)height / (float)reqHeight);    
	            } else {
	                inSampleSize = Math.round((float)width / (float)reqWidth);    
	            }   
	        }

	        return inSampleSize;    
	    }
}
