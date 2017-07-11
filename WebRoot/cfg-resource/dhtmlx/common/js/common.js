dhtmlx.skin = Skin;
// 设置全局的图片路径
dhx_globalImgPath = DHX_PATH + SKIN_TYPE + "/imgs/";
ZeroClipboard.setMoviePath(DHX_RES_PATH + "/common/js/ZeroClipboard.swf");
// 初始化布局基本设置
function initLayout(layout, isMainLayout) {
    if (true === isMainLayout) {
        dhxLayout = layout;
    }
    if (!layout) {
        return;
    }
    layout.cont.obj._offsetTop = 1;
    layout.cont.obj._offsetLeft = 1;
    layout.cont.obj._offsetHeight = -2;
    layout.cont.obj._offsetWidth = -2;
    layout.setSizes();
    return layout;
}
// 页面改变大小时，dhxLayout自适应
function layoutResize() {
    if (dhxLayout) {
        dhxLayout.setSizes();
    }
}
// 操作之后的提示信息
var commonMessage = loadJson(contextPath + "/parameter/system-parameter!getAllMessages.json");
/**
 * 获取提示信息
 * @param {Object} key 提示信息对应的系统参数名称
 * @param {Object} params 提示信息中对应参数,例如{1}
 */
function getMessage(key, params) {
    var value = commonMessage[key];
    if (value == undefined) {
        value = "";
    } else {
        if (params instanceof Array) {
            for (var i in params) {
                value = value.replace("{" + (parseInt(i) + 1) + "}", params[i]);
            }
        } else if (typeof params == "string" || typeof params == "number" || typeof params == "boolean") {
            value = value.replace("{1}", params);
        }
    }
    return value;
}
// 初始化日期格式
dhtmlXCalendarObject.prototype.langData["en"] = {
    dateformat : '%Y-%m-%d',
    monthesFNames : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    monthesSNames : ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
    daysFNames : ["日", "一", "二", "三", "四", "五", "六"],
    daysSNames : ["日", "一", "二", "三", "四", "五", "六"],
    weekstart : 1
};
// 修改界面风格
function changeSkin(skin) {
    addCookie("skin", skin, 365 * 24);
    window.location.reload();
}
// 定义变量
var PAGE_SIZE = 15;
var WIN_ID = "win_id";
var SHOW_CRUD = true;
var dhxLayout, tree, searchForm, toolBar, dataGrid, statusBar, detailForm, dhxWins;
var gridData, detailFormData, currentTreeNodeId, contextMenuNodeId, queryParams, page, pageable = true;
var SAVE_URL, GET_BY_ID_URL, QUERY_URL, DELETE_URL, TREE_URL, MODEL_URL;
var GWIN_WIDTH = 500;
var GWIN_HEIGHT = 300;

// 控件所需图片的地址，ContextPath加上图片所在路径
var IMAGE_PATH = DHX_PATH + SKIN_TYPE + "/imgs/";
// 工具条按钮图标所在路径
var TOOLBAR_IMAGE_PATH = DHX_RES_PATH + "/common/images/icon/";
// 详细form的label和input的宽度
var DETAIL_LABEL_WIDTH = 80, DETAIL_INPUT_WIDTH = 120;
// 获取当期时间
var _date = new Date();
var currentDate = _date.getFullYear() + "-" + (_date.getMonth() + 1) + "-" + _date.getDate();
// 表单格式
var formFormat = [{
            type : "settings",
            position : "label-left",
            labelWidth : DETAIL_LABEL_WIDTH,
            inputWidth : DETAIL_INPUT_WIDTH,
            labelAlign : "right",
            offsetTop : "5"
        }, {
            type : "block",
            list : []
        }, {
            type : "block",
            inputWidth : 240,
            className : 'crlBtn',
            list : []
        }];
// 列表表单格式
var detailFormFormat = [{
            type : "settings",
            position : "label-left",
            labelWidth : DETAIL_LABEL_WIDTH,
            inputWidth : DETAIL_INPUT_WIDTH,
            labelAlign : "right"
        }, {
            type : "block",
            list : []
        }, {
            type : "block",
            inputWidth : 240,
            className : 'crlBtn',
            list : []
        }];
/**
 * 将c的属性复制给o；如果default存在，将那些defaults有而c没有的属性复制给o。
 * @param {Object} o 属性接受方对象
 * @param {Object} c 属性源对象
 * @param {Object} defaults 默认对象，如果该参数存在，o将会得到那些defaults有而c没有的属性
 * @return {Object} o
 */
function apply(o, c, defaults) {
    if (defaults) {
        apply(o, defaults);
    }
    if (o && c && typeof c == 'object') {
        for (var p in c) {
            o[p] = c[p];
        }
    }
    return o;
}
/**
 * 复制所有c的属性至o，如果o已有该属性，则不复制。
 * @param {Object} o 属性接受方对象
 * @param {Object} c 属性源对象
 * @return {Object} o
 */
function applyIf(o, c) {
    if (o) {
        for (var p in c) {
            if (typeof o[p] === 'undefined') {
                o[p] = c[p];
            }
        }
    }
    return o;
}
/**
 * 初始化grid
 * @param {dhtmlXGridObject} grid grid对象
 * @param {object} gridFormat grid初始化格式
 * @param {object} pageBar 分页条位置
 */
function initGrid(grid, gridFormat, pageBar) {
    if (!grid) {
        grid = dataGrid;
    }
    if (!gridFormat) {
        gridFormat = gridData;
    }
    if (!pageBar) {
        pageBar = statusBar;
    }
    grid.setImagePath(IMAGE_PATH);
    grid.setHeader(gridFormat.format.headers.toString());
    grid.setInitWidths(gridFormat.format.colWidths.toString());
    grid.setColTypes(gridFormat.format.colTypes.toString());
    grid.setColAlign(gridFormat.format.colAligns.toString());
    if (gridFormat.format.colTooltips) {
        grid.enableTooltips(gridFormat.format.colTooltips.toString());
    }
    grid.setSkin(Skin);
    grid.init();
    grid.enableMultiselect(true);
    grid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    grid.attachEvent("onRowDblClicked", function(rId, cInd) {
    });
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        grid.enablePaging(true, pagesize, 1, pageBar);
        grid.setPagingSkin('toolbar', Skin);
    }
}
/**
 * 初始化表单格式
 * @param {json} formData 表单结构
 * @return {jsonArray}
 */
function initFormFormat(formData) {
    var format = apply(new Array(), formFormat);
    format[1].list = formData.format;
    for (var key in formData.settings) {
        format[0][key] = formData.settings[key];
    }
    return format;
}
/**
 * 初始化detailFormFormat
 * @param {json} formData 表单结构
 * @return {jsonArray}
 */
function initDetailFormFormat(formData) {
    var format = apply(new Array(), detailFormFormat);
    format[1].list = formData.format;
    for (var key in formData.settings) {
        format[0][key] = formData.settings[key];
    }
    return format;
}
/**
 * 初始化表单，只是简单的对输入框等控件进行清空
 * @param {dhtmlxForm} form 表单
 * @param {boolean} clearId 是否清空ID
 */
function initFormData(form, clearId, clearHidden) {
    if (form == undefined)
        return;
    var type = "";
    form.forEachItem(function(name) {
        type = form.getItemType(name);
        if (name == "updateDate") {
            // do nothing
        } else if ((type == "input") || (type == "combo") || (type == "calendar") || (type == "password")) {
            form.setItemValue(name, "");
        } else if (clearHidden && type == "hidden") {
            form.setItemValue(name, "");
        }
        // 是否清空ID
        if (clearId) {
            if (name.substr(name.lastIndexOf(".") + 1) == "id") {
                form.setItemValue(name, "");
            }
        }
    });
}
/**
 * 通用返回json对象的方法
 * @param {string} url
 * @return {object}
 */
function loadJson(url) {
    if ((url == undefined) || (url == ""))
        return null;
    if (url.indexOf("\&P_timestamp=") < 0 || url.indexOf("\?P_timestamp=") < 0) {
        url = addTimestamp(url);
    }
    var jsonObj = null;

    var loader = dhtmlxAjax.getSync(url);
    if (loader.xmlDoc.responseText == "")
        return null;
    var content = loader.xmlDoc.responseText.replace(/:null/g, ":''");
    jsonObj = eval("(" + content + ")");

    return jsonObj;
}
/**
 * 加载表单的值
 * @param {dhtmlxForm} form 表单
 * @param {string} url
 */
function loadForm(form, url) {
    var formData = loadJson(url);
    // setFormData方法不能设置表单中name为a.b的值
    // form.setFormData(formData);
    // loadFormData方法不能设置表单中类型为radio的值，所有这两个方法同时使用
    loadFormData(form, formData);
}
/**
 * 加载表单的值
 * @param {dhtmlxForm} form 表单
 * @param {obj} formData
 */
function loadFormData(form, formData) {
    /*form.forEachItem(function(name) {
        var type = form.getItemType(name);
        var value = "";
        if ((type == "input") || (type == "combo") || (type == "hidden") || (type == "calendar")
                || (type == "checkbox") || (type == "password")) {
            value = obtainNonNullValue(name, formData);
            form.setItemValue(name, value);
            if (type == "checkbox" && value == "1") {
                form.checkItem(name);
            }
        }
    });*/
	var fData = form.getFormData();
	for (var name in fData) {
		var type = form.getItemType(name);
        var value = "";
        if ((type == "input") || (type == "combo") || (type == "hidden") || (type == "calendar")
                || (type == "checkbox") || (type == "password")) {
            value = obtainNonNullValue(name, formData);
            form.setItemValue(name, value);
            if (type == "checkbox" && value == "1") {
                form.checkItem(name);
            }
        } else {
        	value = obtainNonNullValue(name, formData);
        	type = form.getItemType(name, value);
        	if (type == "radio") {
        	    form.checkItem(name, value);
        	}
        }
	}
}
/**
 * 列表加载数据的方法
 * @param {dhtmlXGridObject} grid grid对象
 * @param {object} gridFormat grid初始化格式
 * @param {string} url 访问地址
 * @param {number} pageNum 页码
 * @param {number} pageSize 每页条数
 */
function search(grid, gridFormat, url, pageNum, pageSize) {
    if (!grid) {
        grid = dataGrid;
    }
    if (!gridFormat) {
        gridFormat = grid.gridFormat || gridData;
    }
    if (!url) {
        url = QUERY_URL;
    }
    try {
    	if (!pageNum) {
    	    pageNum = grid.currentPage;
    	}
        grid.clearAll();
    } catch (e) {
    }
    if (!url) {
        return;
    }
    grid.gridFormat = gridFormat;
    grid.url = url;
    if (pageable) {
        pageSize = pageSize || grid.pageSize || getCookie("pagesize") || PAGE_SIZE;
        pageNum = pageNum || grid.currentPage || 1;
        grid.currentPage = pageNum;
        if (url.indexOf("?") != -1) {
            queryParams = "&E_model_name=datagrid&P_pagesize=" + pageSize + "&P_pageNumber=" + pageNum;
        } else {
            queryParams = "?E_model_name=datagrid&P_pagesize=" + pageSize + "&P_pageNumber=" + pageNum;
        }
    } else {
        if (url.indexOf("?") != -1) {
            queryParams = "&E_model_name=datagrid";
        } else {
            queryParams = "?E_model_name=datagrid";
        }
    }
    if (gridFormat && gridFormat.format && gridFormat.format.cols) {
        queryParams += "&F_in=" + gridFormat.format.cols.toString();
    }
    if (gridFormat && gridFormat.format && gridFormat.format.userDatas) {
        queryParams += "&P_UD=" + gridFormat.format.userDatas.toString();
    }
    if (searchForm) {
        searchForm.send(addTimestamp(url + queryParams), "post", function(loader, response) {
            // grid.clearAll();
            if ((response == undefined) || (response == ""))
                return;
            var rows = eval('(' + response + ')');
            // 如果查询结果为空
            if (rows.rows == undefined) {
                // dhtmlx.message("结果集为空！");
            	if (rows.totalPages != 0 && rows.totalPages < rows.pageNumber) {
                    search(grid, gridFormat, url, rows.totalPages, rows.pageSize);
                }
                return;
            }

            grid.parse(rows, "json");
                // 这里应该有网络连接失败的提醒
            }
        );
    } else {
        var loader = dhtmlxAjax.getSync(addTimestamp(url + queryParams));
        if (loader.xmlDoc.responseText == "")
            return;
        // 替换列表显示为“null” 的列 --wm
        var reg = new RegExp("\"null\"", "g");
        var loaderDoc = loader.xmlDoc.responseText;
        if (loaderDoc.indexOf("null") != -1) {
            var dataJson = loaderDoc.replace(reg, "\"\"");
        }
        var rows = eval('(' + dataJson + ')');
        // 如果查询结果为空
        if (rows.rows == undefined) {
            // dhtmlx.message("结果集为空！");
            if (rows.totalPages != 0 && rows.totalPages < rows.pageNumber) {
                search(grid, gridFormat, url, rows.totalPages, rows.pageSize);
            }
            return;
        }
        grid.parse(rows, "json");
        // 这里应该有网络连接失败的提醒
    }
}
/**
 * 列表加载数据的方法(不分页)
 * @param {dhtmlXGridObject} grid grid对象
 * @param {object} gridFormat grid初始化格式
 * @param {string} url 访问地址
 */
function searchNoPage(grid, gridFormat, url) {
    if (!grid) {
        grid = dataGrid;
    }
    if (!gridFormat) {
        gridFormat = grid.gridFormat || gridData;
    }
    if (!url) {
        url = QUERY_URL;
    }
    try {
        grid.clearAll();
    } catch (e) {
    }
    if (!url) {
        return;
    }
    grid.gridFormat = gridFormat;
    grid.url = url;
    if (url.indexOf("?") != -1) {
        queryParams = "&E_model_name=datagrid";
    } else {
        queryParams = "?E_model_name=datagrid";
    }
    if (gridFormat && gridFormat.format && gridFormat.format.cols) {
        queryParams += "&F_in=" + gridFormat.format.cols.toString();
    }
    if (gridFormat && gridFormat.format && gridFormat.format.userDatas) {
        queryParams += "&P_UD=" + gridFormat.format.userDatas.toString();
    }
    if (searchForm) {
        searchForm.send(url + queryParams, "post", function(loader, response) {
            // grid.clearAll();
            if ((response == undefined) || (response == ""))
                return;
            var rows = eval('(' + response + ')');
            // 如果查询结果为空
            if (rows.rows == undefined) {
                // dhtmlx.message("结果集为空！");
                return;
            }
            grid.parse(rows, "json");
                // 这里应该有网络连接失败的提醒
            }
        );
    } else {
        var loader = dhtmlxAjax.getSync(url + queryParams);
        if (loader.xmlDoc.responseText == "")
            return;
        // 替换列表显示为“null” 的列 --wm
        var reg = new RegExp("\"null\"", "g");
        var loaderDoc = loader.xmlDoc.responseText;
        if (loaderDoc.indexOf("null") != -1) {
            var dataJson = loaderDoc.replace(reg, "\"\"");
        }
        var rows = eval('(' + dataJson + ')');
        // 如果查询结果为空
        if (rows.rows == undefined) {
            // dhtmlx.message("结果集为空！");
            return;
        }
        grid.parse(rows, "json");
        // 这里应该有网络连接失败的提醒
    }
}
/**
 * 加载列表数据（后台传回的格式为List<Object[]>）
 * @param {dhtmlxGrid} grid
 * @param {string} url
 */
function loadGridDataArray(grid, url) {
    grid.clearAll();
    var dataJson = loadJson(url);
    var jsonArray = dataJson.data ? dataJson.data : dataJson;
    var datas = {};
    datas.rows = [];
    for (var i = 0; i < jsonArray.length; i++) {
        var row = {};
        row.id = jsonArray[i][0];
        row.data = [];
        for (var j = 1; j < jsonArray[i].length; j++) {
            row.data[j - 1] = jsonArray[i][j];
        }
        datas.rows[i] = row;
    }
    grid.parse(datas, "json");
}
/**
 * 选择行时触发的方法
 * @param {string} rowId
 * @param {string} celInd
 */
function selectRow(rowId, celInd) {
    // 传值到form里
    initFormData(detailForm, true);
    var selectId = dataGrid.getSelectedRowId();
    if (selectId.indexOf(",") != -1) {
        return;
    }
    GET_BY_ID_URL = MODEL_URL + "/" + selectId + ".json?_method=get";
    loadForm(detailForm, GET_BY_ID_URL);
}
/**
 * 根据ID删除数据
 * @param {string} id
 * @param {function} callback 删除后的回调函数
 */
function deleteById(id, callback) {
    DELETE_URL = MODEL_URL + "/" + id + ".json?_method=delete";
    if (callback && typeof callback == "function") {
        dhtmlxAjax.get(DELETE_URL, callback);
    } else {
        dhtmlxAjax.get(DELETE_URL, function(loader) {
            dhtmlx.message(getMessage("delete_success"));
            dataGrid.clearAll();
            pageable = false;
            search();
        });
    }
}
/**
 * 初始化toolBar
 */
function initToolBar() {
    toolBar.setIconsPath(IMAGE_PATH);
    toolBar.addButton("add", 0, "添加", "new.gif");
    toolBar.addButton("modify", 1, "修改", "update.gif");
    toolBar.addButton("delete", 2, "删除", "delete.gif");
    toolBar.addButton("refresh", 3, "刷新", "true.gif");
    toolBar.attachEvent('onClick', function(id) {
        if (id == "add") {
            initFormData(detailForm, true);
        } else if (id == "modify") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
            GET_BY_ID_URL = MODEL_URL + "/" + selectId + ".json?_method=get";
            loadForm(detailForm, GET_BY_ID_URL);
        } else if (id == "delete") {
            var selectId = dataGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (confirm(getMessage("delete_warning"))) {
                deleteById(selectId);
                detailForm.lock();
            }
        } else if (id == "refresh") {
            search();
        }
    });
}
// 初始化日期
function initCalendar(formObj, formFormat) {
    if ((formObj == undefined) || (formFormat == undefined))
        return;
    var cldList = getCalendar(formFormat);
    var calendar = null;
    for (var i = 0; i < cldList.length; i++) {
        calendar = formObj.getCalendar(cldList[i].name);
        calendar.hideTime();// 隐藏时间
        if (cldList[i].range == "begin") {
            calendar._bind = cldList[i].name.replace("Q_GTE", "Q_LTE");
            calendar._formObj = formObj;
            // calendar.setSensitiveRange(null, currentDate);
            calendar.attachEvent("onClick", function() {
                var _calendar = this._formObj.getCalendar(this._bind);
                _calendar.setSensitiveRange(this.getDate(), null);
            })
        } else if (cldList[i].range == "end") {
            calendar._bind = cldList[i].name.replace("Q_LTE_", "Q_GTE_");
            calendar._formObj = formObj;
            // calendar.setSensitiveRange(currentDate, null);
            calendar.attachEvent("onClick", function() {
                var _calendar = this._formObj.getCalendar(this._bind);
                _calendar.setSensitiveRange(null, this.getDate());
            })
        }
    }
}
// 获取日期
function getCalendar(formFormat, cldList) {
    if (cldList == undefined)
        cldList = [];
    if (formFormat == undefined)
        return cldList;
    for (var i = 0; i < formFormat.length; i++) {
        var type = formFormat[i].type;
        if (type == "calendar") {
            if (formFormat[i].range == undefined)
                continue;
            cldList[cldList.length] = {
                name : formFormat[i].name,
                range : formFormat[i].range
            };
        }
        if (type == "settings") {
            continue;
        } else if (formFormat[i].list != undefined) {
            getCalendar(formFormat[i].list, cldList);
        }
    }
    return cldList;
}
// 初始化下拉框
function initCombo(formObj, formFormat, search) {
    if ((formObj == undefined) || (formFormat == undefined))
        return;
    var comboList = getCombo(formFormat);
    for (var i = 0; i < comboList.length; i++) {
        var _text = getKey(comboList[i]._text_attrName), _value = getKey(comboList[i]._value_attrName);
        var dhxCombo = formObj.getCombo(comboList[i].name);
        dhxCombo._q_like_field = _text;
        dhxCombo._text_attrName = _text;
        dhxCombo._value_attrName = _value;
        dhxCombo._bind = getKey(comboList[i]._bind);
        if (comboList[i].showAll) {
            var jsonObj = loadJson(comboList[i].url);
            if (!jsonObj || !jsonObj.data)
                continue;
            var opt_data = [];
            if (search) {
                opt_data[0] = {
                    text : "全部",
                    value : ""
                };
                for (var m = 0; m < jsonObj.data.length; m++) {
                    opt_data[m + 1] = {
                        text : jsonObj.data[m][_text],
                        value : jsonObj.data[m][_value]
                    };
                }
            } else {
                for (var m = 0; m < jsonObj.data.length; m++) {
                    opt_data[m] = {
                        text : jsonObj.data[m][_text],
                        value : jsonObj.data[m][_value]
                    };
                }
            }
            dhxCombo.addOption(opt_data);
            dhxCombo.enableFilteringMode(false);
        } else {
            dhxCombo.enableFilteringMode(true, comboList[i].url, true, true);
        }
        //当该combo值改变的时候，将值绑定到_bind控件里面
        dhxCombo.attachEvent("onChange", function(param) {
            if (this._bind == undefined)
                return;

            var text_input = getFormItemName(this.name);
            var text = this.getSelectedText();
            var value = this.getSelectedValue();
            //var comboText = this.getComboText();

            //用户必须选择一个，否则为空
            formObj.setItemValue(text_input, text);
            formObj.setItemValue(this._bind, value);
        });
    }
}
function getCombo(formFormat, comboList) {
    if (comboList == undefined)
        comboList = [];
    if (formFormat == undefined)
        return comboList;
    var param1 = "?P_pagesize=30&E_model_name=combo";
    var param2 = "&P_pagesize=30&E_model_name=combo";
    for (var i = 0; i < formFormat.length; i++) {
        var type = formFormat[i].type;
        if (type == "combo") {
            if (formFormat[i].url == undefined)
                continue;
            if (formFormat[i].url == "")
                formFormat[i].url = MODEL_URL + "!search.json";
            formFormat[i].url += (formFormat[i].url.indexOf("?") > 0 ? param2 : param1);

            comboList[comboList.length] = {
                name : formFormat[i].name,
                url : formFormat[i].url,
                showAll : formFormat[i].showAll,
                _bind : formFormat[i]._bind,
                _text_attrName : formFormat[i]._text_attrName,
                _value_attrName : formFormat[i]._value_attrName
            };
        } else if (type == "settings") {
            continue;
        } else if (formFormat[i].list != undefined) {
            getCombo(formFormat[i].list, comboList);
        }
    }
    return comboList;
}
function getKey(key) {
    if ((key == undefined) || (key == ""))
        return null;
    var arr = key.split("_");
    if (arr.length >= 3) {
        return arr[2];
    }
    return key;
}
// 获取与keyArr相对应的值，返回的格式与dhtmlxgrid要求的格式一样的字符串
function obtainColumn(keyArr, jsonArr) {
    if (keyArr == null)
        return null;
    if (jsonArr == null)
        return null;
    var rows = [];
    jsonArr = jsonArr.rows;
    for (var j = 0; j < jsonArr.length; j++) {
        var data = [];
        for (var i = 0; i < keyArr.length; i++) {
            data[i] = obtainNonNullValue(keyArr[i], jsonArr[j]);
        }
        rows[j] = {
            'id' : obtainNonNullValue('id', jsonArr[j]),
            'data' : data
        };
    }
    return {
        'rows' : rows
    };
}
// 获取对象中的特定属性值，可以级联取值（obj.obj.obj.key），返回对象obj中与key相对应的值
function obtainValue(key, obj) {
    if (obj == null)
        return null;
    if (obj instanceof Array)
        return null;
    // 对于对象内部嵌套对象的情形，通过obj.obj.obj.key的形式获取key值
    var keys = key.split(".");
    try {
        for (var i = 0; i < keys.length; i++) {
            key = keys[i];
            obj = obj[key];
        }
        if (obj == undefined) {
            return null;
        }
        return obj;
    } catch (err) {
        return null;
    }
    return null;
}
// 封装obtainValue方法，使得返回值不为null
function obtainNonNullValue(key, obj) {
    var value = obtainValue(key, obj);
    return value == null ? "" : value;
}
/**
 * 创建弹出框并加载新增from元素
 * @param {json} formData 表单结构
 * @param {string} title 标题
 * @param {function} initToolbar 初始化弹出框工具条的方法
 * @param {string} width 宽度
 * @param {string} height 高度
 * @return {dhtmlxForm}
 */
function openWindow(formData, title, initToolbar, width, height) {
    if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var winWidth = width || GWIN_WIDTH;
    var winHeight = height || GWIN_HEIGHT;
    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, winWidth, winHeight);
    dataWin.setModal(true);
    dataWin.setText(title);
    dataWin.center();
    var formFormat = initFormFormat(formData);
    var form = dataWin.attachForm(formFormat);
    // initCombo(form, formFormat);
    if (initToolbar && typeof initToolbar == "function") {
        var statusBar = dataWin.attachStatusBar();
        var toolBar = new dhtmlXToolbarObject(statusBar);
        initToolbar.call(this, toolBar);
    }
    return form;
}
/**
 * 打开新增窗口
 * @param {json} formData 表单结构
 * @param {function} initToolbar
 * @param {string} width 宽度
 * @param {string} height 高度
 * @return {dhtmlxForm}
 */
function openNewWindow(formData, initToolbar, width, height) {
    return openWindow(formData, "新增", initToolbar, width, height);
}
/**
 * 打开查看窗口
 * @param {json} formData 表单结构
 * @param {string} id
 * @param {string} width 宽度
 * @param {string} height 高度
 * @return {dhtmlxForm}
 */
function openDetailWindow(formData, id, width, height, moduleUrl) {
	var loadUrl = MODEL_URL + "/" + id + ".json";
	if (isNotEmpty(moduleUrl)) {
		loadUrl = moduleUrl + "/" + id + ".json";
	}
    var form = openWindow(formData, "查看", width, height);
    form.lock();
    loadForm(form, loadUrl);
    return form;
}
/**
 * 打开修改窗口
 * @param {json} formData 表单结构
 * @param {string} id
 * @param {function} initToolbar
 * @param {string} width 宽度
 * @param {string} height 高度
 * @return {dhtmlxForm}
 */
function openEditWindow(formData, id, initToolbar, width, height, moduleUrl) {
	var loadUrl = MODEL_URL + "/" + id + ".json";
	if (isNotEmpty(moduleUrl)) {
		loadUrl = moduleUrl + "/" + id + ".json";
	}
    var form = openWindow(formData, "修改", initToolbar, width, height);
    loadForm(form, loadUrl);
    return form;
}
/**
 * 继承：可以传入多个父类
 * @author qiucs
 * @returns
 */
function inherit() {
    if (arguments.length == 0)
        return null;
    var o = {};
    var protoprops = ["toString", "valueOf", "constructor", "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable",
            "toLocaleString"];
    for (var i = 0; i < arguments.length; i++) {
        var source = arguments[i];
        for (var prop in source) {
            o[prop] = source[prop];
        }
        if (protoprops[0] in o)
            continue;
        for (var j = 0; j < protoprops.length; j++) {
            prop = protoprops[j];
            if (source.hasOwnProperty(prop))
                o[prop] = source[prop];
        }
    }
    return o;
}
String.prototype.startWith=function(str) {     
    var reg=new RegExp("^"+str);     
    return reg.test(this);        
}  
String.prototype.endWith=function(str) {     
    var reg=new RegExp(str+"$");     
    return reg.test(this);        
}
