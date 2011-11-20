package com.funkymonkeysoftware.adm.download;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Class used to display downloads in the ADM activity
 * 
 * @author James Ravenscroft
 *
 */
public class DownloadRow extends TableRow {

	/**
	 * The download that this row represents
	 */
	ADMDownload download;
	
	public DownloadRow(Context context, ADMDownload download) {
		super(context);
		
		//set a pointer to the download
		this.download = download;
		
		drawControl();
	}
	
	protected void drawControl(){
		
		//remove all child views from this row
		removeAllViews();
		
		//add the url
		TextView urlBox = new TextView(getContext());
		urlBox.setText(download.getTheURL().toString());
		
		//add the status of the download
		TextView statusBox = new TextView(getContext());
		//depending on the text, set colour

		if(download.getStatus().equals("pending") || 
				download.getStatus().equals("in progress")){
			statusBox.setTextColor(Color.YELLOW);
		}else if(download.getStatus().equals("complete")){
			statusBox.setTextColor(Color.GREEN);
		}else if(download.getStatus().equals("offline")){
			statusBox.setTextColor(Color.RED);
		}
		
		statusBox.setText(download.getStatus());
		
		//set up layout for row
		TableRow.LayoutParams tl = new TableRow.LayoutParams();
		tl.column = 0;
		tl.width = LayoutParams.FILL_PARENT;
		tl.gravity = Gravity.LEFT;
		addView(urlBox, tl);
		
		tl = new TableRow.LayoutParams();
		tl.column = 1;
		tl.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
		addView(statusBox, tl);
	}

}
