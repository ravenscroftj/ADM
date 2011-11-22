package com.funkymonkeysoftware.adm.download;


/**
 * Class that does the actual downloading for ADM
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadWorker implements Runnable {
	
	protected DownloaderService parent;
	protected ADMDownload theDl;
	protected ADMDownloader downloader;
	
	public DownloadWorker(DownloaderService parent, ADMDownload theDl) {
		this.theDl = theDl;
		this.parent = parent;
	}
	
	@Override
	public void run() {
			//get a downloader to handle the download process
			downloader = ADMDownloaderFactory.getDownloader(theDl);
			
			downloader.addListener(parent);
			
			//do the actual downloading
			try {
				downloader.downloadURL(theDl);
			} catch (DownloadException e) {
				
			}
			
			downloader.removeListener(parent);
	}

}
