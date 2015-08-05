package org.whut.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.icking.lib.ReaderJni;

import org.whut.gasmanagement.R;
import org.whut.utils.CardStrings;
import org.whut.utils.SPUtils;
import org.whut.utils.ToastHelper;

/**
 * Created by baisu on 15-5-28.
 */
public class WriteCardActivity extends Activity implements View.OnClickListener {

    private  String readText = null;
    private  String writeText = null;

    // 导航栏
    private ImageView backArr;
    private TextView midTitle;
    private RelativeLayout rightLayout;

    private RelativeLayout readType;
    private RelativeLayout writeType;
    private TextView writeTypeTV;
    private TextView readTypeTV;
    private TextView startBtn;

    private ReaderJni reader;
    private String cardPwd;
    int data_offset = 32;
    int data_len = 0;

    private String[] readArray;
    private String[] writeArray;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_card);
        initView();
        initCard();

        readText = getResources().getString(R.string.card_read);
        writeText = getResources().getString(R.string.card_write);
        readArray = getResources().getStringArray(R.array.read_card);
        writeArray = getResources().getStringArray(R.array.write_card);
    }

    private void initView() {
        readType = (RelativeLayout) findViewById(R.id.type_read);
        readType.setOnClickListener(this);

        writeType = (RelativeLayout) findViewById(R.id.type_write);
        writeType.setOnClickListener(this);

        writeTypeTV = (TextView) findViewById(R.id.write_value);
        readTypeTV = (TextView) findViewById(R.id.read_value);

        startBtn = (TextView) findViewById(R.id.tv_start);
        startBtn.setOnClickListener(this);

        // 导航栏
        backArr = (ImageView) findViewById(R.id.iv_topbar_left_back);
        backArr.setOnClickListener(this);
        midTitle = (TextView) findViewById(R.id.tv_topbar_middle_detail);
        midTitle.setText(getString(R.string.card_title));
        rightLayout = (RelativeLayout) findViewById(R.id.tv_topbar_right_map_layout);
        rightLayout.setOnClickListener(this);
    }

    /**
     * IC 卡初始化操作
     */
    private void initCard() {
        reader = new ReaderJni();

//        SharedPreferences sp = getSharedPreferences("card", Activity.MODE_PRIVATE);
        cardPwd = (String)SPUtils.get(WriteCardActivity.this, "card_pwd", "");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.type_read:
                chooseReadType();
                break;
            case R.id.type_write:
                chooseWriteType();
                break;
            case R.id.tv_start:
                startReadOrWrite();
                break;
            case R.id.iv_topbar_left_back:
                finish();
                break;
            case R.id.tv_topbar_right_map_layout:
                nextActivity(WriteCardOnlineActivity.class);
                break;
        }
    }

    /**
     * 处理具体读写卡
     */
    private void startReadOrWrite() {
        String btnName = startBtn.getText().toString();
        if (btnName.equals(readText)) {
            // 读卡
            String readName = readTypeTV.getText().toString();
            if (readName.equals("") || readName == null) {
                ToastHelper.showShort(WriteCardActivity.this, "请选择类型");
            } else {
                readCard(readName);
            }
        } else  {
            // 写卡
            String writeName = writeTypeTV.getText().toString();
            if (writeName.equals("") || writeName == null) {
                ToastHelper.showShort(WriteCardActivity.this, "请选择类型");
            } else {
                writeCard(writeName);
            }
        }
    }

    /**
     * 读卡操作
     *
     * 用户卡的读取范围：  32 - 79, 共48个字节
     * 维修2卡的读取范围： 32 - 79，共48个字节
     * 维修2卡分GRK-3型和非GRK-3型
     * @param cardType
     */
    private void readCard(String cardType) {
        if (reader.ic_init(this, 0, 0) == 0) {
            ToastHelper.showShort(WriteCardActivity.this, "连接成功");
            data_offset = 32;
            data_len = 48;
            byte[] data_buffer = new byte[256];
            for(int i = 0; i < data_buffer.length; i++) {
                data_buffer[i] = (byte) 0;
            }
            int rc;
            rc = reader.srd_4442(0, data_offset, data_len, data_buffer);

            if (0 == rc) {
                String result = get_recv_data_str(data_buffer, data_len);
                ToastHelper.showShort(WriteCardActivity.this, data_buffer.toString());

                if (cardType.equals(readArray[0]) && !("88".equals(result.substring(0, 2)))) {
                    ToastHelper.showShort(WriteCardActivity.this, "不是维修2卡");
                    return;
                } else if (cardType.equals(readArray[1]) && !("DD".equals(result.substring(0, 2)))) {
                    ToastHelper.showShort(WriteCardActivity.this, "不是用户卡");
                    return;
                }else {
                    Intent intent = new Intent(WriteCardActivity.this, CardInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cardData", result);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            } else {
                ToastHelper.showShort(WriteCardActivity.this, "读卡失败");
            }
        } else {
            ToastHelper.showShort(WriteCardActivity.this, "写卡器连接失败");
        }
    }

    /**
     * 写卡操作
     */
    private void writeCard(String cardType) {
        String cardData = null;
        if (cardType.equals(writeArray[0])) {
            ToastHelper.showShort(WriteCardActivity.this, writeArray[0]);
            // 安装卡
            cardData = CardStrings.WRITE_INSTALL_CARD;
        } else if (cardType.equals(writeArray[1])) {
            // 维修卡
            cardData = CardStrings.WRITE_REPAIR_CARD;
        } else {
            // 数据提取卡
            cardData = CardStrings.WRITE_DATA_CARD;
        }

        if (reader.ic_init(this, 0, 0) == 0) {
            chkPwd();
            data_offset = 32;
            data_len = cardData.length() / 2;
            byte[] data_buffer = new byte[256];

            AsciiToHex(cardData, data_buffer);
            int rc;
            rc = reader.swr_4442(0, data_offset, data_len, data_buffer);
            if (0 == rc) {
                ToastHelper.showShort(WriteCardActivity.this, "写卡成功");
            } else {
                ToastHelper.showShort(WriteCardActivity.this, "写卡失败");
            }
        }
        reader.ic_exit(0);
    }

    /**
     * 校验IC卡密码
     * 每次写卡之前都需要校验密码
     */
    private void chkPwd() {
        byte[] key_buffer = new byte[50];
        int rc;
        AsciiToHex(cardPwd, key_buffer);
        rc = reader.csc_4442(0, 3,key_buffer);
        if(rc == 0) {
            ToastHelper.showShort(WriteCardActivity.this, "密码正确");
        } else {
            ToastHelper.showShort(WriteCardActivity.this, "密码错误");
        }
    }

    /**
     * 读卡类型选择
     */
    public void chooseReadType() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteCardActivity.this);
        builder.setTitle("IC卡类型").setItems(readArray, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch(which){
                    case 0:
                        readTypeTV.setText(readArray[0]);
                        break;
                    case 1:
                        readTypeTV.setText(readArray[1]);
                        break;
                }
                writeTypeTV.setText("");
                startBtn.setText(getString(R.string.card_read));
            }
        }).show();
    }

    /**
     * 写卡类型选择
     */
    public void chooseWriteType() {

        AlertDialog.Builder builder = new AlertDialog.Builder(WriteCardActivity.this);
        builder.setTitle("IC卡类型").setItems(writeArray, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                switch(which){
                    case 0:
                        writeTypeTV.setText(writeArray[0]);
                        break;
                    case 1:
                        writeTypeTV.setText(writeArray[1]);
                        break;
                    case 2:
                        writeTypeTV.setText(writeArray[2]);
                        break;
                }
                readTypeTV.setText("");
                startBtn.setText(getString(R.string.card_write));
            }
        }).show();
    }

    /**
     *  把Ascii码转换成Hex0x31则为 1 @param hex @return
     *  注意转换后结果保存在HexData[]
     *  2字节 ACSII转换??字节 Hex
     *  注意：java??byte 范围??127~127，所以很多数据要用到int类型运算
     */
    public static int AsciiToHex(String Ascii, byte[] HexDataOut) {
        byte aLowbit ,aHighbit;
        byte hconval = 0,lconval = 0;
        byte [] stringArr = Ascii.getBytes();
        for(int i=0;i<(Ascii.length()/2);i++){
            aHighbit = stringArr[2*i];
            aLowbit = stringArr[2*i+1];
            if ((aHighbit >= 'a')&&(aHighbit<='f'))
                hconval=(byte)(aHighbit-'a'+10);
            if ((aHighbit>='A')&&(aHighbit<='F'))
                hconval=(byte)(aHighbit-'A'+10);
            if((aHighbit>='0')&&(aHighbit<='9')) hconval=(byte)(aHighbit-'0');
            if ((aLowbit>='a')&&(aLowbit<='f'))
                lconval=(byte)(aLowbit-'a'+10);
            if ((aLowbit>='A')&&(aLowbit<='F'))
                lconval=(byte)(aLowbit-'A'+10);
            if((aLowbit>='0')&&(aLowbit<='9')) lconval=(byte)(aLowbit-'0');
            HexDataOut[i]=(byte)(hconval*16+lconval);
        }
        return 0;
    }

    /**
     * 字节数组转String
     * @param bArray
     * @return
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    String get_recv_data_str(byte[] rcv_buff, int len) {
        int i;

        StringBuffer sb = new StringBuffer();
        for( i = 0; i < len; i++){
            if((0x00f0 & ((char)rcv_buff[i]))==0)sb.append(0);
            sb.append(Integer.toHexString(0x00ff & ((char)rcv_buff[i])).toUpperCase());
//            if((i+1)%16 == 0) sb.append("\r\n");
        }
        String s = sb.toString();
        return s;
    }

    private void nextActivity(Class<?> cls) {
        Intent intent = new Intent(WriteCardActivity.this, cls);
        startActivity(intent);
    }
}
