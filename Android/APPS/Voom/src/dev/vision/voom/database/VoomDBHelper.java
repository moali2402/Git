package dev.vision.voom.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class VoomDBHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "voom.db";
  private static final int DATABASE_VERSION = 1;


  public VoomDBHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    DB_Conversations.onCreate(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(VoomDBHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    
    DB_Conversations.onUpgrade(db, oldVersion, newVersion);
    
    onCreate(db);
  }

public Cursor getAllNotifications() {
	// TODO Auto-generated method stub
	return null;
}

public void insertNotification(String string) {
	// TODO Auto-generated method stub
	
}

} 