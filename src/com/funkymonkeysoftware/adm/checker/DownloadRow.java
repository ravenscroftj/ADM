package com.funkymonkeysoftware.adm.checker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path.FillType;
import android.view.Gravity;
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

	private String url;
	private String status;
	
	private CheckBox cbox;
	
	public DownloadRow(Context context, String url, String status) {
		super(context);
		setURL(url);
		setStatus(status);

		
		//now set up display
		configureDisplay();
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
		urlBox.setText(url);

		TextView statusBox = new TextView(getContext());
		//depending on the text, set colour

		if(status.equals("unchecked")){
			statusBox.setTextColor(Color.YELLOW);
		}else if(status.equals("online")){
			statusBox.setTextColor(Color.GREEN);
		}else if(status.equals("offline")){
			statusBox.setTextColor(Color.RED);
		}
		
		statusBox.setText(status);
		
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
		tl.gravity= Gravity.RIGHT | Gravity.CENTER_VERTICAL;
		addView(statusBox, tl);
	}

	public String getURL(){ 
		return url;
	}
	
	public void setURL(String url){
		this.url = url;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status){
		this.status = status;
	}
	
}
