<%@ page contentType="text/html; charset=UTF-8" %>
<html>
  <%@include file="/head.jsp"%>
<body>
  <div class="wrapper">
    <%@include file="/nav.jsp"%>
    <%@include file="/form.jsp"%>
  </div>
    <script src="http://nctest-server.nis.netease.com/js/c.js"></script>
    <script>
        var opts = {
          "staticServer": "nctest-server.nis.netease.com",
          "apiServer": "nctest-server.nis.netease.com",
          "captchaId": "6b144233421749dabd8f5081f792040f", // 自动切换测试
          "width": 320
        }
        new NECaptcha(document.getElementById('captcha_div'), opts);
    </script>
  </body>
</html>
