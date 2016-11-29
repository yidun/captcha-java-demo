    <%@ page contentType="text/html; charset=UTF-8" %>
    <form class="form-signin" action="/login" method="post">
      <h2 class="form-signin-heading">易盾验证码</h2>
      <input type="hidden" name="type" value="click" />
      <input type="text" class="form-control" name="username" placeholder="账号" autofocus="" />
      <input type="password" class="form-control" name="password" placeholder="密码" />
      <div style="margin: 10px auto;" id="captcha_div"></div>
      <button class="btn btn-lg btn-primary btn-block" type="submit" id="submit-btn">登录</button>
    </form>