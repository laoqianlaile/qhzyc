<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="maxDiv${idSuffix}" class="fill">
	<ces:layoutRegion split="true" minWidth="100" maxWidth="400" style="width:200px;">
		<ces:toolbar height="30" onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
		             data="[{'label': '保存并关闭', 'id':'saveAndClose', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'close', 'disabled': 'false','type': 'button'}]">
		</ces:toolbar>
	</ces:layoutRegion>
	<ces:layout id="layout${idSuffix}" name="layout" fit="true">
		<div id="total${idSuffix}" class="fill">
			<form id="jbfwForm${idSuffix}">
				<div class="fillwidth colspan2 clearfix">
					<div class="app-inputdiv6">
						<label class="app-input-label" for="zhbh">账户编号：</label><input type="text" name="zhbh" />
					</div>
					<div class="app-inputdiv6">
						<label class="app-input-label" for="qysj">启用时间：</label><input type="text" id="qysj${idSuffix}" name="qysj" />
					</div>
					<div class="app-inputdiv6">
						<label class="app-input-label" for="dqsj">到期时间：</label><input type="text" id="dqsj${idSuffix}" name="dqsj" />
					</div>
				</div>
			</form>
			<div id="servicesGrid${idSuffix}">
				<div class="servicesGrid${idSuffix}"></div>
			</div>
		</div>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		rowId : null,//点击列表记录的ID,
		zhbh : null,//账户编号
		init : function () {
			//初始化form
			$("#jbfwForm${idSuffix}").form({
				height : 320
			});
			$(":text[name='zhbh']").textbox({
				width : 275,
				required : true,
				readonly : true,
				value : $.ns('namespaceId${idSuffix}').zhbh
			});
			$(":text[name='qysj']").datepicker({
				width : 275,
				dateFormat : "yyyy-MM-dd"
			});
			$(":text[name='dqsj']").datepicker({
				width : 275,
				dateFormat : "yyyy-MM-dd",
				startDateId:"qysj${idSuffix}"
			});
			//初始化grid
			var $grid = $("#servicesGrid${idSuffix}");
			$grid.grid({
				width: "auto",
				height: "auto",
				fitStyle: "fill",
				datatype: "local",
//				multiselect : true,
				//contentType:"application/json",
				colModel: [
					{name:"FWBH",hidden:true,key:true},
					{name:"FWMC",width:80},
					{name:"FWSL",width:60,formatter:"text"}
				],
				//asyncType:"GET",
				colNames: ["服务编号","服务名称","服务数量"]
			});
			//企业已分配的基本服务
			var orgServices = $.loadJson($.contextPath + "/qyptjbfw!getBaseServicesByOrgId.json?orgId=" + $.ns('namespaceId${idSuffix}').zhbh);
			//启用时间和到期时间
			var qysj = "";
			var dqsj = "";
			for (var i in orgServices) {
				var orgService = orgServices[i];
				$grid.grid("addRowData",orgService.ID,{
					FWBH : orgService.FWBH,
					FWMC : orgService.FWMC,
					FWSL : orgService.FWSL
				},"last");
				if (orgService.QYSJ != null && orgService.QYSJ != "") {
					qysj = orgService.QYSJ;
				}
				if (orgService.DQSJ != null && orgService.DQSJ != "") {
					dqsj = orgService.DQSJ;
				}
			}
			if (qysj != null && qysj != "") {
				$("#qysj${idSuffix}").datepicker("setDate", qysj);
			}
			if (dqsj != null && dqsj != "") {
				$("#dqsj${idSuffix}").datepicker("setDate", dqsj)
			}
		},
		formToolbarClick: function (e, ui) {
			var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
			if (ui.id == "close") {//关闭
				configInfo.dialog.dialog("close");
			} else if (ui.id == "saveAndClose") {//保存并关闭
				var flag = $.ns('namespaceId${idSuffix}').f_save();
				if (flag) {
					configInfo.dialog.dialog("close");
				}
			}
		},
		//基本服务保存
		f_save : function() {
			var qysj = $("#qysj${idSuffix}").datepicker("getDate");
			var qysjValue = $("#qysj${idSuffix}").datepicker("getValue");
			if (qysjValue == "") {
				CFG_message("请选择服务启用时间", "warning");
				return false;
			}
			var dqsj = $("#dqsj${idSuffix}").datepicker("getDate");
			var dqsjValue = $("#dqsj${idSuffix}").datepicker("getValue");
			if (dqsjValue == "") {
				CFG_message("请选择服务到期时间", "warning");
				return false;
			}
			if (dqsj < qysj) {
				CFG_message("到期时间不能小于启用时间", "warning");
				return false;
			}
			var $grid = $("#servicesGrid${idSuffix}");
			var ids = $grid.grid("getDataIDs");
			var services = [];
			for (var i in ids) {
				var rowData = $grid.grid("getRowData", ids[i]);
				if (!isNaN(rowData.FWSL) && parseInt(rowData.FWSL) > 0) {
					services.push({
						zhbh : $.ns('namespaceId${idSuffix}').zhbh,
						fwbh : rowData.FWBH,
						fwsl : rowData.FWSL,
						qysj : qysjValue,
						dqsj : dqsjValue
					});
				}
			}
			var flag = true;
			if ($.isEmptyObject(services)) {
				CFG_message("请选择服务内容", "warning");
				return false;
			}
			$.ajax({
				type: "POST",
				url: $.contextPath + "/qyptjbfw!setBaseService.json",
				async : false,
				data: {"services": JSON.stringify(services),"zhbh" : $.ns('namespaceId${idSuffix}').zhbh},
				success: function (data) {
					CFG_message("授予基本服务成功", "success");
				},
				error: function (data) {
					flag = false;
					CFG_message("授予基本服务失败，请重试或联系技术人员", "danger");
				}
			});
			return flag;
		}
	});
	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'qyptjbfwxxlbs.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage': $('#maxDiv${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone': function () {
				return $('#layout${idSuffix}').layout('panel', 'center');
				//return $("#layout${idSuffix}");
			},
			/** 初始化预留区 */
			'initReserveZones': function (configInfo) {
				CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);
			},
			/** 获取返回按钮添加的位置 */
			'setReturnButton': function (configInfo) {
				CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
			},
			/** 页面初始化的方法 */
			'bodyOnLoad': function (configInfo) {
				$.ns('namespaceId${idSuffix}').rowId = "<%=request.getParameter("paramIn1")%>";
				//获取账户编号
				var zhbh = $.loadJson($.contextPath + "/qyptzhgl!getZhbhByUUID.json?uuid=" + $.ns('namespaceId${idSuffix}').rowId);
				$.ns('namespaceId${idSuffix}').zhbh = zhbh;
				$.ns('namespaceId${idSuffix}').init();
			}
		});
		if (configInfo) {
			//alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
			//		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
			//		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
		}
		// 设置输出参数
		// configInfo.CFG_outputParams.xxx = '';
	});
</script>