<%@ page language="java" import="com.ces.config.utils.CommonUtil" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>

<style type="text/css">
	.showValue {
		margin-left: 10px;
	}
</style>

<ces:form id="enterForm">
	<div class="item">
		<label>企业编码:</label><span class="showValue" id="qybm${idSuffix}"></span></br>
		<label>企业名称:</label><span class="showValue" id="qymc${idSuffix}"></span></br>
		<label>联系人:</label><span class="showValue" id="lxr${idSuffix}"></span></br>
		<label>邮箱:</label><span class="showValue" id="yx${idSuffix}"></span></br>
		<label>手机:</label><span class="showValue" id="sj${idSuffix}"></span></br>
		<label>座机:</label><span class="showValue" id="zj${idSuffix}"></span></br>
		<label>传真:</label><span class="showValue" id="cz${idSuffix}"></span></br>
		<label>地址:</label><span class="showValue" id="dz${idSuffix}"></span></br>
		<label>收件邮箱:</label><span class="showValue" id="sjyx${idSuffix}"></span></br>
		<label>收信手机:</label><span class="showValue" id="sxsj${idSuffix}"></span></br>
	</div>
	<div class="item">
		<label>创建时间:</label><span class="showValue" id="cjsj${idSuffix}"></span></br>
		<label>激活时间:</label><span class="showValue" id="shsj${idSuffix}"></span></br>
		<label>企业用户数:</label><span class="showValue" id="qyyhs${idSuffix}"></span></br>
		<label>基本服务:</label><span class="showValue" id="jbfw${idSuffix}"></span></br>
		<label style="float:left;">增值服务:</label><span class="showValue" id="zzfw${idSuffix}" style="float:left;"></span></br>
	</div>
</ces:form>
<script type="text/javascript">
	$(document).ready(function () {
		$.ajax({
			type: "post",
			url: $.contextPath + "/qyptqtzh!getOrgInfo.json",
			dataType: 'json',
			success: function (data) {
				$("#qybm${idSuffix}").html(data.QYBM)
				$("#qymc${idSuffix}").html(data.QYMC);
				$("#lxr${idSuffix}").html(data.LXR);
				$("#yx${idSuffix}").html(data.YX);
				$("#sj${idSuffix}").html(data.SJ);
				$("#zj${idSuffix}").html(data.ZJ);
				$("#cz${idSuffix}").html(data.CZ);
				$("#dz${idSuffix}").html(data.DZ);
				$("#cjsj${idSuffix}").html(data.ZHCJSJ);
				$("#shsj${idSuffix}").html(data.ZHSHSJ);
				$("#sjyx${idSuffix}").html(data.SJYX);
				$("#sxsj${idSuffix}").html(data.SXSJ);
				$("#qyyhs${idSuffix}").html(data.DQYHS);
				$("#cckj${idSuffix}").html(data.CCKJ);
				//基本服务
				if (data.jbfw) {
					var jbfw = data.jbfw;
					$("#jbfw${idSuffix}").html(jbfw.FWMC + "(" + jbfw.QYSJ + "~" + jbfw.DQSJ + ")");
				} else {
					$("#jbfw${idSuffix}").html("无");
				}
				//增值服务列表
				if (data.zzfwList) {
					var zzfwList = data.zzfwList;
					var zzfwHtml = "";
					for (var i = 0; i < zzfwList.length; ++i) {
						zzfwHtml += "<span style='text-align:left;display:block;width:100%;'>" + zzfwList[i].FWMC + "(" + zzfwList[i].ZZFWZ + zzfwList[i].GG + ")</span>";
					}
					$("#zzfw${idSuffix}").html(zzfwHtml);
				} else {
					$("#zzfw${idSuffix}").html("无");
				}

			}
		})
	})
</script>