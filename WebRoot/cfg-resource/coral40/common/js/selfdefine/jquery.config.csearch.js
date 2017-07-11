/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成配置表单JS
 * 
 * 依赖JS:
 *    
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.cbsearch", {
		version: $.config.version,
		options: {
			menuId : "-1", // 菜单ID
			componentVersionId : "-1",
			moduleId: "-1",
			tableId : null,
			workflowId : null, // 工作流ID
			box     : null, // 工作箱
			number: 1,      // 当前列表在构件中的区域次序
			isNested : true,// 是否为嵌入式查询区 
			//master  : null, // {tableId: "xx", dataId: "yy"}
			global  : null,
			// 列表过滤条件格式，如：EQ_C_NAME≡xxx;LIKE_C_TITLE≡xxx；多个用英文分号(;)分隔
			// 三等于（≡）常量： CFG_cv_split； _C_常量：CFG_oc_split
			// 复杂条件：如   (name like '%qiu%' or birth_day=1983) and height > 168)
			// 则格式为 (((LIKE_C_NAME≡qiu)OR(EQ_C_BIRTH_DAY≡1983))AND(GT_C_HEIGHT≡168)) 注：每一项需要用括号括起来
			columns : null, 
			// 过滤条件 function(filter) {...;return filter;}
			//processFilter : null,
			sequence: 1, // 当前列表在构件中所有列表中的次序
			toolbar : true,
			override : null
		},
		specReg : /[\%\^\_\<\>\'\[\]\(\)]/im, // `~!@#\$\%\^\&\*\(\)_\-\+\=\<\>\?:\"\{\},\.\\\/;\'\[\]
		specMsg : "\%\^\_\<\>\'\[\]\(\)",
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	var globalTimestamp, override;
	    	if (!this.options.global) {
				this.options.global = this.element;
			}
	    	this.element.addClass("config-bsearch");
	    	if (this.options.isNested) {
	    		this.element.addClass("nested");
	    	} else {
	    		this.element.addClass("popup");
	    	}
	    	globalTimestamp = $.config.getTimestampData(this.options.global);
	    	if (isEmpty(globalTimestamp)) {
	    		globalTimestamp = getTimestamp();
	    		$.config.setTimestampData(this.options.global, globalTimestamp);
	    	}
    	    // 构件的时间戳
    	    this.options.globalTimestamp = globalTimestamp;
	    	// 构件组装数据
	    	this.assembleData = $.config.getAssembleData(this.options.global);
	    	// override current search
	    	override = this.options.override;
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
	    	override = window[CFG_overrideName(globalTimestamp)];
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
	    	// 构件区域的时间戳
	    	this.timestamp = getTimestamp();
	    	
	    	this.scfg = this._getSearchCfg();
	    	if (isEmpty(this.scfg.data)) {
	    		CFG_message("基本检索未配置！", "warning");
	    		return;
	    	}
	    	// 基本检索
	    	this._createSearch();
	    	//
	    	this._createTbar();
	    	// 自适应
	    	$.coral.adjusted(this.uiBorder);
	    	// 事件绑定
	    	this.bindEvent();
	    	// 条件控制
	    	this._disableColumnsFilter();
	    	//
	    	this.inited = true;
		},
		
		bindEvent : $.noop,
		// 查询区配置信息
		_getSearchCfg : function () {
			var url = this.getAction() + "!page.json" +
					"?E_frame_name=coral&E_model_name=search" +
					"&P_type=" + (this.options.isNested ? 1 : 0) + 
					"&P_tableId=" + this.options.tableId + 
					"&P_moduleId=" + this.options.moduleId +
					"&P_menuId=" + this.options.menuId +
					"&P_componentVersionId=" + this.options.componentVersionId +
					"&P_workflowId=" + null2empty(this.options.workflowId) +
					"&P_box=" + null2empty(this.options.box) +
					"&P_menuCode=" + this.getParamValue("menuCode") +
					"&P_timestamp=" + this.timestamp,
				scfg;
			scfg = $.loadJson(url);
			if (scfg && isNotEmpty(scfg.data)) {
                scfg.data = scfg.data.replace(/\{action\}/g, this.getAction());
                scfg.data = scfg.data.replace(/\{contextPath\}/g, $.contextPath);
                scfg.data = scfg.data.replace(/\{csearchDivId\}/g, this.element.attr("id"));
            }
			return scfg;
		},
		
		// 基本检索
		_createSearch : function () {
			this.uiBorder = $("<DIV class=\"coral-adjusted\"></DIV>").appendTo(this.element);
			this.uiBorder.css("overflow-x", "hidden !important");
			this.uiBorder.append(this.scfg.data);
			// 
			$.parser.parse(this.uiBorder);
		},
		
		// 创建工具条
		_createTbar : function () {
			var _this = this, data, fn;
			if (!this.options.toolbar) {
				return;
			}
			data = ["", "->", {
						id: "search", label: "查询", cls: "app-search-btn save", type: "button"
					}, {
						id: "reset", label: "重置", cls: "app-reset-btn cancel", type: "button"
					}];
			// 弹出查询区添加“关闭”按钮
			if (!this.options.isNested) {
				data.push({id: "close", label: "关闭", cls: "app-close-btn cancel", type: "button"})
			}
			data = data.concat(["->", ""]);
			// 工具条复写处理
			fn = this.options.toolbar;
			if (typeof fn === "function") {
				data = fn.apply(this, [data]);
			}
			this.uiTbar = $("<DIV class=\"app-bsearch-tbar\">").appendTo(this.uiBorder);			
			this.uiTbar.toolbar({
				onClick : function (e, data) { _this.clickTbar(e, data);},
				data : data
			});
		},

        // 添加回调方法——在构件开发库中增加回调方法名为（callback.name）callback为固定前缀
        addCallback : function (name, fn) {
            if (!this.callback) this.callback = {};
            this.callback[name] = fn;
        },

        // 添加输出参数——在构件开发库中增加方法名为（value.name）value为固定前缀
        addOutputValue : function (name, fn) {
            if (!this.value) this.value = {};
            this.value[name] = fn;
        },
		
		// 获取表单对应的列表
		getSelfGrid : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.grid),
			    sGrid = null, jqUI = null;
			if (storeId in data) {
				sGrid = data[storeId];
				jqUI = sGrid ? sGrid.jqUI : false;
			}
			return jqUI ? jqUI : false;
		},
		
		/**
		 * 获取过滤条件
		 * @returns
		 */
		getSearchFilter : function () {
			var formData = null, jqForm = $("form", this.element),
			    filter = "", p;
			if (!jqForm.form("valid")) return false;
			// 
			formData = jqForm.form("formData", false);
			for (p in formData) {
				if (isNotEmpty(formData[p])) {
					if (this.specReg.test(formData[p])) {
						CFG_message("【" + formData[p] + "】包含特殊字符：" + this.specMsg + "，请修改！");
						return false;
					}
					filter += (";" + p + CFG_cv_split + formData[p]);
				}
			}
			if (filter.length > 0) filter = filter.substring(1);
			
			return filter;
		},
		
		/**
		 * 查询
		 * @returns {Boolean}
		 */
		clickSearch : function() {
			var filter = "", selfGrid;
			filter = this.getSearchFilter();
			if (false === filter) return false;
			selfGrid = this.getSelfGrid();
			selfGrid.searchFilter = filter;
			selfGrid.load();
		},
		
		clickReset : function () {
			// 清空
			$("form", this.element).form("clear");
			// 禁用与树节点冲突的字段
			this._disableColumnsFilter();
		},
		
		generateId : function (prefix) {
			return prefix + "_" + this.options.tableId + "_" + this.uuid + "_" + this.options.number;
		},
		
		//
		clickTbar : function (event, ui) {
			if ("search" == ui.id) {
				this.clickSearch();
			} else if ("reset" == ui.id) {
				this.clickReset();
			} else if ("close" == ui.id) {
				this.close();
			}
		},
		
		// 关闭检索区
		close : function () {
			if (this.options.isNested) return ;
			this.element.hide();
		},
		
		// 打开检索区
		open : function () {
			if (this.options.isNested) return ;
			this.element.show();
			this._disableColumnsFilter();
		},
		
		// 获取action		
		getAction : function () {
			var fn = window[CFG_actionName(this.options.globalTimestamp)];
			if ($.isFunction(fn)) return fn(this);
			
			return $.config.appActionPrefix + "/show-module";
		},
		
		/**
		 * 获取URL中的参数值(如果参数名以“P_”打头的，如P_tableId，则key为tableId)
		 * @return Object
		 */
		getParamValue : function (key) {
			var data = $.config.getGlobalData(this.options.global),
			    val  = null;
			if (key in data) {
				val = data[key];
			} else {
				val = CFG_getInputParamValue(this.assembleData, key);
			}
			return val || "";
		},
		
		// 字段过滤条件转换为键值对({column:value,...})
		_columns2map : function () {
			var cols = {}, colArr, oneCol;
			if (isEmpty(this.options.columns)) return null;
			colArr = this.options.columns.split(";");
			for (var i = 0; i < colArr.length; i++) {
				oneCol = colArr[i];
				cols[oneCol.split(CFG_cv_split)[0].split(CFG_oc_split)[1]] = oneCol.split(CFG_cv_split)[1];
			}
			return cols;
		},
		
		// 基本索引禁用树节点对应的字段
		_disableColumnsFilter : function () {
			var cols  = this._columns2map();
			if (!cols) return;
			var elems = $("input[id$=\"_" + this.timestamp + "\"]", this.uiBorder);
			var jq, role, name, col;
			for (var i = 0, len = elems.length; i < len; i++) {
				jq = $(elems[i]);
			    role = jq.attr("component-role");
				if ($.inArray(role, $.parser.plugins) === -1) {
					continue;
				}
				name = jq[role]("option", "name");
				col =  name.split(CFG_oc_split)[1];
				if (col in cols) {
					jq[role]("setValue", cols[col]);
					jq[role]("disable");
				} else if (jq[role]("option", "disabled")) {
					jq[role]("enable");
				}
			}
		},
		
		// 根据字段名称获取表单元素ID的后缀
		getItemSuffixId : function (columnName) {
			return CFG_oc_split + columnName + "_" + this.timestamp;
		},
		
		/**
		 * 获取字段栏位对应的jquery对象(若同一字段有多个条件，则获取到多个元素；比如日期)
		 * @param columnName
		 *            -- 字段名称
		 */
		getItemJQ : function (columnName) {
			var suffix = this.getItemSuffixId(columnName);
			return $("input[id$=\"" + suffix + "\"]", this.uiBorder);
		},
		
		// 获取字段栏位对应的值
		getItemValue : function (columnName) {
			return this.callItemMethod(columnName, "getValue");
		},
		
		// 设值
		setItemValue : function (columnName, value) {
			this.callItemMethod(columnName, "setValue", value);
		},

		/**
		 * 添加表单元素事件，如onChange事件
		 * @param columnName
		 *            -- 字段名称
		 * @param eName
		 * 	          -- 事件名，如onChange
		 * @param fn
		 *            -- 事件方法，如onChange事件方法 function (e, data) {...}
		 */
		/*addItemEvent : function (columnName, eventName, fn) {
			this.setItemOption(columnName, eventName, fn);
		},*/
		
		/**
		 * 调用表单组件方法，
		 *   如disable方法：ui.callItemMethod("NAME", "disable");
		 *    setValue方法: ui.callItemMethod("NAME", "setValue", "qiucs");
		 * @param columnName
		 *            -- 字段名称
		 * @param method
		 *            -- 方法名
		 * @param args
		 *            -- 为方法(method)对应的参数列表（可选参数），如果method没有参数，则不需要传入
		 */
		callItemMethod : function (columnName, method, args) {
			var jq, role, retVal, slice = Array.prototype.slice;
			jq = this.getItemJQ(columnName);
			role = jq.attr("component-role");
			if (role && $.inArray(role, $.parser.plugins) > -1) {
				retVal = jq[role].apply(jq, slice.call(arguments, 1));
			}
			return retVal;
		},
		
		/**
		 * 获取options中的属性值
		 * @param columnName
		 *            -- 字段名称
		 * @param key
		 * @returns
		 */
		getItemOption : function (columnName, key) {
			var jq, role;
			jq = this.getItemJQ(columnName);
			role = jq.attr("component-role");
			if (role && $.inArray(role, $.parser.plugins) > -1) {
				return jq[role]("option", key);
			}
			return null;
		},
		
		/**
		 * 设置options中的属性值
		 * @param columnName
		 *            -- 字段名称
		 * @param key
		 * @param value
		 */
		setItemOption : function (columnName, key, value) {
			var jq, role;
			jq = this.getItemJQ(columnName);
			role = jq.attr("component-role");
			if (role && $.inArray(role, $.parser.plugins) > -1) {
				jq[role]("option", key, value);
			}
		},
		
		// 刷新
		refresh : function () {
			if (this.uiBorder) {
				this.uiBorder.remove();
			}
			this._create();
		},
		
		// 获取配置信息
		getConfigData : function () {
			return this.scfg;
		}
	});
	
	
	
	
	$.component("config.cgsearch", {
		version: $.config.version,
		options: {
			menuId : "-1", // 菜单ID
			componentVersionId : "-1",
			moduleId: "-1",
			tableId : null,
			number: 1,      // 当前列表在构件中的区域次序
			//master  : null, // {tableId: "xx", dataId: "yy"}
			global  : null,
			// 列表过滤条件格式，如：EQ_C_NAME≡xxx;LIKE_C_TITLE≡xxx；多个用英文分号(;)分隔
			// 三等于（≡）常量： CFG_cv_split； _C_常量：CFG_oc_split
			// 复杂条件：如   (name like '%qiu%' or birth_day=1983) and height > 168)
			// 则格式为 (((LIKE_C_NAME≡qiu)OR(EQ_C_BIRTH_DAY≡1983))AND(GT_C_HEIGHT≡168)) 注：每一项需要用括号括起来
			columns : null, 
			// 过滤条件 function(filter) {...;return filter;}
			//processFilter : null,
			sequence: 1, // 当前列表在构件中所有列表中的次序
			toolbar : true,
			statusbar: true,
			override : null
		},
		specReg : /[\%\^\_\<\>\'\[\]\(\)]/im, // `~!@#\$\%\^\&\*\(\)_\-\+\=\<\>\?:\"\{\},\.\\\/;\'\[\]
		specMsg : "\%\^\_\<\>\'\[\]\(\)",
		// 高级检索常量
		gsconst : {code:{and:"AND", or:"OR"}, name: {and:"并且", or:"或者"}, brakets: {left:"(", right:")"}, space: " "},
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	var globalTimestamp, override;
	    	if (!this.options.global) {
				this.options.global = this.element;
			}
	    	this.element.addClass("config-gsearch");
	    	globalTimestamp = $.config.getTimestampData(this.options.global);
	    	if (isEmpty(globalTimestamp)) {
	    		globalTimestamp = getTimestamp();
	    		$.config.setTimestampData(this.options.global, globalTimestamp);
	    	}
    	    // 构件的时间戳
    	    this.options.globalTimestamp = globalTimestamp;
	    	// 构件组装数据
	    	this.assembleData = $.config.getAssembleData(this.options.global);
	    	// override current search
	    	override = this.options.override;
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
	    	override = window[CFG_overrideName(globalTimestamp)];
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
	    	// 构件区域的时间戳
	    	this.timestamp = getTimestamp();	    	
	    	// 基本检索
	    	this._createSearch();
	    	// 事件绑定
	    	this.bindEvent();
	    	//
	    	this.inited = true;
		},
		
		bindEvent : $.noop,
		// 查询区配置信息
		
		// 基本检索
		_createSearch : function () {
			var jqLdiv = null, jqLfset = null,
			    jqCdiv = null, jqCfset = null,
			    jqRdiv = null, jqRfset = null,
			    _this   = this,jqBorder= null,
			    jqOuter  = $("<div class=\"coral-adjusted\"></div>"),
			    jqTbar;
			if (this.options.toolbar) {
				jqTbar  = $("<DIV class=\"toolbarsnav clearfix\"></DIV>")
				jqTbar.appendTo(this.element);
				this.uiTbar = $("<div>").appendTo(jqTbar).toolbar({
					data: [{
						type: "button",
						label: "检索",
						cls : "save",
						onClick: function () {_this.clickSearch();}
					}, {
						type: "button",
						label: "检索中查询",
						onClick: function () {_this.clickSearch(true);}
					}, {
						type: "button",
						label: "返回",
						onClick: function () {_this.close();}
					}]
				});
			}
			jqOuter.appendTo(this.element);
			// 左侧“高级检索”
			jqLdiv = $("<div class=\"app-search-great-left clearfix\"></div>").appendTo(jqOuter);
			jqLfset = $("<div>").subfield({title: "　　　　高级检索"}).appendTo(jqLdiv);
			jqBorder = $("<div class=\"app-search-great-border clearfix\">").appendTo(jqLdiv);
			$("<div class=\"app-search-great-colspan\">" +
					"<label class=\"appsearch-great-label\">字段：</label>" +
					"<input class=\"coralui-combobox element-gs-column-name\" data-options=\"name:&quot;columnName&quot;,placeholder:&quot;请选择字段&quot;,showClose:false,enableFilter:true\">" +
					"<div>").appendTo(jqBorder);
			
			$("<div class=\"app-search-great-colspan\">" +
					"<label class=\"appsearch-great-label\">条件：</label>" +
					"<input class=\"coralui-combobox element-gs-condition\" data-options=\"name:&quot;condition&quot;,method:&quot;get&quot;,placeholder:&quot;请选择字段&quot;,showClose:false,url:&quot;" + $.cuiFolder + "/common/jsp/data/condition.jsp&quot;\">" +
					"<div>").appendTo(jqBorder);
			
			$("<div class=\"app-search-great-colspan app-search-great-bottom\">" +
					"<label class=\"appsearch-great-label\">　值：</label>" +
					"<input class=\"coralui-textbox element-gs-value\" type=\"text\" data-options=\"name:&quot;value&quot;\">" +
					"<div>").appendTo(jqBorder);
			// 左侧-底部按钮
			jqTbar = $("<div class=\"app-search-great-tbar\"><div>").appendTo(jqLdiv);
			$("<div>").appendTo(jqTbar).toolbar({
				data: ["->",{
					label: "添加",
					type : "button",
					onClick: function () { _this._gsAdd();}
				}]
			});
			// 中间“已定义的查询条件”
			jqCdiv = $("<div class=\"app-search-great-center clearfix\" style=\"height:300px;\"></div>").appendTo(jqOuter);
			jqCfset = $("<div>").subfield({title: "已定义的查询条件"}).appendTo(jqCdiv);
			jqBorder = $("<div class=\"app-search-great-border clearfix\">" +
					"<select name=\"conditionlist\" multiple style=\"height:200px;\" class=\"app-search-great-multiple element-gs-conditionlist\"></select>" +
					"</div>").appendTo(jqCdiv);
			// 中间-底部按钮
			jqTbar = $("<div class=\"app-search-great-tbar\"><div>").appendTo(jqCdiv);
			$("<div>").appendTo(jqTbar).toolbar({
					data: ["->", {
						label: "删除",
						type: "button",
						onClick : function () { _this._gsDelete();}
					}, {
						label: "或者",
						type: "button",
						onClick : function () { _this._gsOr();}
					}, {
						label: "并且",
						type: "button",
						onClick : function () { _this._gsAnd();}
					}, {
						label: "组合",
						type: "button",
						onClick : function () { _this._gsMakeup();}
					}, {
						label: "拆分",
						type: "button",
						onClick : function () { _this._gsSplit();}
					}]
			});
	
			// 右侧 “历史查询条件”
			jqRdiv = $("<div class=\"app-search-great-right clearfix\"></div>").appendTo(jqOuter);
			jqRfset = $("<div>").subfield({title: "　　历史查询条件"}).appendTo(jqRdiv);
			
			jqBorder = $("<div class=\"app-search-great-border clearfix\">" +
					"<div class=\"app-search-great-colspan\">" +
					"<label class=\"appsearch-great-label\">检索条件：</label>" +
					"<input class=\"coralui-combobox element-gs-define-condition\" data-options=\"emptyText:&quot;请选择检索条件&quot;\">" +
					"</div>" + 
					"</div>").appendTo(jqRdiv);
			// 右侧-底部按钮
			jqTbar = $("<div class=\"app-search-great-tbar\"><div>").appendTo(jqRdiv);
			$("<div>").appendTo(jqTbar).toolbar({
				data: ["->", {
					label : "检索保存",
					type : "button",
					onClick : function () { _this._gsDefineSave(); }
				}, {
					label : "检索修改",
					type : "button",
					onClick : function () { _this._gsDefineUpdate(); }
				}, {
					label: "检索删除",
					type: "button",
					onClick : function () { _this._gsDefineDelete(); }
				}]
			});
			// 底部工具条
			if (this.options.statusbar) {
				jqTbar = $("<div class=\"app-bottom-toolbar clearfix\"></div>").appendTo(this.element);
				this.uiSbar = $("<DIV>").appendTo(jqTbar).toolbar({
					data: ["", "->", {
						type: "button",
						cls : "save",
						label: "检索",
						onClick: function () {_this.clickSearch();}
					}, {
						type: "button",
						label: "检索中查询",
						onClick: function () {_this.clickSearch(true);}
					}, {
						type: "button",
						label: "返回",
						onClick: function () {_this.close();}
					}, "->", ""]
				});
			}
			jqTbar = null;
			// 渲染组件
			$.parser.parse(this.element);
			// 初始化检索字段
			this._initSearchColumn();
			// 初始化当前用户历史检索条件
			this._initHistroyCondition();
			// 隐藏外部传递过来的过滤条件
			this._hideColumnsFilter();
			// 自适应
			$.coral.adjusted(jqOuter);
			// 
			$(":coral-toolbar", this.element).toolbar("refresh");
		},

		// 初始化检索字段
		_initSearchColumn : function () {
			var url = $.contextPath + "/appmanage/column-define!search.json?Q_EQ_searchable=1&Q_EQ_tableId=" + this.options.tableId,
				_this = this,
				cols, data, opts = [], i, jqCol, jqVal,
			    searchColumn = {};
			data = $.loadJson(url);
			if (data.data) cols = data.data;
			if (!cols.length)  return;
			for (i = 0; i < cols.length; i++) {
				opts.push({value: cols[i].columnName, text: cols[i].showName});
				searchColumn[cols[i].columnName] = {dataType: cols[i].dataType, codeTypeCode: cols[i].codeTypeCode, inputType: cols[i].inputType, dataTypeExtend: cols[i].dataTypeExtend, inputOption: cols[i].inputOption};
			}
			jqCol = $(".element-gs-column-name", this.element);
			jqVal = $(".element-gs-value", this.element);
			jqCol.combobox("loadData", opts);
			jqCol.combobox("option", "onChange", function(e, ui) {
				if (false === _this.processColumn(e, ui)) {
					// 由构件二次开发处理
					return;
				}
				var newCol = searchColumn[ui.newValue];
				if ($.isCoral(jqVal, "combobox")) {
					jqVal.combobox("destroy");
				} else if ($.isCoral(jqVal, "combotree")) {
					jqVal.combotree("destroy");
				} else if ($.isCoral(jqVal, "combogrid")) {
					jqVal.combogrid("destroy");
				} else if ($.isCoral(jqVal, "textbox")) {
					jqVal.textbox("destroy");
				} else {
					jqVal.datepicker("destroy");
				}
				jqVal.addClass("element-gs-value");
				if ("combobox" === newCol.inputType || "radio" === newCol.inputType
						|| "checkbox" === newCol.inputType) {
					jqVal.combobox({
						name : "value",
						url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&id=" + newCol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
					});
				} else if ("d" === newCol.dataType) {
					jqVal.datepicker({
						name : "value",
						dateFormat : (newcol.dataTypeExtend ? newCol.inputOption : "yyyy-MM-dd HH:mm:ss"),
						srcDateFormat : (newcol.inputOption ? newCol.dataTypeExtend : "yyyy-MM-dd HH:mm:ss"),
					});
				} else if ("combotree" === newCol.inputType) {
					jqVal.combotree({
						name : "value",
						popupDialog:true,
						url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combotree&id=" + newCol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
					});
				} else if ("combogrid" === newCol.inputType) {
					jqVal.combogrid($.loadJson(_this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combogrid&id=" + newCol.codeTypeCode + "&P_tableId=" + _this.options.tableId + "&P_menuCode=" + _this.getParamValue("menuCode")));
				} else {
					jqVal.textbox({
						name : "value",
						validType: ("n" === newCol.dataType ? "float" : null),
						maxlength: newCol.length
					});
				}
			});
		},
		// 隐藏外部传递过来的过滤条件（如树的字段节点）
		_hideColumnsFilter : function () {
			if (!this.initGS) return;
			var cols = this._columns2map(),
			    jq = $(".element-gs-column-name", this.element);
			jq.combobox("showOption");
			if (isEmpty(cols)) return;	
			$.each(cols, function(p, v) {
				jq.combobox("hideOption", p);
			});
		},
		
		// 初始化当前用户历史检索条件
		_initHistroyCondition : function () {
			var jqDc = $(".element-gs-define-condition", this.element),
			    url  = $.contextPath + "/appmanage/app-search-condition!search.json?E_frame_name=coral" +
					"&F_in=id,name,condition&Q_EQ_componentVersionId=" + this.options.componentVersionId + "&Q_EQ_tableId=" + this.options.tableId,
			    data = $.loadJson(url),
			    _this = this, 
			    opts = [], cons = {}, i = 0, one;
			if (!data || !data.data) return;
			for (; i < data.data.length; i++) {
				one = data.data[i];
				opts.push({value: one.id, text: one.name});
				cons[one.id] = one.condition;
			}
			jqDc.data("all-condition", cons);
			jqDc.combobox("loadData", opts);
			jqDc.combobox("option", "onChange", function(e, ui) {
				var nv = ui.newValue, jqSel, con, optArr, oneOpt, i = 0;
				if (isEmpty(nv)) return;
				con = jqDc.data("all-condition")[nv];
				if (isEmpty(con)) return;
				jqSel = $(".element-gs-conditionlist", _this.element);
				jqSel.empty();
				optArr = con.split(";");
				for (; i < optArr.length; i++) {
					oneOpt = optArr[i].split(",");
					jqSel.append("<option value=\"" + oneOpt[0] + "\">" + oneOpt[1] + "</option>");
				}
			});
		},
		// 高级检索-添加
		_gsAdd : function () {
			var jqCol = $(".element-gs-column-name", this.element),
			    jqCon = $(".element-gs-condition", this.element),
			    jqVal = $(".element-gs-value", this.element), 
			    jqSel = $(".element-gs-conditionlist", this.element),
			    val, txt, optVal, optTxt;
			if (isEmpty(jqCol.combobox("getValue"))) {
				CFG_message("请选择字段！", "warning"); return;
			}
			if ($.isCoral(jqVal, "textbox"))  {
				val = jqVal.textbox("getValue");
				txt = val;
			} else if ($.isCoral(jqVal, "combobox")) {
				val = jqVal.combobox("getValue");
				txt = jqVal.combobox("getText");
			} else if ($.isCoral(jqVal, "combotree")) {
				val = jqVal.combotree("getValue");
				txt = jqVal.combotree("getText");
			} else {
				val = jqVal.datepicker("option", "value");
				txt = val;
			}
            if (isEmpty(val)) {
            	CFG_message("请输入值！", "warning");  return;
            }
            if (this.specReg.test(val)) {
            	CFG_message("值不能包含特殊字符：" + this.specMsg + "，请修改！"); 
            	return;
            }
            optVal = this.gsconst.brakets.left + jqCon.combobox("getValue") + CFG_oc_split + jqCol.combobox("getValue")
                    + CFG_cv_split + val + this.gsconst.brakets.right;
            optTxt = this.gsconst.brakets.left + jqCol.combobox("getText") + this.gsconst.space + jqCon.combobox("getText")
                    + this.gsconst.space + txt + this.gsconst.brakets.right;
            if (jqSel.find("option").length > 0) {
                optVal = this.gsconst.code.and + optVal;
                optTxt = this.gsconst.name.and + optTxt;
            }
            jqSel.append("<option value=\"" + optVal + "\">" + optTxt + "</option>");
		},
		// 操作检查
		_gsCheck : function (jqSel) {
			if (0 === jqSel.find("option").length) {
				CFG_message("条件列表中没有条件，请先添加条件！", "warning"); return false;
			}
			if (0 === jqSel.find("option:selected").length) {
				CFG_message("请先选择条件，再操作！", "warning"); return false;
			}
			return true;
		},
		// 高级检索-并且
		_gsAnd : function () {
			var jqSel = $(".element-gs-conditionlist", this.element),
			    _this = this;
            if (false === this._gsCheck(jqSel))  return;
            jqSel.find("option:selected").each(function() {
            	var opt = this, con =_this.gsconst;
            	if (0 == opt.value.indexOf(con.code.or)) {
                    opt.value = opt.value.replace(con.code.or, con.code.and);
                }
                if (0 == opt.text.indexOf(con.name.or)) {
                    opt.text = opt.text.replace(con.name.or, con.name.and);
                }
            });
		},
		// 高级检索-或者
		_gsOr  : function () {
			var jqSel = $(".element-gs-conditionlist", this.element),
		        _this = this;
            if (false === this._gsCheck(jqSel))  return;
            jqSel.find("option:selected").each(function() {
            	var opt = this, con =_this.gsconst;;
            	if (0 == opt.value.indexOf(con.code.and)) {
                    opt.value = opt.value.replace(con.code.and, con.code.or);
                }
                if (0 == opt.text.indexOf(con.name.and)) {
                    opt.text = opt.text.replace(con.name.and, con.name.or);
                }
            });
		},
		// 高级检索-删除
		_gsDelete : function () {
			var jqSel = $(".element-gs-conditionlist", this.element),
			    con   = this.gsconst, fOpt, idx;
            if (false === this._gsCheck(jqSel)) return;
            idx = jqSel.find("option:selected").index();
            jqSel.find("option:selected").remove();
            if (0 === idx) {
            	fOpt = jqSel.find("option:first");
            	if (0 === fOpt.length) return;
            	if (0 == fOpt.text().indexOf(con.name.and)) {
            		fOpt.val(fOpt.val().replace(con.code.and, ""));
                    fOpt.html(fOpt.text().replace(con.name.and, ""));
                } else if (0 == fOpt.text().indexOf(con.name.or)) {
                	fOpt.val(fOpt.val().replace(con.code.or, ""));
                	fOpt.html(fOpt.text().replace(con.name.or, ""));
                }
            }
		},
		// 高级检索-组合
		_gsMakeup : function () {
			var jqSel = $(".element-gs-conditionlist", this.element),
			    con   = this.gsconst,
			    idx = -1, val = "", txt = "", isFirst = true, jqOpt;
            if (false === this._gsCheck(jqSel)) return;
            jqOpt = jqSel.find("option:selected");
            if (jqOpt.length < 2) {
            	CFG_message("至少要选择两个或两个以上的条件，才能进行组合！", "warning");
            	return;
            }
            jqSel.find("option:selected").each(function () {
            	var jq = $(this);
                val += jq.val();
                txt += jq.text();
            	if (!isFirst) {
            		jq.remove();
            	} else {
                    isFirst = false;
                    if (0 == val.indexOf(con.code.and)) {
                        val = val.replace(con.code.and, "");
                    } else if (0 == val.indexOf(con.code.or)) {
                        val = val.replace(con.code.or, "");
                    }
                    if (0 == txt.indexOf(con.name.and)) {
                        txt = txt.replace(con.name.and, "");
                    } else if (0 == txt.indexOf(con.name.or)) {
                        txt = txt.replace(con.name.or, "");
                    }
            	}
            });
            idx = jqOpt.index();
            val = (con.brakets.left + val + con.brakets.right);
            txt = (con.brakets.left + txt + con.brakets.right);
            if (idx > 0) {
            	val = (con.code.and + val);
                txt = (con.name.and + txt);
            }
            jqOpt.html(txt).val(val);
		},
		// 高级检索-拆分
		_gsSplit : function () {
			var jqSel = $(".element-gs-conditionlist", this.element),
			    con   = this.gsconst,
			    idx = -1, val = "", txt = "", isFirst = true, jqOpt;
	        if (false === this._gsCheck(jqSel)) return;
	        jqSel.find("option:selected").each(function () {
	        	var jq  = $(this),
	        	    val = jq.val(), txt = jq.text();
	        	if (txt.indexOf("((") < 0) return;
                val = val.replace(/\({2,}/g, "(").replace(/\){2,}/g, ")");
                txt = txt.replace(/\({2,}/g, "(").replace(/\){2,}/g, ")");
	            while (true) {
	                var vIdx   = val.indexOf(")"),
	                    tIdx   = txt.indexOf(")"),
	                    oneVal = val.substring(0, vIdx + 1), 
	                    oneTxt = txt.substring(0, tIdx + 1);
	                $("<option value=\"" + oneVal + "\">" + oneTxt + "</option>").insertBefore(jq);
	                if (vIdx == (val.length - 1))
	                    break;
	                val = val.substring(vIdx + 1);
	                txt = txt.substring(tIdx + 1);
	            }
	            jq.remove();
	        });
		},
		// 高级检索-保存已定义的条件
		_gsDefineSave : function () {
			var jq    = $("<div>").appendTo(this.options.global),
			    jqOpt = $(".element-gs-conditionlist", this.element).find("option"),
			    _this = this,
			    jqGlobal = this.getDialogAppendTo();
			if (jqOpt.length === 0) {
				CFG_message("请先定义检索条件！", "warning");
				return;
			}
			jq.dialog({
				appendTo : jqGlobal,
				title : "新增检索条件",
				modal : true,
				width: 300,
				height: 200,
				position : {
					of : jqGlobal
				},
				onOpen : function () {
					$("<div style=\"margin-top:20px;\">" +
							"<label style=\"width:80px;text-align:right;\">检索名称：</label>" +
							"<input type=\"text\" class=\"coralui-textbox element-gs-condition-name\" data-options=\"required:true\" width=\"180\">" +
					  "</div>").appendTo(jq);
					// 
					$.parser.parse(jq);
				},
				onClose : function () {
					jq.remove();
				},
				buttons : [{
					text : "保存",
					click : function () {
						var condition = "", url, data, rlt,
						    jqCn = $(".element-gs-condition-name", jq);
						// 
						if (!jqCn.textbox("valid")) return;
						data = {tableId: _this.options.tableId, componentVersionId: _this.options.componentVersionId, condition: null, name: jqCn.textbox("getValue")};
						//
						url = $.contextPath + "/appmanage/app-search-condition!checkUnique.json?Q_EQ_tableId=" + data.tableId +
								"&Q_EQ_componentVersionId=" + data.componentVersionId + "&Q_EQ_name=" + data.name;
						rlt = $.loadJson(encodeURI(url));
						if (!rlt.success) {
							CFG_message("检索名称重复，请修改！", "warning"); return;
						}
						jqOpt.each(function() {
							condition += ";" + this.value + "," + this.text;
						});
						data.condition = condition.substring(1);
						url = $.contextPath + "/appmanage/app-search-condition!save.json";
						$.ajax({
							url : encodeURI(url),
							type : "post",
							data : data,
							dataType : "json",
							success : function(entity) {
								var jqDc = $(".element-gs-define-condition", _this.element);
								jqDc.combobox("addOption", {value: entity.id, text: entity.name});
								jqDc.combobox("setValue", entity.id);
								jqDc.data("all-condition")[entity.id] = entity.condition;
								jq.dialog("close");
								CFG_message("保存成功！", "success");
							},
							error : function () { CFG_message("保存失败！", "error"); }
						});
					}
				}, {
					text : "关闭",
					click : function () {
						jq.dialog("close");
					}
				}]
			});
		},
		// 高级检索-修改已定义的条件
		_gsDefineUpdate : function () {
		    var jqOpt = $(".element-gs-conditionlist", this.element).find("option"),
		        jqDc  = $(".element-gs-define-condition", this.element), 
		        condition = "", data;
			if (jqOpt.length === 0) {
				CFG_message("请先定义检索条件！", "warning"); return;
			}
			
			jqOpt.each(function() {
				condition += ";" + this.value + "," + this.text;
			});
			data = {
					tableId: this.options.tableId, 
					componentVersionId: this.options.componentVersionId, 
					condition: condition.substring(1), 
					id : jqDc.combobox("getValue"),
					name: jqDc.combobox("getText")
					};
			if (isEmpty(data.id)) {
				CFG_message("请先选择具体检索条件！", "warning"); return;
			}
			$.ajax({
				url : $.contextPath + "/appmanage/app-search-condition!save.json",
				type : "post",
				data : data,
				dataType : "json",
				success : function(entity) {
					jqDc.data("all-condition")[entity.id] = entity.condition;
					CFG_message("操作成功！", "success");
				},
				error : function () { CFG_message("操作失败！", "error"); }
			});
		},
		// 高级检索-删除已定义的条件
		_gsDefineDelete : function () {
			var jqDc  = $(".element-gs-define-condition", this.element), 
		        condition = "", data;
			data = {id : jqDc.combobox("getValue")};
			if (isEmpty(data.id)) {
				CFG_message("请先选择具体检索条件！", "warning"); return;
			}
			$.ajax({
				url : $.contextPath + "/appmanage/app-search-condition!destroy.json",
				type : "post",
				data : data,
				dataType : "json",
				success : function(entity) {
					delete jqDc.data("all-condition")[data.id];
					jqDc.combobox("setValue", "");
					jqDc.combobox("removeOption", data.id);
					CFG_message("操作成功！", "success");
				},
				error : function () { CFG_message("操作失败！", "error"); }
			});
		},
		// 获取表单对应的列表
		getSelfGrid : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.grid),
			    sGrid = null, jqUI = null;
			if (storeId in data) {
				sGrid = data[storeId];
				jqUI = sGrid ? sGrid.jqUI : false;
			}
			return jqUI ? jqUI : false;
		},
		
		/**
		 * 获取过滤条件
		 * @returns {String}
		 */
		getSearchFilter : function () {
			var jqOpt = $(".element-gs-conditionlist", this.element).find("option"),
	        	filter = "";
			if (jqOpt.length === 0) {
				filter = false;
			} else {
				jqOpt.each(function() {
					filter += this.value;
				});
			}
			return filter;
		},
		
		// 执行高级检索
		clickSearch : function (inResult) {
			var filter = this.getSearchFilter(),
	        	selfGrid = this.getSelfGrid();
			if (false === filter) {
				CFG_message("请先添加条件再查询！", "warning"); 
				return;
			}
			if (inResult && isNotEmpty(selfGrid.searchFilter)) {
				selfGrid.searchFilter = selfGrid.searchFilter + (isEmpty(filter) ? "" : this.gsconst.code.and) + filter;
			} else {
				selfGrid.searchFilter = filter;
			}
			selfGrid.load();
			// close great search
			this.close();
		},
		
		// 
		close : function () {
			// 清空
			this.element.remove();
			if (this.assembleData && this.assembleData.CFG_navigationBar) {
			    this.assembleData.CFG_navigationBar.goBack(this.assembleData.CFG_navigationBar.getSize()-1, true);
			}
		},
		
		// 获取action		
		getAction : function () {
			var fn = window[CFG_actionName(this.options.globalTimestamp)];
			if ($.isFunction(fn)) return fn(this);
			
			return $.config.appActionPrefix + "/show-module";
		},
		
		/**
		 * 获取URL中的参数值(如果参数名以“P_”打头的，如P_tableId，则key为tableId)
		 * @return Object
		 */
		getParamValue : function (key) {
			var data = $.config.getGlobalData(this.options.global),
			    val  = null;
			if (key in data) {
				val = data[key];
			} else {
				val = CFG_getInputParamValue(this.assembleData, key);
			}
			return val || "";
		},
		
		// 字段过滤条件转换为键值对({column:value,...})
		_columns2map : function () {
			var cols = {}, colArr, oneCol;
			if (isEmpty(this.options.columns)) return null;
			colArr = this.options.columns.split(";");
			for (var i = 0; i < colArr.length; i++) {
				oneCol = colArr[i];
				cols[oneCol.split(CFG_cv_split)[0].split(CFG_oc_split)[1]] = oneCol.split(CFG_cv_split)[1];
			}
			return cols;
		},
		
		generateId : function (prefix) {
			return prefix + "_" + this.options.tableId + "_" + this.uuid + "_" + this.options.number;
		},
		
		// 刷新
		refresh : function () {
			if (this.uiBorder) {
				this.uiBorder.remove();
			}
			this._create();
		},
		
		/**
		 * @param e
		 * @param ui
		 * @returns {Boolean}
		 */
		processColumn : function (e, ui) {
			return true;
		},
		getDialogAppendTo : function () {
			//CFG_getDialogParent(_this.assembleData) || $(this.options.global)
			//return CFG_getDialogParent(this.assembleData) || $("body");
			return $("body");
		}
	});

})( jQuery );
