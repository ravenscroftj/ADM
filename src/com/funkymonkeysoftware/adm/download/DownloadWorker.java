package com.funkymonkeysoftware.adm.download;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Class that does the actual downloading for ADM
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadWorker implements Callable<String> {
	
	protected URL theURL;
	
	public DownloadWorker(String url) throws MalformedURLException {
		this(new URL(url));
	}
	
	public DownloadWorker(URL url) {
		this.theURL = url;
	}
	
	/**
	 * Accessor method for the object's download url
	 * 
	 * @return {@link DownloadWorker#theURL}
	 */
	public synchronized String getURLString(){
		return this.theURL.toString();
	}
	

	public String call() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
