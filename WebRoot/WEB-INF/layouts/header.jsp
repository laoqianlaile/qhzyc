<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<style type="text/css">
	.bg {
		vertical-align: middle;
		padding-left: 30px;
	}
	.bg2 {
		background: url("${ctx}/cfg-resource/dhtmlx/common/images/index_title.jpg");
	}
	.logoutArea {
		width: 120px;
		height: 26px;
		overflow: hidden;
		text-align: center;
		font-size: 12px;
		line-height: 22px;
		border: medium none;
		position: relative;
		background-color: #daeafe;
		padding-top: 2px;
		-moz-user-select: none;
	}
	.logoutArea .logout {
		width: 40px;
		height: 22px;
		line-height: 22px;
		margin-top: 2px;
		margin-left: 2px;
		margin-right: 2px;
		cursor: pointer;
		float: left;
	}
	.logoutArea .logout_over {
		cursor: pointer;
		border: #ffb552 1px solid;
		width: 38px;
		height: 20px;
		line-height: 22px;
		margin-top: 1px;
		margin-left: 2px;
		margin-right: 2px;
		background-color: #ffe4aa;
		float: left;
	}
</style>
<script type="text/javascript">
<!--
	function closeMe() {
		window.location = '${ctx}/logout';
		window.opener = null;
		window.open("","_self") ;
		window.close();
	}
	function logout() {
		window.location = '${ctx}/logout';
	}
//-->
</script>
<div id="header">
	<table height="100%" width="100%" border="0" cellspacing="0"
		cellpadding="0">
		<tr height="80">
			<td colspan="2" width="100%" class="bg2"></td>
		</tr>
		<tr height="20">
			<td width="100%">
				<div id="menuObj" style="height: 20px;overflow: hidden;"></div>
			</td>
			<td>
				<div class="logoutArea">
					<div class="logout" onmouseover="this.className='logout_over'" onmouseout="this.className='logout'" onclick="logout();">注销</div>
					<div class="logout" onmouseover="this.className='logout_over'" onmouseout="this.className='logout'" onclick="closeMe();">退出</div>
				</div>
			</td>
		</tr>
	</table>
</div>