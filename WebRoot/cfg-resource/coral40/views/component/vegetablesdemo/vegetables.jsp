<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("gurl",path + "/vegetables!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=name,id");
	request.setAttribute("turl",path + "/vegetables!tree.json?E_frame_name=coral&E_model_name=tree&F_in=name,id&P_filterId=parentId");
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
	<ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
		<ces:layoutRegion region="center">
			<div class="fill">
				<div class="toolbarsnav clearfix">
					<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
						data="[{'label': '新增', 'id':'add', 'disabled': 'false','type': 'button'},{'label': '修改', 'id':'update', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delete', 'disabled': 'false','type': 'button'}]">
					</ces:toolbar>
				</div>
				<ces:grid id="gridId${idSuffix}" width="auto" height="auto" model="grid" url="${gurl}">
					<ces:gridCols>
						<ces:gridCol name="vegeName" width="180">名称</ces:gridCol>
						<ces:gridCol name="vegeType" width="180">类别</ces:gridCol>
						<ces:gridCol name="vegePrice" width="180">价格</ces:gridCol>
					</ces:gridCols>
					<ces:gridPager gridId="gridId${idSuffix}"/>
				</ces:grid>
			</div>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		asyncOnclick : function(e, treeId, treeNode) {
			$('#gridId${idSuffix}').grid('option', 'url', "${gurl}&Q_EQ_id=" + treeNode.id);
			$('#gridId${idSuffix}').grid('reload');
		},
		toolbarClick : function(event, button) {
			//alert(button.id);
			if (button.id == "add") {
				var dialogId = generateId('dialog');
				$("<div id='"+dialogId+"'></div>").appendTo(document.body);
				$('#'+dialogId).dialog({
					wtype: "dialog",
					title: "测试",
					width: 500,
					height: 400,
					modal: true,
					position: {
                        my: "center",
                        at: "center"
                    },
					beforeclose: function() {
						alert("beforeclose");
					},
					onclose: function() {
						alert("onclose");
					}
				});
				//$('#dialog${idSuffix}').dialog('open');
			} else if (button.id == "CFG_closeComponentZone") {
				if (window.CFG_clickReturnButton) {
					CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
				}
			}
		},
		toolbarClick1 : function(event, button) {
			//alert(button.id);
			if (button.id == "CFG_closeComponentDialog") {
				if (window.CFG_clickCloseButton) {
					CFG_clickCloseButton($('#max${idSuffix}').data('configInfo'));
				}
			}
		},
		/* 构件方法和回调函数 */
		getCategoryId : function() {
			alert("调用方法getCategoryId！");
			var o = {
		        status : true
		    };
		    o.categoryId = '1';
		    return o;
		},
		refreshGrid : function(cbParam1) {
			alert("调用回到函数refreshGrid(" + cbParam1 + ")");
		}
	});
	$(function() {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page' : 'page.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage' : $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone' : function() {
				return $('#layoutId${idSuffix}').layout('panel', 'center');
			},
			/** 初始化预留区 */
			'initReserveZones' : function(configInfo) {
				CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength"));
			},
			/** 获取返回按钮添加的位置 */
			'setReturnButton' : function(configInfo) {
				CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
			},
			/** 获取关闭按钮添加的位置 */
			'setCloseButton' : function(configInfo) {
				CFG_setCloseButton(configInfo, $('#toolbarId1${idSuffix}'));
			},
			/** 页面初始化的方法 */
			'bodyOnLoad' : function(configInfo) {
				alert(1);
			}
		});
		if (configInfo) {
			alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'sysParam1')
					+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1') 
					+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamId'));
		}
		configInfo.CFG_outputParams = {'success':'otp'};
	});
</script>
