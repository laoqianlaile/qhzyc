<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
List ltList = (List)request.getAttribute("ltList");
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
  	<c:forEach items = "${ltList }" var="entity" varStatus="status">
  		<div style="text-align: center;border:1px solid;padding-top:10px; width:200px;height:100px;margin:0 auto;">
  		<a target="_blank" href="<%=path%>/zhuisuliantiao!getZhuisuxxxx?xtlx=${entity['XTLX']}&qybm=${entity['QYBM']}&refId=${entity['REFID']}">
  		<c:if test="${ entity['XTLX']=='1' }">
  			种植基地：
  				${entity['QYMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='2' }">
  			养殖基地：
  				${entity['QYMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='3' }">
  			批发市场：${entity['QYMC']}
  			</br>批发商：${entity['JYZMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='4' }">
  			屠宰场：${entity['QYMC']}
  			</br>货主：${entity['JYZMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='5' }">
  			批发市场：${entity['QYMC']}
  			</br>批发商：${entity['JYZMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='6' }">
  			采购场所：${entity['QYMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='7' }">
  			超市：${entity['QYMC']}
  		</c:if>
  		<c:if test="${ entity['XTLX']=='8' }">
  			零售市场：${entity['QYMC']}
  			</br>零售商：${entity['JYZMC']}
  		</c:if>
  		</a>
  		</div>
  		<c:if test="${ status.index+1!=size }">
  			<div style="text-align: center;width:200px;height:100px;margin:0 auto;">
			<img src="<%=path %>/cfg-resource/coral40/views/cspt/zhuisuliantiao/cha6.gif"></img>
			</div>
		</c:if>
  	</c:forEach>
  </body>
</html>
