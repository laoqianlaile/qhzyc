<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>养殖信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<%@ include file="/cfg-resource/coral40/common/jsp/_cui_library.jsp"%> 
	<%@ include file="/cfg-resource/coral40/views/cspt/zhuisuliantiao/yzxx.jsp"%>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <div style="text-align: center">企业信息</div>
   		<div>企业名称:${map.yzQyxx['QYMC'] }</div>
   		<div>养殖场名称:${map.yzQyxx['YZCMC'] }</div>
   		<div>工商登记证号:${map.yzQyxx['GSZCDJZH'] }</div>
   		<div>产地名称:${map.yzQyxx['CDMC'] }</div>
   		<div>产地证书号:${map.yzQyxx['CDZSH'] }</div>
   		<div>动物防疫条件合格证号:${map.yzQyxx['DWFYTJHGZH'] }</div>
   		<div>养殖场面积（平方米）:${map.yzQyxx['YZCMJ'] }</div>
   		<div>养殖场地址:${map.yzQyxx['YZCDZ'] }</div>
   		<div>产地描述:${map.yzQyxx['CDMS'] }</div>
   		<div style="text-align: center">进栏信息</div>
   		<div>进栏日期:${map.yzJlxx['JLRQ'] }</div>
   		<div>使用猪舍:${map.yzJlxx['SYZS'] }</div>
   		<div>仔猪批次号:${map.yzJlxx['ZZPCH'] }</div>
   		<div>数量（头）:${map.yzJlxx['SL'] }</div>
   		<div>负责人:${map.yzJlxx['FZR'] }</div>
   		<div>品种通用名:${map.yzJlxx['PZTYM'] }</div>
   		<div style="text-align: center">饲料信息</div>
   		<div id="gridDemo1"><div class="gridDemo1"></div></div>
   		<div style="text-align: center">用药信息</div>
   		<div id="gridDemo2"><div class="gridDemo1"></div></div>
   		<div style="text-align: center">出栏信息</div>
   		<div>产地检疫证号:${map.yzClxx['SZCDJYZH'] }</div>
   		<div>出栏日期:${map.yzClxx['CLRQ'] }</div>
   		<div>数量（头）:${map.yzClxx['SL'] }</div>
   		<div>到达地:${map.yzClxx['DDD'] }</div>
   		<div>运输车牌号:${map.yzClxx['YSCPH'] }</div>
   		<div>负责人:${map.yzClxx['FZR'] }</div>
  </body>
</html>
