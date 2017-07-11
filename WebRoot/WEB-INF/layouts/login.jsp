<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<title>Xarch架构:<sitemesh:title/></title>
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<script type="javascript/text">
var contextPath="${ctx}";
</script>

<script src="${ctx}/js/common.js" type="text/javascript"></script>

<sitemesh:head/>
</head>
<body <sitemesh:getProperty property="body.onload" writeEntireProperty="true"></sitemesh:getProperty>>
	<div class="container">
		<%@ include file="/WEB-INF/layouts/header.jsp"%>
		<div id="content">
			<sitemesh:body/>
		</div>
		<%@ include file="/WEB-INF/layouts/footer.jsp"%>
	</div>
</body>
</html>