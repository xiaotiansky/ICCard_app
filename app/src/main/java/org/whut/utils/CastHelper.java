package org.whut.utils;

/**
 * Created by baisu on 15-6-29.
 */
public class CastHelper {

    /**
     *  把Ascii码转换成Hex0x31则为 1 @param hex @return
     *  注意转换后结果保存在HexData[]
     *  2字节 ACSII转换1字节 Hex
     *  注意：java??byte 范围??127~127，所以很多数据要用到int类型运算
     */
    public static int AsciiToHex(String Ascii, byte[] HexDataOut) {
        byte aLowbit ,aHighbit;
        byte hconval = 0,lconval = 0;
        byte [] stringArr = Ascii.getBytes();
        for(int i=0;i<(Ascii.length()/2);i++) {
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

    /**
     * 将十六进制字节数组转化为字符串
     * @param rcv_buff
     * @param len
     * @return
     */
    public static String get_recv_data_str(byte[] rcv_buff, int len) {
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
}
