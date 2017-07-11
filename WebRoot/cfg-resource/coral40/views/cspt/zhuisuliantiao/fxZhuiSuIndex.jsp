<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>反向追溯链条</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  <div style="">追溯码：</div>
  <div style="width:80%"><input type="text" style="" id="zsm" name="zsm"/></div>
  <div><a href="javascript:void(0)" onclick="turnTo()">追溯</button></a>
  </body>
  <script type="text/javascript">
  	function turnTo(){
  		var zsm = document.getElementById("zsm").value;
  		if(zsm==""){
  			alert("请输入追溯码！");
  			return ;
  		}
  		window.location.href="<%=path%>/zhuisuliantiao!list?zsm="+zsm;
  	}
  </script>
</html>
