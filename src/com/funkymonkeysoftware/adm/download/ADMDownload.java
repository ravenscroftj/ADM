package com.funkymonkeysoftware.adm.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ADMDownload {
	/**
	 * This is the URL that the downloader is getting
	 */
	protected URL theURL;
	/**
	 * The status of the download - pending, downloading, complete, error etc
	 */
	protected String status;
	
	/**
	 * The local file that the download info should be stored inside
	 */
	protected File localFile;
	
	/**
	 * The total size that the download is supposed to be
	 */
	protected long totalSize = 0;
	
	/**
	 * The number of bytes that have been downloaded so far
	 */
	protected long downloadedSize = 0;
	
	
	public ADMDownload(String url, String status) throws MalformedURLException{
		this(new URL(url), status);
	}
	
	public ADMDownload(URL theURL, String status){
		this.theURL = theURL;
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public File getLocalFile() {
		return localFile;
	}

	public void setLocalFile(File localFile) {
		this.localFile = localFile;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public long getDownloadedSize() {
		return downloadedSize;
	}

	public void setDownloadedSize(long downloadedSize) {
		this.downloadedSize = downloadedSize;
	}

	public URL getTheURL() {
		return theURL;
	}

}
