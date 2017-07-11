/*!
 * 系统配置平台应用自定义核心JS
 */
(function( $, undefined ) {

// $.config 自定义配置展现命名空间

$.config = $.config || {};

$.extend( $.config, {
	version: "1.0.1",
	appActionPrefix : $.contextPath + "/appmanage",
	preButtonId : "___secbtn_",
	pattern : {
		_1C : "1C",
		_2E : "2E",
		_2U : "2U",
		_3E : "3E",
		_3L : "3L"
	},
	contentType : {
		form: "0",
		grid: "1",
		card: "2",
		search : "3",
		isForm : function (type) {
			return $.config.contentType.form == type;
		},
		isGrid : function (type) {
			return ($.config.contentType.grid == type || $.config.contentType.card == type);
		}
	},
	ui : {
		form : "form",
		grid : "grid"
	},
	DATA_HOST : "config-data-host",
	// 存储全局数据工具方法
	setGlobalData : function (ele, data) {
		if (ele.jquery) ele = ele.get(0);
		var _data = this.getGlobalData(ele) || {};
		$.extend(_data, data);
		$.data(ele, $.config.DATA_HOST, _data);
	},
	// 获取全局数据工具方法
	getGlobalData : function (ele) {
		if (ele.jquery) ele = ele.get(0);
		return $.data(ele, $.config.DATA_HOST) || {};
	},
	// 布局关系存储ID规则
	storeId : function (tableId, type) {
		if ($.config.contentType.isGrid(type)) {
			type = $.config.contentType.grid;
		}
		return "_" + tableId + "_" + type;
	},
	// 构件组装信息
	ASSEMBLE_HOST : "config-assemble-host",
	// 存放构件组装信息
	setAssembleData : function(ele, data) {
		if (ele.jquery) ele = ele.get(0);
		$.data(ele, $.config.ASSEMBLE_HOST, data);
	},
	// 获取构件组装信息
	getAssembleData : function(ele) {
		if (ele.jquery) ele = ele.get(0);
		return $.data(ele, $.config.ASSEMBLE_HOST);
	},
	// 时间戳标记
	TIMESTAMP_HOST : "config-timestamp-host",
	// 存放时间戳
	setTimestampData : function(ele, data) {
		if (ele.jquery) ele = ele.get(0);
		$.data(ele, $.config.TIMESTAMP_HOST, data);
	},
	// 获取时间戳
	getTimestampData : function(ele) {
		if (ele.jquery) ele = ele.get(0);
		return $.data(ele, $.config.TIMESTAMP_HOST);
	},
	// 自定义父列表构件标记
	CGRID_DIV_ID_HOST_M : "config-cgrid-div-id-host-m",
	// 缓存父列表构件ID
	setMasterCgridDivId : function(ele, data) {
		if (ele.jquery) ele = ele.get(0);
		$.data(ele, $.config.CGRID_DIV_ID_HOST_M, data);
	},
	// 获取父列表构件ID
	getMasterCgridDivId : function(ele) {
		if (ele.jquery) ele = ele.get(0);
		return $.data(ele, $.config.CGRID_DIV_ID_HOST_M);
	},
	// 根据从列表cgrid对象
	getDetailCgrid : function (global, tableId) {
		var data    = $.config.getGlobalData(global),
	        storeId = $.config.storeId(tableId, $.config.contentType.grid),
	    relation = null, detail = null;
		if (storeId in data) {
			relation = data[storeId];
		}
		if (relation && relation.detailId) {
			detail = data[relation.detailId];
		}
		return detail;
	},
	// 根据主列表cgrid对象
	getMasterCgrid : function (global, tableId) {
		var data    = $.config.getGlobalData(global),
	        storeId = $.config.storeId(tableId, $.config.contentType.grid),
	    relation = null, master = null;
		if (storeId in data) {
			relation = data[storeId];
		}
		if (relation && relation.masterId) {
			master = data[relation.masterId];
		}
		return master;
	},
	// 根据主表ID获取从表ID
	getDetailCgridTableId : function (global, tableId) {
		var data    = $.config.getGlobalData(global),
	        storeId = $.config.storeId(tableId, $.config.contentType.grid),
	    relation = null, detail = null;
		if (storeId in data) {
			relation = data[storeId];
		}
		if (relation && relation.detailId) {
			detail = data[relation.detailId];
		}
		return (detail && detail.tableId) ? detail.tableId : "";
	},
	// 根据从表ID获取主表ID
	getMasterCgridTableId : function (global, tableId) {
		var data    = $.config.getGlobalData(global),
	        storeId = $.config.storeId(tableId, $.config.contentType.grid),
	    relation = null, master = null;
		if (storeId in data) {
			relation = data[storeId];
		}
		if (relation && relation.masterId) {
			master = data[relation.masterId];
		}
		return (master && master.tableId) ? master.tableId : "";
	},
	// 构造configInfo对象
	makeupAssembleData : function(maxEle, nestedPanel) {
		return CFG_initConfigInfo({
			/** 页面名称 */
			"page" : "MT_page_",
			/** 页面中的最大元素 */
			"maxEleInPage" : maxEle,
			/** 获取构件嵌入的区域 */
			"getEmbeddedZone" : function () {
				return nestedPanel;
			},
			/** 初始化预留区 */
			"initReserveZones" : function(configInfo) {
				//CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', $('#toolbarId${idSuffix}').toolbar("getLength")-1);
			},
			/** 获取返回按钮添加的位置 */
			"setReturnButton" : function(configInfo) {
				//CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
			},
			/** 页面初始化的方法 */
			"bodyOnLoad" : function(configInfo) {
				//alert("bodyOnLoad");
				// 按钮权限控制
			}
		});
	}
});

// 将JSON对象转换为字符串
$.config.toString = function (obj) {
	
	if (!obj) return "";
	
	if (typeof obj === "function") {
		obj = obj();
		return $.config.toString(obj);
	}
	
	if (typeof obj !== "object") return obj;
	
	if (obj instanceof Array) return array2str(obj);
	
	var p, v, str = "";
	for (p in obj )	{
		v = obj[p];
		str += "," + "\"" + p + "\"" + ":";
		if (typeof v === "object") {
			str += $.config.toString(v);
		} else if (typeof v === "number" || typeof v === "boolean") {
			str +=  v;
		} else {
			v = null2empty(v);
			if (v.match(/\"/))  v = v.replace(/\"/g, "\\\"");
			if (v.match(/\\/))  v = v.replace(/\\/g, "\\\\");
			str += "\"" + v + "\"";
		}
	}
	
	if (str.length > 0) {
		str = str.substring(1);
	}
	
	return "{" + str + "}";
	
	/* 将数组转换为字符串.*/
	function array2str(arr) {
		var str = "", i = 0, len = arr.length, v;
		for (; i < len; i++) {
			str += ",";
			v    = arr[i];
			if (typeof v === "object") {
				if (v instanceof Array) {
					str += array2str(v);
				} else {
					str += $.config.toString(v);
				}
			} else if (typeof v === "number" || typeof v === "boolean") {
				str +=  v;
			} else {
				str += "\"" + v + "\"";
			}
		}
		
		if (str.length > 0) {
			str = str.substring(1);
		}
		
		return "[" + str + "]";
	}
};


})( jQuery );
