package dev.vision.relationshipninjas.Fragments;

import java.text.SimpleDateFormat;

import dev.vision.relationshipninjas.R;
import dev.vision.relationshipninjas.Classes.Item;
import dev.vision.relationshipninjas.interfaces.Data;
import dev.vision.relationshipninjas.purchase.Address;
import dev.vision.relationshipninjas.purchase.Card;
import dev.vision.relationshipninjas.purchase.Purchase;
import dev.vision.relationshipninjas.purchase.Purchase_Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class Purchase_Confirmation_Fragment extends Fragment implements Data{
	private Purchase purchase;
	
	EditText cc;
	EditText ba;
	TextView sa;
	LayoutInflater inf;
	private TextView preDate;

	private ListView lv;

	private TextView totalPric;

	private Button place;

	@Override
	public View onCreateView(LayoutInflater inf, ViewGroup container,Bundle savedInstanceState){
		super.onCreateView(inf, container, savedInstanceState);
		this.inf = inf;
		View v = inf.inflate(R.layout.purchase_confirmation, null);
		cc = (EditText) v.findViewById(R.id.cc);
		ba = (EditText) v.findViewById(R.id.ba);
		sa = (TextView) v.findViewById(R.id.sa);
		preDate = (TextView) v.findViewById(R.id.psd);
		totalPric = (TextView) v.findViewById(R.id.total_price);

		lv = (ListView) v.findViewById(R.id.listView1);

		place = (Button) v.findViewById(R.id.button1);
		
		place.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				purchase.placeOrder(getActivity());
				
			}
		});
		
		return v;	
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState); 
		setPurchase();
		setData();

		lv.setAdapter(new PurchaseItem_Adapter());
	}
	
	class PurchaseItem_Adapter extends BaseAdapter{

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View v = inf.inflate(R.layout.purchase_listitem, null);
			TextView name = (TextView) v.findViewById(R.id.name);
			TextView pri = (TextView) v.findViewById(R.id.price);
			TextView qty = (TextView) v.findViewById(R.id.qty);
			TextView qty_total = (TextView) v.findViewById(R.id.qty_total);

			Item i = purchase.getItem();
			
			name.setText(i.getName());
			pri.setText("$"+i.getPrice());
			qty.setText("1");
			qty_total.setText("$"+i.getPrice());

			return v;
		}
		
		
	}

	public void setData() {

		Address b_a =purchase.getBillingAddress();
		Address s_a =purchase.getPre_ShippingAddress();
		Card c = purchase.getCreditCard();
		String format = new SimpleDateFormat("EEEE, MMMM d, yyyy").format(purchase.getPreDate());

		cc.setText(c.getNumber());
		ba.setText(b_a.getAddressee() + ": " + b_a.getAddress1()+", "+ b_a.getAddress2()+", "+ b_a.getCity()+", "+ b_a.getState()+" "+ b_a.getZip()+" "+ b_a.getCountry());
		sa.setText(s_a.getAddressee() + ": " + s_a.getAddress1()+", "+ s_a.getAddress2()+", "+ s_a.getCity()+", "+ s_a.getState()+" "+ s_a.getZip()+" "+ s_a.getCountry());
		preDate.setText(format);
		totalPric.setText("$"+purchase.getItem().getPrice());
	}

	public void setPurchase() {
		purchase = ((Purchase_Activity)getActivity()).getPurchase();
	}
}
