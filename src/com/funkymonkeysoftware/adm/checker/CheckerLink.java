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
	 * Create a new CheckerLink object with the necessary information
	 * 
	 * @param url {@link CheckerLink#url}
	 * @param status {@link CheckerLink#status}
	 * @param selected {@link CheckerLink#selected}
	 */
	public CheckerLink(URL url, String status, boolean selected){
		this.url = url;
		this.status = status;
		this.selected = selected;
	}
	
	public CheckerLink(String url, String status, boolean selected) throws MalformedURLException{
		this(new URL(url), status, selected);
	}
	
	public URL getURL(){
		return url;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setStatus(String status){
		this.status = status;
	}
	
	public boolean isSelected(){
		return selected;
	}
	
	public void setSelected(boolean s){
		selected = s;
	}
}
