package com.example.demo;

import java.io.File;
import java.util.ArrayList;

import com.example.demo.Fragment_Attachment.AttachmentAdapter;
import com.example.demo.Quote_Activity.QuoationListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class Fragment_Quote extends Fragment {
	ListView lv;
    private Spinner terms;
    Context context;
    

	View v;
	public int show=0;
	boolean clicked;
    static String QUOTE_KEY = "QUOTE";
    public Fragment_Quote(){
    };
    
    public static final Fragment_Quote newInstance()
    {
    	Fragment_Quote f = new Fragment_Quote();
    	Bundle bdl = new Bundle(1);
	    //bdl.putSerializable(QUOTE_KEY,q);
	    f.setArguments(bdl);
        return f;
    }

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		super.onCreateView(inflater, container, savedInstanceState);
		v= inflater.inflate(R.layout.quote, null);
		

	     
    	terms = (Spinner) v.findViewById(R.id.terms);
    	
    	
    	

		lv = (ListView) v.findViewById(R.id.listView1);

		return v;
	}
	

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
    	context = getActivity();
    	//quote = (Quote) getArguments().getSerializable(QUOTE_KEY);
		lv.setAdapter(new QuoationListAdapter(((Fragment_Activity)getActivity()).quote.rows));
		terms.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				show= position;
				((Fragment_Activity)getActivity()).quote.setTerm(((String)terms.getAdapter().getItem(position)));
				((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
				showContractTotal();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
			}
		});
    	
	    addRow(6);
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	
	public void addRow(int i){
		for(int x = 0; x<i; x++){
			((Fragment_Activity)getActivity()).quote.rows.add(new Row());
		}
    	((QuoationListAdapter)lv.getAdapter()).notifyDataSetChanged();
	    Utility.setListViewHeightBasedOnChildren(lv);	

	}
	
	public void addRow(Row r){
		addRow(1);
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
				LayoutInflater lf = ((Activity) context).getLayoutInflater();
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
			holder.disc.setFilters(new InputFilter[]{ new InputFilterMinMax("0", "5")});

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
	                      ((Fragment_Activity)getActivity()).quote.rows.set(pos,r);
	
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
			/*
			holder.remove.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
	            	final int pos = (Integer) holder.remove.getTag();

					rows.remove(pos);	
					((Fragment_Activity)getActivity()).quote.calculateData();
					calculateRowsTotal();				     

					((QuoationListAdapter) lv.getAdapter()).notifyDataSetChanged();
					Utility.setListViewHeightBasedOnChildren(lv);

				}
			});
			*/
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

             
             
 			((Fragment_Activity)getActivity()).quote.set(pos,r);
             
 			((Fragment_Activity)getActivity()).quote.calculateData();
     		 
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
		((TextView) v.findViewById(R.id.t_price)).setText("");
		
		((TextView) v.findViewById(R.id.wkTotal1)).setText("");
		((TextView) v.findViewById(R.id.wkTotal2)).setText("");
		((TextView) v.findViewById(R.id.wkTotal3)).setText("");
		((TextView) v.findViewById(R.id.wkTotal4)).setText("");
		((TextView) v.findViewById(R.id.wkTotal5)).setText("");
	}
	
	void resetContractTotal(){
		clicked= false;

		((TextView) v.findViewById(R.id.Total1)).setText("");
		((TextView) v.findViewById(R.id.Total2)).setText("");
		((TextView) v.findViewById(R.id.Total3)).setText("");
		((TextView) v.findViewById(R.id.Total4)).setText("");
		((TextView) v.findViewById(R.id.Total5)).setText("");
	}
		
	public void calculateRowsTotal() {
		
		
		((TextView) v.findViewById(R.id.t_price)).setText(String.format("%.2f", ((Fragment_Activity)getActivity()).quote.total_price));
		
		if(show == 1|| show == 0 )
        	((TextView) v.findViewById(R.id.wkTotal1)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.payable_1));
		else
		((TextView) v.findViewById(R.id.wkTotal1)).setText("");

		if(show == 2  || show == 0)
			((TextView) v.findViewById(R.id.wkTotal2)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.payable_2));
		else
			((TextView) v.findViewById(R.id.wkTotal2)).setText("");

		if(show == 3  || show == 0)
			((TextView) v.findViewById(R.id.wkTotal3)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.payable_3));
		else
			((TextView) v.findViewById(R.id.wkTotal3)).setText("");

		if(show == 4  || show == 0)
			((TextView) v.findViewById(R.id.wkTotal4)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.payable_4));
		else
			((TextView) v.findViewById(R.id.wkTotal4)).setText("");

		if(show == 5  || show == 0)
			((TextView) v.findViewById(R.id.wkTotal5)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.payable_5));
		else
			((TextView) v.findViewById(R.id.wkTotal5)).setText("");

		/*
		((TextView) v.findViewById(R.id.wkTotal1)).setText(String.format("%.2f",quote.payable_1));
		((TextView) v.findViewById(R.id.wkTotal2)).setText(String.format("%.2f",quote.payable_2));
		((TextView) v.findViewById(R.id.wkTotal3)).setText(String.format("%.2f",quote.payable_3));
		((TextView) v.findViewById(R.id.wkTotal4)).setText(String.format("%.2f",quote.payable_4));
		((TextView) v.findViewById(R.id.wkTotal5)).setText(String.format("%.2f",quote.payable_5));
		*/
		if(clicked)
			showContractTotal();
    	 else
    		resetContractTotal();
	}
	
	void showContractTotal() {
		clicked = true;

		if(show == 1|| show == 0 )
        	((TextView) v.findViewById(R.id.Total1)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.total_payable_1));
		else
			((TextView) v.findViewById(R.id.Total1)).setText("");

		if(show == 2  || show == 0)
			((TextView) v.findViewById(R.id.Total2)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.total_payable_2));
		else
			((TextView) v.findViewById(R.id.Total2)).setText("");

		if(show == 3  || show == 0)
			((TextView) v.findViewById(R.id.Total3)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.total_payable_3));
		else
			((TextView) v.findViewById(R.id.Total3)).setText("");

		if(show == 4  || show == 0)
			((TextView) v.findViewById(R.id.Total4)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.total_payable_4));
		else
			((TextView) v.findViewById(R.id.Total4)).setText("");

		if(show == 5  || show == 0)
			((TextView) v.findViewById(R.id.Total5)).setText(String.format("%.2f",((Fragment_Activity)getActivity()).quote.total_payable_5));
		else
			((TextView) v.findViewById(R.id.Total5)).setText("");


		/*
		((TextView) v.findViewById(R.id.Total1)).setText(String.format("%.2f",quote.total_payable_1));
		((TextView) v.findViewById(R.id.Total2)).setText(String.format("%.2f",quote.total_payable_2));
		((TextView) v.findViewById(R.id.Total3)).setText(String.format("%.2f",quote.total_payable_3));
		((TextView) v.findViewById(R.id.Total4)).setText(String.format("%.2f",quote.total_payable_4));
		((TextView) v.findViewById(R.id.Total5)).setText(String.format("%.2f",quote.total_payable_5));		
		*/
	}
	

	
	
	public void getFieldsQ() {
		((Fragment_Activity)getActivity()).quote.cd.getFieldsQ(v);
	}

	
	public void setQuoteClientFields() {
		((Fragment_Activity)getActivity()).quote.cd.setQuoteClientFields(v);
	}

	public void setHeight() {
	    Utility.setListViewHeightBasedOnChildren(lv);			
	}

	public void setList() {
		lv.setAdapter(new QuoationListAdapter(((Fragment_Activity)getActivity()).quote.rows));
	    Utility.setListViewHeightBasedOnChildren(lv);		
	}
	
	
}
