package dev.vision.relationshipninjas.remindme;

import java.util.Calendar;
import java.util.Date;

import dev.vision.relationshipninjas.remindme.model.Alarm;
import dev.vision.relationshipninjas.remindme.model.AlarmMsg;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author appsrox.com
 *
 */
public class MainActivity extends ListActivity {
	
//	private static final String TAG = "MainActivity";
	
	private SQLiteDatabase db;
	
	public final Calendar cal = Calendar.getInstance();
	public final Date dt = new Date();
	
	private Alarm alarm = new Alarm();
	private AlarmMsg alarmMsg = new AlarmMsg();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = RemindMe.db;
       
		
		
    }    
	
	
	
	
    
    @Override
	protected void onResume() {
		super.onResume();

	}
    



	
	void cancelNotification(long id){
		RemindMe.dbHelper.cancelNotification(db, id, false);
		
		Intent cancelThis = new Intent(this, AlarmService.class);
		cancelThis.putExtra(AlarmMsg.COL_ID, String.valueOf(id));
		cancelThis.setAction(AlarmService.CANCEL);
		startService(cancelThis);
	}
	
	void edit(long id){
		alarmMsg.setId(id);
		alarmMsg.load(db);
		alarm.reset();
		alarm.setId(alarmMsg.getAlarmId());
		alarm.load(db);
		
		//showDialog(R.id.menu_edit);
	}
	




	

	void deleteAll(){
		String startTime = ""+(cal.getTimeInMillis()-8600000);
		String endTime = ""+(cal.getTimeInMillis()+8600000);
		RemindMe.dbHelper.cancelNotification(db, startTime, endTime);
		
		Intent cancelAll = new Intent(this, AlarmService.class);
		cancelAll.putExtra(Alarm.COL_FROMDATE, startTime);
		cancelAll.putExtra(Alarm.COL_TODATE, endTime);
		cancelAll.setAction(AlarmService.CANCEL);
		startService(cancelAll);
	}

}