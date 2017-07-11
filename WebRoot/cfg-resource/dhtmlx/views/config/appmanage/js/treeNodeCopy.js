/**
 * 模板字段复制配置
 * @param win
 * @returns {initAppButtonWin}
 */
function initTreeCopyWin(win){
	var _this = this;
	this.moduleNodeId = "-1";
	this.moduleUrl = AppActionURI.treeDefine;
	this.createModuleInited = this.configModuleInited = false;
	var curLayout = win.attachLayout("1C");
	var mbsbar = curLayout.attachStatusBar();
	var mbbtbar = new dhtmlXToolbarObject(mbsbar.id);
	mbbtbar.setIconPath(IMAGE_PATH);
	mbbtbar.addButton("bottom$save", 0, "保存", "save.gif");
	mbbtbar.addSeparator("bottom$septr$01", 1);
	mbbtbar.addButton("bottom$close", 4, "关闭", "default/close.png");
	mbbtbar.setAlign("right");
	mbbtbar.attachEvent("onClick", function(itemId) {
		if ("bottom$close" == itemId) {
			win.close();
		} else if ("bottom$save" == itemId) {
			var nodeIdCopyTo = _this.nodeId;//需要复制到的节点
			var treeNodeId = _this.copyTree.getSelectedItemId();//被复制的树节点
			var treeNodeType = _this.copyTree.getUserData(treeNodeId, "type");//被复制的树节点的类型
			if("-1" == treeNodeId || "" == treeNodeId){
				dhtmlx.alert("不能复制树定义节点！");
				return;
			}else if("0" == treeNodeType){
				dhtmlx.alert("不能复制树根节点！");
				return;
			}else if(treeNodeId == nodeIdCopyTo){
				dhtmlx.alert("不能选择相同的树节点复制！");
				return;
			}else if (_this.copyTree.getParentId(treeNodeId) == nodeIdCopyTo){
				dhtmlx.alert("当前根节点已经有此节点，不需要复制！");
				return;
			}
			dhtmlx.confirm({
				type:"confirm",
				text: "确定要复制所选节点吗？",
				ok: "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
						var CHE_Name_Url = _this.moduleUrl + "!checkTreeNodeCopy.json?P_parentId=" + nodeIdCopyTo + "&P_nodeId=" + treeNodeId;
						dhtmlxAjax.get(CHE_Name_Url, function(loader) {
					    	var obj = eval("(" + loader.xmlDoc.responseText + ")");
					    	if (obj.success == false) {
					    		dhtmlx.alert(obj.message);
					    	}else{
					    		var Copy_Url= _this.moduleUrl + "!treeNodeCopy.json?P_parentId=" + nodeIdCopyTo + "&P_nodeId=" + treeNodeId;
								dhtmlxAjax.get(Copy_Url, function(loader) {
									dhtmlx.message(getMessage("save_success"));
									_this.reloadGrid();
			    					_this.resetForm();
			    					_this.reloadTreeItem();
									win.close();							
								});	
					    	}
					    });
					}
				}
			});
		}
	});
		
	initLayout(curLayout);
	this.maLayout = curLayout.cells("a");
	initTeeeNodeCopy(_this);
}

function initTeeeNodeCopy(that){
	var layout = that.maLayout;
	//layout.setText("档案定义树");
	layout.hideHeader();
	layout.setWidth(240);
	that.copyTree = layout.attachTree();
	// 树样式
	copyTree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	copyTree.attachEvent("onMouseIn", function(id) {
		copyTree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	copyTree.attachEvent("onMouseOut", function(id) {
		copyTree.setItemStyle(id, "background-color:#FFFFFF;");
	});		
	var treeJson = {id:0, item:[{id:"-1",text:"树定义", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",child:1}]};		
	copyTree.setDataMode("json");
	copyTree.enableSmartXMLParsing(true);
	copyTree.setXMLAutoLoading(that.moduleUrl + "!tree.json?E_model_name=tree&P_filterId=parentId&F_in=name,child&P_UD=type,dbId,table1Id,nodeRule&P_orders=showOrder&Q_EQ_dynamic=0");
	copyTree.loadJSONObject(treeJson);
	copyTree.refreshItem("-1");
	copyTree.selectItem("-1", false, "0");
}

