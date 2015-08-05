package org.whut.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpTool {

    public String post(String path, String params) {

        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5 * 1000);

            conn.setDoOutput(true); //　设置可以写入数据
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());
            out.print(params);
            out.flush();

            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()
            ));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return response.toString();
    }

    public String get(String path, String params) {

    	BufferedReader in = null;
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(path + "?" + params);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);

            conn.connect();

            in = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()
            ));
            String line;
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }
}