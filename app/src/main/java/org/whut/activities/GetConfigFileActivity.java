package org.whut.activities;

import org.whut.gasmanagement.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GetConfigFileActivity extends Activity{

	// 导航栏
	private ImageView leftBack;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_config_file);
		
		initView();
	}
	
	private void initView() {
		leftBack = (ImageView) findViewById(R.id.iv_topbar_left_back);
		leftBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
