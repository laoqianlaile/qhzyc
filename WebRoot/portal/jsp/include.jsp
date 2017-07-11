
<%-- 引入公用标签 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%-- 定义常用页面变量 --%>
<c:set var="request"     scope="page" value="${pageContext.request}" />
<c:set var="schemeName"  scope="page" value="${request.scheme}"      />
<c:set var="serverName"  scope="page" value="${request.serverName}"  />
<c:set var="serverPort"  scope="page" value="${request.serverPort}"  />
<c:set var="contextPath" scope="page" value="${request.contextPath}" />
<c:set var="basePath"    scope="page" value="${schemeName}://${serverName}:${serverPort}${contextPath}" />
