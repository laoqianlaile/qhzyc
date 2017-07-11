<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>正向追溯链条</title>
    
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
  <form>
  	<table>
  		<tr>
  			<td>
  				进货批次号：
  			</td>
  			<td>
  				<input type="text" id="jhpch" name="jhpch"/>
  			</td>
  		</tr>
    </table>
    <div><a href="javascript:void(0)" onclick="turnTo()">追溯</button></a>
  </form>
  </body>
  <script type="text/javascript">
  	function turnTo(){
  		var jhpch = document.getElementById("jhpch").value;
  		if(jhpch==""){
  			alert("请输入进货批次号！");
  			return ;
  		}
  		window.location.href="<%=path%>/cfg-resource/coral40/views/cspt/zhuisuliantiao/zhuisuliuchengTest.jsp?jhpch="+jhpch;
  	}
  </script>
</html>
