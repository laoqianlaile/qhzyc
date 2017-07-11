/**************************************************************以下为常量区***********************************************************************/
/* 字段与值的分隔符.*/
var CFG_cv_split = "≡";
/* 操作符关键字与字段的分隔符.*/
var CFG_oc_split = "_C_";
var CFG_app_uri = contextPath + "/appmanage";
/* 公用类(常量).*/
var CFG_common = {
    /* 布局内容.*/
    L_FORM : 0,
    L_GRID : 1,
    /* 按钮类型.*/
    B_BUTTON : "b",
    B_SEPARATOR : "s",
    /* 预置按钮.*/
    P_CREATE : "create", // 新增
    P_SAVE : "save", // 保存
    P_UPDATE : "update", // 修改
    P_BATCH_UPDATE : "batchUpdate", // 批量修改
    P_DELETE : "delete", // 删除
    P_SEARCH : "search", // 检索
    P_REPORT : "report", // 打印
    P_UPLOAD : "upload", // 上传电子全文
    P_SAVE_AND_CREATE : "saveAndCreate", // 保存并新增
    P_SAVE_AND_CLOSE : "saveAndClose", // 保存并关闭
    P_RESET : "reset", // 重置
    P_REPORT_PRE : "_subreport_", // 报表前缀字符
    P_CUSTOM_SEARCH : "customSearch", // 检索自定义
    P_CUSTOM_COLUMN : "customColumn", // 列表自定义
    P_CUSTOM_SORT : "customSort", // 排序自定义
    P_FIRST_RECORD : "firstRecord", // 首条
    P_PREVIOUS_RECORD : "previousRecord", // 上一条
    P_NEXT_RECORD : "nextRecord", // 下一条
    P_LAST_RECORD : "lastRecord" // 末条
};
/**************************************************************以下为自定义列表***********************************************************************/
/** 
 * 获取自定义列表配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @returns {object}
 */
function CFG_gcfg(tableId, moduleId) {
    var url = contextPath + "/appmanage/app-column!dhtmlxGrid.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId;
    return loadJson(url);
}
;
/**
 * 获取自定义列表查询基础URL
 * @param gcfg 列表的配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @param componentVersionId 构件版本ID
 * @returns {String}
 */
function CFG_gbaseUrl(url, gcfg, tableId, moduleId, componentVersionId) {
    return url + (url.indexOf("?") > 0 ? "&" : "?") + "E_frame_name=dhtmlx&E_model_name=datagrid" +
    		"&P_tableId=" + tableId + "&P_moduleId=" + moduleId
            + "&P_componentVersionId=" + componentVersionId + "&P_columns=" + gcfg.columns + "&P_datatypes="
            + gcfg.datatypes + "&P_codetypes=" + gcfg.codetypes + "&P_orders=" + gcfg.orders + "&P_types=" + gcfg.types
            + "&P_urls=" + gcfg.urls;
}
/**
 * 初始化自定义列表
 * @param gcfg 列表的配置信息
 * @param pagebar 分页条位置
 * @param pagebarEvent 分页条上的事件
 */
function CFG_ginit(grid, gcfg, pagebar, pagebarEvent) {
    // 1. init grid
    grid.setImagePath(IMAGE_PATH);
    grid.setHeader(gcfg.headers.toString());
    grid.setInitWidths(gcfg.widths.toString());
    grid.setColTypes(gcfg.types.toString());
    grid.setColAlign(gcfg.aligns.toString());
    grid.setSkin(Skin);
    grid.enableColumnMove(true);
    grid.enableMultiselect(true);
    grid.enableHeaderMenu();
    grid.setStyle("font-weight:bold;", "", "", "");
    grid.init();
    // 2. 分页条
    if (!pagebar) {
        return;
    }
    if (pagebarEvent) {
        grid.setPagebarEvent(pagebarEvent);
    }
    grid.enablePaging(true, PAGE_SIZE, 1, pagebar);
    grid.setPagingSkin('toolbar', Skin);
}
/**
 * 初始化列表配置
 * @param gLayout
 * @param tableId
 * @param moduleId
 * @returns {___anonymous5640_5899}
 */
function CFG_grid(gLayout, tableId, moduleId) {
    var _this = this;
    // URL中的参数
    _this.parameter = {};
    // 查询过滤条件
    _this.filter = {};
    // 列表配置信息
    var gcfg = CFG_gcfg(tableId, moduleId);
    var componentVersionId = loadJson(contextPath + "/appmanage/module!getComponentVersionId.json?id=" + moduleId);
    // 状态栏
    var gsbar = gLayout.attachStatusBar();
    var grid = gLayout.attachGrid();
    var gurl = CFG_gbaseUrl(gcfg, tableId, moduleId, componentVersionId);
    CFG_ginit(grid, gcfg, gsbar, CFG_gload);
    // CFG_gload();

    /** 加载列表数据*/
    function CFG_gload(pageNumber, pageSize) {
        if (undefined == grid || null == grid || undefined == gurl || null == gurl || "" == gurl) {
            return;
        }
        var url = CFG_gurl(pageNumber, pageSize);
        var loader = dhtmlxAjax.getSync(encodeURI(url));
        var loaderDoc = loader.xmlDoc.responseText;
        grid.clearAll();
        if ("" == loaderDoc)
            return;
        var data = eval('(' + loaderDoc + ')');
        // 如果查询结果为空
        if (data.rows == undefined) {
            // dhtmlx.message("结果集为空！");
            return;
        }
        grid.parse(data, "json");// */
    }
    // 获取查询过滤条件
    function CFG_filter() {
        var filter = "";
        for (var p in _this.filter) {
            var v = _this.filter[p];
            if (isEmpty(v))
                continue;
            filter += ("," + p + CFG_cv_split + v);
        }
        return (filter.length > 1 ? filter.substring(1) : "");
    }
    // 获取传递参数
    function CFG_parameter() {
        var parameter = "";
        for (var p in _this.parameter) {
            var v = _this.parameter[p];
            if (isEmpty(v))
                continue;
            parameter += ("&" + p + "=" + v);
        }
        return parameter;
    }
    // 获取URL
    function CFG_gurl(pageNumber, pageSize) {
        var url = gurl;
        // 加上分页过滤条件
        if (!grid.pageSize) {
            grid.pageSize = PAGE_SIZE;
        }
        pageNumber = pageNumber ? pageNumber : grid.currentPage;
        pageSize = pageSize ? pageSize : grid.pageSize;
        addParameter("P_pageNumber", pageNumber);
        addParameter("P_pagesize", pageSize);
        // 加上检索过滤条件
        addParameter("P_filter", CFG_filter());
        // 拼接上外部传递参数
        url += CFG_parameter();
        return url;
    }
    /******************************* 对外接口 *********************************/
    /** 为查询添加过滤条件*/
    function addFilter(name, value) {
        _this.filter[name] = value;
    }
    /** 删除指定过滤条件*/
    function removeFilter(name) {
        if (name in _this.filter) {
            delete _this.filter[name];
        }
    }
    /** 清空所有过滤条件*/
    function clearFilter() {
        _this.filter = {};
    }
    /** 添加参数传递*/
    function addParameter(name, value) {
        _this.parameter[name] = value;
    }
    /** 删除指定参数*/
    function removeParameter(name) {
        if (name in _this.parameter) {
            delete _this.parameter[name];
        }
    }
    /** 清空所有参数*/
    function clearParameter() {
        _this.parameter = {};
    }
    /** */
    return {
        "grid" : grid,
        "search" : CFG_gload,
        "addFilter" : addFilter,
        "addParameter" : addParameter,
        "removeFilter" : removeFilter,
        "removeParameter" : removeParameter,
        "clearFilter" : clearFilter,
        "clearParameter" : clearParameter
    };
}
/*********************************************************************以下为自定义界面****************************************************************/
/**
 * 获取自定义界面配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @returns {object}
 */
function CFG_fcfg(tableId, moduleId) {
    var url = contextPath + "/appmanage/app-form!dhtmlxForm.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId;
    var jsonObj = loadJson(url);
    if (null == jsonObj || "" == jsonObj) {
        return null;
    }
    var cfg = {};
    cfg.formJson = eval("(" + jsonObj.formJson + ")");
    cfg.keptColumns = jsonObj.keptColumns;
    cfg.increaseColumns = jsonObj.increaseColumns;
    cfg.inheritColumns = jsonObj.inheritColumns;
    cfg.defaultValues = jsonObj.defaultValues;
    if ("" == jsonObj.initJs) {
        cfg.initEvent = null;
    } else {
        try {
            cfg.initEvent = eval("(" + jsonObj.initJs + ")");
        } catch (e) {
            dhtmlx.alert("onload事件语法有问题，请联系管理员！");
        }
    }
    if ("" == jsonObj.changeJs) {
        cfg.changeEvent = null;
    } else {
        try {
            cfg.changeEvent = eval("(" + jsonObj.changeJs + ")");
        } catch (e) {
            dhtmlx.alert("onchange事件语法有问题，请联系管理员！");
        }
    }
    if ("" == jsonObj.beforeSaveJs) {
        cfg.beforSaveEvent = null;
    } else {
        try {
            cfg.beforeSaveEvent = eval("(" + jsonObj.beforeSaveJs + ")");
        } catch (e) {
            dhtmlx.alert("beforeSave事件语法有问题，请联系管理员！");
        }
    }
    if ("" == jsonObj.afterSaveJs) {
        cfg.afterSaveEvent = null;
    } else {
        try {
            cfg.afterSaveEvent = eval("(" + jsonObj.afterSaveJs + ")");
        } catch (e) {
            dhtmlx.alert("afterSave事件语法有问题，请联系管理员！");
        }
    }
    return cfg;
}
/**
 * 初始化表单
 * @param layout 表单所在的layoutCell
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @returns {object} 表单对象
 */
function CFG_form(fLayout, tableId, moduleId) {
    var fcfg = CFG_fcfg(tableId, moduleId);
    if (null == fcfg || null == fcfg.formJson) {
        dhtmlx.message("界面未配置！");
        return null;
    }
    // 初始化表单
    var form = fLayout.attachForm(fcfg.formJson);
    // 表单初始化
    if (null != fcfg.initEvent) {
        fcfg.initEvent();
    }
    // onchange 事件
    if (null != fcfg.changeEvent) {
        form.attachEvent("onChange", fcfg.changeEvent);
    }
    return form;
}
;

/******************************************************************以下为自定义工具条******************************************************************/
/**
 * 获取自定义按钮配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @param componentVersionId 构件版本ID
 * @param type 按钮类型:0-表单按钮 1-列表按钮
 * @returns {object}
 */
function CFG_btns(tableId, moduleId, componentVersionId, type) {
    var url = CFG_app_uri + "/app-button!dhtmlxToolbar.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId
            + "&P_componentVersionId=" + componentVersionId + "&menuId=" + CFG_urlParams['menuId'] + "&P_type=" + type;
    return loadJson(url);
}
/**
 * 获取自定义报表配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @returns {object}
 */
function CFG_rpts(tableId, moduleId) {
    var url = CFG_app_uri + "/report-binding!bindedReports.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId;
    return loadJson(url);
}
/**
 * 获取自定义报表（报表权限筛选）配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @param componentVersionId 构件版本ID
 * @returns {object}
 */
function CFG_rpts(tableId, moduleId, componentVersionId) {
    var url = CFG_app_uri + "/report-binding!bindedReports2.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId
            + "&P_componentVersionId=" + componentVersionId;
    return loadJson(url);
}
/**
 * 初始化自定义工具条
 * @param bLayout 工具条所在的layoutCell
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @param componentVersionId 构件版本ID
 * @param type 列表或表单，CFG_common.L_GRID or CFG_common.L_FORM
 * @returns {object} 工具条对象
 */
function CFG_toolbar(bLayout, tableId, moduleId, componentVersionId, type) {
    var btns = CFG_btns(tableId, moduleId, componentVersionId, type);
    if (null == btns || btns.length == 0)
        return null;
    bLayout.detachToolbar();
    var tbar = bLayout.attachToolbar();
    tbar.setIconPath(TOOLBAR_IMAGE_PATH);
    // var pos = 0;
    for (var i = 0; i < btns.length; i++) {
        var btn = btns[i];
        if (CFG_common.B_BUTTON == btn.type) {
            if (CFG_common.P_REPORT == btn.id) {
                tbar.addButtonSelect(btn.id, btn.pos, btn.name, [], btn.image);
                var reports = CFG_rpts(tableId, moduleId);
                for (var j = 0; j < reports.length; j++) {
                    var report = reports[j];
                    tbar.addListOption(btn.id, (CFG_common.P_REPORT_PRE + reports[j][0]), j, "button", reports[j][1],
                            "print.gif"
                    );
                }
            } else {
                tbar.addButton(btn.id, btn.pos, btn.name, btn.image);
            }
        } else if (CFG_common.B_SEPARATOR == btn.type) {
            tbar.addSeparator(btn.id, btn.pos);
        }
        //
        // if (pos < btn.pos) pos = btn.pos;
    }
    return tbar;
}
/******************************************************************以下为自定义查询区******************************************************************/
/**
 * 获取自定义查询区配置信息
 * @param tableId 表ID
 * @param moduleId 模块ID
 * @returns {object}
 */
function CFG_scfg(tableId, moduleId) {
    var url = contextPath + "/appmanage/app-search!searchForm.json?P_tableId=" + tableId + "&P_moduleId=" + moduleId;
    return loadJson(url);
}
/**
 * 初始化自定义查询区
 * @param sLayout 查询区所在的layoutCell
 * @param tableId 表ID
 * @param moduleId 模块ID
 */
function CFG_searcharea(sLayout, tableId, moduleId) {
    var scfg = CFG_scfg(tableId, moduleId);
    if (false == scfg.exists) {
        dhtmlx.alert("检索项未配置！");
        return null;
    } else if ("ERROR" == scfg.exists) {
        dhtmlx.alert("获取检索配置出错！");
        return null;
    }
    sLayout.setHeight(scfg.height);
    // sLayout.collapse();
    sLayout.setText("查询区");
    var formJson = eval("(" + scfg.formJson + ")");
    var form = sLayout.attachForm(formJson);
    return form;
}
/**
 * 将类名转化为访问的action uri
 * @param name 类名称
 * @returns {string}
 */
function CFG_cast2action(name) {
    return name.replace(/[A-Z]/g, function(target, index) {
        if (0 == index)
            return target.toLowerCase();
        return ("-" + target.toLowerCase());
    });
}
/**
 * 获取指定action
 * @param name  类名称
 * @param btnId 按钮ID
 * @returns {string}
 */
function CFG_getAction(name, btnId) {
    if (typeof MT_getAction == "function") {
        return MT_getAction(btnId);
    }
    return CFG_cast2action(name);
}