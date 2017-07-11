<%@ page language="java" import="com.ces.config.utils.CommonUtil" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>

<style type="text/css">
	.fwLi {
		margin-top: 5px;
		font-size: 16px;
	}
</style>

<ces:tabs id="baseFw" heightStyle="fill">
	<ul>
		<li><a href="#fragment-1">基本服务</a></li>
		<li><a href="#fragment-2">增值服务</a></li>
	</ul>
	<div id="fragment-1">
		<div id="menu1" style="margin-top:10px;">
			<div id="btns1" style="display: inline;margin-left:10px;">
				<ces:button id="changeBtn" label="变更服务"></ces:button>
				<ces:button id="conBtn" label="续订服务"></ces:button>
			</div>
			<div id="aUrl1" style="display: inline;float: right;cursor: pointer;">
				<%--<a url="#" style="color:blue;margin-right:10px;">基本服务介绍></a>--%>
			</div>
		</div>
		<div id="con" style="width: 100%;margin-top:100px;">
			<div id="fw" style="width:30%; margin:0 auto;">
				<ul>
					<li class="fwLi" id="fwmc${idSuffix}"></li>
					<li class="fwLi" id="fwnr${idSuffix}"></li>
					<li class="fwLi" id="qysj${idSuffix}"></li>
					<li class="fwLi" id="dqsj${idSuffix}"></li>
				</ul>
			</div>
		</div>
	</div>
	<div id="fragment-2">
		<div id="menu2" style="margin-top:10px;">
			<div id="btns2" style="display: inline;">
				<ces:button id="applyBtn" label="申请服务"></ces:button>
			</div>
			<div id="aUrl2" style="display: inline;float: right;cursor: pointer;">
				<%--<a url="#" style="color:blue;margin-right:10px;">增值服务介绍></a>--%>
			</div>
		</div>
		<div id="gridDemo1">
			<div class="gridDemo1"></div>
		</div>
	</div>
</ces:tabs>
<div id="dialog1"></div>
<div id="dialog2"></div>
<div id="dialog3"></div>
</body>
<script type="text/javascript">
	$(function () {
		var $grid = $("#gridDemo1"),
				_colModel = [
					{name: "ID", sortable: true, width: 100, hidden: true},
					{name: "FWMC", sortable: true, width: 100, align: "center"},
					{name: "FWNR", sortable: true, width: 100, align: "center"},
					{name: "GG", sortable: true, width: 100, align: "center"},
					{name: "ZZFWZ", sortable: true, width: 100, align: "center"},
					{name: "QYSJ", sortable: true, width: 80, align: "center", hidden:true},
					{name: "DQSJ", sortable: true, width: 80, align: "center", hidden:true}
				],
				_colNames = ["ID", "服务名称", "服务内容", "规格", "数量", "启用时间", "到期时间"],
				_setting = {
					width: "auto",
					height: "auto",
					fitStyle: "fill",
					colModel: _colModel,
					colNames: _colNames,
					url: $.contextPath + "/qyptqtfw!getZzfwList.json"
				};
		$grid.grid(_setting);
		//获取企业基本服务信息
		$.ajax({
			type: "post",
			url: $.contextPath + "/qyptqtfw!getBasicService.json",
			dataType: "json",
			success: function (data) {
				$("#fwmc${idSuffix}").html("<a style='font-weight:bold'>服务名称：</a>" + (data.FWMC == null ? "无" : data.FWMC));
				$("#fwnr${idSuffix}").html("<a style='font-weight:bold'>服务内容：</a>" + (data.FWNR == null ? "无" : data.FWNR));
				$("#qysj${idSuffix}").html("<a style='font-weight:bold'>启用时间：</a>" + (data.QYSJ == null ? "无" : data.QYSJ));
				$("#dqsj${idSuffix}").html("<a style='font-weight:bold'>到期时间：</a>" + (data.DQSJ == null ? "无" : data.DQSJ));
			}
		});
		$('#dialog1').dialog({
			width: '60%',
			height: 400,
			modal: true,
			title: '变更服务',
			autoOpen: false,
			reloadOnOpen: true,
			url: '<%=path%>/cfg-resource/coral40/views/component/qyptqtfw/bgfw.jsp',
			onOpen: function () {
				$("#bgxq").val("");
			}
		});
		$('#dialog2').dialog({
			width: '60%',
			height: 500,
			modal: true,
			title: '续订服务',
			autoOpen: false,
			reloadOnOpen: true,
			url: '<%=path%>/cfg-resource/coral40/views/component/qyptqtfw/xdfw.jsp',
			onOpen: function () {
				$("#xdsj").combobox("setValue", "");
				$("#xdxq").val("");
			}
		});
		$('#dialog3').dialog({
			width: '60%',
			height: 500,
			modal: true,
			title: '申请服务',
			autoOpen: false,
			reloadOnOpen: true,
			url: '<%=path%>/cfg-resource/coral40/views/component/qyptqtfw/sqfw.jsp',
			onOpen: function () {
				$("#zzfwmc").combobox("setValue", "");
				$("#xqsm").val("");
			}
		});
	});

	$("#changeBtn").click(function () {
		$('#dialog1').dialog("open");
	});
	$("#conBtn").click(function () {
		$('#dialog2').dialog("open");
	});
	$("#applyBtn").click(function () {
		$('#dialog3').dialog("open");
	});
</script>