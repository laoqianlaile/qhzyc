/**
 * MT => module template 模块模板 NODE => tree 树节点
 * @param that
 */
function MT_NODE_init(that, nLayout, node) {
    // 初始化表单与列表数量
    that.fNum = that.gNum = 0;
    // 页面布局
    if ("3" == node.layoutType) {
        // 整个页面
        MT_NODE_1C_init(that, nLayout, node);
    } else if ("2" == node.layoutType) {
        // 上下结构
        MT_NODE_2E_init(that, nLayout, node);
    } else if ("1" == node.layoutType) {
        // 左右结构
        MT_NODE_2U_init(that, nLayout, node);
    } else if ("4" == node.layoutType) {
        // 左上下结构
        MT_NODE_3L_init(that, nLayout, node);
    }
}
/**
 * 初始化整个页面结构布局
 * @param that
 * @param nLayout
 * @param node
 */
function MT_NODE_1C_init(that, nLayout, node) {
    var subLayout = nLayout.attachLayout("1C");

    var aLayout = subLayout.cells("a");
    aLayout.hideHeader();
    // 初始化区域
    MT_SubLayoutInit(that, aLayout, node.table1Id, node.area1Id, 1);
}
/**
 * 初始化上下结构布局
 * @param that
 * @param nLayout
 * @param node
 */
function MT_NODE_2E_init(that, nLayout, node) {
    var subLayout = nLayout.attachLayout("2E");
    MT_NODE_2_init(subLayout, that, nLayout, node);
}
/**
 * 初始化左右结构布局
 * @param that
 * @param nLayout
 * @param node
 */
function MT_NODE_2U_init(that, nLayout, node) {
    var subLayout = nLayout.attachLayout("2U");
    MT_NODE_2_init(subLayout, that, nLayout, node);
}
/**
 * 初始化2块结构布局
 * @param that
 * @param nLayout
 * @param node
 */
function MT_NODE_2_init(subLayout, that, nLayout, node) {
    var aLayout = subLayout.cells("a");
    var bLayout = subLayout.cells("b");
    aLayout.hideHeader();
    bLayout.hideHeader();
    // 区域一
    MT_SubLayoutInit(that, aLayout, node.table1Id, node.area1Id, 1);
    // 区域二
    MT_SubLayoutInit(that, bLayout, node.table2Id, node.area2Id, 2);
}
/**
 * 初始化左上下结构布局
 * @param that
 * @param nLayout
 * @param node
 */
function MT_NODE_3L_init(that, nLayout, node) {
    var subLayout = nLayout.attachLayout("3L");
    var aLayout = subLayout.cells("a");
    var bLayout = subLayout.cells("b");
    var cLayout = subLayout.cells("c");
    aLayout.hideHeader();
    bLayout.hideHeader();
    cLayout.hideHeader();
    // 区域一
    MT_SubLayoutInit(that, aLayout, node.table1Id, node.area1Id, 1);
    // 区域二
    MT_SubLayoutInit(that, bLayout, node.table2Id, node.area2Id, 2);
    // 区域三
    MT_SubLayoutInit(that, cLayout, node.table3Id, node.area3Id, 3);
}
