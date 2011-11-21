package com.funkymonkeysoftware.adm.download;

import java.util.EventObject;

/**
 * Event passed to any IDownloadListeners
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	protected ADMDownload download;
	
	public DownloadEvent(Object source, ADMDownload download) {
		super(source);
		
		this.download = download;
	}
	
	/**
	 * Return the download associated with this event
	 * 
	 * @return
	 */
	public ADMDownload getDownload(){
		return this.download;
	}

}
