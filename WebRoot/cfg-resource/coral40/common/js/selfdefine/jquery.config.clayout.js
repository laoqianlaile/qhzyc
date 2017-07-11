/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成布局JS
 * 
 * 依赖JS:
 *    
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.clayout", {
		version: $.config.version,
		options: {
			menuId : "-1",   // 菜单ID
			componentVersionId: "-1", // 构件ID
			moduleId: "-1", // 模块ID
			workflowId: null, // 工作流ID
			box     : null,
			split   : true,
			columns : null,
			dataId  : null, // 自定义构件要打开的记录ID
			info   : null,  // 布局配置信息
			master : null,  // {tableId: "xx", dataId: "yy"}
			type   : 0,     // 0-布局定义 1-树布局
			global : null   
		},
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	this.timestamp = $.config.getTimestampData(this.options.global);
	    	this.gSequence = 0; //当前列表在所有列表中的序号
	    	this.fSequence = 0; //当前表单在所有表单中的序号
		    var JQ_override = window[CFG_overrideName(this.timestamp)];
	    	// override current grid
	    	if ($.isFunction(JQ_override)) {
	    		JQ_override.apply(this);
	    	}
	    	if (null === this.options.info) {
	    		CFG_message("获取构件信息出错，请检查！", "error");
	    		return;
	    	}
	    	this.element.addClass("coral-adjusted");
	    	/*if (0 === this.options.type && isEmpty(this.options.componentVersionId)) {
	    		//this.options.moduleId = this.options.info.id;
	    		this.options.componentVersionId
	    		                      = this.options.info.componentVersionId;
	    	}*/
	    	// 各布局之前的关系缓存
	    	this._storeRelation();
	    	// 初始化布局
	    	this._initLayout();
	    	// 初始化每个layout内容
	    	this._initContent();
	    	// 更新全局缓存信息
	    	$.config.setGlobalData(this.options.global, this.relation);	 
		},
		beforeCreate : function () {
			
		},
		/**
		 * 初始化layout
		 **/
		_initLayout : function () {
			
			var mId = this._mLayoutId(), sId = this._sLayoutId(), pattern = this._getPattern(),
			    jqNorth = null, jqWest = null, jqCenter = null,
			    jqSouth = null, jqEast = null,
			    areaSize= null;
			// LTC: layout centre; LTN: layout north; LTW: layout west
			// LTT: layout tree;   LTR: layout right panel
			
			areaSize = this.getAreaSize();
			
			this.mLayoutUI = $("<DIV id=\"" + mId + "\"  data-options=\"fit:true\" style=\"width:100%;height:100%;\"></DIV>").appendTo(this.element);
			if ($.config.pattern._1C === pattern) {
				jqCenter = $("<DIV id=\"" + this._generateId("LTC") + "\" data-options=\"region:'center'\"></DIV>").appendTo(this.mLayoutUI);
			} else if (pattern.indexOf($.config.pattern._2E) > -1) {
				jqNorth = $("<DIV id=\"" + this._generateId("LTN") + "\" data-options=\"region:'north',split:" + this.options.split + 
						(pattern !== $.config.pattern._2E ? ",title:'查询区'" : "") + 
						("1" === this.options.info.collapse1 ? ",collapsed:true" : "") + "\"" +
						" style=\"height:" + areaSize[0] + "px;\"></DIV>").appendTo(this.mLayoutUI);
				jqCenter= $("<DIV id=\"" + this._generateId("LTC") + "\" data-options=\"region:'center'\"" +
						" style=\"height:" + areaSize[1] + "px;\"></DIV>").appendTo(this.mLayoutUI);
			} else if ($.config.pattern._2U === pattern) {
				jqWest  = $("<DIV id=\"" + this._generateId("LTW") + "\" data-options=\"region:'west',split:" + this.options.split + "\"" +
						" style=\"width:" + (0 === this.options.type ? "260px" : this._hw() + "px") + ";\"></DIV>").appendTo(this.mLayoutUI);
				jqCenter=$("<DIV id=\"" + this._generateId("LTC") + "\" data-options=\"region:'center'\"></DIV>").appendTo(this.mLayoutUI);
			} else if ($.config.pattern._3L === pattern) {
				jqWest  = $("<DIV id=\"" + this._generateId("LTT") + "\" data-options=\"region:'west',split:" + this.options.split + "\"" +
						" style=\"width:" + areaSize[0] + "px;\"></DIV>").appendTo(this.mLayoutUI);
				jqCenter= $("<DIV id=\"" + this._generateId("LTR") + "\" data-options=\"region:'center'\"></DIV>").appendTo(this.mLayoutUI);
				
				this.sLayoutUI = $("<DIV id=\"" + sId + "\"  data-options=\"fit:true\" " +
						"style=\"width:100%;height:100%;\"></DIV>").appendTo(jqCenter);
				
				jqNorth = $("<DIV id=\"" + this._generateId("LTN") + "\" data-options=\"region:'north',split:" + this.options.split + "\"" +
						" style=\"height:" + areaSize[1] + "px;\"></DIV>").appendTo(this.sLayoutUI);
				jqCenter= $("<DIV id=\"" + this._generateId("LTC") + "\" data-options=\"region:'center'\"></DIV>").appendTo(this.sLayoutUI);
			} else if ($.config.pattern._3E === pattern) {
				jqNorth = $("<DIV id=\"" + this._generateId("LTN") + "\" data-options=\"region:'north',split:" + this.options.split + "\"" +
						" style=\"height:" + areaSize[0] + "px;\"></DIV>").appendTo(this.mLayoutUI);
				jqCenter= $("<DIV id=\"" + this._generateId("LTR") + "\" data-options=\"region:'center'\"></DIV>").appendTo(this.mLayoutUI);
				jqSouth = $("<DIV id=\"" + this._generateId("LTS") + "\" data-options=\"region:'south',split:" + this.options.split + "\"" +
						" style=\"height:" + areaSize[2] + "px;\"></DIV>").appendTo(this.mLayoutUI);
			}
			
			if (0 === this.options.type) {
				// 全局数据存储用
				//this.options.global = this.mLayoutUI.get(0); 
				// 构件嵌入的区域
				this.nestedPanel = ($.config.pattern._2U === pattern) ? jqCenter : $(this.mLayoutUI).parent();  
		    	// 构件组装信息
		    	this._storeAssembleData();
			}
			
			this.mLayoutUI.layout();
			
			if ($.config.pattern._3L === pattern) {
				this.sLayoutUI.layout();
			}
		},
		// 布局中一半宽度
		_hw : function () {
			return this.element.width() / 2;
		}, 
		// 布局中一半高度
		_hh : function () {
			return this.element.height() / 2;
		},
		// 布局中1/3高度
		_th : function () {
			return this.element.height() / 3;
		},
		// 获取布局区域的大小 
		getAreaSize : function() {
			var allw = this.element.width(), 
			    allh = this.element.height(),
			    a1s  = this.options.info.area1Size,
			    a2s  = this.options.info.area2Size,
			    a3s  = this.options.info.area3Size,
			    tt   = this.options.info.templateType,
			    s1 = 0, s2 = 0, s3 = 0;
			if ($.config.pattern._1C === tt) {
				s1 = allh;
			} else if (tt.indexOf($.config.pattern._2E) > -1) {
				if (isEmpty(a1s) && isEmpty(a2s)) {
					s1 = s2 = allh/2;
				} else if (isNotEmpty(a1s)) {
					s1 = this.processSize(allh, a1s);
					s2 = allh - s1;
				} else {
					s2 = this.processSize(allh, a2s);
					s1 = allh - s2;
				}
			} else if ($.config.pattern._2U === tt) {
				s1 = 260,
				s2 = allw - s1;
			} else if ($.config.pattern._3E === tt) {
				if (isEmpty(a1s) && isEmpty(a2s) && isEmpty(a3s)) {
					s1 = s2 = s3 = allh/3;
				} else if (isNotEmpty(a1s) && isEmpty(a2s) && isEmpty(a3s)) {
					s1 = this.processSize(allh, a1s);
					s2 = s3 = (allh - s1)/2;
				} else if (isNotEmpty(a2s) && isEmpty(a1s) && isEmpty(a3s)) {
					s2 = this.processSize(allh, a2s);
					s1 = s3 = (allh - s2)/2;
				} else if (isNotEmpty(a3s) && isEmpty(a2s) && isEmpty(a1s)) {
					s3 = this.processSize(allh, a3s);
					s2 = s1 = (allh - s3)/2;
				} else if (isNotEmpty(a1s) && isNotEmpty(a2s) /*&& isEmpty(a3s)*/) {
					s1 = this.processSize(allh, a1s);
					s2 = this.processSize(allh, a2s);
					s3 = (allh - s1 - s2);
				} else if (isNotEmpty(a1s) && isNotEmpty(a3s) && isEmpty(a2s)) {
					s1 = this.processSize(allh, a1s);
					s3 = this.processSize(allh, a3s);
					s2 = (allh - s1 - s3);
				} else if (isNotEmpty(a2s) && isNotEmpty(a3s) && isEmpty(a1s)) {
					s2 = this.processSize(allh, a2s);
					s3 = this.processSize(allh, a3s);
					s1 = (allh - s2 - s3);
				}
			} else if ($.config.pattern._3L === tt) {
				s1 = 260;
				if (isEmpty(a2s) && isEmpty(a3s)) {
					s2 = s3 = allh/2;
				} else if (isNotEmpty(a2s)) {
					s2 = this.processSize(allh, a2s);
					s3 = allh - s2;
				} else {
					s3 = this.processSize(allh, a3s);
					s2 = allh - s3;
				}
			}
			return [s1, s2, s3];
		},
		// 将区域大小转换成实际像素大小
		processSize : function (alls, size) {
			var re = /^(\d|[1-9]\d|100)%$/;
			if (re.test(size)) {
				size = alls * parseInt(size.substring(0, size.length - 1), 10) / 100;
			} else {
				size = parseInt(size, 10);
			}
			return size;
		},
		// 获取主表ID
		getMasterTableId : function (tableId, type) {
			var storeId = $.config.storeId(tableId, type),
			    masterTableId = "", data;
			
			if (storeId in this.relation) {
				data = this.relation[storeId];
				data = this.relation[data.masterId];
				if (!data) return "";
				masterTableId = data.tableId;
				if (masterTableId === tableId) {
					return this.getMasterTableId(masterTableId, data.type);
				}
			}
			return masterTableId;
		},
		// 构造创建表单和列表的options
		_getComponentOptions : function (uiPanel, number) {
			var info = this.options.info,
			    masterTableId,
			    opts = {
					menuId : this.options.menuId,
					uiPanel: uiPanel,
					number: number,
					model : info["area" + number + "Id"],
					thumbnail : info["thumbnail" + number],
					sort : info["sort" + number],
					moduleId: this.options.moduleId,
					tableId : info["table" + number + "Id"],
					componentVersionId : null2empty(this.options.componentVersionId),
					workflowId : null2empty(this.options.workflowId),
					box    : null2empty(this.options.box),
					global : this.options.global
				};
			
			if (1 === number || (2 === number && info.table1Id === info.table2Id)) {
				if (null != this.options.master) opts.master = this.options.master;
				if (isNotEmpty(this.options.columns)) opts.columns = this.options.columns;
				// 
				if (isNotEmpty(this.options.box)) opts.box = this.options.box;
				// 组装传入记录ID
				if (isNotEmpty(this.options.dataId)) opts.dataId = this.options.dataId;
			} else if ($.config.contentType.isForm(opts.model)) {
				// 表单对应的主表ID
				masterTableId = this.getMasterTableId(opts.tableId, opts.model);
				if (isNotEmpty(masterTableId)) {
					opts.master = {tableId: masterTableId, dataId: ""};
				}
			}
			//
			if ($.config.contentType.isForm(opts.model)) {
				opts.sequence = (++this.fSequence);
				// opts.statusbar = false;
			} else if ($.config.contentType.isGrid(opts.model)) {
				opts.sequence = (++this.gSequence);
			}
			return opts;
		}, 
		// 初始整个布局内容
		_initContent : function () {
			var pattern = this._getPattern(), 
			    info = this.options.info, options;
			
			if ($.config.pattern._1C === pattern) {
				// 初始化区域一
				this._initLayoutCell(this.mLayoutUI.layout("panel", "center")  , 1);
			} else if (pattern.indexOf($.config.pattern._2E) > -1) {
				// 初始化区域一
				this._initLayoutCell(this.mLayoutUI.layout("panel", "north")  , 1);
				// 初始化区域二
				this._initLayoutCell(this.mLayoutUI.layout("panel", "center") , 2);
			} else if ($.config.pattern._2U === pattern) {
				if (0 === this.options.type) {
					// 初始化树
					this._initTreeCell(this.mLayoutUI);
				} else {
					// 初始化区域一
					this._initLayoutCell(this.mLayoutUI.layout("panel", "west")  , 1);
					// 初始化区域二
					this._initLayoutCell(this.mLayoutUI.layout("panel", "center"), 2);
				}
			} else if ($.config.pattern._3E === pattern) {
				// 初始化区域一
				this._initLayoutCell(this.mLayoutUI.layout("panel", "north"), 1);
				// 初始化区域二
				this._initLayoutCell(this.mLayoutUI.layout("panel", "center"), 2);
				// 初始化区域三
				this._initLayoutCell(this.mLayoutUI.layout("panel", "south"), 3);
			} else if ($.config.pattern._3L === pattern) {
				// 初始化区域一
				this._initLayoutCell(this.mLayoutUI.layout("panel", "west")  , 1);
				// 初始化区域二
				this._initLayoutCell(this.sLayoutUI.layout("panel", "north") , 2);
				// 初始化区域三
				this._initLayoutCell(this.sLayoutUI.layout("panel", "center"), 3);
			}
		},
		// 初始化布局单元内容
		_initLayoutCell : function (uiPanel, number) {
			var jqUI = null, 
			    options = this._getComponentOptions(uiPanel, number),
			    type    = options.model,
			    storeId = $.config.storeId(options.tableId, type),
			    element = uiPanel.panel("body");
			if ($.config.contentType.isForm(type)) {
				jqUI = new $.config.cform(options, element);
			} else if ("3" === type) {
				jqUI = new $.config.cbsearch(options, element);
			} else {
				jqUI = new $.config.cgrid(options, element);
			}
			jqUI.global = this.global;
			
			if (storeId in this.relation) {
				this.relation[storeId].jqUI = jqUI;
			}
	    	// 更新全局缓存信息
	    	$.config.setGlobalData(this.options.global, this.relation);	 
	    	
			return jqUI;
		},
		// 树型布局初始化
		_initTreeCell : function (jq) {
			var rightPanel = jq.layout("panel", "center").panel("body"),
			    element    = jq.layout("panel", "west").panel("body");
			// 初始化树
			return new $.config.ctree({
						menuId : this.options.menuId,
						componentVersionId: this.options.componentVersionId,
						moduleId : this.options.moduleId,
						info   : this.options.info,
						rightPanel : rightPanel,
						global : this.options.global
					}, element);
		},
		// 生成ID规则
		_generateId : function (prefix) {
			return prefix + "_" + this.options.moduleId + "_" + this.uuid;
		},
		// 主布局DIV id
		_mLayoutId : function() {
			return this._generateId("m_lt");
		},
		// 子布局DIV id（3L）
		_sLayoutId : function () {
			return this._generateId("s_lt");
		},
		// 
		_getPattern : function() {
			return this.options.info.templateType;
		},
		// {type: /*自身内容(form/grid/card)*/, masterId: /*关系主表标记*/, detailId: /*关系从表标记*/, jqUI: /*自身对象*/}
		_storeRelation : function () {
			var pattern = this._getPattern(), 
		        info = this.options.info, relation = {};
			var store1Id, store2Id, store3Id;
			
			if (isNotEmpty(info.table1Id)) { // 布局一
				store1Id = $.config.storeId(info.table1Id, info.area1Id);
				relation[store1Id] = {
						tableId : info.table1Id,
						type: info.area1Id,
						masterId: null,
						detailId: null
					};
			}
			if (isNotEmpty(info.table2Id)) { // 布局二
				store2Id = $.config.storeId(info.table2Id, info.area2Id);
				relation[store2Id] = {
						tableId : info.table2Id,
						type: info.area2Id,
						masterId: store1Id,
						detailId: null
					};
				relation[store1Id].detailId = store2Id;
			}
			if (isNotEmpty(info.table3Id)) { // 布局三
				store3Id = $.config.storeId(info.table3Id, info.area3Id);
				relation[store3Id] = {
						tableId : info.table3Id,
						type: info.area3Id,
						masterId: store2Id,
						detailId: null
					};
				relation[store2Id].detailId = store3Id;
			}
			
			this.relation = relation;
		},
		// 存储组装信息(注：请勿复写)
		_storeAssembleData : function () {
			var _this = this, data  = null;
			data = $.config.makeupAssembleData(this.element, this.nestedPanel);
			// 添加构件类型 （目前在获取预留区时使用）
			data.moduleType = this.options.info.type;
			
			$.config.setAssembleData(this.options.global, data);
		},
		
		/**
		 * 通过表ID获取对应的cgrid列表对象
		 * @param tableId
		 * @returns
		 */
		getGrid : function (tableId) {
			var storeId = $.config.storeId(tableId, $.config.contentType.grid);
			if (storeId in this.relation) {
				return this.relation[storeId].jqUI;
			}
			return false;
		},
		
		/**
		 * 通过表ID获取对应的cform列表对象
		 * @param tableId
		 * @returns
		 */
		getForm : function (tableId) {
			var storeId = $.config.storeId(tableId, $.config.contentType.form);
			if (storeId in this.relation) {
				return this.relation[storeId].jqUI;
			}
			return false;
		}, 
		
		// 销毁整个构件
		_destroy : function () {
			var name; 

			name = CFG_overrideName(this.timestamp);
			if (name in window) delete window[name];
			
			name = CFG_actionName(this.timestamp);
			if (name in window) delete window[name];
			
			$.removeData(this.options.global);
		},
		
		refresh: function() {
		    $.coral.adjusted(this.element);
		}

	});
	

})( jQuery );
