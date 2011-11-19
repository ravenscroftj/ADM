package com.funkymonkeysoftware.adm;

import java.net.MalformedURLException;

import com.funkymonkeysoftware.adm.download.DownloadModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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
        
        Button addNewLinksBtn = (Button)findViewById(R.id.addNewLinksBtn);
        addNewLinksBtn.setOnClickListener(this);
        
        //load the downloads model and get all the active downloads
        model = new DownloadModel(this);
        try {
			model.loadDownloads();
		} catch (MalformedURLException e) {
			//TODO: provide an error message and fix the downloads db
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
	   }

	   return true;
	}

    /**
     * Listener for clicks on various buttons 
     */
	public void onClick(View v) {
		
		switch(v.getId()){
		
		case R.id.addNewLinksBtn:
			//show the link checker
			startActivity(new Intent(this, LinkCheckerActivity.class));
			break;
		
		}
	}
}