package com.funkymonkeysoftware.adm.download;

import java.util.LinkedList;

/**
 * Abstract implementation of the IDownloader interface with event dispatch
 * 
 * @author James Ravenscroft
 *
 */
public abstract class ADMDownloader implements IDownloader {

	protected LinkedList<IDownloadListener> listeners;

	/**
	 * Add a download listener from the downloader
	 * 
	 * @param l <p>The listener to add to the list</p>
	 */
	public synchronized void addListener(IDownloadListener l){
		listeners.add(l);
	}
	
	/**
	 * Remove a download listener from the downloader
	 * 
	 * @param l <p>The listener to be removed</p>
	 */
	public synchronized void removeListener(IDownloadListener l){
		listeners.remove(l);
	}
	
	/**
	 * Method called when progress is made on a specific download
	 * 
	 * @param dl <p>The download that has progressed.</p>
	 */
	protected synchronized void fireDownloadProgressEvent(ADMDownload dl){
		
		DownloadEvent evt = new DownloadEvent(this, dl);
		
		for(IDownloadListener l : listeners){
			l.OnDownloadProgress(evt);
		}
	}
	
	/**
	 * Method called when a download has finished downloading
	 * 
	 * @param dl <p>The download that has finished.</p>
	 */
	protected synchronized void fireDownloadFinishedEvent(ADMDownload dl){
		
		DownloadEvent evt = new DownloadEvent(this, dl);
		
		for(IDownloadListener l : listeners){
			l.OnDownloadComplete(evt);
		}
	}

}
