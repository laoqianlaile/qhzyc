<%--
  Created by IntelliJ IDEA.
  User: WILL
  Date: 15/7/17
  Time: 下午2:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="maxDiv${idSuffix}" class="fill">
	<div id="scpl${idSuffix}" style="margin-left: 20px; margin-top: 20px;">
		<label>蔬菜品类查询：</label>
		<ces:combobox id="scCombobox${idSuffix}" width="200" emptyText="请选择蔬菜品类" url="csgl!getAllScFoods.json" enableFilter="true"></ces:combobox>
		<ces:button label="查询" onClick="$.ns('namespaceId${idSuffix}').search"></ces:button>
	</div>
	<div id="scCharts${idSuffix}" style="width: 100%; height: auto">
	</div>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		initCharts: function (spbm) {
			var summary = $.loadJson($.contextPath + "/csgl!getSclyCharts.json",{spbm:spbm});
			if ($.isEmptyObject(summary)) {
				CFG_message("该品类没有来源信息", "warning");
			}
			$('#scCharts${idSuffix}').highcharts({
				chart: {
					type: 'pie',
					options3d: {
						enabled: true,
						alpha: 45
					}
				},
				title: {
					text: '本市蔬菜来源统计'
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
				},
				credits: {
					enabled: false
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						depth: 45,
						dataLabels: {
							enabled: true,
							format: '<b>{point.name}</b>: {point.percentage:.1f} %',
							style: {
								color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
							}
						}
					}
				},
				series: [{
					name: '蔬菜来源',
					data: summary
				}]
			});
		},
		search : function() {
			var spbm = $("#scCombobox${idSuffix}").combobox("getValue");
			$.ns('namespaceId${idSuffix}').initCharts(spbm);
		}
	});
	$(function () {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page': 'csglscly.jsp',
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
				$.ns('namespaceId${idSuffix}').initCharts();
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