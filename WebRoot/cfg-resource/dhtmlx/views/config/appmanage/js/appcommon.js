/* 自定义访问地址*/
var APP_PATH = contextPath + "/appmanage";
/* 应用定义中具体宽度与高度集合.*/
var APP_wincfg = {
		search: {title:"检索配置", width: 700, height: 500},
		column: {title:"列表字段配置", width: 1000, height: 600},
		sort: {title:"排序字段配置", width: 620, height: 520}
	};
/**
 * 初始化列表
 * @param grid
 * @param gridcfg
 * @param url
 */
function initGridWithoutColumnsAndPageable(grid, gridcfg, url) {
	initGridWithoutPageable(grid, gridcfg, url);
}
/**
 * 初始化列表
 * @param grid
 * @param gridcfg
 * @param url
 */
function initGridWithoutPageable(grid, gridcfg, url) {
	// 1. init grid 
	grid.setImagePath(IMAGE_PATH);
	grid.setHeader(gridcfg.format.headers.toString());
	grid.setInitWidths(gridcfg.format.colWidths.toString());
	grid.setColTypes(gridcfg.format.colTypes.toString());
	grid.setColAlign(gridcfg.format.colAligns.toString());
	grid.setSkin(Skin);
	grid.init();
	if (gridcfg.multselect != undefined && false == gridcfg.multselect) {
		grid.enableMultiselect(false);
	} else {
		grid.enableMultiselect(true);
	}
	
	grid.setStyle("font-weight:bold;", "", "", "");
	// 2. load grid data
	loadGridData(grid, gridcfg, url);
}
/**
 * 加载列表数据
 * @param grid
 * @param gridcfg
 * @param url
 */
function loadGridData(grid, gridcfg, url) {
	if (!grid || !grid.clearAll) return;
	grid.clearAll();
	if (isEmpty(url)) return;
	var datas = null;
	if (typeof url === "string") {
		datas = loadJson(url);
	} else {
		datas = url;
	}
	if (undefined != datas.rows) {
		grid.parse(datas, "json");
		return;
	}
	var rows = datas.data ? datas.data : datas;
	var cfg = cfg = {"rows":rows, "columns":gridcfg.format.cols, "id":gridcfg.format.id, "userdata":gridcfg.format.userdata};
	grid.parse(jsonArray2DhtmlxJsonGridData(cfg), "json");
}

/**
 * 加载列表数据并返回其结果值
 * @param grid
 * @param gridcfg
 * @param url
 */
function loadGridDataResult(grid, gridcfg, url) {
	grid.clearAll();
	if (null == url) return;
	if (url.indexOf("&P_timestamp=") < 0) {
		url = addTimestamp(url);
	}
	var datas = loadJson(url);
	if (undefined != datas.rows) {
		grid.parse(datas, "json");
		return;
	}
	var rows = datas.data ? datas.data : datas;
	var cfg = cfg = {"rows":rows, "columns":gridcfg.format.cols, "id":gridcfg.format.id, "userdata":gridcfg.format.userdata};
	grid.parse(jsonArray2DhtmlxJsonGridData(cfg), "json");
	return jsonArray2DhtmlxJsonGridData(cfg);
}

/**
 * 
 * @param cfg 
 *        1. {"rows":[{"id":"","name":"","code":""},{"id":"","name":"","code":""}], "columns":["id","name"], "id":["id"], "userdata":["code"]};
 *        2. {"rows":[["1","a","1a","1b"],["2","b","2b","2a"]], "columns":[1,2,3], "id":[0], "userdata":[1,3]};
 * @returns {___anonymous657_658}
 */
function jsonArray2DhtmlxJsonGridData(cfg){
	var datas = {};
	datas.rows = [];
	//datas.rows[0] = {id:'-1',data:['a','b','c','d','e','f']};
	if (null != cfg.rows) {
		var jsonArray = cfg.rows;
		if (null == jsonArray || jsonArray.length == 0) {
			return datas;
		}
		// 如果数组中的值不是键值对(key:value)，隐藏位的名称需要设置默认前缀
		var userdataPre = "";
		if (undefined != jsonArray[0][0]) {
			userdataPre = "userdata_";
		}
		//ID值所在数组中的索引位置
		var idIdxs       = null;
		//一行中的每一列中数组中的索引位置
		var columnIdxs   = null;
		//一行中的每个隐藏值在数组中的索引位置
		var userdataIdxs = null;
		if (cfg.id) { var idIdxs = cfg.id; }
		if (cfg.columns) { var columnIdxs = cfg.columns; }
		if (cfg.userdata) { var userdataIdxs = cfg.userdata; }
		// build up dhtmlxGrid json data
		var i = j = 0;
		if (columnIdxs != null) {
			for(i=0; i<jsonArray.length; i++) {
				var row = {};
				if (null != idIdxs && 1 == idIdxs.length) {
					row.id = jsonArray[i][idIdxs[0]];
				} else if (null != idIdxs && idIdxs.length > 1) {
					var id = "";
					for (j = 0; j < idIdxs.length; j++) {
						id += ";" + null2empty(jsonArray[i][idIdxs[j]]);
					}
					row.id = id.substring(1); j = 0;
				} else {
					row.id = jsonArray[i].id;// jsonArray[i] = {"id":"12334","name":"author",...};
				}
				
				row.data = [];
				//1. grid data 
				for (j = 0; j < columnIdxs.length; j++) {
					row.data[j] = null2empty(jsonArray[i][columnIdxs[j]]);
				}
				j = 0;
				//2. grid userdata
				if (userdataIdxs) {
					row.userdata = {};
					for (j = 0; j < userdataIdxs.length; j++) {
						var prop = ("" == userdataPre) ? userdataIdxs[j] : (userdataPre + j);
						row.userdata[prop] = null2empty(jsonArray[i][userdataIdxs[j]]);
					}
					j = 0;
				}
				datas.rows[i] = row;
			}
		} else {
			for(i=0; i<jsonArray.length; i++) {
				var row = {};
				if (null != idIdxs && 1 == idIdxs.length) {
					row.id = jsonArray[i][idIdxs[0]];
				} else if (null != idIdxs && idIdxs.length > 1) {
					var id = "";
					for (j = 0; j < idIdxs.length; j++) {
						id += ";" + null2empty(jsonArray[i][idIdxs[j]]);
					}
					row.id = id.substring(1); j = 0;
				} else {
					row.id = jsonArray[i][0]; // jsonArray[i]=["1","name","code",...];
				}
				row.data = [];
				var userdata = {};
				var jsonlen = jsonArray[i].length;
				var len = jsonlen;
				// 1. grid data
				for (j = 1; j < len; j++) {
					row.data[j-1] = null2empty(jsonArray[i][j]);
				}
				// 2. grid userdata
				if (userdataIdxs) {
					for (j = 0; j < userdataIdxs.length; j++) {
						var prop = "userdata_" + j;
						userdata[prop] = null2empty(jsonArray[i][userdataIdxs[j]]);
					}
					row.userdata = userdata; j = 0;
				}
				datas.rows[i] = row;
			}
		}
		//*/
	}
	return datas;
}
/**
 * 把null置为空
 * @param obj
 * @returns
 */
function null2empty(obj) {
	return (undefined == obj || null == obj) ? "" : obj;
}
/**
 * 创建一个空的DIV
 * @param divId
 */
function createEmptyDiv(divId, topPos) {
	var obj = document.getElementById(divId);
	
	if (obj == null) {
		if (null == topPos) topPos = 40;
		obj = document.createElement("DIV");
		obj.setAttribute("id", divId);
		obj.setAttribute("style", "position:relative;top:" + topPos + "%;display: none;");
		document.body.appendChild(obj);
	} else {
		obj.innerHTML = "";
	}
}
/**
 * 在指定列表中检索指定列
 * @param targetGrid
 *        列表
 * @param value
 *        检索匹配值
 * @param index
 *        指定列索引
 */
function searchInGrid(targetGrid, value, index) {
	value = value.toLowerCase();
	targetGrid.forEachRow(function(rowId) {
		var columnData = targetGrid.cells(rowId,index).getValue();
		columnData = columnData.toLowerCase();
		var pinyin = PinYin4Js.toPinyin(columnData);
		var firstC = PinYin4Js.getFirstChar(columnData);
		var hidden = ((null == value || "" == value) ? false : (columnData.indexOf(value) < 0 && 
				pinyin.indexOf(value) < 0 && firstC.indexOf(value) < 0));
		//dhtmlx.alert("hidden = " + hidden);
		targetGrid.setRowHidden(rowId, hidden);
	});
};
/**
 * 
 * @param config
 *        配置信息，格式：{id:"", parentId:"", title:"", width:"", height:""}
 */
function createDhxWindow(config) {
	var subDhxWins = new dhtmlXWindows();
	if (config.parentId) {
		subDhxWins.enableAutoViewport(false);
		subDhxWins.attachViewportTo(config.parentId);
	}
	subDhxWins.setImagePath(IMAGE_PATH);
	var w = config.width ? config.width : 300;
	var h = config.height ? config.height : 400;
	var win = subDhxWins.createWindow(config.id, 0, 0, w, h);
	win.setModal(true);
	//win.denyResize();
	//win.denyPark();
	win.button("park").hide();
	win.button("minmax1").hide();
	win.button("minmax2").hide();
	//win.button("close").hide();
	win.setText(config.title);
	win.center();
	return win;
}
/**
 * 
 * @returns {Array}
 */
function getMoveButton(cfg) {
	var formJson = [{
		type:"block",
		offsetLeft: 20,
		list: []
	}];
	if (null == cfg || false != cfg.allToRight) {
		formJson[0].list.push({
			type: "button",
			name: "allToRight",
			value: "&gt;&gt;",
			width: 60
		});
	}
	formJson[0].list.push({
		type: "button",
		name: "toRight",
		value: "&gt;",
		width: 60
	});
	formJson[0].list.push({
		type: "button",
		name: "toLeft",
		value: "&lt;",
		width: 60
	});
	if (null == cfg || false != cfg.allToLeft) {
		formJson[0].list.push({
			type: "button",
			name: "allToLeft",
			value: "&lt;&lt;",
			width: 60
		});
	}
	if (cfg && true == cfg.saveButton) {
		formJson[0].list.push({
			type: "button",
			name: "save",
			value: "保存",
			width: 60
		});
	}
	if (cfg && true == cfg.clearButton) {
		formJson[0].list.push({
			type: "button",
			name: "clear",
			value: "删除配置",
			width: 60
		});
	}
	return formJson;
}