<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>批发市场信息</title>
    
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
   		<div>企业名称:${map.pcQyxx['QYMC'] }</div>
   		<div>批发市场名称:${map.pcQyxx['PFSCMC'] }</div>
   		<div>工商登记证号:${map.pcQyxx['CSZCDJZH'] }</div>
   		<div>法人代表:${map.pcQyxx['FRDB'] }</div>
   		<div>经营地址:${map.pcQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.pcQyxx['LXDH'] }</div>
   		<div>传真:${map.pcQyxx['CZ'] }</div>
   		<div style="text-align: center">进场信息</div>
   		<div>批发商名称:${map.pcJclhxx['PFSMC'] }</div>
   		<div>进场日期:${map.pcJclhxx['JCRQ'] }</div>
   		<div>产地凭证号:${map.pcJclhxx['CDPZH'] }</div>
   		<div>运输车牌号:${map.pcJclhxx['YSCPH'] }</div>
   		<div>产地名称:${map.pcJclhxx['CDMC'] }</div>
   		<div>生产基地:${map.pcJclhxx['SCJD'] }</div>
   		<div>进场货物重量:${map.pcJclhxx['JCHWZL'] }</div>
   		<div>商品名称:${map.pcJclhxx['SPMC'] }</div>
   		<div>重量:${map.pcJclhxx['ZL'] }</div>
   		<div>单价:${map.pcJclhxx['DJ'] }</div>
   		<div>金额:${map.pcJclhxx['JE'] }</div>
   		<div style="text-align: center">检测信息</div>
   		<div>批发商名称:${map.pcJcxx['PFSMC'] }</div>
   		<div>商品名称:${map.pcJcxx['SPMC'] }</div>
   		<div>样品编号:${map.pcJcxx['YPBH'] }</div>
   		<div>检测员:${map.pcJcxx['JCY'] }</div>
   		<div>检测日期:${map.pcJcxx['JCRQ'] }</div>
   		<div>检测结果:${map.pcJcxx['JCJG'] }</div>
   		<div>结果说明:${map.pcJcxx['JCJGSM'] }</div>
   		<div style="text-align: center">交易信息</div>
   		<div>批发商名称:${map.pcJyxx['PFSMC'] }</div>
   		<div>商品名称:${map.pcJyxx['SPMC'] }</div>
   		<div>交易日期:${map.pcJyxx['JYRQ'] }</div>
   		<div>重量:${map.pcJyxx['ZL'] }</div>
   		<div>单价:${map.pcJyxx['DJ'] }</div>
   		<div>金额:${map.pcJyxx['JE'] }</div>
   		<div>到达地:${map.pcJyxx['DDD'] }</div>
   </div>
  </body>
</html>
