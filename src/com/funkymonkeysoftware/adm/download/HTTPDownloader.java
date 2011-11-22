package com.funkymonkeysoftware.adm.download;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Basic HTTP Downloader, mainly for Proof of concept
 * 
 * @author James Ravenscroft
 *
 */
public class HTTPDownloader extends ADMDownloader {
	
	@Override
	public boolean isResumable() {
		return false;
	}

	@Override
	public void downloadURL(ADMDownload dl) throws DownloadException {
		if( dl.getLocalFile() == null){
			throw new DownloadException("Could not download, no local path set.");
		}
		//do the download and catch any exceptions - throw download exception
		try {
			doDownload(dl);
		} catch (Exception e) {
			throw new DownloadException(e);
		}
	}

	/**
	 * Do the actual downloading process, fires events too!
	 * 
	 * @param dl
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected void doDownload(ADMDownload dl) 
			throws ClientProtocolException, IOException {
		//build a HTTP getter for the 
		HttpClient client = new DefaultHttpClient();
		//build a GET request for the URL
		HttpGet get = new HttpGet(dl.getTheURL().toString());
		//get head
		HttpResponse resp = client.execute(get);
		
		HttpEntity dlEnt = resp.getEntity();
		
		InputStream in = dlEnt.getContent();
		
		FileOutputStream fout = new FileOutputStream(dl.getLocalFile());
		
		byte[] buffer = new byte[4098];
		
		long downByteCount = 0;
		int bytesRead = 0;
		
		while( (bytesRead = in.read(buffer) ) > -1){
			downByteCount += bytesRead;
			fout.write(buffer, 0, bytesRead);
			dl.setDownloadedSize(downByteCount);
			fireDownloadProgressEvent(dl);
		}
		
		fireDownloadFinishedEvent(dl);
	}
	
	@Override
	public String getMatchPattern() {
		return "http(s?)\\://\\S+";
	}

	@Override
	public Integer getPriority() {
		return 0;
	}

}
