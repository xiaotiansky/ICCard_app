package org.whut.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.whut.gasmanagement.R;
import org.whut.utils.SPUtils;
import org.whut.utils.ToastHelper;

import java.util.regex.Pattern;

/**
 * Created by baisu on 15-6-30.
 */
public class WebServicePropertyActivity extends Activity implements View.OnClickListener {

    private EditText ipTv;
    private EditText portTv;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webservice_property);
        initView();
    }

    private void initView() {
        ipTv = (EditText) findViewById(R.id.tv_web_ip);
        portTv = (EditText) findViewById(R.id.tv_web_port);
        submitBtn = (Button) findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                saveWebData();
                break;
        }
    }

    private void saveWebData() {
        String ip = ipTv.getText().toString().trim();
        if (ip.length() < 1) {
            ToastHelper.showShort(WebServicePropertyActivity.this, "ip地址不能为空");
            return;
        }
        String port = portTv.getText().toString().trim();
        if (port.length() < 1) {
            ToastHelper.showShort(WebServicePropertyActivity.this, "端口号不能为空");
            return;
        }
        Pattern portReg = Pattern.compile("[0-9]{1,5}");
        if (!portReg.matcher(port).matches()) {
            ToastHelper.showShort(WebServicePropertyActivity.this, "端口号为0-65535之间的数字");
            return;
        }

        SPUtils.put(WebServicePropertyActivity.this, "ip", ip);
        SPUtils.put(WebServicePropertyActivity.this, "port", port);
        ToastHelper.showShort(WebServicePropertyActivity.this, "修改成功");
        finish();
    }
}
