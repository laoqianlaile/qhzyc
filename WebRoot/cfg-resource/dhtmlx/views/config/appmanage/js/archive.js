
function initArchiveTreeList(layoutB,parId) {
	var snum = 0;
	var dhxLayout = new dhtmlXLayoutObject(layoutB,"2E");
	dhxLayout.cont.obj._offsetTop = 1;
	dhxLayout.cont.obj._offsetLeft = 1;
	dhxLayout.cont.obj._offsetHeight = -2;
	dhxLayout.cont.obj._offsetWidth = -2;
	dhxLayout.setSizes();
	
    dhxLayout.cells("b").hideHeader();
	var layoutGrid = dhxLayout.cells("a");
   
	dhxLayout.cells("a").setHeight(365);
	statusBar = dhxLayout.cells("a").attachStatusBar();
	layoutGrid.hideHeader();
	
	dataGrid = dhxLayout.cells("a").attachGrid();
	dhxLayout.cells("a").detachToolbar();
	var toolbar = dhxLayout.cells("a").attachToolbar();
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	toolbar.addButton("add", 1, "新增", "new.gif");
	toolbar.addButton("delete", 3, "删除", "delete.gif");
	gridData  = {
			format: {
				headers: ["节点名称","节点类型"],
				   cols: ["name","type"],
				colWidths: ["300","*"],
				colTypes: ["ro","co"],
				colAligns: ["left","left"]
			}
		};
	
	var colTypeCombo = dataGrid.getCombo(1);
		colTypeCombo.put("0","档案树");
		colTypeCombo.put("1","空节点");
		colTypeCombo.put("2","表节点");
		colTypeCombo.put("3","字段节点");
		
		
	QUERY_URL = contextPath + "/appmanage/tree-define!search.json";
	if(parId == undefined){
		parId = "-1";
		var Q_params = "Q_EQ_parentId=-1&P_orders=id";
	}else{
		var Q_params = "Q_EQ_parentId="+parId+"&P_orders=id";
	}
	
	COLUMN_MODEL_URL = contextPath + "/appmanage/tree-define";
	MODEL_URL = COLUMN_MODEL_URL;
	initGrid();
    if (pageable) {
	    pagesize = getCookie("pagesize") || PAGE_SIZE;
	    dataGrid.enablePaging(true, pagesize, 1, statusBar);
	    dataGrid.setPagingSkin('toolbar', Skin);
	}
	search(Q_params);

	var rId;
	dataGrid.attachEvent("onRowSelect",function(id,ind){
		rId = id;
	});
	
	toolbar.attachEvent("onClick", function(id){
		if (id == "add") {
			if(parId == "-1"){
				dhtmlx.alert("请选中档案树下的节点再进行新增操作!");
			}else{
				detailForm.enableItem("type","1");
				detailForm.enableItem("type","2");
				detailForm.enableItem("type","3");
				initFormData(detailForm, true);
	            initCombo(detailForm, formFormat);
            }
        } else if (id == "delete") {
        	if(parId == "-1"){
        		dhtmlx.alert("档案树删除直接右击档案树操作!");
			} else{
	            var selectId = dataGrid.getSelectedRowId();
	            if (selectId == undefined) {
	            	dhtmlx.message(getMessage("select_record"));
	                return;
	            } else {
	            	dhtmlx.confirm({
	    				type:"confirm",
	    				text: getMessage("delete_warning"),
	    				ok: "确定",
	    				cancel: "取消",
	    				callback: function(flag) {
	    					if (flag) {
	    						deleteById(selectId);
	    		            	if(parId == undefined){
	    							var Q_params = "Q_EQ_parentId=-1&P_orders=id";
	    						}else{
	    							var Q_params = "Q_EQ_parentId="+parId+"&P_orders=id";
	    						}
	    		                search(Q_params);
	    					}
	    				}
	    			});
	            }
            }
        }
	});
	
	dataGrid.attachEvent("onRowSelect", function(id,ind){
		
        	initCombo(detailForm, formFormat);
            var selectId =dataGrid.getSelectedRowId();
            if (selectId == undefined) {
            	dhtmlx.message(getMessage("select_record"));
                return;
            }
            if (selectId.indexOf(",") != -1) {
            	dhtmlx.message(getMessage("select_only_one_record"));
            	return;
            }
             var type = dataGrid.cells(rId,1).getValue();
             
            if(type != "0"){
				if(type=="1"){
					ininNull();
					detailForm.checkItem("type","1");
					detailForm.disableItem("type","2");
					detailForm.disableItem("type","3");
					detailForm.enableItem("type","1");
				}else if(type=="2"){
					inintable();
					detailForm.checkItem("type","2");
					detailForm.disableItem("type","1");
					detailForm.disableItem("type","3");
					detailForm.enableItem("type","2");
				}else if(type=="3"){
					ininColumn();
					detailForm.checkItem("type","3");
					detailForm.disableItem("type","1");
					detailForm.disableItem("type","2");
					detailForm.enableItem("type","3");
				}
				GET_BY_ID_URL = MODEL_URL + "/" + selectId + ".json?_method=get";
		    	loadForm(detailForm, GET_BY_ID_URL);
		    	initCombo(detailForm, formFormat);
	            
	            var sector = detailForm.getCheckedValue("sector1");
	            
	            if(sector == "1"){
	            	detailForm.isItemChecked("sector1","2");
	            }else{
	            	detailForm.checkItem("sector1","2");
	            }
	            var sector2 = detailForm.getItemValue("sector2");
	            if(sector == "1"){
	            	detailForm.checkItem("sector2","1");
	            }else{
	            	detailForm.checkItem("sector2","2");
	            }
             }
	});
	
	var tlbarBottom = dhxLayout.cells("b").attachToolbar();
	tlbarBottom.setIconsPath(TOOLBAR_IMAGE_PATH);
	tlbarBottom.addButton("mews", 3, "新增", "new.gif");
	tlbarBottom.addText("empty", 4, "    ");
	tlbarBottom.addButton("submit", 1, "保存", "save.gif");
	tlbarBottom.addSeparator("septr$01", 2);

	tlbarBottom.attachEvent("onClick", function(id) {
			if (id == "mews") {
				if(parId == "-1"){
					dhtmlx.alert("请选中档案树下的节点再进行新增操作!");
				}else{
					detailForm.enableItem("type","1");
					detailForm.enableItem("type","2");
					detailForm.enableItem("type","3");
					initFormData(detailForm, true);
	           	 	initCombo(detailForm, formFormat);
           		 }
			}else if(id == "submit"){
				var sector2 = detailForm.getItemValue("sector2");
				var cid = detailForm.getItemValue("id");
				if(cid == ""){
					SAVE_URL = MODEL_URL;
		        	detailForm.setItemValue("_method", "post");
				}else{
					SAVE_URL = MODEL_URL + "/" + cid;
	        		detailForm.setItemValue("_method", "put");
	        	}
				detailForm.send(SAVE_URL, "post", function(loader, response){
	        		if(parId == undefined){
						var Q_params = "Q_EQ_parentId=-1&P_orders=id";
					}else{
						var Q_params = "Q_EQ_parentId="+parId+"&P_orders=id";
					}
	                search(Q_params);
        		});
			}
		});
	
	
	detailForm = dhxLayout.cells("b").attachForm();
	if(snum == 0){
		ininNull();
	}

	
	function ininNull(){
		nulldetailFormData = {
				format: [
					{type: "hidden", name: "_method"},
					{type: "hidden", name: "id"},
					{type: "hidden", name: "parentId",value:parId, maxLength:50},
					{type: "fieldset", label:"节点类型",name: "typedata",   width: "900", list:[
						{type: "radio", label: "空节点", name: "type",value:"1",checked:"true", labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
						{type:"newcolumn"},
						{type: "radio", label: "表节点", name: "type",value:"2",Readonly:true, labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
						{type:"newcolumn"},
						{type: "radio", label: "字段节点",name: "type",value:"3", labelWidth:"60",position:"label-right",offsetLeft:"20",labelAlign:"left"}
					]},
					{type: "fieldset", label:"节点类型",name: "nullNode",  width: "900", list:[
						{type: "input", label: "节点名称:", name: "name", maxLength:50,required: true}
					]},
					
		        ],
				settings: {labelWidth: 80, inputWidth: 150}
			};
		detailForm = dhxLayout.cells("b").attachForm(initFormFormat(nulldetailFormData));
		detailForm.attachEvent("onChange", function (id, value){
		if ("type" == id) {
			if ("1" == value) {
				ininNull();
			}else if ("2" == value) {
				inintable();
			}else if ("3" == value) {
				ininColumn();
				initCombo(detailForm, formFormat);
			}
		}
	});
		initCombo(detailForm, formFormat);
	}
	
	function inintable(){
		taabledetailFormData = {
			format: [
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "parentId",value:parId, maxLength:50},
				{type: "fieldset", label:"节点类型",name: "typedata",   width: "900", list:[
					{type: "radio", label: "空节点", name: "type",value:1, labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
					{type:"newcolumn"},
					{type: "radio", label: "表节点", name: "type",value:2,Readonly:true, checked:"true",labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
					{type:"newcolumn"},
					{type: "radio", label: "字段节点",name: "type",value:3, labelWidth:"60",position:"label-right",offsetLeft:"20",labelAlign:"left"}
				]},
				{type: "fieldset", label:"节点类型",name: "tableNode",  width: "900", list:[
					{type: "block", width: "300", list:[
						{type: "input", label: "节点名称:", name: "name", maxLength:50, required: true},
						{type: "combo", label: "选择表:", name: "dbId", maxLength:25,  readonly: true, 
							url: contextPath +"/appmanage/table-define!tree.json?F_in=id,name", _text_attrName: "name", _value_attrName: "id",
							showAll: true},
					]},
					{type:"newcolumn"},
					{type: "block", width: "400", list:[
							{type: "block", width: "400", list:[
								{type: "combo", label: "选择布局:　",  name: "layoutContent",readonly:"true",
									options:[
										 {value: "0", text: "选择布局",selected:"1"},
										{value: "1", text: "上下结构"},
									    {value: "2", text: "左右结构"},
									    {value: "3", text: "整张页面"}
								]},
							]},
					]},
					{type: "block", width: "400", list:[
							{type: "label", label: "区域1:", offsetLeft:"30", labelWidth:"60"},
							{type:"newcolumn"},
							{type: "radio", label: "查询界面", name: "sector1",value:"1", labelWidth:"60",checked:"true",position:"label-right",offsetLeft:"-5",labelAlign:"left",},
							{type:"newcolumn"},
							{type: "radio", label: "列表界面", name: "sector1",value:"2", labelWidth:"60",position:"label-right",offsetLeft:"0",labelAlign:"left",},
					]},
					{type: "block", width: "400", list:[
						{type: "combo", label: "关联表： ", name: "originTableId", maxLength:25,  readonly: true, offsetLeft:"20",
							url: contextPath +"/appmanage/table-define!tree.json?F_in=id,name", _text_attrName: "name", _value_attrName: "id",
							showAll: true},
					]},
					{type: "block", width: "400", list:[
							{type: "label", label: "区域2:", offsetLeft:"30", labelWidth:"60"},
							{type:"newcolumn"},
							{type: "radio", label: "查询界面", name: "sector2",value:"1", labelWidth:"60",checked:"true",position:"label-right",offsetLeft:"-5",labelAlign:"left",},
							{type:"newcolumn"},
							{type: "radio", label: "列表界面", name: "sector2",value:"2", labelWidth:"60",position:"label-right",offsetLeft:"0",labelAlign:"left",},
					]},
					
				]},
				
	        ],
			settings: {labelWidth: 80, inputWidth: 150}
		};
	detailForm = dhxLayout.cells("b").attachForm(initFormFormat(taabledetailFormData));
	
		detailForm.attachEvent("onChange", function (id, value){
		if ("type" == id) {
			if ("1" == value) {
				ininNull();
			}else if ("2" == value) {
				inintable();
			}else if ("3" == value) {
				ininColumn();
				initCombo(detailForm, formFormat);
			}
		}
	});
	initCombo(detailForm, formFormat);
	}
	
	function ininColumn(){
		
		columndetailFormData = {
			format: [
				{type: "hidden", name: "_method"},
				{type: "hidden", name: "id"},
				{type: "hidden", name: "parentId",value:parId, maxLength:50},
				{type: "fieldset", label:"节点类型",name: "typedata",   width: "900", list:[
					{type: "radio", label: "空节点", name: "type",value:"1", labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
					{type:"newcolumn"},
					{type: "radio", label: "表节点", name: "type",value:"2",Readonly:true, labelWidth:"50",position:"label-right",offsetLeft:"20",labelAlign:"left"},
					{type:"newcolumn"},
					{type: "radio", label: "字段节点",name: "type",value:"3", labelWidth:"60",checked:"true",position:"label-right",offsetLeft:"20",labelAlign:"left"},
			
				]},
				{type: "fieldset", label:"节点类型",name: "columnNode",  width: "900", list:[
					{type: "block", width: "300", list:[
						{type: "combo", label: "选择字段:", name: "dbId", maxLength:25, required: true, readonly: true, 
							url: contextPath +"/appmanage/tree-define!tree.json?F_in=id,name&Q_EQ_parentId="+parId, _text_attrName: "name", _value_attrName: "id",
							showAll: true},
					]},
					{type: "block", width: "300", list:[
							{type: "block", width: "300", list:[
								{type: "input", label: "节点名称:", name: "name", maxLength:50, required: true},
								{type: "input", label: "节点值:", name: "value", maxLength:50, required: true},
							]},
					]},
					{type:"newcolumn"},
					{type: "block", width: "400", list:[
							{type: "block", width: "400", list:[
								{type: "combo", label: "选择布局:　",  name: "layoutContent",readonly:"true",
									 options:[
										{value: "0", text: "选择布局",selected:"1"},
										{value: "1", text: "上下结构"},
									    {value: "2", text: "左右结构"},
									    {value: "3", text: "整张页面"}
								]},
							]},
					]},
					{type: "block", width: "400", list:[
							{type: "label", label: "区域1:", offsetLeft:"30", labelWidth:"60"},
							{type:"newcolumn"},
							{type: "radio", label: "查询界面", name: "sector1",value:"1", labelWidth:"60",checked:"true",position:"label-right",offsetLeft:"-5",labelAlign:"left",},
							{type:"newcolumn"},
							{type: "radio", label: "列表界面", name: "sector1",value:"2", labelWidth:"60",position:"label-right",offsetLeft:"0",labelAlign:"left",},
					]},
					{type: "block", width: "400", list:[
						{type: "combo", label: "关联表： ", name: "originTableId", maxLength:25,  readonly: true, offsetLeft:"20",
							url: contextPath +"/appmanage/table-define!tree.json?F_in=id,name", _text_attrName: "name", _value_attrName: "id",
							showAll: true},
					]},
					{type: "block", width: "400", list:[
							{type: "label", label: "区域2:", offsetLeft:"30", labelWidth:"60"},
							{type:"newcolumn"},
							{type: "radio", label: "查询界面", name: "sector2",value:"1", labelWidth:"60",checked:"true",position:"label-right",offsetLeft:"-5",labelAlign:"left"},
							{type:"newcolumn"},
							{type: "radio", label: "列表界面", name: "sector2",value:"2", labelWidth:"60",position:"label-right",offsetLeft:"0",labelAlign:"left"}
					]},
				]},
	        ],
			settings: {labelWidth: 80, inputWidth: 150}
		};
	detailForm = dhxLayout.cells("b").attachForm(initFormFormat(columndetailFormData));
	detailForm.attachEvent("onChange", function (id, value){
		if ("type" == id) {
			if ("1" == value) {
				ininNull();
			}else if ("2" == value) {
				inintable();
			}else if ("3" == value) {
				ininColumn();
				initCombo(detailForm, formFormat);
			}
		}
	});
	initCombo(detailForm, formFormat);
	}	
				

	detailForm.attachEvent("onChange", function (id, value){
		if ("type" == id) {
			if ("1" == value) {
				ininNull();
			}else if ("2" == value) {
				inintable();
			}else if ("3" == value) {
				ininColumn();
				initCombo(detailForm, formFormat);
			}
		}
	});
	
	
}





