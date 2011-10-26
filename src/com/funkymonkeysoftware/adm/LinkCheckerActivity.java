package com.funkymonkeysoftware.adm;

import java.net.URL;

import com.funkymonkeysoftware.adm.download.LinkChecker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LinkCheckerActivity extends Activity implements OnClickListener{
	
	private Button addLinksBtn;
	private DownloadsDBOpenHelper dbhelper;
	
	/**
	 * This class  runs URLCheckers over all the URLs
	 * 
	 * @author James Ravenscroft
	 *
	 */
	private class LinkCheckerTask extends AsyncTask<URL, Integer, String[]>{

		@Override
		protected String[] doInBackground(URL... params) {
		
			String[] result = new String[params.length];
			
			for(int i=0; i < params.length; i++){
				//check the status of the named url
				URL current = params[i];
				
				//TODO: integrate checker manager here that runs different checkers
				LinkChecker chk;
			}
			
			return result;
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set up the link checker
		setContentView(R.layout.linkchecker);
		
		//initialise the database helper
		dbhelper = new DownloadsDBOpenHelper(this);
		
		//set up as listener for buttons
		addLinksBtn = (Button)findViewById(R.id.addLinksBtn);
		addLinksBtn.setOnClickListener(this);
		
		//load the links
		loadLinks();
	}
	
	private void loadLinks(){
		//get the table to append things to
		TableLayout table = (TableLayout)findViewById(R.id.checkLinksTable);
	
		//get all downloads that are unchecked or online or offline
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		Cursor c = db.rawQuery("SELECT * FROM downloads WHERE " +
				"status='unchecked' OR status='online' OR " +
				"status='offline'", null);
		
		if(c.getCount() < 1) {
			//provide some kind of error message
			TextView error = new TextView(this);
			TableRow r = new TableRow(this);
			error.setText("No URLS, consider adding some!");
			r.addView(error);
			table.addView(r);
		}else{
			
			while(c.moveToNext()){
				
				TableRow tr = new TableRow(this);
				TextView url = new TextView(this);
				url.setText(c.getString(1));
				
				
				TextView statusBox = new TextView(this);
				//depending on the text, set colour
				String status = c.getString(2);
				
				if(status.equals("unchecked")){
					statusBox.setTextColor(Color.YELLOW);
				}else if(status.equals("online")){
					statusBox.setTextColor(Color.GREEN);
				}else if(status.equals("offline")){
					statusBox.setTextColor(Color.RED);
				}
				
				statusBox.setText(status);
				
				tr.addView(url);
				tr.addView(statusBox);
				table.addView(tr);
				
			}
			
		}
	}
	
	

	@Override
	public void onClick(View v) {
		if(v.equals(addLinksBtn)){
			//show the add links view
			startActivity(new Intent(this, LinkInputActivity.class));
		}
	}
	
}
