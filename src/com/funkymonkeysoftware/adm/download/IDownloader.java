package com.funkymonkeysoftware.adm.download;

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
	
	/**
	 * Method used to actually do the downloading for an ADMDownload resource
	 * 
	 * @param dl <p>The download to get</p>
	 */
	public void downloadURL(ADMDownload dl) throws DownloadException;
	
}
