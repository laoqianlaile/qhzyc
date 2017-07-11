/**
 * 当前操作者选择时存放的变量
 * 作用：在节点选择参与者后，dataGrid的值显示显示name,将id按照存放在这个变量中
 * 当写入xml结构时改写成此变量的值
 */
var currentPerformer = "";


/**
 * 当前参与者选择时存放的变量
 * 作用：当选择公式-->与操作者相同的节点时将节点id存在在此,dataGrid显示name,
 * 当写入xml结构时改成成此变量的值
 */
var currentParticipant = "";

var currentActualParm = "";

var currentToolIds = [];

var transTrueAndFalse2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "TRUE"){
			return "是";
		}else if(value.toUpperCase() == "FALSE"){
			return "否";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transTrueAndFalse2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "是"){
			return "TRUE";
		}else if(value == "否"){
			return "FALSE";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transDispatchType2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "COMPETE"){
			return "抢占";
		}else if(value.toUpperCase() == "ORDER"){
			return "顺序";
		}else if(value.toUpperCase() == "OUTORDER"){
			return "无序";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transDispatchType2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "抢占"){
			return "COMPETE";
		}else if(value == "顺序"){
			return "ORDER";
		}else if(value == "无序"){
			return "OUTORDER";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transUnit2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "MONTH"){
			return "月";
		}else if(value.toUpperCase() == "DAY"){
			return "日";
		}else if(value.toUpperCase() == "HOUR"){
			return "时";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transUnit2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "月"){
			return "MONTH";
		}else if(value == "日"){
			return "DAY";
		}else if(value == "时"){
			return "HOUR";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transImpl2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "NO"){
			return "无";
		}else if(value.toUpperCase() == "TOOL"){
			return "工具";
		}else if(value.toUpperCase() == "SUBFLOW"){
			return "子流程";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transImpl2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "无"){
			return "No";
		}else if(value == "工具"){
			return "Tool";
		}else if(value == "子流程"){
			return "SubFlow";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transStartAndFinishMode2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "AUTOMATIC"){
			return "自动";
		}else if(value.toUpperCase() == "MANUAL"){
			return "手动";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transStartAndFinishMode2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "自动"){
			return "AUTOMATIC";
		}else if(value == "手动"){
			return "MANUAL";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transJoinAndSplit2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "AND"){
			return "与";
		}else if(value.toUpperCase() == "OR"){
			return "或";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transJoinAndSplit2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "与"){
			return "AND";
		}else if(value == "或"){
			return "OR";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transFormalParamMode2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "IN"){
			return "输入";
		}else if(value.toUpperCase() == "INOUT"){
			return "输入输出";
		}else if(value.toUpperCase() == "OUT"){
			return "输出";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transFormalParamMode2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "输入"){
			return "IN";
		}else if(value == "输入输出"){
			return "INOUT";
		}else if(value == "输出"){
			return "OUT";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transImplType2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "PROCEDURE"){
			return "过程";
		}else if(value.toUpperCase() == "WEBPAGE"){
			return "页面";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transImplType2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "过程"){
			return "PROCEDURE";
		}else if(value == "页面"){
			return "WEBPAGE";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transSubFlowType2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "ASYNCHR"){
			return "异步";
		}else if(value.toUpperCase() == "SYNCHR"){
			return "同步";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transSubFlowType2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "异步"){
			return "ASYNCHR";
		}else if(value == "同步"){
			return "SYNCHR";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transDataType2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "STRING"){
			return "字符型";
		}else if(value.toUpperCase() == "BOOLEAN"){
			return "布尔型";
		}else if(value.toUpperCase() == "INTEGER"){
			return "整型";
		}else if(value.toUpperCase() == "FLOAT"){
			return "浮点型";
		}else if(value.toUpperCase() == "DATETIME"){
			return "日期型";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transDataType2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "字符型"){
			return "STRING";
		}else if(value == "布尔型"){
			return "BOOLEAN";
		}else if(value == "整型"){
			return "INTEGER";
		}else if(value == "浮点型"){
			return "FLOAT";
		}else if(value == "日期型"){
			return "DATETIME";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transParticipantType2Chinese = function(value){
	if(typeof(value) != undefined && value != null){
		if(value.toUpperCase() == "DEPARTMENT"){
			return "指定部门";
		}else if(value.toUpperCase() == "HUMAN"){
			return "指定用户";
		}else if(value.toUpperCase() == "FORMULA"){
			return "公式";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transParticipantType2English = function(value){
	if(typeof(value) != undefined && value != null){
		if(value == "指定部门"){
			return "DEPARTMENT";
		}else if(value == "指定用户"){
			return "HUMAN";
		}else if(value == "公式"){
			return "FORMULA";
		}else{
			return value;
		}
	}else{
		return "";
	}
};
var transToolId2Chinese = function(value,options){
	var returnValue = value;
	if(typeof(value) != undefined && typeof(options) != undefined && value != null){
		for(var i=0;i<options.length;i++){
			if(options[i].id == value){
				returnValue = options[i].name;
				break;
			}
		}
	}
	return returnValue;
};
var transToolId2English = function(value,options){
	var returnValue = value;
	if(typeof(value) != undefined && typeof(options) != undefined && value != null){
		for(var i=0;i<options.length;i++){
			if(options[i].name == value){
				returnValue = options[i].id;
				break;
			}
		}
	}
	return returnValue;
};

var transActualParam2Chinese = function(nodeValue,workflowProcess){
	var ps = getDataFields(workflowProcess);
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
};
var transSubFlowId2English = function(value,xmlDoc){
	var returnValue = "";
	if(typeof(value) != undefined && value != null){
		var workflows = selectNodes(xmlDoc,xmlDoc,".//WorkflowProcess");
		if(workflows != null){
			for(var i=0;i<workflows.length;i++){
				if(value == workflows[i].getAttribute("name")){
					returnValue = workflows[i].getAttribute("id");
					break;
				}
			}
		}
	}
	return returnValue;
};
