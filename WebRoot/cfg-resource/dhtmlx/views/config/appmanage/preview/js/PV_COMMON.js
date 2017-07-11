/* 模块展现中全局变量(回调函数与输出参数寄存对象).*/
var _M_this = this;

/* 自定义模块URI.*/
var PV_app_uri = contextPath + "/appmanage";

/* 字段与值的分隔符.*/
var PV_cv_split = "≡";

/* 字段与值的分隔符.*/
var PV_oc_split = "_C_";

/** 公用类(常量).*/
PV_common = function() {
	return {
		/* 布局内容.*/
		L_FORM:0,
		L_GRID:1,
		/* 表单与列表的预留区前缀.*/
		RZ_PRE_FORM: "RZ_F",
		RZ_PRE_GRID: "RZ_G",
		/* 按钮类型.*/
		B_BUTTON   :"b",
		B_SEPARATOR:"s",
		/* 预置按钮.*/
		P_CREATE: "create",
		P_SAVE  : "save",
		P_UPDATE: "update",
		P_DELETE: "delete",
		P_SEARCH: "search",
		P_REPORT: "report",
		P_SUBMIT: "submit",
		P_CLOSE : "close",
		P_SAVE_AND_CREATE: "saveAndCreate",
		P_SAVE_AND_SUBMIT: "saveAndSubmit",
		P_SAVE_AND_CLOSE : "saveAndClose",
		P_RESET : "reset", // 重置
		P_REPORT_PRE : "_subreport_",

		P_UPLOAD   : "upload",  //上传电子全文
		/* 按钮分隔符前缀.*/
		SEPARATOR_PRE: "PV_sep_",
		/* 检索方式.*/
		S_BASE : "b",  // 基本检索
		S_GREAT: "g",   // 高级检索
		P_FIRST_RECORD : "firstRecord", // 首条
	    P_PREVIOUS_RECORD : "previousRecord", // 上一条
	    P_NEXT_RECORD : "nextRecord", // 下一条
	    P_LAST_RECORD : "lastRecord" // 末条
	};
} ();

/**
 * 初始化指定区域内容
 * @param that
 *        全局变量
 * @param layout
 *        区域(dhtmlXLayoutObject)
 * @param tableId
 *        表ID
 * @param contentType
 *        内容形式：0-表单、1-列表
 * @param areaIndex
 *        区域索引位置：如区域一为1， 区域二为2 
 */
function PV_SubLayoutInit(that, layout, tableId, contentType, areaIndex) {
	if (that.fNum == undefined) {that.fNum = 0;}
	if (that.gNum == undefined) {that.gNum = 0;}
	if (PV_common.L_FORM == contentType) {
		// 表单
		that.fNum ++;
		PV_FORM_init(that, layout, tableId, areaIndex);
	} else if (PV_common.L_GRID == contentType) {
		// 列表
		that.gNum ++;
		PV_GRID_init(that, layout, tableId, areaIndex);
	} else {
		// 其他
	}
}
/**
 * 
 * @param config
 *        配置信息，格式：{id:"", parentId:"", title:"", width:"", height:""}
 */
function PV_CreateDhxWindow(config) {
	var subDhxWins = new dhtmlXWindows();
	if (config.parentId) {
		subDhxWins.enableAutoViewport(false);
		subDhxWins.attachViewportTo(config.parentId);
	}
	subDhxWins.setImagePath(IMAGE_PATH);
	var w = config.width ? config.width : 300;
	var h = config.height ? config.height : 400;
	var win = subDhxWins.createWindow(config.id, 0, 0, w, h);
	win.setModal((config.modal ? config.modal : true));
	win.button("park").hide();
	win.button("minmax1").hide();
	win.button("minmax2").hide();	
	win.setText(config.title);
	win.center();
	return win;
}
/**
 * JS对象属性名：用于存储表单相关操作
 * @param tableId
 * @returns {String}
 */
function PV_P_FormId(tableId) {
	return "FORM$" + tableId;
}
/**
 * JS对象属性名：用于存储列表相关操作
 * @param tableId
 * @returns {String}
 */
function PV_P_GridId(tableId) {
	return "GRID$" + tableId;
}
/**
 * JS对象属性名：用于存储列表对应的表ID值
 * @param index
 * @returns {String}
 */
function PV_P_GridTableId(index) {
	return "GRID$TABLEID$00" + index;
}
/**
 * 查看对象属性(测试用)
 * @param obj
 */
function PV_SeeProperty(obj) {
	if (null == obj) return;
	for (var prop in obj) {
		var data = obj[prop];
		alert("property=" + prop);
		if (typeof data == "object") {
			PV_SeeProperty(data);
		//} else if (typeof data == "function"){
		//	data();
		} else {
			alert("value=" + data);
		}
	}
}
