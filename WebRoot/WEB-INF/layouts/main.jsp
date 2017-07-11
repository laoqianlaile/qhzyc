<%@page import="com.ces.config.utils.StringUtil"%>
<%@page import="com.ces.config.utils.SystemParameterUtil"%>
<%@page import="com.ces.config.utils.CommonUtil"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String userId = CommonUtil.getUser().getId();
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title><sitemesh:title/></title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="text/javascript">
	var contextPath = "${ctx}";
	var basePath = "<%=basePath%>";
	var userId = "<%=userId %>";
</script>
<%
    String views = "dhtmlx";
    String servletPath = request.getServletPath();
    if (StringUtil.isBooleanTrue(request.getParameter("isConfigPlatform"))) {
        views = "dhtmlx";
    } else {
        if (servletPath.indexOf("/dhtmlx/") != -1) {
            views = "dhtmlx";
        } else if (servletPath.indexOf("/coral40/") != -1) {
            views = "coral40";
        } else {
            views = "dhtmlx";
        }
    }
    if ("dhtmlx".equals(views)) {
%>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/base.js" type="text/javascript"></script>
<script src="${ctx}/cfg-resource/dhtmlx/common/js/common.js" type="text/javascript"></script>
<%
    } else if ("coral40".equals(views)) {
        // coral40的代码已经不用装饰器了
    }
%>
<sitemesh:head/>
</head>
<body onresize="layoutResize()" <sitemesh:getProperty property="body.onload" writeEntireProperty="true"></sitemesh:getProperty>>
	<div id="content">
		<sitemesh:body/>
	</div>
</body>
</html>