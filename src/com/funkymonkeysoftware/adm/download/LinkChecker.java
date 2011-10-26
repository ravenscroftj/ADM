package com.funkymonkeysoftware.adm.download;

import java.io.IOException;
import java.net.URL;

public interface LinkChecker {

	/**
	 * Return a REGEX pattern that can be used to check compatibility with an URL
	 * @return
	 */
	public String getMatchPattern();
	
	/**
	 * Determines the order in which checkers are matched to an URL.
	 * 
	 * @return <p> Return the Link Checker priority lower means less important</p>
	 */
	public Integer getPriority();
	
	/**
	 * Run a check on a given URL returning its online or offline status
	 *
	 * @throws IOException <p>If there is no connectivity, this exception is thrown</p>
	 * 
	 * @param theUrl <p>the URL to be checked, must be compatible (match with 
	 * 				 the string returned from {@link LinkChecker#getMatchPattern()}</p>
	 * @return <p>'online' if URL is found, 'offline' if there is a problem.</p>
	 */
	public String checkURL(URL theUrl) throws IOException;
	
}
