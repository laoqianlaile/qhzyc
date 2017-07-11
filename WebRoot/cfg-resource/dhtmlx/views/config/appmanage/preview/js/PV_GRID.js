/**
 * MT   => module template
 *       模块模板
 * GRID => grid
 *       列表
 * @param that
 *       页面全局变量
 * @param layout
 *       列表布局区域
 * @param tableId
 *       列表对应表ID
 * @param areaIndex
 *       区域索引位置
 */
function PV_GRID_init(that, layout, tableId, areaIndex) {
	layout.hideHeader();
	//
	var _this = this;
	var gLayout = null;
	var sLayout = null;
	var sform   = null;
	var scfg = CFG_scfg(tableId, that.moduleId);
	if (scfg.type == 1 && true == scfg.exists) {
		PV_search_area(that, layout, tableId, scfg);
		sform   = that.sform;
		sLayout = that.sLayout;
		gLayout = that.gLayout;
	} else {
		gLayout = layout;
	}
	// GRID配置信息
	var gcfg = CFG_gcfg(tableId, that.moduleId);
	if (null == gcfg || 0 == gcfg.headers.length) {
		dhtmlx.alert("列表未配置！");
		return;
	};
	/* 构造列表与表单对象属性ID*/ 
	var pGId = PV_P_GridId(tableId);
	var pFId = PV_P_FormId(tableId);
	var pTId = PV_P_GridTableId(that.gNum);
	/* 列表对应的表ID*/
	that[pTId] = tableId;//用于双列表关联查询
	/* 列表信息存储对象*/
	that[pGId] = {};
	/* 基本检索面板表单中的值对象*/
	that[pGId].PV_DATA_b_seach = null;
	/* 高级检索面板表单中的值对象*/
	that[pGId].PV_DATA_g_seach = null;
	/* 检索方式：PV_common.S_BASE/PV_common.S_GREAT*/
	that[pGId].PV_DATA_seach_type = PV_common.S_BASE;
	/* 检索面板表单中的值对象*/
	//that[pGId].PV_DATA_seach = null;
	that[pGId].index = that.gNum; // 列表索引
	that[pGId].curRowId = null;   // 当前选中记录的数据ID(一条)
	/************************列表公用事件****************************/
	/** 列表数据刷新.*/ 
	that[pGId].PV_GRID_reload = function() {
		PV_GRID_load();
	};
	/** 禁用工具条上的所有按钮.*/ 
	that[pGId].PV_GRID_disable_toolbar = function() {
		if (gtbar) {
			gtbar.forEachItem(function(itemId){
				gtbar.disableItem(itemId);
		    });
		}
	};
	/** 启用工具条上的所有按钮.*/ 
	that[pGId].PV_GRID_enable_toolbar = function() {
		if (gtbar) {
			gtbar.forEachItem(function(itemId){
				gtbar.enableItem(itemId);
		    });
		}
	};
	/** 
	 * 根据属性获取ID.
	 * @param id
	 * @param prop -- processInstanceId(流程实例ID)/workitemId(工作项ID)/workitemStatus(工作项状态)
	 * */
	that[pGId].PV_coflowId = function(id, prop) {
		if (PV_common.ROW_ID == prop) {
			return grid.getSelectedRowId();
		}
		return grid.getUserData(id, prop);
	};
	
	/** 每页显示条数.*/ 
	function PV_GRID_PageSize() {
		return getCookie("pagesize") || PAGE_SIZE;
	};
	var grid = gLayout.attachGrid();
	var gurl = CFG_gbaseUrl((contextPath + "/appmanage/show-module!search.json"), gcfg, tableId, that.moduleId, "");;
	/*****************************(工具条事件)*******************************/
	this.clickEvent = function(id) {
		if (PV_common.P_CREATE == id) {// 新增
			if (that.fNum > 0 && that[pFId]) {
				that[pFId].PV_FORM_create();
			} else {
				PV_GRID_create(undefined, "新增");
			}
		} else if (PV_common.P_UPDATE == id) {// 修改
			if (that.fNum > 0 && that[pFId]) {
				that[pFId].PV_FORM_load();
			} else {
				PV_GRID_update();
			}
		} else if (PV_common.P_DELETE == id) {// 删除
			PV_GRID_delete();
		} else if (PV_common.P_SEARCH == id) {// 检索
			PV_GRID_search();
		} else if (PV_common.P_UPLOAD == id) {// 上传附近
			PV_GRID_upload();
		} else if (id.indexOf(PV_common.P_REPORT_PRE) > -1) {
			PV_GRID_report(id);
		}
	};
	// 工具条初始化
	var gtbar = PV_TBAR_init(that, gLayout, tableId, PV_common.L_GRID, _this.clickEvent, areaIndex);
	//
	if (scfg.type == 1) {
		try {
			var searchPos = (gtbar.getPosition(PV_common.P_SEARCH));
			if (null != searchPos && searchPos > -1) {
				gtbar.setItemText(PV_common.P_SEARCH, "显示检索区");
			};
		} catch (e) {}
	}
	// 状态栏
	var gsbar = gLayout.attachStatusBar();
	/** 初始化列表*/ 
	function PV_GRID_InitBody() {
		// 1. init grid 
		grid.setImagePath(IMAGE_PATH);
		grid.setHeader(gcfg.headers.toString());
		grid.setInitWidths(gcfg.widths.toString());
		grid.setColTypes(gcfg.types.toString());
		grid.setColAlign(gcfg.aligns.toString());
		grid.setSkin(Skin);
		grid.init();
		grid.enableMultiselect(true);
		grid.setStyle("font-weight:bold;", "", "", "");
		// 2. 分页条
		grid.enablePaging(true, PV_GRID_PageSize(), 1, gsbar);
		grid.setPagingSkin('toolbar', Skin);
	};
	/** 加载列表数据*/ 
	function PV_GRID_load(pageNum) {
		if (undefined == grid || null == grid 
				|| undefined == gurl || null == gurl || "" == gurl) {
			return;
		}
        grid.clearAll();
		var url = gurl;
		// 加上检索过滤条件
		url += "&P_filter=" + PV_GRID_filter();
		//
		if (that.coflow && that.coflow.box) {
			url += "&P_box=" + that.coflow.box;
		}
		// 判断是否有主列表：如果有主列表需要根据主列表的当前选中记录的数据ID	来过滤
		var mTableId = mDataId = "";
		if (that[pGId].index > 1) {
			// 1. 获取主列表的表ID
			var pMtId = PV_P_GridTableId(that[pGId].index - 1);
			mTableId  = that[pMtId];
			// 2. 获取主列表中当前选中记录的数据ID
			var pMgId = PV_P_GridId(mTableId);
			mDataId   = that[pMgId].curRowId;
			// 3. 如果主列表未选中记录，则不主动查询
			if (null == mDataId) return;
		}
		url += "&P_M_tableId=" + mTableId + "&P_M_dataId=" + mDataId;
		// 加上分页过滤条件
        pageNum = pageNum ? pageNum : grid.currentPage;
        url += "&P_pagesize=" + PV_GRID_PageSize() + "&P_pageNumber=" + pageNum;
        
        var loader = dhtmlxAjax.getSync(encodeURI(url));
        var loaderDoc = loader.xmlDoc.responseText;
        if ("" == loaderDoc)
            return;
        var data = eval('(' + loaderDoc + ')');
        // 如果查询结果为空
        if (data.rows == undefined) {
            //dhtmlx.message("结果集为空！");
            return;
        }
        grid.parse(data, "json");//*/
	}
	/** 组装列表查询过滤条件.*/
	function PV_GRID_filter() {
		var filter = "";
		//主列表时， 如果树节点为字段节点时，需要把这个字段加入过滤条件中
		if (that[pGId].index == 1 && undefined != that.PV_tree_column_filter 
				&& null != that.PV_tree_column_filter && "" != that.PV_tree_column_filter) {
			filter = ("," + that.PV_tree_column_filter);
		}
		/*var data = that[pGId].PV_DATA_seach;
		if (null != data) {
			for (var p in data) {
				var v = data[p];
				if (null == v || "" == v) continue;
				filter += ("," + p + PV_cv_split + v);
			}
		}//*/

		if (PV_common.S_BASE == that[pGId].PV_DATA_seach_type) {
			// 基本查询区
			var data = PV_GRID_SearchAreaFilter();
			if (null != data) {
				for (var p in data) {
					var v = data[p];
					if (null == v || "" == v) continue;
					filter += ("," + p + CFG_cv_split + v);
				}
			}
		} else { 
			// 高级查询区
			if (that[pGId].PV_DATA_g_seach && "" != that[pGId].PV_DATA_g_seach) {
				filter += ("," + that[pGId].PV_DATA_g_seach);
			}
		}
		if (filter.length > 0) {
			return filter.substring(1);
		}
		return filter;
	}
	PV_GRID_InitBody();
	PV_GRID_load();
	// 如果是明细列表的话，初始化时禁用工具条
	if (that[pGId].index > 1) {
		that[pGId].PV_GRID_disable_toolbar();
		that[pGId].disable_toolbar = true;
	}
	/****************************** 列表双击事件 *********************************/
	var eurl = contextPath + "/appmanage/app-define!getDbclickEvent.json?E_model_name=datagrid&Q_tableId="
			+ tableId + "&Q_moduleId=" + that.moduleId;
	var dbclickData = loadJson(eurl);
	if("0" != dbclickData){
		grid.attachEvent("onRowDblClicked", function(rId, ind) {
			if("2" == dbclickData){
				that.isView = true;
			}
			PV_GRID_create(rId);
		});
	}
	/****************************** 列表单击事件 *********************************/
	// 查看或修改
	grid.attachEvent("onRowSelect", function(rId, ind) {
		// 当前选中记录的数据ID
		that[pGId].curRowId = rId;
		// 判断列表对应的表单是否在同一个页面中
		if (that.fNum > 0 && that[pFId]) {
			that[pFId].PV_FORM_load(rId);
		}
		// 判断是否有从表列表
		if (that.gNum > that[pGId].index) {
			var pRtId = PV_P_GridTableId(that[pGId].index + 1); // 列表对应的表ID属性名
			if (undefined != that[pRtId] && null != that[pRtId]) {
				var pRgId = PV_P_GridId(that[pRtId]);
				that[pRgId].PV_GRID_reload();
				// 激活明细列表工具条
				if (true == that[pRgId].disable_toolbar) {
					that[pRgId].PV_GRID_enable_toolbar();
					that[pRgId].disable_toolbar = false;
				}
			}
		}
	});
	/*******************************工具条按钮事件【开始】*********************************/
	/** 新增.*/
	function PV_GRID_create (dataId, title) {
		title = title || "详细信息";
		var win = PV_CreateDhxWindow({id:"WIN$" + tableId, title:title, width: width(), height: 400});
		// 顶部“关闭”按钮事件重写
		win.button("close").attachEvent("onClick", function() {
			close();
		});
		// 底部工具条“关闭”按钮
		var sbar = win.attachStatusBar();
		var tbar = new dhtmlXToolbarObject(sbar.id);
		tbar.setIconPath(TOOLBAR_IMAGE_PATH);
		tbar.addButton("bottom$close", 4, "关闭", "close.gif");
		tbar.setAlign("right");
		tbar.attachEvent("onClick", function(id) {
			if ("bottom$close" == id) {
				close();
			}
		});//*/
		var master = null; // 关联主列表对象
		if (that[pGId].index > 1) {
			var pRTId = PV_P_GridTableId(that[pGId].index - 1); // 关联主列表表ID寄存属性
			if (undefined != that[pRTId]) {
				master = {};
				var rTableId = that[pRTId]; // 关联主列表表id值
				var pRGId = PV_P_GridId(rTableId);  // 关联主列表信息寄存属性
				master.tableId = rTableId;
				master.dataId  = that[pRGId].curRowId; // 关联主列表选中行的rowId值
			}
		}
		// 初始化表单
		PV_FORM_init(that, win, tableId, areaIndex, dataId, master);
		win.showHeader();
		/** 窗口宽度.*/
		function width() {
			var url = contextPath + "/appmanage/app-form!domain.json?P_tableId=" + tableId + "&P_moduleId=" + that.moduleId;
			var obj = loadJson(url);
			var width = (obj.width + 20);
			if (width > 1024) {
				width = 1024;
			}
			return width;
		}
		/** 关闭事件.*/
		function close() {
			// 1. 刷新列表
			that[pGId].PV_GRID_reload();
			// 2. 删除表单窗口属性
			if (undefined != that[pFId]) { that[pFId] = null; }
			// 3. 关闭表单窗口
			win.close();
		}
		
	};
	/** 修改.*/
	function PV_GRID_update () {
		var rowId = grid.getSelectedRowId();
		if (null == rowId || "" == rowId) {
			dhtmlx.message(getMessage("select_record"));
			return;
		}
		if (rowId.split(",").length > 1) {
			dhtmlx.message(getMessage("select_only_one_record"));
			return;
		}
		PV_GRID_create(rowId, "修改");
		//that[pFId].PV_FORM_load(rowId);
	};
	/** 删除.*/
	function PV_GRID_delete () {
		var ids = grid.getSelectedRowId();
		if (null == ids || "" == ids) {
			dhtmlx.message(getMessage("select_record"));
			return;
		}
		dhtmlx.confirm({text:getMessage("delete_warning"), ok:"确定", cancel:"取消", callback: function(success) {
			if (false == success) return;
			var url = contextPath + "/appmanage/show-module/" + ids + ".json?_method=delete&P_tableId=" + tableId;
			dhtmlxAjax.get(url, function(loader) {
				PV_GRID_load();
				dhtmlx.message(getMessage("delete_success"));
	        });
		}});
	};
	/** 检索.*/
	function PV_GRID_search() {
		if (false == scfg.exists) {
			dhtmlx.alert("检索项未配置！");	return;
		} else if ("ERROR" == scfg.exists) {
			dhtmlx.alert("获取检索配置出错！"); return;
		}
		if (1 == scfg.type) {
			if (null == sLayout) return;
			if (sLayout.isCollapsed()) {
				sLayout.expand();
				gtbar.setItemText(PV_common.P_SEARCH, "隐藏检索区");
			} else {
				//MT_GRID_load();
				sLayout.collapse();
				gtbar.setItemText(PV_common.P_SEARCH, "显示检索区");
			}
			return;
		}
		var struction = eval("(" + scfg.formJson + ")");
		var win = PV_CreateDhxWindow({title:"检索", width: parseInt(scfg.width) + 20, height: scfg.height, modal: false});
		// 底部工具条“检索”按钮
		var sbar = win.attachStatusBar();
		var tbar = new dhtmlXToolbarObject(sbar.id);
		tbar.setIconPath(TOOLBAR_IMAGE_PATH);
		tbar.addButton("bottom$search", 1, "搜索", "search.gif");
		tbar.setAlign("right");
		tbar.attachEvent("onClick", function(id) {
			if ("bottom$search" == id) {
				PV_GRID_search_data();
			}
		});//*/
		// 初始化检索面板表单
		sform = win.attachForm(struction);
		// 上一次检索面板表单中的值
		if (null != that[pGId].PV_DATA_seach) sform.setFormData(that[pGId].PV_DATA_seach);
	}
	/** 组装查询区的过滤条件.*/
	function PV_GRID_SearchAreaFilter() {
		if (null == sform) return;
		//that[pGId].MT_DATA_seach = {};
		that[pGId].PV_DATA_b_seach = {};
		sform.forEachItem(function(id) {
			if (id.indexOf(CFG_oc_split) > 0 && sform.isItemEnabled(id)) {
				var val = sform.getItemValue(id);
				if (null == val) { val = ""; }
				that[pGId].PV_DATA_b_seach[id] = val;//sform.getItemValue(id);
			}
		});
		return that[pGId].PV_DATA_b_seach;
	}
	/** 执行数据查询.*/
	function PV_GRID_search_data() {
		if (null == that[pGId].PV_DATA_seach) that[pGId].PV_DATA_seach = {};
		sform.forEachItem(function(id) {
			if (id.indexOf(PV_oc_split) > 0) {
				var val = sform.getItemValue(id);
				if (null == val) { val = ""; }
				that[pGId].PV_DATA_seach[id] = val;//sform.getItemValue(id);
			}
		});
		PV_GRID_load();
	}
	/** 跟踪.*/
	function PV_GRID_track() {
		var rowId = grid.getSelectedRowId();
		if (null == rowId || "" == rowId) {
			dhtmlx.alert("请选择一条记录进行跟踪！");
			return;
		}
		if (rowId.split(",").length > 1) {
			dhtmlx.alert("只能选择一条数据进行跟踪！");
			return;
		}
		// TODO
		
		
	}
	/** 撤回.*/
	function PV_GRID_recall() {
		var rowId = grid.getSelectedRowId();
		if (null == rowId || "" == rowId) {
			dhtmlx.alert("请选择一条记录进行撤回！");
			return;
		}
		if (rowId.split(",").length > 1) {
			dhtmlx.alert("只能选择一条数据进行撤回！");
			return;
		}
		// TODO
		var workitemId = grid.getUserData(rowId, PV_common.W_ID);
		var url = (PV_app_uri + "/show-module!coflow.json?P_op=" + PV_common.P_RECALL + "&P_workitemId=" + workitemId);
		dhtmlxAjax.get(url,function(loader){
			var wid = eval("(" + loader.xmlDoc.responseText + ")");
			if (typeof(wid) == "number" && wid > 0) {
				dhtmlx.message(getMessage("operate_success"));
				return;
			}
			dhtmlx.message(getMessage("operate_failure"));
		});
		
	}
	/** 打印.*/
	function PV_GRID_report(btnId) {
		//var name = gtbar.getItemText(btnId);
		var reportId = btnId.substring(PV_common.P_REPORT_PRE.length);
		var url = DHX_RES_PATH + "/views/config/appmanage/report/reportprint.jsp?P_reportId=" + reportId + "&P_tableId=" + tableId;
		
		var filter = grid.getSelectedRowId();
		if (null == filter || "" == filter) {
			filter = PV_GRID_filter();
			if (null != filter && "" != filter) {
				url += "&P_columnFilter=" + filter;
			}
		} else {
			url += "&P_rowIds=" + filter;
		}
		
		window.open(encodeURI(url), "打印", 'left=0,top=0,width=' + (screen.availWidth - 10) + ',height=' + (screen.availHeight-50) + ',toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes');
	}

	/**
	 * 附件上传
	 * @memberOf {TypeName} 
	 * @return {TypeName} 
	 */
	function PV_GRID_upload(){
		
		var tUrl = contextPath + "/appmanage/table-define!tableLabel.json?id=" + tableId;
		var tObj = loadJson(tUrl);
		if ("" != tObj) {
			var mTableId = mDataId = "";
			if (that[pGId].index > 1) {
				// 1. 获取主列表的表ID
				var pMtId = PV_P_GridTableId(that[pGId].index - 1);
				mTableId  = that[pMtId];
				// 2. 获取主列表中当前选中记录的数据ID
				var pMgId = PV_P_GridId(mTableId);
				mDataId   = that[pMgId].curRowId;
			}else{
				mTableId = tableId;
			}
		}else{
			var rowId = grid.getSelectedRowId();
			if (null == rowId || "" == rowId) {
				dhtmlx.message(getMessage("select_record"));
				return;
			}
			if (rowId.split(",").length > 1) {
				dhtmlx.message(getMessage("select_only_one_record"));
				return;
			}
		}
		
		//模块ID
		var modlueId = that.moduleId;
		var docUrl = contextPath+"/upload/document-upload";
		//判断该表是否有上传附件的权限
		dhtmlxAjax.get(docUrl+"!getDocUploadMessages.json?P_tableId=" + tableId, function(loader){
		var result = eval("(" + loader.xmlDoc.responseText + ")");
			if (!result.success) {
				alert(result.message);
	   		 } else {
	   			if (!dhxWins) {
		    		dhxWins = new dhtmlXWindows();
		    	}
			    dataWin = dhxWins.createWindow(WIN_ID, 0, 0, 450, 280);
			    dataWin.setModal(true);
			    dataWin.setText("附件导入");
			    dataWin.center();
			    dataWin.button('park').hide();
			    dataWin.button('minmax1').hide();
			    var vaultDiv = document.createElement("div");
			    vaultDiv.setAttribute("id", "vaultDiv");
			    document.body.appendChild(vaultDiv);
			    
			    var upload_url = docUrl + "!uploadHandler";// -处理文件上传
			    var getInfo_url = docUrl + "!getInfoHandler"; //处理进度提示
			    var getId_url = docUrl + "!getIdHandler"; //-处理会话初始化
	        	var vault = new dhtmlXVaultObject();
	            vault.setImagePath(DHX_RES_PATH + "/common/css/imgs/");
	            vault.setServerHandlers(upload_url, getInfo_url, getId_url);  //向服务器端发送处理
	            vault.setFilesLimit(100);//选择上传文件个数
				vault.strings = { remove: "移除", done: "完成", error: "上传失败!", btnAdd : "选择文件",btnUpload : "上传",btnClean : "清空"};
	            vault.onAddFile = function(fileName) { 
					var ext = this.getFileExtension(fileName); 
					return true;
				};
				//获取文件对象属性      ---
				 vault.onUploadComplete = function(files) {
					 for (var i=0; i<files.length; i++) {
						 var file = files[i];
						 var saveFileUrl;
						 if ("" != tObj){
							 saveFileUrl = docUrl+"!saveFileMessages.json?P_tableId=" + mTableId+"&P_moduleId="+modlueId+"&rId="+mDataId+"&P_fileName="+file.name;
						 }else{
							 saveFileUrl = docUrl+"!saveFileMessages.json?P_tableId=" + tableId+"&P_moduleId="+modlueId+"&rId="+rowId+"&P_fileName="+file.name;
						 }
						 dhtmlxAjax.get(saveFileUrl, function(loader){
						var mes_result = eval("(" + loader.xmlDoc.responseText + ")");
							if (!mes_result.success) {
					    		dhtmlx.message(mes_result.message);
					   		 } else {
					   			dhtmlx.message(mes_result.message);
					   			if ("" != tObj){
									 PV_GRID_load();
								 }else{
									 grid.selectRowById(rowId,false,false,true);
								 }
					    	}
						});
					}
				};
				
	            vault.create("vaultDiv");
			    dataWin.attachObject(vaultDiv);
		    	}
			});
	}
	
	
	
	/*******************************工具条按钮事件【结束】*********************************/
	// 分页条
	var ptbar = grid.aToolBar;
	// 分页条事件控制
	if (null != ptbar) {
		ptbar.detachEvent(ptbar.clickEventId);
		ptbar.attachEvent("onClick",function(val){ 
			val=val.split("_");
			switch (val[0]){
				case "leftabs":
					PV_GRID_move(1);
					break;
				case "left":
					PV_GRID_move(grid.currentPage-1);
					break;
				case "rightabs":
					PV_GRID_move(Math.ceil(grid.rowsBuffer.length/grid.rowsBufferOutSize));
					break;
				case "right":
					PV_GRID_move(grid.currentPage + 1);
					break;
				case "perpagenum":
					if (val[1]===this.undefined) return;
					grid.rowsBufferOutSize = parseInt(val[1]);				
					addCookie("pagesize",val[1],365*24);
					PV_GRID_move(1);				
					ptbar.setItemText("perpagenum","<div style='width:100%; text-align:right'>"+val[1]+" "+grid.i18n.paging.perpage+"</div>");
					break;
				case "pages":
					if (val[1]===this.undefined) return;
					PV_GRID_move(val[1]);
					ptbar.setItemText("pages","<div style='width:100%; text-align:right'>"+grid.i18n.paging.page+val[1]+"</div>");
					break;
			}
		});
		/** 翻页.*/
		function PV_GRID_move(pageNumber) {
			PV_GRID_load(pageNumber);
			grid.changePage(pageNumber);
		};
	}
	
	that[pGId].grid = grid;
}
/**
 * 列表链接列事件
 * @param url
 */
function MT_GRID_link(url, tableId) {
	var that = this;
	var pGId = PV_P_GridId(tableId);
	if (isEmpty(url)) {
		dhtmlx.message("未设置链接地址！");
		return;
	}
	var paramObj = parameters(url);
	url += "&P_moduleId=" + that.moduleId + "&P_tableId=" + tableId;
	if (url.indexOf(".json?") > 0) {
		dhtmlxAjax.get(addTimestamp(url), function(loader) {
			var jsonObj = eval("(" + loader.xmlDoc.responseText + ")");
			if (jsonObj.success) {
				if (jsonObj.message && isNotEmpty(jsonObj.message)) {
					dhtmlx.message(jsonObj.message);
				} else {
					dhtmlx.message(getMessage("operate_success"));
				}
				that[pGId].PV_GRID_reload();
			} else {
				dhtmlx.message(getMessage("operate_failure"));
			}
		});
	} else {
		var t = paramObj.title || "详细信息";
		var w = paramObj.width || 800;
		var h = paramObj.height || 600;
		if (h > document.body.clientHeight) {
			h = document.body.clientHeight;
		}
		var win = PV_CreateDhxWindow({title: t, width: w, height: h, modal: false});
		win.attachURL(addTimestamp(url));
		win.button("close").attachEvent("onClick", function() {
			win.close();
			that[pGId].PV_GRID_reload();
		});
	}
	/**
	 * 存储URL中的所有参数
	 * @param url
	 * @returns {___anonymous25694_25695}
	 */
	function parameters(url) {
		var paramObj = {};
		var idx = url.indexOf("?");
		if (idx < 0) return paramObj;
		var paramStr = url.substring(idx + 1);
		var paramArr = paramStr.split("&");
		for (var i = 0; i < paramArr.length; i++) {
			var oneParamStr = paramArr[i];
			var oneParamArr = oneParamStr.split("=");
			paramObj[oneParamArr[0]] = (oneParamArr.length == 1 ? "" : oneParamArr[1]);
		}
		return paramObj;
	}
}

