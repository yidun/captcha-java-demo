package com.netease.is.nc.demo.web;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
public class ForwardApiServlet extends ForwardServlet {
    /**
     * 易盾验证码v2只有jsonp请求，不再使用post请求
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String qs = request.getQueryString();

        String forwardURL = "http://" + getHost() + "/" + uri + "?" + qs;
        log.debug("forwardURL={}", forwardURL);

        super.forwardGet(forwardURL, copyRequestHeaders(request), response);
    }

    @Override
    protected String getHost() {
        return YIDUN_API_HOST;
    }
}
