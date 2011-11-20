package com.funkymonkeysoftware.adm.download;

import java.net.MalformedURLException;
import java.util.LinkedList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.funkymonkeysoftware.adm.DownloadsDBOpenHelper;

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
	
	/**
	 * Method used to get a clone of the downloads list for use in the UI
	 * @return
	 */
	public synchronized LinkedList<ADMDownload> getDownloads() {
		return new LinkedList<ADMDownload>(activeDownloads);
	}
	
	/**
	 * Method used to force the download model to refresh its database data
	 * 
	 * @throws MalformedURLException
	 */
	public synchronized void loadDownloads() throws MalformedURLException{
		
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
