function addPanel(xmlDoc,tabId,tabTitle){
	var accept = "node_"+tabId;
	var nodeClassName = "node "+accept;
	$('#process_tabs').tabs('add',{
		title: tabTitle,
		content:"<div id='tool1_"+tabId+"' class='toolbar'>"+
				"<input type='button' class='btn' value='保存' id='toolbar_btn_save_"+tabId+"'/>"+
				"<input type='button' class='btn' value='查看' id='toolbar_btn_show_"+tabId+"'/>"+
				"<input type='button' class='btn' value='校验' id='toolbar_btn_validate_"+tabId+"'/>"+
				/*"<input type='button' class='btn' value='删除' id='toolbar_btn_delete_"+tabId+"'/>"+*/
				/*"<input type='button' class='btn' value='同步服务器' onclick='syncServer();' id='toolbar_btn_syncServer_"+tabId+"'/>"+*/
				"<input type='button' class='btn' value='上传流程' onclick='uploadProcess();' id='toolbar_btn_upload_"+tabId+"'/>"+
				"<input type='button' class='btn' value='注册流程' onclick='registerProcess();' id='toolbar_btn_reg_"+tabId+"'/>"+
				"<input type='button' class='btn' value='同步相关数据表' onclick='syncDataField();' id='toolbar_btn_syncDataFieldTable_"+tabId+"'/>"+
				"<input type='button' class='btn' value='启动流程' onclick='startProcess();' id='toolbar_btn_start_"+tabId+"'/>"+
				"<input type='button' class='btn' value='停止流程' onclick='stopProcess();' id='toolbar_btn_stop_"+tabId+"'/>"+
				"<input type='button' class='btn' value='刷新流程' onclick='refreshProcess();' id='toolbar_btn_refresh_"+tabId+"'/>"+
				"<input type='button' class='btn' value='删除流程' onclick='deleteProcess();' id='toolbar_btn_delete_"+tabId+"'/>"+
				"<input type='button' class='btn' value='增加本地副本' onclick='addLocalCopyDialog();' id='toolbar_btn_fileCopy_"+tabId+"'/>"+
				"</div>"+
				"<div id='tool2_"+tabId+"' class='toolbar'>"+
				"<div class='other' type='POINTER'><img src='"+basePath+"/coflow/img/select.png' alt='11111111'/></div>"+
				 "<div class='other' type='PATH'><img src='"+basePath+"/coflow/img/transition.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='START' and='AND' or='AND'><img src='"+basePath+"/coflow/img/start.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='FINISH' and='AND' or='AND'><img src='"+basePath+"/coflow/img/end.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='NORMAL' and='AND' or='AND'><img src='"+basePath+"/coflow/img/andand.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='NORMAL' and='AND' or='OR'><img src='"+basePath+"/coflow/img/andor.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='NORMAL' and='OR' or='AND'><img src='"+basePath+"/coflow/img/orand.png' /></div>"+
				 "<div class='"+nodeClassName+"' type='NORMAL' and='OR' or='OR'><img src='"+basePath+"/coflow/img/oror.png' /></div>"+
				 "</div>"+
				 "<div style=\"width:100%;height:100%;overflow:auto;\">" +
				 "<div id='area_"+tabId+"' class='paper'></div>"+
				 "</div>",
		closable: true
	});
	var tab = $('#process_tabs').tabs("getSelected");
	$(tab).panel('options').tab.parent().hide();
	$('#process_tabs').tabs("resize");
	$(tab).data("tabId",tabId);
	$(tab).data("xmlDoc",xmlDoc);
	$(tab).data("backup",xmlToString(xmlDoc));
	$(tab).data("currentXmlDoc",xmlToString(xmlDoc));
	$(tab).unbind("data").bind("data",function(){
		initZtree(xmlDoc);
	});
	$(tab).unbind("updateCurrentXmlDoc").bind("updateCurrentXmlDoc",function(){
		$(tab).data("currentXmlDoc",xmlToString(xmlDoc));
	});
	createRaphaelPaper(xmlDoc,tabId,accept);
}
function createRaphaelPaper(xmlDoc,tabId,accept){
	var wps = getWorkflowProcesses(xmlDoc);
	var r = null;
	if(wps.length == 0){
		r = new RaphaelPaper("area_"+tabId,xmlDoc,null);
	}else{
		r = new RaphaelPaper("area_"+tabId,xmlDoc,wps[0]);
	}
	$("#area_"+tabId).droppable({
		accept:"."+accept,
		onDrop: function(e,source){
			if (checkRunning())  {
				pwin.dhtmlx.message("运行中的流程，不可添加节点！");
				return false;
			}
			new Rect(r,source.getAttribute("type"),"","",source.getAttribute("and"),source.getAttribute("or"),null);
		}
	});
	
	$(".node").draggable({
		revert:true,
		proxy:function(source){
			var n = $('<div class="proxy"></div>');
			n.html($(source).html()).appendTo('body');
			return n;
		}
	});
	initZtree(xmlDoc);
}


function initZtree(xmlDoc){
	var setting = {
			view:{
				showIcon:false,
				addHoverDom: addHoverDom,
				removeHoverDom: removeHoverDom,
				selectedMulti: false
			},
			edit: {
				drag:{
					autoExpandTrigger: false,
					isCopy:false,
					isMove:false
					
				},
				enable: true,
				showRemoveBtn: showRemoveBtn,
				showRenameBtn: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				beforeRemove: beforeRemove,
				onClick: onClick
				//onRightClick: onRightClick
			}
	};
	function onClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.pId == "ws"){
			var packageNode = xmlDoc.getElementsByTagName("Package")[0];
			var w = selectNode(xmlDoc,packageNode,"WorkflowProcesses/WorkflowProcess[@id='"+treeNode.id+"']");
			var tab = $('#process_tabs').tabs("getSelected");
			var tabId = $(tab).data("tabId");
			$("#area_"+tabId).html("");
			var r = null;
			if(w == null){
				r = new RaphaelPaper("area_"+tabId,xmlDoc,null);
			}else{
				r = new RaphaelPaper("area_"+tabId,xmlDoc,w);
			}
			var accept = "node_"+tabId;
			$("#area_"+tabId).droppable({
				accept:"."+accept,
				onDrop: function(e,source){
					new Rect(r,source.getAttribute("type"),"","",source.getAttribute("and"),source.getAttribute("or"),null);
				}
			});
			
			$(".node").draggable({
				revert:true,
				proxy:function(source){
					var n = $('<div class="proxy"></div>');
					n.html($(source).html()).appendTo('body');
					return n;
				}
			});
		}else{
			treeNode.showProps(treeNode);
		}
	}
	function beforeRemove(treeId, treeNode) {
		if(confirm("确认删除吗?")){
			if(treeNode.pId == "ws" && treeNode.getParentNode().children.length == 1){
				alert("至少要存在一个流程");
			}else{
				treeNode.remove();
				return true;
			}
		}
		return false;
	}
	function showRemoveBtn(treeId, treeNode) {
		if(treeNode.id == "as" || treeNode.id == "ps" || treeNode.id == "ws" || treeNode.pId == null){
			return false;
		}else{
			return true;
		}
	}
	
	function addHoverDom(treeId, treeNode) {
		var Node = function(id,pId,name,isOpen,obj){
			this.id = id;
			this.pId = pId;
			this.name = name;
			this.open = isOpen;
			this.showProps = function(treeNode){
				showPackageProps(obj,treeNode);
			};
			this.remove = function(){
				obj.parentNode.removeChild(obj);
			};
		};
		if(treeNode.id == "ps" || treeNode.id == "as" || treeNode.id == "ws"){
			var sObj = $("#" + treeNode.tId + "_span");
			if (treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length>0) return;
			var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
			+ "' title='add node' onfocus='this.blur();'></span>";
			sObj.after(addStr);
			var btn = $("#addBtn_"+treeNode.tId);
			if (btn){
				btn.bind("click", function(){
					var zTree = $.fn.zTree.getZTreeObj("packageView");
					var xmlNode = null;
					if(treeNode.id == "ps"){
						xmlNode = createParticipant(xmlDoc);
					}else if(treeNode.id == "as"){
						xmlNode = createApplication(xmlDoc);
					}else if(treeNode.id == "ws"){
						xmlNode = createWorkflowProcess(xmlDoc);
					}
					if(xmlNode != null){
						var node = new Node(xmlNode.getAttribute("id"),treeNode.id,xmlNode.getAttribute("name"),false,xmlNode);
						zTree.addNodes(treeNode, node);
					}
					return false;
				});
			} 
		}
	};
	function removeHoverDom(treeId, treeNode) {
		$("#addBtn_"+treeNode.tId).unbind().remove();
	};
	var zNodes = packageTree(xmlDoc);
	$.fn.zTree.init($("#packageView"), setting, zNodes);
}

