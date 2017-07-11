var menuItemId, type, classification, currentSystemId, currentSystemName;
/**
 * 权限定义树初始化
 * @param layoutA
 * @param layoutB
 */
function initAuthorityTree(layoutA, layoutB) {
    layoutA.setText("权限定义树");
    layoutA.setWidth(240);
    tree = layoutA.attachTree();
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    // 这几个分类是固定的
    var treeJson = {id:0, item:[
       {id:"-A", text: "权限定义树", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif", open:true, item:[
            {id:'-R',text:"角色", child:1, type:"role"}, 
            {id:'-U',text:"用户", child:1, type:"user"}
        ]}
    ]};
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    tree.setXMLAutoLoading(AUTHORITY_URL + "!tree.json?E_model_name=tree&F_in=text,child&P_UD=type");
    tree.loadJSONObject(treeJson);
    // 点击节点刷新右边列表
    tree.attachEvent("onClick", function(id) {
        if (id == "-A" || id == "-R" || id == "-U" || id.indexOf("O_")==0 || id.indexOf("S_")==0) {
            initLayoutB_Auth_Help(layoutB);
            currentSystemId = null;
        } else {
            // 记录当前树节点ID
            objectNodeId = id;
            if (id.indexOf("R_") > -1) {
                currentSystemId = this.getParentId(id).replace("S_", "");
                currentSystemName = this.getItemText(this.getParentId(id));
            }
            if (id.indexOf("R_") > -1 || id.indexOf("U_") > -1) {
                var parentId = tree.getParentId(id);
                var objectId = id.substring(2);
                // 角色
                var objectType = "0";
                // 用户
                if (id.indexOf("U_") > -1) {
                    objectType = "1";
                }
                initLayoutBContent(layoutB, objectId, objectType);
            }
        }
    });
    tree.selectItem("-A", true);
}