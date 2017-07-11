<%@ include file="/portal/jsp/include.jsp" %>
<%@ tag language="java" pageEncoding="UTF-8" body-content="tagdependent"%>

<%@ attribute name="uitag" type="java.lang.String" description="指定ui标签"%>
<%@ attribute name="target" type="java.lang.String" description="加载UI库"%>
<%@ attribute name="themes" type="java.lang.String" description="加载UI皮肤"%>
<%@ attribute name="patchs" type="java.lang.String" description="加载JS补丁"%>
<%@ attribute name="packages" type="java.lang.String" description="加载UI组件"%>
<%@ attribute name="stylePath" type="java.lang.String" description="样式路径"%>
<%@ tag import="com.ces.xarch.core.security.entity.SysUser"%>
<%@ tag import="org.springframework.security.core.context.SecurityContext"%>



<!-- 引入公共样式 -->
<link href="${basePath}/portal/css/common.css" rel="stylesheet" type="text/css"/>
<link href="${basePath}/portal/css/icon.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">		
	var basePath = '${basePath}';
	var stylePath = '${stylePath}';
</script>

<!-- basic js -->
<c:choose>
	<c:when test='${uitag eq "ace"}'>
		<script type="text/javascript" src="${basePath}/cfg-resource/coral40/_cui_library/ui/jquery-1.11.1.min.js"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="${basePath}/cfg-resource/coral40/_cui_library/ui/jquery-1.9.1.js"></script>
	</c:otherwise>
</c:choose>

<script type="text/javascript" src="${basePath}/portal/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${basePath}/portal/js/jquery.common.js" ></script>
<script type="text/javascript" src="${basePath}/portal/js/jquery.constant.js" ></script>

<!--[if IE 6]> 
<script type="text/javascript" src="${basePath}/resources/scripts/patch/jquery.ifixpng.js"></script>
<![endif]-->


<!-- 指定ui标签 -->


<!-- 加载UI库 -->
<c:choose>
	<c:when test='${target eq "easyui"}'>
		<link href="${basePath}/coflow/js/jquery-easyui-1.3.2/themes/${themes}/easyui.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${basePath}/coflow/js/jquery-easyui-1.3.2/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="${basePath}/coflow/js/jquery-easyui-1.3.2/locale/easyui-lang-zh_CN.js"></script>
	</c:when>
</c:choose>

<!-- 加载UI组件 -->
<c:forEach var="pack" items='${fn:split(packages,",")}'>
	<c:if test="${!empty pack}">
		<script type="text/javascript" src="${basePath}/portal/js/${pack}.js"></script>
	</c:if>
</c:forEach>
