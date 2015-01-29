
<%@page import="java.util.stream.IntStream"%>
<%@page import="java.util.stream.Stream"%>
<%@page import="vgames.util.VGLogin"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="myclass.wrap.MyArray"%>

<%@page import="myclass.util.Convert"%>
<%@page import="vgames.table.User"%>

<%@page import="myclass.servlet.Path"%>
<%@page import="vgames.util.WrapJspWriter"%>
<%@page import="myclass.database.MyDatabase"%>
<%@page import="java.util.stream.Stream" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>V GAMES マイページ</title>


<%
request.setCharacterEncoding("utf8");
User user=new User();
MyDatabase db= VGLogin.checkLogin(request, response,user);
if(user.getId()==null){
    db.close();
    return;
}
final String
	CONPATH=request.getContextPath()+"/";
WrapJspWriter.writeScript(out, CONPATH, Path.JS_PATH, Path.getJsLibs());
WrapJspWriter.writeScript(out, CONPATH, Path.JS_PATH, Path.LOGOUTJS,Path.CONTROLLERJS);

String escUname=Convert.escapeHtml(user.getName());

db.close();
%>

</head>
<body>
<div id="header">
<h3>ようこそ<%=escUname %>さん</h3>
<%=IntStream.iterate(2, f->{
    return f*f;
    }).limit(10).sum()%>
</div>
</body>
</html>
