package com.funkymonkeysoftware.adm.download;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Parcel;
import android.os.Parcelable;

public class ADMDownload implements Parcelable{
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
	
	/**
	 * Flag raised if this download is selected in the GUI
	 */
	protected boolean selected = false;	
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public ADMDownload(String url, String status, boolean selected) throws MalformedURLException{
		this(new URL(url), status, selected);
	}
	
	public ADMDownload(URL theURL, String status, boolean selected){
		this.theURL = theURL;
		this.status = status;
		this.selected = selected;
	}

	public ADMDownload(Parcel in) {
		try {
			theURL = new URL(in.readString());
		} catch (MalformedURLException e) {
			theURL = null;
		}
		setStatus(in.readString());
		setLocalFile(new File(in.readString()));
		setTotalSize(in.readLong());
		setDownloadedSize(in.readLong());
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

	
	//------------------PARCEL API STUFF -------------------//
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getTheURL().toString());
		dest.writeString(getStatus());
		dest.writeString(getLocalFile().getAbsolutePath());
		dest.writeLong(getTotalSize());
		dest.writeLong(getDownloadedSize());
	}
	
    public static final Parcelable.Creator<ADMDownload> CREATOR
	    = new Parcelable.Creator<ADMDownload>() {
	public ADMDownload createFromParcel(Parcel in) {
	    return new ADMDownload(in);
	}
	
	public ADMDownload[] newArray(int size) {
	    return new ADMDownload[size];
	}
	};
}
