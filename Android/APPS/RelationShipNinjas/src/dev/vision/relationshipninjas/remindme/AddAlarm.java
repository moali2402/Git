package dev.vision.relationshipninjas.remindme;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import dev.vision.relationshipninjas.Classes.Event;
import dev.vision.relationshipninjas.remindme.model.Alarm;
import dev.vision.relationshipninjas.remindme.model.AlarmMsg;
import dev.vision.relationshipninjas.remindme.model.AlarmTime;
import dev.vision.relationshipninjas.remindme.model.DbHelper;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.TimePicker;


/**
 * @author appsrox.com
 *
 */
public class AddAlarm {
	
//	private static final String TAG = "AddAlarmActivity";
	
	private SQLiteDatabase db;
	
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat();
	
	
	
    public AddAlarm() {
        db = RemindMe.db;
		sdf.applyPattern(RemindMe.getDateFormat());
       
    }
    

	
	/* Save */
	public void create(Context c, Event ev, String msg, boolean sound, Calendar d) {
		
		Alarm alarm = new Alarm();
		alarm.setName(msg);
		alarm.setSound(sound);
		AlarmTime alarmTime = new AlarmTime();
		long alarmId = Long.parseLong(ev.getRelationshipid());
		
		
		alarm.setFromDate(DbHelper.getDateStr(d.get(Calendar.YEAR), d.get(Calendar.MONTH)+1, d.get(Calendar.DATE)));
		alarmId = alarm.persist(db);
		
		alarmTime.setAt(DbHelper.getTimeStr(0, 0));
		alarmTime.setAlarmId(alarmId);
		alarmTime.persist(db);
			
		
		
		Intent service = new Intent(c, AlarmService.class);
		service.putExtra(AlarmMsg.COL_ALARMID, String.valueOf(alarmId));
		service.setAction(AlarmService.POPULATE);
		c.startService(service);

		//finish();
	}



	public void create(Context c, Event ev, boolean sound) {
		Alarm alarm = new Alarm();
		alarm.setName(ev.getName());
		alarm.setSound(sound);
		AlarmTime alarmTime = new AlarmTime();
		long alarmId = 0;
		//DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
		try {
			
			date = sdf.parse(ev.getDate());
		
			Calendar d = DateToCalendar(date);
			d.add(Calendar.DATE, -3);
			System.out.println(DbHelper.getDateStr(d.get(Calendar.YEAR), d.get(Calendar.MONTH)+1, d.get(Calendar.DATE)));
			//d = Calendar.getInstance();
			alarm.setFromDate(DbHelper.getDateStr(d.get(Calendar.YEAR), d.get(Calendar.MONTH)+1, d.get(Calendar.DATE)));
			alarmId = alarm.persist(db);
			
			alarmTime.setAt(DbHelper.getTimeStr(Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE)+2));
			alarmTime.setAlarmId(alarmId);
			alarmTime.persist(db);
				
			Intent service = new Intent(c, AlarmService.class);
			service.putExtra(AlarmMsg.COL_ALARMID, String.valueOf(alarmId));
			service.setAction(AlarmService.POPULATE);
			c.startService(service);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
    
	public static Calendar DateToCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
}
