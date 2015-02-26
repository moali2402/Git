/**
 * 
 */
package dev.vision.split.it.activities;

import java.util.Random;

import dev.vision.split.it.R;
import dev.vision.split.it.Restaurant;
import dev.vision.split.it.Restaurant.Restaurant_Interface;
import dev.vision.split.it.extras.PullScrollView;
import dev.vision.split.it.extras.RectangleView;
import dev.vision.split.it.extras.PullScrollView.OnTurnListener;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

/**
 * @author Mo
 *
 */
public class Restaurant_Activity extends Activity implements Restaurant_Interface, OnTurnListener{

	Restaurant rs;
	private PullScrollView mScrollView;
	private RectangleView mHeadImg;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ac_pull);
		//rs = (Restaurant) getIntent().getExtras().getSerializable("Restaurant");
		//createTable();
		//rs.setOnTableListener(this);
	
	}
	
	public void createTable(){
		rs.createNewTable(String.valueOf(new Random().nextInt()));
	}
	
	public void joinTable(int no){
		rs.joinTable(no); //virtual String.valueOf(new Random().nextInt())
	}
	
	class createTable extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// getTables from server with users
			rs.setTables(rs.getTables());
			return null;
		}
	}
	
	
	class GetTables extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// getTables from server with users
			rs.setTables(rs.getTables());
			return null;
		}
	}


	@Override
	public void onTableChange() {
		
	}

	@Override
	public void onIJoin() {
		
	}

	@Override
	public void onTurn() {
		// TODO Auto-generated method stub
		
	}

}
