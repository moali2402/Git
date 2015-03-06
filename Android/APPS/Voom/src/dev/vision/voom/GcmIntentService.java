package dev.vision.voom;

import java.util.ArrayList;

import dev.vision.voom.activity.LoginActivity;
import dev.vision.voom.database.VoomDBHelper;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.InboxStyle;

public class GcmIntentService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        mNotificationManager = (NotificationManager)
            this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification noti;
        
        noti = getNotifications();      
        
        mNotificationManager.notify(NOTIFICATION_ID, noti);

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

	private Notification getNotifications() {
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), 0);
		
		VoomDBHelper dbHelper = new VoomDBHelper(this);
        dbHelper.getReadableDatabase();
        Cursor notifications = dbHelper.getAllNotifications();
        //dbHelper.insertNotification("");
        
        ArrayList<String> m = new ArrayList<String>();
        while(notifications.moveToNext()){
        	
        }
    	
    	return buildNotification("Voom", "New Message!", m, contentIntent);
	}
	
    private Notification buildNotification(String title, String text, ArrayList<String> m, PendingIntent contentIntent){

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);

        Notification n = new NotificationCompat.InboxStyle(mBuilder)
        		.setBigContentTitle("New Vooms!")
            	.addLine("New Message!")
            	.addLine("New Message!")
                .setSummaryText("More messages")
                .build();
        
        return n;
    }

}

