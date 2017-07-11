<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String gurl = path + "/appmanage/column-define!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=columnName,showName";
    String turl = path + "/appmanage/tree-define!tree.json?E_frame_name=coral&E_model_name=tree&P_ISPARENT=child&F_in=name,id&P_OPEN=true&P_filterId=parentId&P_CHECKED=838386ae45acc0c60145acc412bb0003";
    String qybh = request.getParameter("qybh");
    String dkbh = request.getParameter("dkbh");
    String dybh = request.getParameter("dybh");
    String icNum = request.getParameter("icNum");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>操作页面</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <%@include file="../../common/jsp/_cui_library.jsp" %>
<style type="text/css">
    a{
        cursor: hand;
        font-size: 20px;
    }
</style>
</head>

<body>
<div>
    <div>
        <ul>
            <li><a href="<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/bzczym.jsp?qybh=<%=qybh%>&icNum=<%=icNum%>">播种</a></li>
            <li><a href="<%=path%>/cfg-resource/coral40/views/nsjlcmpzxt/ggczym.jsp?qybh=<%=qybh%>&icNum=<%=icNum%>">灌溉</a></li>
            <li><a>施肥</a></li>
            <li><a>用药</a></li>
            <li><a>锄草</a></li>
            <li><a>采收</a></li>
            <li><a>其他</a></li>
        </ul>
    </div>
</div>
</body>
</html>