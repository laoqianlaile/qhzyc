<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/4/28
  Time: 上午10:19
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
	<ces:layoutRegion split="true" minWidth="100" maxWidth="400" style="width:200px;">
		<ces:toolbar onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
		             data="[{'label': '保存并关闭', 'id':'saveAndClose', 'disabled': 'false','type': 'button'},{'label': '关闭', 'id':'close', 'disabled': 'false','type': 'button'}]">
		</ces:toolbar>
	</ces:layoutRegion>
	<ces:layout id="layout${idSuffix}" name="layout" fit="true">
		<ces:layoutRegion region="center">
			<div id="total${idSuffix}" class="fill">
				<ces:form id="zzfwForm${idSuffix}"></ces:form>
			</div>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		rowId: null,//点击列表记录的ID
		initForm: function () {
			$.get($.contextPath + "/qyptzzfw!getAllIncrementServices.json", function (data) {
				var html = "";
				for (var i in data) {
					var service = data[i];
					if (service.QYZT == '1') {//启用
						html += "<div class='app-inputdiv9' style='margin-left: 25px;'><label class='app-input-label'>" + service.FWMC + "：</label><input id='incrementServiceInput" + service.FWBH + "${idSuffix}'  name='" + service.FWBH + "' type='text' ></input><span>&nbsp;&nbsp;" + service.GGMC + "</span></div>";
					} else {
						html += "<div style='display: none'><label>" + service.FWMC + "：</label><input id='incrementServiceInput" + service.FWBH + "${idSuffix}' name='" + service.FWBH + "' type='text'></input><span>&nbsp;&nbsp;" + service.GGMC + "</span></div>";
					}
				}
				$("#zzfwForm${idSuffix}").append(html);
				$("[id^=incrementServiceInput]").textbox({
					type: "text",
					value: "0",
					width : 320,
					maxlength: 10,
					pattern:'//^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$//',
					errMsg:'请输入非负浮点数'
				});
				$.get($.contextPath + "/qyptzzfw!getIncrementServiceById.json?uuid=" + $.ns('namespaceId${idSuffix}').rowId, function (fdata) {
					for (var i in fdata) {
						var zzfwbh = fdata[i].ZZFWBH;
						var zzfwz = fdata[i].ZZFWZ;
						$("#incrementServiceInput" + zzfwbh + "${idSuffix}").textbox("setValue", zzfwz);
					}
				});
			});
		},
		formToolbarClick: function (e, ui) {
			var configInfo = $("#maxDiv${idSuffix}").data('configInfo');
			if (ui.id == "close") {//关闭
				configInfo.dialog.dialog("close");
			} else if (ui.id == "saveAndClose") {//保存并关闭
				var flag = $.ns('namespaceId${idSuffix}').f_save();
				if (flag == true) {
					configInfo.dialog.dialog("close");
				}
			}
		},
		f_save : function() {
			var valid = {flag: false};
			var flag = false;
			$("[id^=incrementServiceInput]").each(function () {
				var value = $(this).textbox("getValue");
                if (isNaN(value)||value>=0) {
                    valid.flag = true;
                }else{
                    valid.flag = false;
                }
			});
            //判断表单里的值非负
            if(!$("#zzfwForm${idSuffix}").form("valid")){
                valid.flag = false;
            };
            var services = {};
			$("[id^=incrementServiceInput]").each(function () {
				var value = $(this).textbox("getValue");
				var sid = $(this).attr("name");
				if (value != "" && value != "0") {
					services[sid] = value;
				}
			});
			if ($.isEmptyObject(services)) {
				$.message({message: "请选择需要的增值服务", cls: "warning"});
				return;
			}
            if (!valid.flag ) {
                $.message({message: "选择的增值服务值无效", cls: "warning"});
                return;
            }else{
                $.ajax({
                    type: "POST",
                    url: $.contextPath + "/qyptzzfw!setIncrementService.json",
                    async : false,
                    data: {"services": JSON.stringify(services), uuid: $.ns('namespaceId${idSuffix}').rowId},
                    success: function (data) {
                        flag = true;
                        $.message({message: "授予增值服务成功", cls: "success"});
                    },
                    error: function (data) {
                        $.message({message: "授予增值服务失败，请重试或联系技术人员", cls: "danger"});
                    }
                });
            }
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
				$.ns('namespaceId${idSuffix}').rowId = "<%=request.getParameter("paramIn1")%>";
				$.ns('namespaceId${idSuffix}').initForm();
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
