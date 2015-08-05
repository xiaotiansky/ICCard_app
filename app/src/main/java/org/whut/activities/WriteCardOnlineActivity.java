package org.whut.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.icking.lib.ReaderJni;

import org.ksoap2.serialization.SoapObject;
import org.whut.gasmanagement.R;
import org.whut.utils.CardHelper;
import org.whut.utils.SPUtils;
import org.whut.utils.ToastHelper;
import org.whut.utils.WebServiceUtils;

import java.util.HashMap;

/**
 * Created by baisu on 15-6-30.
 */
public class WriteCardOnlineActivity extends Activity implements View.OnClickListener {

    // 导航栏
    private ImageView backArr;
    private TextView midTitle;

    private EditText idTv;
    private EditText infoTv;
    private Button submitBtn;

    private ReaderJni reader;
    private String data = null;
    private String cardPwd;

    public static final int GET_DATA_FAIL = 0;
    public static final int GET_DATA_SUCCESS = 1;
    public static final int WRITE_CARD_FAIL = 2;
    public static final int WRITE_CARD_SUCCESS = 3;

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch(msg.what){
                case GET_DATA_FAIL:
                    ToastHelper.showShort(WriteCardOnlineActivity.this, "获取数据失败");
                    break;
                case GET_DATA_SUCCESS:
                    ToastHelper.showShort(WriteCardOnlineActivity.this, "获取数据成功");
                    writeToCard();
                    break;
                case WRITE_CARD_FAIL:
                    ToastHelper.showShort(WriteCardOnlineActivity.this, "写卡失败");
                    break;
                case WRITE_CARD_SUCCESS:
                    ToastHelper.showShort(WriteCardOnlineActivity.this, "写卡成功");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_card_online);
        initView();
        initCard();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_topbar_left_back:
                finish();
                break;
            case R.id.btn_submit:
                writeOnLine();
                break;

        }
    }

    private void initView() {

        //导航栏
        backArr = (ImageView) findViewById(R.id.iv_topbar_left_back);
        backArr.setOnClickListener(this);
        midTitle = (TextView) findViewById(R.id.tv_topbar_middle_detail);
        midTitle.setText(getString(R.string.online_write));

        idTv = (EditText) findViewById(R.id.tv_meter_id);
        idTv.setText("0110140805206");
        infoTv = (EditText) findViewById(R.id.tv_meter_info);
        submitBtn = (Button) findViewById(R.id.btn_submit);
        submitBtn.setOnClickListener(this);
    }

    /**
     * IC 卡初始化操作
     */
    private void initCard() {
        reader = new ReaderJni();

        SharedPreferences sp = getSharedPreferences("card", Activity.MODE_PRIVATE);
        cardPwd = sp.getString("card_pwd", "");
    }

    /**
     * 写入到IC卡中
     */
    private void writeOnLine() {
        String meterId = idTv.getText().toString().trim();
        String info = infoTv.getText().toString().trim();
        getData(meterId, info);
    }

    private void writeToCard() {
        if (data.charAt(0) == 'S') {
            CardHelper helper = new CardHelper(WriteCardOnlineActivity.this, reader);
            ToastHelper.showShort(WriteCardOnlineActivity.this, data.substring(1));
            ToastHelper.showShort(WriteCardOnlineActivity.this, data.substring(1).length() + "");
            helper.writeCard(32, data.substring(1), cardPwd);
        } else if (data.charAt(0) == 'F') {
            ToastHelper.showShort(WriteCardOnlineActivity.this, data.substring(1));
        }
    }

    /**
     * 通过webService 获取数据
     * @return
     */
    private void getData(String meterId, String info) {
        String ip = (String)SPUtils.get(WriteCardOnlineActivity.this, "ip", "");
        String port = (String)SPUtils.get(WriteCardOnlineActivity.this, "port", "");
        final String namespace = ip;
        final String url = ip + ":" + port + "/SmCard.asmx";
        final String method = "GetCardString";

        final HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("meterID", meterId);
        properties.put("info", info);

        WebServiceUtils.callWebService(namespace, url, method, properties, new MyWebServiceCallBack());

    }

    class MyWebServiceCallBack implements WebServiceUtils.WebServiceCallBack {

        @Override
        public void onSuccess(SoapObject result) {
            data = result.getProperty(0).toString();
            Log.i("TAG", data);
            handler.sendEmptyMessage(GET_DATA_SUCCESS);
        }

        @Override
        public void onFail(Exception e) {
            Log.i("TAG", e.toString());
            Log.i("TAG", "获取数据失败");
            handler.sendEmptyMessage(GET_DATA_FAIL);
        }
    }
}
