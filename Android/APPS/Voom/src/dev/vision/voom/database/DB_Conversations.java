package dev.vision.voom.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DB_Conversations extends DB{
	
	class  ConversationsEntry implements BaseColumns{
		static final String TABLE_NAME = "conversation";
	}
	
	// Database creation sql statement
    private static final String DATABASE_CREATE = 
		CREATE_TABLE + ConversationsEntry.TABLE_NAME + "("
	  + COLUMN_ID + INTEGER + PRIMARY_KEY + "AUTOINCREMENT " + COMMA
	  + " text NOT NULL);";

    public static void onCreate(SQLiteDatabase database) {
    	database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    	database.execSQL("DROP TABLE IF EXISTS " + ConversationsEntry.TABLE_NAME);
    }	
}