package com.funkymonkeysoftware.adm.checker;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;


/**
 * HTTP checker - checks http status code of given url
 * 
 * @author James Ravenscroft
 *
 */
public class HTTPChecker implements LinkChecker {

	public String getMatchPattern() {
		return "http(s?)\\://\\S+";
	}

	/**
	 * HTTP checker is a base module and therefore has low priority
	 */
	public Integer getPriority() {
		return 0;
	}
	
	public void checkURL(CheckerLink link) throws IOException {
		//build a HTTP getter for the 
		HttpClient client = new DefaultHttpClient();
		//build a HEAD request for the url
		HttpHead h = new HttpHead(link.getURL().toString());
		//get head
		HttpResponse resp = client.execute(h);
		
		//find out how long the document is
		Header clen = resp.getFirstHeader("Content-Length");
		
		
		long contentLength;
		try{
			contentLength = Long.valueOf(clen.getValue());
		}catch(Exception e){
			contentLength = -1;
		}
		String status  = 
			resp.getStatusLine().getStatusCode() == 200 ? "online" : "offline";
		
		
		link.setStatus(status);
		link.setContentLength(contentLength);
	}


}
