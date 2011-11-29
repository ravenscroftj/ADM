package com.funkymonkeysoftware.adm;

import java.net.MalformedURLException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;

import com.funkymonkeysoftware.adm.config.ADMPreferencesActivity;
import com.funkymonkeysoftware.adm.download.ADMDownload;
import com.funkymonkeysoftware.adm.download.DownloadModel;
import com.funkymonkeysoftware.adm.download.DownloadRow;

/**
 * The main entrypoint window for the ADM downloader
 * 
 * This class provides functionality for the main entrypoint window within the program.
 * All other activites are started from this class.
 * 
 * @author James Ravenscroft
 *
 */
public class ADMActivity extends Activity implements OnClickListener{
	
	protected DownloadModel model;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //load the downloads model and get all the active downloads
        model = new DownloadModel(this);
        try {
			model.loadDownloads();
		} catch (MalformedURLException e) {
			//TODO: provide an error message and fix the downloads db
		}
        
        //draw the downloads table
        drawDownloads();
    }
    
    protected void drawDownloads() {
    	
    	TableLayout dlTable = 
    			(TableLayout)findViewById(R.id.activeDownloadsTable);
    	
    	//clear the downloads table
    	dlTable.removeAllViews();
    	
    	//get a list of downloads from the model
    	for(ADMDownload dl : model.getDownloads()){
    		//show a table row for each download
    		DownloadRow dr = new DownloadRow(this, dl);
    		dlTable.addView(dr);
    	}
    }
    
    /**
     * Inflate the options menu for the main adm downloader
     */
   @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_options_menu, menu);
	    return true;

	}
    
   @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	   switch(item.getItemId()) {
	   		
		   case R.id.addLinksAction:
				//show the link checker
				startActivity(new Intent(this, LinkCheckerActivity.class));
				break;
				
		   case R.id.managePreferences:
			   startActivity(new Intent(this, ADMPreferencesActivity.class));
			   break;
			
	   }

	   return true;
	}

    /**
     * Listener for clicks on various buttons 
     */
	public void onClick(View v) {

	}
	
	
}