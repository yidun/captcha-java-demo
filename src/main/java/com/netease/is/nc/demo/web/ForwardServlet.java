package com.netease.is.nc.demo.web;

import com.netease.is.nc.sdk.utils.HttpClient4Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 转发
 * /api/v2/getconf
 * /api/v2/get
 * /api/v2/check
 * /api/v2/collect
 *
 * 请求到易盾服务器
 *
 * Created by captcha_dev on 16-10-9.
 */
@Slf4j
public abstract class ForwardServlet extends HttpServlet {
    protected static final String YIDUN_API_HOST = "c.dun.163yun.com";
    protected static final String YIDUN_STATIC_HOST = "cstaticdun.126.net";
    protected CloseableHttpClient httpClient = HttpClient4Utils.createHttpClient(10, 10, 5000, 5000, 3000);

    /**
     * 转发get请求到易盾服务器
     *
     * @param forwardURL
     * @param headers
     * @param response
     */
    protected void forwardGet(String forwardURL, List<Header> headers, HttpServletResponse response) {
        HttpGet get = new HttpGet(forwardURL);
        if (headers != null && headers.size() > 0) {
            get.setHeaders(headers.toArray(new Header[0]));
        }
        CloseableHttpResponse resp = null;
        try {
            resp = httpClient.execute(get);
            // 复制请求响应头部参数
            for (Header header : resp.getAllHeaders()) {
                response.addHeader(header.getName(), header.getValue());
                log.debug("{}: {}", header.getName(), header.getValue());
            }
            response.setStatus(resp.getStatusLine().getStatusCode());
            if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_MODIFIED) {
                response.setIntHeader(HttpHeaders.CONTENT_LENGTH, 0);
            } else {
                log.debug("copying stream...");
                try (InputStream ins = resp.getEntity().getContent(); OutputStream outs = response.getOutputStream()) {
                    IOUtils.copy(ins, outs);
                    outs.flush();
                }
                log.debug("copying stream finished.");
            }
        } catch (IOException e) {
            log.error("error", e);
        } finally {
            IOUtils.closeQuietly(resp);
        }
    }

    /**
     * 复制所有请求头参数
     *
     * @param request
     * @return
     */
    protected List<Header> copyRequestHeaders(HttpServletRequest request) {
        Enumeration<String> enumHeaderNames = request.getHeaderNames();
        List<Header> headers = new ArrayList<>();
        while (enumHeaderNames.hasMoreElements()) {
            String headerName = enumHeaderNames.nextElement();
            String headerValue = request.getHeader(headerName);

            if (StringUtils.equalsIgnoreCase(headerName, HttpHeaders.HOST)) {
                headerValue = getHost(); // 覆盖原有的Host参数
            }
            log.debug("{}: {}", headerName, headerValue);
            headers.add(new BasicHeader(headerName, headerValue));
        }
        return headers;
    }

    protected abstract String getHost();
}
