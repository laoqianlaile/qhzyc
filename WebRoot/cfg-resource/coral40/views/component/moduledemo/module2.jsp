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
			<ces:tabs id="tabbarDiv${idSuffix}" cls="tabs-top" heightStyle="fill" onTabClose="tabClose"><ul></ul></ces:tabs>
		</ces:layoutRegion>
	</ces:layout>
</div>
<script type="text/javascript">
	$.extend($.ns("namespaceId${idSuffix}"), {
		currentTreeNodeId : '',
		asyncOnclick : function(e, treeId, treeNode) {
			CFG_clearComponentZone($('#max${idSuffix}').data('configInfo'));
			CFG_addTabbar($('#max${idSuffix}').data('configInfo'), "TabReserve", $('#tabbarDiv${idSuffix}'), '4', $.ns("namespaceId${idSuffix}"));
			/*if (treeNode.name == "TreeNodeReserve") {
				alert(1);
				CFG_clickButtonOrTreeNode($('#max${idSuffix}').data('configInfo'), "TreeNodeReserve", "树节点预留区", "3", $.ns("namespaceId${idSuffix}"));
				return;
			}
			alert($.ns("namespaceId${idSuffix}").currentTreeNodeId);
			$.ns("namespaceId${idSuffix}").currentTreeNodeId = treeNode.id;
			$('#gridId${idSuffix}').grid('option', 'url', "${gurl}&Q_EQ_parentId=" + treeNode.id);
			$('#gridId${idSuffix}').grid('reload');
			*/
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
		refreshGrid : function(o) {
			if (o) {
				alert("调用回到函数refreshGrid(" + o.cbParam1 + ")");
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
				CFG_addTabbar(configInfo, "TabReserve", $('#tabbarDiv${idSuffix}'), '4', $.ns("namespaceId${idSuffix}"));
			}
		});
		var systemParam1 = CFG_getSystemParamValue(configInfo, 'systemParam1'); // 获取构件系统参数
		var selfParam1 = CFG_getSelfParamValue(configInfo, 'selfParam1'); // 获取构件自身参数
		var inputParam1 = CFG_getInputParamValue(configInfo, 'inputParamName_1'); // 获取构件输入参数
		configInfo.CFG_outputParams.xxx = ''; // 设置输出参数
	});
</script>
