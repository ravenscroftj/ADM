package com.funkymonkeysoftware.adm.download;

import java.net.MalformedURLException;
import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
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
	
	/**
	 * SQLite database helper object used to open db connections
	 */
	protected DownloadsDBOpenHelper dbhelper;
	
	/**
	 * The context that this model belongs to
	 */
	protected Context context;
	
	public DownloadModel(Context c){
		dbhelper = new DownloadsDBOpenHelper(c);
		context = c;
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
					result.getString(2),
					true);
			
			activeDownloads.add(dl);
		}
		
		//close database connection
		db.close();
	}
	
	/**
	 * Method used to start the downloads process
	 * 
	 */
	public synchronized void downloadSelected() {
		
		Intent dlIntent = new Intent(DownloaderService.DOWNLOAD_ACTION);
		
		LinkedList<ADMDownload> result = new LinkedList<ADMDownload>();
		
		for(ADMDownload dl : activeDownloads){
			if(dl.isSelected()){
				result.add(dl);
			}
		}
		
		dlIntent.putExtra("downloads", 
				result.toArray(new ADMDownload[result.size()]));
		
		context.startService(dlIntent);
	}
	
	public synchronized void removeDownload(ADMDownload dl) {
		
		//get a connection to the downloads database
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		
		//remove the download from the table
		db.delete("downloads", "url=?", new String[]{dl.getTheURL().toString()});
		
		//remove the download from the list 
		activeDownloads.remove(dl);
	}
}
