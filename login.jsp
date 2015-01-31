
<%@page import="vgames.util.WrapJspWriter"%>
<%@page import="myclass.servlet.Path"%>
<%@page import="vgames.util.VGLogin"%>
<%@page import="vgames.table.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>VGAMES Login</title>
<%
request.setCharacterEncoding("utf8");
final String CONPATH=request.getContextPath()+"/";
User user =new User();
VGLogin.checkLogin(request, response, user,CONPATH+Path.SERVLET+Path.MYPAGEHTML,true).close();

final String[]
        JSLIBS=Path.getJsLibs();
WrapJspWriter.writeScript(out, CONPATH, Path.JS_PATH, JSLIBS);
%>
<script type="text/javascript">
(function(smr, undefined){
  this.onload= function(){
      var document = this.document;
      var body = document.body;
      smr.dom.Icon().prependTo(body).setText("V<BR>GAMES");
      var Element = smr.dom.Element;
      var doc = Element();
      var id = doc.query("#id");
      var pass = doc.query("#pass");
      var button = doc.query("#login");
      button.onclick=function(){
        smr.ajax.load({
          type:"POST",
          data:{id:id.value,pass:pass.value},
          charset:"UTF-8",
          url:"<%=CONPATH+Path.SERVLET+Path.LOGINJSON%>",
          dataType:"json",
          success:function(data){
            var s = data.status;
            if(s==="success"){
              smr.global.location.href=data.text;
            }else{
              alert(data.text);
            }
          }
        });
      };

  };
})(smr);
</script>
</head>
<body >

<div style="width:600px; margin:50px auto; text-align: center; ">
ID<br>
<input id="id" type="text"  style="margin: 10px;" /><br>
PASSWORD<br>
<input id="pass" type="password" style="margin: 10px;"/><br>
<button type="button" id="login">ログイン</button>
<div style="height: 50px;"></div>
まだ登録していない場合は、<a href="<%=CONPATH+Path.CREATE_ACCOUNTJSP%>">新規登録</a>

</div>
</body>
</html>
