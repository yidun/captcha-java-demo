<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>易盾验证码-DEMO</title>
    <!-- 演示用css，非组件依赖 -->
    <link href='//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css' rel='stylesheet'>
</head>
<body>
<form style="max-width: 320px; margin: 120px auto;" action="/login" method="post">
    <h2 class="form-signin-heading">易盾验证码</h2>
    <input type="text" class="form-control" name="username" placeholder="账号" />
    <input type="password" class="form-control" name="password" placeholder="密码" />
    <div style="margin: 10px auto;" id="captcha_div"></div> <!-- 验证码容器元素定义 -->
    <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit-btn">登录</button>
</form>
<script src="//cstaticdun.126.net/load.min.js"></script>
<script> // 验证码组件初始化
      initNECaptcha({
          captchaId: 'YOUR_CAPTCHA_ID',
          element: '#captcha_div',
          mode: 'embed', // 如果要用触发式，这里改为float即可
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
  </script>
</body>
</html>
