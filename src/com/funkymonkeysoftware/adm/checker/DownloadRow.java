package com.funkymonkeysoftware.adm.checker;

import java.io.File;
import java.net.URL;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Class used to display a download and associated status on link checker
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadRow extends TableRow {
	
	private CheckBox cbox;
	
	private CheckerLink theLink;
	
	public DownloadRow(Context context, CheckerLink theLink) {
		super(context);

		//get reference to checker link
		this.theLink = theLink;
		
		//now set up display
		configureDisplay();
		
		setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setSelected(!getSelected());
			}
		});
	}
	
	/**
	 * Set whether this element is checked or not
	 * 
	 */
	public void setSelected(boolean s){
		cbox.setChecked(s);
	}
	
	/**
	 * Return whether this element is checked or not
	 * 
	 * @return True if the download is selected
	 */
	public boolean getSelected(){
		return cbox.isChecked();
	}
	
	/**
	 * Internal function used to lay out this view
	 */
	private void configureDisplay(){
		
		TextView urlBox = new TextView(getContext());
		
		if(theLink.getURL().toString().length() > 25){
			URL theURL = theLink.getURL();
			
			File theFile = new File( theURL.getFile());
			String displayText = theURL.getProtocol() + "://" +
					theURL.getHost() + 
					"..." + theFile.getName();
			
			urlBox.setText(displayText);
		}else{
			urlBox.setText(theLink.getURL().toString());
		}
		
		

		TextView statusBox = new TextView(getContext());
		//depending on the text, set colour

		if(theLink.getStatus().equals("unchecked")){
			statusBox.setTextColor(Color.YELLOW);
		}else if(theLink.getStatus().equals("online")){
			statusBox.setTextColor(Color.GREEN);
		}else if(theLink.getStatus().equals("offline")){
			statusBox.setTextColor(Color.RED);
		}
		
		statusBox.setText(theLink.getStatus());
		
		//add row for download size
		TextView dlSize = new TextView(getContext());
		dlSize.setText(getFriendlySize());
		
		//add checkbox
		cbox = new CheckBox(getContext());
		
		//box is initially checked
		cbox.setChecked(true);

		//set up layout for row
		TableRow.LayoutParams tl = new TableRow.LayoutParams();
		tl.column = 0;
		tl.gravity = Gravity.LEFT;
		addView(cbox, tl);
		
		tl = new TableRow.LayoutParams();
		tl.column = 1;
		tl.width = LayoutParams.FILL_PARENT;
		tl.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		addView(urlBox, tl);
		
		tl = new TableRow.LayoutParams();
		tl.column = 2;
		tl.gravity= Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
		addView(statusBox, tl);
		
		tl = new TableRow.LayoutParams();
		tl.column = 3;
		tl.gravity= Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		addView(dlSize, tl);
		
	}
	
	protected String getFriendlySize() {
		String status = "-";
		
		String[] suffixes = {"B", "KiB","MiB","GiB"};
		
		long size = theLink.getContentLength();
		int dIndex = 0;
		
		while(size > 1024 && dIndex < suffixes.length) {
			size /= 1024;
			dIndex ++;
		}
		
		if(size > 0){
			status = String.format("%d %s", size, suffixes[dIndex]);
		}

		return status;
	}

	public String getURL(){ 
		return theLink.getURL().toString();
	}
	
	public String getStatus() {
		return theLink.getStatus();
	}
	
}
