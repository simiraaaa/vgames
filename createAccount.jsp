<%@page import="myclass.servlet.Path"%>
<%@page import="vgames.util.WrapJspWriter"%>
<%@page import="myclass.MyCreateTag"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
request.setCharacterEncoding("utf8");
final String CONPATH=request.getContextPath()+"/";
final String[]
        JSLIBS=Path.getJsLibs(),
        SCRIPTS={Path.CREATE_ACCOUNTJS};
WrapJspWriter.writeScript(out, CONPATH, Path.JS_PATH, JSLIBS,SCRIPTS);
%>
<script type="text/javascript">smr.ajax._url="<%=CONPATH+Path.SERVLET+Path.ACREG%>";</script>
<title>アカウント作成</title>
</head>
<body>
</body>
</html>