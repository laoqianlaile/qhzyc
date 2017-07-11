<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Coflow3.0流程设计器</title>
    <script type="text/javascript">
    	var basePath = '${ctx}';
    	var versionId = "${param.versionId}";
    	var version =  "${param.version}";
    </script>
    <script src="${ctx}/coflow/js/jquery-easyui-1.3.2/jquery-1.8.0.min.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/jquery-easyui-1.3.2/jquery.easyui.min.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/jquery.xslt.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/custom.prototype.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/selectbox.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/raphael.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/js/raphaelUtil.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/xmlUtil.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/ztreeUtil.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/toolUtil.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/selectUtil.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/dataView.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/jquery.selectbox.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/jquery.participantbox.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/jquery.actualParambox.js" type="text/javascript"></script>
    <script src="${ctx}/coflow/js/easyui.custom.extend.js" type="text/javascript"> </script>
    <script src="${ctx}/coflow/zTree-v3.5.15/js/jquery.ztree.all-3.5.js"></script>
    <script src="${ctx}/coflow/js/Math.uuid.js"></script>
	<link rel='stylesheet' type='text/css' href="${ctx}/coflow/zTree-v3.5.15/css/zTreeStyle/zTreeStyle.css"  />
    <link rel="stylesheet" type="text/css" href="${ctx}/coflow/js/jquery-easyui-1.3.2/themes/default/easyui.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/coflow/js/jquery-easyui-1.3.2/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/coflow/css/easyui.css" />
</head>
<style>
.paper {
	background-image: url("${ctx}/coflow/img/bg.png");
	/*margin-top: 56px;*/
	width:100%;
	height:3000px;
}
.toolbar{
	/*background-color: gray;*/
	/*position: fixed;*/
	/*z-index: 111;*/
	width: 100%;
	display: inline-block;
	background: none repeat scroll 0 0 #f8f8f8;
}
.panel-body {
	overflow-y: hidden; 
}
.node,.other{
    float: left;
    height: 28px;
    margin-left: 0px;
}
.f_table{
	width:100%;
	height:100%;
}
.f_disable{
	background: none repeat scroll 0 0 #EEEEEE;
}
.f_box{
	border: 1px solid #ccc;
}
.btn {
    -moz-border-bottom-colors: none;
    -moz-border-left-colors: none;
    -moz-border-right-colors: none;
    -moz-border-top-colors: none;
    background-color: #f5f5f5;
    background-image: linear-gradient(to bottom, #fff, #e6e6e6);
    background-repeat: repeat-x;
    border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) #b3b3b3;
    border-image: none;
    border-style: solid;
    border-width: 1px;
    box-shadow: 0 1px 0 rgba(255, 255, 255, 0.2) inset, 0 1px 2px rgba(0, 0, 0, 0.05);
    cursor: pointer;
    display: inline-block;
    line-height: 24px;
    margin-bottom: 0;
    text-align: center;
    text-shadow: 0 1px 1px rgba(255, 255, 255, 0.75);
    vertical-align: middle;
}
</style>
<script>
	$(function(){
		var projectViewSetting = {
			view:{
				showIcon:true,
				selectedMulti: false,
				open:true
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				//onDblClick:projectViewOnDbclick
			}
		};
		var projectNodes = [];
		/*$.fn.zTree.init($("#projectView"), projectViewSetting, projectNodes);
		$.ajax({
			  type: "POST",
			  url: "${ctx}/action/work-flow-manager!loadAllFlow",
			  success: function(msg){
			    var zTree = $.fn.zTree.getZTreeObj("projectView");
				zTree.addNodes(null,eval(msg));
				zTree.expandAll(true);
			  }
		});//*/
		
		$('#process_tabs').tabs({
			  onSelect:function(title,index){
				  var tab = $('#process_tabs').tabs("getSelected");
				  $(tab).trigger("data"); 
			  },
			  onBeforeClose: function(title,index){
				  if(isForceCloseTab){
					  var tabs = $('#process_tabs').tabs("tabs");
					  if(tabs.length == 1){
						  $.fn.zTree.destroy("packageView");
					  }
					  return;
				  }
				  var tab = $('#process_tabs').tabs("getTab",index);
				  var backupXmlDoc = $(tab).data("backup");
				  $(tab).trigger("updateCurrentXmlDoc");
				  var currentXmlDoc = $(tab).data("currentXmlDoc");
				  if(backupXmlDoc != currentXmlDoc){
					  if(confirm("需要保存后再关闭吗？")){
						  saveXmlDocToServer(tab,true);
					  }
				  }
				  var tabs = $('#process_tabs').tabs("tabs");
				  if(tabs.length == 1){
					  $.fn.zTree.destroy("packageView");
				  }
				  return true;
			  }
			});
		
		$(document).unbind("keyup mouseup").bind("keyup mouseup",function(){
			var tab = $('#process_tabs').tabs("getSelected");
			autoCheckChange(tab);
		});
		$("input[name=gs_operator]").click(function(){
			$("#orgViewDiv").css("display","none");
			$("#orgViewDiv2").css("display","none");
			var id = $(this).attr("id");
			if(id == "gs_department"){
				$("#orgViewDiv").css("display","block");
			}else if(id == "gs_human"){
				$("#orgViewDiv2").css("display","block");
			}
		});
		
		
		//组织用户异步树
		var orgSetting = {
				view: {
					selectedMulti: false,
					showIcon:false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				async: {
					enable: true,
					 url: pwin.AppActionURI.workflowVersion + "!parseOrganization.json",
					autoParam:["id"]
				}
			};
		var userSetting = {
				view: {
					selectedMulti: false,
					showIcon:false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				async: {
					enable: true,
					 url: pwin.AppActionURI.workflowVersion + "!parseUser.json",
					autoParam:["id"]
				}
			};
		$.fn.zTree.init($("#orgView"), orgSetting);
		$.fn.zTree.init($("#userView"), userSetting);
		
		openXmlDoc(versionId,version);
		
	});

</script>
<body class="easyui-layout" style="width:100%;height:100%;">
	<!-- <div data-options="region:'west',split:true" title="工程" style="width:240px;">
		<div data-options="region:'center',border:false,split:true">
			<div id="projectView" class="ztree"></div>
		</div>
	</div> -->
	<div data-options="region:'center'">
		<div class="easyui-layout" data-options="fit:true">
			<div data-options="region:'center',border:false">
				<div id="process_tabs" class="easyui-tabs" data-options="fit:true"></div>
			</div>
			<div data-options="region:'south',border:false,split:true" style="height:260px" title="包和属性">
				<div class="easyui-layout" data-options="fit:true">
					<div data-options="region:'west',border:false,split:true" style="width:180px" title="包">
						<div id="packageView" class="ztree"></div>
					</div>
					<div data-options="region:'center',border:false,split:true"  title="属性">
						<div class="easyui-tabs" data-options="fit:true">
							<div title="基本属性">
								<div id="basicPropsContainer">
									 <table id="basicProps" class="easyui-propertygrid" data-options="columns: [[{field:'name',title:'属性',width:60},
											{field:'value',title:'值',width:200}]]">
									</table>
								</div>
							</div>
							<div title="形参">
								<div id="formalParamContainer" style="display:none">
									<table id="formalParam" class="easyui-datagrid" data-options="toolbar:'#formalParamBar'">
									</table>
									<div id="formalParamBar">
										<a href="javascript:void(0)" id="formalBtn_add" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"></a>
										<a href="javascript:void(0)" id="formalBtn_del" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'"></a>
									</div>
								</div>
							</div>
							<div title="相关数据">
								<div id="dataFieldContainer" style="display:none">
									<table id="dataField" class="easyui-datagrid" data-options="toolbar:'#dataFieldBar'">
									</table>
									<div id="dataFieldBar">
										<a href="javascript:void(0)" id="dataFieldBtn_add" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"></a>
										<a href="javascript:void(0)" id="dataFieldBtn_del" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'"></a>
										<a href="javascript:void(0)" id="dataFieldBtn_import" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-import'">导入</a>
									</div>
								</div>
							</div>
							<div title="实现工具">
								<div id="implToolContainer" style="display:none">
									<table id="implTool" class="easyui-datagrid" data-options="toolbar:'#implToolBar'" style="display:none">
									</table>
									<div id="implToolBar">
										<a href="javascript:void(0)" id="implToolBtn_add" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'"></a>
										<a href="javascript:void(0)" id="implToolBtn_del" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'"></a>
									</div>
								</div>
							</div>
							<div title="实现子流程" >
								<div id="implSubflowContainer" style="display:none">
									<table id="implSubflow" class="easyui-datagrid">
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="dlg" class="easyui-dialog" title="XML" data-options="closed:true" style="width:600px;height:400px;padding:0px;overflow:hidden">
		<textarea id="dlg_xmlContent" readonly="readonly" style="border:none;padding:0px;margin:0px;width:100%;height:100%;font-size:12px"></textarea>
	</div>
	<div id="dlg_import_dataField" class="easyui-dialog" title="导入相关数据" data-options="closed:true" style="width:600px;height:400px;padding:0px;overflow:auto">
		<table id="dataField_preview" class="easyui-datagrid"></table>
	</div>
	<div id="dlg_select" style="display:none">
		<form name=frm1>
			<table width="100%" border="0" align="left" cellpadding="1" cellspacing="1" class="docBoxNoPanel">
				<tr>
					<td width="170px" align="center" id="SrcSelect">
						<table  width="100%">
							<tr><td align="left" id="srcSelectTitle">可选参数列表:</td></tr>
							<tr>
								<td align="left">
									<select name="SrcSelect" size=6 style="height:150px;width:100%" multiple ondblclick="moveLeftOrRight(document.frm1.SrcSelect,document.frm1.ObjSelect)"></select>
								</td>
							</tr>
						</table>
			    	</td>
			    	<td width="30px" align="left">
			        	<input align="left" type=button style="width:30px" value=">>" title="全部右移" onclick="moveLeftOrRightAll(document.frm1.SrcSelect,document.frm1.ObjSelect)" ><br>
			        	<input align="left" type=button style="width:30px" value=">"  title="右移" onclick="moveLeftOrRight(document.frm1.SrcSelect,document.frm1.ObjSelect)" ><br>
			        	<input align="left" type=button style="width:30px" value="<" title="左移" onclick="moveLeftOrRight(document.frm1.ObjSelect,document.frm1.SrcSelect)" ><br>
			        	<input align="left" type=button style="width:30px" value="<<" title="全部左移" onclick="moveLeftOrRightAll(document.frm1.ObjSelect,document.frm1.SrcSelect)" >
			    	</td>
			    	<td width="170px" align="center" id="ObjSelect">
			    		<table width="100%">
			    			<tr>
			    				<td align="left" id="objSelectTitle">已选参数列表:</td>
			    			</tr>
			    			<tr>
			    				<td align="left">
			    					<select name="ObjSelect" size=6 style="height:150px;width:100%" multiple ondblclick="moveLeftOrRight(document.frm1.ObjSelect,document.frm1.SrcSelect)"></select>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
			    	<td width="30px" align="left">
			        	<input type=button style="width:30px" value="︽" title="移到最顶" onclick="moveToTop(document.frm1.ObjSelect)" ><br>
			        	<input type=button style="width:30px" value="︿" title="上移" onclick="moveUp(document.frm1.ObjSelect)" ><br>
			        	<input type=button style="width:30px" value="﹀" title="下移" onclick="moveDown(document.frm1.ObjSelect)" ><br>
			        	<input type=button style="width:30px" value="︾" title="移到最底" onclick="moveToBottom(document.frm1.ObjSelect)" ><br>
			    	</td>
			    	<td width="200px" align="center" id="formalParamSelect">
			    		<table width="100%">
			    			<tr>
			    				<td align="left">
			    					对应形参列表:
			    				</td>
			    			</tr>
			    			<tr>
			    				<td align="left">
			    					<select name="formalParamSelect" size=6 style="height:150px;width:100%" multiple"></select>
			    				</td>
			    			</tr>
			    		</table>
			    	</td>
				</tr>
			</table>
		</form>
	</div>
	<div id="dailog_participant" title="参与者" style="height:325px;display:none;">
		<div style="height:180px;float:left">
			<table class="f_table">
				<tr>
					<td><input name="gs_operator" id="gs_department" type="radio"/><label for="gs_department">指定部门</label></td>
				</tr>
				<tr>
					<td><input name="gs_operator" id="gs_human" type="radio"/><label for="gs_human">指定用户</label></td>
				</tr>
				<tr>
					<td><input name="gs_operator" id="gs_creater" type="radio"/><label for="gs_creater">创建者</label></td>
				</tr>
				<!-- <tr>
					<td><input name="gs_operator" id="gs_creater_leader" type="radio"/><label for="gs_creater_leader">创建者领导</label></td>
				</tr> -->
				<tr>
					<td><input name="gs_operator" id="gs_pre_operator" type="radio"/><label for="gs_pre_operator">上一节点操作者</label></td>
				</tr>
				<!-- <tr>
					<td><input name="gs_operator" id="gs_pre_operator_leader" type="radio"/><label for="gs_pre_operator_leader">上一节点操作者领导</label></td>
				</tr> -->
				<tr>
					<td><input name="gs_operator" id="gs_same_node_operator" type="radio"/><label for="gs_same_node_operator">与节点相同的操作者</label><select id="creator_node" style="width:120px;"></select></td>
				</tr>
				<!-- <tr>
					<td><input name="gs_operator" id="gs_custom_class" type="radio"/><label for="gs_custom_class">自定义处理类</label><br/>&nbsp;&nbsp;&nbsp;&nbsp;<input id="customClass" type="text" style="max-width:100px;"/><select id="creator_node2" style="width:120px;"></select></td>
				</tr> -->
				<tr>
					<td>
						<table id="custom_gs_table" align="left" cellpadding="2" cellspacing="0"></table>
					</td>
				</tr>
			</table>
		</div>
		<div id="orgViewDiv" style="display:block;float:left;background: #EBF0A8">
			<table class="f_table">
				<tr height="310px"><td>
					<div class="f_box" style="height:310px;width:240px;overflow:auto">
						<div id="orgView" class="ztree"></div>
					</div>
				</td></tr>
			</table>
		</div>
		<div id="orgViewDiv2" style="display:none;float:left;background: #EBF0A8">
			<table class="f_table">
				<tr height="310px"><td>
					<div class="f_box" style="height:310px;width:240px;overflow:auto">
						<div id="userView" class="ztree"></div>
					</div>
				</td></tr>
			</table>
		</div>
		<!-- <div style="height:40px;">
			<table class="f_table">
				<tr><td><input name="cyz" id="xt" type="radio"/><label for="xt">系统</label></td></tr>
			</table>
		</div> -->
	</div>
	
	<div id="dlg_addLocalCopyProcess" class="easyui-dialog" title="新增本地副本" data-options="closed:true,modal:true" style="width:260px;height:120px;padding:10px">
		<form action="" method="post">
			<table>
				<tr>
					<td>版本号:</td>
					<td><input id="f_addLocalCopy_version" name="version" type="text" value=""></input></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="button" value="确定" onclick="addLocalCopy()"></input></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>