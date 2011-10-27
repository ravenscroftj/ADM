package com.funkymonkeysoftware.adm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.funkymonkeysoftware.adm.download.HTTPChecker;
import com.funkymonkeysoftware.adm.download.LinkChecker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
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

/**
 * User interface class for the link checker activity
 * 
 * @author James Ravenscroft
 *
 */
public class LinkCheckerActivity extends Activity implements OnClickListener{
	
	/**
	 * This is a reference to the add links button
	 */
	private Button addLinksBtn;
	
	/**
	 * Button that forces the link checker to go off and do its stuff
	 */
	private Button checkLinksBtn;
	
	/**
	 * Object designed to assist in the opening and closing of the db
	 */
	private DownloadsDBOpenHelper dbhelper;
	
	private ProgressDialog pdialog;
	

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
		
		checkLinksBtn = (Button)findViewById(R.id.checkLinksBtn);
		checkLinksBtn.setOnClickListener(this);
		
		//load the links
		loadLinks();
	}
	
	
	private void loadLinks(){
		//get the table to append things to
		TableLayout table = (TableLayout)findViewById(R.id.checkLinksTable);
	
		//empty the table view
		table.removeAllViews();
		
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
		}else if(v.equals(checkLinksBtn)){
				checkUrls();
		}
	}
	
	
	/**
	 * This function carries out initialisation of the link checker etc
	 */
	private void checkUrls(){
		
		//get a read only db
		SQLiteDatabase db = dbhelper.getReadableDatabase();
		
		Cursor c = db.rawQuery("SELECT * FROM downloads WHERE " +
				"status='unchecked' OR status='online' OR " +
				"status='offline'", null);
		
		//get all urls and store in array
		URL[] urls = new URL[c.getCount()];
		
		for(int i=0; c.moveToNext(); i++){
			try {
				urls[i] = new URL(c.getString(1));
			} catch (MalformedURLException e) {
				urls[i] = null;
			}
		}
		
		LinkCheckerTask task = new LinkCheckerTask();
		
		pdialog = new ProgressDialog(this);
		pdialog.setMax(c.getCount());
		pdialog.setProgress(0);
		
		//set progress dialog text
		pdialog.setMessage(String.format("Checking URL 1/%d", c.getCount()));
		
		//show the progress bar
		pdialog.show();
		
		//run the task
		task.execute(urls);		
	}
	
	
	/**
	 * This class  runs URLCheckers over all the URLs
	 * 
	 * @author James Ravenscroft
	 *
	 */
	private class LinkCheckerTask extends AsyncTask<URL, Integer, String[]>{

		URL[] theURLS;
		
		@Override
		protected String[] doInBackground(URL... params) {
		
			//make a reference to the url array
			theURLS = params;
			
			String[] result = new String[params.length];
			
			LinkChecker chk = new HTTPChecker();
			
			for(int i=0; i < params.length; i++){
				//check the status of the named url			
				try {
					result[i] = chk.checkURL(params[i]);
				} catch (IOException e) {
					//if there was an IO exception, assume it to be offline
					result[i] = "offline";
				}
				
				publishProgress(i+1);
			}
			
			return result;
		}

		/**
		 * When an update to the progress is made, update the shiny!
		 * 
		 */
		protected void onProgressUpdate(Integer... progress){
				pdialog.incrementProgressBy(1);
				//set text
				pdialog.setMessage(String.format("Checking URL %d/%d", 
						progress[0], theURLS.length));
		}
		
		protected void onPostExecute(String[] result){
			//update the database
			SQLiteDatabase db = dbhelper.getWritableDatabase();
			
			for(int i=0; i<result.length; i++){
				//set up a map for content values
				ContentValues values = new ContentValues();
				values.put("status", result[i]);
				String[] where = {theURLS[i].toString()};
				db.update("downloads", values, "url=?", where);
				
			}
			
			//close the database
			db.close();
			
			//hide the progress bar
			pdialog.hide();
			
			//rebuild the UI for this object
			loadLinks();
		}
	}
}
