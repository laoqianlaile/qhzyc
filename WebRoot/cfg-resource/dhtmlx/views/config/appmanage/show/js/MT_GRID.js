/**
 * MT => module template 模块模板 GRID => grid 列表
 * @param that 页面全局变量
 * @param layout 列表布局区域
 * @param tableId 列表对应表ID
 * @param areaIndex 区域索引位置
 */
function MT_GRID_init(that, layout, tableId, areaIndex, gIndex) {
    MT_BASE_GRID_init.call(this, that, layout, tableId, areaIndex, gIndex);
}
/**
 * 列表链接列事件
 * @param url
 */
function MT_GRID_link(url, tableId) {
    MT_BASE_GRID_link.call(this, url, tableId);
}
