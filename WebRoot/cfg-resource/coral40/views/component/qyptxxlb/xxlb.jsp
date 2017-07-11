<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/4/28
  Time: 下午2:53
  To change this template use File | Settings | File Templates.
--%>
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
	<ces:layout id="layout${idSuffix}" name="layout" fit="true" style="float:left">
		<ces:layoutRegion region="north" split="true" >

			<ces:toolbar onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="center" 
			             data="[{'label': '发送', 'id':'send', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'close', 'disabled': 'false','type': 'button'}]">
			</ces:toolbar>
			<div id="total${idSuffix}" class="fill" >
				<ces:combobox id="xxlb${idSuffix}" emptyText="请选择消息" enableFilter="true" width="800"></ces:combobox>
				
			</div>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		rowIds: null,//点击记录的ID
		initCombobox: function () {
			$.get($.contextPath + "/qyptxxsz!getAllMsgs.json", function(data) {
				$("#xxlb${idSuffix}").combobox("reload",data);
			});
		},
		toolbarClick: function (e, ui) {
			var msgId = $("#xxlb${idSuffix}").combobox("getValue");
			if (msgId == "") {
				CFG_message("请先选择需要发送的消息","danger");
				return;
			}
			var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
			if (ui.id == "close") {//关闭
				configInfo.dialog.dialog("close");
			} else if (ui.id == "send") {//发送消息
				var flag = $.ns('namespaceId${idSuffix}').f_send(msgId);
				if (flag == true) {
					configInfo.dialog.dialog("close");
				}
			}
		},
		f_send : function(msgId) {
			var flag = false;
			var jsonData= $.loadJson($.contextPath + "/qyptxxsz!checkYxId.json?ids="+$.ns('namespaceId${idSuffix}').rowIds);
			var flagYx=true;

			for(var i=0;i<jsonData.length;i++){
				if(jsonData[i].YX==null&&jsonData[i].SJ==null){
					flagYx=false;
				}
			}
			if(!flagYx){
				CFG_message("请输入邮箱或手机号码!","danger");
				return;
			}
			$.ajax({
				url : $.contextPath + "/qyptxxsz!sendMsg.json",
				type : "POST",
				async : false,
				data: {"rowIds": $.ns('namespaceId${idSuffix}').rowIds, msgId: msgId},
				success: function (data) {
					flag = true;
					CFG_message("发送成功","success");
				},
				error: function (data) {
					CFG_message("发送失败，请重试","danger");
				}
			});
			return flag;
		}
	});
	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'qyptzzfwxx.jsp',
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
				$.ns('namespaceId${idSuffix}').rowIds = "<%=request.getParameter("paramIn1")%>";
				$.ns('namespaceId${idSuffix}').initCombobox();
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
