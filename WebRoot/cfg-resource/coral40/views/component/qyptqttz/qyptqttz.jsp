<%@ page language="java" import="com.ces.config.utils.CommonUtil" pageEncoding="UTF-8" %>
<%@ page import="com.ces.xarch.core.security.entity.SysUser" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
	SysUser user = CommonUtil.getUser();
%>
<style type="text/css">
	.conStyle {
		width: 90%;
		height: 80%;
		margin-top: 6%;
	}

	#leftTDiv, #rightTDiv, #leftBDiv, #rightBDiv {
		width: 48%;
		height: 48%;
		border: 1px solid gray;
		float: left;
		margin-top: 10px;
		margin-left: 10px;
	}
	.leftTitleColumn{
		width:30%;
		text-align:center;
		float:left;
		border:1px solid;
	}
	.titleColumn{
		width:30%;
		text-align:center;
		float:left;
		border-top: 1px solid;
		border-bottom: 1px solid;
		border-right:1px solid;
	}
	.leftContentColumn{
		width:30%;
		text-align:center;
		float:left;
		border-bottom: 1px solid;
		border-left:1px solid;
		border-right:1px solid;
	}
	.contentColumn{
		width:30%;
		text-align:center;
		float:left;
		border-bottom: 1px solid;
		border-right:1px solid;
	}
</style>
<div id="max${idSuffix}" style="height:100%;width:100%">
<div id="title" style="margin-top:20px;">
	<a style="font-size:20px;color:blue;margin-left:5%;">您好！<%=user.getName()%>!</a>
</div>
<div id="content" class="conStyle">
	<div id="leftTDiv">
		<div style="margin-top:3px;margin-left:5px;font-size:17px;">账户信息</div>
		<div style="margin-top:3px;margin-left:5px;">
			<ul>
				<li style="font-size:15px;margin-top:5px;">单位名称：<span id="qymc${idSuffix}"></span></li>
				<li style="font-size:15px;margin-top:5px;">审核时间：<span id="shsj${idSuffix}"></span></li>
				<li style="font-size:15px;margin-top:5px;">工商执照：<span id="yyzz${idSuffix}"></span></li>
			</ul>
		</div>
	</div>
	<div id="rightTDiv">
		<div style="margin-top:3px;margin-left:5px;font-size:17px;">消息列表</div>
		<div id="xxlb${idSuffix}" style="margin-top:3px;margin-left:5px;">
			<ul>
			</ul>
		</div>
	</div>
	<div id="leftBDiv">
		<div style="margin-top:3px;margin-left:5px;font-size:17px;">基本服务</div>
		<div style="margin-top:3px;margin-left:5px;">
			<ul>
				<li style="font-size:15px;margin-top:5px;">服务名称：<span id="fwmc${idSuffix}"></span></li>
				<li style="font-size:15px;margin-top:5px;">服务起始时间：<span id="qssj${idSuffix}"></span></li>
				<li style="font-size:15px;margin-top:5px;">服务结束时间：<span id="jssj${idSuffix}"></span></li>
			</ul>
		</div>
		<div style="margin-top:3px;margin-left:5px;font-size:17px;">系统状态</div>
		<div style="margin-top:3px;margin-left:5px;">
			<ul>
				<!--
				<li style="font-size:15px;margin-top:5px;">存储空间使用情况：<span id="shsj"></span></li>
				-->
				<li style="font-size:15px;margin-top:5px;">当前用户数：<span id="dqyhs${idSuffix}"></span></li>
			</ul>
		</div>

	</div>
	<div id="rightBDiv">
		<div style="margin-top:3px;margin-left:5px;font-size:17px">企业用户</div>
		<div id="userList" style="margin-left:10%">
			<div>
				<div class="leftTitleColumn" >企业用户名</div>
				<div class="titleColumn" >用户姓名</div>
				<div class="titleColumn" >状态</div>
			</div>
			<div>
				<div class="leftContentColumn">1</div>
				<div class="contentColumn">2</div>
				<div class="contentColumn">3</div>
			</div>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		currentId:'',
		infoClick: function (dataId) {
			currentId = dataId;
			CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "info", "消息", 2, $.ns("namespaceId${idSuffix}"));
		},
		setXxId:function(o){
			return {
				status: true,
				xxid: currentId
			};
		}
	});

	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'qyptqttz.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage': $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone': function () {
				return $('#max${idSuffix}');
			},
			/** 初始化预留区 */
			<%--'initReserveZones' : function(configInfo) {--%>
			<%--CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'cdmc', $('#toolbarId${idSuffix}').toolbar("getLength")-1);--%>
			<%--},--%>
			/** 获取返回按钮添加的位置 */
			<%--'setReturnButton' : function(configInfo) {--%>
			<%--CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));--%>
			<%--},--%>
			/** 页面初始化的方法 */
			'bodyOnLoad': function (configInfo) {
				//alert("bodyOnLoad");
				// 按钮权限控制
				//alert(configInfo.notAuthorityComponentButtons);
				/*if (configInfo.notAuthorityComponentButtons) {
					$.each(configInfo.notAuthorityComponentButtons, function (i, v) {
						if (v == 'add') {
							//$('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
							$('#toolbarId${idSuffix}').toolbar('hide', 'add');
						} else if (v == 'update') {
							//$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
							$('#toolbarId${idSuffix}').toolbar('hide', 'update');
						} else if (v == 'delete') {
							//$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
							$('#toolbarId${idSuffix}').toolbar('hide', 'delete');
						}
					});
				}*/
				$.ajax({
					url: $.contextPath + "/qyptqttz!queryTzxx.json",
					type: "post",
					success: function (data) {
						var qyxx = data.qyxx;
						$("#qymc${idSuffix}").html(qyxx.QYMC);
						$("#shsj${idSuffix}").html(qyxx.ZHSHSJ);
						$("#yyzz${idSuffix}").html(qyxx.YYZZ);
						$("#dqyhs${idSuffix}").html(qyxx.DQYHS);
						if (qyxx.jbfw) {
							var jbfw = qyxx.jbfw;
							$("#fwmc${idSuffix}").html(jbfw.FWMC == null ? "无" : jbfw.FWMC);
							$("#qssj${idSuffix}").html(jbfw.QYSJ == null ? "无" : jbfw.QYSJ);
							$("#jssj${idSuffix}").html(jbfw.DQSJ == null ? "无" : jbfw.DQSJ);
						} else {
							$("#fwmc${idSuffix},#qssj${idSuffix},#jssj${idSuffix}").html("无");
						}
						var xxlb = data.xxlb;
						$.each(xxlb, function (index) {
							var li = $("<li></li>");
							var number = parseInt(index)+1;
							var a = $("<a style='cursor: hand' onclick='$.ns(\"namespaceId${idSuffix}\").infoClick(\""+this.ID+"\")'>"+number+"、"+this.ZT + "-----------------------------" + this.FSSJ+"</a>")
							li.append(a);
							$("#xxlb${idSuffix} ul").append(li);
						})
						/*var userList = data.userList;
						 $.each(userList, function (index) {
						 var userDiv = $("#userList${idSuffix}");
						 userDiv.append("<div id='user${idSuffix}"+index+"'></div>");
						 var columnDiv = $("#user${idSuffix}"+index);
						 columnDiv.append("<div class='leftContentColumn'>"+this+"</div>")
						 /!*<div class="leftContentColumn">1</div>
						 <div class="contentColumn">2</div>
						 <div class="contentColumn">3</div>*!/
						 })*/
					}
				})
			}
		});
	})
</script>
