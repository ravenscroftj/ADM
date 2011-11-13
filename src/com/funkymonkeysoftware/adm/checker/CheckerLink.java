package com.funkymonkeysoftware.adm.checker;

import java.net.MalformedURLException;
import java.net.URL;

public class CheckerLink {
	/**
	 * The actual URL of the link
	 */
	private URL url;
	/**
	 * The current state of the link: offline, online, unchecked
	 */
	private String status;
	/**
	 * Flag raised if this link is selected for an operation
	 */
	private boolean selected;
	
	/**
	 * The number of bytes that this content is
	 */
	private long contentLength;
	
	/**
	 * Create a new CheckerLink object with the necessary information
	 * 
	 * @param url {@link CheckerLink#url}
	 * @param status {@link CheckerLink#status}
	 * @param selected {@link CheckerLink#selected}
	 */
	public CheckerLink(URL url, String status, boolean selected, long clen){
		this.url = url;
		this.status = status;
		this.selected = selected;
		this.contentLength = clen;
	}
	
	public CheckerLink(String url, String status, boolean selected, long clen) 
	throws MalformedURLException{
		this(new URL(url), status, selected, clen);
	}
	
	public synchronized URL getURL(){
		return url;
	}
	
	public synchronized String getStatus(){
			return status;
	}
	
	public synchronized void setStatus(String status){
			this.status = status;
	}
	
	public synchronized boolean isSelected(){
		return selected;
	}

	public synchronized void setSelected(boolean s){
		selected = s;
	}
	
	public synchronized void setContentLength(long clen){
			this.contentLength = clen;
	}
	
	public synchronized long getContentLength(){
			return contentLength;
	}

}
