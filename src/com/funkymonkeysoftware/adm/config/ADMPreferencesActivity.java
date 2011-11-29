package com.funkymonkeysoftware.adm.config;

import com.funkymonkeysoftware.adm.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Class for user management of ADM settings and preferences
 * 
 * @author James Ravenscroft
 *
 */
public class ADMPreferencesActivity extends PreferenceActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//load the preferences
		addPreferencesFromResource(R.xml.userprefs);
		
	}
	
}
