/**
 * MT => module template 模块模板 FORM => form 表单
 * @param that 页面全局变量
 * @param fLayout 表单布局区域
 * @param tableId 表单对应表ID
 * @param areaIndex 区域索引位置
 * @param showInWindow 是否是弹出框的方式打开的
 */
function MT_FORM_init(that, fLayout, tableId, areaIndex, fIndex, dataId, master/*关联主列表{tableId:'', dataId:''}*/,
        showInWindow) {
    MT_BASE_FORM_init.call(this, that, fLayout, tableId, areaIndex, fIndex, dataId, master, showInWindow);
}