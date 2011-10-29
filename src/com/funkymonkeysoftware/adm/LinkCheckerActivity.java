package com.funkymonkeysoftware.adm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.funkymonkeysoftware.adm.checker.DownloadRow;
import com.funkymonkeysoftware.adm.download.HTTPChecker;
import com.funkymonkeysoftware.adm.download.LinkChecker;

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
	 * Button that removes all the offline links
	 */
	private Button removeOfflineBtn;
	
	/**
	 * Button for selecting or deselecting all urls
	 */
	private Button selectAllBtn;
	
	/**
	 * Object designed to assist in the opening and closing of the db
	 */
	private DownloadsDBOpenHelper dbhelper;
	
	/**
	 * A progress dialog shown for when the link checker is actually running
	 */
	private ProgressDialog pdialog;
	
	/**
	 * Flag determines whether all links are selected or not
	 */
	private boolean selectAll = true;
	
	private LinkedList<DownloadRow> rows;
	

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
		
		removeOfflineBtn = (Button)findViewById(R.id.removeOfflineBtn);
		removeOfflineBtn.setOnClickListener(this);
		
		selectAllBtn = (Button)findViewById(R.id.toggleSelectAllBtn);
		selectAllBtn.setOnClickListener(this);
		
		//initialise list of rows
		rows = new LinkedList<DownloadRow>();
		
		//load the links
		loadLinks();
	}
	
	
	private void loadLinks(){
		//get the table to append things to
		TableLayout table = (TableLayout)findViewById(R.id.checkLinksTable);
	
		table.setColumnStretchable(1, true);
		
		//set select all to none (since all links are initially selected)
		selectAll = true;
		selectAllBtn.setText(R.string.select_none);
		
		//empty the table view
		table.removeAllViews();
		rows.clear();
		
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
				
				DownloadRow tr = new DownloadRow(this, c.getString(1), c.getString(2));
				
				//add the row to the table and the rows list
				rows.add(tr);
				
				table.addView(tr, new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, 
						LayoutParams.FILL_PARENT));
				
			}
			
		}
	}
	
	/**
	 * Called when the user presses select all/none button
	 */
	private void toggleSelectAll(){
		
		//swap the flag
		selectAll = !selectAll;
		
		//see what to display on the select button
		int id = selectAll ? R.string.select_none : R.string.select_all;
		
		//set the new text
		selectAllBtn.setText(id);
		
		//now iterate through all rows and select them
		
		for(DownloadRow r : rows){
			r.setSelected(selectAll);
		}
	}
	
	/**
	 * Force the window to redraw all links when its shown again
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//call redraw process
		loadLinks();
	}

	/**
	 * Delete all URLS in the database in the 'offline' state
	 */
	private void removeOffline(){
		//run the query
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.delete("downloads", "status=?", new String[]{"offline"});
		
		//redraw the table
		loadLinks();
	}
	

	@Override
	public void onClick(View v) {
		
		if(v.equals(addLinksBtn)){
			//show the add links view
			startActivity(new Intent(this, LinkInputActivity.class));
		}else if(v.equals(checkLinksBtn)){
			checkUrls();
		}else if(v.equals(removeOfflineBtn)){
			removeOffline();
		}else if(v.equals(selectAllBtn)) {
			toggleSelectAll();
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
		pdialog.setMessage(String.format("Initialising Checker...", c.getCount()));
		
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
		
		/**
		 * Method executed when all links have been checked.
		 * 
		 * @param result <p>An array of strings that map directly to 
		 * 					each of the input URLs</p>
		 */
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
