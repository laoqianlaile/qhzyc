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
    
    <title>企业信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@ include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp"%> 
	<%@ include file="/cfg-resource/coral40/views/cspt/zhuisuliantiao/zzxx.jsp"%>
 </head>
  
  <body>
   <div>
   		<div style="text-align: center">企业信息</div>
   		<div>企业名称:${map.zzQyxx['QYMC'] }</div>
   		<div>种植基地名称:${map.zzQyxx['ZZJDMC'] }</div>
   		<div>工商登记证号:${map.zzQyxx['GSZCDJZH'] }</div>
   		<div>产地名称:${map.zzQyxx['CDMC'] }</div>
   		<div>产地证书号:${map.zzQyxx['CDZSH'] }</div>
   		<div>种植基地面积:${map.zzQyxx['ZZJDMJ'] }</div>
   		<div>种植基地地址:${map.zzQyxx['ZZJDDZ'] }</div>
   		<div>产地描述:${map.zzQyxx['CDMS'] }</div>
   		<div style="text-align: center">栽培信息</div>
   		<div>使用菜种:${map.zzZpxx['SYCZ'] }</div>
   		<div>使用菜田:${map.zzZpxx['SYCT'] }</div>
   		<div>栽培日期:${map.zzZpxx['ZPRQ'] }</div>
   		<div>负责人:${map.zzZpxx['FZR'] }</div>
   		<div>商品名称:${map.zzZpxx['SPMC'] }</div>
   		<div style="text-align: center">饲养信息</div>
   		<div id="gridDemo1"><div class="gridDemo1"></div>
   		<div style="text-align: center">用药信息</div>
   		<div id="gridDemo2"><div class="gridDemo1"></div>
   		<div style="text-align: center">出厂信息</div>
   		<div>负责人:${map.zzCcxx['FZR'] }</div>
   		<div>采收日期:${map.zzCcxx['CSRQ'] }</div>
   		<div>储存方式:${map.zzCcxx['CCFS'] }</div>
   		<div>出场日期:${map.zzCcxx['CCRQ'] }</div>
   		<div>产地证明号:${map.zzCcxx['CDZMH'] }</div>
   		<div>检测合格证号:${map.zzCcxx['JCHGZH'] }</div>
   		<div>重量:${map.zzCcxx['ZL'] }</div>
   		<div>到达地:${map.zzCcxx['DDD'] }</div>
   		<div>运输车牌号:${map.zzCcxx['YSCPH'] }</div>
   </div>
  </body>
</html>
