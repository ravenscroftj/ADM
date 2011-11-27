package com.funkymonkeysoftware.adm.download;

import java.io.File;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.funkymonkeysoftware.adm.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;

/**
 * Service for initialising and running the actual download process
 * 
 * <p>This is the main service used for downloading links that are in 
 * the ADM database</p>
 * 
 * @author James Ravenscroft
 *
 */
public class DownloaderService extends IntentService implements IDownloadListener{
	
	public static final String CANCEL_ACTION = "adm.action.cancel";
	public static final String PAUSE_ACTION = "adm.action.pause";
	public static final String DOWNLOAD_ACTION = "adm.action.download";
	
	public static final int NOTIFY_START_DL = 1;
	public static final int NOTIFY_FINISH_DL = 2;
	
	/**
	 * Collection of executors ready to download things
	 */
	protected ExecutorService es;
	
	protected NotificationManager nm;
	
	/**
	 * Create a new ADM Downloader service instance
	 */
	public DownloaderService() {
		super("ADMDownloadService");
		
		//set up a pooled thread downloader for ADM
		es = Executors.newFixedThreadPool(5);
		//get pointer to the system notification service
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
	}
	
	/**
	 * Method is triggered when a new download intent is broadcast
	 * 
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		
		if(intent.getAction().equals(DOWNLOAD_ACTION)){
			
			
			//get the number of downloads and turn them into tasks
			if(intent.getIntExtra("downloads.queuesize", 0) > 0){
				queueDownloads((ADMDownload[])intent.getParcelableArrayExtra("downloads"));
			}
			
		}
		
	}
	
	/**
	 * Given a list of URLS, enqueue and download them
	 * 
	 * @param urls <p>Add a list of downloads to the download
	 * queue.</p>
	 */
	protected void queueDownloads(ADMDownload[] dls){
		
		Notification note = new Notification();
		
		note.flags = Notification.FLAG_ONGOING_EVENT;
		note.tickerText = String.format(
				String.valueOf(getText(R.string.notify_start_download)),
				dls.length);
		
		nm.notify(NOTIFY_START_DL, note);
		
		for(ADMDownload dl : dls){
				
				if(dl.getLocalFile() == null){
					
				}
			
				es.submit(new DownloadWorker(this, dl));
		}
	}
	
	protected void generateLocalFile(ADMDownload dl){
		
		if(Environment.getExternalStorageState().
				equals(Environment.MEDIA_MOUNTED)){
		
			//get the directory for the external media
			File sdcard = Environment.getExternalStorageDirectory();
			
			//get the path to the downloads dir
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			
			String dldir = prefs.getString("storage_path","Downloads");
			
			//get a file name from the URL
			String filename = "";
			
			//assemble the full path
			File localPath = new File(sdcard.getAbsolutePath() + 
					File.separator + dldir + File.separator + filename);
			
			//set the path for the download
			dl.setLocalFile(localPath);
		}
		
	}

	public String getDownloadFilename(URL downloadURL, String localPath) throws DownloadException{
		
		if(downloadURL == null)
			throw new DownloadException("Cannot generate filename for null URL");
		
		String result = "";
		
		//first figure out the filename from the URL
		
		return result;
	}
	
	/**
	 * Method called when the download process has finished
	 * 
	 */
	@Override
	public void OnDownloadComplete(DownloadEvent evt) {
		
		Notification n = new Notification();
		
		n.tickerText = String.format(
				String.valueOf(getText(R.string.notify_finished_download)),
				evt.getDownload().getLocalFile().getName());
		
		nm.notify(NOTIFY_FINISH_DL, n);
	}

	@Override
	public void OnDownloadProgress(DownloadEvent evt) {
	}
	
	
}
