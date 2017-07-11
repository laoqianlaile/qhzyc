<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.EjDatabaseUtil" %>

<%

	String sql = "select iRecId,sPrtCode   from tbRecsLink  ";
	List<Map<String, Object>> listej = EjDatabaseUtil.queryForList(sql);

%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP '500.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">

</script>
  </head>
  
  <body >
  <%System.out.println("-----"+listej.size());
	  if (listej != null && listej.size() > 0) {

		  for (Map<String, Object> ejmap : listej) {
			  System.out.println(ejmap.get("iRecId"));
			  System.out.println(ejmap.get("sPrtCode"));
			  /*记录ID	iRecId
			  产品编码	sPrtCode
			  条码	sCode
			  父级条码	sPcode
			  成品批次号	sBatchNo
			  SAP订单号	SapNo
			  生产日期	dManufacturingDate
			  有效期	dExpirationDate
			  购买方或领料方	sBuyer
			  是否是尾箱	iTrunk
			  是否是整箱	iFull
			  是否检验合格	iCheck
			  是否同步	iSynStatus
			  同步时间	dSynDate
			  立体库是否同步	iPack
			  立体库同步时间	dPacksynDate
			  数据上传时间	dUpDate
			  数据关联时间	dRecsLinkDate*/
		  }
		  }
  %>
    <br>
  </body >
</html>
