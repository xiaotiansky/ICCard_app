package org.whut.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.whut.gasmanagement.R;

public class AboutActivity extends Activity {

	// 导航栏
	private TextView topbarMid;
	private TextView topbarRight;
	private ImageView leftBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		initView();
	}
	
	private void initView() {
		topbarMid = (TextView) findViewById(R.id.tv_topbar_middle_detail);
		topbarMid.setText(getString(R.string.about));

		topbarRight = (TextView) findViewById(R.id.tv_topbar_right);
		topbarRight.setVisibility(View.GONE);

		leftBack = (ImageView) findViewById(R.id.iv_topbar_left_back);
		leftBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}
}
