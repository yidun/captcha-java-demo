<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <head>
  <title>易盾验证码-DEMO</title>
  <link rel='stylesheet' href='http://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.css'><!-- 演示用css，非组件依赖 -->
<style>
.wrapper {
  margin-bottom: 80px;
}
.form-signin {
  max-width: 390px;
  padding: 15px 35px 45px;
  margin: 20px auto;
  background-color: #fff;
  border: 1px solid rgba(0,0,0,0.1);
}
</style>
</head>
<body>
  <div class="wrapper">
    <nav class="navbar navbar-default">
      <div class="container-fluid">
        <div class="navbar-header">
          <a class="navbar-brand" href="#">易盾验证码</a>
        </div>
        <ul class="nav navbar-nav">
          <li><a href="/index.php">首页</a></li>
        </ul>
      </div>
    </nav>
    <form class="form-signin" action="/login" method="post">
      <h2 class="form-signin-heading">易盾验证码</h2>
      <input type="hidden" name="type" value="click" />
      <input type="text" class="form-control" name="username" placeholder="账号" autofocus="" />
      <input type="password" class="form-control" name="password" placeholder="密码" />
      <div style="margin: 10px auto;" id="captcha_div"></div>
      <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit-btn">登录</button>
    </form>
  </div>
  <script src="http://nctest-server.nis.netease.com/js/c.js"></script>
  <script>
      var opts = {
        "staticServer": "nctest-server.nis.netease.com",
        "apiServer": "nctest-server.nis.netease.com",
        "captchaId": "6b144233421749dabd8f5081f792040f",
        "width": 320
      }
      var instance = new NECaptcha(document.getElementById('captcha_div'), opts);
  </script>
</body>
</html>
