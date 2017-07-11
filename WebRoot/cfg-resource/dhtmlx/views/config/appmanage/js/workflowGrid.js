/**
 * 列表初始化
 */
function initWfGrid(layout, that) {
	// 隐藏标题
	layout.hideHeader();
	// 初始变量
	that.gtbar = layout.attachToolbar();
	that.grid  = layout.attachGrid();
	that.gcfg  = {
			format: {
				headers: ["<center>名称</center>","<center>类型<center/>","<center>包ID<center/>","<center>流程ID<center/>","<center>备注</center>","<center>操作</center>",""],
				cols:["name", "type", "packageId", "processId", "remark", "operator"],
				userdata:["tableId"],
				colWidths: ["160","60","100","100","200","120","*"],
				colTypes: ["ro","coro","ro","ro","ro","link","ro"],
				colAligns: ["left","center","left","left","left","center","left"]
			}
		};
	this.gurl = getGridUrl(that);
	
	var _this = this;
	var ttbar = that.gtbar;
	var grid  = that.grid;
	var cfg   = that.gcfg;
	var url   = that.gurl;
	ttbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	ttbar.addButton("add", 1, "新增", "new.gif");
	ttbar.addSeparator("top$septr$01", 2);
	ttbar.addButton("del", 5, "删除", "delete.gif");
	
	ttbar.attachEvent("onClick", function(itemId) {
		if ("add" == itemId) {
			_this.add();
		} else if ("del" == itemId) {
			_this.del();
		}
	});
	//
	this.add = function () {
		that.resetForm();
	};
	// 
	this.del = function () {
		/*var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.alert("请选择要删除的记录（一条或多条）！");
			return;
		}
		var idArr = rowIds.split(",");
		var clfNames = "", tip="确定要删除所选记录？";
		for(var i = 0; i < idArr.length; i++) {
			var type = grid.getUserData(idArr[i], "type");
			if (type == 0) {
				clfNames += "," + grid.cells(idArr[i], 0).getValue();
			}
		}
		if (clfNames.length > 0) {
			clfNames = clfNames.substring(1);
			tip = "确定要删除所选记录（选中记录中有分类节点，如果删除，则会删除分类【" + clfNames + "】节点下所有子节点）？";
		}
		
		if (confirm(tip)) {
			var deleteUrl = that.moduleUrl + "/" + rowIds + "?_method=delete";
			dhtmlxAjax.get(deleteUrl, function(loader) {
				that.reloadGrid();
				that.reloadTreeItem();
			});
		}//*/
		var rowIds = grid.getSelectedRowId();
		if (null == rowIds || "" == rowIds) {
			dhtmlx.message(getMessage("select_record"));
			return;
		}
		dhtmlx.confirm({
			type:"confirm",
			text: getMessage("delete_warning"),
			ok: "确定",
			cancel: "取消",
			callback: function(flag) {
				if (flag) {
					var deleteUrl = that.moduleUrl + "/" + rowIds + ".json?_method=delete";
					dhtmlxAjax.get(deleteUrl, function(loader) {
						var obj = eval("(" + loader.xmlDoc.responseText + ")");
						if (obj.success == false) {
							var id = obj.message;
							if (id == "ERROR") {
								dhtmlx.alert(getMessage("delete_failure"));
								return;
							}
							var name = grid.cells(id, 0).getValue();
							dhtmlx.alert("【" + name + "】有子节点，请先删除子节点！");
							return;
						} else {
							dhtmlx.message(getMessage("delete_success"));
							that.reloadGrid();
							that.reloadTreeItem();
							that.resetForm();
						}
					});
				}else{
					return;
				}
			}
		});
	};
	//
	var typeCombo = grid.getCombo(1);
	typeCombo.put("0","分类");
	typeCombo.put("1","工作流");
	initGridWithoutPageable(grid, cfg, url);
	grid.enableDragAndDrop(true);
	grid.enableTooltips("true,true,true,true,false,false");
	//
	grid.attachEvent("onRowSelect", function(id,ind){
		_this.view(id);
	});
	grid.attachEvent("onDrag",function(sIds,tId) {
		// 判断选中的记录是否连续，如果不连续，则不能调整顺序
		if (sIds.indexOf(",") != -1) {
			var idArr = sIds.split(",");
			var preIdx = null, curIdx = null;
			for (var i = 0; i < idArr.length; i++) {
				curIdx = grid.getRowIndex(idArr[i]);
				if (null == preIdx) {
					preIdx = curIdx;
					continue;
				}
				if (Math.abs(curIdx - preIdx) != 1) {
					dhtmlx.message("请选择连续的记录进行调整顺序！");
					return false;
				}
				preIdx = curIdx;
			}
		}
		return true;
	});
	grid.attachEvent("onDrop", function(sId,tId,dId,sObj,tObj,sCol,tCol){
		if(undefined != tId){
			var adjustUrl = that.moduleUrl + "!adjustShowOrder?P_parentId=" + that.nodeId + "&P_sourceIds=" + sId + "&P_targetId=" + tId;
			dhtmlxAjax.get(adjustUrl, function(loader) {
				that.reloadGrid();
				that.grid.selectRowById(sId);
				that.reloadTreeItem();
			});
		}		
	});
	//
	this.view = function(id) {
		if (id.indexOf(",") > -1) {
			return;
		}
		var updateUrl = that.moduleUrl + "/" + id + ".json";
		var formData = loadJson(updateUrl);
		var form = that.form;
		form.setFormData(formData);
		form.disableItem("type", "0");
		form.disableItem("type", "1");
		form.enableItem("type", formData.type);
		that.hidePPId(formData.type);
		if("0" == formData.type){
			form.setRequired("tableId", false);
			form.setRequired("packageId", false);
			form.setRequired("processId", false);
		}else if("1" == formData.type){
			form.setRequired("tableId",true);
			form.setRequired("packageId", true);
			form.setRequired("processId", true);
		}
	};
}
/**
 * 列表数据加载URL
 */
function getGridUrl(that) {
	return that.moduleUrl + "!search.json?Q_EQ_parentId=" + that.nodeId + "&P_orders=showOrder";
}
/**
 * 工作流配置
 */
function opencfg(id) {
	var tree = _initObj.tree;
	
	tree.selectItem(id, true, "0");
	//_initObj.nodeId = id;
	//initConfigLayout(_initObj);
}
/**
 * 启动或停止工作流
 */
function start(id) {
	var url = _initObj.moduleUrl + "!started?id=" + id;
	dhtmlxAjax.get(url, function(loader) {
		var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
		if (!jsonObj.status) {
			dhtmlx.message("启动失败！");
		} else {
			_initObj.reloadGrid();
		}
	});
}