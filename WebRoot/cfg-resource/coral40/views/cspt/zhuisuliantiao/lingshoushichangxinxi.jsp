<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>零售信息</title>
    
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
   		<div>企业名称:${map.lsQyxx['QYMC'] }</div>
   		<div>工商登记证号:${map.lsQyxx['CSZCDJZH'] }</div>
   		<div>法人代表:${map.lsQyxx['FRDB'] }</div>
   		<div>经营地址:${map.lsQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.lsQyxx['LXDH'] }</div>
   		<div>传真:${map.lsQyxx['CZ'] }</div>
   		<c:if test="${not empty map.lsScJcxx }">
	   		<div style="text-align: center">蔬菜进场信息</div>
	   		<div>供应商名称:${map.lsScJcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.lsScJcxx['JCRQ'] }</div>
	   		<div>产地凭证号:${map.lsScJcxx['CDPZH'] }</div>
	   		<div>产地名称:${map.lsScJcxx['CDMC'] }</div>
	   		<div>商品名称:${map.lsScJcxx['SPMC'] }</div>
	   		<div>重量:${map.lsScJcxx['ZL'] }</div>
	   		<div>单价:${map.lsScJcxx['DJ'] }</div>
	   		<div>金额:${map.lsScJcxx['JE'] }</div>
   		</c:if>
   		<c:if test="${not empty map.lsRpJcxx }">
	   		<div style="text-align: center">肉品进场信息</div>
	   		<div>零售商名称:${map.lsRpJcxx['GYSMC'] }</div>
	   		<div>进场日期:${map.lsRpJcxx['JCRQ'] }</div>
	   		<div>产地凭证号:${map.lsRpJcxx['CDPZH'] }</div>
	   		<div>产地名称:${map.lsRpJcxx['CDMC'] }</div>
	   		<div>商品名称:${map.lsRpJcxx['SPMC'] }</div>
	   		<div>重量:${map.lsRpJcxx['ZL'] }</div>
	   		<div>单价:${map.lsRpJcxx['DJ'] }</div>
	   		<div>金额:${map.lsRpJcxx['JE'] }</div>
   		</c:if>
  </body>
</html>
