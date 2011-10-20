package com.funkymonkeysoftware.adm;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LinkCheckerActivity extends Activity implements OnClickListener{
	
	private Button addLinksBtn;
	private DownloadsDBOpenHelper dbhelper;
	
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
	}
	
	private void loadLinks(){
		
		//get the table to append things to
		TableLayout table = (TableLayout)findViewById(R.id.checkLinksTable);
	
		//get all downloads that are unchecked or online or offline
		SQLiteDatabase db = dbhelper.getWritableDatabase();
		
		Cursor c = db.rawQuery("SELECT * FROM downloads WHERE " +
				"status='unchecked' OR status='online' OR " +
				"status='offline'", null);
		
		if(!c.moveToFirst()) {
			//provide some kind of error message
			TextView error = new TextView(this);
			error.setText("No URLS, consider adding some!");
			table.addView(error);
		}else{
			
			for(c.moveToFirst(); c.moveToNext() ;){
				
				TableRow tr = new TableRow(this);
				
				TextView url = new TextView(this);
				url.setText(c.getString(1));
				TextView status = new TextView(this);
				status.setText(c.getString(2));
				
				tr.addView(url);
				tr.addView(status);
				
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
