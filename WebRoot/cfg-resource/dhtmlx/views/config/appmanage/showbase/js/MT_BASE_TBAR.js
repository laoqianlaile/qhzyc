/**
 * MT => module template 模块模板 B => tool bar 工具条
 * @param that
 * @param bLayout
 * @param tableId 表ID
 * @param type 1-列表按钮，0-表单按钮
 * @param clickEvent 工具条单击事件
 * @param areaIndex 区域索引位置：如区域一为1， 区域二为2 (标记预留区用的)
 */
function MT_BASE_TBAR_init(that, bLayout, tableId, type, clickEvent, areaIndex) {
    var btns = CFG_btns(tableId, that.moduleId, that.componentVersionId, type);
    bLayout.detachToolbar();
    var tbar = bLayout.attachToolbar();
    tbar.setIconPath(TOOLBAR_IMAGE_PATH);
    var pos = 0;
    // 工作流主列表必须一定是在区域一中，其他区域则按按钮配置显示
    if (that.type == undefined || "9" != that.type || ("9" == that.type && MT_common.L_GRID == type && areaIndex > 1)) {
        for (var i = 0; i < btns.length; i++) {
            var btn = btns[i];
            if (MT_common.B_BUTTON == btn.type) {
                if (MT_common.P_REPORT == btn.id) {
                    tbar.addButtonSelect(btn.id, btn.pos, btn.name, [], btn.image);
                    var reports = CFG_rpts(tableId, that.moduleId, that.componentVersionId);
                    for (var j = 0; j < reports.length; j++) {
                        var report = reports[j];
                        tbar.addListOption(btn.id, (MT_common.P_REPORT_PRE + reports[j][0]), j, "button",
                                reports[j][1], "print.gif"
                        );
                    }
                } else {
                    tbar.addButton(btn.id, btn.pos, btn.name, btn.image);
                }
            } else if (MT_common.B_SEPARATOR == btn.type) {
                tbar.addSeparator(btn.id, btn.pos);
            }
            if (pos < btn.pos)
                pos = btn.pos;
        }
    }
    // 工作箱
    if (that.type && "9" == that.type) {
        if (MT_common.L_FORM == type) {
            if (MT_common.BOX_APPLYFOR == that.coflow.box) {
                tbar.addButton(MT_common.P_SAVE, ++pos, "保存", "save.gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                tbar.addButton(MT_common.P_START, ++pos, "启动", MT_common.P_START + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
            } else if (MT_common.BOX_TODO == that.coflow.box) {
                tbar.addButton(MT_common.P_CHECKOUT, ++pos, "签收", MT_common.P_CHECKOUT + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                tbar.addButton(MT_common.P_COMPLETE, ++pos, "提交", MT_common.P_COMPLETE + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                tbar.addButton(MT_common.P_UNTREAD, ++pos, "退回", MT_common.P_UNTREAD + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                /*
                 * tbar.addButton(MT_common.P_REASSIGN, ++pos, "转办",
                 * MT_common.P_REASSIGN + ".gif");
                 * tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                 * tbar.addButton(MT_common.P_DELIVER, ++pos, "传阅",
                 * MT_common.P_DELIVER + ".gif");
                 * tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                 */
                tbar.addButton(MT_common.P_TRACK, ++pos, "跟踪", MT_common.P_TRACK + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);//
            } else if (MT_common.BOX_HASDONE == that.coflow.box) {
                tbar.addButton(MT_common.P_RECALL, ++pos, "撤回", MT_common.P_RECALL + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
            } else if (MT_common.BOX_COMPLETE == that.coflow.box) {
                tbar.addButton(MT_common.P_TRACK, ++pos, "跟踪", MT_common.P_TRACK + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
            } else if (MT_common.BOX_TOREAD == that.coflow.box) {
                tbar.addButton(MT_common.P_RECALL, ++pos, "跟踪", MT_common.P_RECALL + ".gif");
                tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
            }
        } else {
            if (1 == areaIndex) {
                if (MT_common.BOX_APPLYFOR == that.coflow.box) {
                    tbar.addButton(MT_common.P_CREATE, ++pos, "新增", "new.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                    tbar.addButton(MT_common.P_UPDATE, ++pos, "修改", "update.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                    tbar.addButton(MT_common.P_DELETE, ++pos, "删除", "delete.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                } else if (MT_common.BOX_TODO == that.coflow.box) {
                    tbar.addButton(MT_common.P_UPDATE, ++pos, "办理", "update.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                    tbar.addButton(MT_common.P_TRACK, ++pos, "跟踪", MT_common.P_TRACK + ".gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                } else if (MT_common.BOX_HASDONE == that.coflow.box) {
                    tbar.addButton(MT_common.P_UPDATE, ++pos, "查看", "update.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                    // tbar.addButton(MT_common.P_RECALL,
                    // ++pos, "撤回",
                    // MT_common.P_RECALL + ".gif");
                    // tbar.addSeparator(MT_common.SEPARATOR_PRE
                    // + (++pos),
                    // pos);
                } else if (MT_common.BOX_COMPLETE == that.coflow.box) {
                    tbar.addButton(MT_common.P_UPDATE, ++pos, "查看", "update.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                } else if (MT_common.BOX_TOREAD == that.coflow.box) {
                    tbar.addButton(MT_common.P_UPDATE, ++pos, "查看", "update.gif");
                    tbar.addSeparator(MT_common.SEPARATOR_PRE + (++pos), pos);
                }
                /* 检索与打印按钮. */
                for (var i = 0; i < btns.length; i++) {
                    var btn = btns[i];
                    if (MT_common.B_BUTTON == btn.type && MT_common.P_REPORT == btn.id) {
                        tbar.addButtonSelect(btn.id, (++pos), btn.name, [], btn.image);
                        var reports = CFG_rpts(tableId, that.moduleId);
                        for (var j = 0; j < reports.length; j++) {
                            var report = reports[j];
                            tbar.addListOption(btn.id, (MT_common.P_REPORT_PRE + reports[j][0]), j, "button",
                                    reports[j][1], "print.gif"
                            );
                        }
                    } else if (MT_common.P_CREATE == btn.id || MT_common.P_UPDATE == btn.id
                            || MT_common.P_DELETE == btn.id) {
                        i++;
                        continue;
                    }
                }
            }
        }
    }
    var rzName = MT_ZoneName(tableId, type, areaIndex);
    // 列表工具条添加“更多”按钮
    if (MT_common.L_GRID == type) {
        // 二次开发向列表添加按钮
        MT_addGridToolbar(tbar, pos, tableId);
        // 个性化自定义
        tbar.addButtonSelect("CFG_Config", ++pos, "配置", [], "setup.gif", null, "disabled", true);
        var searchPos = tbar.getPosition(MT_common.P_SEARCH);
        if (null != searchPos && searchPos > -1) {
            tbar.addListOption("CFG_Config", MT_common.P_CUSTOM_SEARCH, ++pos, "button", "检索自定义", "setup.gif");
        }
        tbar.addListOption("CFG_Config", MT_common.P_CUSTOM_COLUMN, ++pos, "button", "列表自定义", "setup.gif");
        tbar.addListOption("CFG_Config", MT_common.P_CUSTOM_SORT, ++pos, "button", "排序自定义", "setup.gif");
    } else {
        // 二次开发向表单添加按钮
        MT_addFormToolbar(tbar, pos, tableId);
    }
    CFG_addToolbarButtons(tbar, rzName, pos++);
    /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮设置 start */
    CFG_setReturnButton(tbar);
    /** 构件组装方式为内嵌的代码，内嵌构件上添加返回按钮设置 end */

    var btnCnt = 0;
    tbar.forEachItem(function(itemId) {
        btnCnt++;
    });
    if (btnCnt == 0) {
        bLayout.detachToolbar();
        tbar = null;
    } else {
        tbar.attachEvent("onClick", function(id) {
            if (typeof clickEvent == "function") {
                buttonType = 0;
                clickEvent(id);
            }
        });
    }
    return tbar;
}