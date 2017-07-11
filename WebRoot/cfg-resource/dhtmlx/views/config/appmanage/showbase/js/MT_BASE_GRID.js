/**
 * MT => module template 模块模板 GRID => grid 列表
 * @param that 页面全局变量
 * @param layout 列表布局区域
 * @param tableId 列表对应表ID
 * @param areaIndex 区域索引位置
 */
function MT_BASE_GRID_init(that, layout, tableId, areaIndex, gIndex) {
    layout.hideHeader();
    var gLayout = layout;
    var sLayout = null;
    var sform = null;
    var scfg = CFG_scfg(tableId, that.moduleId);
    if (scfg.type == 1 && true == scfg.exists) {
        MT_search_area(that, layout, tableId, scfg);
        sform = that.sform;
        sLayout = that.sLayout;
        gLayout = that.gLayout;
    }
    // 获取超链接预留区
    var linkReserveZoneName = MT_ZoneName(tableId, MT_common.L_GRID, areaIndex) + "_LINK";
    var hasLink = false;
    // 判断超链接预留区是否绑定了构件
    if (that.CFG_isReserveZoneBinding && that.CFG_isReserveZoneBinding(linkReserveZoneName, '1')) {
        hasLink = true;
    }
    // GRID配置信息
    var gcfg = CFG_gcfg(tableId, that.moduleId);
    if (null == gcfg || 0 == gcfg.headers.length) {
        dhtmlx.message("列表未配置！");
        return;
    }
    // 工具条初始化
    var gtbar = MT_TBAR_init(that, gLayout, tableId, MT_common.L_GRID, gClickEvent, areaIndex);
    if (scfg.type == 1) {
        try {
            var searchPos = gtbar.getPosition(MT_common.P_SEARCH);
            if (null != searchPos && searchPos > -1) {
                gtbar.setItemText(MT_common.P_SEARCH, "显示检索区");
            }
        } catch (e) {
        }
    }
    // 状态栏
    var gsbar = gLayout.attachStatusBar();
    var grid = gLayout.attachGrid();
    var gurl = CFG_gbaseUrl((MT_getAction() + "!search.json"), gcfg, tableId, that.moduleId, that.componentVersionId);
    if (hasLink) {
        gcfg.headers.push("<center>操作</center>");
        gcfg.widths.push("120");
        gcfg.types.push("ro");
        gcfg.aligns.push("center");
    }
    // 初始化列表
    CFG_ginit(grid, gcfg, gsbar, MT_GRID_load);
    // 二次开发：对列表进行初始化
    MT_afterGridInit(grid, tableId, that, areaIndex);
    /* 构造列表与表单对象属性ID */
    var pGId = MT_P_GridId(tableId);
    var pFId = MT_P_FormId(tableId);
    var pTId = MT_P_GridTableId(gIndex);
    /* 列表对应的表ID */
    that[pTId] = tableId;// 用于双列表关联查询
    /* 列表信息存储对象 */
    that[pGId] = {};
    /* 基本检索面板表单中的值对象 */
    that[pGId].MT_DATA_b_seach = null;
    /* 高级检索面板表单中的值对象 */
    that[pGId].MT_DATA_g_seach = null;
    /* 检索方式：MT_common.S_BASE/MT_common.S_GREAT */
    that[pGId].MT_DATA_seach_type = MT_common.S_BASE;
    that[pGId].index = gIndex; // 列表索引
    that[pGId].curRowId = null; // 当前选中记录的数据ID(一条)
    /** **********************列表公用事件*************************** */
    /** 列表数据刷新. */
    that[pGId].MT_GRID_reload = function() {
        MT_GRID_load();
        if (null != that[pGId].curRowId) {
            grid.selectRowById(that[pGId].curRowId);
        }
    };
    /** 列表数据清空. */
    that[pGId].MT_GRID_clear = function() {
        grid.clearAll();
    };
    /** 禁用工具条上的所有按钮. */
    that[pGId].MT_GRID_disable_toolbar = function() {
        if (gtbar) {
            gtbar.forEachItem(function(itemId) {
                gtbar.disableItem(itemId);
            });
        }
        grid.clearAll();
    };
    /** 启用工具条上的所有按钮. */
    that[pGId].MT_GRID_enable_toolbar = function() {
        if (gtbar) {
            gtbar.forEachItem(function(itemId) {
                gtbar.enableItem(itemId);
            });
        }
    };
    /**
     * 根据属性获取ID.
     * @param id
     * @param prop -- processInstanceId(流程实例ID)/workitemId(工作项ID)/workitemStatus(工作项状态)
     */
    that[pGId].MT_coflowId = function(id, prop) {
        if (MT_common.ROW_ID == prop) {
            return grid.getSelectedRowId();
        }
        return grid.getUserData(id, prop);
    };
    // 列表数据加载
    MT_GRID_load();
    // 如果是明细列表的话，初始化时禁用工具条
    if (that[pGId].index > 1) {
        that[pGId].MT_GRID_disable_toolbar();
        that[pGId].disable_toolbar = true;
    }
    // 用户个性设置：表头宽度
    grid.attachEvent("onResize", function(cInd, cWidth, obj) {
        // 如果最一列为空列，则需要以下这个限制
        // if (cInd >= gcfg.headers.length - 1) return false;
        obj["resizeColumnIndex"] = cInd;
        return true;
    });
    grid.attachEvent("onResizeEnd", function(obj) {
        var index = obj["resizeColumnIndex"];
        if (undefined != index && undefined != gcfg.columnIds[index]) {
            var width = obj.getColWidth(index);
            gcfg.widths[index] = width;
            var sUrl = CFG_app_uri + "/app-column!setUserWidth.json?P_tableId=" + tableId + "&P_moduleId="
                    + that.moduleId + "&P_columnId=" + gcfg.columnIds[index] + "&P_showOrder=" + (index + 1)
                    + "&P_width=" + width;
            dhtmlxAjax.get(addTimestamp(sUrl), function(loader) {
                var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
                if (jsonObj.success) {
                    dhtmlx.message(getMessage("operate_success"));
                } else {
                    dhtmlx.message(getMessage("operate_failure"));
                }
            });
        }
    });
    // 用户个性设置：表头位置
    grid.attachEvent("onBeforeCMove", function(cInd, posInd) {
        // 如果最一列为空列，则需要以下这个限制
        /*
         * if (cInd >= gcfg.headers.length - 1) return false; if (posInd >=
         * gcfg.headers.length - 1) return false;//
         */
        return true;
    });
    grid.attachEvent("onAfterCMove", function(cInd, posInd) {
        MT_GRID_gcfgAdjust(cInd, posInd);
        var sUrl = CFG_app_uri + "/app-column!setUserColumnPosition.json?P_tableId=" + tableId + "&P_moduleId="
                + that.moduleId + "&P_sourcePos=" + cInd + "&P_targetPos=" + posInd;
        dhtmlxAjax.get(addTimestamp(sUrl), function(loader) {
            var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
            if (true == jsonObj.success) {
                dhtmlx.message(getMessage("operate_success"));
            } else {
                dhtmlx.message(getMessage("operate_failure"));
            }
        });
    });
    // 查看或修改
    grid.attachEvent("onRowSelect", function(rId, ind) {
        // 当前选中记录的数据ID
        var rowIds = grid.getSelectedRowId();
        if (isEmpty(rowIds) || rowIds.indexOf(",") > -1) {
            that[pGId].curRowId = null;
            return;
        }
        that[pGId].curRowId = rId;
        // 判断列表对应的表单是否在同一个页面中
        if (that.fNum > 0 && that[pFId]) {
            if (that[pFId].disable_toolbar == true) {
                that[pFId].MT_FORM_enableToolbarAndForm();
                that[pFId].disable_toolbar = false;
            }
            that[pFId].MT_FORM_load(rId);
        } else if (that.gNum > that[pGId].index) { // 判断是否有从表列表
            var pRtId = MT_P_GridTableId(that[pGId].index + 1); // 列表对应的表ID属性名
            if (undefined != that[pRtId] && null != that[pRtId]) {
                var pRgId = MT_P_GridId(that[pRtId]);
                that[pRgId].MT_GRID_reload();
                // 激活明细列表工具条
                if (true == that[pRgId].disable_toolbar) {
                    that[pRgId].MT_GRID_enable_toolbar();
                    that[pRgId].disable_toolbar = false;
                }
                // 如果存在明细列表存在表单，则禁用
                var pFId1 = MT_P_FormId(that[pRtId]);
                if (undefined != that[pFId1] && null != that[pFId1] && that[pFId1].disable_toolbar == false) {
                    that[pFId1].MT_FORM_disableToolbarAndForm();
                    that[pFId1].disable_toolbar = true;
                }
            }
            // 从表的从表要清空数据，禁用工具条,如：点项目列表数据，禁用卷内列表工具条，清空卷内列表数据
            for (var i = that[pGId].index + 2; i <= that.gNum; i++) {
                var pRtId2 = MT_P_GridTableId(i);
                if (undefined != that[pRtId2] && null != that[pRtId2]) {
                    var pRgId2 = MT_P_GridId(that[pRtId2]);
                    that[pRgId2].MT_GRID_clear();
                    if (false == that[pRgId2].disable_toolbar) {
                        that[pRgId2].MT_GRID_disable_toolbar();
                        that[pRgId2].disable_toolbar = true;
                    }
                }
                // 如果存在明细列表存在表单，则禁用
                var pFId2 = MT_P_FormId(that[pRtId2]);
                if (undefined != that[pFId2] && null != that[pFId2] && that[pFId2].disable_toolbar == false) {
                    that[pFId2].MT_FORM_disableToolbarAndForm();
                    that[pFId2].disable_toolbar = true;
                }
            }
        }
    });
    /** **************************** 列表辅助方法定义区 ******************************** */
    /** 列表配置信息随表头移动而已调整 */
    function MT_GRID_gcfgAdjust(sourcePos, targetPos) {
        moveArrayPosition(gcfg.headers, sourcePos, targetPos);
        moveArrayPosition(gcfg.columns, sourcePos, targetPos);
        moveArrayPosition(gcfg.columnIds, sourcePos, targetPos);
        moveArrayPosition(gcfg.widths, sourcePos, targetPos);
        moveArrayPosition(gcfg.aligns, sourcePos, targetPos);
        moveArrayPosition(gcfg.types, sourcePos, targetPos);
        moveArrayPosition(gcfg.codetypes, sourcePos, targetPos);
        moveArrayPosition(gcfg.datatypes, sourcePos, targetPos);
        moveArrayPosition(gcfg.urls, sourcePos, targetPos);
        /*
         * 数组中的位置调整 把sourcePos位置调到targetPos位置上
         */
        function moveArrayPosition(arr/* array */, sourcePos, targetPos) {
            arr.splice(targetPos, 0, arr[sourcePos]);
            arr.splice(((sourcePos > targetPos) ? sourcePos + 1 : sourcePos), 1);
            return arr;
        }
    }
    /** 重新加载自定义列表. */
    function MT_GRID_reloadGrid() {
        MT_GRID_init(that, layout, tableId, areaIndex, gIndex);
    }
    /** 加载列表数据 */
    function MT_GRID_load(pageNumber, pageSize) {
        if (undefined == grid || null == grid || undefined == gurl || null == gurl || "" == gurl) {
            return;
        }
        // 禁用与树节点冲突的条件
        MT_GRID_DisableColumnNodeFilter();
        var url = MT_GRID_gurl(pageNumber, pageSize);
        if (isEmpty(url))
            return;
        var loader = dhtmlxAjax.getSync(encodeURI(url));
        grid.clearAll();
        var loaderDoc = loader.xmlDoc.responseText;
        if ("" == loaderDoc)
            return;
        var data = eval('(' + loaderDoc + ')');
        // 如果查询结果为空
        if (data.rows == undefined) {
            // dhtmlx.message("结果集为空！");
            return;
        }
        if (hasLink) {
            var link = CFG_getLinkReserveZoneButton(linkReserveZoneName);
            for (var i in data.rows) {
                data.rows[i].data.push(link);
            }
        }
        grid.parse(data, "json");// */
        /** 获取查询条件 作为构件方法的返回值，供根据查询结果做处理的功能使用 start */
        queryCondition = loadJson(MT_getAction() + "!getQueryCondition.json");
        /** 获取查询条件 作为构件方法的返回值，供根据查询结果做处理的功能使用 end */
    }
    /** 查询URL */
    function MT_GRID_gurl(pageNumber, pageSize) {
        var url = gurl;
        // 加上检索过滤条件
        url += "&P_filter=" + MT_GRID_filter();
        if (that.coflow && that.coflow.box) {
            url += "&P_box=" + that.coflow.box;
        }
        // 判断是否有主列表：如果有主列表需要根据主列表的当前选中记录的数据ID 来过滤
        var mTableId = mDataId = "";
        // 双列表
        if (that[pGId].index > 1) {
            // 1. 获取主列表的表ID
            var pMtId = MT_P_GridTableId(that[pGId].index - 1);
            mTableId = that[pMtId];
            // 2. 获取主列表中当前选中记录的数据ID
            var pMgId = MT_P_GridId(mTableId);
            mDataId = that[pMgId].curRowId;
            // 3. 如果主列表未选中记录，则不主动查询
            if (isEmpty(mDataId))
                return null;
        } else if (((1 == areaIndex/* 传入表ID与主列表为主从关系 */) || (2 == areaIndex && 1 == that[pGId].index/* 上表单（主表），下列表（从表） */))
                && ("masterId" in that)
                && isNotEmpty(that.masterId)
                && ("masterTableId" in that)
                && isNotEmpty(that.masterTableId) && that.masterTableId != tableId) {
            // 单列表
            mTableId = that.masterTableId;
            mDataId = masterId;
        }
        url += "&P_M_tableId=" + mTableId + "&P_M_dataId=" + mDataId;
        // 加上分页过滤条件
        if (!grid.pageSize) {
            grid.pageSize = PAGE_SIZE;
        }
        pageNumber = pageNumber ? pageNumber : grid.currentPage;
        pageSize = pageSize ? pageSize : grid.pageSize;
        url += "&P_pagesize=" + pageSize + "&P_pageNumber=" + pageNumber;
        return addTimestamp(url);
    }
    /** 组装列表查询过滤条件. */
    function MT_GRID_filter() {
        var filter = "";
        // 主列表时， 如果树节点为字段节点时，需要把这个字段加入过滤条件中
        if (that[pGId].index == 1 && undefined != that.MT_tree_column_filter && null != that.MT_tree_column_filter
                && "" != that.MT_tree_column_filter) {
            filter = ("," + that.MT_tree_column_filter);
        }
        if (MT_common.S_BASE == that[pGId].MT_DATA_seach_type) {
            // 基本查询区
            var data = MT_GRID_SearchAreaFilter();
            if (null != data) {
                for (var p in data) {
                    var v = data[p];
                    if (null == v || "" == v)
                        continue;
                    filter += ("," + p + CFG_cv_split + v);
                }
            }
        } else {
            // 高级查询区
            if (that[pGId].MT_DATA_g_seach && "" != that[pGId].MT_DATA_g_seach) {
                filter += ("," + that[pGId].MT_DATA_g_seach);
            }
        }
        if (filter.length > 0) {
            return filter.substring(1);
        }
        return filter;
    }
    /** **************************** 列表双击事件 ******************************** */
    var eurl = contextPath + "/appmanage/app-grid!show.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId;
    var appgrid = loadJson(eurl);
    if (appgrid && appgrid.dblclick && 0 != appgrid.dblclick) {
        grid.attachEvent("onRowDblClicked", function(rId, ind) {
            if (2 == appgrid.dblclick) {
                that.isView = true;
            }
            MT_GRID_create(rId, "修改");
        });
    }
    /** ***************************(工具条事件)****************************** */
    function gClickEvent(id) {
        if (MT_common.P_CREATE == id) {// 新增
            if (that.fNum > 0 && that[pFId]) {
                that[pFId].MT_FORM_create();
            } else {
                MT_GRID_create(undefined, "新增");
            }
        } else if (MT_common.P_UPDATE == id) {// 修改
            if (that.fNum > 0 && that[pFId]) {
                var rowId = grid.getSelectedRowId();
                if (null == rowId || "" == rowId) {
                    dhtmlx.alert("请选择一条记录进行修改！");
                    return;
                }
                if (rowId.split(",").length > 1) {
                    dhtmlx.alert("一次只能修改一条记录！");
                    return;
                }
                that[pFId].MT_FORM_load(rowId);
                if (that[pFId].disable_toolbar == true) {
                    that[pFId].MT_FORM_enableToolbarAndForm();
                    that[pFId].disable_toolbar = false;
                }
            } else {
                MT_GRID_update();
            }
        } else if (MT_common.P_DELETE == id) {// 删除
            MT_GRID_delete();
        } else if (MT_common.P_SEARCH == id) {// 检索
            MT_GRID_search();
        } else if (MT_common.P_TRACK == id) {// 跟踪
            MT_GRID_track();
        } else if (MT_common.P_RECALL == id) {// 撤回
            MT_GRID_recall();
        } else if (MT_common.P_UPLOAD == id) {// 上传附近
            MT_GRID_upload();
        } else if (id.indexOf(MT_common.P_REPORT_PRE) > -1) {
            MT_GRID_report(id);
        } else if (MT_common.P_CUSTOM_SEARCH == id) { // 检索自定义
            MT_GRID_custom(APP_searchcfg, APP_wincfg.search.title, APP_wincfg.search.width, APP_wincfg.search.height);
        } else if (MT_common.P_CUSTOM_COLUMN == id) { // 列表自定义
            MT_GRID_custom(APP_columncfg, APP_wincfg.column.title, APP_wincfg.column.width, APP_wincfg.column.height);
        } else if (MT_common.P_CUSTOM_SORT == id) { // 列表自定义
            MT_GRID_custom(APP_sortcfg, APP_wincfg.sort.title, APP_wincfg.sort.width, APP_wincfg.sort.height);
        } else if (id.match("CD_BUTTON_")) {
            CFG_clickToolbar(MT_ZoneName(tableId, MT_common.L_GRID, areaIndex), id);
        } else if (id == "CFG_closeComponentZone") {
            /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮点击事件设置 start */
            if (window.CFG_clickReturnButton) {
                CFG_clickReturnButton(id);
            }
            /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮点击事件设置 end */
        } else {
            MT_addGridToolbarClickEvent(id, grid, tableId, MT_GRID_load, that, areaIndex);
        }
    }
    /** *****************************工具条按钮事件【开始】******************************** */
    /** 个性化配置. */
    function MT_GRID_custom(customfun/* function */, title, width, height) {
        if (typeof customfun != "function")
            return;
        var w = (width > document.body.clientWidth) ? document.body.clientWidth : width;
        var h = (height > document.body.clientHeight) ? document.body.clientHeight : height;
        var app = {};
        app.tableId = tableId;
        app.modified = false;
        app.isCustom = true;
        var win = MT_CreateDhxWindow({
            id : "WIN$" + tableId,
            title : title,
            width : w,
            height : h
        });
        win.button("close").attachEvent("onClick", function() {
            closeDhtmlxWin();
        });
        customfun(win, app, that.moduleId, closeDhtmlxWin/* function */);
        /* 窗口关闭提示 */
        function closeDhtmlxWin() {
            if (app.modified === true) {
                dhtmlx.confirm({
                    type : "confirm",
                    text : "配置信息未保存，确定要关闭吗？",
                    ok : "确定",
                    cancel : "取消",
                    callback : function(flag) {
                        if (flag)
                            _close();
                    }
                });
            } else {
                _close();
            }
        }
        /** 关闭窗口事件. */
        function _close() {
            win.close();
            MT_GRID_reloadGrid();
        }
    }
    /** 新增. */
    function MT_GRID_create(dataId, title) {
        title = title || "详细信息";
        var win = MT_CreateDhxWindow({
            id : "WIN$" + tableId,
            title : title,
            width : width(),
            height : 400
        });
        // 顶部“关闭”按钮事件重写
        win.button("close").attachEvent("onClick", function() {
            close();
        });
        // 底部工具条“关闭”按钮
        var sbar = win.attachStatusBar();
        var tbar = new dhtmlXToolbarObject(sbar.id);
        tbar.setIconPath(TOOLBAR_IMAGE_PATH);
        tbar.addButton("bottom$close", 4, "关闭", "close.gif");
        tbar.setAlign("right");
        tbar.attachEvent("onClick", function(id) {
            if ("bottom$close" == id) {
                close();
            }
        });
        var master = null; // 关联主列表对象
        if (that[pGId].index > 1) {
            var pRTId = MT_P_GridTableId(that[pGId].index - 1); // 关联主列表表ID寄存属性
            if (undefined != that[pRTId]) {
                master = {};
                var rTableId = that[pRTId]; // 关联主列表表id值
                var pRGId = MT_P_GridId(rTableId); // 关联主列表信息寄存属性
                master.tableId = rTableId;
                master.dataId = that[pRGId].curRowId; // 关联主列表选中行的rowId值
            }
        }
        // 初始化表单
        MT_FORM_init(that, win, tableId, areaIndex, gIndex, dataId, master, true);
        win.showHeader();
        /** 窗口宽度. */
        function width() {
            var url = contextPath + "/appmanage/app-form!domain.json?P_tableId=" + tableId + "&P_moduleId="
                    + that.moduleId;
            var obj = loadJson(addTimestamp(url));
            var width = (obj.width + 20);
            if (width > 1024) {
                width = 1024;
            }
            return width;
        }
        /** 关闭事件. */
        function close() {
            // 1. 刷新列表
            // that[pGId].MT_GRID_reload(); // 保存时，已经刷新过列表
            // 2. 删除表单窗口属性
            if (undefined != that[pFId]) {
                that[pFId] = null;
            }
            // 3. 关闭表单窗口
            win.close();
        }
    }
    /** 修改. */
    function MT_GRID_update() {
        var rowId = grid.getSelectedRowId();
        if (null == rowId || "" == rowId) {
            dhtmlx.alert("请选择一条记录进行修改！");
            return;
        }
        if (rowId.split(",").length > 1) {
            dhtmlx.alert("一次只能修改一条记录！");
            return;
        }
        MT_GRID_create(rowId, "修改");
        // that[pFId].MT_FORM_load(rowId);
    }
    /** 删除. */
    function MT_GRID_delete() {
        var ids = grid.getSelectedRowId();
        if (null == ids || "" == ids) {
            dhtmlx.alert(getMessage("select_record"));
            return;
        }
        dhtmlx.confirm({
            text : getMessage("delete_warning"),
            ok : "确定",
            cancel : "取消",
            callback : function(success) {
                if (false == success)
                    return;
                var dTableId = ""; // 从表ID
                if (that.gNum > that[pGId].index) { // 判断是否有从表列表
                    var pMtId = MT_P_GridTableId(that[pGId].index + 1); // 列表对应的表ID属性名
                    var dTableId = that[pMtId];
                }
                var url = MT_getAction() + "/" + ids + ".json?_method=delete&P_tableId=" + tableId + "&P_D_tableId="
                        + dTableId;
                dhtmlxAjax.get(addTimestamp(url), function(loader) {
                    MT_GRID_load();
                    dhtmlx.message(getMessage("delete_success"));
                });
            }
        });
    }
    /** 检索. */
    function MT_GRID_search() {
        if (false == scfg.exists) {
            dhtmlx.alert("检索项未配置！");
            return;
        } else if ("ERROR" == scfg.exists) {
            dhtmlx.alert("获取检索配置出错！");
            return;
        }
        if (1 == scfg.type) {
            if (null == sLayout)
                return;
            if (sLayout.isCollapsed()) {
                sLayout.expand();
                gtbar.setItemText(MT_common.P_SEARCH, "隐藏检索区");
            } else {
                // MT_GRID_load();
                sLayout.collapse();
                gtbar.setItemText(MT_common.P_SEARCH, "显示检索区");
            }
            return;
        }
        var struction = eval("(" + scfg.formJson + ")");
        var win = MT_CreateDhxWindow({
            title : "检索",
            width : parseInt(scfg.width) + 20,
            height : scfg.height,
            modal : false
        });
        win.button("close").attachEvent("onClick", function() {
            win.close();
            if (null != sform) {
                sform.unload();
                sform = null;
            }
        });
        // 底部工具条“检索”按钮
        var sbar = win.attachStatusBar();
        var tbar = new dhtmlXToolbarObject(sbar.id);
        tbar.setIconPath(TOOLBAR_IMAGE_PATH);
        tbar.addButton("bottom$search", 1, "搜索", "search.gif");
        tbar.addButton("bottom$reset", 2, "重置", "reset.gif");
        tbar.setAlign("right");
        tbar.attachEvent("onClick", function(id) {
            if ("bottom$search" == id) {
                MT_GRID_load();
            } else if ("bottom$reset" == id) {
            	if (null != sform) {
	                initFormData(sform);
	            }
            }
        });// */
        // 初始化检索面板表单
        sform = win.attachForm(struction);
        // 上一次检索面板表单中的值
        // if (null != that[pGId].MT_DATA_seach)
        // sform.setFormData(that[pGId].MT_DATA_seach);
        if (null != that[pGId].MT_DATA_b_seach)
            sform.setFormData(that[pGId].MT_DATA_b_seach);
        // 把过滤条件中与树节点中字段节点相同条件置为不可用.
        MT_GRID_DisableColumnNodeFilter();
    }
    /** 组装查询区的过滤条件. */
    function MT_GRID_SearchAreaFilter() {
        if (null == sform)
            return;
        // that[pGId].MT_DATA_seach = {};
        that[pGId].MT_DATA_b_seach = {};
        var formData = sform.getFormData(true);
        for (var prop in formData) {
            if (prop.indexOf(CFG_oc_split) > 0 && sform.isItemEnabled(prop)) {
                var val = formData[prop];
                if (null == val) {
                    val = "";
                }
                that[pGId].MT_DATA_b_seach[prop] = val;// sform.getItemValue(id);
            }
        }
        return that[pGId].MT_DATA_b_seach;
    }
    /** 初始化嵌入式查询区
    function MT_GRID_SearchArea(sLayout) {
    	//sLayout.setHeight(scfg.height);
    	//sLayout.collapse();
    	//sLayout.setText("查询区");
    	var formJson = eval("(" + scfg.formJson + ")");
    	var form = sLayout.attachForm(formJson);
    	return form;
    }*/
    /** 把过滤条件中与树节点中字段节点相同条件置为不可用. */
    function MT_GRID_DisableColumnNodeFilter() {
        if (null == sform)
            return;
        var filter = that.MT_tree_column_filter;
        if (isEmpty(filter))
            filter = "";
        sform.forEachItem(function(id) {
            var ocp = CFG_oc_split + id.split(CFG_oc_split)[1] + CFG_cv_split;
            var idx = filter.indexOf(ocp);
            if (idx > -1) {
                var sIdx = filter.indexOf(CFG_cv_split, idx);
                var eIdx = filter.indexOf(",", sIdx);
                var val = "";
                sIdx += CFG_cv_split.length;
                if (eIdx > 0) {
                    val = filter.substring(sIdx, eIdx);
                } else {
                    val = filter.substring(sIdx);
                }
                sform.setItemValue(id, val);
                sform.disableItem(id);
            } else if (scfg.type == 1 && !sform.isItemEnabled(id)) {
                sform.enableItem(id);
                sform.setItemValue(id, "");
            }
        });
    }
    /** 跟踪.*/
    function MT_GRID_track() {
        var rowId = grid.getSelectedRowId();
        if (null == rowId || "" == rowId) {
            dhtmlx.alert("请选择一条记录进行跟踪！");
            return;
        }
        if (rowId.split(",").length > 1) {
            dhtmlx.alert("只能选择一条数据进行跟踪！");
            return;
        }
        var pid = that[pGId].MT_coflowId(rowId, MT_common.P_ID);
        var url = MT_getAction() + "!coflowTrack?P_op=graph&P_processInstanceId=" + pid;
        var iWidth = window.screen.width;
        var iHeight = window.screen.height;
        var win = window.open(addTimestamp(url), "流程跟踪", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width='
                + iWidth + ',innerWidth=' + iWidth
                + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
        return win;
    }
    /** 撤回.*/
    function MT_GRID_recall() {
        var rowId = grid.getSelectedRowId();
        if (null == rowId || "" == rowId) {
            dhtmlx.alert("请选择一条记录进行撤回！");
            return;
        }
        if (rowId.split(",").length > 1) {
            dhtmlx.alert("只能选择一条数据进行撤回！");
            return;
        }
        var workitemId = grid.getUserData(rowId, MT_common.W_ID);
        var url = (MT_getAction() + "!coflow.json?P_op=" + MT_common.P_RECALL + "&P_workitemId=" + workitemId);
        dhtmlxAjax.get(addTimestamp(url), function(loader) {
            var wid = eval("(" + loader.xmlDoc.responseText + ")");
            if (typeof(wid) == "number" && wid > 0) {
                dhtmlx.alert(getMessage("operate_success"));
                return;
            }
            dhtmlx.alert(getMessage("operate_failure"));
        });
    }
    /** 打印.*/
    function MT_GRID_report(btnId) {
        // var name = gtbar.getItemText(btnId);
        var reportId = btnId.substring(MT_common.P_REPORT_PRE.length);
        var url;
        if (_isIE) {
            url = DHX_RES_PATH + "/views/config/appmanage/report/reportprint.jsp?P_reportId=" + reportId
                    + "&P_tableId=" + tableId;
        } else {
            url = contextPath + "/cfg-resource/cell/views/reportpreview.jsp?reportPrintUrl=" + DHX_RES_PATH
                    + "/views/config/appmanage/report/reportprint.jsp?P_reportId=" + reportId + "&P_tableId=" + tableId;
        }
        var filter = grid.getSelectedRowId();
        if (null == filter || "" == filter) {
            filter = MT_GRID_filter();
            if (null != filter && "" != filter) {
                url += "&P_columnFilter=" + filter;
            }
        } else {
            url += "&P_rowIds=" + filter;
        }
        window.open(encodeURI(addTimestamp(url)), "打印", 'left=0,top=0,width=' + (screen.availWidth - 10) + ',height='
                + (screen.availHeight - 50)
                + ',toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes');
    }
    /**
     * 附件上传
     * @memberOf {TypeName}
     * @return {TypeName}
     */
    function MT_GRID_upload() {
        var tUrl = contextPath + "/appmanage/table-define!tableLabel.json?id=" + tableId;
        var tObj = loadJson(tUrl);
        if ("" != tObj) {
            var mTableId = mDataId = "";
            if (that[pGId].index > 1) {
                // 1. 获取主列表的表ID
                var pMtId = MT_P_GridTableId(that[pGId].index - 1);
                mTableId = that[pMtId];
                // 2. 获取主列表中当前选中记录的数据ID
                var pMgId = MT_P_GridId(mTableId);
                mDataId = that[pMgId].curRowId;
            } else {
                mTableId = tableId;
                mDataId = that[pGId].curRowId;
                if ("DOCUMENT" != tObj) {
                    if (null == mDataId || "" == mDataId) {
                        dhtmlx.message(getMessage("select_record"));
                        return;
                    }
                    if (mDataId.split(",").length > 1) {
                        dhtmlx.message(getMessage("select_only_one_record"));
                        return;
                    }
                }
            }
        } else {
            var rowId = grid.getSelectedRowId();
            if (null == rowId || "" == rowId) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (rowId.split(",").length > 1) {
                dhtmlx.message(getMessage("select_only_one_record"));
                return;
            }
        }
        // 模块ID
        var modlueId = that.moduleId;
        var docUrl = contextPath + "/upload/document-upload";
        // 判断该表是否有上传附件的权限
        dhtmlxAjax.get(docUrl + "!getDocUploadMessages.json?P_tableId=" + tableId, function(loader) {
            var result = eval("(" + loader.xmlDoc.responseText + ")");
            if (!result.success) {
                dhtmlx.alert(result.message);
            } else {
                if (!dhxWins) {
                    dhxWins = new dhtmlXWindows();
                }
                dataWin = dhxWins.createWindow(WIN_ID, 0, 0, 450, 280);
                dataWin.setModal(true);
                dataWin.setText("上传电子全文");
                dataWin.center();
                dataWin.button('park').hide();
                dataWin.button('minmax1').hide();
                var vaultDiv = document.createElement("div");
                vaultDiv.setAttribute("id", "vaultDiv");
                document.body.appendChild(vaultDiv);
                var upload_url = docUrl + "!uploadHandler";// -处理文件上传
                var getInfo_url = docUrl + "!getInfoHandler"; // 处理进度提示
                var getId_url = docUrl + "!getIdHandler"; // -处理会话初始化
                var vault = new dhtmlXVaultObject();
                vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
                vault.setServerHandlers(upload_url, getInfo_url, getId_url); // 向服务器端发送处理
                vault.setFilesLimit(100);// 选择上传文件个数
                vault.strings = {
                    remove : "移除",
                    done : "完成",
                    error : "上传失败!",
                    btnAdd : "选择文件",
                    btnUpload : "上传",
                    btnClean : "清空"
                };
                vault.onAddFile = function(fileName) {
                    var ext = this.getFileExtension(fileName);
                    return true;
                };
                // 获取文件对象属性
                vault.onUploadComplete = function(files) {
                    for (var i = 0; i < files.length; i++) {
                        var file = files[i];
                        var saveFileUrl;
                        if ("" != tObj) {
                            saveFileUrl = docUrl + "!saveFileMessages.json?P_tableId=" + mTableId + "&P_moduleId="
                                    + modlueId + "&rId=" + mDataId + "&P_fileName=" + file.name;
                        } else {
                            saveFileUrl = docUrl + "!saveFileMessages.json?P_tableId=" + tableId + "&P_moduleId="
                                    + modlueId + "&rId=" + rowId + "&P_fileName=" + file.name;
                        }
                        dhtmlxAjax.get(encodeURI(saveFileUrl), function(loader) {
                            var mes_result = eval("(" + loader.xmlDoc.responseText + ")");
                            if (!mes_result.success) {
                                if (mes_result.status == "1") {
                                    dhtmlx.alert(mes_result.message);
                                } else {
                                    dhtmlx.message(mes_result.message);
                                }
                            } else {
                                dhtmlx.message(mes_result.message);
                                if ("" != tObj) {
                                    if (tObj == "DOCUMENT") {
                                        MT_GRID_load();
                                    } else {
                                        grid.selectRowById(mDataId, false, false, true);
                                    }
                                } else {
                                    MT_GRID_load();
                                    grid.selectRowById(rowId, false, false, true);
                                }
                            }
                        });
                    }
                };
                vault.create("vaultDiv");
                dataWin.attachObject(vaultDiv);
            }
        });
    }
    /** *****************************(预留区、回调函数、输出参数函数)******************************** */
    var name = null;
    /** 输出参数方法. */
    name = MT_ReturnFunctionName(areaIndex, MT_common.L_GRID);
    _M_this[name] = function() {
        return {
            /* 选中一行的数据ID. */
            selectedRowId : function() {
                var ids = grid.getSelectedRowId();
                if (isEmpty(ids)) {
                    dhtmlx.alert(getMessage("select_record"));
                    return {
                        status : false,
                        id : ""
                    };
                }
                if (ids.indexOf(",") > 0) {
                    dhtmlx.alert(getMessage("select_only_one_record"));
                    return {
                        status : false,
                        id : ""
                    };
                }
                return {
                    status : true,
                    id : ids
                };
            },
            /* 选中行的数据ID. */
            selectedRowIds : function() {
                var ids = grid.getSelectedRowId();
                if (isEmpty(ids)) {
                    dhtmlx.alert(getMessage("select_record"));
                    return {
                        status : false,
                        id : ""
                    };
                }
                return {
                    status : true,
                    ids : ids
                };
            },
            /* 选中行的数据ID，允许为空. */
            selectedRowIdsAllowBlank : function() {
                var ids = grid.getSelectedRowId();
                return {
                    status : true,
                    ids : ids
                };
            },
            /* 所有行的数据ID. */
            allRowIds : grid.getAllRowIds(),
            /* 当前列表对应的表ID. */
            tableId : tableId,
            /* 当前列表所处的模块ID. */
            moduleId : that.moduleId,
            /* 如果列表与树存在关系，则为列表对应的树节点ID. */
            treeNodeId : that.nodeId,
			/* 上一次查询的查询条件.*/
			queryCondition : queryCondition
        };
    };
    /** 回调函数：刷新列表. */
    name = MT_ReturnPrefix(areaIndex) + "_g_reload";
    _M_this[name] = function() {
        MT_GRID_load();
    };
    
    that[pGId].grid = grid;
}
/**
 * 列表链接列事件
 * @param url
 */
function MT_BASE_GRID_link(url, tableId) {
    var that = this;
    var pGId = MT_P_GridId(tableId);
    if (isEmpty(url)) {
        dhtmlx.message("未设置链接地址！");
        return;
    }
    var paramObj = parameters(url);
    url += "&P_moduleId=" + that.moduleId + "&P_tableId=" + tableId;
    if (url.indexOf(".json?") > 0) {
        dhtmlxAjax.get(addTimestamp(url), function(loader) {
            var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
            if (jsonObj.success) {
                if (jsonObj.message && isNotEmpty(jsonObj.message)) {
                    dhtmlx.message(jsonObj.message);
                } else {
                    dhtmlx.message(getMessage("operate_success"));
                }
                that[pGId].MT_GRID_reload();
            } else {
                dhtmlx.message(getMessage("operate_failure"));
            }
        });
    } else {
    	if (paramObj.assembleType == "1") {
    		CFG_openComponent(url, paramObj.title || "详细信息");
    	} else {
	        var t = paramObj.title || "详细信息";
	        var w = paramObj.width || 800;
	        var h = paramObj.height || 600;
	        if (h > document.body.clientHeight) {
	            h = document.body.clientHeight;
	        }
	        var win = MT_CreateDhxWindow({
	            title : t,
	            width : w,
	            height : h,
	            modal : false
	        });
	        win.attachURL(addTimestamp(url));
	        win.button("close").attachEvent("onClick", function() {
	            win.close();
	            that[pGId].MT_GRID_reload();
	        });
	        var statusBar = win.attachStatusBar();
	        var toolBar = new dhtmlXToolbarObject(statusBar);
	        toolBar.setIconsPath(TOOLBAR_IMAGE_PATH);
			toolBar.addButton("close", 1, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
			toolBar.setAlign("right");
			toolBar.attachEvent("onClick", function(id) {
				if (id == "close") {
					win.close();
				}
			});
    	}
    }
    /**
     * 存储URL中的所有参数
     * @param url
     * @returns {___anonymous25694_25695}
     */
    function parameters(url) {
        var paramObj = {};
        var idx = url.indexOf("?");
        if (idx < 0)
            return paramObj;
        var paramStr = url.substring(idx + 1);
        var paramArr = paramStr.split("&");
        for (var i = 0; i < paramArr.length; i++) {
            var oneParamStr = paramArr[i];
            var oneParamArr = oneParamStr.split("=");
            paramObj[oneParamArr[0]] = (oneParamArr.length == 1 ? "" : oneParamArr[1]);
        }
        return paramObj;
    }
}
