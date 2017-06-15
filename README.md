# captcha-java-demo
易盾验证码java演示

# demo运行步骤
* 修改index.html
```
initNECaptcha({
  captchaId: 'YOUR_CAPTCHA_ID', // <-- 这里填入在易盾官网申请的验证码id
  element: '#captcha_div',
  mode: 'float',
  width: '320px',
  onVerify: function(err, ret){
    if(!err){
        // ret['validate'] 获取二次校验数据
    }
  }
}, function (instance) {
  // 初始化成功后得到验证实例instance，可以调用实例的方法
}, function (err) {
  // 初始化失败后触发该函数，err对象描述当前错误信息
})
```

* 修改 LoginServlet.java
```
    private static final String captchaId = "YOUR_CAPTCHA_ID"; // 验证码id
    private static final String secretId = "YOUR_SECRET_ID"; // 密钥对id
    private static final String secretKey = "YOUR_SECRET_KEY"; // 密钥对key
```

* `mvn tomcat7:run`
* 浏览器访问 http://localhost:8181/ 查看演示