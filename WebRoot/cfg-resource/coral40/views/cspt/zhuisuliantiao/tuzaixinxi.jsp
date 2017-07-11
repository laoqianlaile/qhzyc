<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>屠宰场信息</title>
    
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
   		<div>企业名称:${map.tzQyxx['QYMC'] }</div>
   		<div>屠宰场名称:${map.tzQyxx['TZCMC'] }</div>
   		<div>工商登记证号:${map.tzQyxx['GSZCDJZH'] }</div>
   		<div>备案日期:${map.tzQyxx['BARQ'] }</div>
   		<div>法人代表:${map.tzQyxx['FRDB'] }</div>
   		<div>经营地址:${map.tzQyxx['JYDZ'] }</div>
   		<div>联系电话:${map.tzQyxx['LXDH'] }</div>
   		<div>传真:${map.tzQyxx['CZ'] }</div>
    	<div style="text-align: center">生猪进场信息</div>
   		<div>产地检疫证号:${map.tzJcxx['SZCDJYZH'] }</div>
   		<div>货主名称:${map.tzJcxx['HZMC'] }</div>
   		<div>生猪进场日期:${map.tzJcxx['SZJCRQ'] }</div>
   		<div>检疫证进场数:${map.tzJcxx['JYZJCSL'] }</div>
   		<div>实际进场数:${map.tzJcxx['SJJCSL'] }</div>
   		<div>途亡数:${map.tzJcxx['TWSL'] }</div>
   		<div>实际进场重量:${map.tzJcxx['SJJCZL'] }</div>
   		<div>采购价:${map.tzJcxx['CGJ'] }</div>
   		<div>检疫结果:${map.tzJcxx['JYJG'] }</div>
   		<div>产地名称:${map.tzJcxx['CDMC'] }</div>
   		<div>养殖场名称:${map.tzJcxx['YZCMC'] }</div>
   		<div>运输车牌号:${map.tzJcxx['YSCPH'] }</div>
   		<div style="text-align: center">生猪检疫信息</div>
   		<div>货主名称:${map.tzSzJyxx['HZMC'] }</div>
   		<div>产地检疫证号:${map.tzSzJyxx['SZCDJYZH'] }</div>
   		<div>检疫证进场数量:${map.tzSzJyxx['JYZJCSL'] }</div>
   		<div>采样头数:${map.tzSzJyxx['CYTS'] }</div>
   		<div>采样样品编号:${map.tzSzJyxx['CYYPBH'] }</div>
   		<div>检验员:${map.tzSzJyxx['JYY'] }</div>
   		<div>抽检日期:${map.tzSzJyxx['CJRQ'] }</div>
   		<div>阳性头数:${map.tzSzJyxx['YXTS'] }</div>
   		<div style="text-align: center">交易信息</div>
   		<div>货主名称:${map.tzJyxx['HZMC'] }</div>
   		<div>交易日期:${map.tzJyxx['JYRQ'] }</div>
   		<div>买主名称:${map.tzJyxx['MZMC'] }</div>
   		<div>产地检疫证号:${map.tzJyxx['SZCDJYZH'] }</div>
   		<div>交易数量:${map.tzJyxx['JYSL'] }</div>
   		<div>商品名称:${map.tzJyxx['SPMC'] }</div>
   		<div>重量:${map.tzJyxx['ZL'] }</div>
   		<div>单价:${map.tzJyxx['DJ'] }</div>
   		<div>金额:${map.tzJyxx['JE'] }</div>
   		<div>到达地:${map.tzJyxx['DDD'] }</div>
  </body>
</html>
