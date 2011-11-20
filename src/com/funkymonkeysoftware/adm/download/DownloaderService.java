package com.funkymonkeysoftware.adm.download;

import java.net.MalformedURLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.IntentService;
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
public class DownloaderService extends IntentService{
	
	public static final String CANCEL_ACTION = "cancel";
	public static final String DOWNLOAD_ACTION = "download";
	
	protected boolean running = false;

	protected ExecutorService es;
	
	
	/**
	 * Create a new ADM Downloader service instance
	 */
	public DownloaderService() {
		super("ADMDownloadService");
		
		//set up a pooled thread downloader for ADM
		es = Executors.newFixedThreadPool(5);

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
				queueDownloads(intent.getStringArrayExtra("downloads.urls"));
			}
			
		}
		
	}
	
	
	
	/**
	 * Given a list of URLS, enqueue and download them
	 * 
	 * @param urls <p>Add a list of downloads to the download
	 * queue.</p>
	 */
	protected void queueDownloads(String[] urls){
		for(String url : urls){
			try {
				es.submit(new DownloadWorker(url));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
}
