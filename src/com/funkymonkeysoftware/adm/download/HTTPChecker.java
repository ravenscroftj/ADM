package com.funkymonkeysoftware.adm.download;

import java.io.IOException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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

	/**
	 * This is where the checker actually does its magic!
	 */
	public String checkURL(URL theUrl) throws IOException {
		//build a HTTP getter for the 
		HttpClient client = new DefaultHttpClient();
		//build a HEAD request for the url
		HttpHead h = new HttpHead(theUrl.toString());
		//get head
		HttpResponse resp = client.execute(h);
		
		if(resp.getStatusLine().getStatusCode() == 200 ){
			return "online";
		}else{
			return "offline";
		}
	}

}
