package com.funkymonkeysoftware.adm.download;

import java.util.EventListener;

public interface IDownloadListener extends EventListener {

	/**
	 * This method is called when a download is finished
	 * 
	 * @param evt <p>The Download event that contains information on the 
	 * download that has finished</p>
	 */
	public void OnDownloadComplete(DownloadEvent evt);
	
	/**
	 * Method called when a download is progressing
	 * 
	 * @param evt <p>The event information on the download that has updated
	 * its progress</p>
	 */
	public void OnDownloadProgress(DownloadEvent evt);

}
