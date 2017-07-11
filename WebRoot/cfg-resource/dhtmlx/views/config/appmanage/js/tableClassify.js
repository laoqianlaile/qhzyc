
function initClassfyGrid() {
    dataGrid.setImagePath(IMAGE_PATH);
    dataGrid.setHeader(gridData.format.headers.toString());
    dataGrid.setInitWidths(gridData.format.colWidths.toString());
    dataGrid.setColTypes(gridData.format.colTypes.toString());
    dataGrid.setColAlign(gridData.format.colAligns.toString());
    dataGrid.setSkin(Skin);
    dataGrid.init();
    dataGrid.enableMultiselect(true);
    dataGrid.setStyle("font-weight:bold;font-size:12px;", "", "", "");
    dataGrid.attachEvent("onRowDblClicked", function(rId, cInd) {
    });
    dataGrid.enableDragAndDrop(true, true);
	dataGrid.setDragBehavior("complex", true);
    dataGrid.attachEvent("onDrag",function(sId,tId){
		var url = MODEL_URL + "!sort.json?start=" + sId + "&end=" + tId;
		loadJson(url);
		QUERY_URL = AppActionURI.tableDefine + "!search.json?Q_EQ_parentId="+nodeId+"&P_orders=showOrder";
		search();
	});
    if (pageable) {
        pagesize = getCookie("pagesize") || PAGE_SIZE;
        dataGrid.enablePaging(true, pagesize, 1, statusBar);
        dataGrid.setPagingSkin('toolbar', Skin);
    }
}


function initTableClass(cltabbar){
	
	TREE_URL = AppActionURI.tableDefine;
	MODEL_URL=TREE_URL;
	var classify= cltabbar.cells("class$table$01");
	var dhxLayout = new dhtmlXLayoutObject(classify,"2E");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
	var layGrid = dhxLayout.cells("a");
	var layFrom = dhxLayout.cells("b");
	var ctabbar = dhxLayout.cells("b").attachTabbar();
	
	dhxLayout.cells("b").setHeight(200);
	
	layGrid.detachToolbar();
	var toolbar = layGrid.attachToolbar();
	
	toolbar.setIconsPath(IMAGE_PATH);
	toolbar.addButton("add", 1, "新增", "new.gif");
	toolbar.addSeparator("septr$01", 2);
	toolbar.addButton("delete", 6, "删除", "delete.gif");
	toolbar.addSeparator("septr$02", 5);
	if (classification == 'V') { // 视图不可以新增
		toolbar.disableItem("add");
		toolbar.disableItem("delete");
	}
	layGrid.hideHeader();	 
	pageable = false;
	
	dataGrid = layGrid.attachGrid();
	gridData  = {
			format: {
				headers: ["中文名称","英文名称","类型"],
				   cols: ["text","realTableName","type"],
				colWidths: ["200","200","*"],
				colTypes: ["ro","ro","co"],
				colAligns: ["left","left","left"]
			}
		};
	
		var colTypeCombo = dataGrid.getCombo(2);
			colTypeCombo.put("0","分类");
			colTypeCombo.put("1","表");
			    	    
	QUERY_URL = AppActionURI.tableDefine + "!search.json?Q_EQ_parentId="+nodeId+"&P_orders=showOrder";
	initClassfyGrid();
	search();
	
	
	dataGrid.attachEvent("onRowSelect", function(id,ind){
            var selectId =dataGrid.getSelectedRowId();
            if (selectId == undefined) {
            	dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
            	dhtmlx.message(getMessage("select_only_one_record"));
            	return;
            }
            var columnType = dataGrid.cells(selectId,2).getValue();
			if(columnType=="0"){
				ctabbar.setTabActive("tab$classfiy$01");
			}else if(columnType=="1"){
				ctabbar.setTabActive("tab$tables$02");
			}
			GET_BY_ID_URL = MODEL_URL + "/" + selectId + ".json?_method=get";
	    	loadForm(detailForm, GET_BY_ID_URL);
	    	detailForm.setReadonly("tableName", true);
	});
	
	ctabbar.setImagePath(IMAGE_PATH);
    ctabbar.addTab("tab$classfiy$01", "分类", "70px");
    ctabbar.addTab("tab$tables$02", "表", "70px");
    ctabbar.setTabActive("tab$classfiy$01");
    var t=0;
    if(t==0){
		initClassify(ctabbar);
	}
	
	toolbar.attachEvent("onClick", function(id){

		if (id == "add"){
			 initFormData(detailForm, true);
		}else if(id == "delete"){
			var selectId =dataGrid.getSelectedRowId();
            DELETE_URL = MODEL_URL + "/" + selectId + "?_method=delete";
		    dhtmlxAjax.get(DELETE_URL, function(loader) {
		    	QUERY_URL = AppActionURI.tableDefine + "!search.json?Q_EQ_parentId="+nodeId+"&P_orders=showOrder";
                search();
		    });
 		}
	});
	
	ctabbar.attachEvent("onSelect", function(id,last_id){
			t = 1;
			if (id == "tab$classfiy$01") {
	   			initClassify(ctabbar);
		    }else if (id == "tab$tables$02") {
	   			inittable(ctabbar);
		    }
	       	initCombo(detailForm, formFormat);
	       	return true;
   	});

	
function initClassify(zdtabbar){
	MODEL_URL = AppActionURI.tableDefine;
	
	zdtabbar.cells("tab$classfiy$01").detachToolbar();
	var toolbar = zdtabbar.cells("tab$classfiy$01").attachToolbar();
	
	toolbar.setIconsPath(IMAGE_PATH);
	toolbar.addButton("save", 3, "新增", "new.gif");
	toolbar.addText("empty", 4, "    ");
	toolbar.addButton("submit", 1, "保存", "save.gif");
	toolbar.addSeparator("septr$01", 2);
	
	toolbar.attachEvent("onClick", function(id) {
			 if (id == "save") {
				   initFormData(detailForm, true);
			}else if(id == "submit"){
				var cid = detailForm.getItemValue("id");
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
				}else{
					SAVE_URL = MODEL_URL + "/" + cid;
	        		detailForm.setItemValue("_method", "put");
	        	}
				detailForm.send(SAVE_URL, "post", function(loader, response){
					QUERY_URL = AppActionURI.tableDefine + "!search.json?Q_EQ_parentId="+nodeId+"&P_orders=showOrder";
                    search();
    			});
			}
		});
	
	detailFormData = {
			format: [
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "classification"},
				{type: "hidden", name: "created",value:"0"},
				{type: "block", width:320, list:[
					{type: "input", label: "分类名称", name: "text",required:true,width:160,maxLength:24},
					{type: "hidden", name: "parentId"},
					{type: "hidden", name: "type",value:"0"},
					{type: "input", label: "显示顺序", name: "showOrder",required:true,validate:"ValidNumeric",width:160,maxLength:4}
				]},	

	        ],
			settings: {labelWidth: 80, inputWidth: 100}
		};
	detailForm = zdtabbar.cells("tab$classfiy$01").attachForm(initFormFormat(detailFormData));
}


function inittable(zdtabbar){
	MODEL_URL = AppActionURI.tableDefine;
	
	var PJ_From = zdtabbar.cells("tab$tables$02").attachForm();
	zdtabbar.cells("tab$tables$02").detachToolbar();
	var toolbar = zdtabbar.cells("tab$tables$02").attachToolbar();
	
	toolbar.setIconsPath(IMAGE_PATH);
	toolbar.addButton("reset", 3, "新增", "new.gif");
	toolbar.addText("empty", 4, "    ");
	toolbar.addButton("submit", 1, "保存", "save.gif");
	toolbar.addSeparator("septr$01", 2);

	
	toolbar.attachEvent("onClick", function(id) {
			 if (id == "reset") {
				   initFormData(detailForm, true);
			}else if(id == "submit"){
				var cid = detailForm.getItemValue("id");
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
				}else{
					SAVE_URL = MODEL_URL + "/" + cid;
	        		detailForm.setItemValue("_method", "put");
	        	}
				detailForm.send(SAVE_URL, "post", function(loader, response){
					QUERY_URL = AppActionURI.tableDefine + "!search.json?Q_EQ_parentId="+nodeId+"&P_orders=showOrder";
                    search();
    			});
			}
		});
	
	detailFormData = {
		format: [
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "classification"},
					{type: "hidden", name: "created",value:"0"},
					{type: "block", width:320, list:[
						{type: "input", label: "显示名称：", name: "text",required:true,width:160,maxLength:24},
						{type: "hidden", name: "parentId"},
						{type: "hidden", name: "type",value:"1"},
						{type: "input", label: "表名称：           ", name: "tableName",required:true,width:160,maxLength:20},
						{type: "input", label: "显示顺序：", name: "showOrder",required:true,validate:"ValidNumeric",width:160,maxLength:4}
					]},
				],
				settings: {labelWidth: 80, inputWidth: 160}
		};
	detailForm = zdtabbar.cells("tab$tables$02").attachForm(initFormFormat(detailFormData));
}

}
