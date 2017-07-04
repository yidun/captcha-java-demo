/*
 * @(#) HttpClientUtils.java Feb 20, 2013
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.netease.is.nc.sdk.utils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.util.Map;

/**
 * 规范httpclient的实例化，统一设置超时等关键参数
 *
 * @author vivi
 * @version Feb 20, 2013
 */
public class HttpClientUtils {
    private static final int CONNECTION_TIMEOUT_MILLS = 5000;// 与服务器建立连接的超时参数
    private static final int SOCKET_TIMEOUT_MILLS = 5000;// 与服务器传输数据时的超时参数，即如果一个调用x毫秒没有返回，则认为是调用超时了

    private static HttpClient httpClient = makeHttpClient();

    /**
     * 工厂函数，每次调用返回一个新建的HttpClient对象
     * 这里只做常用的参数设置，如果需要特殊的调整，可以调用
     * httpClient.getHttpConnectionManager().getParams().setxxx()进行调整
     *
     * @return
     */
    public static HttpClient makeHttpClient() {
        MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
        httpConnectionManager.getParams().setConnectionTimeout(CONNECTION_TIMEOUT_MILLS);
        httpConnectionManager.getParams().setSoTimeout(SOCKET_TIMEOUT_MILLS);
        HttpClient httpClient = new HttpClient(httpConnectionManager);

        return httpClient;
    }

    /**
     * 发送http post请求，将字符串结果返回，如果接口返回 status!=200 那么抛出异常
     *
     * @param url    接口地址
     * @param params 接口参数
     * @return 接口返回的字符串
     */
    public static String sendPost(String url, Map<String, String> params) {
        PostMethod post = new PostMethod(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            post.addParameter(new NameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            int status = httpClient.executeMethod(post);
            String response = post.getResponseBodyAsString(4096); // 接口最多不会返回超过4096长度
            if (status != HttpStatus.SC_OK) {
                System.out.println("error status code:" + status + ", response:" + response);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            post.releaseConnection();
        }
        return "";
    }
}
