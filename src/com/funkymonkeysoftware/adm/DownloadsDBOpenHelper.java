package com.funkymonkeysoftware.adm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for managing the opening of a connection to SQLite
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadsDBOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "adm";
	private static final int DATABASE_VERSION = 2;

	
	public DownloadsDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL("CREATE TABLE downloads (" +
				"_id INTEGER PRIMARY KEY," +
				"url TEXT," +
				"status TEXT," +
				"localPath TEXT," +
				"filesize INTEGER) ");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		int cVersion = oldVersion;
		
		while(cVersion < newVersion) {
			
			if(cVersion == 1){
				db.execSQL("ALTER TABLE downloads ADD filesize INTEGER");
			}
			
			cVersion++;
		}
	}

}
