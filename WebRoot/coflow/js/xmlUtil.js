/**
 * 将string转换成dom对象
 */
var stringToXml = function(xmlString){
        var xmlDoc=null;
        //判断浏览器的类型
        //支持IE浏览器 
        if((!!window.ActiveXObject || "ActiveXObject" in window)){   //window.DOMParser 判断是否是非ie浏览器
            var xmlDomVersions = ['MSXML.2.DOMDocument.6.0','MSXML.2.DOMDocument.3.0','Microsoft.XMLDOM'];
            for(var i=0;i<xmlDomVersions.length;i++){
                try{
                    xmlDoc = new ActiveXObject(xmlDomVersions[i]);
                    xmlDoc.async = false;
                    if(xmlString != "")
                    	xmlDoc.loadXML(xmlString); //loadXML方法载入xml字符串
                    break;
                }catch(e){
                }
            }
        }
        //支持Mozilla浏览器
        else if(window.DOMParser && document.implementation && document.implementation.createDocument){
            try{
                /* DOMParser 对象解析 XML 文本并返回一个 XML Document 对象。
                 * 要使用 DOMParser，使用不带参数的构造函数来实例化它，然后调用其 parseFromString() 方法
                 * parseFromString(text, contentType) 参数text:要解析的 XML 标记 参数contentType文本的内容类型
                 * 可能是 "text/xml" 、"application/xml" 或 "application/xhtml+xml" 中的一个。注意，不支持 "text/html"。
                 */
                domParser = new  DOMParser();
                if(xmlString != "")
                	xmlDoc = domParser.parseFromString(xmlString, 'text/xml');
                else
                	xmlDoc = document.implementation.createDocument('','',null);
            }catch(e){
            }
        }
        else{
            return null;
        }
        return xmlDoc;
    };
/**
 * 创建xml基本结构
 * rootId xml根id 
 */
var creatBaseXml = function(xmlDoc, rootId, name, vendor, author, description, scriptType){
	//文档类型
	var docType = xmlDoc.createProcessingInstruction("xml","version='1.0' encoding='utf-8'");
	xmlDoc.appendChild(docType);
	//文档根元素
	var root = xmlDoc.createElement("Package");
	root.setAttribute("id",rootId);
	root.setAttribute("name",name);
	xmlDoc.appendChild(root);
	
	//包头
	var packageHeader = xmlDoc.createElement("PackageHeader");
	var currentDate = new Date();
	//创建时间
	var created = xmlDoc.createElement("Created");
	created.appendChild(xmlDoc.createTextNode(currentDate.toUTCString()));
	//供应商
	var vendor1 = xmlDoc.createElement("Vendor");
	vendor1.appendChild(xmlDoc.createTextNode(typeof(vendor)=='undefined'?"":vendor));
	//作者
	var author1 = xmlDoc.createElement("Author");
	author1.appendChild(xmlDoc.createTextNode(typeof(author)=='undefined'?"":author));
	//描述
	var description1 = xmlDoc.createElement("Description");
	description1.appendChild(xmlDoc.createTextNode(typeof(description)=='undefined'?"":description));
	
	packageHeader.appendChild(created);
	packageHeader.appendChild(vendor1);
	packageHeader.appendChild(author1);
	packageHeader.appendChild(description1);
	root.appendChild(packageHeader);
	
	//脚本类型
	var script = xmlDoc.createElement("Script");
	script.setAttribute("type",scriptType);
	root.appendChild(script);
	
	//参与者集
	var participants = xmlDoc.createElement("Participants");
	root.appendChild(participants);
	
	//应用程序集
	var applications = xmlDoc.createElement("Applications");
	root.appendChild(applications);
	
	//流程集
	var workflowProcesses = xmlDoc.createElement("WorkflowProcesses");
	root.appendChild(workflowProcesses);
	
	//扩展属性集
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	root.appendChild(extendedAttributes);
	return xmlDoc;
	//alert(xmlToString(xmlDoc));
};

/**
 * 创建流程
 */
var createWorkflowProcess = function(xmlDoc){
	//流程
	var workflowProcess = xmlDoc.createElement("WorkflowProcess");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"name","流程"+idIndex)){
		idIndex++;
	}
	workflowProcess.setAttribute("name","流程"+idIndex);
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","workflowprocess"+idIndex)){
		idIndex++;
	}
	workflowProcess.setAttribute("id","workflowprocess"+idIndex);
	
	//形参集
	var formalParameters = xmlDoc.createElement("FormalParameters");
	workflowProcess.appendChild(formalParameters);
	
	//相关数据集
	var dataFields = xmlDoc.createElement("DataFields");
	workflowProcess.appendChild(dataFields);
	
	//节点集
	var activities = xmlDoc.createElement("Activities");
	workflowProcess.appendChild(activities);
	
	//流转集
	var transitions = xmlDoc.createElement("Transitions");
	workflowProcess.appendChild(transitions);
	
	//扩展属性集
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	workflowProcess.appendChild(extendedAttributes);
	
	xmlDoc.getElementsByTagName("WorkflowProcesses")[0].appendChild(workflowProcess);
	
	return workflowProcess;
};

/**
 * 创建节点
 */
var createActivity = function(xmlDoc,workflowProcessId,activityType,x,y,and,or){
	var activity = xmlDoc.createElement("Activity");
	if(activityType == "START"){
		activity.setAttribute("name","开始");
		activity.setAttribute("type","START");
	}else if(activityType == "FINISH"){
		activity.setAttribute("name","结束");
		activity.setAttribute("type","FINISH");
	}else{
		var idIndex = 0;
		while(checkAttr(xmlDoc,"name","节点"+idIndex)){
			idIndex++;
		}
		activity.setAttribute("name","节点"+idIndex); 
		activity.setAttribute("type","NORMAL"); 
	}
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","activity"+idIndex)){
		idIndex++;
	}
	activity.setAttribute("id","activity"+idIndex);
	
	//分派类型
	var dispatchType = xmlDoc.createElement("DispatchType");
	dispatchType.setAttribute("type","COMPETE");
	activity.appendChild(dispatchType);
	
	//实现
	var implementation = xmlDoc.createElement("Implementation");
	var no = xmlDoc.createElement("No");
	implementation.appendChild(no);
	activity.appendChild(implementation);
	
	//参与者
	var performer = xmlDoc.createElement("Performer");
	performer.appendChild(xmlDoc.createTextNode(""));
	activity.appendChild(performer);
	
	//人员控制
	var performerConrol = xmlDoc.createElement("PerformerChanageFlag");
	performerConrol.appendChild(xmlDoc.createTextNode("FALSE"));
	activity.appendChild(performerConrol);
	
	//开始模式
	var startMode = xmlDoc.createElement("StartMode");
	if(activityType == "START" || activityType == "FINISH"){
		startMode.appendChild(xmlDoc.createTextNode("AUTOMATIC"));
	}else{
		startMode.appendChild(xmlDoc.createTextNode("MANUAL"));
	}
	activity.appendChild(startMode);
	
	//结束模式
	var finishMode = xmlDoc.createElement("FinishMode");
	if(activityType == "START" || activityType == "FINISH"){
		finishMode.appendChild(xmlDoc.createTextNode("AUTOMATIC"));
	}else{
		finishMode.appendChild(xmlDoc.createTextNode("MANUAL"));
	}
	activity.appendChild(finishMode);
	
	var transitionRestrictions = xmlDoc.createElement("TransitionRestrictions");
	var transitionRestriction = xmlDoc.createElement("TransitionRestriction");
	//合类型
	var join = xmlDoc.createElement("Join");
	join.setAttribute("type",and);
	transitionRestriction.appendChild(join);
	//分类型
	var split = xmlDoc.createElement("Split");
	split.setAttribute("type",or);
	transitionRestriction.appendChild(split);
	transitionRestrictions.appendChild(transitionRestriction);
	activity.appendChild(transitionRestrictions);
	
	//描述
	var discription = xmlDoc.createElement("Description");
	discription.appendChild(xmlDoc.createTextNode(""));
	activity.appendChild(discription);
	
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	var extendedAttribute_x = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_x.setAttribute("name","CLFLOW_LOCATION_X");
	extendedAttribute_x.setAttribute("value",x);
	extendedAttributes.appendChild(extendedAttribute_x);
	
	var extendedAttribute_y = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_y.setAttribute("name","CLFLOW_LOCATION_Y");
	extendedAttribute_y.setAttribute("value",y);
	extendedAttributes.appendChild(extendedAttribute_y);
	
	var extendedAttribute_show_next_page = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_show_next_page.setAttribute("name","CLFLOW_SHOW_NEXT_PAGE");
	extendedAttribute_show_next_page.setAttribute("value","TRUE");
	extendedAttributes.appendChild(extendedAttribute_show_next_page);
	
	var extendedAttribute_maxLine= xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_maxLine.setAttribute("name","CLFLOW_MAXLINE");
	extendedAttribute_maxLine.setAttribute("value","1");
	extendedAttributes.appendChild(extendedAttribute_maxLine);
	
	var extendedAttribute_minLine = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_minLine.setAttribute("name","CLFLOW_MINLINE");
	extendedAttribute_minLine.setAttribute("value","1");
	extendedAttributes.appendChild(extendedAttribute_minLine);
	
	var extendedAttribute_freeIn = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_freeIn.setAttribute("name","FREEIN");
	
	var extendedAttribute_freeOut = xmlDoc.createElement("ExtendedAttribute");
	extendedAttribute_freeOut.setAttribute("name","FREEOUT");
	
	if(activityType == "FINISH"){
		extendedAttribute_freeIn.setAttribute("value","TRUE");
		extendedAttributes.appendChild(extendedAttribute_freeIn);
	}else if(activityType != "START"){
		extendedAttribute_freeOut.setAttribute("value","TRUE");
		extendedAttributes.appendChild(extendedAttribute_freeOut);
	}
	
	activity.appendChild(extendedAttributes);
	
	var activities = getActivitiesNode(xmlDoc,workflowProcessId);
	activities.appendChild(activity);
	return activity;
	
};
/**
 * 创建流传
 */
var createTransition = function(xmlDoc,workflowProcessId,fromId,toId){
	var transition = xmlDoc.createElement("Transition");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"name","流传"+idIndex)){
		idIndex++;
	}
	transition.setAttribute("name","流传"+idIndex);
	transition.setAttribute("to",toId);
	transition.setAttribute("from",fromId)
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","transition"+idIndex)){
		idIndex++;
	}
	transition.setAttribute("id","transition"+idIndex);
	
	//扩展属性集
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	transition.appendChild(extendedAttributes);
	transitions = getTransitionsNode(xmlDoc,workflowProcessId);
	transitions.appendChild(transition);
	return transition;
};

/**
 * 创建参与者
 */
var createParticipant = function(xmlDoc){
	var participant = xmlDoc.createElement("Participant");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"name","参与者"+idIndex)){
		idIndex++;
	}
	participant.setAttribute("name","参与者"+idIndex);
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","partiicipant"+idIndex)){
		idIndex++;
	}
	participant.setAttribute("id","partiicipant"+idIndex);
	var participantType = xmlDoc.createElement("ParticipantType");
	participant.appendChild(participantType);
	var discription = xmlDoc.createElement("Description");
	discription.appendChild(xmlDoc.createTextNode(""));
	participant.appendChild(discription);
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	participant.appendChild(extendedAttributes);
	var participants = xmlDoc.getElementsByTagName("Participants")[0];
	participants.appendChild(participant);
	return participant;
};

/**
 * 创建应用程序
 */
var createApplication = function(xmlDoc){
	var application = xmlDoc.createElement("Application");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"name","应用程序"+idIndex)){
		idIndex++;
	}
	application.setAttribute("name","应用程序"+idIndex);
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","application"+idIndex)){
		idIndex++;
	}
	application.setAttribute("id","application"+idIndex);
	//形参集
	var formalParameters = xmlDoc.createElement("FormalParameters");
	application.appendChild(formalParameters);
	var discription = xmlDoc.createElement("Description");
	discription.appendChild(xmlDoc.createTextNode(""));
	application.appendChild(discription);
	var extendedAttributes = xmlDoc.createElement("ExtendedAttributes");
	application.appendChild(extendedAttributes);
	var applications = xmlDoc.getElementsByTagName("Applications")[0];
	applications.appendChild(application);
	return application;
};

/**
 *创建形参 
 */
var createFormalParameter = function(xmlDoc,workflowProcess){
	var formalParameter = xmlDoc.createElement("FormalParameter");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"index",idIndex)){
		idIndex++;
	}
	formalParameter.setAttribute("index",idIndex);
	formalParameter.setAttribute("mode","IN");
	idIndex = 0;
	while(checkAttr(xmlDoc,"id","formalparameter"+idIndex)){
		idIndex++;
	}
	formalParameter.setAttribute("id","formalparameter"+idIndex);
	var dataType = xmlDoc.createElement("DataType");
	var basicType = xmlDoc.createElement("BasicType");
	basicType.setAttribute("type","STRING");
	dataType.appendChild(basicType);
	formalParameter.appendChild(dataType);
	
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		if(workflowProcess.childNodes[i].nodeName == "FormalParameters"){
			var fps = workflowProcess.childNodes[i];
			fps.appendChild(formalParameter);
			break;
		}
	}
	return formalParameter;
};
/**
 * 创建相关数据
 */
var createDataField = function(xmlDoc,workflowProcess){
	var dataField = xmlDoc.createElement("DataField");
	var idIndex = 0;
	while(checkAttr(xmlDoc,"name","相关数据"+idIndex)){
		idIndex++;
	}
	dataField.setAttribute("name","相关数据"+idIndex);
	var idPrefix = "";
	idIndex = 97;
	while(checkAttr(xmlDoc,"id","datafield"+(idPrefix+String.fromCharCode(idIndex)))){
		if(idIndex == 122){
			idIndex = 97;
			idPrefix += "a";
		}
		idIndex++;
	}
	dataField.setAttribute("id","datafield"+(idPrefix+String.fromCharCode(idIndex)));
	var dataType = xmlDoc.createElement("DataType");
	var basicType = xmlDoc.createElement("BasicType");
	basicType.setAttribute("type","STRING");
	dataType.appendChild(basicType);
	dataField.appendChild(dataType);
	
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		if(workflowProcess.childNodes[i].nodeName == "DataFields"){
			var dfs = workflowProcess.childNodes[i];
			dfs.appendChild(dataField);
			break;
		}
	}
	return dataField;
};
/**
 * 创建导入的相关数据
 * xmlDoc 对象
 * workflowProcess 当前流程
 * dataFieldObj 要创建的相关数据对象
 */
var createImportDataField = function(xmlDoc,workflowProcess,dataFieldObj){
	var dataField = xmlDoc.createElement("DataField");
	dataField.setAttribute("name",dataFieldObj.name);
	dataField.setAttribute("id",dataFieldObj.id);
	var dataType = xmlDoc.createElement("DataType");
	var basicType = xmlDoc.createElement("BasicType");
	basicType.setAttribute("type",dataFieldObj.dataType==null?"STRING":dataFieldObj.dataType.toUpperCase());
	dataType.appendChild(basicType);
	dataField.appendChild(dataType);
	
	var initValue = xmlDoc.createElement("InitialValue");
	initValue.appendChild(xmlDoc.createTextNode(dataFieldObj.initValue));
	dataField.appendChild(initValue);
	
	var desc = xmlDoc.createElement("Description");
	desc.appendChild(xmlDoc.createTextNode(dataFieldObj.desc));
	dataField.appendChild(desc);
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		if(workflowProcess.childNodes[i].nodeName == "DataFields"){
			var dfs = workflowProcess.childNodes[i];
			dfs.appendChild(dataField);
			break;
		}
	}
	return dataField;
};
/**
 * 创建实现工具
 */
var createTool = function(xmlDoc,activity){
	var tool = xmlDoc.createElement("Tool");
	tool.setAttribute("type","");
	tool.setAttribute("id","");
	var ap = xmlDoc.createElement("ActualParameters");
	tool.appendChild(ap);
	var desc = xmlDoc.createElement("Description");
	desc.appendChild(xmlDoc.createTextNode(""));
	tool.appendChild(desc);
	var ext = xmlDoc.createElement("ExtendedAttributes");
	tool.appendChild(ext);
	var node = getSubNode(activity,"Implementation");
	node.appendChild(tool);
	return tool;
}
/**
 * 创建子流程
 */
var createSubFlow = function(xmlDoc,activity){
	var subflow = xmlDoc.createElement("SubFlow");
	subflow.setAttribute("id","");
	var ap = xmlDoc.createElement("ActualParameters");
	subflow.appendChild(ap);
	var node = getSubNode(activity,"Implementation");
	node.appendChild(subflow);
	return subflow;
}
/**
 * 获取流程下的Activities节点
 */
var getActivitiesNode = function(xmlDoc,workflowProcessId){
	var activities = null;
	var workflowProcesses = xmlDoc.getElementsByTagName("WorkflowProcesses")[0];
	for(var i=0;i<workflowProcesses.childNodes.length;i++){
		var node = workflowProcesses.childNodes[i];
		if(node.nodeName == "WorkflowProcess"){
			if(node.getAttribute("id") == workflowProcessId){
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "Activities"){
						activities = node2;
						break;
					}
				}
				break;
			}
		}
	}
	return activities;
};
/**
 * 获取流程下的Transitions节点
 */
var getTransitionsNode = function(xmlDoc,workflowProcessId){
	var transitions = null;
	var workflowProcesses = xmlDoc.getElementsByTagName("WorkflowProcesses")[0];
	for(var i=0;i<workflowProcesses.childNodes.length;i++){
		var node = workflowProcesses.childNodes[i];
		if(node.nodeName == "WorkflowProcess"){
			if(node.getAttribute("id") == workflowProcessId){
				for(var j=0;j<node.childNodes.length;j++){
					var node2 = node.childNodes[j];
					if(node2.nodeName == "Transitions"){
						transitions = node2;
						break;
					}
				}
				break;
			}
		}
	}
	return transitions;
};
/**
 * 得到指定节点的字节点,深度为1
 */
var getSubNode = function(parentNode,nodeName){
	var node = null;
	for(var i=0;i<parentNode.childNodes.length;i++){
		if(parentNode.childNodes[i].nodeName == nodeName){
			node = parentNode.childNodes[i];
			break;
		}
	}
	return node;
};

/**
 * 移除所有子节点
 */
var removeAllSubNode = function(parentNode){
	var len = parentNode.childNodes.length;
	for(var i=0;i<len;i++){
		parentNode.removeChild(parentNode.childNodes[0]);
	}
};

/**
 * 获取节点下实现的所有工具
 */
var getActivityTools = function(activity){
	var tools = [];
	var node = getSubNode(activity,"Implementation");
	for(var i=0;i<node.childNodes.length;i++){
		if(node.childNodes[i].nodeName == "Tool"){
			tools.push(node.childNodes[i]);
		}
		
	}
	return tools;
};
/**
 * 获取节点的子流程实现
 */
var getActivitySubFlow = function(activity){
	var subflow = null;;
	var node = getSubNode(activity,"Implementation");
	for(var i=0;i<node.childNodes.length;i++){
		if(node.childNodes[i].nodeName == "SubFlow"){
			subflow = node.childNodes[i];
			break;
		}
	}
	return subflow;
};

/**
 * 获取所有应用程序
 */
var getApplications = function(xmlDoc){
	var apps = [];
	var as = xmlDoc.getElementsByTagName("Applications")[0];
	for(var i=0;i<as.childNodes.length;i++){
		if(as.childNodes[i].nodeName != "#text"){
			apps.push(as.childNodes[i]);
		}
	}
	return apps;
};
/**
 * 获取流程的所有形参
 */
var getFormalParameters = function(workflowProcess){
	var fps =[];
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		var node = workflowProcess.childNodes[i];
		if(node.nodeName == "FormalParameters"){
			for(var j=0;j<node.childNodes.length;j++){
				var node2 = node.childNodes[j];
				if(node2.nodeName == "FormalParameter"){
					fps.push(node2);
				}
			}
			break;
		}
	}
	return fps;
};
/**
 * 获取流程的所有相关数据
 */
var getDataFields = function(workflowProcess){
	var dfs =[];
	for(var i=0;i<workflowProcess.childNodes.length;i++){
		var node = workflowProcess.childNodes[i];
		if(node.nodeName == "DataFields"){
			for(var j=0;j<node.childNodes.length;j++){
				var node2 = node.childNodes[j];
				if(node2.nodeName == "DataField"){
					dfs.push(node2);
				}
			}
			break;
		}
	}
	return dfs;
};

/**
 * 获取所有流程
 */
var getWorkflowProcesses = function(xmlDoc){
	var wps = xmlDoc.getElementsByTagName("WorkflowProcess");
	return wps;
};

/**
 * 获取所有参与者
 */
var getParticipants = function(xmlDoc){
	var ps = xmlDoc.getElementsByTagName("Participant");
	return ps;
};

/**
 * 获取所有节点
 */
var getActivities = function(xmlDoc){
	var as = xmlDoc.getElementsByTagName("Activity");
	return as;
};

/**
 * 检查属性值是否存在
 * xmlDoc 对象
 * attrKey 属性key
 * value 属性值
 */

var checkAttr = function(xmlDoc,attrkey,value){
	var packageNode = xmlDoc.getElementsByTagName("Package")[0];
	var attrValue = packageNode.getAttribute(attrkey);
	var isExist = false;
	if(attrValue != null && attrValue == value){
		isExist = true;
		return isExist;
	}
	getMaxIdIndex(packageNode);
	function getMaxIdIndex(packageNode){
		for(var i=0;i<packageNode.childNodes.length;i++){
			if(packageNode.childNodes[i].nodeName != "#text" && packageNode.childNodes[i].nodeName != "#cdata-section"){
				attrValue = packageNode.childNodes[i].getAttribute(attrkey);
				if(attrValue != null && attrValue == value){
					isExist = true;
					break;
				}
				getMaxIdIndex(packageNode.childNodes[i]);
			}
		}
	}
	return isExist;
};

//包视图树生成
var packageTree = function(xmlDoc){
	var zNodes = [];
	var Node = function(id,pId,name,isOpen,obj){
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.open = isOpen;
		this.showProps = function(treeNode){
			showPackageProps(obj,treeNode);
		};
		this.remove = function(){
			var parentNode = obj.parentNode;
			parentNode.removeChild(obj);
		};
	};
	var packageNode = xmlDoc.getElementsByTagName("Package")[0];
	var root = new Node(packageNode.getAttribute("id"),null,packageNode.getAttribute("name"),true,packageNode);
	zNodes.push(root);
	var participants = new Node("ps",root.id,"参与者集",true,null);
	zNodes.push(participants);
	var ps = xmlDoc.getElementsByTagName("Participant");
	for(var i=0;i<ps.length;i++){
		var p = new Node(ps[i].getAttribute("id"),"ps",ps[i].getAttribute("name"),false,ps[i]);
		zNodes.push(p);
	}
	
	var applications = new Node("as",root.id,"应用程序集",true);
	zNodes.push(applications);
	var as = xmlDoc.getElementsByTagName("Application");
	for(var i=0;i<as.length;i++){
		var a = new Node(as[i].getAttribute("id"),"as",as[i].getAttribute("name"),false,as[i]);
		zNodes.push(a);
	}
	var workflowProcesses = new Node("ws",root.id,"流程集",true);
	zNodes.push(workflowProcesses);
	var ws = xmlDoc.getElementsByTagName("WorkflowProcess");
	for(var i=0;i<ws.length;i++){
		var w = new Node(ws[i].getAttribute("id"),"ws",ws[i].getAttribute("name"),false,ws[i]);
		zNodes.push(w);
	}
	return zNodes;
}

//xml转换为string
function xmlToString(xmlDoc) {
	return $.xslt.xmlToText(xmlDoc);
}

/**
 * 选择节点集合
 * @param xmlDoc xmlDOM对象
 * @param contextNode 当前节点
 * @param xPath xPath表达式
 * @returns
 */
function selectNodes(xmlDoc,contextNode,xPath){
	if (!!window.ActiveXObject || "ActiveXObject" in window){
		var result = contextNode.selectNodes(xPath);
		return result;
	}else{
		var result = xmlDoc.evaluate(xPath, contextNode, null, XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);
		var nodes = new Array();
		var node = null;
		while((node = result.iterateNext()) != null){
			nodes.push(node);
		}
		return nodes;
	}
}
/**
 * 选择节点
 * @param xmlDoc xmlDOM对象
 * @param contextNode 当前节点
 * @param xPath xPath表达式
 * @returns
 */
function selectNode(xmlDoc,contextNode,xPath){
	if (!!window.ActiveXObject || "ActiveXObject" in window){
		var result = contextNode.selectSingleNode(xPath);
		return result;
	}else{
		var result = xmlDoc.evaluate(xPath, contextNode, null, XPathResult.ANY_UNORDERED_NODE_TYPE, null);
		return result.singleNodeValue;
	}
}