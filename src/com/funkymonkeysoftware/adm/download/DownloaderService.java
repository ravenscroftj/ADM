package com.funkymonkeysoftware.adm.download;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.funkymonkeysoftware.adm.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;

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
				es.submit(new DownloadWorker(this, dl));
		}
	}

	@Override
	public void OnDownloadComplete(DownloadEvent evt) {
		
		Notification n = new Notification();
		
		n.tickerText = String.format(
				String.valueOf(getText(R.string.notify_finished_download)),
				evt.getDownload().getLocalFile().getName());
	}

	@Override
	public void OnDownloadProgress(DownloadEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	
}
