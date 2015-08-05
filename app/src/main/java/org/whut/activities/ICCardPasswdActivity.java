package org.whut.activities;

import org.whut.gasmanagement.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ICCardPasswdActivity extends Activity {

	private Button chPwdBtn;
	private TextView oldPwd;
	private TextView newPwd;
	private TextView newPwd2;

	// 导航栏
	private TextView topbarMid;
	private ImageView leftBack;
	private RelativeLayout rightLay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_iccard_passwd);
		initView();
	}
	
	private void initView() {
		oldPwd = (TextView) findViewById(R.id.old_passwd);
		newPwd = (TextView) findViewById(R.id.new_passwd);
		newPwd2 = (TextView) findViewById(R.id.new_passwd2);
		chPwdBtn = (Button) findViewById(R.id.ch_pwd_btn);
		chPwdBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				boolean flag = true;
				String oldPwdStr = oldPwd.getText().toString();
				if (!chkOldPwd(oldPwdStr)) {
					flag = false;
					Toast.makeText(ICCardPasswdActivity.this, 
							"初始密码错误", Toast.LENGTH_SHORT).show();
				}
				if (!chkPwd()) {
					flag = false;
					Toast.makeText(ICCardPasswdActivity.this, 
							"两次密码输入不一致", Toast.LENGTH_SHORT).show();
				}
				if (flag) {
					chPwd(newPwd.getText().toString());
					Toast.makeText(ICCardPasswdActivity.this, 
							"修改成功", Toast.LENGTH_SHORT).show();
				}
			}
		});

		topbarMid = (TextView) findViewById(R.id.tv_topbar_middle_detail);
		topbarMid.setText(getString(R.string.card_passwd_settings));

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
	
	/**
	 * 检查两次输入的密码是否一致
	 * 一致返回 true
	 */
	private boolean chkPwd() {
		String newPwdStr = newPwd.getText().toString();
		String newPwd2Str = newPwd2.getText().toString();
		return newPwdStr.equals(newPwd2Str);
	}
	
	/**
	 * 检查初始IC密码
	 * 正确返回 true
	 */
	private boolean chkOldPwd(String oldPwdStr) {
		SharedPreferences sp = getSharedPreferences("card", Activity.MODE_PRIVATE);
		String pwd = sp.getString("card_pwd", "");
		return oldPwdStr.equals(pwd);
	}
	
	/**
	 * 修改IC卡默认密码
	 */
	private void chPwd(String passwd) {
		SharedPreferences sp = getSharedPreferences("card", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("card_pwd", passwd);
		editor.commit();
	}
}
