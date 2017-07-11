<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>超市信息</title>
    
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
   <div>
   		<div style="text-align: center">企业信息</div>
   		<div>企业名称:${map.csQyxx['QYMC'] }</div>
   		<div>工商登记证号:${map.csQyxx['CSZCDJZH'] }</div>
   		<div>法人代表:${map.csQyxx['FRDB'] }</div>
   		<div>经营地址:${map.csQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.csQyxx['LXDH'] }</div>
   		<div>传真:${map.csQyxx['CZ'] }</div>
   		<c:if test="${not empty map.csScJcxx }">
	   		<div style="text-align: center">蔬菜进场信息</div>
	   		<div>供应商名称:${map.csScJcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.csScJcxx['JCRQ'] }</div>
	   		<div>产地凭证号:${map.csScJcxx['CDPZH'] }</div>
	   		<div>产地名称:${map.csScJcxx['CDMC'] }</div>
	   		<div>商品名称:${map.csScJcxx['SPMC'] }</div>
	   		<div>重量:${map.csScJcxx['ZL'] }</div>
	   		<div>单价:${map.csScJcxx['DJ'] }</div>
	   		<div>金额:${map.csScJcxx['JE'] }</div>
   		</c:if>
   		<c:if test="${not empty map.csRpJcxx }">
	   		<div style="text-align: center">蔬菜进场信息</div>
	   		<div>零售商名称:${map.csRpjcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.csRpjcxx['JCRQ'] }</div>
	   		<div>产地凭证号:${map.csRpjcxx['CDPZH'] }</div>
	   		<div>产地名称:${map.csRpjcxx['CDMC'] }</div>
	   		<div>商品名称:${map.csRpjcxx['SPMC'] }</div>
	   		<div>重量:${map.csRpjcxx['ZL'] }</div>
	   		<div>单价:${map.csRpjcxx['DJ'] }</div>
	   		<div>金额:${map.csRpjcxx['JE'] }</div>
   		</c:if>
   </div>
  </body>
</html>
