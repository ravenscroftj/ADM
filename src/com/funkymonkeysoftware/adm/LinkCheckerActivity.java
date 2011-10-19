package com.funkymonkeysoftware.adm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LinkCheckerActivity extends Activity implements OnClickListener{
	
	private Button addLinksBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//set up the link checker
		setContentView(R.layout.linkchecker);
		
		//set up as listener for buttons
		addLinksBtn = (Button)findViewById(R.id.addLinksBtn);
		
		addLinksBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.equals(addLinksBtn)){
			//show the add links view
			startActivity(new Intent(this, LinkInputActivity.class));
		}
	}
	
}
