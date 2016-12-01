# captcha-java-demo
易盾验证码java演示

# demo运行步骤
* 修改index.html
```
var opts = {
    "element": "captcha_div",
    "captchaId": "YOUR_CAPTCHA_ID", // 验证码id
    "width": 320
  }
```

* 修改 LoginServlet.java
```
    private static final String captchaId = "YOUR_CAPTCHA_ID"; // 验证码id
    private static final String secretId = "YOUR_SECRET_ID"; // 密钥对id
    private static final String secretKey = "YOUR_SECRET_KEY"; // 密钥对key
```

* `mvn tomcat7:run`
* 浏览器访问 http://localhost:8181/ 查看演示