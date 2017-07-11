var oldValue;//用于存储各属性修改前的值
var oldRow = {};
var getPathString = function(rect1,rect2){
		//var bbox1 = rect1.getBBox();
		var bbox1 = {"x":eval(rect1.attr("x")),"y":eval(rect1.attr("y")),"width":eval(rect1.attr("width")),"height":eval(rect1.attr("height")),
				"x2":eval(rect1.attr("x"))+eval(rect1.attr("width")),"y2":eval(rect1.attr("y"))+eval(rect1.attr("height"))};
		//var bbox2 = rect2.getBBox();
		var bbox2 = {"x":eval(rect2.attr("x")),"y":eval(rect2.attr("y")),"width":eval(rect2.attr("width")),"height":eval(rect2.attr("height")),
				"x2":eval(rect2.attr("x"))+eval(rect2.attr("width")),"y2":eval(rect2.attr("y"))+eval(rect2.attr("height"))};
		var p1 = {x:bbox1.x+bbox1.width/2,y:bbox1.y2};
		var p2 = {x:bbox2.x+bbox2.width/2,y:bbox2.y};
		var pathString = "";
		if(p1.y<=p2.y){
			pathString = "M"+p1.x+" "+p1.y+"v0 "+(p2.y-p1.y)/2+"h0 "+(p2.x-p1.x)+"v0 "+(p2.y-p1.y)/2;
		}else{
			pathString = "M"+p1.x+" "+p1.y+"v0 10h0 "+(p2.x-p1.x)/2+"v0 "+(p2.y-p1.y-20)+"h0 "+(p2.x-p1.x)/2+"v0 10";
		}
		return pathString;
	};
var containerShow = function(divs){
	//divs 数组
	var defaultDivs = ["basicPropsContainer","formalParamContainer","dataFieldContainer","implToolContainer","implSubflowContainer"];
	for(var i=0;i<defaultDivs.length;i++){
		$("#"+defaultDivs[i]).hide();
	}
	for(var i=0;i<divs.length;i++){
		$("#"+divs[i]).show();
	}
};
var RaphaelPaper = function(raphaelAreaId,xmlDoc,workflowProcess){
	if(workflowProcess == null){
		workflowProcess = createWorkflowProcess(xmlDoc);
	}
	this.xmlDoc = xmlDoc;
	this.workflowProcess = workflowProcess;
	this.currentIdIndex = "";
	var own = this;
	this.width = $("#"+raphaelAreaId).width();
	this.height = $("#"+raphaelAreaId).height();
	var paper = Raphael(raphaelAreaId,this.width, this.height);
	var paperX = "";
	var paperY = "";
	var currentRects = [];
	var connections = [];
	this.pathWhere = [];
	this.tmpPath ="";
	this.getPaper = function(){
		return paper;
	};
	this.getPoint = function(){
		return {x:paperX,y:paperY};
	};
	this.getRects = function(){
		return currentRects;
	};
	this.getConnections = function(){
		return connections;
	};
	$("#"+raphaelAreaId).unbind("mousemove").bind("mousemove",function(e){
		var top,left,oDiv;
		oDiv = document.getElementById(raphaelAreaId); 
		top = getY(oDiv); 
		left = getX(oDiv); 
		paperX = (e.clientX-left + oDiv.parentElement.scrollLeft);
		paperY = (e.clientY-top + oDiv.parentElement.scrollTop);
	});
	$("#"+raphaelAreaId).unbind("click").bind("click",function(){
		$(paper).data("currNode",null);
		for(var i=0;i<currentRects.length;i++){
			currentRects[i].hide();
		}
		for(var i=0;i<connections.length;i++){
			connections[i].path.removePoints();
		}
		containerShow(["basicPropsContainer","formalParamContainer","dataFieldContainer"]);
		showProps();
		showDataFiled();
		showFormalParam();
	});
	
	var tempId = raphaelAreaId.substring(5);
	$("#tool2_"+tempId+" > div[type='PATH']").unbind("click").bind("click",function(){
		$(paper).data("mod","path");
	});
	$("#tool2_"+tempId+" > div[type='POINTER']").unbind("click").bind("click",function(){
		$(paper).data("mod","pointer");
	});
	$("#toolbar_btn_show_"+tempId).unbind("click").bind("click",function(){
		formatShow(xmlDoc);
	});
	$("#toolbar_btn_save_"+tempId).unbind("click").bind("click",function(){
		var tab = $('#process_tabs').tabs("getSelected");
		saveXmlDocToServer(tab,false);
	});
	$("#toolbar_btn_validate_"+tempId).unbind("click").bind("click",function(){
		var tab = $('#process_tabs').tabs("getSelected");
		validate(tab);
	});
	
	this.keyFun = function(e){
		var currNode = $(paper).data("currNode");
		if (currNode) {
			if (e.keyCode == 46) {
				if (checkRunning()) {
					pwin.dhtmlx.message("运行中的流程，不能删除节点！");
					return;
				}
				currNode.remove();
				$(paper).removeData("currNode");
				if(own.tmpPath != ""){
					own.tmpPath.remove();
					own.pathWhere = [];
					$(document).unbind("mousemove");
				}
				e.preventDefault();
			}else if(e.keyCode == 37){
				//向左
				if("rectNudgeByKey" in currNode){
					if(e.shiftKey == true){
						currNode.rectNudgeByKey("left",8);
					}else{
						currNode.rectNudgeByKey("left",1);
					}
				}
				e.preventDefault();
			}else if(e.keyCode == 38){
				//向上
				if("rectNudgeByKey" in currNode){
					if(e.shiftKey == true){
						currNode.rectNudgeByKey("up",8);
					}else{
						currNode.rectNudgeByKey("up",1);
					}
				}
				e.preventDefault();
			}else if(e.keyCode == 39){
				//向右
				if("rectNudgeByKey" in currNode){
					if(e.shiftKey == true){
						currNode.rectNudgeByKey("right",8);
					}else{
						currNode.rectNudgeByKey("right",1);
					}
				}
				e.preventDefault();
			}else if(e.keyCode == 40){
				//向下
				if("rectNudgeByKey" in currNode){
					if(e.shiftKey == true){
						currNode.rectNudgeByKey("down",8);
					}else{
						currNode.rectNudgeByKey("down",1);
					}
				}
				e.preventDefault();
			}
		}
		
	};
	function showProps(){
		$('#basicProps').datagrid('loadData',{total:0,rows:[]}); 
		var props = [{key:"row_id",name:"ID",editor:"text"},{key:"row_name",name:"名称",editor:"text"},
		             {key:"row_desc",name:"描述",editor:"text"},
		             {key:"row_viewFormPage",name:"查看表单页面",editor:"text"}
		             ];
		var rows = [];
		var row_id = {name:"ID",value:own.workflowProcess.getAttribute("id"),editor:'text'};
		rows.push(row_id);
		var row_name = {name:"名称",value:own.workflowProcess.getAttribute("name"),editor:'text'};
		rows.push(row_name);
		for(var i=0;i<own.workflowProcess.childNodes.length;i++){
			var node = own.workflowProcess.childNodes[i];
			if(node.nodeName == "Description"){
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_desc = {name:"描述",value:node2.nodeValue,editor:'text'};
						rows.push(row_desc);
						break;
					}
				}
			}else if(node.nodeName == "ExtendedAttributes"){
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "ExtendedAttribute" && node2.getAttribute("name") == "CLFLOW_VIEW_FORM_PAGE"){
						var row_viewFormPage = {name:"查看表单页面",value:node2.getAttribute("value"),editor:'text'};
						rows.push(row_viewFormPage);
						break;
					}
				}
				
			}
		}
		for(var i=0;i<props.length;i++){
			var hasElement = false;
			for(var j=0;j<rows.length;j++){
				if(props[i].name == rows[j].name){
					hasElement = true;
					$('#basicProps').propertygrid('appendRow',rows[j]);
					break;
				}
			}
			if(!hasElement){
				var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
				$('#basicProps').propertygrid('appendRow',tempRow);
			}
		}
		$('#basicProps').propertygrid({
			onBeforeEdit:function(index,row){
				oldValue = row.value;
			},
		    onAfterEdit:function(index,row){
		    	if(row.value == oldValue){
		    		return;
		    	}
		    	switch (index) {
				case 0://id修改
					if(checkAttr(xmlDoc,"id",row.value)){
						$.messager.alert('Warning','ID已经存在!');
						return;
					}
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','ID不能为空!');
						return;
					}
					var treeObj = $.fn.zTree.getZTreeObj("packageView");
					var wsNode = treeObj.getNodeByParam("id", "ws");
					var wpId = own.workflowProcess.getAttribute("id");
					for(var i=0;i<wsNode.children.length;i++){
						if(wsNode.children[i].id == wpId){
							wsNode.children[i].id = row.value;
							treeObj.updateNode(wsNode.children[i]);
						}
					}
					var srcId = own.workflowProcess.getAttribute("id");
					var destId = row.value;
					own.workflowProcess.setAttribute("id",row.value);
					triggerIdChange(xmlDoc, "WorkflowProcess", srcId, destId);
					break;
				case 1://名称修改
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','名称不能为空!');
						return;
					}
					if(checkAttr(xmlDoc,"name",row.value)){
						$.messager.alert('Warning','名称已经存在!');
						return;
					}
					var treeObj = $.fn.zTree.getZTreeObj("packageView");
					var wsNode = treeObj.getNodeByParam("id", "ws");
					var wpId = own.workflowProcess.getAttribute("id");
					for(var i=0;i<wsNode.children.length;i++){
						if(wsNode.children[i].id == wpId){
							wsNode.children[i].name = row.value;
							treeObj.updateNode(wsNode.children[i]);
						}
					}
					own.workflowProcess.setAttribute("name",row.value);
					break;
				case 2://描述修改
					var hasElement = false;
					var desc = getSubNode(own.workflowProcess,"Description");
					if(desc != null){
						hasElement = true;
						removeAllSubNode(desc);
						if(row.value != ""){
							desc.appendChild(xmlDoc.createTextNode(row.value));
						}
					}
					if(!hasElement && row.value != ""){
						var desc = xmlDoc.createElement("Description");
						desc.appendChild(xmlDoc.createTextNode(row.value));
						own.workflowProcess.appendChild(desc);
					}
					break;
				case 3://查看表单页面修改
					var hasElement = false;
					var exts= getSubNode(own.workflowProcess,"ExtendedAttributes");
					if(exts != null){
						removeAllSubNode(exts);
						if(row.value != ""){
							var e = xmlDoc.createElement("ExtendedAttribute");
							e.setAttribute("name","CLFLOW_VIEW_FORM_PAGE");
							e.setAttribute("value",row.value);
							exts.appendChild(e);
						}
					}
					break;
				default:
					break;
				}
		    	var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
		    }
		});
	};
	function showDataFiled(){
		$('#dataField').datagrid('loadData',{total:0,rows:[]});
		var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"字符型",
						"text":"字符型"
					},{
						"value":"布尔型",
						"text":"布尔型"
					},{
						"value":"整型",
						"text":"整型"
					},{
						"value":"浮点型",
						"text":"浮点型"
					},{
						"value":"日期型",
						"text":"日期型"
					}],
					"panelHeight":"auto"
				}
			};
		$('#dataField').datagrid({
			columns: [[{field: 'ck',checkbox:true },
			           {field: 'id',title: 'ID',width: 150,editor: "text" }, 
			           {field: 'name',title: '名称',width: 100,editor: "text" },
			           {field: 'type',title: '数据类型',width: 100,editor: typeEditor },
			           {field: 'initValue',title: '初始值',width: 100,editor: "text" },
			           {field: 'note',title: '描述',width: 200,editor: "text" }]]
		});
		$("#dataFieldBtn_add").unbind("click").bind("click",function(){
			var dataField = createDataField(xmlDoc,own.workflowProcess);
			var row = {id:"",name:"",type:"",initValue:"",note:"",obj:dataField};
			row.id = dataField.getAttribute("id");
			row.name = dataField.getAttribute("name");
			var dataType = getSubNode(dataField,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = transDataType2Chinese(basicType.getAttribute("type"));
				}
			}
			var initValue = getSubNode(dataField,"InitialValue");
			if(initValue != null){
				for(var i=0;i<initValue.childNodes.length;i++){
					var node = initValue.childNodes[i];
					if(node.nodeName == "#text"){
						row.initValue = node.nodeValue;
						break;
					}
				}
			}
			var desc = getSubNode(dataField,"Description");
			if(desc != null){
				for(var i=0;i<desc.childNodes.length;i++){
					var node = desc.childNodes[i];
					if(node.nodeName == "#text"){
						row.note = node.nodeValue;
						break;
					}
				}
			}
			$('#dataField').datagrid("appendRow",row);
		});
		$("#dataFieldBtn_del").unbind("click").bind("click",function(){
			var checked = $('#dataField').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#dataField').datagrid('getRowIndex',checked[i]);
				var node = checked[i].obj;
				node.parentNode.removeChild(node);
				$('#dataField').datagrid('deleteRow',index);
			}
		});
		$("#dataFieldBtn_import").unbind("click").bind("click",function(){
		    var tab = $('#process_tabs').tabs("getSelected");
			var tabId = $(tab).data("tabId");
		    $('#dataField_preview').datagrid({
		    	url: pwin.AppActionURI.workflowVersion + "!importDataField.json?id=" + tabId,
		        columns:[[
		        {field: 'ck',checkbox:true },
		        {field:'id',title:'ID',width:100},
		        {field:'name',title:'名称',width:100},
		        {field:'dataType',title:'数据类型',width:100},
		        {field:'initValue',title:'初始值',width:100},
		        {field:'desc',title:'描述',width:134}
		        ]]
		        });
			$("#dlg_import_dataField").dialog({
				buttons:[{
					text:'确定',
					handler:function(){
						var checkedDataFields = $('#dataField_preview').datagrid('getChecked');
						for(var i=0;i<checkedDataFields.length;i++){
							var importDataField = checkedDataFields[i];
							var dataField = createImportDataField(xmlDoc,own.workflowProcess,importDataField);
							var row = {id:importDataField.id,name:importDataField.name,type:"",initValue:importDataField.initValue,note:importDataField.desc,obj:dataField};
							var dataType = getSubNode(dataField,"DataType");
							if(dataType != null){
								var basicType = getSubNode(dataType,"BasicType");
								if(basicType != null){
									row.type = transDataType2Chinese(importDataField.dataType);
								}
							}
							$('#dataField').datagrid("appendRow",row);
						}
						$("#dlg_import_dataField").dialog("close");
					}
				},{
					text:'取消',
					handler:function(){
						$("#dlg_import_dataField").dialog("close");
					}
				}]
			});
			$("#dlg_import_dataField").dialog("open");
		});
		function updateDataField(row){
			var dataField = row.obj;
			dataField.setAttribute("id",row.id);
			dataField.setAttribute("name",row.name);
			var dataType = getSubNode(dataField,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					basicType.setAttribute("type",transDataType2English(row.type));
				}
			}
			var initValue = getSubNode(dataField,"InitialValue");
			if(initValue == null){
				if(row.initValue != ""){
					initValue = xmlDoc.createElement("InitialValue");
					initValue.appendChild(xmlDoc.createTextNode(row.initValue));
					dataField.appendChild(initValue);
				}
			}else{
				removeAllSubNode(initValue);
				if(row.initValue != ""){
					initValue.appendChild(xmlDoc.createTextNode(row.initValue));
				}
			}
			
			
			var desc = getSubNode(dataField,"Description");
			if(desc == null){
				if(row.note != ""){
					desc = xmlDoc.createElement("Description");
					desc.appendChild(xmlDoc.createTextNode(row.note));
					dataField.appendChild(desc);
				}
			}else{
				removeAllSubNode(desc);
				if(row.note != ""){
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}
		};
		
		
		var dataFields = getDataFields(own.workflowProcess);
		for(var i=0;i<dataFields.length;i++){
			var df = dataFields[i];
			var row = {id:"",name:"",type:"",initValue:"",note:"",obj:df};
			row.id = df.getAttribute("id");
			row.name = df.getAttribute("name");
			var dataType = getSubNode(df,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = transDataType2Chinese(basicType.getAttribute("type"));
				}
			}
			var initValue = getSubNode(df,"InitialValue");
			if(initValue != null){
				for(var j=0;j<initValue.childNodes.length;j++){
					var node = initValue.childNodes[j];
					if(node.nodeName == "#text"){
						row.initValue = node.nodeValue;
						break;
					}
				}
			}
			var desc = getSubNode(df,"Description");
			if(desc != null){
				for(var j=0;j<desc.childNodes.length;j++){
					var node = desc.childNodes[j];
					if(node.nodeName == "#text"){
						row.note = node.nodeValue;
						break;
					}
				}
				
			}
			$('#dataField').datagrid("appendRow",row);
		}
		
		$('#dataField').datagrid({
			onClickCell:onClickCell,
			onAfterEdit:function(index,row){ 
				updateDataField(row);
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true;};
			 if ($('#dataField').datagrid('validateRow', editIndex)){
				 $('#dataField').datagrid('endEdit', editIndex);
				 editIndex = undefined;
				 return true;
			 } else {
				 return false;
			 }
		}

		function onClickCell(index, field){
			 if (endEditing()){
				 $('#dataField').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				 editIndex = index;
				 $(document).unbind('.datagrid').bind('mousedown.datagrid', function(e){
					 var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
					 if (p.length){return;}
					 endEditing();
					 editIndex = undefined;
				 });
			 }
		}
	};
	function showFormalParam(){
		$('#formalParam').datagrid('loadData',{total:0,rows:[]});
		var modeEditor = {
			type : 'combobox',
			options:{
				data:[{
					"value":"输入",
					"text":"输入"
				},{
					"value":"输入输出",
					"text":"输入输出"
				},{
					"value":"输出",
					"text":"输出"
				}],
				"panelHeight":"auto"
			}
		};
		var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"字符型",
						"text":"字符型"
					},{
						"value":"布尔型",
						"text":"布尔型"
					},{
						"value":"整型",
						"text":"整型"
					},{
						"value":"浮点型",
						"text":"浮点型"
					},{
						"value":"日期型",
						"text":"日期型"
					}],
					"panelHeight":"auto"
				}
			};
		$('#formalParam').datagrid({
			columns: [[{field: 'ck',checkbox:true },
			           {field: 'id',title: 'ID',width: 150,editor: "text" }, 
			           {field: 'index',title: '索引',width: 100,editor: "numberbox" },
			           {field: 'mode',title: '模式',width: 100,editor: modeEditor },
			           {field: 'type',title: '数据类型',width: 100,editor: typeEditor },
			           {field: 'note',title: '备注',width: 200,editor: "text" }]]
		});
		$("#formalBtn_add").unbind("click").bind("click",function(){
			var formalParam = createFormalParameter(xmlDoc,own.workflowProcess);
			var row = {id:"",index:"",mode:"",type:"",note:"",obj:formalParam};
			row.id = formalParam.getAttribute("id");
			row.index = formalParam.getAttribute("index");
			row.mode = transFormalParamMode2Chinese(formalParam.getAttribute("mode"));
			var dataType = getSubNode(formalParam,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = transDataType2Chinese(basicType.getAttribute("type"));
				}
			}
			var desc = getSubNode(formalParam,"Description");
			if(desc != null){
				for(var i=0;i<desc.childNodes.length;i++){
					var node = desc.childNodes[i];
					if(node.nodeName == "#text"){
						row.note = node.nodeValue;
						break;
					}
				}
			}
			$('#formalParam').datagrid("appendRow",row);
		});
		$("#formalBtn_del").unbind("click").bind("click",function(){
			var checked = $('#formalParam').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#formalParam').datagrid('getRowIndex',checked[i]);
				var node = checked[i].obj;
				node.parentNode.removeChild(node);
				$('#formalParam').datagrid('deleteRow',index);
			}
		});
		
		function updateFormalParam(row){
			var formalParam = row.obj;
			formalParam.setAttribute("id",row.id);
			formalParam.setAttribute("index",row.index);
			formalParam.setAttribute("mode",transFormalParamMode2English(row.mode));
			var dataType = getSubNode(formalParam,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					basicType.setAttribute("type",transDataType2English(row.type));
				}
			}
			var desc = getSubNode(formalParam,"Description");
			if(desc == null){
				if(row.note != ""){
					desc = xmlDoc.createElement("Description");
					formalParam.appendChild(desc);
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}else{
				removeAllSubNode(desc);
				if(row.note != ""){
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}
		};
		
		
		var formalParams = getFormalParameters(own.workflowProcess);
		for(var i=0;i<formalParams.length;i++){
			var fp = formalParams[i];
			var row = {id:"",index:"",mode:"",type:"",note:"",obj:fp};
			row.id = fp.getAttribute("id");
			row.index = fp.getAttribute("index");
			row.mode = transFormalParamMode2Chinese(fp.getAttribute("mode"));
			var dataType = getSubNode(fp,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = transDataType2Chinese(basicType.getAttribute("type"));
				}
			}
			var desc = getSubNode(fp,"Description");
			if(desc != null){
				for(var j=0;j<desc.childNodes.length;j++){
					var node = desc.childNodes[j];
					if(node.nodeName == "#text"){
						row.note = node.nodeValue;
						break;
					}
				}
			}
			$('#formalParam').datagrid("appendRow",row);
		}
		
		$('#formalParam').datagrid({
			onClickCell:onClickCell,
			onAfterEdit:function(index,row){ 
				updateFormalParam(row);
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true;};
			 if ($('#formalParam').datagrid('validateRow', editIndex)){
				 $('#formalParam').datagrid('endEdit', editIndex);
				 editIndex = undefined;
				 return true;
			 } else {
				 return false;
			 }
		}

		function onClickCell(index, field){
			 if (endEditing()){
				 $('#formalParam').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				 editIndex = index;
				 $(document).unbind('.datagrid').bind('mousedown.datagrid', function(e){
					 var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
					 if (p.length){return;}
					 endEditing();
					 editIndex = undefined;
				 });
			 }
		}
	};
	
	//1：生成全部节点
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		var node = workflowProcess.childNodes[i];
		if(node.nodeName == "Activities"){
			for(var j=0;j<node.childNodes.length;j++){
				var node2 = node.childNodes[j];
				if(node2.nodeName == "Activity"){//节点
					var type = node2.getAttribute("type");
					var l_x = "";
					var l_y = "";
					for(var k= 0;k<node2.childNodes.length;k++){
						var node3 = node2.childNodes[k];
						if(node3.nodeName == "ExtendedAttributes"){
							for(var m=0;m<node3.childNodes.length;m++){
								var node4 = node3.childNodes[m];
								if(node4.nodeName == "ExtendedAttribute" && node4.getAttribute("name") == "CLFLOW_LOCATION_X"){
									l_x = node4.getAttribute("value");
								}else if(node4.nodeName == "ExtendedAttribute" && node4.getAttribute("name") == "CLFLOW_LOCATION_Y"){
									l_y = node4.getAttribute("value");
								}
							}
						}
					}
					new Rect(own,type,l_x,l_y,"","",node2);
				}
			}
			break;
		}
	}
	//2:生成流传线
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		var node = workflowProcess.childNodes[i];
		if(node.nodeName == "Transitions"){
			for(var j=0;j<node.childNodes.length;j++){
				var node2 = node.childNodes[j];
				if(node2.nodeName == "Transition"){
					var rect_from = node2.getAttribute("from");
					var rect_to = node2.getAttribute("to");
					var rect1 = null;
					var rect2 = null;
					for(var k =0;k<currentRects.length;k++){
						var rect = currentRects[k];
						var a = rect.getActivity();
						if(a.getAttribute("id") == rect_from){
							rect1 = rect;
						}else if(a.getAttribute("id") == rect_to){
							rect2 = rect;
						}
						if(rect1 != null && rect2 != null){
							new Path(rect1,rect2,own,node2);
							break;
						}
					}
				}
			}
			break;
		}
	}
	containerShow(["basicPropsContainer","formalParamContainer","dataFieldContainer"]);
	showProps();
	showDataFiled();
	showFormalParam();
};
var Rect = function(raphaelPaper,type,x,y,and,or,activity){
	var own = this;
	var xmlDoc = raphaelPaper.xmlDoc;
	var paper = raphaelPaper.getPaper();
	if(x == "" || y == ""){
		var p = raphaelPaper.getPoint();
		x = p.x; 
		y = p.y;
	}
	var rect = null;
	var text = null;
	var joinText = null;
	var splitText = null;
	if(type == "START"){
		rect = paper.rect(x,y,80,30,10);
		//rect = paper.image("/webcoflow-utf8/coflow/img/node/start.png",x,y,40,40);
		rect.attr({"fill":"#F6F7FF","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = {"x":eval(rect.attr("x")),"y":eval(rect.attr("y")),"width":eval(rect.attr("width")),"height":eval(rect.attr("height")),
				"x2":eval(rect.attr("x"))+eval(rect.attr("width")),"y2":eval(rect.attr("y"))+eval(rect.attr("height"))};
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "开始").attr("font-size",13);
	}else if(type == "FINISH"){
		rect = paper.rect(x,y,80,30,10);
		//rect = paper.image("/webcoflow-utf8/coflow/img/node/end.png",x,y,40,40);
		//rect.attr({"fill":"red","stroke":"#03689A","stroke-width":"2"});
		rect.attr({"fill":"#F6F7FF","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = {"x":eval(rect.attr("x")),"y":eval(rect.attr("y")),"width":eval(rect.attr("width")),"height":eval(rect.attr("height")),
				"x2":eval(rect.attr("x"))+eval(rect.attr("width")),"y2":eval(rect.attr("y"))+eval(rect.attr("height"))};
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "结束").attr("font-size",13);
	}else{
		rect = paper.rect(x,y,100,50);
		rect.attr({"fill":"#F6F7FF","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = {"x":eval(rect.attr("x")),"y":eval(rect.attr("y")),"width":eval(rect.attr("width")),"height":eval(rect.attr("height")),
				"x2":eval(rect.attr("x"))+eval(rect.attr("width")),"y2":eval(rect.attr("y"))+eval(rect.attr("height"))};
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "节点").attr("font-size",12);
		if(Raphael.vml){
			joinText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y+rectBBox.height/4, "合").attr("font-size",11);
			splitText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y2-rectBBox.height/4+5.5, "分").attr("font-size",11);
		}else{
			joinText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y+rectBBox.height/4-5.5, "合").attr("font-size",11);
			splitText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y2-rectBBox.height/4+5.5, "分").attr("font-size",11);
		}
	}
	if(activity == null){
		activity = createActivity(xmlDoc,raphaelPaper.workflowProcess.getAttribute("id"),type,x,y,and,or);
	}
	if(text != null){
		text.attr("text",activity.getAttribute("name"));
	}
	if(joinText != null){
		var transitionRestrictions = getSubNode(activity,"TransitionRestrictions");
		if(transitionRestrictions != null){
			var transitionRestriction = getSubNode(transitionRestrictions,"TransitionRestriction");
			if(transitionRestriction != null){
				var join = getSubNode(transitionRestriction,"Join");
				if(join != null){
					joinText.attr("text","合:"+transJoinAndSplit2Chinese(join.getAttribute("type")));
				}
			}
		}
	}
	if(splitText != null){
		var transitionRestrictions = getSubNode(activity,"TransitionRestrictions");
		if(transitionRestrictions != null){
			var transitionRestriction = getSubNode(transitionRestrictions,"TransitionRestriction");
			if(transitionRestriction != null){
				var split = getSubNode(transitionRestriction,"Split");
				if(split != null){
					splitText.attr("text","分:"+transJoinAndSplit2Chinese(split.getAttribute("type")));
				}
			}
		}
	}
	rect.drag(move, start, end);
	if(text != null){
		text.drag(move, start, end);
	}
	if(joinText != null){
		joinText.drag(move, start, end);
	}
	if(splitText != null){
		splitText.drag(move, start, end);
	}
	var ox = 0;
	var oy = 0;
	var cloneRect = null;
	function move(dx,dy){
		var lastX = cloneRect.ox+dx;
		var lastY = cloneRect.oy+dy;
		if(lastX >0 && lastX < eval(raphaelPaper.width)-100){
			cloneRect.attr({x:lastX});
		}
		if(lastY > 0 && lastY < eval(raphaelPaper.height)-50){
			cloneRect.attr({y:lastY});
		}
	};
	function start(dx,dy){
		$(paper).data("currNode",own);
		hideOtherFocus();
		rect.ox = eval(rect.attr("x"));
		rect.oy = eval(rect.attr("y"));
		ox = eval(rect.attr("x"));
		oy = eval(rect.attr("x"));
		resetI();
		rect.attr("opacity","0.5");
		if(cloneRect != null){
			cloneRect.remove();
			cloneRect = null;
		}
		cloneRect = rect.clone();
		cloneRect.ox = eval(rect.attr("x"));
		cloneRect.oy = eval(rect.attr("y"));
		cloneRect.toBack();
	};
	function end(){
		rect.attr({x:cloneRect.attr("x")});
		rect.attr({y:cloneRect.attr("y")});
		cloneRect.remove();
		cloneRect = null;
		resetI();
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<connections.length;i++){
			var  obj = connections[i];
			if(own == obj.rect1 || own == obj.rect2){
				obj.path.getNode().attr("path",getPathString(obj.rect1.getNode(),obj.rect2.getNode()));
			}
		}
		rect.attr("opacity","1");
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_X",rect.attr("x"));
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_Y",rect.attr("y"));
		var tab = $('#process_tabs').tabs("getSelected");
		autoCheckChange(tab);
	};
	this.rectNudgeByKey = function (direction,pixel){
		if(direction == "left"){
			var lastX = eval(rect.attr("x"))-pixel;
			if(lastX>0){
				rect.attr({x:lastX});
			}
		}else if(direction == "up"){
			var lastY = eval(rect.attr("y"))-pixel;
			if(lastY>0){
				rect.attr({y:lastY});
			}
		}else if(direction == "right"){
			var lastX = eval(rect.attr("x"))+pixel;
			if(lastX<eval(raphaelPaper.width)-eval(rect.attr("width"))){
				rect.attr({x:lastX});
			}
		}else if(direction == "down"){
			var lastY = eval(rect.attr("y"))+pixel;
			if(lastY<eval(raphaelPaper.height)-eval(rect.attr("height"))){
				rect.attr({y:lastY});
			}
		}
		resetI();
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<connections.length;i++){
			var  obj = connections[i];
			if(own == obj.rect1 || own == obj.rect2){
				obj.path.getNode().attr("path",getPathString(obj.rect1.getNode(),obj.rect2.getNode()));
			}
		}
		rect.attr("opacity","1");
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_X",rect.attr("x"));
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_Y",rect.attr("y"));
		var tab = $('#process_tabs').tabs("getSelected");
		autoCheckChange(tab);
	};
	
	var attr = {"fill":"#000","stroke":"#fff","stroke-width":"2"};
	var h = 5;
	var i = {};//边角点 l=left,t=top,r=right,b=bottom
	i.lt = paper.rect(0,0,h,h).attr(attr).hide();//左上
	i.t = paper.rect(0,0,h,h).attr(attr).hide();//上
	i.rt = paper.rect(0,0,h,h).attr(attr).hide();//右上
	i.r = paper.rect(0,0,h,h).attr(attr).hide();//右
	i.rb = paper.rect(0,0,h,h).attr(attr).hide();//右下
	i.b = paper.rect(0,0,h,h).attr(attr).hide();//下
	i.lb = paper.rect(0,0,h,h).attr(attr).hide();//左下
	i.l = paper.rect(0,0,h,h).attr(attr).hide();//左
	var i_path = paper.path("M"+(i.lt.attr("x")+h/2)+" "+(i.lt.attr("y")+h/2)+"L"+(i.t.attr("x")+h/2)+" "+(i.t.attr("y")+h/2)
			+"L"+(i.rt.attr("x")+h/2)+" "+(i.rt.attr("y")+h/2)+"L"+(i.r.attr("x")+h/2)+" "+(i.r.attr("y")+h/2)
			+"L"+(i.rb.attr("x")+h/2)+" "+(i.rb.attr("y")+h/2)+"L"+(i.b.attr("x")+h/2)+" "+(i.b.attr("y")+h/2)
			+"L"+(i.lb.attr("x")+h/2)+" "+(i.lb.attr("y")+h/2)+"L"+(i.l.attr("x")+h/2)+" "+(i.l.attr("y")+h/2)
			+"L"+(i.lt.attr("x")+h/2)+" "+(i.lt.attr("y")+h/2));
	var clickArray = new Array();
	clickArray.push(rect.node);
	if(text!= null){
		clickArray.push(text.node);
	}
	if(joinText != null){
		clickArray.push(joinText.node);
	}
	if(splitText != null){
		clickArray.push(splitText.node);
	}
	$(clickArray).unbind("click").bind("click",function(){
		hideOtherFocus(); 
		resetI();
		$(paper).data("currNode",own);
		if($(paper).data("mod") == "path"){
			var len = raphaelPaper.pathWhere.length;
			if(len == 0){
				raphaelPaper.pathWhere[0] = own;
				raphaelPaper.tmpPath = paper.path("M0 0L0 0").attr({"arrow-end":"open-narrow-midium","stroke-width":"2"});
				$(document).unbind("mousemove").bind("mousemove",function(e){
					var bbox = {"x":eval(rect.attr("x")),"y":eval(rect.attr("y")),"width":eval(rect.attr("width")),"height":eval(rect.attr("height")),
							"x2":eval(rect.attr("x"))+eval(rect.attr("width")),"y2":eval(rect.attr("y"))+eval(rect.attr("height"))};
					var p = {x:bbox.x+bbox.width/2,y:bbox.y2};
					var destX = raphaelPaper.getPoint().x;
					if(destX>p.x){//以避免鼠标点击在绘制的箭头上
						destX -= 2;
					}else{
						destX += 2;
					}
					var pathString = "M"+p.x+" "+p.y+"L"+destX+" "+raphaelPaper.getPoint().y;
					raphaelPaper.tmpPath.attr({path:pathString});
				});
			}else if(raphaelPaper.pathWhere[0] != own){
				raphaelPaper.tmpPath.remove();
				$(document).unbind("mousemove");
				new Path(raphaelPaper.pathWhere[0],own,raphaelPaper,null);
				raphaelPaper.pathWhere = [];
			}
		}
		$(document).unbind("keydown").bind("keydown",function(e){raphaelPaper.keyFun(e);});
		$(document).unbind("click").bind("click",function(){
			if(raphaelPaper.tmpPath != ""){
				raphaelPaper.tmpPath.remove();
				raphaelPaper.pathWhere = [];
				$(document).unbind("mousemove");
			}
		});
		showContainer();
		showProps();
		showImplTools();
		showImplSubFlow();
		return false;
	});
	function show(){
		for(var o in i){
			i[o].show();
			i[o].toFront();
		}
		i_path.show();
	};
	this.hide = function(){
		for ( var o in i) {
			i[o].hide();
		}
		i_path.hide();
	};
	function removeI(){
		for ( var o in i) {
			i[o].remove();
		}
		i_path.remove();
	};
	function hideOtherFocus(){
		var currentRects = raphaelPaper.getRects();
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<currentRects.length;i++){
			if(currentRects[i] != own){
				currentRects[i].hide();
			}
		}
		for(var i=0;i<connections.length;i++){
			connections[i].path.removePoints();
		}
	}
	function resetI(){
		var margin = 4;
		var bbox = {"x":eval(rect.attr("x")),"y":eval(rect.attr("y")),"width":eval(rect.attr("width")),"height":eval(rect.attr("height")),
				"x2":eval(rect.attr("x"))+eval(rect.attr("width")),"y2":eval(rect.attr("y"))+eval(rect.attr("height"))};
		q = {
				x : bbox.x - margin,
				y : bbox.y - margin,
				width : bbox.width + margin * 2,
				height : bbox.height + margin * 2
			};
		if(text != null){
			text.attr({"x":bbox.x + bbox.width/2,"y":bbox.y + bbox.height/ 2});
		}
		if(joinText != null){
			if(Raphael.vml){
				joinText.attr({"x":bbox.x + bbox.width/2,"y":bbox.y+bbox.height/4});
			}else{
				joinText.attr({"x":bbox.x + bbox.width/2,"y":bbox.y+bbox.height/4-5.5});
			}
		}
		if(splitText != null){
			if(Raphael.vml){
				splitText.attr({"x":bbox.x + bbox.width/2,"y":bbox.y2-bbox.height/4+5.5});
			}else{
				splitText.attr({"x":bbox.x + bbox.width/2,"y":bbox.y2-bbox.height/4+5.5});
			}
		}
		var margin = 1;
		i.t.attr({
			x : q.x + q.width / 2 - h / 2,
			y : q.y - h / 2 - margin
		});
		i.lt.attr({
			x : q.x - h / 2 - margin,
			y : q.y - h / 2 - margin
		});
		i.l.attr({
			x : q.x - h / 2 - margin,
			y : q.y - h / 2 + q.height / 2
		});
		i.lb.attr({
			x : q.x - h / 2 -margin,
			y : q.y - h / 2 + q.height + margin
		});
		i.b.attr({
			x : q.x - h / 2 + q.width / 2,
			y : q.y - h / 2 + q.height + margin
		});
		i.rb.attr({
			x : q.x - h / 2 + q.width +margin,
			y : q.y - h / 2 + q.height+margin
		});
		i.r.attr({
			x : q.x - h / 2 + q.width+margin,
			y : q.y - h / 2 + q.height / 2
		});
		i.rt.attr({
			x : q.x - h / 2 + q.width+margin,
			y : q.y - h / 2-margin
		});
		i_path.attr("path","M"+(i.lt.attr("x")+h/2)+" "+(i.lt.attr("y")+h/2)+"L"+(i.t.attr("x")+h/2)+" "+(i.t.attr("y")+h/2)
				+"L"+(i.rt.attr("x")+h/2)+" "+(i.rt.attr("y")+h/2)+"L"+(i.r.attr("x")+h/2)+" "+(i.r.attr("y")+h/2)
				+"L"+(i.rb.attr("x")+h/2)+" "+(i.rb.attr("y")+h/2)+"L"+(i.b.attr("x")+h/2)+" "+(i.b.attr("y")+h/2)
				+"L"+(i.lb.attr("x")+h/2)+" "+(i.lb.attr("y")+h/2)+"L"+(i.l.attr("x")+h/2)+" "+(i.l.attr("y")+h/2)
				+"L"+(i.lt.attr("x")+h/2)+" "+(i.lt.attr("y")+h/2));
		//i_path.toFront();
		show();
	};
	this.getNode = function(){
		return rect;
	};
	this.getActivity = function(){
		return activity;
	};
	this.remove = function(){
		rect.remove();
		if(text != null){
			text.remove();
		}
		if(joinText != null){
			joinText.remove();
		}
		if(splitText != null){
			splitText.remove();
		}
		activity.parentNode.removeChild(activity);
		removeI();
		var connections = raphaelPaper.getConnections();
		var cloneConnections = [];
		for(var i=0;i<connections.length;i++){
			cloneConnections.push(connections[i]);
		}
		for(var i=0;i<cloneConnections.length;i++){
			var path = cloneConnections[i].path;
			var rect1 = cloneConnections[i].rect1;
			var rect2 = cloneConnections[i].rect2;
			if(rect == rect1.getNode() || rect == rect2.getNode()){
				path.remove();
			}
		}
		raphaelPaper.getRects().remove(own);
	};
	raphaelPaper.getRects().push(own);
	function showContainer(){
		for(var i=0;i<activity.childNodes.length;i++){
			var node = activity.childNodes[i];
			if(node.nodeName == "Implementation"){
				var n = "Tool";
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "No"){
						n = "No";
						break;
					}else if(node2.nodeName == "Tool"){
						n = "Tool";
						break;
					}else if(node2.nodeName == "SubFlow"){
						n = "SubFlow";
						break;
					}
				}
				if(n == "No"){
					containerShow(["basicPropsContainer"]);
				}else if(n == "Tool"){
					containerShow(["basicPropsContainer","implToolContainer"]);
				}else if(n == "SubFlow"){
					containerShow(["basicPropsContainer","implSubflowContainer"]);
				}else{
					containerShow(["basicPropsContainer"]);
				}
			}
		}
	}
	
	/**
	 * 实现子流程
	 */
	function showImplSubFlow(){
		$('#implSubflow').datagrid('loadData',{total:0,rows:[]});
		var idEditor = {
				type : 'combobox',
				options:{
					data:[],
					"panelHeight":"auto"
				}
		};
		var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"异步",
						"text":"异步"
					},{
						"value":"同步",
						"text":"同步"
					}],
					"panelHeight":"auto"
				}
		};
		var actualParamEditor = {
				type:"actualParambox",
				options:{
					workflowProcess:raphaelPaper.workflowProcess,
					xmlDoc:xmlDoc,
					flag:"subflow",
					subflow:null
				}
		};
		$('#implSubflow').datagrid({
			columns: [[{field: 'id',title: 'ID',width: 150,editor: idEditor,readonly:true }, 
			           {field: 'type',title: '类型',width: 150,editor: typeEditor },
			           {field: 'actualParam',title: '实参',width: 200,editor: actualParamEditor }]]
		});
		var row = {id:"",type:"同步",actualParam:""};
		var subflow = getActivitySubFlow(activity);
		var actualParams = [];
		if(subflow != null){
			var subflowId  = subflow.getAttribute("id");
			var workflow = selectNode(xmlDoc,xmlDoc,"//WorkflowProcess[@id='"+subflowId+"']");
			if(workflow != null){
				row.id = workflow.getAttribute("name");
			}else{
				row.id = subflow.getAttribute("id");
			}
			row.type = transSubFlowType2Chinese(subflow.getAttribute("execution"));
			for(var i=0;i<subflow.childNodes.length;i++){
				var node = subflow.childNodes[i];
				if(node.nodeName == "ActualParameters"){
					for(var j=0;j<node.childNodes.length;j++){
						var node2 = node.childNodes[j];
						if(node2.nodeName == "ActualParameter"){
							for(var k =0;k<node2.childNodes.length;k++){
								var node3 = node2.childNodes[k];
								if(node3.nodeName == "#text"){
									actualParams.push(transActualParam2Chinese(node3.nodeValue,raphaelPaper.workflowProcess));
									break;
								}
							}
						}
					}
					break;
				}
			}
		}
		row.actualParam = actualParams.join(",");
		$('#implSubflow').datagrid('appendRow',row);
		$('#implSubflow').datagrid({
			onClickCell:onClickCell,
			onBeforeEdit:function(index,row){
				currentActualParm = "";//清空当前实参对象
				var ws = getWorkflowProcesses(xmlDoc);
				var datas = [];
				if(ws != null){
					for(var i=0;i<ws.length;i++){
						var id = ws[i].getAttribute("id");
						var name = ws[i].getAttribute("name");
						if(id != raphaelPaper.workflowProcess.getAttribute("id")){
							datas.push({"value":name,"text":name});
						}
					}
				}
				idEditor.options.data = datas;
				oldValue = row["actualParam"];
			},
			onAfterEdit:function(index,row){ 
				if(subflow == null){
					subflow = createSubFlow(xmlDoc,activity);
				}
				updateSubFlow(subflow,row);
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		function updateSubFlow(subflow,row){
			subflow.setAttribute("execution",transSubFlowType2English(row.type));
			subflow.setAttribute("id",transSubFlowId2English(row.id==null?"":row.id,xmlDoc));
			for(var i=0;i<subflow.childNodes.length;i++){
				if(subflow.childNodes[i].nodeName == "ActualParameters" && oldValue != row["actualParam"]){
					var aps = subflow.childNodes[i];
					var as = [];
					if(currentActualParm != ""){
						as = currentActualParm.split(",");
					}
					var len = aps.childNodes.length;
					for(var j=0;j<len;j++){
						aps.removeChild(aps.childNodes[0]);
					}
					for(var j=0;j<as.length;j++){
						if(as[j] == "")
							continue;
						var act = xmlDoc.createElement("ActualParameter");
						act.appendChild(xmlDoc.createTextNode(as[j]));
						aps.appendChild(act);
					}
				}
			}
		}
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true;};
			 if ($('#implSubflow').datagrid('validateRow', editIndex)){
				 $('#implSubflow').datagrid('endEdit', editIndex);
				 editIndex = undefined;
				 return true;
			 } else {
				 return false;
			 }
		}
		var lastCellFlag = undefined;
		function onClickCell(index, field){
			 if (endEditing()){
				 var row = $('#implSubflow').datagrid('getRows')[index];
					if(typeof(row)==undefined || row == null){
						return;
					}
					if(typeof(row.id) == undefined || row.id == null || row.id == ""){
						if(field != "id" && lastCellFlag =="id"){
							alert("请先选择ID项");
							return;
						}
					}
				var subflowDom = selectNode(xmlDoc,xmlDoc,"//WorkflowProcess[@name='"+row.id+"']");
				actualParamEditor.options.subflow = subflowDom;
				 $('#implSubflow').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				 editIndex = index;
				 lastCellFlag = field;
				 $(document).unbind('.datagrid').bind('mousedown.datagrid', function(e){
					 var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
					 if (p.length){return;}
					 endEditing();
					 editIndex = undefined;
				 });
			 }
		}
	}
	
	/**
	 * 实现工具
	 */
	function showImplTools(){
		$('#implTool').datagrid('loadData',{total:0,rows:[]});
		var idEditor = {
				type : 'combobox',
				options:{
					data:(function(){
						var opts = [];
						var apps = getApplications(xmlDoc);
						//清空全局变量
						currentToolIds = [];
						for(var i=0;i<apps.length;i++){
							var id = apps[i].getAttribute("id");
							var name = apps[i].getAttribute("name");
							opts.push({"value":name,"text":name});
							//应用id和name加入到全局变量中
							currentToolIds.push({"id":id,"name":name});
							
						}
						return opts;
					})(),
					"panelHeight":"auto"
				}
		};
		var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"过程",
						"text":"过程"
					},{
						"value":"页面",
						"text":"页面"
					}],
					"panelHeight":"auto"
				}
		};
		
		var actualParamEditor = {
			type:"actualParambox",
			options:{
				workflowProcess:raphaelPaper.workflowProcess,
				xmlDoc:xmlDoc,
				flag:"tool",
				application:null
			}
		};
		$('#implTool').datagrid({
			columns: [[{field: 'ck',checkbox:true },
			           {field: 'id',title: 'ID',width: 150,editor: idEditor }, 
			           {field: 'type',title: '类型',width: 150,editor: typeEditor },
			           {field: 'actualParam',title: '实参',width: 200,editor: actualParamEditor },
			           {field: 'note',title: '备注',width: 200,editor: "text" }]]
		});
		
		var tools = getActivityTools(activity);
		for(var i=0;i<tools.length;i++){
			var tool = tools[i];
			var row = {id:transToolId2Chinese(tool.getAttribute("id"),currentToolIds),type:transImplType2Chinese(tool.getAttribute("type")),actualParam:"",note:"",obj:tool};
			var actualParams = [];
			for(var j=0;j<tool.childNodes.length;j++){
				var node = tool.childNodes[j];
				if(node.nodeName == "ActualParameters"){
					for(var k=0;k<node.childNodes.length;k++){
						var  node2 = node.childNodes[k];
						if(node2.nodeName == "ActualParameter"){
							for(var m=0;m<node2.childNodes.length;m++){
								var node3 = node2.childNodes[m];
								if(node3.nodeName == "#text"){
									actualParams.push(transActualParam2Chinese(node3.nodeValue,raphaelPaper.workflowProcess));
									break;
								}
							}
						}
					}
					row.actualParam = actualParams.join(",");
				}else if(node.nodeName == "Description"){
					for(var k=0;k<node.childNodes.length;k++){
						var node2 = node.childNodes[k];
						if(node2.nodeName == "#text"){
							row.note =node2.nodeValue;
							break;
						}
					} 
					
				}
			}
			$('#implTool').datagrid('appendRow',row);
		};
		 
		$('#implTool').datagrid({
			onClickCell:onClickCell,
			onBeforeEdit:function(index,row){
				currentActualParm = "";//清空当前实参对象
				oldValue = row["actualParam"];
			},
			onAfterEdit:function(index,row){ 
				updateTool(row);
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		$("#implToolBtn_add").unbind("click").bind("click",function(){
			var tool = createTool(xmlDoc,activity);
			var row = {id:"",type:"",actualParam:"",note:"",obj:tool};
			$('#implTool').datagrid('appendRow',row);
		});
		$("#implToolBtn_del").unbind("click").bind("click",function(){
			var checked = $('#implTool').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#implTool').datagrid('getRowIndex',checked[i]);
				var node = checked[i].obj;
				node.parentNode.removeChild(node);
				$('#implTool').datagrid('deleteRow',index);
			}
		});
		
		
		function updateTool(row){
			var tool = row.obj;
			tool.setAttribute("type",transImplType2English(row.type==null?"":row.type));
			tool.setAttribute("id",transToolId2English(row.id==null?"":row.id,currentToolIds));
			var hasDesc = false;
			for(var i=0;i<tool.childNodes.length;i++){
				if(tool.childNodes[i].nodeName == "ActualParameters" && oldValue != row["actualParam"]){
					var aps = tool.childNodes[i];
					var as =[];
					if(currentActualParm != "")
						as = currentActualParm.split(",");
					var len = aps.childNodes.length;
					for(var j=0;j<len;j++){
						aps.removeChild(aps.childNodes[0]);
					}
					for(var j=0;j<as.length;j++){
						var act = xmlDoc.createElement("ActualParameter");
						act.appendChild(xmlDoc.createTextNode(as[j]));
						aps.appendChild(act);
					}
				}else if(tool.childNodes[i].nodeName == "Description"){
					hasDesc = true;
					removeAllSubNode(tool.childNodes[i]);
					if(row.note != ""){
						tool.childNodes[i].appendChild(xmlDoc.createTextNode(row.note));
					}
				}
			}
			if(!hasDesc && row.note != ""){
				var desc = tool.appendChild(xmlDoc.createElement("Description"));
				desc.appendChild(xmlDoc.createTextNode(row.note));
			}
			
		}
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true;};
			 if ($('#implTool').datagrid('validateRow', editIndex)){
				 $('#implTool').datagrid('endEdit', editIndex);
				 editIndex = undefined;
				 return true;
			 } else {
				 return false;
			 }
		}
		var lastCellFlag = undefined;
		function onClickCell(index, field){
			 if (endEditing()){
				 var row = $('#implTool').datagrid('getRows')[index];;
					if(typeof(row)==undefined || row == null){
						return;
					}
					if(typeof(row.id) == undefined || row.id == null || row.id == ""){
						if(field != "id" && lastCellFlag =="id"){
							alert("请先选择ID项");
							return;
						}
					}
				 var app = selectNode(xmlDoc,xmlDoc,".//Application[@name='"+row.id+"']");
				 actualParamEditor.options.application = app;
				 $('#implTool').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
				 editIndex = index;
				 lastCellFlag = field;
				 $(document).unbind('.datagrid').bind('mousedown.datagrid', function(e){
					 var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
					 if (p.length){return;}
					 endEditing();
					 editIndex = undefined;
				 });
			 }
		}
		 
		
	};
	
	/**
	 * 节点基本属性
	 */
	function showProps(){
		$('#basicProps').datagrid('loadData',{total:0,rows:[]}); 
		var rows = [];
		var dispatchTypeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"抢占",
						"text":"抢占"
					},{
						"value":"顺序",
						"text":"顺序"
					},{
						"value":"无序",
						"text":"无序"
					}],
					"panelHeight":"auto"
				}
		};
		var limitUnitEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"月",
						"text":"月"
					},{
						"value":"日",
						"text":"日"
					},{
						"value":"时",
						"text":"时"
					}],
					"panelHeight":"auto"
				}
		};
		var implEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"无",
						"text":"无"
					},{
						"value":"工具",
						"text":"工具"
					},{
						"value":"子流程",
						"text":"子流程"
					}],
					"panelHeight":"auto"
				}
		};
		var startModeEditor = finishModeEditor= {
				type : 'combobox',
				options:{
					data:[{
						"value":"自动",
						"text":"自动"
					},{
						"value":"手动",
						"text":"手动"
					}],
					"panelHeight":"auto"
				}
		};
		var joinEditor = splitEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"与",
						"text":"与"
					},{
						"value":"或",
						"text":"或"
					}],
					"panelHeight":"auto"
				}
		};
		var showNextPageEditor = performerControlEditor = freeInEditor = freeOutEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"是",
						"text":"是"
					},{
						"value":"否",
						"text":"否"
					}],
					"panelHeight":"auto"
				}
		};
		var selectParticipantEditor = {
				type : "participantSelect",
				options:{
					xmlDoc:xmlDoc
				}
		};
		var props = [{key:"row_id",name:"ID",editor:"text"},
		             {key:"row_name",name:"名称",editor:"text"},
		             {key:"row_desc",name:"描述",editor:"text"},
		             {key:"row_performer",name:"默认操作者",editor:selectParticipantEditor},
		             {key:"row_optional_performer",name:"可选操作者",editor:selectParticipantEditor},
		             {key:"row_performerControl",name:"人员控制",editor:performerControlEditor},
		             {key:"row_dispatchType",name:"分派类型",editor:dispatchTypeEditor},
		             {key:"row_limitCount",name:"期限数量",editor:"text"},{key:"row_limitCount",name:"期限单位",editor:limitUnitEditor},
		             {key:"row_impl",name:"实现",editor:implEditor},{key:"row_startMode",name:"开始模式",editor:startModeEditor},
		             {key:"row_finishMode",name:"结束模式",editor:finishModeEditor},{key:"row_join",name:"合类型",editor:joinEditor},
		             {key:"row_split",name:"分类型",editor:splitEditor},{key:"row_showNextPage",name:"显示一步页面",editor:showNextPageEditor},
		             {key:"row_maxLine",name:"最大选择路线",editor:"numberbox"},{key:"row_minLine",name:"最小选择路线",editor:"numberbox"},
		             {key:"row_freeIn",name:"自由流入",editor:freeInEditor},{key:"row_freeOut",name:"自由流出",editor:freeOutEditor}
		             ];
		//1:ID
		var id = activity.getAttribute("id");
		var row_Id = {name:"ID",value:id,editor:'text'};
		rows.push(row_Id);
		//2:名称
		var name = activity.getAttribute("name");
		var row_name = {name:"名称",value:name,editor:'text'};
		rows.push(row_name);
		for(var i=0;i<activity.childNodes.length;i++){
			var node = activity.childNodes[i];
			if(node.nodeName == "Description"){//描述
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_desc = {name:"描述",value:node2.nodeValue,editor:'text'};
						rows.push(row_desc);
						break;
					}
				}
			}else if(node.nodeName == "Performer"){//默认操作者
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						//var row_performer = {name:"操作者",value:node2.nodeValue,editor:selectParticipantEditor};
						var row_performer = {name:"默认操作者",value:initPerformer(xmlDoc,node2.nodeValue),editor:selectParticipantEditor};
						rows.push(row_performer);
						break;
					}
				}
			}else if(node.nodeName == "OptionalPerformer"){//可选操作者
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						//var row_performer = {name:"操作者",value:node2.nodeValue,editor:selectParticipantEditor};
						var row_optional_performer = {name:"可选操作者",value:initPerformer(xmlDoc,node2.nodeValue),editor:selectParticipantEditor};
						rows.push(row_optional_performer);
						break;
					}
				}
			}else if(node.nodeName == "PerformerChanageFlag"){//人员控制
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_performerControl = {name:"人员控制",value:transTrueAndFalse2Chinese(node2.nodeValue),editor:performerControlEditor};
						rows.push(row_performerControl);
						break;
					}
				}
			}else if(node.nodeName == "DispatchType"){//分派类型
				//var row_dispatchType = {name:"分派类型",value:node.getAttribute("type"),editor:dispatchTypeEditor};
				var row_dispatchType = {name:"分派类型",value:transDispatchType2Chinese(node.getAttribute("type")),editor:dispatchTypeEditor};
				rows.push(row_dispatchType);
			}else if(node.nodeName == "Limit"){
				//期限数量
				var count = node.getAttribute("count");
				if(count){
					var row_limitCount = {name:"期限数量",value:count,editor:"text"};
					rows.push(row_limitCount);
				}
				//期限单位
				var unit = node.getAttribute("unit");
				if(unit){
					//var row_limitUnit = {name:"期限单位",value:unit,editor:limitUnitEditor};
					var row_limitUnit = {name:"期限单位",value:transUnit2Chinese(unit),editor:limitUnitEditor};
					rows.push(row_limitUnit);
				}
			}else if(node.nodeName == "Implementation"){
				var n = "Tool";
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "No"){
						n = "No";
						break;
					}else if(node2.nodeName == "Tool"){
						n = "Tool";
						break;
					}else if(node2.nodeName == "SubFlow"){
						n = "SubFlow";
						break;
					}
				}
				var row_impl ={name:"实现",value:transImpl2Chinese(n),editor:implEditor};
				rows.push(row_impl);
			}else if(node.nodeName == "StartMode"){//开始模式
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[0];
					if(node2.nodeName == "#text"){
						var row_startMode = {name:"开始模式",value:transStartAndFinishMode2Chinese(node2.nodeValue),editor:startModeEditor};
						rows.push(row_startMode);
						break;
					}
				}
			}else if(node.nodeName == "FinishMode"){//结束模式
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_finishMode = {name:"结束模式",value:transStartAndFinishMode2Chinese(node2.nodeValue),editor:finishModeEditor};
						rows.push(row_finishMode);
						break;
					}
				}
			}else if(node.nodeName == "TransitionRestrictions"){
				var transitionRestriction = getSubNode(node,"TransitionRestriction");
				if(transitionRestriction != null){
					var join = getSubNode(transitionRestriction,"Join");
					if(join != null){
						//合类型
						var row_join = {name:"合类型",value:transJoinAndSplit2Chinese(join.getAttribute("type")),editor:joinEditor};
						rows.push(row_join);
					}
					var split = getSubNode(transitionRestriction,"Split");
					if(split != null){
						//分类型
						var row_split = {name:"分类型",value:transJoinAndSplit2Chinese(split.getAttribute("type")),editor:splitEditor};
						rows.push(row_split);
					}
				}
			}else if(node.nodeName == "ExtendedAttributes"){
				for(var j=0;j<node.childNodes.length;j++){
					var n = node.childNodes[j];
					if(n.nodeName == "ExtendedAttribute"){
						if(n.getAttribute("name") == "CLFLOW_SHOW_NEXT_PAGE"){
							//显示一步页面
							var row_showNextPage = {name:"显示一步页面",value:transTrueAndFalse2Chinese(n.getAttribute("value")),editor:showNextPageEditor};
							rows.push(row_showNextPage);
						}else if(n.getAttribute("name") == "CLFLOW_MAXLINE"){
							//最大选择路线
							var row_maxLine = {name:"最大选择路线",value:n.getAttribute("value"),editor:"numberbox"};
							rows.push(row_maxLine);
						}else if(n.getAttribute("name") == "CLFLOW_MINLINE"){
							//最小选择路线
							var row_minLine = {name:"最小选择路线",value:n.getAttribute("value"),editor:"numberbox"};
							rows.push(row_minLine);
						}else if(n.getAttribute("name") == "FREEIN"){
							//自由流入
							var row_freeIn = {name:"自由流入",value:transTrueAndFalse2Chinese(n.getAttribute("value")),editor:freeInEditor};
							rows.push(row_freeIn);
						}else if(n.getAttribute("name") == "FREEOUT"){
							//自由流出
							var row_freeOut = {name:"自由流出",value:transTrueAndFalse2Chinese(n.getAttribute("value")),editor:freeOutEditor};
							rows.push(row_freeOut);
						}
					}
				}
				
			}
		}
		for(var i=0;i<props.length;i++){
			var hasElement = false;
			for(var j=0;j<rows.length;j++){
				if(props[i].name == rows[j].name){
					hasElement = true;
					$('#basicProps').propertygrid('appendRow',rows[j]);
					
					break;
				}
			}
			if(!hasElement){
				var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
				$('#basicProps').propertygrid('appendRow',tempRow);
			}
		}
		$('#basicProps').propertygrid({
			onBeforeEdit:function(index,row){
				if (0 === index && checkRunning()) {
					pwin.dhtmlx.message("运行中的流程，节点ID不能修改！");
					return false;
				}
				oldValue = row.value;
			},
		    onAfterEdit:function(index,row){
		    	if(row.value == oldValue){
		    		return;
		    	}
		    	switch (index) {
				case 0:
					if(checkAttr(xmlDoc,"id",row.value)){
						$.messager.alert('Warning','ID已经存在!');
						return;
					}
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','ID不能为空!');
						return;
					}
					var srcId = activity.getAttribute("id");
					var destId = row.value;
					updateAcivity("Activity","id",row.value);
					triggerIdChange(xmlDoc, "Activity", srcId, destId);
					break;
				case 1:
					if(checkAttr(xmlDoc,"name",row.value)){
						$.messager.alert('Warning','名称已经存在!');
						return;
					}
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','名称不能为空!');
						return;
					}
					updateAcivity("Activity","name",row.value);
					if(text != null){
						text.attr("text",row.value);
					}
					break;
				case 2:
					updateAcivity("Description","",row.value);
					break;
				case 3:
					updateAcivity("Performer","",currentPerformer);
					break;
				case 4:
					updateAcivity("OptionalPerformer","",currentPerformer);
					break;
				case 5:
					updateAcivity("PerformerChanageFlag","",transTrueAndFalse2English(row.value));
					break;
				case 6:
					updateAcivity("DispatchType","type",transDispatchType2English(row.value));
					break;
				case 7:
					updateAcivity("Limit","count",row.value);
					break;
				case 8:
					updateAcivity("Limit","unit",transUnit2English(row.value));
					break;
				case 9:
					updateAcivity("Implementation","",transImpl2English(row.value));
					if(transImpl2English(row.value) == "Tool"){
						containerShow(["basicPropsContainer","implToolContainer"]);
					}else if(transImpl2English(row.value) == "SubFlow"){
						containerShow(["basicPropsContainer","implSubflowContainer"]);
					}else{
						containerShow(["basicPropsContainer"]);
					}
					break;
				case 10:
					updateAcivity("StartMode","",transStartAndFinishMode2English(row.value));
					break;
				case 11:
					updateAcivity("FinishMode","",transStartAndFinishMode2English(row.value));
					break;
				case 12:
					updateAcivity("Join","type",transJoinAndSplit2English(row.value));
					if(joinText != null){
						joinText.attr("text","合:"+row.value);
					}
					break;
				case 13:
					updateAcivity("Split","type",transJoinAndSplit2English(row.value));
					if(splitText != null){
						splitText.attr("text","分:"+row.value);
					}
					break;
				case 14:
					updateAcivity("ExtendedAttribute","CLFLOW_SHOW_NEXT_PAGE",transTrueAndFalse2English(row.value));
					break;
				case 15:
					updateAcivity("ExtendedAttribute","CLFLOW_MAXLINE",row.value);
					break;
				case 16:
					updateAcivity("ExtendedAttribute","CLFLOW_MINLINE",row.value);
					break;
				case 17:
					updateAcivity("ExtendedAttribute","FREEIN",transTrueAndFalse2English(row.value));
					break;
				case 18:
					updateAcivity("ExtendedAttribute","FREEOUT",transTrueAndFalse2English(row.value));
					break;
				default:
					break;
				}
		    	//alert(index);
		        //row.editing = false;     
		        //$('#tt').datagrid('refreshRow', index);  
		    	var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
		    }    
		});
		
	}
	
	function updateAcivity(nodeName,key,value){
		var updateLevelOneTextNode = function(nodeName,value){
			var hasElement = false;
			for(var i=0;i<activity.childNodes.length;i++){
				if(activity.childNodes[i].nodeName == nodeName){
					hasElement = true;
					var node = activity.childNodes[i];
					var len = node.childNodes.length;
					for(var j=0;j<len;j++){
						node.removeChild(node.childNodes[0]);
					}
					node.appendChild(xmlDoc.createTextNode(value));
					break;
				}
			}
			if(!hasElement){
				var node = xmlDoc.createElement(nodeName);
				node.appendChild(xmlDoc.createTextNode(value));
				activity.appendChild(node);
			}
		};
		var updateLevelOneAttrNode = function(nodeName,key,value){
			var hasElement = false;
			for(var i=0;i<activity.childNodes.length;i++){
				if(activity.childNodes[i].nodeName == nodeName){
					hasElement = true;
					if(value != "")
						activity.childNodes[i].setAttribute(key,value);
					break;
				}
			}
			if(!hasElement && value != ""){
				var node = xmlDoc.createElement(nodeName);
				node.setAttribute(key,value);
				activity.appendChild(node);
			}
		};
		if(nodeName == "Activity"){
			if(key == "id"){
				activity.setAttribute("id",value);
			}else if(key == "name"){
				activity.setAttribute("name",value);
			}
		}else if(nodeName == "Description" || nodeName == "Performer" || nodeName == "OptionalPerformer" || nodeName == "PerformerChanageFlag" || nodeName == "StartMode" || nodeName == "FinishMode"){
			updateLevelOneTextNode(nodeName,value);
		}else if(nodeName == "DispatchType" || nodeName == "Limit"){
			updateLevelOneAttrNode(nodeName,key,value);
		}else if(nodeName == "Implementation"){
			var node = getSubNode(activity,nodeName);
			var len = node.childNodes.length;
			for(var i=0;i<len;i++){
				node.removeChild(node.childNodes[0]);
			}
			if(value == "No"){
				var no = xmlDoc.createElement("No");
				node.appendChild(no);
			}else if(value == "Tool"){
				
			}else if(value == "SubFlow"){
				
			}
		}else if(nodeName == "Join" || nodeName == "Split"){
			var node = getSubNode(activity,"TransitionRestrictions");
			node = getSubNode(node,"TransitionRestriction");
			node = getSubNode(node,nodeName);
			node.setAttribute(key,value);
		}else if(nodeName == "ExtendedAttribute"){
			var node = getSubNode(activity,"ExtendedAttributes");
			var hasElement = false;
			for(var i=0;i<node.childNodes.length;i++){
				if(node.childNodes[i].nodeName == "ExtendedAttribute"){
					if(node.childNodes[i].getAttribute("name") == key){
						node.childNodes[i].setAttribute("value",value);
						hasElement = true;
						break;
					}
				}
			}
			if(!hasElement){
				var extNode = xmlDoc.createElement(nodeName);
				extNode.setAttribute("name",key);
				extNode.setAttribute("value",value);
				node.appendChild(extNode);
			}
		}
		
	};
	
};
var Path = function(rect1,rect2,raphaelPaper,transition){
	if(rect1 == rect2)
		return;
	var connections = raphaelPaper.getConnections();
	for(var i=0;i<connections.length;i++){
		if(connections[i].rect1 == rect1 &&  connections[i].rect2 == rect2){
			return;
		}
	}
	
	var own = this;
	var xmlDoc = raphaelPaper.xmlDoc;
	var paper = raphaelPaper.getPaper();
	var path = paper.path(getPathString(rect1.getNode(),rect2.getNode())).attr({"arrow-end":"open-narrow-long","stroke-width":"2","cursor":"pointer"});
	path.attr({"arrow-end":"open-narrow-short","stroke":"#909090","stroke-width":"2","cursor":"pointer"});
	var fromActivity = rect1.getActivity();
	var toActivity = rect2.getActivity();
	
	if(transition == null){
		transition = createTransition(xmlDoc,raphaelPaper.workflowProcess.getAttribute("id"),fromActivity.getAttribute("id"),toActivity.getAttribute("id"));
	}
	$(path.node).bind("click",function(){
		own.showPoints(rect1.getNode(),rect2.getNode());
		$(paper).data("currNode",own);
		hideOtherFocus();
		containerShow(["basicPropsContainer"]);
		showProps();
		$(document).unbind("keydown").bind("keydown",function(e){raphaelPaper.keyFun(e);});
		return false;
	});
	var rollBackNode = selectNode(xmlDoc,transition,"ExtendedAttributes/ExtendedAttribute[@name='CLFLOW_ROLLBACK']");
	if(rollBackNode != null){
		if(rollBackNode.getAttribute("value") == "TRUE"){
			path.attr("stroke","#FF0000");
		}
	}
	function showProps(){
		$('#basicProps').datagrid('loadData',{total:0,rows:[]}); 
		var rollBackEditor = manualEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"是",
						"text":"是"
					},{
						"value":"否",
						"text":"否"
					}],
					"panelHeight":"auto"
				}
			};
		var props = [{key:"row_id",name:"ID",editor:"text"},
		             {key:"row_name",name:"名称",editor:"text"},
		             {key:"row_desc",name:"描述",editor:"text"},
		             {key:"row_from",name:"源节点"},
		             {key:"row_to",name:"目标节点"},
		             {key:"row_condition",name:"条件",editor:"text"},
		             {key:"row_rollBack",name:"回退",editor:rollBackEditor},
		             {key:"row_manual",name:"人工干预",editor:manualEditor}
		             ];
		var rows = [];
		var row_id = {name:"ID",value:transition.getAttribute("id"),editor:'text'};
		rows.push(row_id);
		var row_name = {name:"名称",value:transition.getAttribute("name"),editor:'text'};
		rows.push(row_name);
		var desc = getSubNode(transition,"Description");
		if(desc != null){
			for(var i=0;i<desc.childNodes.length;i++){
				var node = desc.childNodes[i];
				if(node.nodeName == "#text"){
					var row_desc = {name:"描述",value:node.nodeValue,editor:'text'};
					rows.push(row_desc);
					break;
				}
			}
		}
		var exts = getSubNode(transition,"ExtendedAttributes");
		if(exts != null){
			for(var i=0;i<exts.childNodes.length;i++){
				var node = exts.childNodes[i];
				if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_ROLLBACK"){
					var row_rollBack = {name:"回退",value:transTrueAndFalse2Chinese(node.getAttribute("value")),editor:rollBackEditor};
					rows.push(row_rollBack);
				}else if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_MANUAL"){
					var row_manual = {name:"人工干预",value:transTrueAndFalse2Chinese(node.getAttribute("value")),editor:manualEditor};
					rows.push(row_manual);
				}
			}
		}
		var condition = getSubNode(transition,"Condition");
		if(condition != null){
			for(var i=0;i<condition.childNodes.length;i++){
				var node = condition.childNodes[i];
				if(node.nodeName == "#cdata-section"){
					var row_condition = {name:"条件",value:node.nodeValue,editor:'text'};
					rows.push(row_condition);
					break;
				}
			}
		}
		var fromActivityId = transition.getAttribute("from");
		var from_Activity = selectNode(xmlDoc,raphaelPaper.workflowProcess,"Activities/Activity[@id='"+fromActivityId+"']");
		var row_from = {name:"源节点",value:fromActivityId};
		if(from_Activity != null){
			row_from.value = from_Activity.getAttribute("name");
		}
		rows.push(row_from);
		var toActivityId = transition.getAttribute("to");
		var to_Activity = selectNode(xmlDoc,raphaelPaper.workflowProcess,"Activities/Activity[@id='"+toActivityId+"']");
		var row_to = {name:"目标节点",value:toActivityId};
		if(to_Activity != null){
			row_to.value = to_Activity.getAttribute("name");
		}
		rows.push(row_to);
		for(var i=0;i<props.length;i++){
			var hasElement = false;
			for(var j=0;j<rows.length;j++){
				if(props[i].name == rows[j].name){
					hasElement = true;
					$('#basicProps').propertygrid('appendRow',rows[j]);
					break;
				}
			}
			if(!hasElement){
				var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
				$('#basicProps').propertygrid('appendRow',tempRow);
			}
		}
		$('#basicProps').propertygrid({
			onBeforeEdit:function(index,row){
				oldValue = row.value;
			},
		    onAfterEdit:function(index,row){
		    	if(row.value == oldValue){
		    		return;
		    	}
		    	switch (index) {
				case 0:
					if(checkAttr(xmlDoc,"id",row.value)){
						$.messager.alert('Warning','ID已经存在!');
						return;
					}
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','ID不能为空!');
						return;
					}
					transition.setAttribute("id",row.value);//ID
					break;
				case 1:
					if($.trim(row.value) == ""){
						$.messager.alert('Warning','名称不能为空!');
						return;
					}
					if(checkAttr(xmlDoc,"name",row.value)){
						$.messager.alert('Warning','名称已经存在!');
						return;
					}
					transition.setAttribute("name",row.value);//名称
					break;
				case 2://描述
					var hasElement = false;
					for(var i=0;i<transition.childNodes.length;i++){
						var node = transition.childNodes[i];
						if(node.nodeName == "Description"){
							hasElement = true;
							removeAllSubNode(node);
							if(row.value != ""){
								node.appendChild(xmlDoc.createTextNode(row.value));
							}
							break;
						}
					}
					if(!hasElement && row.value != ""){
						var desc = xmlDoc.createElement("Description");
						desc.appendChild(xmlDoc.createTextNode(row.value));
						transition.appendChild(desc);
					}
					break;
				case 3://源节点
					//transition.setAttribute("from",row.value);
					break;
				case 4://目标节点
					//transition.setAttribute("to",row.value);
					break;
				case 5://条件
					var hasElement = false;
					for(var i=0;i<transition.childNodes.length;i++){
						var node = transition.childNodes[i];
						if(node.nodeName == "Condition"){
							hasElement = true;
							removeAllSubNode(node);
							if(row.value != ""){
								node.appendChild(xmlDoc.createCDATASection(row.value));
							}else{
								transition.removeChild(node);
							}
							break;
						}
					}
					if(!hasElement){
						if(row.value != ""){
							var condition = xmlDoc.createElement("Condition");
							condition.appendChild(xmlDoc.createCDATASection(row.value));
							transition.appendChild(condition);
						}
					}
					break;
				case 6://回退
					var hasElement = false;
					for(var i=0;i<transition.childNodes.length;i++){
						var node = transition.childNodes[i];
						if(node.nodeName == "ExtendedAttributes"){
							for(var j=0;j<node.childNodes.length;j++){
								var node2 = node.childNodes[j];
								if(node2.nodeName == "ExtendedAttribute" && node2.getAttribute("name") == "CLFLOW_ROLLBACK"){
									node2.setAttribute("value",transTrueAndFalse2English(row.value));
									hasElement = true;
									break;
								}
							}
							break;
						}
					}
					if(!hasElement){
						for(var i=0;i<transition.childNodes.length;i++){
							var node = transition.childNodes[i];
							if(node.nodeName == "ExtendedAttributes"){
								var e = xmlDoc.createElement("ExtendedAttribute");
								e.setAttribute("name","CLFLOW_ROLLBACK");
								e.setAttribute("value",transTrueAndFalse2English(row.value));
								node.appendChild(e);
								break;
							}
						}
					}
					if(row.value == "TRUE"){
						path.attr("stroke","#FF0000");
					}else{
						path.attr("stroke","#000000");
					}
					break;
				case 7://人工干预
					var hasElement = false;
					for(var i=0;i<transition.childNodes.length;i++){
						var node = transition.childNodes[i];
						if(node.nodeName == "ExtendedAttributes"){
							for(var j=0;j<node.childNodes.length;j++){
								var node2 = node.childNodes[j];
								if(node2.nodeName == "ExtendedAttribute" && node2.getAttribute("name") == "CLFLOW_MANUAL"){
									node2.setAttribute("value",transTrueAndFalse2English(row.value));
									hasElement = true;
									break;
								}
							}
							break;
						}
					}
					if(!hasElement){
						for(var i=0;i<transition.childNodes.length;i++){
							var node = transition.childNodes[i];
							if(node.nodeName == "ExtendedAttributes"){
								var e = xmlDoc.createElement("ExtendedAttribute");
								e.setAttribute("name","CLFLOW_MANUAL");
								e.setAttribute("value",transTrueAndFalse2English(row.value));
								node.appendChild(e);
								break;
							}
						}
					}
					break;
				default:
					break;
				}
		    	var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
		    }
		});
	
	}
	
	function hideOtherFocus(){
		var currentRects = raphaelPaper.getRects();
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<currentRects.length;i++){
			currentRects[i].hide();
		}
		for(var i=0;i<connections.length;i++){
			if(connections[i].path != own)
				connections[i].path.removePoints();
		}
	}
	var points = [];
	this.showPoints = function(rect1,rect2){
		this.removePoints();
		var bbox1 = {"x":eval(rect1.attr("x")),"y":eval(rect1.attr("y")),"width":eval(rect1.attr("width")),"height":eval(rect1.attr("height")),
				"x2":eval(rect1.attr("x"))+eval(rect1.attr("width")),"y2":eval(rect1.attr("y"))+eval(rect1.attr("height"))};
		var bbox2 = {"x":eval(rect2.attr("x")),"y":eval(rect2.attr("y")),"width":eval(rect2.attr("width")),"height":eval(rect2.attr("height")),
				"x2":eval(rect2.attr("x"))+eval(rect2.attr("width")),"y2":eval(rect2.attr("y"))+eval(rect2.attr("height"))};
		var p1 = {x:bbox1.x+bbox1.width/2,y:bbox1.y2};
		var p2 = {x:bbox2.x+bbox2.width/2,y:bbox2.y};
		var h = 6;
		var attr = {"fill":"#000","stroke":"#fff","stroke-width":"2"};
		if(p1.y <= p2.y){
			var rect1 = paper.rect(p1.x-h/2,p1.y-h/2,h,h).attr(attr);
			var rect2 = paper.rect(p1.x-h/2,p1.y+(p2.y-p1.y)/4,h,h).attr(attr);
			var rect3 = paper.rect(p1.x+(p2.x-p1.x)/2,p1.y+(p2.y-p1.y)/2-h/2,h,h).attr(attr);
			var rect4 = paper.rect(p2.x-h/2,p2.y-(p2.y-p1.y)/4,h,h).attr(attr);
			var rect5 = paper.rect(p2.x-h/2,p2.y,h,h).attr(attr);
			points.push(rect1);
			points.push(rect2);
			points.push(rect3);
			points.push(rect4);
			points.push(rect5);
		}else{
			var rect1 = paper.rect(p1.x-h/2,p1.y,h,h).attr(attr);
			var rect2 = paper.rect(p1.x+(p2.x-p1.x)/4,p1.y+10-h/2,h,h).attr(attr);
			var rect3 = paper.rect(p1.x+(p2.x-p1.x)/2-h/2,p1.y+(p2.y-p1.y)/2,h,h).attr(attr);
			var rect4 = paper.rect(p2.x-(p2.x-p1.x)/4,p2.y-10-h/2,h,h).attr(attr);
			var rect5 = paper.rect(p2.x-h/2,p2.y,h,h).attr(attr);
			points.push(rect1);
			points.push(rect2);
			points.push(rect3);
			points.push(rect4);
			points.push(rect5);
		}
	};
	this.removePoints = function(){
		for(var i=0;i<points.length;i++){
			points[i].remove();
		}
		points = [];
	};
	this.remove = function(){
		path.remove();
		this.removePoints();
		transition.parentNode.removeChild(transition);
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<connections.length;i++){
			if(connections[i].path == own){
				raphaelPaper.getConnections().remove(connections[i]);
				break;
			}
		}
	};
	this.getNode = function(){
		return path;
	};
	this.getTransition = function(){
		return transition;
	};
	raphaelPaper.getConnections().push({path:own,rect1:rect1,rect2:rect2});
};
var showPackageProps = function(packageObj,treeNode){
	$('#basicProps').datagrid('loadData',{total:0,rows:[]});
	if(packageObj == null){
		containerShow([]);
	}else{
		var treeObj = $.fn.zTree.getZTreeObj("packageView");
		containerShow(["basicPropsContainer"]);
		var nodeName = packageObj.nodeName;
		var xmlDoc = packageObj.ownerDocument;
		switch (nodeName) {
		case "Package":
			var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"JAVA",
						"text":"JAVA"
					},{
						"value":"JAVASCRIPT",
						"text":"JAVASCRIPT"
					}],
					"panelHeight":"auto"
				}
			};
			var props = [{key:"row_id",name:"ID",editor:"text"},{key:"row_name",name:"名称",editor:"text"},
			             {key:"row_desc",name:"描述",editor:"text"},
			             {key:"row_createDate",name:"创建日期",editor:"text"},
			             {key:"row_vendor",name:"供应商",editor:"text"},
			             {key:"row_author",name:"作者",editor:"text"},
			             {key:"row_scriptType",name:"脚本类型",editor:typeEditor}
			             ];
			var rows = [];
			var row_id = {name:"ID",value:packageObj.getAttribute("id"),editor:'text'};
			rows.push(row_id);
			var row_name = {name:"名称",value:packageObj.getAttribute("name"),editor:'text'};
			rows.push(row_name);
			
			for(var i=0;i<packageObj.childNodes.length;i++){
				var node = packageObj.childNodes[i];
				if(node.nodeName == "PackageHeader"){
					for(var j=0;j<node.childNodes.length;j++){
						var node2 = node.childNodes[j];
						if(node2.nodeName == "Created"){
							for(var k=0;k<node2.childNodes.length;k++){
								var node3 = node2.childNodes[k];
								if(node3.nodeName == "#text"){
									var row_createDate = {name:"创建日期",value:node3.nodeValue,editor:'text'};
									rows.push(row_createDate);
									break;
								}
							}
						}else if(node2.nodeName == "Vendor"){
							for(var k=0;k<node2.childNodes.length;k++){
								var node3 = node2.childNodes[k];
								if(node3.nodeName == "#text"){
									var row_vendor = {name:"供应商",value:node3.nodeValue,editor:'text'};
									rows.push(row_vendor);
									break;
								}
							}
						}else if(node2.nodeName == "Author"){
							for(var k=0;k<node2.childNodes.length;k++){
								var node3 = node2.childNodes[k];
								if(node3.nodeName == "#text"){
									var row_author = {name:"作者",value:node3.nodeValue,editor:'text'};
									rows.push(row_author);
									break;
								}
							}
						}else if(node2.nodeName == "Description"){
							for(var k=0;k<node2.childNodes.length;k++){
								var node3 = node2.childNodes[k];
								if(node3.nodeName == "#text"){
									var row_desc = {name:"描述",value:node3.nodeValue,editor:'text'};
									rows.push(row_desc);
									break;
								}
							}
						}
					}
				}else if(node.nodeName == "Script"){
					var row_scriptType = {name:"脚本类型",value:node.getAttribute("type"),editor:typeEditor};
					rows.push(row_scriptType);
				}
			}
			for(var i=0;i<props.length;i++){
				var hasElement = false;
				for(var j=0;j<rows.length;j++){
					if(props[i].name == rows[j].name){
						hasElement = true;
						$('#basicProps').propertygrid('appendRow',rows[j]);
						break;
					}
				}
				if(!hasElement){
					var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
					$('#basicProps').propertygrid('appendRow',tempRow);
				}
			}
			$('#basicProps').propertygrid({
				onBeforeEdit:function(index,row){
					oldValue = row.value;
				},
			    onAfterEdit:function(index,row){
			    	if(row.value == oldValue){
			    		return;
			    	}
			    	switch (index) {
					case 0:
						if(checkAttr(xmlDoc,"id",row.value)){
							$.messager.alert('Warning','ID已经存在!');
							return;
						}
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','ID不能为空!');
							return;
						}
						packageObj.setAttribute("id",row.value);
						break;
					case 1:
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','名称不能为空!');
							return;
						}
						if(checkAttr(xmlDoc,"name",row.value)){
							$.messager.alert('Warning','名称已经存在!');
							return;
						}
						packageObj.setAttribute("name",row.value);
						treeNode.name = row.value;
						treeObj.updateNode(treeNode);
						break;
					case 2:
						var hasElement = false;
						var packageHeader = null;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "PackageHeader"){
								packageHeader = node; 
								for(var j=0;j<node.childNodes.length;j++){
									var node2 = node.childNodes[j];
									if(node2.nodeName == "Description"){
										hasElement = true;
										var len = node2.childNodes.length;
										for(var k=0;k<len;k++){
											node2.removeChild(node2.childNodes[0]);
										}
										node2.appendChild(xmlDoc.createTextNode(row.value));
										break;
									}
								}
								break;
							}
						}
						if(!hasElement && packageHeader != null){
							var desc = xmlDoc.createElement("Description");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageHeader.appendChild(desc);
						}
						break;
					case 3:
						var hasElement = false;
						var packageHeader = null;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "PackageHeader"){
								packageHeader = node; 
								for(var j=0;j<node.childNodes.length;j++){
									var node2 = node.childNodes[j];
									if(node2.nodeName == "Created"){
										hasElement = true;
										var len = node2.childNodes.length;
										for(var k=0;k<len;k++){
											node2.removeChild(node2.childNodes[0]);
										}
										node2.appendChild(xmlDoc.createTextNode(row.value));
										break;
									}
								}
								break;
							}
						}
						if(!hasElement && packageHeader != null){
							var desc = xmlDoc.createElement("Created");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageHeader.appendChild(desc);
						}
						break;
					case 4:
						var hasElement = false;
						var packageHeader = null;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "PackageHeader"){
								packageHeader = node; 
								for(var j=0;j<node.childNodes.length;j++){
									var node2 = node.childNodes[j];
									if(node2.nodeName == "Vendor"){
										hasElement = true;
										var len = node2.childNodes.length;
										for(var k=0;k<len;k++){
											node2.removeChild(node2.childNodes[0]);
										}
										node2.appendChild(xmlDoc.createTextNode(row.value));
										break;
									}
								}
								break;
							}
						}
						if(!hasElement && packageHeader != null){
							var desc = xmlDoc.createElement("Vendor");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageHeader.appendChild(desc);
						}
						break;
					case 5:
						var hasElement = false;
						var packageHeader = null;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "PackageHeader"){
								packageHeader = node; 
								for(var j=0;j<node.childNodes.length;j++){
									var node2 = node.childNodes[j];
									if(node2.nodeName == "Author"){
										hasElement = true;
										var len = node2.childNodes.length;
										for(var k=0;k<len;k++){
											node2.removeChild(node2.childNodes[0]);
										}
										node2.appendChild(xmlDoc.createTextNode(row.value));
										break;
									}
								}
								break;
							}
						}
						if(!hasElement && packageHeader != null){
							var desc = xmlDoc.createElement("Author");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageHeader.appendChild(desc);
						}
						break;
					case 6:
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "Script"){
								hasElement = true;
								node.setAttribute("type",row.value);
							}
						}
						break;
					default:
						break;
					}
			    	var tab = $('#process_tabs').tabs("getSelected");
					autoCheckChange(tab);
			    }
			});
		
			break;
		case "Participant":
			var participantbox = {
				type:"participantbox",
				options:{
					xmlDoc:xmlDoc,
					participantType:null
				}
			};
			var props = [{key:"row_id",name:"ID",editor:"text"},{key:"row_name",name:"名称",editor:"text"},
			             {key:"row_desc",name:"描述",editor:"text"},
			             {key:"row_participant",name:"参与者",editor:participantbox},
			             {key:"row_type",name:"类型",editor:""}
			             ];
			var rows = [];
			var row_id = {name:"ID",value:packageObj.getAttribute("id"),editor:'text'};
			rows.push(row_id);
			var row_name = {name:"名称",value:packageObj.getAttribute("name"),editor:'text'};
			rows.push(row_name);
			
			var desc = getSubNode(packageObj,"Description");
			if(desc != null){
				var v = "";
				for(var i=0;i<desc.childNodes.length;i++){
					var node = desc.childNodes[0];
					if(node.nodeName == "#text"){
						var row_desc = {name:"描述",value:node.nodeValue,editor:'text'};
						rows.push(row_desc);
						break;
					}
				}
			}
			var type = getSubNode(packageObj,"ParticipantType");
			var typeValue = null;
			if(type != null){
				typeValue = type.getAttribute("type");
				var row_type = {name:"类型",value:transParticipantType2Chinese(type.getAttribute("type")),editor:""};
				rows.push(row_type);
			}
			var exts = getSubNode(packageObj,"ExtendedAttributes");
			if(typeValue == "HUMAN"){
				if(exts != null){
					var humanId = null;
					var humanName = null;
					for(var i=0;i<exts.childNodes.length;i++){
						var node = exts.childNodes[i];
						if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_HUMAN_ID"){
							humanId = node.getAttribute("value");
						}
						if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_HUMAN_NAME"){
							humanName = node.getAttribute("value");
						}
					}
					if(humanId != null && humanName != null){
						var v = humanId+":"+humanName;
						var row_participant = {name:"参与者",value:v,editor:participantbox};
						rows.push(row_participant);
					}
				}
			}else if(typeValue == "DEPARTMENT"){
				if(exts != null){
					var departId = null;
					var departName = null;
					for(var i=0;i<exts.childNodes.length;i++){
						var node = exts.childNodes[i];
						if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_DEPARTMENT_ID"){
							departId = node.getAttribute("value");
						}
						if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_DEPARTMENT_NAME"){
							departName = node.getAttribute("value");
						}
					}
					if(departId != null && departName != null){
						var v = departId+":"+departName;
						var row_participant = {name:"参与者",value:v,editor:participantbox};
						rows.push(row_participant);
					}
				}
			
			}else if(typeValue == "FORMULA"){
				if(exts != null){
					for(var i=0;i<exts.childNodes.length;i++){
						var node = exts.childNodes[i];
						if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_FORMUAL_EXPRESSION"){
							//var row_participant = {name:"参与者",value:node.getAttribute("value"),editor:participantbox};
							var row_participant = "";
							row_participant = {name:"参与者",value:initParticipantBox(xmlDoc,node.getAttribute("value"),typeValue),editor:participantbox};
							rows.push(row_participant);
							break;
						}
					}
				}
			}else if(typeValue == "SYSTEM"){
				if(exts != null){
					var len = exts.childNodes.length;
					for(var i=0;i<len;i++){
						exts.removeChild(exts.childNodes[0]);
					}
					var row_participant = {name:"参与者",value:"",editor:participantbox};
					rows.push(row_participant);
				}
			}
			for(var i=0;i<props.length;i++){
				var hasElement = false;
				for(var j=0;j<rows.length;j++){
					if(props[i].name == rows[j].name){
						hasElement = true;
						$('#basicProps').propertygrid('appendRow',rows[j]);
						break;
					}
				}
				if(!hasElement){
					var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
					$('#basicProps').propertygrid('appendRow',tempRow);
				}
			}
			$('#basicProps').unbind("typeUpdate").bind("typeUpdate",function(e,value){
				var type = getSubNode(packageObj,"ParticipantType");
				if(type != null){
					type.setAttribute("type",value);
				}else{
					type = xmlDoc.createElement("ParticipantType");
					type.setAttribute("type",value);
					packageObj.appendChild(type);
				}
			});
			$('#basicProps').propertygrid({
				onBeforeEdit:function(index,row){
					oldValue = row.value;
					if(index == 3){
						var typeRow = $('#basicProps').propertygrid("getRows")[index+1];
						if(typeRow != null)participantbox.options.participantType = transParticipantType2English(typeRow.value);
					}
				},
			    onAfterEdit:function(index,row){
			    	if(row.value == oldValue){
			    		return;
			    	}
			    	switch (index) {
					case 0:
						if(checkAttr(xmlDoc,"id",row.value)){
							$.messager.alert('Warning','ID已经存在!');
							return;
						}
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','ID不能为空!');
							return;
						}
						var srcId = packageObj.getAttribute("id");
						var destId = row.value;
						packageObj.setAttribute("id",row.value);
						triggerIdChange(xmlDoc, "Participant", srcId, destId);
						break;
					case 1:
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','名称不能为空!');
							return;
						}
						if(checkAttr(xmlDoc,"name",row.value)){
							$.messager.alert('Warning','名称已经存在!');
							return;
						}
						packageObj.setAttribute("name",row.value);
						treeNode.name = row.value;
						treeObj.updateNode(treeNode);
						break;
					case 2:
						var hasElement = false;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "Description"){
								hasElement = true;
								var len = node.childNodes.length;
								for(var j=0;j<len;j++){
									node.removeChild(node.childNodes[0]);
								}
								node.appendChild(xmlDoc.createTextNode(row.value));
								break;
							}
						}
						if(!hasElement){
							var desc = xmlDoc.createElement("Description");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageObj.appendChild(desc);
						}
						break;
					case 3:
						var exts = getSubNode(packageObj,"ExtendedAttributes");
						if(exts != null){
							var participantType = getSubNode(packageObj,"ParticipantType");
							var type = participantType.getAttribute("type");
							removeAllSubNode(exts);
							if(type == "HUMAN"){
								if(row != ""){
									var rowArray = row.value.split(":");
									var id = rowArray[0];
									var name = rowArray[1];
									var ext = xmlDoc.createElement("ExtendedAttribute");
									ext.setAttribute("name","CLFLOW_HUMAN_ID");
									ext.setAttribute("value",id);
									exts.appendChild(ext);
									ext = xmlDoc.createElement("ExtendedAttribute");
									ext.setAttribute("name","CLFLOW_HUMAN_NAME");
									ext.setAttribute("value",name);
									exts.appendChild(ext);
								}
							}else if(type == "DEPARTMENT"){
								if(row != ""){
									var rowArray = row.value.split(":");
									var id = rowArray[0];
									var name = rowArray[1];
									var ext = xmlDoc.createElement("ExtendedAttribute");
									ext.setAttribute("name","CLFLOW_DEPARTMENT_ID");
									ext.setAttribute("value",id);
									exts.appendChild(ext);
									ext = xmlDoc.createElement("ExtendedAttribute");
									ext.setAttribute("name","CLFLOW_DEPARTMENT_NAME");
									ext.setAttribute("value",name);
									exts.appendChild(ext);
								}
							}else if(type == "FORMULA"){
								var ext = xmlDoc.createElement("ExtendedAttribute");
								ext.setAttribute("name","CLFLOW_FORMUAL_EXPRESSION");
								//ext.setAttribute("value",row.value);
								ext.setAttribute("value",currentParticipant);
								exts.appendChild(ext);
							}else if(type == "SYSTEM"){
							}
						}
						break;
					default:
						break;
					}
			    	var tab = $('#process_tabs').tabs("getSelected");
					autoCheckChange(tab);
			    }
			});
			break;
		case "Application":
			containerShow(["basicPropsContainer","formalParamContainer"]);
			var props = [{key:"row_id",name:"ID",editor:"text"},{key:"row_name",name:"名称",editor:"text"},
			             {key:"row_desc",name:"描述",editor:"text"},
			             {key:"row_resource",name:"资源",editor:"text"}
			             ];
			var rows = [];
			var row_id = {name:"ID",value:packageObj.getAttribute("id"),editor:'text'};
			rows.push(row_id);
			var row_name = {name:"名称",value:packageObj.getAttribute("name"),editor:'text'};
			rows.push(row_name);
			for(var i=0;i<packageObj.childNodes.length;i++){
				var node = packageObj.childNodes[i];
				if(node.nodeName == "Description"){
					for(var j=0;j<node.childNodes.length;j++){
						var node2 = node.childNodes[j];
						if(node2.nodeName == "#text"){
							var row_desc = {name:"描述",value:node2.nodeValue,editor:'text'};
							rows.push(row_desc);
							break;
						}
					}
				}
			}
			var row_resource = {name:"资源",value:packageObj.getAttribute("resource"),editor:"text"};
			rows.push(row_resource);
			for(var i=0;i<props.length;i++){
				var hasElement = false;
				for(var j=0;j<rows.length;j++){
					if(props[i].name == rows[j].name){
						hasElement = true;
						$('#basicProps').propertygrid('appendRow',rows[j]);
						break;
					}
				}
				if(!hasElement){
					var tempRow = {name:props[i].name,value:"",editor:props[i].editor};
					$('#basicProps').propertygrid('appendRow',tempRow);
				}
			}
			$('#basicProps').propertygrid({
				onBeforEdit:function(index,row){
					oldValue = row.value;
				},
			    onAfterEdit:function(index,row){
			    	if(row.value == oldValue){
			    		return;
			    	}
			    	switch (index) {
					case 0:
						if(checkAttr(xmlDoc,"id",row.value)){
							$.messager.alert('Warning','ID已经存在!');
							return;
						}
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','ID不能为空!');
							return;
						}
						var srcId = packageObj.getAttribute("id");
						var destId = row.value;
						packageObj.setAttribute("id",row.value);
						triggerIdChange(xmlDoc, "Application", srcId, destId);
						break;
					case 1:
						if($.trim(row.value) == ""){
							$.messager.alert('Warning','名称不能为空!');
							return;
						}
						if(checkAttr(xmlDoc,"name",row.value)){
							$.messager.alert('Warning','名称已经存在!');
							return;
						}
						packageObj.setAttribute("name",row.value);
						treeNode.name = row.value;
						treeObj.updateNode(treeNode);
						break;
					case 2:
						var hasElement = false;
						for(var i=0;i<packageObj.childNodes.length;i++){
							var node = packageObj.childNodes[i];
							if(node.nodeName == "Description"){
								hasElement = true;
								var len = node.childNodes.length;
								for(var j=0;j<len;j++){
									node.removeChild(node.childNodes[0]);
								}
								node.appendChild(xmlDoc.createTextNode(row.value));
								break;
							}
						}
						if(!hasElement){
							var desc = xmlDoc.createElement("Description");
							desc.appendChild(xmlDoc.createTextNode(row.value));
							packageObj.appendChild(desc);
						}
						break;
					case 3:
						packageObj.setAttribute("resource",row.value);
						break;
					default:
						break;
					}
			    	var tab = $('#process_tabs').tabs("getSelected");
					autoCheckChange(tab);
			    }
			});
		
			
			//-----应用程序形参

			$('#formalParam').datagrid('loadData',{total:0,rows:[]});
			var modeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"输入",
						"text":"输入"
					},{
						"value":"输入输出",
						"text":"输入输出"
					},{
						"value":"输出",
						"text":"输出"
					}],
					"panelHeight":"auto"
				}
			};
			var typeEditor = {
					type : 'combobox',
					options:{
						data:[{
							"value":"字符型",
							"text":"字符型"
						},{
							"value":"布尔型",
							"text":"布尔型"
						},{
							"value":"整型",
							"text":"整型"
						},{
							"value":"浮点型",
							"text":"浮点型"
						},{
							"value":"日期型",
							"text":"日期型"
						}],
						"panelHeight":"auto"
					}
				};
			$('#formalParam').datagrid({
				columns: [[{field: 'ck',checkbox:true },
				           {field: 'id',title: 'ID',width: 150,editor: "text" }, 
				           {field: 'index',title: '索引',width: 100,editor: "numberbox" },
				           {field: 'mode',title: '模式',width: 100,editor: modeEditor },
				           {field: 'type',title: '数据类型',width: 100,editor: typeEditor },
				           {field: 'note',title: '备注',width: 200,editor: "text" }]]
			});
			$("#formalBtn_add").unbind("click").bind("click",function(){
				var formalParam = createFormalParameter(xmlDoc,packageObj);
				var row = {id:"",index:"",mode:"",type:"",note:"",obj:formalParam};
				row.id = formalParam.getAttribute("id");
				row.index = formalParam.getAttribute("index");
				row.mode = transFormalParamMode2Chinese(formalParam.getAttribute("mode"));
				for(var i=0;i<formalParam.childNodes.length;i++){
					var node = formalParam.childNodes[i];
					if(node.nodeName == "DataType"){
						for(var j=0;j<node.childNodes.length;j++){
							var node2 = node.childNodes[j];
							if(node2.nodeName == "BasicType"){
								row.type = transDataType2Chinese(node2.getAttribute("type"));
								break;
							}
						}
						break;
					}
				}
				var desc = getSubNode(formalParam,"Description");
				if(desc != null){
					for(var i=0;i<desc.childNodes.length;i++){
						var node = desc.childNodes[i];
						if(node.nodeName == "#text"){
							row.note = node.nodeValue;
							break;
						}
					}
				}
				$('#formalParam').datagrid("appendRow",row);
			});
			$("#formalBtn_del").unbind("click").bind("click",function(){
				var checked = $('#formalParam').datagrid('getChecked');
				for(var i=0;i<checked.length;i++){
					var index = $('#formalParam').datagrid('getRowIndex',checked[i]);
					var node = checked[i].obj;
					node.parentNode.removeChild(node);
					$('#formalParam').datagrid('deleteRow',index);
				}
			});
			
			function updateFormalParam(row){
				var formalParam = row.obj;
				formalParam.setAttribute("id",row.id);
				formalParam.setAttribute("index",row.index);
				formalParam.setAttribute("mode",transFormalParamMode2English(row.mode));
				for(var i=0;i<formalParam.childNodes.length;i++){
					var node = formalParam.childNodes[i];
					if(node.nodeName == "DataType"){
						for(var j=0;j<node.childNodes.length;j++){
							var node2 = node.childNodes[j];
							if(node2.nodeName == "BasicType"){
								node2.setAttribute("type",transDataType2English(row.type));
								break;
							}
						}
						break;
					}
				}
				var desc = getSubNode(formalParam,"Description");
				if(desc == null){
					desc = xmlDoc.createElement("Description");
					formalParam.appendChild(desc);
				}else{
					var len = desc.childNodes.length;
					for(var i=0;i<len;i++){
						desc.removeChild(desc.childNodes[0]);
					}
				}
				desc.appendChild(xmlDoc.createTextNode(row.note));
			};
			
			
			var formalParams = getFormalParameters(packageObj);
			for(var i=0;i<formalParams.length;i++){
				var fp = formalParams[i];
				var row = {id:"",index:"",mode:"",type:"",note:"",obj:fp};
				row.id = fp.getAttribute("id");
				row.index = fp.getAttribute("index");
				row.mode = transFormalParamMode2Chinese(fp.getAttribute("mode"));
				for(var j=0;j<fp.childNodes.length;j++){
					var node = fp.childNodes[j];
					if(node.nodeName == "DataType"){
						for(var k=0;k<node.childNodes.length;k++){
							var node2 = node.childNodes[k];
							if(node2.nodeName == "BasicType"){
								row.type = transDataType2Chinese(node2.getAttribute("type"));
								break;
							}
						}
						break;
					}
				}
				var desc = getSubNode(fp,"Description");
				if(desc != null){
					for(var j=0;j<desc.childNodes.length;j++){
						var node = desc.childNodes[j];
						if(node.nodeName == "#text"){
							row.note = node.nodeValue;
							break;
						}
					}
				}
				$('#formalParam').datagrid("appendRow",row);
			}
			
			$('#formalParam').datagrid({
				onClickCell:onClickCell,
				onAfterEdit:function(index,row){ 
					updateFormalParam(row);
					var tab = $('#process_tabs').tabs("getSelected");
					autoCheckChange(tab);
				}
			});
			break;
		case "WorkflowProcess":
			
			break;
		default:
			break;
		}
	}
	var editIndex = undefined;
	function endEditing (){
		 if (editIndex == undefined){return true;};
		 if ($('#formalParam').datagrid('validateRow', editIndex)){
			 $('#formalParam').datagrid('endEdit', editIndex);
			 editIndex = undefined;
			 return true;
		 } else {
			 return false;
		 }
	}

	function onClickCell(index, field){
		 if (endEditing()){
			 $('#formalParam').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
			 editIndex = index;
			 $(document).unbind('.datagrid').bind('mousedown.datagrid', function(e){
				 var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
				 if (p.length){return;}
				 endEditing();
				 editIndex = undefined;
			 });
		 }
	}
};
var triggerIdChange = function(xmlDoc, type, srcId, destId){
	var rootNode = xmlDoc.getElementsByTagName("Package")[0];
	if(type == "WorkflowProcess"){
		//修改id改变影响到子流程id
		var subflows = selectNodes(xmlDoc,rootNode,"WorkflowProcesses/WorkflowProcess/Activities/Activity/Implementation/SubFlow[@id='"+srcId+"']");
		if(subflows != null){
			for(var i=0;i<subflows.length;i++){
				var subflow = subflows[i];
				subflow.setAttribute("id",destId);
			}
		}
	}else if(type == "Activity"){
		//节点id的改变会影响到参与者以及流传对节点id的引用
		var ps = getSubNode(rootNode,"Participants");
		if(ps != null){
			for(var i=0;i<ps.childNodes.length;i++){
				var node = ps.childNodes[i];
				if(node.nodeName == "Participant"){
					var pariticipantTypeNode = getSubNode(node,"ParticipantType");
					if(pariticipantTypeNode != null){
						var pariticipantType = pariticipantTypeNode.getAttribute("type");
						if(pariticipantType == "FORMULA"){
							var extsNode = getSubNode(node,"ExtendedAttributes");
							if(extsNode != null){
								for(var j=0;j<extsNode.childNodes.length;j++){
									var node2 = extsNode.childNodes[j];
									if(node2.nodeName == "ExtendedAttribute" && node2.getAttribute("name") == "CLFLOW_FORMUAL_EXPRESSION"){
										var srcValue = node2.getAttribute("value");
										var destValue = srcValue.replaceAll(srcId,destId);
										node2.setAttribute("value",destValue);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		var ts =  xmlDoc.getElementsByTagName("Transition");
		if(ts != null){
			for(var i=0;i<ts.length;i++){
				var fromValue = ts[i].getAttribute("from");
				var toValue = ts[i].getAttribute("to");
				if(fromValue == srcId){
					ts[i].setAttribute("from",destId);
				}
				if(toValue == srcId){
					ts[i].setAttribute("to",destId);
				}
			}
		}
	}else if(type == "Participant"){
		//参与者id修改影响到节点的参与者
		var as = xmlDoc.getElementsByTagName("Activity");
		if(as != null){
			for(var i=0;i<as.length;i++){
				var performerNode = getSubNode(as[i],"Performer");
				if(performerNode != null){
					for(var j=0;j<performerNode.childNodes.length;j++){
						var node = performerNode.childNodes[j];
						if(node.nodeName == "#text"){
							var srcValue = node.nodeValue;
							var destValue = srcValue.replaceAll(srcId,destId);
							var parentNode = node.parentNode;
							parentNode.removeChild(node);
							parentNode.appendChild(xmlDoc.createTextNode(destValue));
							break;
						}
					}
				}
			}
		}
	}else if(type == "Application"){
		//应用程序id改变影响到实现工具
		var ts = xmlDoc.getElementsByTagName("Tool");
		if(ts != null){
			for(var i=0;i<ts.length;i++){
				if(ts[i].getAttribute("id") == srcId){
					ts[i].setAttribute("id",destId);
				}
			}
		}
	}
};

var PreRect = function(){
	
}