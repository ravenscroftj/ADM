package com.funkymonkeysoftware.adm.download;

import com.funkymonkeysoftware.adm.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Service for initialising and running the actual download process
 * 
 * <p>This is the main service used for downloading links that are in 
 * the ADM database</p>
 * 
 * @author James Ravenscroft
 *
 */
public class DownloaderService extends Service{

	protected NotificationManager mNM;
	
	protected int NOTIFICATION_ID;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		//get the notification ID
		NOTIFICATION_ID = R.string.notification_id;
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		//make the service sticky i.e. keep running
		return START_STICKY;
	}
	
    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.notification_id);

        // Set the icon, scrolling text and timestamp
        Notification notification = new Notification(R.drawable.icon, text,
                System.currentTimeMillis());

    }

	
}
