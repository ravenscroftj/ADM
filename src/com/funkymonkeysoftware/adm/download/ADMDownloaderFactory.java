package com.funkymonkeysoftware.adm.download;

public class ADMDownloaderFactory {
	
	public static ADMDownloader getDownloader(ADMDownload dl){
		return new HTTPDownloader();
	}

}
