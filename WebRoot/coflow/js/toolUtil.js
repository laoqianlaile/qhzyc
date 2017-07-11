var isForceCloseTab = false;
var process_tabs_parent = null;
var pwin = window.parent.window;
/**
 * 新建流程版本文件
 */
function createVersionFile(){
	var id = $("#f_flowDefineId").val();
	var version = $("#f_version").val();
	var desc = $("#f_desc").val();
	
	if($.trim(version) == ""){
		$.messager.alert('Warning','版本不能为空');
		return;
	}
	$.ajax({
		type: "POST",
		url: basePath+"/action/work-flow-manager!addVersionFile",
		data:"flowDefineId="+id+"&version="+version+"&desc="+desc,
		success:function(data){
			try{
				var rdata = $.parseJSON(data);
				if(rdata != null){
					 var zTree = $.fn.zTree.getZTreeObj("projectView");
					 var node = zTree.getNodeByParam("id",rdata[0].pId,null);
					 if(node != null){
						zTree.addNodes(node,rdata);
						$('#dlg_new_file').dialog('close');
					 }
				}
			}catch(e){
				if(data != ""){
					alert(data);
				}
			}
		},
		error:function(){
			 alert("创建失败,当前网络发生错误!");
		 }
	});
}

/**
 * 项目树鼠标双击
 */
function openXmlDoc(versionId){
	if(versionId != null){
		$.ajax({
			  type: "POST",
			  url: pwin.AppActionURI.workflowVersion + "!getProcessFile.json",
			  data:"id="+versionId,
			  success: function(data){
				  if(data != ""){
					  var tabId = versionId;//树节点(版本)id
					  var xmlData = data.xmlDoc;//流程图数据
					  var xmlDoc = stringToXml(xmlData);
					  var packageNode = xmlDoc.getElementsByTagName("Package")[0];
					  var packageId = packageNode.getAttribute("id");
					  var packageName = packageNode.getAttribute("name");
					  var div = $("#area_"+tabId);
					  var title = packageName + data.version;
					  addPanel(xmlDoc,tabId,title);
					  initProcessMenu(data.status, tabId);
				  }
			  },
			  error:function(){
				  pwin.dhtmlx.alert("加载失败,当前网络发生错误!");
			  }
		});
	}
	
}
function initProcessMenu(status,tabId) {
	status = status.toLowerCase();
	var showItems = [];
	if (status == "unknown" || status=="error") {
		showItems = ["fileCopy"];
	} else if(status == "local" || status == "undefined") {
		showItems = ["upload","fileCopy"];
	} else if (status == "running" || status == "modified running") {
		showItems = ["stop","syncDataFieldTable","fileCopy","refresh"];
	} else if(status == "updated" || status == "modified updated") {
		showItems = ["stop","fileCopy"];
	} else if(status == "stoped" || status == "modified stoped") {
		showItems = ["start","delete","fileCopy"];
	} else if(status == "unregist" || status == "modified unregist") {
		showItems = ["delete","reg","syncDataFieldTable","fileCopy"];
	}
	projectMenuItem(showItems,tabId);
}


function projectMenuItem(showItems,tabId){
	var menuItems = ["delete","reg","upload",
	 				"start","stop","syncDataFieldTable","fileCopy","refresh"
	 			];
	var prefix = "toolbar_btn_";
	var suffix = "_"+tabId;
	for(var i=0;i<menuItems.length;i++){
		$('#'+prefix+menuItems[i]+suffix).attr("disabled","true");
	}
	for(var i=0;i<showItems.length;i++){
		$('#'+prefix+showItems[i]+suffix).removeAttr("disabled");
	}
	
}


/**
 * 同步相关数据表
 */
function syncDataField(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		  type: "POST",
		  url: pwin.AppActionURI.workflowVersion + "!syncDataFieldTable.json",
		  data:"id="+tabId,
		  success: function(msg) {
			  if(msg.success) {
				  pwin.dhtmlx.message("操作成功！");
			  } else {
				  pwin.dhtmlx.message(msg.message);
			  }
		  },
		  error:function() {
			  pwin.dhtmlx.alert("同步相关数据表发生错误，请联系管理员！");
		  }
	});
}

/**
 * 上传流程
 */
function uploadProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!uploadProcess.json",
		data: "id=" + tabId,
		success: function(msg) {
			if (msg.success) {
				var d = msg.data;
				var text = d.version + "（" + d.statusStr + "）";
				var userdata = [{name: "type", content: "2"},{name: "status", content: d.status}];
				pwin.dhtmlx.message("上传成功！");
				// refresh tree node
				pwin.updateCurrentNode(text, userdata);
				initProcessMenu(d.status, d.id);
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("上传流程发生错误，请联系管理员！");
		}
	});
}

/**
 * 刷新流程
 */
function refreshProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	if (checkBeforeSave(tab)) {
		pwin.dhtmlx.message('请先保存流程信息，再刷新流程！');
		return;
	}
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!refreshProcess.json",
		data: "id=" + tabId,
		success: function(msg) {
			if (msg.success) {
				pwin.dhtmlx.message("刷新流程成功！");
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("刷新流程发生错误，请联系管理员！");
		}
	});
}

/**
 * 注册流程
 */
function registerProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!registerProcess.json",
		data:"id="+tabId,
		success: function(msg){
			if (msg.success) {
				var items = msg.data, status;
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					if (tabId == item.id) {
						initProcessMenu(item.status, tabId);
					}
					var text = item.version + "（" + item.statusStr + "）";
					var userdata = [{name: "type", content: "2"}, {name: "status", content: item.status}];
					pwin.updateTreeNode(item.id, text, userdata);
				}
				pwin.dhtmlx.alert("注册成功！");
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("注册流程发生错误，请联系管理员！");
		}
	});
}
/**
 * 启动流程
 */
function startProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!startProcess.json",
		data:"id="+tabId,
		success: function(msg){
			if (msg.success) {
				var items = msg.data, status;
				for (var i = 0; i < items.length; i++) {
					var item = items[i];
					if (tabId == item.id) {
						initProcessMenu(item.status, tabId);
					}
					var text = item.version + "（" + item.statusStr + "）";
					var userdata = [{name: "type", content: "2"}, {name: "status", content: item.status}];
					pwin.updateTreeNode(item.id, text, userdata);
				}
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("启动流程发生错误，请联系管理员！");
		}
	});
}
/**
 * 停止流程
 */
function stopProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!stopProcess.json",
		data:"id="+tabId,
		success: function(msg){
			if (msg.success) {
				var d = msg.data;
				var text = d.version + "（" + d.statusStr + "）";
				var userdata = [{name: "type", content: "2"},{name: "status", content: d.status}];
				pwin.dhtmlx.message("操作成功！");
				// refresh tree node
				pwin.updateCurrentNode(text, userdata);
				initProcessMenu(d.status, d.id);
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("停止流程发生错误，请联系管理员！");
		}
	});
}
/**
 * 删除流程(删除的是引擎端的)
 */
function deleteProcess(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!deleteProcess.json",
		data:"id="+tabId,
		success: function(msg){
			if (msg.success) {
				var d = msg.data;
				var text = d.version + "（" + d.statusStr + "）";
				var userdata = [{name: "type", content: "2"},{name: "status", content: d.status}];
				pwin.dhtmlx.message("操作成功！");
				// refresh tree node
				pwin.updateCurrentNode(text, userdata);
				initProcessMenu(d.status, d.id);
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("删除流程发生错误，请联系管理员！");
		}
	});
}
/**
 * 更新组织用户
 */
function updateOrganization(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: basePath+"/action/work-flow-manager!updateOrganization",
		data:"id="+tabId,
		success: function(data){
			if(data == "success"){
				alert("成功");
			}else if(data == "error"){
				alert("失败");
			}else{
				alert(data);
			}
		},
		error:function(){
			pwin.dhtmlx.alert("更新组织用户发生错误，请联系管理员！");
		}
	});
}
/**
 * 同步服务器
 */
function syncServer(){
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!syncServer.json",
		data:"id="+tabId,
		success: function(data){
			/*try{
				var rdata = $.parseJSON(data);
				if(rdata != null){
					 var zTree = $.fn.zTree.getZTreeObj("projectView");
					  for(var i=0;i<rdata.length;i++){
						  var node2 = zTree.getNodeByParam("id", rdata[i].id);
						  if(node2 != null){
							  node2.name = rdata[i].name;
							  if(rdata[i].status)
								  node2.status = rdata[i].status;
							  zTree.updateNode(node2);
							  if(rdata[i].id == tabId){
								  initProcessMenu(rdata[i].status,tabId);
							  }
						  }else{
							  zTree.addNodes(node,rdata[i]);
						  }
					  }
				}
			}catch(e){
				if(data != ""){
					alert(data);
				}
			}*/
			if (msg.success) {
				var d = msg.data;
				//pwin.updateCurrentNode(d.text, d.userdata);
				//initProcessMenu(rdata[i].status,rdata[i].id);
			} else {
				pwin.dhtmlx.message("操作失败！");
			}
		},
		error:function(){
			alert("同步失败失败,当前网络发生错误!");
		}
	});
	
}

function addLocalCopyDialog(){
	$('#dlg_addLocalCopyProcess').dialog('open');
}
function addLocalCopy(){
	var version  = $("#f_addLocalCopy_version").val();
	if(typeof(version)== undefined || $.trim(version) == ""){
		alert("版本号不能为空");
		return false;
	}
	var tab = $('#process_tabs').tabs("getSelected");
	var tabId = $(tab).data("tabId");
	$.ajax({
		type: "POST",
		url: pwin.AppActionURI.workflowVersion + "!addLocalCopy.json",
		data:"id="+tabId+"&version="+version,
		success: function(msg){
			if (msg.success) {
				var d = msg.data;
				var text = d.version + "（" + d.statusStr + "）";
				var userdata = [{name: "type", content: "2"},{name: "status", content: d.status}];
				pwin.dhtmlx.message("操作成功！");
				// refresh tree node
				pwin.insertTreeNode(d.id, text, userdata);
				$('#dlg_addLocalCopyProcess').dialog('close');
			} else {
				pwin.dhtmlx.message(msg.message);
			}
		},
		error:function(){
			alert("增加失败,当前网络发生错误!");
		}
	});
}
/**
 * 项目右键菜单处理
 * @param item
 */
function projectViewRmenuHandler(item){
	if(item.name == "add"){//新建文件
		showCreateFile();
	}else if(item.name == "delete"){//删除流程资源文件
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			if(confirm("确定要删除本地资源吗？")){
				var node = nodes[0];
				$.ajax({
					  type: "POST",
					  url: basePath+"/action/project-manager!deleteItem",
					  data:"id="+node.id+"&pid="+node.pId,
					  success: function(data){
						 var tabs = $('#process_tabs').tabs("tabs");
						 if(node.pId == null){//父节点
							 var childs = node.children;
							 if(childs){
								 isForceCloseTab = true;
								 for(var i=0;i<childs.length;i++){
									 for(var j=0;j<tabs.length;j++){
										 if(tabs[j].data("tabId") == childs[i].id){
											  var tabIndex =  $('#process_tabs').tabs("getTabIndex",tabs[j]);
											  $('#process_tabs').tabs('close', tabIndex);
											  break;
										  }
									 }
								  }
								 isForceCloseTab = false;
							 }
						 }else{
							 for(var i=0;i<tabs.length;i++){
								  if(tabs[i].data("tabId") == node.id){
									  var tabIndex =  $('#process_tabs').tabs("getTabIndex",tabs[i]);
									  isForceCloseTab = true;
									  $('#process_tabs').tabs('close', tabIndex);
									  isForceCloseTab = false;
								  }
							  }
						 }
						  zTree.removeNode(node);
					  },
					  error:function(){
						  alert("删除失败,当前网络发生错误!");
					  }
				});
			}
		}
	}else if(item.name == "properties"){//项目属性
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!getProjectInfo",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null && rdata.length == 1){
							var dataOne = rdata[0];
							$("#f_modifyProject").form("reset");
							$("#modify_projectId").val(dataOne.id);
							$("#modify_projectName").val(dataOne.name);
							$("#modify_serverAddress").val(dataOne.serverAddress);
							$("#modify_isConnectionServer").attr("checked", dataOne.isConnectionServer);
							$("#modify_desc").val(dataOne.description);
							$('#dlg_modify_project').dialog('open');
						}
					}catch(e){
						if(data != ""){
							alert(e);
						}
					}
				},
				error:function(){
					alert("当前网络发生错误!");
				}
			});
		}
	
	}else if(item.name == "syncDataField"){//同步相关数据表
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				  type: "POST",
				  url: basePath+"/action/project-manager!synchronizeDataFieldTable",
				  data:"id="+node.id,
				  success: function(data){
					  if(data != ""){
						  alert(data);
					  }
				  },
				  error:function(){
					  alert("同步失败,当前网络发生错误!");
				  }
			});
		}
	}else if(item.name == "uploadProcess"){//上传流程
		$('#dlg_uploadProcess').dialog('open');
	}else if(item.name == "registerProcess"){//注册流程
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!registerProcess",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							 var zTree = $.fn.zTree.getZTreeObj("projectView");
							  for(var i=0;i<rdata.length;i++){
								  var node = zTree.getNodeByParam("id", rdata[i].id);
								  if(node != null){
									  node.name = rdata[i].name;
									  node.status = rdata[i].status;
									  zTree.updateNode(node);
								  }
							  }
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("注册失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "startProcess"){//启动流程
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!startProcess",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							 var zTree = $.fn.zTree.getZTreeObj("projectView");
							  for(var i=0;i<rdata.length;i++){
								  var node = zTree.getNodeByParam("id", rdata[i].id);
								  if(node != null){
									  node.name = rdata[i].name;
									  node.status = rdata[i].status;
									  zTree.updateNode(node);
								  }
							  }
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("启动失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "stopProcess"){//停止流程
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!stopProcess",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							var zTree = $.fn.zTree.getZTreeObj("projectView");
							for(var i=0;i<rdata.length;i++){
								var node = zTree.getNodeByParam("id", rdata[i].id);
								if(node != null){
									node.name = rdata[i].name;
									node.status = rdata[i].status;
									zTree.updateNode(node);
								}
							}
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("停止失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "deleteProcess"){//删除流程
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!deleteProcess",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							 var zTree = $.fn.zTree.getZTreeObj("projectView");
							  for(var i=0;i<rdata.length;i++){
								  var node = zTree.getNodeByParam("id", rdata[i].id);
								  if(node != null){
									  node.name = rdata[i].name;
									  node.status = rdata[i].status;
									  zTree.updateNode(node);
								  }
							  }
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("删除失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "updateOrganization"){//同步组织结构
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!updateOrganization",
				data:"id="+node.id+"&pid="+node.pId,
				success: function(data){
					if(data == "success"){
						alert("更新组织结构成功");
					}else if(data == "error"){
						alert("更新组织结构失败");
					}else{
						alert(data);
					}
				},
				error:function(){
					alert("更新组织结构失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "updateProject"){//与服务器同步(项目)
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!updateProject",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							 var zTree = $.fn.zTree.getZTreeObj("projectView");
							  for(var i=0;i<rdata.length;i++){
								  var node2 = zTree.getNodeByParam("id", rdata[i].id);
								  if(node2 != null){
									  node2.name = rdata[i].name;
									  if(rdata[i].status)
										  node2.status = rdata[i].status;
									  zTree.updateNode(node2);
								  }else{
									  zTree.addNodes(node,rdata[i]);
								  }
							  }
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("同步失败失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "updateFile"){//与服务器同步(文件)
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!updateFile",
				data:"id="+node.id,
				success: function(data){
					if(data != ""){
						alert(data);
					}
				},
				error:function(){
					alert("上传失败,当前网络发生错误!");
				}
			});
		}
	}else if(item.name == "addLocalCopy"){
		var zTree = $.fn.zTree.getZTreeObj("projectView");
		var nodes = zTree.getSelectedNodes();
		if(nodes.length > 0){
			var node = nodes[0];
			$.ajax({
				type: "POST",
				url: basePath+"/action/project-manager!addLocalCopy",
				data:"id="+node.id,
				success: function(data){
					try{
						var rdata = $.parseJSON(data);
						if(rdata != null){
							zTree.addNodes(node.getParentNode(),rdata);
						}
					}catch(e){
						if(data != ""){
							alert(data);
						}
					}
				},
				error:function(){
					alert("增加失败,当前网络发生错误!");
				}
			});
		}
	}
}

/**
 * 修改项目信息
 */
function modifyProject(){
	var projectName = $("#modify_projectName").val();
	if($.trim(projectName) == ""){
		$.messager.alert('Warning','名称不能为空');
		return;
	}
	$('#f_modifyProject').form("submit",{
		success:function(data){
			if(data == "error"){
				alert("该项目已经存在");
			}else{
				try {
					var rdata = $.parseJSON(data);
					if (rdata != null) {
						var zTree = $.fn.zTree.getZTreeObj("projectView");
						var node = zTree.getNodeByParam("id", rdata.id);
						if (node) {
							node.name = rdata.name;
							zTree.updateNode(node);
							$('#dlg_modify_project').dialog('close');
						}
					}
				} catch (e) {
					if(data != ""){
						alert(data);
					}
				}
			}
		},
		error:function(){
			 alert("修改失败,当前网络发生错误!");
		 }
	});
}

/**
 * 保存前校验
 * @param tab
 * @returns {Boolean}
 *          -- false 不需要保存
 *          -- true  需要保存
 */
function checkBeforeSave(tab) {
	var tabId = $(tab).data("tabId");
	var backupXmlDocString = $(tab).data("backup");
	$(tab).trigger("updateCurrentXmlDoc");
	var currentXmlDocString = $(tab).data("currentXmlDoc");
	if(backupXmlDocString == currentXmlDocString){
		return false;
	}
	return true;
}

/**
 * 保存
 * @param tab
 * @param isClosed
 */
function saveXmlDocToServer(tab,isClosed){
	var tabId = $(tab).data("tabId");
	var backupXmlDocString = $(tab).data("backup");
	$(tab).trigger("updateCurrentXmlDoc");
	var currentXmlDocString = $(tab).data("currentXmlDoc");
	if(backupXmlDocString == currentXmlDocString){
		pwin.dhtmlx.message('流程信息未修改，无需保存！');
		return;
	}
	//var zTree = $.fn.zTree.getZTreeObj("projectView");
	//var node = zTree.getNodeByParam("id", tabId);
	var status = pwin.getCurrentStatus();
	if (status) status = status.toLowerCase();
	if(typeof(status) == undefined || status == "unknown" || status == "updated" || status == "modified updated"){
		pwin.dhtmlx.alert('处于未知的流程不能够被保存.');
		return;
	}
	if(status == "running" || status == "modified running"){
		//确认对话框,
		pwin.dhtmlx.confirm({
			type:"confirm",
			text: "请确保不要修改影响流程正常运行的相关属性，确定要修改吗？",
			ok: "确定",
			cancel: "取消",
			callback: function(sure) {
				if (sure) _save();
			}
		});
	} else {
		_save();
	}
	
	function _save() {
		var data = tabId + "々" + currentXmlDocString;
		$.ajax({
			  type: "POST",
			  url: pwin.AppActionURI.workflowVersion + "!saveProcessFile.json",
			  data: {"data": data},
			  success: function(msg){
				  if(!isClosed){
					  $(tab).data("backup",currentXmlDocString);
					  var t = tab.panel('options').tab;
					  var title = tab.panel("options").title;
					  var s_title = t.find('span.tabs-title');
					  if(title.substring(0,1) == "*"){
						  title = title.substring(1);
					  }
					  s_title.html(title);
				  }
				  $(tab).data("isChange",false);
				  var text, userdata;
				  if (msg.success) {
					  text = msg.data.version + "（" + msg.data.statusStr + "）";
					  userdata = [{"name":"status", "content": msg.data.status},{"name":"type", "content": "2"}];
					  pwin.updateCurrentNode(text, userdata);
					  pwin.dhtmlx.message("保存成功!");
					  if(status == "running" || status == "modified running") {
						  //确认对话框,
						  pwin.dhtmlx.confirm({
							  type:"confirm",
							  text: "是否刷新到当前运行流程中？",
							  ok: "确定",
							  cancel: "取消",
							  callback: function(sure) {
								  if (sure) refreshProcess();
							  }
						  });
					  }
				  } else {
					  pwin.dhtmlx.message("保存失败!");
				  }
				  
			  },
			  error:function(){
				  pwin.dhtmlx.alert("保存失败,当前网络发生错误!");
			  }
		});
	}
	
}

/**
 * 流程校验
 * @param tab
 */
function validate(tab){
	var tabId = $(tab).data("tabId");
	var xmlDoc = $(tab).data("xmlDoc");
	var data = tabId + "々" + xmlToString(xmlDoc);
	$.ajax({
		  type: "POST",
		  url: pwin.AppActionURI.workflowVersion + "!checkProcessFile.json?",
		  data:{"data": data},
		  success: function(msg){
			  if (msg.success) {
				  pwin.dhtmlx.message("校验成功！");
			  } else {
				  pwin.dhtmlx.alert(msg.message);
			  }
		  },
		  error:function(){
			  pwin.dhtmlx.alert("校验失败,当前网络发生错误!");
		  }
	});

}
/**
 * 格式化展示
 */
function formatShow(xmlDoc){
	var data = xmlToString(xmlDoc);
	$.ajax({
		  type: "POST",
		  url: pwin.AppActionURI.workflowVersion + "!formatShow.json",
		  data:"data="+data,
		  success: function(data){
			  $("#dlg_xmlContent").val(data);
			  $('#dlg').dialog('open');
		  },
		  error:function(){
			  alert("当前网络发生错误!");
		  }
	});
}
/**
 * 自动检测当前xmlDoc是否发生改变
 * @param tab
 */
function autoCheckChange(tab){
	if(tab != null){
		if($(tab).data("isChange")){
			return;
		}
		var backupXmlDoc = $(tab).data("backup");
		$(tab).trigger("updateCurrentXmlDoc");
		var currentXmlDoc = $(tab).data("currentXmlDoc");
		var t = tab.panel('options').tab;
		var title = tab.panel("options").title;
		var s_title = t.find('span.tabs-title');
		if(backupXmlDoc != currentXmlDoc){
			$(tab).data("isChange",true);
			if(title.substring(0,1) != "*"){
				title = "*"+title;
			}
			s_title.html(title)
		}
	}
}

function getX(obj){ 
	var parObj=obj; 
	var left=obj.offsetLeft; 
	while(parObj=parObj.offsetParent){ 
		left+=parObj.offsetLeft; 
	} 
	return left; 
} 
	 
function getY(obj){ 
	var parObj=obj; 
	var top=obj.offsetTop; 
	while(parObj = parObj.offsetParent){ 
		top+=parObj.offsetTop; 
	} 
	return top; 
} 
/**
 * 通过index排序形参
 * @param x
 * @param y
 * @returns
 */
function sortFormalParamByIndexAsc(x,y){
	return (eval(x.index)>eval(y.index))?1:-1;
}

/**
 * 判断流程是否为运行状态
 * @returns {Boolean}
 */
function checkRunning() {
	var status = pwin.getCurrentStatus();
	return (status == "running" || status == "modified running"
		|| status === "updated" || status == "modified updated");
}