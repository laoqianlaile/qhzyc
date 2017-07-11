//选中项向左移动或向右移动
function moveLeftOrRight(fromObj,toObj){
    var fromObjOptions=fromObj.options;
    for(var i=0;i<fromObjOptions.length;i++){
        if(fromObjOptions[i].selected){
            toObj.appendChild(fromObjOptions[i]);
            i--;
        }
    }
}
//左边全部右移动，或右边全部左移
function moveLeftOrRightAll(fromObj,toObj){
    var fromObjOptions=fromObj.options;
    for(var i=0;i<fromObjOptions.length;i++){
        fromObjOptions[0].selected=true;
        toObj.appendChild(fromObjOptions[i]);
        i--;
    }
}
//向上移动
function moveUp(selectObj){
    var theObjOptions=selectObj.options;
    for(var i=1;i<theObjOptions.length;i++) {
        if( theObjOptions[i].selected && !theObjOptions[i-1].selected ) {
            swapOptionProperties(theObjOptions[i],theObjOptions[i-1]);
        }
    }
}
//向下移动
function moveDown(selectObj){
    var theObjOptions=selectObj.options;
    for(var i=theObjOptions.length-2;i>-1;i--) {
        if( theObjOptions[i].selected && !theObjOptions[i+1].selected ) {
            swapOptionProperties(theObjOptions[i],theObjOptions[i+1]);
        }
    }
}
//移动至最顶端
function moveToTop(selectObj){
    var theObjOptions=selectObj.options;
    var oOption=null;
    for(var i=0;i<theObjOptions.length;i++) {
        if( theObjOptions[i].selected && oOption) {
            selectObj.insertBefore(theObjOptions[i],oOption);
        }
        else if(!oOption && !theObjOptions[i].selected) {
            oOption=theObjOptions[i];
        }
    }
}
//移动至最低端
function moveToBottom(selectObj){
    var theObjOptions=selectObj.options;
    var oOption=null;
    for(var i=theObjOptions.length-1;i>-1;i--) {
        if( theObjOptions[i].selected ) {
            if(oOption) {
                oOption=selectObj.insertBefore(theObjOptions[i],oOption);
            }
            else oOption=selectObj.appendChild(theObjOptions[i]);
        }
    }
}
//全部选中
function selectAllOption(selectObj){
    var theObjOptions=selectObj.options;
    for(var i=0;i<theObjOptions.length;i++){
        theObjOptions[0].selected=true;
    }
}

/* private function */
function swapOptionProperties(option1,option2){
    //option1.swapNode(option2);
    var tempStr=option1.value;	
    option1.value=option2.value;	
    option2.value=tempStr;

	var tempValSource=option1.valSource;//
	option1.valSource=option2.valSource;//
	option2.valSource=tempValSource;//

    tempStr=option1.text;
    option1.text=option2.text;
    option2.text=tempStr;
    tempStr=option1.selected;
    option1.selected=option2.selected;
    option2.selected=tempStr;
}

function resetAutoWidth(obj){
    var tempWidth=obj.style.getExpression("width");
    if(tempWidth!=null) {
        obj.style.width="auto";
        obj.style.setExpression("width",tempWidth);
        obj.style.width=null;
    }
}

function checkSelectedOption(targetBox){
	var ObjSelect = document.frm1.ObjSelect;
	var itemField= new Array();
	var itemName=new Array();
	if(ObjSelect&&ObjSelect.options&&ObjSelect.options.length>0){
		var len = ObjSelect.options.length;
		for(var j=0; j<len; j++){
			itemField.push(ObjSelect.options[j].value);
			var name = ObjSelect.options[j].text;
			if(name != null){
				name = name.split(":")[0];
			}
			itemName.push(name);
		}
	}
	if(typeof(targetBox) != undefined){
		if(targetBox == "performer"){
			currentPerformer = itemField.join(",");
		}else if(targetBox == "actualParam"){
			currentActualParm = itemField.join(",");
		}
	}
	return itemName.join(",");
}

/**
 * 
 * @param xmlDoc xmlDocD对象
 * @param workflowProcess 当前流程对象
 * @param srcOptions 所有选项
 * @param objOptions 已经选择的选项
 * @param formalParmas 形参
 * @param targetBox 用于目标(对象)
 */
function resetOptions(xmlDoc,workflowProcess,srcOptions,objOptions,formalParmas,targetBox){
	var srcSelect = document.frm1.SrcSelect;
	var objSelect = document.frm1.ObjSelect;
	var formalParamSelect = document.frm1.formalParamSelect;
	if(srcSelect && srcSelect.options&&srcSelect.options.length>0){
		var len = srcSelect.options.length;
		for(var i=0;i<len;i++){
			srcSelect.remove(srcSelect.options[0]);
		}
	}
	if(objSelect && objSelect.options&&objSelect.options.length>0){
		var len = objSelect.options.length;
		for(var i=0;i<len;i++){
			objSelect.remove(objSelect.options[0]);
		}
	}
	if(objOptions && objOptions.length>0){
		if(srcOptions && srcOptions.length>0){
			for(var i=0;i<objOptions.length;i++){
				for(var j=0;j<srcOptions.length;j++){
					if(objOptions[i] == srcOptions[j]){
						srcOptions.remove(srcOptions[j]);
					}
				}
			}
		}
	}
	
	/*if(srcOptions && srcOptions.length>0){
		for(var i=0;i<srcOptions.length;i++){
			var option = $("<option value=''></option>");
			option.val(srcOptions[i]);
			option.text(srcOptions[i]);
			option.appendTo($(srcSelect));
		}
	}
	if(objOptions&&objOptions.length>0){
		for(var i=0;i<objOptions.length;i++){
			var option = $("<option value=''></option>");
			option.val(objOptions[i]);
			option.text(objOptions[i]);
			option.appendTo($(objSelect));
		}
	}*/
	
	
	var ps = [];
	if(typeof(targetBox) != undefined){
		if(targetBox == "performer"){
			$("#formalParamSelect").css("display","none");
			$("#SrcSelect").css("width","50%");
			$("#ObjSelect").css("width","50%");
			ps = getParticipants(xmlDoc);
		}else if(targetBox == "subflow" || targetBox == "tool"){
			$("#formalParamSelect").css("display","block");
			$("#formalParamSelect").css("width","200px");
			$("#SrcSelect").css("width","170px");
			$("#ObjSelect").css("width","170px");
			ps = getDataFields(workflowProcess);
		}
	}
	if(ps == null) ps = [];
	if(srcOptions && srcOptions.length>0){
		for(var i=0;i<srcOptions.length;i++){
			var hasName = false;
			var option = $("<option value=''></option>");
			option.val(srcOptions[i]);
			for(var j=0;j<ps.length;j++){
				var p = ps[j];
				if(p.getAttribute("id") == srcOptions[i]){
					var name = p.getAttribute("name");
					if(typeof(name) != undefined && name != null && name != ""){
						var basicType = selectNode(xmlDoc,p,".//BasicType");
						if(basicType!=null){
							name = name +":"+transDataType2Chinese(basicType.getAttribute("type"));
						}
						option.text(name);
						hasName = true;
						break;
					}
				}
			}
			if(!hasName){
				option.text(srcOptions[i]);
			}
			option.appendTo($(srcSelect));
		}
	}
	if(objOptions&&objOptions.length>0){
		for(var i=0;i<objOptions.length;i++){
			var hasName = false;
			var option = $("<option value=''></option>");
			option.val(objOptions[i]);
			for(var j=0;j<ps.length;j++){
				var p = ps[j];
				if(p.getAttribute("id") == objOptions[i]){
					var name = p.getAttribute("name");
					if(typeof(name) != undefined && name != null && name != ""){
						var basicType = selectNode(xmlDoc,p,".//BasicType");
						if(basicType!=null){
							name = name +":"+transDataType2Chinese(basicType.getAttribute("type"));
						}
						option.text(name);
						hasName = true;
						break;
					}
				}
			}
			if(!hasName){
				option.text(objOptions[i]);
			}
			option.appendTo($(objSelect));
		}
	}
	
	
	
	if(formalParamSelect && formalParamSelect.options&&formalParamSelect.options.length>0){
		var len = formalParamSelect.options.length;
		for(var i=0;i<len;i++){
			formalParamSelect.remove(formalParamSelect.options[0]);
		}
	}
	if(formalParmas && formalParmas.length>0){
		for(var i=0;i<formalParmas.length;i++){
			var option = $("<option value=''></option>");
			option.val(formalParmas[i]);
			option.text(formalParmas[i]);
			option.appendTo($(formalParamSelect));
		}
	}
}

function initCreaterNode(options){
	var objSelect = document.getElementById("creator_node");
	if(objSelect && objSelect.options&&objSelect.options.length>0){
		var len = objSelect.options.length;
		for(var i=0;i<len;i++){
			objSelect.remove(0);
		}
	}
	if(options && options.length>0){
		for(var i=0;i<options.length;i++){
			var option = $("<option value=''></option>");
			option.val(options[i]);
			option.text(options[i]);
			option.appendTo($(objSelect));
		}
	}
}
function initCreaterNodeMap(options,domId,defaultValue,participantType){
	var objSelect = document.getElementById(domId);
	if(objSelect && objSelect.options&&objSelect.options.length>0){
		var len = objSelect.options.length;
		for(var i=0;i<len;i++){
			objSelect.remove(0);
		}
	}
	if(options && options){
		 for(var key in options){  
			 var option = $("<option value=''></option>");
				option.val(key);
				option.text(options[key]);
				option.appendTo($(objSelect));
	     } 
	}
	if(participantType == "FORMULA"){
		if(typeof(defaultValue)!=undefined && defaultValue != "@_" && defaultValue.substring(0,2) == "@_"){
			var text = defaultValue.substring(2);
			$("#creator_node option").each(function(index,value){
				if($(value).text() == text){
					$(value).attr("selected","selected");
					return false;
				}
			});
		}
	}
	
}
function initParticipantDefaultSelect(defaultValue,participantType){
	$("#orgViewDiv").css("display","none");
	$("#orgViewDiv2").css("display","none");
	/**
	 *原来的值判断哪个单选项被选中
	 *值有：无、指定部门、指定用户、创建者、上一节点操作者、与节点相同的操作者、自定义的处理类 
	 */
	if(typeof(defaultValue) == undefined || defaultValue == ""){//默人选择指定部门
		$("#orgViewDiv").css("display","block");
	}else{
		if(participantType == "FORMULA"){//公式
			if(defaultValue == "创建者" || defaultValue == "@creator"){
				$("#gs_creater").attr("checked","true");
			}else if(defaultValue == "上一节点操作者" || defaultValue == "@_"){
				$("#gs_pre_operator").attr("checked","true");
			}else if(defaultValue == "与节点相同的操作者" || defaultValue.substring(0,2) == "@_"){
				$("#gs_same_node_operator").attr("checked","true");
			}else if(defaultValue.substring(0,2) == "@?"){
				$("input[name='gs_operator'][value='"+defaultValue+"']").attr("checked","true");
			}else{
				$.each($("input[name='gs_operator']"),function(index,value){
					if($(value).next("label").text() == defaultValue){
						$(value).attr("checked","true");
						return false;
					}
				});
			}
		}else if(participantType == "DEPARTMENT"){//指定部门
			$("#gs_department").attr("checked","true");
			$("#orgViewDiv").css("display","block");
		}else if(participantType == "HUMAN"){//指定用户
			$("#gs_human").attr("checked","true");
			$("#orgViewDiv2").css("display","block");
		}
	}
}
function initParticipantBox(xmlDoc,nodeValue,type){
	var as = getActivities(xmlDoc);
	if(type == "FORMULA"){
		if(typeof(nodeValue) != undefined && nodeValue != ""){
			if(nodeValue == "@creator"){//创建者
				nodeValue = "创建者";
			}else if(nodeValue == "@_"){//上一节点操作者
				nodeValue = "上一节点操作者";
			}else if(nodeValue.substring(0,2) == "@_"){//与节点相同的操作者
				var nodeId = nodeValue.substring(2);
				for(var i=0;i<as.length;i++){
					if(nodeId == as[i].getAttribute("id")){
						nodeValue = "@_"+as[i].getAttribute("name");
						break;
					}
				}
			}else if(nodeValue.substring(0,2) == "@?"){//自定义处理
				
			}/*else if(nodeValue.substring(0,2) == "@?"){
				var preValue = nodeValue.substring(0,nodeValue.lastIndexOf("?")+1);
				var nodeId = nodeValue.substring(nodeValue.lastIndexOf("?")+1);
				for(var i=0;i<as.length;i++){
					if(nodeId == as[i].getAttribute("id")){
						nodeValue = preValue+as[i].getAttribute("name");
						break;
					}
				}
			}*/
		}
	}
	return nodeValue;
}
function initPerformer(xmlDoc,nodeValue){
	var ps = getParticipants(xmlDoc);
	if(ps == null) return nodeValue;
	var returnValue = new Array();
	if(typeof(nodeValue) != undefined && nodeValue != ""){
		var valArray = nodeValue.split(",");
		for(var i=0;i<valArray.length;i++){
			var v = valArray[i];
			for(var j=0;j<ps.length;j++){
				if(v == ps[j].getAttribute("id")){
					v = ps[j].getAttribute("name");
					break;
				}
			}
			returnValue.push(v);
		}
		nodeValue = returnValue.join(",");
	}
	return nodeValue;
}