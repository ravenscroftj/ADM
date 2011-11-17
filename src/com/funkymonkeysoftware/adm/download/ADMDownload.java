package com.funkymonkeysoftware.adm.download;

import java.io.File;
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

}
