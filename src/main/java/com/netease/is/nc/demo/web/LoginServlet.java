package com.netease.is.nc.demo.web;

import com.netease.is.nc.sdk.NECaptchaVerifier;
import com.netease.is.nc.sdk.NESecretPair;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by captcha_dev on 16-10-9.
 */
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = -3185301474503659058L;
    private static final String captchaId = "YOUR_CAPTCHA_ID"; // 验证码id
    private static final String secretId = "YOUR_SECRET_ID"; // 密钥对id
    private static final String secretKey = "YOUR_SECRET_KEY"; // 密钥对key

    private final NECaptchaVerifier verifier = new NECaptchaVerifier(captchaId, new NESecretPair(secretId, secretKey));

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String validate = request.getParameter(NECaptchaVerifier.REQ_VALIDATE); // 从请求体里获得验证码validate数据
        String user = "{'id':'123456'}";

        boolean isValid = verifier.verify(validate, user); // 发起二次校验

        System.out.println("validate = " + validate + ", isValid = " + isValid);
        if (isValid) {
            response.sendRedirect("/success.jsp");
        } else {
            response.sendRedirect("/fail.jsp");
        }
    }
}
