package org.whut.utils;

import android.app.Activity;
import com.icking.lib.ReaderJni;

/**
 * Created by baisu on 15-6-29.
 */
public class CardHelper {

    private ReaderJni reader;
    private Activity mActivity;

    public CardHelper(Activity mActivity, ReaderJni reader) {
        this.mActivity = mActivity;
        this.reader = reader;
    }

    /**
     * 校验IC卡密码
     * 每次写卡之前都需要校验密码
     */
    public void chkPwd(String cardPwd) {
        byte[] key_buffer = new byte[50];
        int rc;
        CastHelper.AsciiToHex(cardPwd, key_buffer);
        rc = reader.csc_4442(0, 3,key_buffer);
        if(rc == 0) {
            ToastHelper.showShort(mActivity, "密码正确");
        } else {
            ToastHelper.showShort(mActivity, "密码错误");
        }
    }

    /**
     * 将给定字符串写入到IC卡中指定位置
     * @param offset
     * @param cardData
     */
    public void writeCard(int offset, String cardData, String cardPwd) {
        if (reader.ic_init(mActivity, 0, 0) == 0) {
            chkPwd(cardPwd);

            byte[] data_buffer = new byte[256];

            CastHelper.AsciiToHex(cardData, data_buffer);
            int rc;
            rc = reader.swr_4442(0, offset, cardData.length()/2, data_buffer);
            if (0 == rc) {
                ToastHelper.showShort(mActivity, "写卡成功");
            } else {
                ToastHelper.showShort(mActivity, "写卡失败");
            }
        }
        reader.ic_exit(0);
    }

    public String readCard(int offset, int len) {
        String result = null;
        if (reader.ic_init(mActivity, 0, 0) == 0) {
            ToastHelper.showShort(mActivity, "连接成功");

            byte[] data_buffer = new byte[256];
            for(int i = 0; i < data_buffer.length; i++) {
                data_buffer[i] = (byte) 0;
            }
            int rc;
            rc = reader.srd_4442(0, offset, len, data_buffer);

            if (0 == rc) {
                result = CastHelper.get_recv_data_str(data_buffer, len);
            } else {
                ToastHelper.showShort(mActivity, "读卡失败");
            }
        } else {
            ToastHelper.showShort(mActivity, "写卡器连接失败");
        }
        return result;
    }
}
