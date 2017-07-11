<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>猪肉批发信息</title>
    
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
   		<div>企业名称:${map.rpQyxx['QYMC'] }</div>
   		<div>批发市场名称:${map.rpQyxx['PFSCMC'] }</div>
   		<div>工商登记证号:${map.rpQyxx['CSZCDJZH'] }</div>
   		<div>法人代表:${map.rpQyxx['FRDB'] }</div>
   		<div>经营地址:${map.rpQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.rpQyxx['LXDH'] }</div>
   		<div>传真:${map.rpQyxx['CZ'] }</div>
   		<div style="text-align: center">进场信息</div>
   		<div>批发商名称:${map.rpJcxx['PFSMC'] }</div>
   		<div>进场日期:${map.rpJcxx['JCRQ'] }</div>
   		<div>运输车牌号:${map.rpJcxx['YSCPH'] }</div>
   		<div>产地名称:${map.rpJcxx['CDMC'] }</div>
   		<div>商品名称:${map.rpJcxx['SPMC'] }</div>
   		<div>重量:${map.rpJcxx['ZL'] }</div>
   		<div>单价:${map.rpJcxx['DJ'] }</div>
   		<div>金额:${map.rpJcxx['JE'] }</div>
   		<div style="text-align: center">检测信息</div>
   		<div>批发商名称:${map.rpJcxx2['PFSMC'] }</div>
   		<div>商品名称:${map.rpJcxx2['SPMC'] }</div>
   		<div>检测员:${map.rpJcxx2['JCY'] }</div>
   		<div>检测日期:${map.rpJcxx2['JCRQ'] }</div>
   		<div>检测结果:${map.rpJcxx2['JCJG'] }</div>
   		<div style="text-align: center">交易信息</div>
   		<div>批发商名称:${map.rpJyxx['PFSMC'] }</div>
   		<div>商品名称:${map.rpJyxx['SPMC'] }</div>
   		<div>交易日期:${map.rpJyxx['JYRQ'] }</div>
   		<div>重量:${map.rpJyxx['ZL'] }</div>
   		<div>单价:${map.rpJyxx['DJ'] }</div>
   		<div>金额:${map.rpJyxx['JE'] }</div>
   		<div>到达地:${map.rpJyxx['DDD'] }</div>
  </body>
</html>
