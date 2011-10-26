 package com.funkymonkeysoftware.adm;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LinkInputActivity extends Activity implements OnClickListener{
	
	private Button addBtn;
	private DownloadsDBOpenHelper dbhelper;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set up the link input window
		setContentView(R.layout.link_input);
		
		//set up database helper
		dbhelper = new DownloadsDBOpenHelper(this);

		//get reference to the button and set up a listener
		addBtn = (Button)findViewById(R.id.addLinksToCheckerBtn);
		addBtn.setOnClickListener(this);
	}

	/**
	 * Click listener for the buttons attached to this view
	 */
	@Override
	public void onClick(View v) {
		
		if(v.equals(addBtn)){
			parseAndStore();
		}
		
	}
	
	/**
	 * This method parses all the URLs that are pasted
	 */
	private void parseAndStore(){
		
		//get the text field
		EditText inputField = (EditText)findViewById(R.id.pasteLinksTxt);
		//find the input
		String input = inputField.getText().toString();
		
		boolean invalidDetected = false;
		
		//break the input up into lines
		for(String line : input.split("\n")){
			
			try {
				URL theUrl = new URL(line);
				addDownload(theUrl);
				
			} catch (MalformedURLException e) {
				invalidDetected = true;
			}
			
		}
		
		//if there was an invalid url, tell the user
		if(invalidDetected){
			AlertDialog a = new AlertDialog.Builder(this).create();
			
			a.setMessage("One or more invalid URLS were detected. " +
					"These were not added to the link checker.");
		}
		//close the database
		dbhelper.getWritableDatabase().close();
		//close the view
		finish();
	}
	
	/**
	 * Add a new download to the database to be checked
	 * 
	 * @param u <p>The new URL to be added to the database</p>
	 */
	private void addDownload(URL u){
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		String[] selection = {u.toString()};
		Cursor c = db.rawQuery("SELECT * FROM downloads WHERE url=?", 
														selection);
		
		if(c.moveToFirst()) return;
		
		//otherwise carry out the insert
		ContentValues values = new ContentValues();
		values.put("url", u.toString());
		values.put("status", "unchecked");
		db.insert("downloads", null, values);
	}
	
	
	
}
