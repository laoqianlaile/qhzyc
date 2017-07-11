
var myDate = new Date();
var currentTime = myDate.getHours()+":"+myDate.getMinutes();
function initTimeDetil(tabbar){
	var timing = tabbar.cells("timing$02");
	var dhxLayout = tabbar.cells("timing$02").attachLayout("2U");
	initLayout(dhxLayout);	
	var layGrid = dhxLayout.cells("a");
	var layFrom = dhxLayout.cells("b");	
	pageable = false;
	dataGrid = layGrid.attachGrid();	
	dhxLayout.cells("b").setWidth(500);
	dhxLayout.cells("b").setText("详细信息");
	layGrid.detachToolbar();
	var toolbar = layGrid.attachToolbar();	
	toolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	//toolbar.addButton("add", 1, "新增", "new.gif");
	//toolbar.addSeparator("septr$01", 2);
	toolbar.addButton("delete", 6, "删除", "delete.gif");
	layGrid.hideHeader();		
	MODEL_URL = contextPath+"/appmanage/timing";
	gridData  = {
			format: {
				headers: ["<center>任务名称</center>","<center>定时时间</center>","<center>方式</center>","<center>操作</center>","<center>时间类型</center>","<center>运行明细</center>"],
				   cols: ["name","time","type","operatesArea","timingType","taskDetailArea"],
				colWidths: ["200","180","150","100","150","200","*"],
				colTypes: ["ro","ro","co","link","co","link"],
				colAligns: ["left","left","left","left","left","left"]
			}
		};

	var colTypeCombo = dataGrid.getCombo(2);
	colTypeCombo.put("0","JAVA");
	colTypeCombo.put("1","批处理");
	
	var colTypeCombo_ = dataGrid.getCombo(4);
	colTypeCombo_.put("0","定时任务");
	colTypeCombo_.put("1","间隔任务");
	//隐藏列
	//dataGrid.setColumnHidden(4,true);
	
	QUERY_URL = contextPath+"/appmanage/timing!search.json?P_orders=id";
	initGrid();
	dataGrid.enableTooltips("true,true,true,false,true,false");
	search();
	
	dataGrid.attachEvent("onRowSelect", function(id,ind){
		var type = dataGrid.cells(id, 2).getValue();
			if("0"==type){
				detailForm.hideItem("arar2");
				detailForm.showItem("arar1");
			}else if("1"==type){
				detailForm.hideItem("arar1");
				detailForm.showItem("arar2");
			}
		var timingType = dataGrid.cells(id, 4).getValue();
			if("0"==timingType){
				detailForm.hideItem("timeArar2");
				detailForm.showItem("timeArar1");
			}else if("1"==timingType){
				detailForm.hideItem("timeArar1");
				detailForm.showItem("timeArar2");
			}
		
		GET_BY_ID_URL = MODEL_URL + "/" + id + ".json";
    	var formData = loadJson(GET_BY_ID_URL);
    	//定时时间输入时把时间拆分 填充到相应的控件
    	var ty = formData.timingType;
    	if("1"==ty){
			var tm = formData.time;
			var rowArray = tm.split(":");
	    	for (var i = 0; i < rowArray.length; i++) {
	    		var hous  = rowArray[0];
	    		var minutes = rowArray[1];
	    		
			    detailForm.setItemValue("hours", hous);
			    detailForm.setItemValue("minutes", minutes);	
	    	}
		}
		detailForm.setFormData(formData);
	});
	
	toolbar.attachEvent("onClick", function(id){
		if (id == "add") {
			initFormData(detailForm, true);
        }else if (id == "delete") {
            var rId = dataGrid.getSelectedRowId();
            if (rId == undefined) {
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
    					DELETE_URL = contextPath + "/appmanage/timing/" + rId + ".json?_method=delete";
    				    dhtmlxAjax.get(DELETE_URL, function(loader) {
    				    	var rlt = eval("(" + loader.xmlDoc.responseText + ")");
    				    	if(rlt.success){
    				    		dhtmlx.message(getMessage("delete_success"));
    				    	}else{
    				    		dhtmlx.message(getMessage("delete_failure"));
    				    	}
    	                    search();
    					});
    				}
    			}
    		});
        }
	});

	layFrom.detachToolbar();
	var btoolbar = layFrom.attachToolbar();	
	btoolbar.setIconsPath(TOOLBAR_IMAGE_PATH);
	btoolbar.addButton("reset", 3, "新增", "new.gif");
	btoolbar.addText("empty", 4, "    ");
	btoolbar.addButton("submit", 1, "保存", "save.gif");
	btoolbar.addSeparator("septr$01", 2);
		
	btoolbar.attachEvent("onClick", function(id) {
		if (id == "reset") {
			 initFormData(detailForm, true);
		}else if(id == "submit"){
			var cid = detailForm.getItemValue("id");
			var timingType = detailForm.getItemValue("timingType");
			var time;
			detailForm.setItemValue("method", detailForm.getItemValue("method").trim());
			detailForm.setItemValue("beanId", detailForm.getItemValue("beanId").trim());
			if (timingType == "0") {
				time = detailForm.getItemValue("time");
				if(time == null){
					time = "00:00";
				}
				detailForm.setItemValue("time", time);
			} else if (timingType == "1") {
				var hh = detailForm.getItemValue("hours");
				var mm = detailForm.getItemValue("minutes");
				if(hh==""){
					hh="00";
				}if(mm==""){
					mm="00";
				}
				var ts = hh+":"+mm;
				
				detailForm.setItemValue("time", ts);
			}
//			if(time == null){
//				var hh = detailForm.getItemValue("hours");
//				var mm = detailForm.getItemValue("minutes");
//				if(hh==""){
//					hh="00";
//				}if(mm==""){
//					mm="00";
//				}
//				var ts = hh+":"+mm;
//				
//				detailForm.setItemValue("time", ts);
//			}
			if(cid == ""){
			SAVE_URL = MODEL_URL;
			detailForm.setItemValue("_method", "post");
			}else{
				SAVE_URL = MODEL_URL + "/" + cid;
			detailForm.setItemValue("_method", "put");
			}
			detailForm.send(SAVE_URL + ".json", "post", function(loader, response){
				var formData = eval("(" + loader.xmlDoc.responseText + ")");
				if (false == formData.status) {
					dhtmlx.message(getMessage("save_failure"));
					return;
				}
				dhtmlx.message(getMessage("save_success"));
			    search();
			});
		}
	});
		
	detailFormData = {
		format: [
			{type: "hidden", name: "_method"},
			{type: "hidden", name: "id"},
			{type: "hidden", name: "isOperates",value:"0"},
			{type: "block", width: "430", list:[			
				{type: "block", width: "420", list:[
					{type: "block", width: "390", list:[
						{type: "input", label: "名称:", name: "name", maxLength:50, required: true},
					]},
					{type: "block", width: "430", list:[
						{type: "radio", label: "定时:", name: "timingType",checked: true,value:"0",width:80},
						{type:"newcolumn"},
						{type: "radio", label: "间隔:", name: "timingType",value:"1",inputWidth: 20},
					]},		
					{type: "block", width: "400",name:"timeArar2", list:[
 						{type: "input", label: "时:", name: "hours", maxLength:50, inputWidth: 80},
 						{type:"newcolumn"},
 						{type: "input", label: "分:", name: "minutes", maxLength:50, inputWidth: 80},
 					]},
					{type: "block", width: "400",name:"timeArar1", list:[
 						{type: "calendar", label: "定时时间:", name: "time",dateFormat: "%H:%i", maxLength:50},
 					]},
					{type: "block", width: "390", list:[
						{type: "radio", label: "java:", name: "type",value:"0",width:80},
						{type:"newcolumn"},
						{type: "radio", label: "批处理:", name: "type",value:"1",checked: true,inputWidth: 30},
					]},
					{type: "block", width: "390",name:"arar1", list:[
						{type: "input", label: "Spring ID:", name: "beanId", maxLength:50},
						{type: "input", label: "方法名称:", name: "method", maxLength:50},
					]},
					{type: "block", width: "420",name:"arar2", list:[
						{type: "input", label: "CMD命令:", name: "command", maxLength:200, rows:3,inputWidth: 300},
					]},
							
					{type: "block", width: "420", list:[
						{type: "input", label: "描述:", name: "remark", maxLength:200, rows:3,inputWidth:320},
					]},
				]},
			]},
        ],
		settings: {labelWidth: 80, inputWidth: 150}
	};
	detailForm = layFrom.attachForm(initFormFormat(detailFormData));
	
	detailForm.showItem("arar2");
	detailForm.hideItem("arar1");
	detailForm.hideItem("timeArar2");
	detailForm.showItem("timeArar1");
	detailForm.attachEvent("onChange", function (id, value){
		if("type"== id){
			var type = detailForm.getItemValue("type");
			if("0"==type){
				detailForm.hideItem("arar2");
				detailForm.showItem("arar1");
			}else if("1"==type){
				detailForm.hideItem("arar1");
				detailForm.showItem("arar2");
			}
		}
		if("timingType"== id){
			var timingType = detailForm.getItemValue("timingType");
			if("0"==timingType){
				detailForm.hideItem("timeArar2");
				detailForm.showItem("timeArar1");
			}else if("1"==timingType){
				detailForm.hideItem("timeArar1");
				detailForm.showItem("timeArar2");
			}
		}
	});
	return dataGrid;
}

function Operates(status,type,id){
	var surl = contextPath + "/appmanage/timing!updTimingOperates.json?Id="+id+"&status="+status;
		dhtmlxAjax.get(surl,function(loader){
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				dhtmlx.message(getMessage("operate_success"));
		    	mofify_a(status,type,id);
			    search();
		    } else {
		    	dhtmlx.message(getMessage("operate_failure"));
		    }
		});
}

function mofify_a(status,type,id){
	var div = document.getElementById(id);
	status = status==1?0:1;
	var operName = status==1?"停止":"启动";
	div.firstChild.attributes[0].value = "javascript:Operates("+status+","+type+",'"+id+"')";
	div.firstChild.innerHTML=operName;
}

function taskDetail(status,type,id){
	if (!dhxWins) {
        dhxWins = new dhtmlXWindows();
    }
    var winWidth = 500;
    var winHeight = 300;
    dataWin = dhxWins.createWindow("win_id", 0, 0, winWidth, winHeight);
    dataWin.setModal(true);
    dataWin.setText("运行明细");
    dataWin.center();
    if (initToolbar && typeof initToolbar == "function") {
        var statusBar = dataWin.attachStatusBar();
        var toolBar = new dhtmlXToolbarObject(statusBar);
        initToolbar.call(this, toolBar);
    }
	var taskDataGrid = dhxWins.window("win_id").attachGrid();
	//MODEL_URL = contextPath+"/appmanage/timing-log";
	taskGridData  = {
			format: {
				headers: ["<center>开始时间</center>","<center>结束时间</center>","<center>执行结果</center>","<center>返回结果</center>"],
				   cols: ["startDate","endDate","success","message"],
				colWidths: ["150","150","100","250"],
				colTypes: ["ro","ro","co","ro"],
				colAligns: ["left","left","left","left"]
			}
		};

	var colTypeCombo = taskDataGrid.getCombo(2);
	colTypeCombo.put("0","执行失败");
	colTypeCombo.put("1","执行成功");
	
	task_url = contextPath+"/appmanage/timing-log!search.json?Q_EQ_timingId="+id;
	initGrid(taskDataGrid,taskGridData);
	dataGrid.enableTooltips("true,true,true,true");
	search(taskDataGrid,taskGridData,task_url);
}

function initToolbar(toolBar) {
    toolBar.addButton("close", 3, "&nbsp;&nbsp;关闭&nbsp;&nbsp;");
    toolBar.setAlign("right");
    toolBar.attachEvent("onClick", function(id) {
        if (id == "close") {
            dataWin.close();
        }
    });
}