/**
 * MT => module template 模块模板 FORM => form 表单
 * @param that 页面全局变量
 * @param fLayout 表单布局区域
 * @param tableId 表单对应表ID
 * @param dataId 数据ID
 */
function MT_FORM_VIEW_init(that, fLayout, tableId, dataId) {
    var _this = this;
    var thatParent = that.parent;
    var fcfg = CFG_fcfg(tableId, that.moduleId);
    if (null == fcfg || null == fcfg.formJson) {
        dhtmlx.message("界面未配置！");
        return;
    }
    // 属性名
    var pGId = MT_P_GridId(tableId);
    var pFId = MT_P_FormId(tableId);
    // 配置首条、上一条、下一条、末条时使用
    var rowIndex, maxRowIndex;
    if (dataId) {
        rowIndex = thatParent[pGId].grid.getRowIndex(dataId);
        if (thatParent[pGId].grid.currentPage == thatParent[pGId].grid.totalPages) {
            maxRowIndex = thatParent[pGId].grid.rowsBuffer.length % thatParent[pGId].grid.pageSize - 1;
        } else {
            maxRowIndex = thatParent[pGId].grid.pageSize - 1;
        }
    }
    // 工具条初始化
    var ftbar = fLayout.attachToolbar();
    ftbar.addButton(MT_common.P_FIRST_RECORD, 0, "首条");
    ftbar.addButton(MT_common.P_PREVIOUS_RECORD, 1, "上一条");
    ftbar.addButton(MT_common.P_NEXT_RECORD, 2, "下一条");
    ftbar.addButton(MT_common.P_LAST_RECORD, 3, "末条");
    if (window.CFG_setReturnButton) {
	    CFG_setReturnButton(ftbar);
    }
    // 处理工具条上面的按钮
    if (dataId) {
        if (thatParent[pGId].grid) {
            if (rowIndex == 0) {
                if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
                    ftbar.disableItem(MT_common.P_FIRST_RECORD);
                }
                if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
                    ftbar.disableItem(MT_common.P_PREVIOUS_RECORD);
                }
            }
            if (rowIndex == maxRowIndex) {
                if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
                    ftbar.disableItem(MT_common.P_NEXT_RECORD);
                }
                if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
                    ftbar.disableItem(MT_common.P_LAST_RECORD);
                }
            }
        }
    } else {
        if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
            ftbar.hideItem(MT_common.P_FIRST_RECORD);
        }
        if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
            ftbar.hideItem(MT_common.P_PREVIOUS_RECORD);
        }
        if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
            ftbar.hideItem(MT_common.P_NEXT_RECORD);
        }
        if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
            ftbar.hideItem(MT_common.P_LAST_RECORD);
        }
    }
    // 初始化表单
    var form = fLayout.attachForm(fcfg.formJson);
    // 表单初始化
    if (null != fcfg.initEvent) {
        fcfg.initEvent(form);
    }
    // 数据加载
    MT_FORM_load(dataId);
    /** 表单数据加载. */
    function MT_FORM_load(id) {
        var url = contextPath + "/appmanage/show-module!show.json?P_tableId=" + tableId + "&P_moduleId="
                + that.moduleId + "&id=" + id;
        var json = loadJson(url);
        if (null != json && "" != json) {
            form.setFormData(json);
        }
        form.lock();
    }
    /** ********************* 工具条事件定义 ************************* */
    ftbar.attachEvent("onclick", function(id) {
        if (MT_common.P_FIRST_RECORD == id) {
            rowIndex = 0;
            PV_FORM_LoadByIndex(rowIndex);
        } else if (MT_common.P_PREVIOUS_RECORD == id) {
            PV_FORM_LoadByIndex(--rowIndex);
        } else if (MT_common.P_NEXT_RECORD == id) {
            PV_FORM_LoadByIndex(++rowIndex);
        } else if (MT_common.P_LAST_RECORD == id) {
            rowIndex = maxRowIndex;
            PV_FORM_LoadByIndex(rowIndex);
        } else if (id == "CFG_closeComponentZone") {
            if (window.CFG_clickReturnButton) {
                CFG_clickReturnButton(id);
            }
        }
    });
    /** 首条、上一条、下一条、末条. */
    function PV_FORM_LoadByIndex(rowIndex) {
        var rowId = thatParent[pGId].grid.getRowId(rowIndex);
        MT_FORM_load(rowId);
        if (rowIndex == 0) {
            if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
                ftbar.disableItem(MT_common.P_FIRST_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
                ftbar.disableItem(MT_common.P_PREVIOUS_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
                ftbar.enableItem(MT_common.P_NEXT_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
                ftbar.enableItem(MT_common.P_LAST_RECORD);
            }
        } else if (rowIndex == maxRowIndex) {
            if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
                ftbar.enableItem(MT_common.P_FIRST_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
                ftbar.enableItem(MT_common.P_PREVIOUS_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
                ftbar.disableItem(MT_common.P_NEXT_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
                ftbar.disableItem(MT_common.P_LAST_RECORD);
            }
        } else {
            if (ftbar.getPosition(MT_common.P_FIRST_RECORD) != null) {
                ftbar.enableItem(MT_common.P_FIRST_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_PREVIOUS_RECORD) != null) {
                ftbar.enableItem(MT_common.P_PREVIOUS_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_NEXT_RECORD) != null) {
                ftbar.enableItem(MT_common.P_NEXT_RECORD);
            }
            if (ftbar.getPosition(MT_common.P_LAST_RECORD) != null) {
                ftbar.enableItem(MT_common.P_LAST_RECORD);
            }
        }
    }
}