package org.whut.activities;

import org.whut.gasmanagement.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends Activity implements OnClickListener {

	// 导航栏
	private TextView topbarMid;
	private TextView topbarRight;
	private ImageView leftBack;

	private LinearLayout cardPasswdLay;
	private LinearLayout aboutLay;
	private LinearLayout webLay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}
	
	private void initView() {
		topbarMid = (TextView) findViewById(R.id.tv_topbar_middle_detail);
		topbarMid.setText(getString(R.string.setting));

		topbarRight = (TextView) findViewById(R.id.tv_topbar_right);
		topbarRight.setVisibility(View.GONE);

		leftBack = (ImageView) findViewById(R.id.iv_topbar_left_back);
		leftBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		cardPasswdLay = (LinearLayout) findViewById(R.id.card_passwd_settings);
		cardPasswdLay.setOnClickListener(this);
		aboutLay = (LinearLayout) findViewById(R.id.about);
		aboutLay.setOnClickListener(this);
		webLay = (LinearLayout) findViewById(R.id.webService);
		webLay.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.card_passwd_settings:
				nextActivity(ICCardPasswdActivity.class);
				break;
			case R.id.about:
				nextActivity(AboutActivity.class);
				break;
			case R.id.webService:
				nextActivity(WebServicePropertyActivity.class);
				break;
		}
	}
	
	public void nextActivity(Class<?> cls) {
		Intent intent = new Intent(SettingActivity.this, cls);
		startActivity(intent);
	}
}
