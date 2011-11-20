package com.funkymonkeysoftware.adm.checker;

import java.io.IOException;

import com.funkymonkeysoftware.adm.IDownloaderComponent;

public interface ILinkChecker extends IDownloaderComponent{

	/**
	 * Run a check on a given URL returning its online or offline status
	 *
	 * @throws IOException <p>If there is no connectivity, this exception is thrown</p>
	 * 
	 * @param theUrl <p>the URL to be checked, must be compatible (match with 
	 * 				 the string returned from {@link ILinkChecker#getMatchPattern()}</p>
	 * @return <p>'online' if URL is found, 'offline' if there is a problem.</p>
	 */
	public void checkURL(CheckerLink link) throws IOException;
	
}
