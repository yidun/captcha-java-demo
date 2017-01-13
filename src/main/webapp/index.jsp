<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>易盾验证码-DEMO</title>
    <!-- 演示用js/css，非组件依赖 -->
    <script src="//cdn.bootcss.com/jquery/3.1.1/jquery.min.js"></script>
    <link href='//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css' rel='stylesheet'>
</head>
<body>
<form style="max-width: 320px; margin: 120px auto;" action="/login" method="post">
    <h2 class="form-signin-heading">易盾验证码</h2>
    <input type="text" class="form-control" name="username" placeholder="账号" />
    <input type="password" class="form-control" name="password" placeholder="密码" />
    <div style="margin: 10px auto;" id="captcha_div"></div> <!-- 验证码容器元素定义 -->
    <button class="btn btn-lg btn-primary btn-block" type="submit" disabled="disabled" id="submit-btn">登录</button>
</form>
<script src="//c.dun.163yun.com/js/c.js"></script><!-- 验证码组件js -->
<script> // 验证码组件初始化
      var opts = {
        "element": "captcha_div", // 可以是验证码容器id，也可以是HTMLElement
        "captchaId": "YOUR_CAPTCHA_ID", // 这里填入申请到的验证码id
        "width": 320, // 验证码组件显示宽度
        "verifyCallback": function(ret){ // 用户只要有拖动/点击，就会触发这个回调
          if(ret['value']){ // true:验证通过 false:验证失败
            $("#submit-btn").removeAttr("disabled"); // 用户完成拖动之后再启用提交按钮
          }
        }
      }
      new NECaptcha(opts);
  </script>
</body>
</html>
