package com.funkymonkeysoftware.adm.download;

import java.net.MalformedURLException;
import java.util.LinkedList;

import com.funkymonkeysoftware.adm.DownloadsDBOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Internal representation of the ADM download list
 * 
 * <p>This class is used by ADM to represent a list of downloads that 
 * should be downloaded by the {@link DownloaderService}.</p>
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadModel {

	/**
	 * A list of downloads associated with this download model
	 */
	protected LinkedList<ADMDownload> activeDownloads;
	
	protected DownloadsDBOpenHelper dbhelper;
	
	public DownloadModel(Context c){
		//create a download database helper
		dbhelper = new DownloadsDBOpenHelper(c);
	}
	
	public void loadDownloads() throws MalformedURLException{
		
		//empty the activeDownloads list
		activeDownloads = new LinkedList<ADMDownload>();
		
		//get a connection to the downloads database
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		
		Cursor result =  db.rawQuery("SELECT * FROM downloads WHERE status IN" +
				"('pending','in progress','complete')", null);
		
		//while there are some downloads to be had
		while(result.moveToNext()){
			ADMDownload dl = new ADMDownload(
					result.getString(1),
					result.getString(2));
			
			activeDownloads.add(dl);
		}
	}
	
}
