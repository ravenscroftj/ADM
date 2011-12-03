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
import android.os.Parcelable;
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
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		//set up a pooled thread downloader for ADM
		es = Executors.newFixedThreadPool(5);
		//get pointer to the system notification service
		nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		return super.onStartCommand(intent, flags, startId);
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
				
				queueDownloads(intent.getParcelableArrayExtra("downloads"));
			}
			
		}
		
	}
	
	/**
	 * Given a list of URLS, enqueue and download them
	 * 
	 * @param urls <p>Add a list of downloads to the download
	 * queue.</p>
	 */
	protected void queueDownloads(Parcelable[] dls){
		
		Notification note = new Notification();
		
		note.flags = Notification.FLAG_ONGOING_EVENT;
		note.tickerText = String.format(
				String.valueOf(getText(R.string.notify_start_download)),
				dls.length);
		
		nm.notify(NOTIFY_START_DL, note);
		
		for(Parcelable p : dls){
				
				if( !(p instanceof ADMDownload))
					return;
				
				ADMDownload dl = (ADMDownload)p;
			
				if(dl.getLocalFile() == null){
					generateLocalFile(dl);
				}
			
				es.submit(new DownloadWorker(this, dl));
		}
	}
	
	public void generateLocalFile(ADMDownload dl){
		
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

	/**
	 * Test getting the download filename from the URL
	 * 
	 * @param downloadURL <p>The URL to check for a valid filename</p>
	 * @param localPath <p>The directory that the file will be stored in</p>
	 * @return
	 * @throws DownloadException
	 */
	public String getDownloadFilename(URL downloadURL, String localPath) throws DownloadException{
		
		if(downloadURL == null)
			throw new DownloadException("Cannot generate filename for null URL");
		
		String result = downloadURL.getFile();

		//first figure out the filename from the URL
		if(result.indexOf("?") > 0){

			result = downloadURL.getFile().substring(1, 
					downloadURL.getFile().indexOf("?"));
		}
		
		if(result.endsWith("/")){
			result = "index.html";
		}
		
		//find the file extension and file name
		String ext = result.substring(result.indexOf("."), result.length());
		//and the file name bit
		String name = result.substring(0, result.indexOf("."));
		
		File pathFile = new File(localPath + File.separator + name + ext);
		int i = 0;
		
		while(pathFile.exists()){
			pathFile = new File(localPath + File.separator + 
					name + "(" + String.valueOf(i) + ")"+ ext);
		}
		
		result = pathFile.getAbsolutePath();
		
		return result;
	}
	
	/**
	 * Method called when the download process has finished
	 * 
	 */
	public void OnDownloadComplete(DownloadEvent evt) {
		
		Notification n = new Notification();
		
		n.tickerText = String.format(
				String.valueOf(getText(R.string.notify_finished_download)),
				evt.getDownload().getLocalFile().getName());
		
		nm.notify(NOTIFY_FINISH_DL, n);
	}

	public void OnDownloadProgress(DownloadEvent evt) {
	}
	
	
}
