/**
 * MT => module template 模块模板 TREE => tree
 * @param tLayout 树布局面板
 * @param rLayout 树节点布局面板
 */
function MT_BASE_USER_init(tLayout, clickEvent, showLoginName) {
    tLayout.setText("机构人员树");
    tLayout.setWidth(240);
    var tree = tLayout.attachTree();
    var rootId = "-1";
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    // 树
    var treeJson = {
        id : 0,
        item : [{
            id : rootId,
            text : "机构人员树",
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1,
            type : "D"
        }]
    };
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    var url = MT_getAction() + "!treeOfUser.json?E_model_name=tree&P_showLoginName=" + (showLoginName ? 1 : 0);
    tree.setXMLAutoLoading(url);
    tree.loadJSONObject(treeJson);
    tree.refreshItem(rootId);
    tree.selectItem(rootId, false, "0");
    // 点击节点刷新右边区域
    tree.attachEvent("onClick", function(nId) {
        var type = tree.getAttribute(nId, "type");
        var name = tree.getItemText(nId);
        clickEvent(nId, name, type);
    });
}