<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	request.setAttribute("gurl",path + "/module-category!search.json?E_frame_name=coral&E_model_name=jqgrid&F_in=name,id");
	request.setAttribute("turl",path + "/module-category!tree.json?E_frame_name=coral&E_model_name=tree&F_in=name,id&P_filterId=parentId");
	request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<div id="max${idSuffix}" class="fill">
	<ces:layout id="layoutId${idSuffix}" name="" style="width:800px;height:600px;" fit="true">
		<ces:layoutRegion region="west" split="true" minWidth="300" maxWidth="400" style="width:150px;padding:10px;">
			<ces:tree id="treeId${idSuffix}"  asyncEnable="true" asyncType="post" data="[{ name:\"分类管理\",id:\"-1\",isParent:true}]" asyncUrl="${turl}" asyncAutoParam="id,name" onClick="jQuery.ns('namespaceId${idSuffix}').asyncOnclick" onLoad="asyncExpandnode">  
			</ces:tree>
		</ces:layoutRegion>
		<ces:layoutRegion region="center">
			<div class="fill">
				<div class="toolbarsnav clearfix">
					<ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick"
						data="[{'label': '新增', 'id':'add', 'disabled': 'false','type': 'button'},{'label': '修改', 'id':'update', 'disabled': 'false','type': 'button'},{'label': '删除', 'id':'delete', 'disabled': 'false','type': 'button'},{'label': '选中记录', 'id':'selRecs', 'disabled': 'false','type': 'button'},{'label': '按钮预留区', 'id':'buttonReserve', 'disabled': 'false','type': 'button'},{'label': '更多', 'id':'more', 'disabled': 'false','type': 'splitbutton'}]">
					</ces:toolbar>
				</div>
				<!-- <button class="coral-button coral-component coral-state-default coral-corner-all coral-button-text-only" onClick="alert(1)"><span class="coral-button-text">保存</span></button> -->
				<ces:grid id="gridId${idSuffix}" multiselect="true" rownumbers="false" shrinkToFit="true" forceFit="true" fitStyle="fill" model="grid" url="${gurl}">
					<ces:gridCols>
						<ces:gridCol name="id" edittype="text" width="100">id</ces:gridCol>
						<ces:gridCol name="name" width="180" formatter="$.ns('namespaceId${idSuffix}').formatName">名称</ces:gridCol>
					</ces:gridCols>
					<ces:gridPager gridId="gridId${idSuffix}"/>
				</ces:grid>
				<div id="hiddenDiv${idSuffix}" style="display: none;">
					<div id="formDiv${idSuffix}">
						<div class="toolbarsnav clearfix">
						    <ces:toolbar id="formToolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').formToolbarClick"
								data="[{'label': '保存', 'id':'save', 'disabled': 'false','type': 'button'},{'label': '返回', 'id':'CFG_closeComponentZone', 'disabled': 'false','type': 'button'}]">
							</ces:toolbar>
						</div>
					    <ces:form id="form${idSuffix}" name="form" action="">
							<input id="id${idSuffix}" name="id" type="hidden" />
							<input id="categoryId${idSuffix}" name="categoryId" type="hidden"/>
							<div>
								<table>
								<tr>
									<td width="100" align="right"><label>名称:</label></td>
									<td><ces:input id="name${idSuffix}" name="name" required="true" width="200"/></td>
								</tr>
								<br/>
								<tr>
									<td width="100" align="right"><label>值:</label></td>
									<td><ces:input id="value${idSuffix}" name="value" required="true" width="200"/></td>
								</tr>
								<br/>
								<tr>
									<td width="100" align="right"><label>参数说明：</label></td>
									<td><ces:textarea id="remark${idSuffix}" name="remark" required="true" width="200" ></ces:textarea></td>
								</tr>
								</table>
							</div>
						</ces:form>
					</div>
				</div>
			</div>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
function buttonsave() {
    $.alert("save");
}
	$.extend($.ns("namespaceId${idSuffix}"), {
		currentTreeNodeId : '',
		asyncOnclick : function(e, treeId, treeNode) {
			if (treeNode.name == "TreeNodeReserve") {
				alert(1);
				CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
				return;
			}
			if (treeNode.name == "TreeNodeReserve2") {
				alert(1);
				CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve2", "树节点预留区2", "3", $.ns("namespaceId${idSuffix}"));
				return;
			}
			CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
			
			alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
			$.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
			$('#gridId${idSuffix}').grid('option', 'url', "${gurl}&Q_EQ_parentId=" + treeNode.id);
			$('#gridId${idSuffix}').grid('reload');
			
		},
		toolbarClick : function(event, button) {
			//alert(button.id);
			if (button.id == "add") {
				var dialogId = generateId('dialog');
				$("<div id='"+dialogId+"'></div>").appendTo(document.body);
				$('#'+dialogId).dialog({
					closeText: "关闭",
					appendTo : $("#layoutId${idSuffix}"),
					wtype: "dialog",
					title: "测试",
					width: 500,
					height: 400,
					modal: true,
					position: {
						my: "center",
						at: "center",
						of: $("#layoutId${idSuffix}"),
						collision: "fit",
						// 确保标题栏始终可见
						using: function( pos ) {
							var topOffset = $( this ).css( pos ).offset().top;
							if ( topOffset < 0 ) {
								$( this ).css( "top", pos.top - topOffset );
							}
						}
					},
					onOpen: function() {
					    $('#'+dialogId).append($('#formDiv${idSuffix}'));
	                },
					beforeclose: function() {
						alert("beforeclose");
					},
					onClose: function() {
						alert("onClose");
						$('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
						$('#'+dialogId).remove();
					}
				});
				//$('#dialog${idSuffix}').dialog('open');
			} else if (button.id == "update") {
				var div = CFG_getEmbeddedDiv($('#max${idSuffix}').data('configInfo'), "", "修改", function() {
					$('#hiddenDiv${idSuffix}').append($('#formDiv${idSuffix}'));
				});
				div.append($('#formDiv${idSuffix}'));
			} else if (button.id == "delete") {
			} else if (button.id == "selRecs") {
				alert($('#gridId${idSuffix}').grid("option", "selarrrow"));
			} else if (button.id == "CFG_closeComponentZone") {
				if (window.CFG_clickReturnButton) {
					CFG_clickReturnButton($('#max${idSuffix}').parent().data('parentConfigInfo'));
				}
			} else if (button.id == "buttonReserve") {
				CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "buttonReserve", "按钮预留区测试", 2, $.ns("namespaceId${idSuffix}"));
			} else {
				CFG_clickToolbar($('#max${idSuffix}').data('configInfo'), 'toolBarReserve', button.id, 0, $.ns("namespaceId${idSuffix}"));
			}
		},
		formatName : function() {
			return "<a href='#'>aaaa</a>";
		},
		formToolbarClick : function(event, button) {
			//alert(button.id);
			if (button.id == "save") {
				alert('保存');
				//$('#dialog${idSuffix}').dialog('open');
			} else if (button.id == "CFG_closeComponentZone") {
				if (window.CFG_clickReturnButton) {
					CFG_clickReturnButton($('#max${idSuffix}').data('configInfo'));
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
		transferTo : function() {
		    var o = {
		        status : true
		    };
		    o.condition = '2';
		    o.params = 'inputParamId≡111;inputParamId1≡112';
		    return o;
		},
		refreshGrid : function(o) {
			if (o) {
				alert("调用回到函数refreshGrid(" + o.cbParam1 + ")");
			} else {
				alert("调用回到函数refreshGrid");
			}
		}
	});
	$(function() {
		var configInfo = CFG_initConfigInfo({
			/** 页面名称 */
			'page' : 'module.jsp',
			/** 页面中的最大元素 */
			'maxEleInPage' : $('#max${idSuffix}'),
			/** 获取构件嵌入的区域 */
			'getEmbeddedZone' : function() {
				return $('#layoutId${idSuffix}').layout('panel', 'center');
			},
			/** 初始化预留区 */
			'initReserveZones' : function(configInfo) {
				CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength")-1);
			},
			/** 获取返回按钮添加的位置 */
			'setReturnButton' : function(configInfo) {
				CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
			},
			/** 页面初始化的方法 */
			'bodyOnLoad' : function(configInfo) {
				//alert("bodyOnLoad");
				// 按钮权限控制
				//alert(configInfo.notAuthorityComponentButtons);
				if (configInfo.notAuthorityComponentButtons) {
					$.each(configInfo.notAuthorityComponentButtons, function(i, v){
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
				}
			}
		});
		var systemParam1 = CFG_getSystemParamValue(configInfo, 'systemParam1'); // 获取构件系统参数
		var selfParam1 = CFG_getSelfParamValue(configInfo, 'selfParam1'); // 获取构件自身参数
		var inputParam1 = CFG_getInputParamValue(configInfo, 'inputParamName_1'); // 获取构件输入参数
		//alert(inputParam1);
		configInfo.CFG_outputParams.xxx = ''; // 设置输出参数
		alert(configInfo.assembleType);
	});
</script>
