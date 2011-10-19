package com.funkymonkeysoftware.adm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	private Button addNewLinksBtn;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        addNewLinksBtn = (Button)findViewById(R.id.addNewLinksBtn);
        addNewLinksBtn.setOnClickListener(this);
        
    }

    /**
     * Listener for clicks on various buttons
     */
	@Override
	public void onClick(View v) {
		
		
		if(v.equals(addNewLinksBtn)){
			//show the link checker
			startActivity(new Intent(this, LinkCheckerActivity.class));
		}
		
		
	}
}