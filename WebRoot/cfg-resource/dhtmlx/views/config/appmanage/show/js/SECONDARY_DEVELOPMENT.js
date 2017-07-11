/**
 * 二次开发定位自己controller
 * @returns {String}
 */
function MT_getAction() {
    return contextPath + "/appmanage/show-module";
}

/**
 * 二次开发初始化列表信息
 */
function MT_afterGridInit(grid, tableId, that, areaIndex) {

}

/**
 * 二次开发给列表工具条添加按钮
 * @param gtbar
 * @param pos --表示列表上“更多”按钮之前的位置 第一个按钮位置为10，第二按钮位置为20，以此类推
 *            一个按钮占两个位置（一个是按钮位置，另一个分隔符位置） 如果要添加的按钮要放在第一个位置后面，则此按钮位置范围为[12~19]
 * @param tableId --该参数可以区别给不同列表添加按钮，比如树型或上下主从列表等
 */
function MT_addGridToolbar(tbar, pos, tableId) {
    // 添加按钮示例
    /*var url = MT_app_uri + "/table-define!show.json?id=" + tableId;
    var jsonObj = loadJson(url);
    if (jsonObj.name=="待分类文件表")  {
    	tbar.addButton(MT_btnid_pre + "TH", (++pos), "退回", "update.gif");
    	tbar.addSeparator(MT_btnid_pre + "01", (++pos));
    	tbar.addButton(MT_btnid_pre + "YGD", (++pos), "预归档", "update.gif");
    	tbar.addSeparator(MT_btnid_pre + "02", (++pos));
    } else if (jsonObj.name=="文书案卷")  {
    	tbar.addButton(MT_btnid_pre + "ZJ", (++pos), "组卷", "update.gif");
    	tbar.addSeparator(MT_btnid_pre + "01", (++pos));
    	tbar.addButton(MT_btnid_pre + "CJ", (++pos), "拆卷", "update.gif");
    	tbar.addSeparator(MT_btnid_pre + "02", (++pos));
    	tbar.addButton(MT_btnid_pre + "BH", (++pos), "编号", "update.gif");
    	tbar.addSeparator(MT_btnid_pre + "03", (++pos));
    }//*/
}

/**
 * 二次开发给列表工具条添加按钮事件
 */
function MT_addGridToolbarClickEvent(id, grid, tableId, MT_GRID_reload/*function: reload grid data*/, that, areaIndex) {
    // 二次开发按钮事件示例
    /*if (id == (MT_btnid_pre + "12")) {
    	// to do...
    } else if (id == (MT_btnid_pre + "13")) {
    	// to do...
    } ...//*/
}

/**
 * 二次开发，当表单初始化好后，再做一些表单初始信息
 * @param form
 */
function MT_afterFormInit(form, tableId, that, areaIndex) {

}

/**
 * 二次开发：表单保存前校验
 * @return {success: true/false, message: "提示信息"}
 */
function MT_beforeFormSave(form, tableId, that, areaIndex) {
    return {
        success : true,
        message : "OK"
    };
}

/**
 * 二次开发：表单保存成功后做的事情
 */
function MT_afterFormSave(form, tableId, that, areaIndex) {

}

/**
 * 二次开发给表单工具条添加按钮
 * @param tbar
 * @param pos --表示表单上最后一个按钮位置 第一个按钮位置为10，第二按钮位置为20，以此类推
 *            一个按钮占两个位置（一个是按钮位置，另一个分隔符位置） 如果要添加的按钮要放在第一个位置后面，则此按钮位置范围为[12~19]
 * @param tableId -- 该参数可以区别给不同表单添加按钮，比如树型或上下主从列表等
 */
function MT_addFormToolbar(tbar, pos, tableId) {

}

/**
 * 二次开发给表单工具条添加按钮事件
 */
function MT_addFormToolbarClickEvent(id, form, tableId, that, areaIndex) {

}