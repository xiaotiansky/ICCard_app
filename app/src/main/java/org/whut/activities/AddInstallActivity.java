package org.whut.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import org.whut.gasmanagement.R;

public class AddInstallActivity extends Activity implements OnClickListener {

	private TextView addrText;
	private ImageView getAddrBtn;
	private Button addBtn;

	// 导航栏
	private TextView topbarMid;
	private ImageView leftBack;
	private RelativeLayout rightLay;

	public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_install);
		initLocation();
		initView();
	}
	
	private void initView() {
		addrText = (TextView) findViewById(R.id.addr_text);
		getAddrBtn = (ImageView) findViewById(R.id.get_addr_btn);
		getAddrBtn.setOnClickListener(this);
		addBtn = (Button) findViewById(R.id.add_install_btn);
		addBtn.setOnClickListener(this);

		topbarMid = (TextView) findViewById(R.id.tv_topbar_middle_detail);
		topbarMid.setText(getString(R.string.add_install));

		rightLay = (RelativeLayout) findViewById(R.id.tv_topbar_right_map_layout);
		rightLay.setVisibility(View.GONE);

		leftBack = (ImageView) findViewById(R.id.iv_topbar_left_back);
		leftBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.get_addr_btn:
			initLocOption();
			mLocationClient.start();
			break;
		case R.id.add_install_btn:
			break;
		}
	}
	
	private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }
	
	private void initLocOption() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09"); // 使用百度经纬度坐标系
        int span = 1000;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }
	
	public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            String naddr = location.getAddrStr();
            addrText.setText(naddr);
            mLocationClient.stop();
        }
    }
	
	@Override
    protected void onStop() {
        mLocationClient.stop();
        super.onStop();
    }
}
