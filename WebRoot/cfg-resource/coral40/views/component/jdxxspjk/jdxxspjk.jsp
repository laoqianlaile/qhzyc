<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
%>
<!doctype html>
<html>
<head>
    <title>视频监控</title>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
</head>
<body>
<div style="text-align:center;">
    <iframe src="http://jk.hjtej.com:9000" width="790" height="530" frameborder=0></iframe>
</div>
</body>
</html>