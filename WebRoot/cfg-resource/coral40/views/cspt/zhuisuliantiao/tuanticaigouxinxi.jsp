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
    
    <title>团体信息</title>
    
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
    <div style="text-align: center">企业信息</div>
   		<div>企业名称:${map.ttQyxx['QYMC'] }</div>
   		<div>工商登记证号:${map.ttQyxx['CSZCDJZH'] }</div>
   		<div>法人代表:${map.ttQyxx['FRDB'] }</div>
   		<div>经营地址:${map.ttQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.ttQyxx['LXDH'] }</div>
   		<div>传真:${map.ttQyxx['CZ'] }</div>
   		<c:if test="${not empty map.ttScJcxx }">
	   		<div style="text-align: center">蔬菜进场信息</div>
	   		<div>供应商名称:${map.ttScJcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.ttScJcxx['JCRQ'] }</div>
	   		<div>产地凭证号:${map.ttScJcxx['CDPZH'] }</div>
	   		<div>产地名称:${map.ttScJcxx['CDMC'] }</div>
	   		<div>商品名称:${map.ttScJcxx['SPMC'] }</div>
	   		<div>重量:${map.ttScJcxx['ZL'] }</div>
	   		<div>单价:${map.ttScJcxx['DJ'] }</div>
	   		<div>金额:${map.ttScJcxx['JE'] }</div>
   		</c:if>
   		<c:if test="${not empty map.ttRpJcxx }">
	   		<div style="text-align: center">肉品进场信息</div>
	   		<div>零售商名称:${map.ttRpJcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.ttRpJcxx['JCRQ'] }</div>
	   		<div>商品名称:${map.ttRpJcxx['SPMC'] }</div>
	   		<div>重量:${map.ttRpJcxx['ZL'] }</div>
	   		<div>单价:${map.ttRpJcxx['DJ'] }</div>
	   		<div>金额:${map.ttRpJcxx['JE'] }</div>
   		</c:if>
  </body>
</html>
