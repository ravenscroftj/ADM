package com.funkymonkeysoftware.adm.download;

import java.io.File;
import java.net.URL;

import com.funkymonkeysoftware.adm.IDownloaderComponent;

/**
 * Interface implemented by any downloader plugins
 * 
 * @author James Ravenscroft
 *
 */
public interface IDownloader extends IDownloaderComponent{

	/**
	 * Method returns true if this downloader supports resume function
	 * 
	 * @return
	 */
	public boolean isResumable();
	
	public void downloadURL(URL theURL, File localFile);
	
}
