<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'demo.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<%@include file="../common/jsp/_cui_library.jsp" %>
  </head>
  
  <body>
  <br/>
  <ces:button id="btn" label="test"></ces:button>
  <div id="combobox"></div>
  
  <ces:grid id="gridDemo2" width="auto" height="300" gridModel="grid" url="<%=gurl %>">
	<ces:gridCols>
		<ces:gridCol name="columnName" width="100">字段名额</ces:gridCol>
		<ces:gridCol name="showName" width="180">中文名称</ces:gridCol>
	</ces:gridCols>
	<ces:gridPager gridId="gridDemo2" />
</ces:grid>
<br/>
<ces:tree id="tree5"  asyncEnable="true" asyncType="post" checkable="true" asyncUrl="<%=turl %>" asyncAutoParam="id,name" onClick="asyncOnclick" onLoad="asyncExpandnode">  
	
	</ces:tree>
    This is my JSP page. <br> 
    <script type="text/javascript">
    	//alert($.contexPath);
    	$(function() {
    		$("#combobox").combobox({width: 180, showClose: true});
    	});
    </script>
  </body>
</html>
