/**
 * MT => module template 模块模板 TREE => tree 树
 * @param that 全局变量
 * @param tLayout 树布局面板
 * @param rLayout 树节点布局面板
 */
function MT_BASE_TREE_init(that, tLayout, nLayout) {
    tLayout.setWidth(240);
    var tree = tLayout.attachTree();
    var moduleUrl = that.moduleUrl;
    tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
    tree.attachEvent("onMouseIn", function(id) {
        tree.setItemStyle(id, "background-color:#D5E8FF;");
    });
    tree.attachEvent("onMouseOut", function(id) {
        tree.setItemStyle(id, "background-color:#FFFFFF;");
    });
    /** 树节点信息. */
    function MT_TREE_GetTreeItem(id) {
        var url = contextPath + "/appmanage/tree-define/" + id + ".json?_method=get";
        return loadJson(url);
    }
    // 模块配置
    var mObj = that.mObj;
    if (null == mObj || null == mObj.treeId || "" == mObj.treeId) {
        return;
    }
    // 树
    var tObj = MT_TREE_GetTreeItem(mObj.treeId);
    if (null == tObj || undefined == tObj.id || null == tObj.id || "" == tObj.id) {
        dhtmlx.alert("树未配置或树被删除了，请检查！");
        return;
    }
    that.nodeId = tObj.id;
    // 判断模块布局是否选中树节点筛选定位
    if ("" == mObj.isFiltrate || "0" == mObj.isFiltrate) {
        tLayout.setText("档案树");
        tLayout.showHeader();
    } else {
        tLayout.detachToolbar();
        var toolbar = tLayout.attachToolbar();
        toolbar.addInputText("searchTree", 1, "档案树节点检索(支持拼音)", "215");
        // toolbar 检索树节点
        toolbar.attachEvent("onEnter", function(itemId, value) {
            if ("searchTree" == itemId) {
                searchTreeNode(value);
            }
        });
        // 获取光标事件
        toolbar.attachEvent("onFocus", function(itemId) {
            toolbar.setValue("searchTree", "");
        });
        // 获取光标失焦事件
        toolbar.attachEvent("onblur", function(itemId) {
            toolbar.setValue("searchTree", "档案树节点检索(支持拼音)");
        });
    }
    function searchTreeNode(value) {
        var treeSubIds = tree.getAllSubItems(tObj.id);// 获取树的所有节点ItemId
        if (null == value || "" == value) {
            tree.setItemColor(treeArray[i], "black", "black");
            tree.openAllItems(tObj.id);
            return;
        }
        var noTreeNode = "";
        if (treeSubIds.indexOf(",") > 0) {
            var treeArray = treeSubIds.split(",");
            for (var i = 0; i < treeArray.length; i++) {
                var treeItemId = treeArray[i];
                tree.setItemColor(treeItemId, "black", "black");
                var treeText = tree.getItemText(treeItemId);
                var pinyin = PinYin4Js.toPinyin(treeText);
                var firstC = PinYin4Js.getFirstChar(treeText);
                if (treeText.indexOf(value) > -1 || pinyin.indexOf(value) > -1 || firstC.indexOf(value) > -1) {
                    tree.setItemColor(treeItemId, "red", "red");
                    tree.openItem(tree.getParentId(treeItemId));
                    tree.closeItem(treeItemId);
                    noTreeNode += value + ",";
                } else {
                    tree.closeItem(treeItemId);
                }
            }
            if (noTreeNode == undefined || noTreeNode == "") {
                dhtmlx.message("没有检索到你想要的节点!");
                tree.openAllItems(tObj.id);
            }
        }
    }
    var treeIds = MT_TREE_authority_tree_ids();
    var treeJson = {
        id : 0,
        item : [{
            id : tObj.id,
            text : tObj.name,
            im0 : "safe_close.gif",
            im1 : "safe_open.gif",
            im2 : "safe_close.gif",
            child : 1,
            userdata : [{
                name : "type",
                content : "0"
            }]
        }]
    };
    tree.setDataMode("json");
    tree.enableSmartXMLParsing(true);
    var url = MT_app_uri + "/tree-define!treeWithAuthority.json?E_model_name=tree&P_OPEN="
            + (("1" == mObj.isFiltrate ? true : false)) + "&Q_EQ_nodeRule=0" + "&P_orders=showOrder"
            + "&P_filterId=parentId" + "&F_in=name,child"
            + "&P_UD=type,dbId,value,layoutType,table1Id,area1Id,table2Id,area2Id,table3Id,area3Id,showNodeCount" + "&P_treeIds="
            + treeIds + "&P_moduleId=" + that.moduleId + "&P_componentVersionId=" + that.componentVersionId
            + "&P_showNodeCount=" + mObj.showNodeCount;
    tree.setXMLAutoLoading(url);
    tree.loadJSONObject(treeJson);
    tree.refreshItem(that.nodeId);
    tree.selectItem(that.nodeId, false, "0");
    var preNode = null;
    tree.attachEvent("onXLE", function(tree, nId){
    	if (mObj.showNodeCount) {
	    	var childNodeIds = tree.getAllSubItems(nId).split(',');
	    	for (var i in childNodeIds) {
	    		if (childNodeIds[i].indexOf("NR2_") == 0) {
	    			continue;
	    		}
	    		if (isNotEmpty(tree.getUserData(childNodeIds[i], "table1Id")) && true == tree.getUserData(childNodeIds[i], "showNodeCount")) {
	    			var data = loadJson(MT_app_uri + "/tree-define!getTreeNodeRecordCount.json?P_treeNodeId=" + childNodeIds[i] + "&P_moduleId=" + that.moduleId + "&P_componentVersionId=" + that.componentVersionId);
	    			if (typeof data == 'string') {
	    				data = eval("(" + data + ")");
	    			}
	    			var nodeText = tree.getItemText(childNodeIds[i]);
	    			tree.setItemText(childNodeIds[i], nodeText + "(" + data + ")");
	    		}
	    	}
    	}
    });
    // 点击节点刷新右边区域
    tree.attachEvent("onClick", function(nId) {
        that.type = tree.getUserData(nId, "type");
        // 树节点ID
        that.nodeId = nId;
        if ("0" == that.type || "1" == that.type || "4" == that.type) {
            MT_HELP_init(nLayout);
            preNode = null;
        } else {
            var layout = tree.getUserData(that.nodeId, "layoutType");
            if (null != layout && "" == layout) {
                preNode = null;
                return;
            }
            if ("9" == that.type) {
                that.MT_tree_column_filter = null;
                that.coflow = {};
                that.coflow.box = tree.getUserData(nId, "value");
            } else {
                var filterArr = MT_TREE_node_filter(nId);
                that.MT_tree_column_filter = filterArr.toString();
            }
            var curNode = MT_TREE_AssembleNode();
            // 渲染页面或重新加载数据
            MT_TREE_node_page(curNode);
            // 更新前一次节点信息
            preNode = curNode;
        }
        /** 构件组装方式为内嵌的代码，主动添加导航（非打开构件）的设置 start */
        // 1、清除页面上的嵌入的构件
        if (window.CFG_clearComponentZone) {
            CFG_clearComponentZone();
        }
        // 2、添加导航或改变导航的显示名称，CFG_NVG_pos1为记录导航所在导航条上的位置（变量可以自定义，但不要使用CFG_NVG_pos）
        if (window.CFG_NVG_pos1 != undefined) {
            if (window.CFG_NVG_setText) {
                CFG_NVG_setText(window.CFG_NVG_pos1, tree.getItemText(nId));
            }
        } else {
            if (window.CFG_NVG_addItem) {
                window.CFG_NVG_pos1 = CFG_NVG_addItem(tree.getItemText(nId));
            }
        }
        /** 构件组装方式为内嵌的代码，主动添加导航（非打开构件）的设置 end */
    });
    /**
     * 获取当前登录人员的有权限的树节点IDs
     */
    function MT_TREE_authority_tree_ids() {
        var url = contextPath + "/authority/authority-tree!getTreeNodeIds.json?P_componentVersionId=" + that.componentVersionId;
        var json = loadJson(url);
        return json.toString();
    }
    /**
     * 判断当前节点与上一次的页面布局是否相同、表是否相同；如果相同，则重置一下页面数据；否则，则重新布局。
     * @param preNode
     * @param curNode
     */
    function MT_TREE_node_page(curNode) {
        if ("9" == that.type || preNode == null || preNode.layoutType != curNode.layoutType
                || preNode.table1Id != curNode.table1Id) {
            // 渲染布局
            MT_NODE_init(that, nLayout, curNode);
        } else {
            MT_TREE_init_area(curNode.area1Id, curNode.table1Id);
            MT_TREE_init_area(curNode.area2Id, curNode.table2Id);
            MT_TREE_init_area(curNode.area3Id, curNode.table3Id);
        }
    }
    /**
     * 重置一个区域页面
     * @param areaId
     * @param tableId
     */
    function MT_TREE_init_area(areaId, tableId) {
        if (isEmpty(areaId) || isEmpty(tableId)) {
            return;
        }
        if (MT_common.L_FORM == areaId) { // 表单
            var pFId = MT_P_FormId(tableId);
            that[pFId].MT_FORM_create();
        } else { // 列表
            var pGId = MT_P_GridId(tableId);
            if (that[pGId].index > 1) {
                that[pGId].MT_GRID_disable_toolbar();
                that[pGId].disable_toolbar = true;
            } else {
                that[pGId].MT_GRID_reload();
            }
        }
    }
    /**
     * 节点过滤条件（从该节点到根节点所有字段节点添加到过滤条件中）
     * @param nId
     * @param filterArr
     * @returns Aarry
     */
    function MT_TREE_node_filter(nId, filterArr) {
        if (undefined == filterArr || null == filterArr) {
            filterArr = new Array();
        }
        var columnId = tree.getUserData(nId, "dbId");
        var value = tree.getUserData(nId, "value");
        var type = tree.getUserData(nId, "type");
        var table1Id = tree.getUserData(nId, "table1Id");
        var parentId = tree.getParentId(nId);
        if ("3" == type || "4" == type) {
            var isId = ("3" == type);
            var filter = MT_TREE_column(columnId, value, isId);
            if (null != filter)
                filterArr.push(filter);
        }
        if (parentId == tObj.id) {
            return filterArr;
        }
        return MT_TREE_node_filter(parentId, filterArr);
    }
    /**
     * 组装字段过渡条件（与列表检索条件形式一致）.
     * @param columnId
     * @param value
     * @param isId --columnId是否为字段ID：true-是，false-否(字段英文名)
     */
    function MT_TREE_column(columnId, value, isId) {
        if (isId) {
            var url = MT_app_uri + "/column-define!filterColumnName.json?id=" + columnId;
            var col = loadJson(url);
            if ("" == col)
                return null;
            return (col + CFG_cv_split + value);
        }

        return (("EQ_C_" + columnId) + CFG_cv_split + value);
    }
    /** 节点信息. */
    function MT_TREE_AssembleNode() {
        return {
            id : that.nodeId,
            name : tree.getItemText(that.nodeId),
            layoutType : tree.getUserData(that.nodeId, "layoutType"),
            table1Id : tree.getUserData(that.nodeId, "table1Id"),
            area1Id : tree.getUserData(that.nodeId, "area1Id"),
            table2Id : tree.getUserData(that.nodeId, "table2Id"),
            area2Id : tree.getUserData(that.nodeId, "area2Id"),
            table3Id : tree.getUserData(that.nodeId, "table3Id"),
            area3Id : tree.getUserData(that.nodeId, "area3Id")
        };
    }
    /** 树节点. */
    that.MT_TREE_reload = function() {
        tree.refreshItem(that.nodeId);
    };
    /** 刷新父节点. */
    that.MT_TREE_refresh_parent = function() {
        if (tObj.id == that.nodeId) {
            tree.refreshItem(that.nodeId);
        } else {
            var pNodeId = tree.getParentId(that.nodeId);
            tree.refreshItem(pNodeId);
        }
            // tree.selectItem(that.nodeId, false, "0");
    };
    /** 选中当前行. */
    that.MT_TREE_SelectNode = function(nodeId) {
        if (undefined == nodeId) {
            tree.selectItem(that.nodeId, false, "0");
        }
        tree.selectItem(nodeId, false, "0");
    };
}
function MT_HELP_init(hLayout) {
    hLayout.setText("操作说明");
    hLayout.showHeader();
    hLayout.attachObject(MT_HELP_create());
}
function MT_HELP_create() {
    var obj = document.getElementById("DIV-help");
    if (null == obj) {
        obj = document.createElement("DIV");
        obj.setAttribute("id", "DIV-help");
        obj.setAttribute("style", "font-family: Tahoma; font-size: 11px;display: none;");
        obj.innerHTML = "<ul> \n" + "<li type=\"square\">" + "<p><b>操作说明：</b><br></p> \n"
                + "<p>1. 选择树中的节点，右侧出现该节点的操作界面<br></p> \n" + "</li> \n" + "</ul> \n";
    }
    return obj;
}