/*
 * @(#) HttpConnection.java 2018-08-30
 *
 * Copyright 2018 NetEase.com, Inc. All rights reserved.
 */

package com.netease.is.nc.sdk.utils;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * @author hzzhuxiafeng
 * @version 2018-08-30
 */
public class HttpConnectionUtils {

    private static final String QP_SEP_A = "&";
    private static final String QP_SEP_S = ";";
    private static final String NAME_VALUE_SEPARATOR = "=";

    /**
     * 发送GET请求，获取服务器返回结果
     *
     * @param url
     * @return 服务器返回结果
     * @throws IOException
     */
    public static String readContentFromGet(String url) throws IOException {

        URL getUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        // 设置从主机读取数据超时（单位：毫秒）
        connection.setReadTimeout(2000);
        // 设置连接主机超时（单位：毫秒）
        connection.setConnectTimeout(2000);
        connection.connect();
        String responseString = "";
        if (connection.getResponseCode() == 200) {
            // 发送数据到服务器并使用Reader读取返回的数据
            StringBuffer sBuffer = new StringBuffer();
            byte[] buf = new byte[1024];
            InputStream inStream = connection.getInputStream();
            for (int n; (n = inStream.read(buf)) != -1; ) {
                sBuffer.append(new String(buf, 0, n, "UTF-8"));
            }
            inStream.close();
            responseString = sBuffer.toString();
        } else {
            System.out.println(String.format("connection response code is not correct,code=%s,msg=%s ",
                    connection.getResponseCode(), connection.getResponseMessage()));
        }
        connection.disconnect();// 断开连接
        return responseString;
    }

    /**
     * 发送POST请求，获取服务器返回结果
     *
     * @param url      请求的url
     * @param paramMap 测试参数
     * @return 服务器返回结果
     * @throws IOException
     */
    public static String readContentFromPost(String url, Map<String, String> paramMap) throws IOException {
        String data = getUrlParamsByMap(paramMap);
        URL postUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
        // 设置从主机读取数据超时（单位：毫秒）
        connection.setReadTimeout(2000);
        // 设置连接主机超时（单位：毫秒）
        connection.setConnectTimeout(2000);
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        // 建立与服务器的连接，并未发送数据
        connection.connect();
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        outputStreamWriter.write(data);
        outputStreamWriter.flush();
        outputStreamWriter.close();
        String response = "";
        if (connection.getResponseCode() == 200) {
            // 发送数据到服务器并使用Reader读取返回的数据
            StringBuffer sBuffer = new StringBuffer();
            byte[] buf = new byte[1024];
            InputStream inStream = connection.getInputStream();
            for (int n; (n = inStream.read(buf)) != -1; ) {
                sBuffer.append(new String(buf, 0, n, "UTF-8"));
            }
            // 断开连接
            inStream.close();
            response = sBuffer.toString();
        } else {
            System.out.println(String.format("connection response code is not correct,code=%s,msg=%s ",
                    connection.getResponseCode(), connection.getResponseMessage()));
        }
        connection.disconnect();
        return response;
    }

    private static String getUrlParamsByMap(Map<String, String> map) throws UnsupportedEncodingException {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = URLEncoder.encode(entry.getKey(), "Utf-8");
            String value = URLEncoder.encode(entry.getValue(), "Utf-8");
            sb.append(key + NAME_VALUE_SEPARATOR + value);
            sb.append(QP_SEP_A);
        }
        String s = sb.toString();
        if (s.endsWith(QP_SEP_A)) {
            s = StringUtils.substringBeforeLast(s, QP_SEP_A);
        }
        return s;
    }
}
