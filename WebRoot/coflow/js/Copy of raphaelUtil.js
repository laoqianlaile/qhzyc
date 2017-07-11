var getPathString = function(rect1,rect2){
		var bbox1 = rect1.getBBox();
		var bbox2 = rect2.getBBox();
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
	var paper = Raphael(raphaelAreaId,$("#"+raphaelAreaId).width(), $("#"+raphaelAreaId).height());
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
	$("#"+raphaelAreaId).mousemove(function(e){
		var top,left,oDiv;
		oDiv = document.getElementById(raphaelAreaId); 
		top = getY(oDiv); 
		left = getX(oDiv); 
		paperX = (e.clientX-left + oDiv.parentElement.scrollLeft);
		paperY = (e.clientY-top + oDiv.parentElement.scrollTop);
	});
	$("#"+raphaelAreaId).bind("click",function(){
		var date = new Date();
		var currentTime = date.getTime();
		$(paper).data("currNode",null);
		for(var i=0;i<currentRects.length;i++){
			currentRects[i].hide();
		}
		for(var i=0;i<connections.length;i++){
			connections[i].path.removePoints();
		}
		containerShow(["basicPropsContainer","formalParamContainer","dataFieldContainer"]);
		var date = new Date();
		var ctime = date.getTime();
		showProps();
		date = new Date();
		console.log(date.getTime()-ctime);
		showDataFiled();
		showFormalParam();
	});
	
	var tempId = raphaelAreaId.substring(5);
	$("#tools_"+tempId+" > div[type='PATH']").bind("click",function(){
		$(paper).data("mod","path");
	});
	$("#tools_"+tempId+" > div[type='POINTER']").bind("click",function(){
		$(paper).data("mod","pointer");
	});
	$("#tools_"+tempId+" > div[type='SHOW']").bind("click",function(){
		//alert(checkId(xmlDoc,"workflowprocess1"));
		$("#dlg_xmlContent").text(xmlToString(xmlDoc));
		$('#dlg').dialog('open');
	});
	$("#tools_"+tempId+" > div[type='SAVE']").bind("click",function(){
		 var tab = $('#process_tabs').tabs("getSelected");
		 saveXmlDocToServer(tab,false);
	});
	$("#tools_"+tempId+" > div[type='VALIDATE']").bind("click",function(){
		var tab = $('#process_tabs').tabs("getSelected");
		validate(tab);
	});
	
	this.deleteFun = function(e){
		if (e.keyCode == 46) {
			var currNode = $(paper).data("currNode");
			if (currNode) {
				currNode.remove();
				$(paper).removeData("currNode");
				if(own.tmpPath != ""){
					own.tmpPath.remove();
					own.pathWhere = [];
					$(document).unbind("mousemove");
				}
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
		//获取节点的描述对象
		var desc = selectNode(xmlDoc,own.workflowProcess,"Description");
		if(desc != null){
			for(var i=0;i<desc.childNodes.length;i++){
				var node2 = desc.childNodes[i];
				if(node2.nodeName == "#text"){
					var row_desc = {name:"描述",value:node2.nodeValue,editor:'text'};
					rows.push(row_desc);
					break;
				}
			}
		}
		//获取节点查看表单页面
		var extNode = selectNode(xmlDoc,own.workflowProcess,"ExtendedAttributes/ExtendedAttribute[@name='CLFLOW_VIEW_FORM_PAGE']");
		if(extNode != null){
			var row_viewFormPage = {name:"查看表单页面",value:extNode.getAttribute("value"),editor:'text'};
			rows.push(row_viewFormPage);
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
		    onAfterEdit:function(index,row){ 
		    	switch (index) {
				case 0://id修改
					own.workflowProcess.setAttribute("id",row.value);
					break;
				case 1://名称修改
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
						"value":"STRING",
						"text":"STRING"
					},{
						"value":"BOOLEAN",
						"text":"BOOLEAN"
					},{
						"value":"INTEGER",
						"text":"INTEGER"
					},{
						"value":"FLOAT",
						"text":"FLOAT"
					},{
						"value":"DATETIME",
						"text":"DATETIME"
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
			var row = {id:"",name:"",type:"",initValue:"",note:""};
			row.id = dataField.getAttribute("id");
			row.name = dataField.getAttribute("name");
			var dataType = getSubNode(dataField,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = basicType.getAttribute("type");
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
			$(row).unbind("delete").bind("delete",function(){
				dataField.parentNode.removeChild(dataField);
			});
			$(row).unbind("update").bind("update",function(){
				updateDataField(dataField,this);
			});
		});
		$("#dataFieldBtn_del").unbind("click").bind("click",function(){
			var checked = $('#dataField').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#dataField').datagrid('getRowIndex',checked[i]);
				$(checked[i]).trigger("delete");
				$('#dataField').datagrid('deleteRow',index);
			}
		});
		
		function updateDataField(dataField,row){
			dataField.setAttribute("id",row.id);
			dataField.setAttribute("name",row.name);
			var dataType = getSubNode(dataField,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					basicType.setAttribute("type",row.type);
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
				if(row.node != ""){
					desc = xmlDoc.createElement("Description");
					desc.appendChild(xmlDoc.createTextNode(row.note));
					dataField.appendChild(desc);
				}
			}else{
				removeAllSubNode(desc);
				if(row.node != ""){
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}
		};
		
		
		var dataFields = getDataFields(own.workflowProcess);
		for(var i=0;i<dataFields.length;i++){
			var df = dataFields[i];
			var row = {id:"",name:"",type:"",initValue:"",note:""};
			row.id = df.getAttribute("id");
			row.name = df.getAttribute("name");
			var dataType = getSubNode(df,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = basicType.getAttribute("type");
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
			$(row).unbind("delete").bind("delete",function(){
				df.parentNode.removeChild(df);
			});
			$(row).unbind("update").bind("update",function(){
				updateDataField(df,this);
			});
		}
		
		$('#dataField').datagrid({
			onClickCell:onClickCell,
			onAfterEdit:function(index,row){ 
				$(row).trigger("update");
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true};
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
					"value":"IN",
					"text":"IN"
				},{
					"value":"INOUT",
					"text":"INOUT"
				},{
					"value":"OUT",
					"text":"OUT"
				}],
				"panelHeight":"auto"
			}
		};
		var typeEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"STRING",
						"text":"STRING"
					},{
						"value":"BOOLEAN",
						"text":"BOOLEAN"
					},{
						"value":"INTEGER",
						"text":"INTEGER"
					},{
						"value":"FLOAT",
						"text":"FLOAT"
					},{
						"value":"DATETIME",
						"text":"DATETIME"
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
			var row = {id:"",index:"",mode:"",type:"",note:""};
			row.id = formalParam.getAttribute("id");
			row.index = formalParam.getAttribute("index");
			row.mode = formalParam.getAttribute("mode");
			var dataType = getSubNode(formalParam,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = basicType.getAttribute("type");
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
			$(row).unbind("delete").bind("delete",function(){
				formalParam.parentNode.removeChild(formalParam);
			});
			$(row).unbind("update").bind("update",function(){
				updateFormalParam(formalParam,this);
			});
		});
		$("#formalBtn_del").unbind("click").bind("click",function(){
			var checked = $('#formalParam').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#formalParam').datagrid('getRowIndex',checked[i]);
				$('#formalParam').datagrid('deleteRow',index);
				$(checked[i]).trigger("delete");
			}
		});
		
		function updateFormalParam(formalParam,row){
			formalParam.setAttribute("id",row.id);
			formalParam.setAttribute("index",row.index);
			formalParam.setAttribute("mode",row.mode);
			var dataType = getSubNode(formalParam,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					basicType.setAttribute("type",row.type);
				}
			}
			var desc = getSubNode(formalParam,"Description");
			if(desc == null){
				if(row.node != ""){
					desc = xmlDoc.createElement("Description");
					formalParam.appendChild(desc);
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}else{
				removeAllSubNode(desc);
				if(row.node != ""){
					desc.appendChild(xmlDoc.createTextNode(row.note));
				}
			}
		};
		
		
		var formalParams = getFormalParameters(own.workflowProcess);
		for(var i=0;i<formalParams.length;i++){
			var fp = formalParams[i];
			var row = {id:"",index:"",mode:"",type:"",note:""};
			row.id = fp.getAttribute("id");
			row.index = fp.getAttribute("index");
			row.mode = fp.getAttribute("mode");
			var dataType = getSubNode(fp,"DataType");
			if(dataType != null){
				var basicType = getSubNode(dataType,"BasicType");
				if(basicType != null){
					row.type = basicType.getAttribute("type");
				}
			}
			var desc = getSubNode(fp,"Description");
			if(desc != null){
				for(var j=0;j<desc.childNodes.length;j++){
					var node = desc.childNodes[j];
					if(node.nodeName == "#text"){
						row.node = node.nodeValue;
						break;
					}
				}
			}
			$('#formalParam').datagrid("appendRow",row);
			$(row).unbind("delete").bind("delete",function(){
				fp.parentNode.removeChild(fp);
			});
			$(row).unbind("update").bind("update",function(){
				updateFormalParam(fp,this);
			});
		}
		
		$('#formalParam').datagrid({
			onClickCell:onClickCell,
			onAfterEdit:function(index,row){ 
				$(row).trigger("update");
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		var editIndex = undefined;
		function endEditing(){
			 if (editIndex == undefined){return true};
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
		rect = paper.rect(x,y,50,50);
		rect.attr({"fill":"green","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = rect.getBBox();
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "开始").attr("font-size",13);
	}else if(type == "FINISH"){
		rect = paper.rect(x,y,50,50);
		rect.attr({"fill":"red","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = rect.getBBox();
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "结束").attr("font-size",13);
	}else{
		rect = paper.rect(x,y,100,50);
		rect.attr({"fill":"#F6F7FF","stroke":"#03689A","stroke-width":"2"});
		var rectBBox = rect.getBBox();
		text = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height/ 2, "节点").attr("font-size",12);
		joinText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y+10, "合").attr("font-size",11);
		splitText = paper.text(rectBBox.x + rectBBox.width/2,rectBBox.y + rectBBox.height-11, "分").attr("font-size",11);
	}
	if(activity == null){
		activity = createActivity(xmlDoc,raphaelPaper.workflowProcess.getAttribute("id"),type,x,y,and,or);
	}
	text.attr("text",activity.getAttribute("name"));
	if(joinText != null){
		var transitionRestrictions = getSubNode(activity,"TransitionRestrictions");
		if(transitionRestrictions != null){
			var transitionRestriction = getSubNode(transitionRestrictions,"TransitionRestriction");
			if(transitionRestriction != null){
				var join = getSubNode(transitionRestriction,"Join");
				if(join != null){
					joinText.attr("text","合:"+join.getAttribute("type"));
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
					splitText.attr("text","分:"+split.getAttribute("type"));
				}
			}
		}
	}
	rect.drag(move, start, end);
	text.drag(move, start, end);
	if(joinText != null){
		joinText.drag(move, start, end);
	}
	if(splitText != null){
		splitText.drag(move, start, end);
	}
	function move(dx,dy){
		rect.attr({x:rect.ox+dx,y:rect.oy+dy});
		resetI();
		rect.attr("opacity","0.5");
		var connections = raphaelPaper.getConnections();
		for(var i=0;i<connections.length;i++){
			var  obj = connections[i];
			if(own == obj.rect1 || own == obj.rect2){
				obj.path.getNode().attr("path",getPathString(obj.rect1.getNode(),obj.rect2.getNode()));
			}
		}
	};
	function start(dx,dy){
		$(paper).data("currNode",own);
		hideOtherFocus();
		rect.ox = eval(rect.attr("x"));
		rect.oy = eval(rect.attr("y"));
		resetI();
		//rect.attr({"stroke":"#000000","stroke-width":"3","stroke-dasharray":"--","stroke-linecap":"square"});
		rect.attr("opacity","0.5");
	};
	function end(){
		rect.attr("opacity","1");
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_X",rect.attr("x"));
		updateAcivity("ExtendedAttribute","CLFLOW_LOCATION_Y",rect.attr("y"));
		var tab = $('#process_tabs').tabs("getSelected");
		autoCheckChange(tab);
	};
	
	
	var attr = {"fill":"#000","stroke":"#000","stroke-width":"2"};
	var h = 2;
	var i = {};//边角点 l=left,t=top,r=right,b=bottom
	i.lt = paper.rect(0,0,h,h).attr(attr).hide();//左上
	i.t = paper.rect(0,0,h,h).attr(attr).hide();//上
	i.rt = paper.rect(0,0,h,h).attr(attr).hide();//右上
	i.r = paper.rect(0,0,h,h).attr(attr).hide();//右
	i.rb = paper.rect(0,0,h,h).attr(attr).hide();//右下
	i.b = paper.rect(0,0,h,h).attr(attr).hide();//下
	i.lb = paper.rect(0,0,h,h).attr(attr).hide();//左下
	i.l = paper.rect(0,0,h,h).attr(attr).hide();//左
	var clickArray = new Array();
	clickArray.push(rect.node);
	clickArray.push(text.node);
	if(joinText != null){
		clickArray.push(joinText.node);
	}
	if(splitText != null){
		clickArray.push(splitText.node);
	}
	$(clickArray).bind("click",function(){
		hideOtherFocus(); 
		resetI();
		$(paper).data("currNode",own);
		if($(paper).data("mod") == "path"){
			var len = raphaelPaper.pathWhere.length;
			if(len == 0){
				raphaelPaper.pathWhere[0] = own;
				raphaelPaper.tmpPath = paper.path("M0 0L0 0").attr({"arrow-end":"open-narrow-long","stroke-width":"2"});
				$(document).unbind("mousemove").bind("mousemove",function(e){
					var bbox = rect.getBBox();
					var p = {x:bbox.x+bbox.width/2,y:bbox.y2};
					var pathString = "M"+p.x+" "+p.y+"L"+raphaelPaper.getPoint().x+" "+raphaelPaper.getPoint().y;
					raphaelPaper.tmpPath.attr({path:pathString});
				});
			}else if(raphaelPaper.pathWhere[0] != own){
				raphaelPaper.tmpPath.remove();
				$(document).unbind("mousemove");
				new Path(raphaelPaper.pathWhere[0],own,raphaelPaper,null);
				raphaelPaper.pathWhere = [];
			}
		}
		$(document).unbind("keydown").bind("keydown",function(e){raphaelPaper.deleteFun(e);});
		$(document).unbind("click").bind("click",function(){
			if(raphaelPaper.tmpPath != ""){
				raphaelPaper.tmpPath.remove();
				raphaelPaper.pathWhere = [];
				$(document).unbind("mousemove");
			}
		});
		//showContainer();
		//showProps();
		//showImplTools();
		return false;
	});
	
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
					}
				}
				if(n == "No"){
					containerShow(["basicPropsContainer"]);
				}else if(n == "Tool"){
					containerShow(["basicPropsContainer","implToolContainer"]);
				}else{
					containerShow(["basicPropsContainer"]);
				}
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
						for(var i=0;i<apps.length;i++){
							opts.push({"value":apps[i].getAttribute("id"),"text":apps[i].getAttribute("id")});
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
						"value":"PROCEDURE",
						"text":"PROCEDURE"
					},{
						"value":"WEBPAGE",
						"text":"WEBPAGE"
					}],
					"panelHeight":"auto"
				}
		};
		
		var actualParamEditor = {
			type:"actualParambox",
			options:{
				workflowProcess:raphaelPaper.workflowProcess
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
			var row = {id:tool.getAttribute("id"),type:tool.getAttribute("type"),actualParam:"",note:""};
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
									actualParams.push(node3.nodeValue);
									break;
								}
							}
							break;
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
			$(row).unbind("delete").bind("delete",function(){
				tool.parentNode.removeChild(tool);
			});
			$(row).unbind("update").bind("update",function(){
				updateTool(tool,this);
			});
			
			
		};
		 
		$('#implTool').datagrid({
			onClickCell:onClickCell,
			onAfterEdit:function(index,row){ 
				$(row).trigger("update");
				var tab = $('#process_tabs').tabs("getSelected");
				autoCheckChange(tab);
			}
		});
		
		$("#implToolBtn_add").unbind("click").bind("click",function(){
			var row = {id:"",type:"",actualParam:"",note:""};
			$('#implTool').datagrid('appendRow',row);
			var tool = createTool(xmlDoc,activity);
			$(row).unbind("delete").bind("delete",function(){
				tool.parentNode.removeChild(tool);
			});
			$(row).unbind("update").bind("update",function(){
				updateTool(tool,this);
			});
		});
		$("#implToolBtn_del").unbind("click").bind("click",function(){
			var checked = $('#implTool').datagrid('getChecked');
			for(var i=0;i<checked.length;i++){
				var index = $('#implTool').datagrid('getRowIndex',checked[i]);
				$('#implTool').datagrid('deleteRow',index);
				$(checked[i]).trigger("delete");
			}
		});
		
		
		function updateTool(tool,row){
			tool.setAttribute("type",row.type);
			tool.setAttribute("id",row.id);
			var hasDesc = false;
			for(var i=0;i<tool.childNodes.length;i++){
				if(tool.childNodes[i].nodeName == "ActualParameters"){
					var aps = tool.childNodes[i];
					var as = row.actualParam.split(",");
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
					if(row.node != ""){
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
			 if (editIndex == undefined){return true};
			 if ($('#implTool').datagrid('validateRow', editIndex)){
				 $('#implTool').datagrid('endEdit', editIndex);
				 editIndex = undefined;
				 return true;
			 } else {
				 return false;
			 }
		}

		function onClickCell(index, field){
			 if (endEditing()){
				 $('#implTool').datagrid('selectRow', index).datagrid('editCell', {index:index,field:field});
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
						"value":"COMPETE",
						"text":"COMPETE"
					},{
						"value":"ORDER",
						"text":"ORDER"
					},{
						"value":"OUTORDER",
						"text":"OUTORDER"
					}],
					"panelHeight":"auto"
				}
		};
		var limitUnitEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"MONTH",
						"text":"MONTH"
					},{
						"value":"DAY",
						"text":"DAY"
					},{
						"value":"HOUR",
						"text":"HOUR"
					}],
					"panelHeight":"auto"
				}
		};
		var implEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"No",
						"text":"No"
					},{
						"value":"Tool",
						"text":"Tool"
					},{
						"value":"Subflow",
						"text":"Subflow"
					}],
					"panelHeight":"auto"
				}
		};
		var startModeEditor = finishModeEditor= {
				type : 'combobox',
				options:{
					data:[{
						"value":"AUTOMATIC",
						"text":"AUTOMATIC"
					},{
						"value":"MANUAL",
						"text":"MANUAL"
					}],
					"panelHeight":"auto"
				}
		};
		var joinEditor = splitEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"AND",
						"text":"AND"
					},{
						"value":"OR",
						"text":"OR"
					}],
					"panelHeight":"auto"
				}
		};
		var showNextPageEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"TRUE",
						"text":"TRUE"
					},{
						"value":"FALSE",
						"text":"FALSE"
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
		var props = [{key:"row_id",name:"ID",editor:"text"},{key:"row_name",name:"名称",editor:"text"},{key:"row_desc",name:"描述",editor:"text"},
		             {key:"row_performer",name:"操作者",editor:selectParticipantEditor},{key:"row_dispatchType",name:"分派类型",editor:dispatchTypeEditor},
		             {key:"row_limitCount",name:"期限数量",editor:"text"},{key:"row_limitCount",name:"期限单位",editor:limitUnitEditor},
		             {key:"row_impl",name:"实现",editor:implEditor},{key:"row_startMode",name:"开始模式",editor:startModeEditor},
		             {key:"row_finishMode",name:"结束模式",editor:finishModeEditor},{key:"row_join",name:"合类型",editor:joinEditor},
		             {key:"row_split",name:"分类型",editor:splitEditor},{key:"row_showNextPage",name:"显示一步页面",editor:showNextPageEditor},
		             {key:"row_maxLine",name:"最大选择路线",editor:"numberbox"},{key:"row_minLine",name:"最小选择路线",editor:"numberbox"}
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
			}else if(node.nodeName == "Performer"){//操作者
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_performer = {name:"操作者",value:node2.nodeValue,editor:selectParticipantEditor};
						rows.push(row_performer);
						break;
					}
				}
			}else if(node.nodeName == "DispatchType"){//分派类型
				var row_dispatchType = {name:"分派类型",value:node.getAttribute("type"),editor:dispatchTypeEditor};
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
					var row_limitUnit = {name:"期限单位",value:unit,editor:limitUnitEditor};
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
					}
				}
				var row_impl ={name:"实现",value:n,editor:implEditor};
				rows.push(row_impl);
			}else if(node.nodeName == "StartMode"){//开始模式
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[0];
					if(node2.nodeName == "#text"){
						var row_startMode = {name:"开始模式",value:node2.nodeValue,editor:startModeEditor};
						rows.push(row_startMode);
						break;
					}
				}
			}else if(node.nodeName == "FinishMode"){//结束模式
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "#text"){
						var row_finishMode = {name:"结束模式",value:node2.nodeValue,editor:finishModeEditor};
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
						var row_join = {name:"合类型",value:join.getAttribute("type"),editor:joinEditor};
						rows.push(row_join);
					}
					var split = getSubNode(transitionRestriction,"Split");
					if(split != null){
						//分类型
						var row_split = {name:"分类型",value:split.getAttribute("type"),editor:splitEditor};
						rows.push(row_split);
					}
				}
			}else if(node.nodeName == "ExtendedAttributes"){
				for(var j=0;j<node.childNodes.length;j++){
					var n = node.childNodes[j];
					if(n.nodeName == "ExtendedAttribute"){
						if(n.getAttribute("name") == "CLFLOW_SHOW_NEXT_PAGE"){
							//显示一步页面
							var row_showNextPage = {name:"显示一步页面",value:n.getAttribute("value"),editor:showNextPageEditor};
							rows.push(row_showNextPage);
						}else if(n.getAttribute("name") == "CLFLOW_MAXLINE"){
							//最大选择路线
							var row_maxLine = {name:"最大选择路线",value:n.getAttribute("value"),editor:"numberbox"};
							rows.push(row_maxLine);
						}else if(n.getAttribute("name") == "CLFLOW_MINLINE"){
							//最小选择路线
							var row_minLine = {name:"最小选择路线",value:n.getAttribute("value"),editor:"numberbox"};
							rows.push(row_minLine);
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
		    onAfterEdit:function(index,row){
		    	switch (index) {
				case 0:
					var srcId = activity.getAttribute("id");
					var destId = row.value;
					updateAcivity("Activity","id",row.value);
					triggerIdChange(xmlDoc, "Activity", srcId, destId);
					break;
				case 1:
					updateAcivity("Activity","name",row.value);
					text.attr("text",row.value);
					break;
				case 2:
					updateAcivity("Description","",row.value);
					break;
				case 3:
					updateAcivity("Performer","",row.value);
					break;
				case 4:
					updateAcivity("DispatchType","type",row.value);
					break;
				case 5:
					updateAcivity("Limit","count",row.value);
					break;
				case 6:
					updateAcivity("Limit","unit",row.value);
					break;
				case 7:
					updateAcivity("Implementation","",row.value);
					if(row.value == "Tool"){
						containerShow(["basicPropsContainer","implToolContainer"]);
					}else{
						containerShow(["basicPropsContainer"]);
					}
					break;
				case 8:
					updateAcivity("StartMode","",row.value);
					break;
				case 9:
					updateAcivity("FinishMode","",row.value);
					break;
				case 10:
					updateAcivity("Join","type",row.value);
					if(joinText != null){
						joinText.attr("text","合:"+row.value);
					}
					break;
				case 11:
					updateAcivity("Split","type",row.value);
					if(splitText != null){
						splitText.attr("text","分:"+row.value);
					}
					break;
				case 12:
					updateAcivity("ExtendedAttribute","CLFLOW_SHOW_NEXT_PAGE",row.value);
					break;
				case 13:
					updateAcivity("ExtendedAttribute","CLFLOW_MAXLINE",row.value);
					break;
				case 14:
					updateAcivity("ExtendedAttribute","CLFLOW_MINLINE",row.value);
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
		}else if(nodeName == "Description" || nodeName == "Performer" || nodeName == "StartMode" || nodeName == "FinishMode"){
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
				
			}else if(value == "Subflow"){
				
			}
		}else if(nodeName == "Join" || nodeName == "Split"){
			var node = getSubNode(activity,"TransitionRestrictions");
			node = getSubNode(node,"TransitionRestriction");
			node = getSubNode(node,nodeName);
			node.setAttribute(key,value);
		}else if(nodeName == "ExtendedAttribute"){
			var node = getSubNode(activity,"ExtendedAttributes");
			for(var i=0;i<node.childNodes.length;i++){
				if(node.childNodes[i].nodeName == "ExtendedAttribute"){
					if(node.childNodes[i].getAttribute("name") == key){
						node.childNodes[i].setAttribute("value",value);
						break;
					}
				}
			}
		}
		
	};
	
	function show(){
		for(var o in i){
			i[o].show();
		}
	};
	this.hide = function(){
		for ( var o in i) {
			i[o].hide();
		}
	};
	function removeI(){
		for ( var o in i) {
			i[o].remove();
		}
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
		var bbox = rect.getBBox();
		q = {
				x : bbox.x - margin,
				y : bbox.y - margin,
				width : bbox.width + margin * 2,
				height : bbox.height + margin * 2
			};
		text.attr({"x":bbox.x + bbox.width/2,"y":bbox.y + bbox.height/ 2});
		if(joinText != null){
			joinText.attr({"x":bbox.x+bbox.width/2,"y":bbox.y+10});
		}
		if(splitText != null){
			splitText.attr({"x":bbox.x + bbox.width/2,"y":bbox.y + bbox.height-11});
		}
		i.t.attr({
			x : q.x + q.width / 2 - h / 2,
			y : q.y - h / 2
		});
		i.lt.attr({
			x : q.x - h / 2,
			y : q.y - h / 2
		});
		i.l.attr({
			x : q.x - h / 2,
			y : q.y - h / 2 + q.height / 2
		});
		i.lb.attr({
			x : q.x - h / 2,
			y : q.y - h / 2 + q.height
		});
		i.b.attr({
			x : q.x - h / 2 + q.width / 2,
			y : q.y - h / 2 + q.height
		});
		i.rb.attr({
			x : q.x - h / 2 + q.width,
			y : q.y - h / 2 + q.height
		});
		i.r.attr({
			x : q.x - h / 2 + q.width,
			y : q.y - h / 2 + q.height / 2
		});
		i.rt.attr({
			x : q.x - h / 2 + q.width,
			y : q.y - h / 2
		});
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
		text.remove();
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
	path.attr({"arrow-end":"open-narrow-long","stroke-width":"2","cursor":"pointer"});
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
		$(document).unbind("keydown").bind("keydown",function(e){raphaelPaper.deleteFun(e);});
		return false;
	});
	
	function showProps(){
		$('#basicProps').datagrid('loadData',{total:0,rows:[]}); 
		var rollBackEditor = manualEditor = {
				type : 'combobox',
				options:{
					data:[{
						"value":"TRUE",
						"text":"TRUE"
					},{
						"value":"FALSE",
						"text":"FALSE"
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
		var exts = getSubNode(transition,"transition");
		if(exts != null){
			for(var i=0;i<exts.childNodes.length;i++){
				var node = exts.childNodes[i];
				if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_ROLLBACK"){
					var row_rollBack = {name:"回退",value:node.getAttribute("value"),editor:rollBackEditor};
					rows.push(row_rollBack);
				}else if(node.nodeName == "ExtendedAttribute" && node.getAttribute("name") == "CLFLOW_MANUAL"){
					var row_manual = {name:"人工干预",value:node.getAttribute("value"),editor:manualEditor};
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
		var row_from = {name:"源节点",value:transition.getAttribute("from")};
		rows.push(row_from);
		var row_to = {name:"目标节点",value:transition.getAttribute("to")};
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
		    onAfterEdit:function(index,row){ 
		    	switch (index) {
				case 0:
					transition.setAttribute("id",row.value);//ID
					break;
				case 1:
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
					transition.setAttribute("from",row.value);
					break;
				case 4://目标节点
					transition.setAttribute("to",row.value);
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
									node2.setAttribute("value",row.value);
									hasElement = true;
									break;
								}
							}
						}
						break;
					}
					if(!hasElement){
						for(var i=0;i<transition.childNodes.length;i++){
							var node = transition.childNodes[i];
							if(node.nodeName == "ExtendedAttributes"){
								var e = xmlDoc.createElement("ExtendedAttribute");
								e.setAttribute("name","CLFLOW_ROLLBACK");
								e.setAttribute("value",row.value);
								node.appendChild(e);
								break;
							}
						}
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
									node2.setAttribute("value",row.value);
									hasElement = true;
									break;
								}
							}
						}
						break;
					}
					if(!hasElement){
						for(var i=0;i<transition.childNodes.length;i++){
							var node = transition.childNodes[i];
							if(node.nodeName == "ExtendedAttributes"){
								var e = xmlDoc.createElement("ExtendedAttribute");
								e.setAttribute("name","CLFLOW_MANUAL");
								e.setAttribute("value",row.value);
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
		var bbox1 = rect1.getBBox();
		var bbox2 = rect2.getBBox();
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
			    onAfterEdit:function(index,row){ 
			    	switch (index) {
					case 0:
						packageObj.setAttribute("id",row.value);
						break;
					case 1:
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
					xmlDoc:xmlDoc
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
				var row_type = {name:"类型",value:type.getAttribute("type"),editor:""};
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
							var row_participant = {name:"参与者",value:node.getAttribute("value"),editor:participantbox};
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
			    onAfterEdit:function(index,row){ 
			    	switch (index) {
					case 0:
						var srcId = packageObj.getAttribute("id");
						var destId = row.value;
						packageObj.setAttribute("id",row.value);
						triggerIdChange(xmlDoc, "Participant", srcId, destId);
						break;
					case 1:
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
								ext.setAttribute("value",row.value);
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
			    onAfterEdit:function(index,row){ 
			    	switch (index) {
					case 0:
						var srcId = packageObj.getAttribute("id");
						var destId = row.value;
						packageObj.setAttribute("id",row.value);
						triggerIdChange(xmlDoc, "Application", srcId, destId);
						break;
					case 1:
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
						"value":"IN",
						"text":"IN"
					},{
						"value":"INOUT",
						"text":"INOUT"
					},{
						"value":"OUT",
						"text":"OUT"
					}],
					"panelHeight":"auto"
				}
			};
			var typeEditor = {
					type : 'combobox',
					options:{
						data:[{
							"value":"STRING",
							"text":"STRING"
						},{
							"value":"BOOLEAN",
							"text":"BOOLEAN"
						},{
							"value":"INTEGER",
							"text":"INTEGER"
						},{
							"value":"FLOAT",
							"text":"FLOAT"
						},{
							"value":"DATETIME",
							"text":"DATETIME"
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
				var row = {id:"",index:"",mode:"",type:"",note:""};
				row.id = formalParam.getAttribute("id");
				row.index = formalParam.getAttribute("index");
				row.mode = formalParam.getAttribute("mode");
				for(var i=0;i<formalParam.childNodes.length;i++){
					var node = formalParam.childNodes[i];
					if(node.nodeName == "DataType"){
						for(var j=0;j<node.childNodes.length;j++){
							var node2 = node.childNodes[j];
							if(node2.nodeName == "BasicType"){
								row.type = node2.getAttribute("type");
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
				$(row).unbind("delete").bind("delete",function(){
					formalParam.parentNode.removeChild(formalParam);
				});
				$(row).unbind("update").bind("update",function(){
					updateFormalParam(formalParam,this);
				});
			});
			$("#formalBtn_del").unbind("click").bind("click",function(){
				var checked = $('#formalParam').datagrid('getChecked');
				for(var i=0;i<checked.length;i++){
					var index = $('#formalParam').datagrid('getRowIndex',checked[i]);
					$('#formalParam').datagrid('deleteRow',index);
					$(checked[i]).trigger("delete");
				}
			});
			
			function updateFormalParam(formalParam,row){
				formalParam.setAttribute("id",row.id);
				formalParam.setAttribute("index",row.index);
				formalParam.setAttribute("mode",row.mode);
				for(var i=0;i<formalParam.childNodes.length;i++){
					var node = formalParam.childNodes[i];
					if(node.nodeName == "DataType"){
						for(var j=0;j<node.childNodes.length;j++){
							var node2 = node.childNodes[j];
							if(node2.nodeName == "BasicType"){
								node2.setAttribute("type",row.type);
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
				var row = {id:"",index:"",mode:"",type:"",note:""};
				row.id = fp.getAttribute("id");
				row.index = fp.getAttribute("index");
				row.mode = fp.getAttribute("mode");
				for(var j=0;j<fp.childNodes.length;j++){
					var node = fp.childNodes[j];
					if(node.nodeName == "DataType"){
						for(var k=0;k<node.childNodes.length;k++){
							var node2 = node.childNodes[k];
							if(node2.nodeName == "BasicType"){
								row.type = node2.getAttribute("type");
								break;
							}
						}
						break;
					}
				}
				var desc = getSubNode(fp,"Description");
				if(desc != null){
					row.note = desc.childNodes[0].nodeValue;
				}
				$('#formalParam').datagrid("appendRow",row);
				$(row).unbind("delete").bind("delete",function(){
					fp.parentNode.removeChild(fp);
				});
				$(row).unbind("update").bind("update",function(){
					updateFormalParam(fp,this);
				});
			}
			
			$('#formalParam').datagrid({
				onClickCell:onClickCell,
				onAfterEdit:function(index,row){ 
					$(row).trigger("update");
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
		 if (editIndex == undefined){return true};
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
					ts[i].setAttribute("from",destId)
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
							xmlDoc.documentElement.replaceChild(node,xmlDoc.createTextNode(destValue));
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