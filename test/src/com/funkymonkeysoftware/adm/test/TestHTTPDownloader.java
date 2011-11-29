package com.funkymonkeysoftware.adm.test;

import java.io.File;
import java.net.MalformedURLException;

import android.os.Environment;
import android.test.AndroidTestCase;

import com.funkymonkeysoftware.adm.download.ADMDownload;
import com.funkymonkeysoftware.adm.download.DownloadEvent;
import com.funkymonkeysoftware.adm.download.DownloadException;
import com.funkymonkeysoftware.adm.download.HTTPDownloader;
import com.funkymonkeysoftware.adm.download.IDownloadListener;

public class TestHTTPDownloader extends AndroidTestCase implements IDownloadListener{

	private boolean downloadCompleted = false;
	private boolean downloadProgressed = false;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		downloadCompleted = false;
		downloadProgressed = false;
	}

	/**
	 * Test to ensure that a valid URL is downloaded by the HTTPDownloader
	 */
	public void testValidDownload(){
		
		ADMDownload dl = null;
		try {
			dl = new ADMDownload("http://www.google.com", "pending", true);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		dl.setLocalFile(new File(Environment.getExternalStorageDirectory() + 
				File.separator + "google.html"));
		
		HTTPDownloader dlm = new HTTPDownloader();
		
		dlm.addListener(this);
		
		try {
			dlm.downloadURL(dl);
		} catch (DownloadException e) {
			fail(e.getMessage());
		}
		
		dlm.removeListener(this);
		
		
		//make sure that the file downloaded correctly
		assert(downloadCompleted && downloadProgressed);
	}
	
	@Override
	public void OnDownloadComplete(DownloadEvent evt) {
		downloadCompleted = true;
	}

	@Override
	public void OnDownloadProgress(DownloadEvent evt) {
		downloadProgressed = true;
	}
	
	//-----------------------------Test Methods------------------------------//
	

}
