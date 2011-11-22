package com.funkymonkeysoftware.adm.download;

/**
 * Exception thrown by downloader when there is a problem
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadException extends Exception {
	private static final long serialVersionUID = 1L;

	public DownloadException(String message) {
		super(message);
	}
	
	public DownloadException(Throwable e){
		super(e);
	}
	
	public DownloadException(String message, Throwable e){
		super(message,e);
	}
}
