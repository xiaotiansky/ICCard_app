package org.whut.utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by baisu on 15-6-30.
 */
public class WebServiceUtils {

    public static void callWebService(final String namespace, final String url, final String method,
                                      final HashMap<String, String> properties, final WebServiceCallBack webServiceCallBack) {

        new Thread() {
            @Override
            public void run() {
                super.run();
                SoapObject request = new SoapObject(namespace, method);
                if (properties != null) {
                    for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
                            .iterator(); it.hasNext();) {
                        Map.Entry<String, String> entry = it.next();
                        request.addProperty(entry.getKey(), entry.getValue());
                        Log.i("TAG", entry.getKey());
                        Log.i("TAG", entry.getValue());
                    }
                }
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.setOutputSoapObject(request);
                envelope.dotNet = true;
                envelope.encodingStyle = "UTF-8";
                HttpTransportSE androidHttpTransport = new HttpTransportSE(url);

                try {
                    androidHttpTransport.debug = true;
                    androidHttpTransport.call(namespace + method, envelope);
                    Object resultObj = envelope.bodyIn;
                    Log.i("TAG", resultObj.toString());
                    SoapObject result = (SoapObject)envelope.bodyIn;
                    System.out.println(result.toString());
                    webServiceCallBack.onSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    webServiceCallBack.onFail(e);
                }
            }
        }.start();
    }

    /**
     *  回调接口
     */
    public interface WebServiceCallBack {
        void onSuccess(SoapObject result);

        void onFail(Exception e);
    }

}
