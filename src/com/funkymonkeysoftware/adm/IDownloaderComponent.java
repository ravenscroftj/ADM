package com.funkymonkeysoftware.adm;

/**
 * Interface for linkcheckers and downloader plugins for ADM
 * 
 * <p>This interface is used for figuring out which downloader component 
 * should be used for downloading or checking an URL within ADM.</p>
 * 
 * <p>When a download is checked or downloaded, ADM finds a downloader
 * component that A) can match the provided regex pattern and B)
 * is of the highest priority available.</p>
 * 
 * @author James Ravenscroft
 *
 */
public interface IDownloaderComponent {
	/**
	 * Return a REGEX pattern that can be used to check compatibility with an URL
	 * @return
	 */
	public String getMatchPattern();
	
	/**
	 * Determines the order in which checkers are matched to an URL.
	 * 
	 * @return <p> Return the component priority lower means less important</p>
	 */
	public Integer getPriority();
}
