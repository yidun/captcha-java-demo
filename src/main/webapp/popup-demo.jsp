<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <meta name="referrer" content="no-referrer">
  <title>验证码示例-popup模式</title>
  <link href='//cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css' rel='stylesheet'>
</head>
<body>
<form style="max-width: 320px; margin: 120px auto;" action="/login" method="post">
    <h2 class="form-signin-heading">易盾验证码</h2>
    <input type="text" class="form-control" name="username" placeholder="账号" />
    <input type="password" class="form-control" name="password" placeholder="密码" />
    <div style="margin: 10px auto;" id="captcha_div"></div> <!-- 验证码容器元素定义 -->
    <button style="width: 320px; height:34px; margin: 10px auto;" id="j-popup">点击弹出验证码</button>
    <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit-btn">登录</button>
</form>
<script charset="UTF-8" type="text/javascript" src="http://cstaticdun.126.net/load.min.js"></script>
<script>
  var captchaIns;
  initNECaptcha({
    element: '#captcha_div',
    captchaId: 'eda6d7f57cf54b5d8f9b0ed24e5b6e66',
    mode: 'popup',
    width: '320px',
  }, function (instance) {
    // 初始化成功后得到验证实例instance，可以调用实例的方法
    captchaIns = instance
  }, function (err) {
    // 初始化失败后触发该函数，err对象描述当前错误信息
  })

  // 监听button的点击事件，弹出验证码
  document.getElementById('j-popup').addEventListener('click', function (event) {
    event.preventDefault();
    captchaIns.popUp();
  });
</script>
</body>
</html>