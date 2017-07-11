/**
 * MT => module template 模块模板 B => tool bar 工具条
 * @param that
 * @param bLayout
 * @param tableId 表ID
 * @param type 1-列表按钮，0-表单按钮
 * @param clickEvent 工具条单击事件
 * @param areaIndex 区域索引位置：如区域一为1， 区域二为2 (标记预留区用的)
 */
function MT_TBAR_init(that, bLayout, tableId, type, clickEvent, areaIndex) {
    return MT_BASE_TBAR_init.call(this, that, bLayout, tableId, type, clickEvent, areaIndex);
}