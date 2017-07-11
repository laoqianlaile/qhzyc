
var G_URL;var cRelation;
var CRGrid;var grid;
var columnNodeId,uId;
var columns_data; var f_combo_data;	var zdtabbar;
var widthDiffer = 2;
//if (_isIE) {	widthDiffer = 0;}
if (_isIE && navigator.appVersion.indexOf("MSIE 8.0") != -1) {
	 
} else if (_isIE && navigator.appVersion.indexOf("MSIE 9.0") != -1) {
	widthDiffer = 0;
} else if (_isIE && navigator.appVersion.indexOf("MSIE 10.0") != -1) {
	
}

//初始化字段关系页面
function loadColumnRelation(tabbar,treeName) {
	var zdgl = tabbar.cells("tab$table$03");
	var dhxLayout = new dhtmlXLayoutObject(zdgl,"2E");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var layoutGrid = dhxLayout.cells("a");
	var layoutFrom = dhxLayout.cells("b");
	zdtabbar = dhxLayout.cells("b").attachTabbar();
	dhxLayout.cells("b").setHeight(240);
	
	layoutGrid.hideHeader();
	CRGrid = dhxLayout.cells("a").attachGrid();
	dhxLayout.cells("a").detachToolbar();
	var toolbar = dhxLayout.cells("a").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolbar.addButton("add", 1, "新增", "new.gif");
	toolbar.addButton("delete", 3, "删除", "delete.gif");
	toolbar.addButton("copy", 4, "复制", "copy.gif");
	
	grid  = {
			format: {
				headers: ["名称","字段定义类型"],
				userdata: [2],
				colWidths: ["260","*"],
				colTypes: ["ro","co"],
				colAligns: ["left","left"]
			}
		};
	var colTypeCombo = CRGrid.getCombo(1);
			colTypeCombo.put("0","字段拼接");
			colTypeCombo.put("1","字段截取");
			colTypeCombo.put("2","字段继承");
			colTypeCombo.put("3","字段求和");
			colTypeCombo.put("4","字段最值计算");
			colTypeCombo.put("5","字段特殊业务");
			    
	G_URL = AppActionURI.columnRelation + "!getAllColumnRelationList.json?E_model_name=datagrid&P_orders=showOrder&tableId="+nodeId;
	CRGrid.enableTooltips("true,true,false");
	CRGrid.enableDragAndDrop(true);
	initGridWithoutColumnsAndPageable(CRGrid, grid, G_URL);
	
	/** 列表拖拽调整顺序*/
    CRGrid.attachEvent("onDrag",function(sId,tId) {
		if (sId.indexOf(",") != -1) {
			dhtmlx.message(getMessage("drag_one_record"));
			return false;
		}
		return true;
	});
    
	CRGrid.attachEvent("onDrop", function(sId, tId) {
    	if(undefined != tId){
			var sortUrl = AppActionURI.columnRelation + "!sortShowOrderRelation?start=" + sId + "&end=" + tId+"&tableId="+nodeId;
			dhtmlxAjax.get(sortUrl, function(loader) {
			});
		}
    });
	
	initCombo4F();
	//选择记录表单布局显示相应的tab页
	CRGrid.attachEvent("onRowSelect", function(id,ind){
            var selectId =CRGrid.getSelectedRowId();
            cRelation = selectId;
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
            	dhtmlx.message(getMessage("select_only_one_record"));
            	return;
            }
            var columnId = CRGrid.getUserData(selectId,"userdata_0");
			if(columnId=="0"){
				zdtabbar.setTabActive("tab$column$01");
			}else if(columnId=="1"){
				zdtabbar.setTabActive("tab$column$02");
			}else if(columnId=="2"){
				zdtabbar.setTabActive("tab$column$03");
				detailForm.setItemValue("mTable",treeName);
				detailForm.setItemValue("tableId",nodeId);
			}else if(columnId=="3"){
				zdtabbar.setTabActive("tab$column$04");
				detailForm.setItemValue("mTable",treeName);
			}else if(columnId=="4"){
				zdtabbar.setTabActive("tab$column$05");
				detailForm.setItemValue("mTable",treeName);
			}else if(columnId=="5"){
				zdtabbar.setTabActive("tab$column$06");
			}
            GET_BY_ID_URL = MODEL_URL + "/" + selectId + ".json";
	    	var formData = loadJson(GET_BY_ID_URL);
            
            if(columnId=="0"){
				var  columnNum = formData.columnNum;
				columnNumberChange(detailForm, columnNum);
			}
            detailForm.setFormData(formData);
	});

	toolbar.attachEvent("onClick", function(id){
		if (id == "add") {
            initFormData(detailForm, true);
            t_Change();
            detailForm.setItemValue("mTable",treeName);
        }else if (id == "delete") {
            var selectId = CRGrid.getSelectedRowId();
            if (selectId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") > 0) {
            	dhtmlx.message(getMessage("select_only_one_record"));
            	return;
            }
    		dhtmlx.confirm({
				type:"confirm",
				text: getMessage("delete_warning"),
				ok  : "确定",
				cancel: "取消",
				callback: function(flag) {
					if (flag) {
			            var url = AppActionURI.columnRelation + "!destroy.json?id="+selectId;
						dhtmlxAjax.get(url, function(loader){
							var rlt = eval("(" + loader.xmlDoc.responseText + ")");
							if (rlt.success) {
								initGridAgain(CRGrid, grid, G_URL);
								detailForm.clear();
								detailForm.setItemValue("tableId", nodeId);
								if (rlt.message == "OK") {
									dhtmlx.message(getMessage("delete_success"));
								} else {
									dhtmlx.message(getMessage("delete_success") + "但触发器重新编译出问题！");
								}
							} else {
								dhtmlx.message(getMessage("delete_failure"));
							}
						});
					}
				}
			});
        }else if(id == "copy"){
        	var rowId = CRGrid.getSelectedRowId();
            if (rowId == undefined) {
                dhtmlx.message(getMessage("select_record"));
                return;
            }else if (rowId.indexOf(",") != -1) {
            	dhtmlx.message(getMessage("select_only_one_record"));
            	return;
            }
           var type = CRGrid.getUserData(rowId,"userdata_0");
           var url = AppActionURI.columnRelation + "!saveCopyColumn.json?rId="+rowId+"&type="+type;
			dhtmlxAjax.get(url, function(loader){
				var rlt = eval("(" + loader.xmlDoc.responseText + ")");
				if (rlt.success) {
					dhtmlx.message("复制成功!");
					initGridAgain(CRGrid, grid, G_URL);
				}else{
					dhtmlx.message("复制失败!");
				}
			});
        }
	});
	pageable = true;
    zdtabbar.setImagePath(IMAGE_PATH);
    zdtabbar.addTab("tab$column$01", "字段拼接", "70px");
    zdtabbar.addTab("tab$column$02", "字段截取", "70px");
    zdtabbar.addTab("tab$column$03", "继承", "50px");
    zdtabbar.addTab("tab$column$04", "求和", "50px");
    zdtabbar.addTab("tab$column$05", "最值计算", "70px");
    zdtabbar.addTab("tab$column$06", "特殊业务", "70px");
    zdtabbar.setTabActive("tab$column$01");
    w=+3;
	if(w==3){
    	initZDPJ(zdtabbar);
    	 t_Change();
    }
	zdtabbar.attachEvent("onSelect", function(id,last_id){
			 if (id == "tab$column$01") {
	   			initZDPJ(zdtabbar);
	   			 t_Change();
		    }else if (id == "tab$column$02") {
	   			initZDJQ(zdtabbar);
		    }else if (id == "tab$column$03") {
	       		initJC(zdtabbar,treeName);
			}else if (id == "tab$column$04") {
	       		initQH(zdtabbar,treeName);
			}else if (id == "tab$column$05") {
	       		initZZJS(zdtabbar,treeName);
			}else if (id == "tab$column$06") {
	       		initTSYW(zdtabbar);
			}
	       	return true;
   	});
}


//初始化父表和子表字段下拉框的值
function initCombo4F(){
	var url_ = AppActionURI.columnDefine + "!search.json?F_in=showName,id&Q_EQ_tableId=" + nodeId;
	var datas = loadJson(url_).data;
	columns_data = new Array({value:"",text:"请选择"});
	for (var i = 0; i < datas.length; i++) {
		columns_data.push({value: datas[i].id, text: datas[i].showName});
	}
	url_ = AppActionURI.tableRelation + "!queryRelationTableByTableID.json?tableId=" + nodeId;
	f_combo_data = loadJson(url_);
}

//Tab页切换(物理表、表定义、字段关联)后,有父表、子表关系的Tab页要重新加载(继承、求和、最值)
function initTab4TabChange(){
	var id = zdtabbar.getActiveTab();
	if (id == "tab$column$01") {
	   			initZDPJ(zdtabbar);
	   			 t_Change();
		    }else if (id == "tab$column$02") {
	   			initZDJQ(zdtabbar);
		    }else if (id == "tab$column$03") {
	       		initJC(zdtabbar,treeName);
			}else if (id == "tab$column$04") {
	       		initQH(zdtabbar,treeName);
			}else if (id == "tab$column$05") {
	       		initZZJS(zdtabbar,treeName);
			}else if (id == "tab$column$06") {
	       		initTSYW(zdtabbar);
			}
	       	return true;
}


//combo设置选项
function setComboValue(combo,url){
	combo.clearAll(true);
	var datas = loadJson(url).data;
	var od = new Array({value:"",text:"请选择"});
	for (var i = 0; i < datas.length; i++) {
		od.push({value: datas[i].id, text: datas[i].showName});
	}
	combo.addOption(od);
	combo.selectOption(0);
}

//父表字段下拉框和子表字段下拉框过滤掉两表关联条件
function filterCombo(url,f_f_combo,c_f_combo){
	var data = loadJson(url);
	var child_id = data.child_cols;
	var parent_id = data.parent_cols;
	for(var i=0;i<child_id.length;i++){
		if(c_f_combo.getOption(child_id[i])){
			c_f_combo.deleteOption(child_id[i]);
		}
	}
	for(var i=0;i<parent_id.length;i++){
		if(f_f_combo.getOption(parent_id[i])){
			f_f_combo.deleteOption(parent_id[i]);
		}
	}
}
   
	function unlockFormToolBar() {
    	toolbarBottom.enableItem("submit");
    	toolbarBottom.enableItem("reset");
    }
    
   function lockFormToolBar() {
    	toolbarBottom.enableItem("submit");
    	toolbarBottom.enableItem("reset");
    }
  
   function t_Change() {
		detailForm.attachEvent("onChange", function (id, value){
			if("originTableId" == id){
				columnNodeId = detailForm.getItemValue("originTableId");
				var OCICombo = detailForm.getCombo("originColumnId");
				OCICombo.clearAll();
				var url = AppActionURI.columnDefine + "!search.json?F_in=showName,id&Q_EQ_tableId="+columnNodeId;
				var jsonObj = loadJson(url);
				if(jsonObj && jsonObj.data && jsonObj.data.length) {
					var opt_data = [];
					for (var m=0; m<jsonObj.data.length; m++) {
						opt_data[m] = {text: jsonObj.data[m].showName, value: jsonObj.data[m].id};
					};
					OCICombo.addOption(opt_data);
				}
			}else if ("columnNum" == id) {
				columnNumberChange(detailForm, value);
			}
		});
}

function initGridAgain(grid, gridcfg, url) {
	grid.setImagePath(IMAGE_PATH);
	grid.setHeader(gridcfg.format.headers.toString());
	grid.setInitWidths(gridcfg.format.colWidths.toString());
	grid.setColTypes(gridcfg.format.colTypes.toString());
	grid.setColAlign(gridcfg.format.colAligns.toString());
	grid.setSkin(Skin);
	grid.enableMultiselect(true);
	grid.setStyle("font-weight:bold;", "", "", "");
	loadGridData(grid, gridcfg, url);
}
