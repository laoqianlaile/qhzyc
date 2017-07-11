/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成配置列表JS
 * 
 * 依赖JS:
 *    
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.cgrid", {
		version: $.config.version,
		options: {
			menuId : "-1", // 菜单ID
			componentVersionId : "-1",
			moduleId: "-1",
			tableId : null,
			workflowId : null, // 工作流ID
			box     : null, // 工作箱
			number: 1,      // 当前列表在构件中的区域次序
			model : 1, // 1-普通列表 2-缩略图列表
			thumbnail : 0, // 0-页面打开时默认显示列表 1-页面打开时默认显示缩略图
			sort : 0, // 0-不支持拖动排序 1-支持拖动排序
			isAlone : false,// 是否独立使用：true-独立使用与主列表无关系，false-不是独立使用，如果有主列表，则主列表有关系
			master  : null, // {tableId: "xx", dataId: "yy"}
			toolbar : true, // false 表示不需要工具条
			global  : null,
			// 列表过滤条件格式，如：EQ_C_NAME≡xxx;LIKE_C_TITLE≡xxx；多个用英文分号(;)分隔
			// 三等于（≡）常量： CFG_cv_split； _C_常量：CFG_oc_split
			// 复杂条件：如   (name like '%qiu%' or birth_day=1983) and height > 168)
			// 则格式为 (((LIKE_C_NAME≡qiu)OR(EQ_C_BIRTH_DAY≡1983))AND(GT_C_HEIGHT≡168)) 注：每一项需要用括号括起来
			columns : null, 
			// 过滤条件 function(filter) {...;return filter;}
			processFilter : null,
			sequence: 1, // 当前列表在构件中所有列表中的次序
			/**
			 * 用new $.config.cgrid(opt, jq)时，复写自身
			 * function() {
			 *     var cgrid = this;
			 *     cgrid.beforeInitGrid = function(setting) {
			 *         ...
			 *         return setting;
			 *     };
			 * }
			 * */
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
	    	this.element.addClass("config-grid");
	    	globalTimestamp = $.config.getTimestampData(this.options.global);
	    	if (isEmpty(globalTimestamp)) {
	    		globalTimestamp = getTimestamp();
	    		$.config.setTimestampData(this.options.global, globalTimestamp);
	    	}
	    	// console.log("cgrid timestamp: " + globalTimestamp);
    	    // 构件的时间戳
    	    this.options.globalTimestamp = globalTimestamp;
	    	// 构件组装数据
	    	this.assembleData = $.config.getAssembleData(this.options.global);
	    	// override current grid
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
	    	// 列表配置信息，当为流程列表时，this.gcfg.tableId为对应的视图ID，此时与options.tableId不一致
	    	this.gcfg = this._getGridCfg();
	    	// 工具条
	    	this._createTbar();
	    	// 列表
	    	this._createGrid();
	    	// 标记已被初始化过了
	    	this.inited = true;
	    	// 默认输出的回调方法与参数方法
	    	this._defaultOutput();
		},
		// 销毁功能
		_destroy: function () {
			CFG_removeJQ(this.component());
		},
		
		// 基本检索
		_createBaseSearch : function (pos) {
			var top   = this.tbarPanel.height() - 1,
			    panel = $("<div class=\"panelSearch\"></div>");			
			
			panel.insertAfter(this.element);
			panel.css("overflow", "hidden");
			panel.css("max-height", this.element.height() - 50);
			panel.css("top", top);
			
			this.bsearchUI = new $.config.cbsearch({
				menuId : this.options.menuId, // 菜单ID
				componentVersionId : this.options.componentVersionId,
				moduleId: this.options.moduleId,
				tableId : this.options.tableId,
				workflowId : this.options.workflowId, // 工作流ID
				box     : this.options.box,      // 工作箱
				number: this.options.number,     // 当前列表在构件中的区域次序
				isNested : false,
				//master  : this.options.master, // {tableId: "xx", dataId: "yy"}
				global  : this.options.global,
				columns : this.options.columns, 
				sequence: this.options.sequence  // 当前列表在构件中所有列表中的次序
			}, panel);
			// 基本检索初始化标记
			//this.initBS = true;
		},
		
		// 高级检索
		_createGreatSearch : function () {
			var jq = this.getNestedJq();
            // 向导航条添加导航信息
            CFG_addNavigationItem(this.assembleData, jq, "高级检索");
			new $.config.cgsearch({
				menuId : this.options.menuId, // 菜单ID
				componentVersionId : this.options.componentVersionId,
				moduleId: this.options.moduleId,
				tableId : this.gcfg.tableId,  // 注： 由于工作流是查询对应的视图表
				workflowId : this.options.workflowId, // 工作流ID
				box     : this.options.box, // 工作箱
				number: this.options.number,      // 当前列表在构件中的区域次序
				global  : this.options.global,
				columns : this.options.columns, 
				sequence: this.options.sequence // 当前列表在构件中所有列表中的次序
				
			}, jq);
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
		
		// 打开基本检索
		_openBS : function () {
			// if disabled, return
			if (this.options.disabled) return;
			if (!this.bsearchUI) {
				this._createBaseSearch();
				if (!this.bsearchUI.inited) {
					this.bsearchUI.close();
				} 
			} else if (!this.bsearchUI.inited) {
				CFG_message("基本检索未配置！", "warning");
			} else {
				this.bsearchUI.open();
			}
		}, 
		
		// 关闭基本检索
		_closeBS : function () {
			// if disabled, return
			if (this.options.disabled) return;
			if (this.bsearchUI) {
				this.bsearchUI.close();
			}
		},
		
		// 执行一体化查询
		executeIS : function (value) {
			// if disabled, return
			if (this.options.disabled || this.specReg.test(value)) {
				CFG_message("关键字不能包含特殊字符：" + this.specMsg + "，请修改！");
				return;
			}
			var cols = this.integrationBeans, i, len, codeTypeCode, j, codeLen, code, oldItem, newItem;
			if (isEmpty(value)) {
				this.searchFilter = "";
			} else {
				if (isEmpty(this.integrationFilter)) {
					CFG_message("一体化检索未配置!", "warning");
					return;
				}
				this.searchFilter = this.integrationFilter.replace(/\{1\}/g, value);
				for (i = 0, len = cols.length; i < len; i ++) {
					codeTypeCode = cols[i].codeTypeCode;
					if (isNotEmpty(codeTypeCode)) {
						codeLen = cols[i].codes ? cols[i].codes.length : 0;
						for (j = 0; j < codeLen; j++) {
							code = cols[i].codes[j];
							if (code.name === value) {
								this.searchFilter = this.searchFilter.replace("{" + codeTypeCode + "}", code.value);
								break;
							}
						}
						oldItem = ("EQ"   + CFG_oc_split + cols[i].columnName + CFG_cv_split + "{" +　codeTypeCode +　"}");
						newItem = ("LIKE" + CFG_oc_split + cols[i].columnName + CFG_cv_split +  value);
						this.searchFilter = this.searchFilter.replace(oldItem, newItem);
					}
				}
			}
			this.load();
		},
		
		// 打开高级检索
		_openGS : function () {
			//  if disabled, return
			if (this.options.disabled) return;
			// 关闭基本检索
			this._closeBS();
			// 打开高级检索
			this._createGreatSearch();
		},
		
		// 打开用户个性定义
		_openST : function () {
			//  if disabled, return
			if (this.options.disabled) return;
			// 关闭基本检索
			this._closeBS();
			// 打开个性设置
			var url = $.contextPath + "/cfg-resource/coral40/common/jsp/appdefine/CFG_appdefine.jsp?tableId=" + this.options.tableId
			          + "&componentVersionId=" + this.options.componentVersionId
			          + "&menuId=" + this.options.menuId
			          + "&cgridDivId=" + this.element.attr("id")
			          + "&idSuffix=" + this.options.globalTimestamp,
			    _this = this; 
			CFG_openComponent(this.assembleData, url, "设置");
		},
		
		// 工具条
		_createTbar : function () {
			if (!this.options.toolbar) return;
			var _this = this,
				rtbar = $("<span class=\"toolbarsnav-right\"></span>"),
				ctbar, comboData, rightData;
			this.tbarPanel = $("<DIV id=\"" + this.tbarPanelId() + "\" class=\"toolbarsnav clearfix\"></DIV>").appendTo(
					this.element
					);
			
			this.uiTbar = $("<DIV id=\"" + this.tbarId() + "\">").appendTo(this.tbarPanel);
			ctbar = new $.config.ctbar ({
				menuId  : this.options.menuId,
				moduleId: this.options.moduleId,
				componentVersionId : this.options.componentVersionId,
				tableId : this.options.tableId,
				workflowId : null2empty(this.options.workflowId),
				box : null2empty(this.options.box),
				type    : $.config.contentType.grid,
				model : this.options.model,
				number  : this.options.number,
				global : this.options.global,
				onClick : function (event, ui) {
					_this.clickTbar(event, ui);
				},
				processData : function (data, pos) {
					// 组装类型： 0--弹出；1--嵌入
					var assembleType = _this.getParamValue("assembleType"), 
					    idx = $.inArray("->", data), fn;
					// 全局默认的工具条按钮处理方法
					fn = $.fn["ctbar"].defaults.processData;
					if (typeof fn === "function") {
						data = fn.apply(this, [data, pos]);
					}
					// 工具条处理
					fn = _this.options.toolbar;
					if (typeof fn === "function") {
						data = fn.apply(_this, [data, pos]);
					}
					
					if ("1" == assembleType && 1 === _this.options.number) {
						if (idx === -1) {
							data.push({id: "back2grid", label: "返回", type: "button"});
						} else {
							data.splice(idx, 0, {id: "back2grid", label: "返回", type: "button"});
						}
					} 
					
					_this._processRightToolbar(data);
					
					return data;
				} //*/
			});
			// 
			ctbar.render(this.uiTbar, "top");
			// this.options.sequence : 当前列表在所有列表中的序号
			if (!this.inited && this.options.sequence !== 1 && !this.isRefresh) {
				this.disableToolbar();
			}
		},
		// 右边的工具条处理（下拉框查询、基本查询、高级查询、设置、缩略图切换）
		_processRightToolbar : function (data) {
			var _this = this, idx = $.inArray("->", data), len = data.length,
				i, item;
			
			if (-1 === idx && $.config.contentType.card !== this.options.model) {
				return ;
			}
			for (i = len - 1; i > idx; i--) {
				item = data[i];
				if (item.id === "COMBOBOX_SEARCH") {
					// 下拉框检索
					data.splice(i, 1);
					if (item.label) {
						data.splice(i, 0, {
							"id": ("COMBOBOX_LABEL_" + _this.timestamp),
							"type": "html",
							"frozen" : true,
							"content": item.label
						});
					}
					if (item.columnName) {
						data.splice(item.label ? i + 1 : i, 0, {
							"id": ("COMBOBOX_SEARCH_" + _this.timestamp),
							"type": "combobox",	
							"frozen" : true,
							"name": item.columnName,
							"data": item.options,
							"onChange": function (e, data) {
								_this.comboboxSearch(e, data);
							}
						});
					}
				} else if (item.id === "integrationSearch") {
					// 一体化查询框（integration）
					if (typeof this.integrationFilter === "undefined") {
						var url = $.contextPath + "/appmanage/column-define!search.json?Q_EQ_phraseable=1&Q_EQ_tableId=" + this.gcfg.tableId,
						colData = $.loadJson(url),
						cols = colData.data ? colData.data : [],
								j = 0, clen = cols.length, codeTypeCode, showName, 
								filter = "", integrationCols = "", integrationColDetail = "";
						this.integrationBeans = cols;
						for (; j < clen; j++) {
							codeTypeCode = cols[j].codeTypeCode;
							if (isNotEmpty(codeTypeCode)) {
								filter += "OR(" + "EQ" + CFG_oc_split + cols[j].columnName + CFG_cv_split + "{" +　codeTypeCode +　"})";
							} else {
								filter += "OR(" + "LIKE" + CFG_oc_split + cols[j].columnName + CFG_cv_split + "{1})";
							}
							showName = this._getGridHeaderName(cols[j].columnName, cols[j].showName);
							integrationColDetail += "|" + showName;
							integrationCols += "|" + showName;
						}
						if (integrationCols.length > 15) {
							integrationCols = integrationCols.substring(0, 14) + "...";
						}
						if (filter.length > 0) filter = "(" + filter.substring(2) + ")";
						this.integrationFilter = filter;
						this.integrationCols = integrationCols;
						this.integrationColDetail = integrationColDetail.substring(1);
					}
					data.splice(i, 1, {
						"type" : "textbox",
						"frozen" : true,
						width: 200,
						title: this.integrationColDetail,
						placeholder: this.integrationCols ? this.integrationCols.substring(1) : "一体化检索未配置",
								icons: [{icon: "icon-search3", click: function() {
									_this.executeIS(this.value);
								}}],
								onKeyDown: function(e, ui) {
									var keycode = e.which;
									if (keycode == 13) {
										e.preventDefault ? e.preventDefault() : (e.returnValue = false);
										_this.executeIS(this.value);
									}
								}
					});
				} else if (item.id === "baseSearch") {
					// 基本检索
					data.splice(i, 1, {
						"type" : "button",
						"label" : "基本检索",
						"frozen" : true,
						"text" : false,
						"cls"  :  "btnSearch",
						"icons"  :  "icons-search",
						"onClick" : function () {
							_this._openBS($(this));
						}
					});
				} else if (item.id === "greatSearch") {
					// 高级检索
					data.splice(i, 1, {
						"type" : "button",
						"label" : "高级检索",
						"frozen" : true,
						"text" : false,
						"cls"  :  "btnHighSearch",
						"icons"  :  "icons-search",
						"onClick" : function () {
							_this._openGS();
						}
					});
				} else if (item.id === "setting") {
					// 设置
					data.splice(i, 1, {
						"type" : "button",
						"label" : "设置",
						"frozen" : true,
						"text" : false,
						"cls"  :  "btnSetting",
						"icons"  :  "icons-setting",
						"onClick" : function () {
							_this._openST();
						}
					});
				}
			}
			// 缩略图切换
			if ($.config.contentType.card == this.options.model) {
				if (idx === -1) {
					data.push("", "->");
				}
				data.push({
					"type" : "button",
					"frozen" : true,
					"label" : "缩略图列表与文本列表切换",
					"text" : false,
					"cls"  : ("1" == this.options.thumbnail ? "app-toolbar-txtgrid" : "app-toolbar-picgrid" ),
					"icons"  :  "icons-switch",
					"onClick" : function() {
						_this.clickSwitchGridModel($(this));
					}
				});
			}
		},
		// 根据字段名称获取表头显示名称
		_getGridHeaderName : function(columnName, showName) {
			var columns = this.gcfg.columns, names = this.gcfg.colNames, idx;
			idx = $.inArray(columnName, columns);
			if (idx > -1) return names[idx];
			return showName;
		},
		// 列表区
		_createGrid : function () {
			var _this = this, 
			    filter= this._getSearchFilter(),
			    url   = this._getBaseUrl(),
			    setting  = {
					height: "auto",
					rownumbers: this.gcfg.hasRowNumber,
					width : "auto",
					model: "grid",
					url   : url,
					postData : filter,
					multiselect : this.gcfg.multiselect,
					singleselect : this.gcfg.singleselect,
					cellEdit : this.gcfg.cellEdit,
					editableRows : this.gcfg.editableRows,
					clicksToEdit : this.gcfg.clicksToEdit,
					colNames: this.gcfg.colNames,
					colModel: this.gcfg.colModel,
					fitStyle: "fill",//
					shrinkToFit: this.gcfg.adaptive, // false 列不自适应
					forceFit: true,
					rowList : this.getPageSizeList(),
					rowNum : (this.gcfg.pageable ? this.getPageSize() : 999999),  // pageSize  
					datatype: this.getDataType(filter),
					sortable: true,
					onSelectRow: function(e, ui) {
						_this.eSelectRow(e, ui);
					},
					onDblClickRow : function (e, ui) {
						_this.eDblClickRow(e, ui);
					},
					onResizeStop : function (e, ui) {
						if (0 == _this.gcfg.headerSetting) return;
						_this._setUserWidths(e, ui);
					},
					onSortableColumns : function (e, ui) {
						if (0 == _this.gcfg.headerSetting) return;
						_this._setUserHeaders(e, ui);
					}
				},
				i, len, url, idx;
			// 列表DIV
			this.uiGrid = $("<DIV id=\"" + this.gridId() + "\"></DIV>").appendTo(this.element);
			// 操作列预留区
			if (this.gcfg.linkIndex > -1) {
				setting.colModel[this.gcfg.linkIndex].formatter = function(value, options, rowObj) {
					var id = rowObj[_this.gcfg.keyColumn],
					    cgridDivId = _this.element.attr("id");
					return _this.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id);
				};
			}
			// 链接列
			if (this.gcfg.urls) {
				for (i = 0, len = this.gcfg.urls.length; i < len; i++) {
					url = this.gcfg.urls[i];
					setting.colModel[url.idx].formatter = function(value, options, rowObj) {
						var id = rowObj[_this.gcfg.keyColumn], cgridDivId = _this.element.attr("id");
						return ("<a style=\"color:#428bca;\" href=\"javascript:void(0);\" onclick=\"MT_GRID_link('" +
									url.content + "','" + id +"','" + cgridDivId + "');return false;\">"+ value + 
								"</a>");
					};
				}
			}
			
			// 缩略图列表设置
			if ($.config.contentType.card == this.options.model) {				
				setting.picTemplate = function (rData, ni, multi) {
					var itemJson = {}, itemData = [],
					    ccols = _this.gcfg.cardColumns, 
					    url   = _this.getThumbnailUrl(rData, ni, multi),
					    i     = 0;
					
					if (ccols && ccols.length > 0) {
						for (i = 0; i < ccols.length; i++) {
							itemData.push("<div>" + rData[ccols[i]] + "</div>");
						}
					} else {
						itemData.push("<div>" + rData[_this.gcfg.columns[0]] + "</div>");
					}
	
					itemJson["coreData"]    = "<img src=\"" + url + "\">";
					//itemJson["toolbarsData"]= "";
					itemJson["itemData"]    = itemData.join("");
					//itemJson["buttonsData"] = "";					
					return itemJson;
				}
			}
			// 分页条
			if(this.gcfg.pageable)  this._createPbar();
			
			// 设置初始化时显示缩略图还是列表
			if ($.config.contentType.card == this.options.model && "1" == this.options.thumbnail) {
				setting.model = "card";
			}
			// 拖动排序
			if ("1" == this.options.sort) {
				setting.onSortableRows = function(e, ui) {
					// 拖动事件需要二次开发
					_this.eOnSortableRows(e, ui);
				};
				setting.afterSortableRows = function(e, ui) {
					// 拖动事件需要二次开发
					_this.eAfterSortableRow(e, ui);
				};
			}
			// 初始化列表之前事件
			setting = this.beforeInitGrid(setting);
			// 初始化列表
			this.uiGrid.grid(setting);
		},
		
		// 获取缩略图URL
		getThumbnailUrl : function (rData, ni, multi) {
			var idName = this.getKeyColumnName();
			return this.getAction() + "!thumbnail?P_tableId=" + this.options.tableId + "&id=" + rData[idName] + "&P_menuCode=" + this.getParamValue("menuCode");
		},
		
		//获取列表数据类型
		getDataType : function (filter) {
			// isAlone为馆室项目 从列表为动态列表二次开发中使用的
			return ((this.options.sequence === 1 || this.inited || 
					isNotEmpty(filter["P_M_tableId"]) || this.options.isAlone) ? "json" : "local");
		},
		//获取界面分页参数
		getPageSize : function () {
			return this.gcfg.pageSize;
		},
		
		//获取分页列表
		getPageSizeList : function() {
			var arr = [10, 20, 30], ps = this.getPageSize();
			if ($.inArray(ps, arr) == -1) {
				arr.push(ps);
				arr.sort(function compare(a,b){return a-b;});
			}
			return arr;
		},
		
		// 初始化列表之前事件，必须返回列表配置信息
		beforeInitGrid : function (setting /*列表配置信息*/) {
			return setting;
		},
		
		// 分页条
		_createPbar : function () {
			this.uiPbar = $("<DIV id=\"" + this.pbarId() + "\" class=\"" + this.gridId() + "\"></DIV>").appendTo(
					this.uiGrid
				);
		},
		
		// 获取列表配置信息
		_getGridCfg : function () {
			var url = this.getAction() + "!page.json?E_frame_name=coral&E_model_name=grid" +
					"&P_tableId=" + this.options.tableId +
					"&P_moduleId=" + this.options.moduleId + 
					"&P_menuId=" + this.options.menuId +
					"&P_componentVersionId=" + null2empty(this.options.componentVersionId) + 
					"&P_workflowId=" + null2empty(this.options.workflowId) +
					"&P_box=" + null2empty(this.options.box) +
					"&P_menuCode=" + this.getParamValue("menuCode");
			return $.loadJson(url);
		},
		
		// 检索区和树节点的过滤条件
		_getSearchFilter : function () {
			var postData = {}, master = null, filter = "", jqCombo = $("#COMBOBOX_SEARCH_" + this.timestamp, this.element);
			
			// 主列表过滤条件
			if (isNotEmpty(this.options.master) && isNotEmpty(this.options.master.tableId)) {
				postData["P_M_tableId"] = this.options.master.tableId;
				postData["P_M_dataId"] = this.options.master.dataId;
			} else {
				master = this._getMaster();
				if (master) {
					if ($.config.contentType.isGrid(master.type)) {
						if (isNotEmpty(master.jqUI.selectedRowId)) {
							postData["P_M_tableId"] = master.tableId;
							postData["P_M_dataId"] = master.jqUI.selectedRowId;
						}
					} else {
						var jqUI = master.jqUI;
						if (master.tableId === this.options.tableId) {
							jqUI = this.getMasterForm();
						}
						if (jqUI) {
							postData["P_M_tableId"] = jqUI.options.tableId;
							postData["P_M_dataId"] = null2empty(jqUI.options.dataId);
						}
					}
				}
			}
			
			// 自身列表过滤条件
			if (isNotEmpty(this.searchFilter)) {
				filter += this.searchFilter;
	        }
			if (isNotEmpty(this.options.columns)) {
				if (filter.length > 0) filter += ";";
				filter += this.options.columns;
			}
			// 批量修改预览查询过滤条件
			if (this.previewScope === 0) {
				if (filter.length > 0) filter += ";";
				filter += ("IN" + CFG_oc_split + "ID" + CFG_cv_split + this.getSelectedRowId().join(","));
				delete this.previewScope;
			}
			// 下拉框检索条件
			if (jqCombo.length > 0 && isNotEmpty(jqCombo.combobox("getValue"))) {
				if (filter.length > 0) filter += ";";
				filter += jqCombo.combobox("option", "name") + CFG_cv_split + jqCombo.combobox("getValue");
			}
			postData["P_filter"] = this.processFilter(filter);
			return this.processPostData(postData);
		},
		// 查询URL(不变的部分)
		_getBaseUrl : function () {
			var globalData = $.config.getGlobalData(this.options.global);
			var url = this.getAction() + "!search.json"
			   + "?E_frame_name=coral&E_model_name=datagrid"
			   + "&P_searchType=" + this.gcfg.searchType
			   + "&P_tableId=" + this.options.tableId
			   + "&P_tableSuffix=" + null2empty(this.options.tableSuffix)
	           + "&P_moduleId=" + this.options.moduleId
               + "&P_componentVersionId=" + this.options.componentVersionId
               + "&P_menuId=" + this.options.menuId
               + "&P_workflowId=" + null2empty(this.options.workflowId)
               + "&P_box=" + null2empty(this.options.box)
               + "&P_orders=" + this.gcfg.orders.toString()
               + "&P_cgridDivId=" + this.element.attr("id")
               + "&P_topComVersionId=" + this.getParamValue("topComVersionId")
               + "&P_treeNodeId=" + null2empty(globalData.treeNodeId)
               + "&P_menuCode=" + this.getParamValue("menuCode");
			if (typeof this.batchUpdateGridInfo === "object") {
				url += "&P_columns=" + this.batchUpdateGridInfo.columns.toString();
				delete this.batchUpdateGridInfo;
			} else {
				url += "&P_columns=" + this.gcfg.columns.toString();
			}
			return (url + "&P_timestamp=" + this.options.globalTimestamp);
		},
		
		// 列表宽度用户个性化设置
		_setUserWidths : function (e, ui) {
			var widthArr = [], i = 0, colModel = this.uiGrid.grid("option", "colModel");
			for (; i < ui.headers.length; i++) {
				if ($(ui.headers[i].el).is(":hidden")) {
					continue;
				}
				widthArr.push(ui.headers[i].width);
			}
			
			$.ajax({
				url : $.config.appActionPrefix + "/app-grid!setUserWidths.json?P_tableId=" + this.options.tableId + 
						"&P_componentVersionId=" + this.options.componentVersionId + "&P_widths=" + widthArr.toString() + "&P_menuId=" + this.options.menuId,
				dataType : "json",
				success : function () {
					CFG_message("列表宽度设置成功!", "success");
				},
				error : function () {
					CFG_message("列表宽度设置出错!", "error");
				}
			});
		},
		// 列表表头用户个性化设置
		_setUserHeaders : function (e, ui) {
			var indexes = ui.permutation.toString();
			$.ajax({
				url : $.config.appActionPrefix + "/app-grid!setUserHeaders.json?P_tableId=" + this.options.tableId + 
						"&P_componentVersionId=" + this.options.componentVersionId + "&P_indexes=" + indexes + "&P_menuId=" + this.options.menuId,
				dataType : "json",
				success : function () {
					CFG_message("列表表头设置成功!", "success");
				},
				error : function () {
					CFG_message("列表表头设置出错!", "warning");
				}
			});
		},
		// 生成ID规则
		_generateId : function (prefix) {
			return prefix + "_" + this.options.tableId + "_" + this.uuid + "_" + this.options.number; 
		},
		// 列表div ID
		gridId : function () {
			return this._generateId("gd");
		},
		// 工具条所在面板div ID
		tbarPanelId : function () {
			return this._generateId("tp");
		},
		// 工具条div ID
		tbarId : function () {
			return this._generateId("gt");
		},
		// 分页条div ID
		pbarId : function () {
			return this._generateId("gp");
		},
		// 与当前列表的所有关系对象
		_getRelation : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.grid),
			    relation = null, detailId = null;
			if (storeId in data) {
				return data[storeId];
			}
			return null;
		},
		// 从表关系对象
		_getDetail : function () {
			var data = $.config.getGlobalData(this.options.global), 
			    relation = this._getRelation();
			
			if (null != relation && relation.detailId) {
				return data[relation.detailId];
			}
			
			return null;
		},
		// 主表关系对象
		_getMaster : function () {
			var data = $.config.getGlobalData(this.options.global), 
			    relation = this._getRelation();
			
			if (null != relation && relation.masterId) {
				return data[relation.masterId];
			}
			
			return null;
		},
		// 获取列表对应的主表的表单
		getMasterForm : function () {
			var relationUI = this._getMaster();
			if (!relationUI) return false;
			if (this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isForm(relationUI.type)) return relationUI.jqUI; // detail grid -> master form
				else return relationUI.jqUI.getMasterForm(); // detail grid -> master grid -> master form
			} else {
				if ($.config.contentType.isForm(relationUI.type)) {
					return relationUI.jqUI.getMasterForm(); // detail grid -> detail form -> master form
				}
			}
			return false;
		},
		// 获取列表对应的表单
		getSelfForm : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.form),
			    sForm = null, jqUI = false;
			if (storeId in data) {
				sForm = data[storeId];
				jqUI = sForm ? sForm.jqUI : false;
			}
			return jqUI;
		}, 
		// 获取列表对应的查询区（布局中的查询区）
		getSelfBaseSearch : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.search),
			    sSearch = null, jqUI = false;
			if (storeId in data) {
				sSearch = data[storeId];
				jqUI = sSearch ? sSearch.jqUI : false;
			}
			return jqUI;
		}, 
		// 获取列表对应的主列表
		getMasterGrid : function () {
			var relationUI = this._getMaster();
			if (!relationUI) return false;
			if (this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isGrid(relationUI.type)) return relationUI.jqUI; //detail grid -> master grid
				else return relationUI.jqUI.getMasterGrid(); //detail grid -> master form -> master grid
			} else {
				if ($.config.contentType.isForm(relationUI.type)) {
					return relationUI.jqUI.getMasterGrid(); //detail grid -> detail form -> master grid
				}
			}
			return false;
		}, 
		// 获取列表对应的从列表
		getDetailGrid : function () {
			var relationUI = this._getDetail();
			if (!relationUI) return false;
			if (this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isGrid(relationUI.type)) return relationUI.jqUI; //master grid -> detail grid
				else return relationUI.jqUI.getDetailGrid(); //master grid ->master form -> detail grid
			} else {
				if ($.config.contentType.isForm(relationUI.type)) {
					return relationUI.jqUI.getDetailGrid(); //master grid -> detail form -> detail grid
				}
			}
			return false;
		},
		// 获取下拉检索框的值
		getSearchComboValue : function () {
			var jqCombo = $("#COMBOBOX_SEARCH_" + this.timestamp, this.element);
			if (jqCombo) {
				return jqCombo.combobox("getValue");
			}
			return "";
		},
		// 禁用工具条
		disableToolbar : function () {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("disable");
			}
			this.options.disabled = true;
		},
		// 启用工具条
		enableToolbar : function () {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("enable");
			}
			this.options.disabled = false;
		}, 
		// 禁用工具条按钮
		disableToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("disableItem", id);
			}
		}, 
		// 启用工具条按钮
		enableToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("enableItem", id);
			}
		},
		// 隐藏工具条按钮
		hideToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("hide", id);
			}
		}, 
		// 显示工具条按钮
		showToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("show", id);
			}
		},
		// 移除工具条按钮
		removeToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) {
				this.uiTbar.toolbar("remove", id);
			}
		},
		// 获取列表列ID属性字段
		getKeyColumnName : function () {
			var idName, i = 0, len = this.gcfg.colModel.length;
			for (; i < len; i++) {
				if (this.gcfg.colModel[i].key === true) {
					idName = this.gcfg.colModel[i].name;
					break;
				}
			}
			return idName || "ID";
		},
		// 添加列表数据
		addRowData : function (data) {
			var id, idName = this.getKeyColumnName(), allRowIds = this._getAllRowIds();
			if ($.isPlainObject(data)) {
				id = data[idName];
				if (isEmpty(id)) {
					id = "UNSAVE_" + getTimestamp();
					data[idName] = id;
				}
			}
			if (-1 === $.inArray(id, allRowIds)) {
				this.uiGrid.grid("addRowData", id, data);
			} else {
				this.uiGrid.grid("setRowData", id, data);
			}
		},
		/**
		 * 获取列表数据
		 * @param rowId --列表行ID（为可选参数）；如果不传的话，则获取整个列表数据
		 * @returns
		 */
		getRowData : function (rowId) {
			return rowId ? this.uiGrid.grid("getRowData", rowId) 
					: this.uiGrid.grid("getRowData");
		},
		// 将列表中的数据转为表单数据集（有对应表单）
		toFormData : function () {
			var cform = this.getSelfForm();
			if (!cform) return [];
			var fData = cform.getFormData(),
			    rData = this.getRowData(),
			    fDataArr = [], i = 0, len = rData.length,
			    tRow, sRow, prop;
			for (; i < len; i++) {
				sRow = rData[i];
				tRow = {};
				for (prop in fData) tRow[prop] = sRow[prop];
				fDataArr.push(tRow);
			}
			return fDataArr;
		},
		// 清空列表数据
		clearGridData : function () {
			this.uiGrid.grid("clearGridData");
		},
		// 清除所有从列表并禁用相应的工具条
		clearDetailGridData : function () {
			clear(this._getDetail());
			//清除所有从列表并禁用相应的工具条辅助方法
			function clear (ui) {
				if (ui && $.config.contentType.isGrid(ui.type)) {
					var jqUI = ui.jqUI;
					jqUI.disableToolbar();
					jqUI.clearGridData();
					clear(jqUI._getDetail());
				}
				return;
			}
		},
		// 获取选中行ID (Array)
		getSelectedRowId : function () {
			if (this.uiGrid.grid("option", "multiselect")) {
				return this.uiGrid.grid("option", "selarrrow");
			}
			var rowIdArr =[], rowId = this.uiGrid.grid("option", "selrow");
			if (isNotEmpty(rowId)) rowIdArr.push(rowId);
			return rowIdArr;
		},
		// 工作流流转地址
		_getCoflowUrl : function(op, id) {
			return this.getAction() + "!coflow.json?P_workflowId=" + null2empty(this.options.workflowId) + "&P_op=" + op
			    + "&P_workitemId=" + this._getWorkitemId(id) 
			    + "&P_tableId=" + this.options.tableId 
			    + "&id=" + id.split("_")[0]
			    + "&P_menuCode=" + this.getParamValue("menuCode");
		},
		// 流程实例ID（工作流）
		_getProccessInstanceId : function (id) {
			var prop, rowData = this.uiGrid.grid("getRowData", id);
			// 工作流定义（视图）流程实例ID字段名称
			prop = CFG_common.P_ID;
			if (prop in rowData) {
				return rowData[prop];
			} 
			// 工作流对应的物理表中的流程实例ID字段名称
			prop = prop.replace(/\_$/, "");
			if (prop in rowData) {
				return rowData[prop];
			}
			return false;
		},
		// 节点ID（工作流）
		_getActivityId : function (id) {
			var val = this.uiGrid.grid("getRowData", id)[CFG_common.ACTIVITY_ID];
			return null2empty(val);
		},
		// 工作项ID（工作流）
		_getWorkitemId : function (id) {
			var val = this.uiGrid.grid("getRowData", id)[CFG_common.W_ID];
			return null2empty(val);
		},
		// 工作项状态（工作流）
		_getWorkitemStatus : function (id) {
			var val = this.uiGrid.grid("getRowData", id)[CFG_common.W_STATUS];
			return null2empty(val);
		},
		// 工作流按钮处理（工作流）
		_processCoflowButton : function (idArr) {
			if (CFG_common.BOX_TODO !== this.options.box) {
				return;
			}
			if (!$.isCoral(this.uiTbar, "toolbar")) {
				return;
			}
			var _this = this;
			if (!idArr || 0 == idArr.length) {
				return;
			}
			var wStatus = this._getWorkitemStatus(idArr[0]), btnArr;
			this.uiTbar.toolbar("enable");
			if (11 != wStatus) {
				this.uiTbar.toolbar("disableItem", CFG_common.P_CHECKOUT);
				//this.uiTbar.toolbar("disableItem", CFG_common.P_RECALL);
				this.uiTbar.toolbar("enableItem", CFG_common.P_REASSIGN);
				this.uiTbar.toolbar("enableItem", CFG_common.P_TERMINATION);
			} else {
				this.uiTbar.toolbar("enableItem", CFG_common.P_CHECKOUT);
				//this.uiTbar.toolbar("enableItem", CFG_common.P_RECALL);
				this.uiTbar.toolbar("disableItem", CFG_common.P_REASSIGN);
				this.uiTbar.toolbar("disableItem", CFG_common.P_TERMINATION);
			}
			btnArr = buttonSetting();
			for (var i = 0;i < btnArr.length; i++) {
				this.uiTbar.toolbar("disableItem", btnArr[i]);
			}
			
			// 获取列表待办箱按钮配置
			function buttonSetting() {
				var url = _this.getAction() + "!coflowButtonSetting.json?P_workflowId=" + _this.options.workflowId + "" +
						"&P_workitemId=" + _this._getWorkitemId(idArr[0]) +
						"&P_activityId=" + _this._getActivityId(idArr[0]);
				return $.loadJson(url);
			}
		},
		// 获取所有行ID
		_getAllRowIds : function () {
			return this.uiGrid.grid("getDataIDs");
		},
		// 获取修改字段信息
		_getUpdateColumns : function () {
			var url = $.config.appActionPrefix + "/column-define!updateColumns.json?P_tableId=" + this.options.tableId;
			return $.loadJson(url);
		},
		// 批量修改增加一个字段
		_appendBatchUpdateFormItem : function (jqForm) {
			var _this = this;
			var columns = jqForm.data("columns"),
				searchColumn = {},
			    index   = jqForm.index,
			    jqOuter = $("<div class=\"app-batch-update\" style=\"width:100%;\"></div>").appendTo(jqForm);
			$("<DIV class=\"app-inputdiv12\"><DIV class=\"subfield-" + index + "\"></DIV></DIV>").appendTo(jqOuter);
			$("<DIV class=\"app-inputdiv12\">" +
					"<label class=\"app-input-label\">修改字段：</label>" +
					"<INPUT class=\"coralui-combobox columnName-" + index + "\" name=\"columnName-" + index + "\"  data-options=\"width:200,required:true\">" +
			  "</DIV>").appendTo(jqOuter);
			$("<DIV class=\"app-inputdiv12\">" +
					"<label class=\"app-input-label\">修改方式：</label>" +
					"<INPUT class=\"coralui-radiolist updateMode-" + index + "\" name=\"updateMode-" + index + "\" data-options=\"data:&quot;fixed:固定填充;find:替换填充;step:等差填充&quot;,value:&quot;find&quot;\">" +
			  "</DIV>").appendTo(jqOuter);
			$("<DIV class=\"app-inputdiv12 old-value-border\">" +
					"<label class=\"app-input-label\">查找内容：</label>" +
					"<INPUT class=\"coralui-textbox oldValue-" + index + "\" name=\"oldValue-" + index + "\" type=\"text\" data-options=\"width:200\">" +
			  "</DIV>").appendTo(jqOuter);
			$("<DIV class=\"app-inputdiv12 new-value-border\">" +
					"<label class=\"app-input-label\">替换内容：</label>" +
					"<INPUT class=\"coralui-textbox newValue-" + index + "\" name=\"newValue-" + index + "\" type=\"text\" data-options=\"width:200\">" +
			  "</DIV>").appendTo(jqOuter);	
			$("<DIV class=\"app-inputdiv12 start-no-border\">" +
					"<label class=\"app-input-label\">起始号：</label>" +
					"<INPUT class=\"coralui-textbox startNo-" + index + "\" name=\"startNo-" + index + "\" type=\"text\" data-options=\"width:200,validType:&quot;number&quot;\">" +
			  "</DIV>").appendTo(jqOuter);	
			$("<DIV class=\"app-inputdiv12 step-border\">" +
					"<label class=\"app-input-label\">步长：</label>" +
					"<INPUT class=\"coralui-textbox step-" + index + "\" name=\"step-" + index + "\" type=\"text\" data-options=\"width:200,validType:&quot;number&quot;\">" +
			  "</DIV>").appendTo(jqOuter);	
			
			$(".start-no-border", jqOuter).hide();
			$(".step-border", jqOuter).hide();
			
			$(".subfield-" + index, jqOuter).subfield({
				title: 0 === index ? "" : "<span title=\"删除\">&nbsp;x&nbsp;</span>",
				lineCls : "app-batch-update-fieldset",
				textCls : "app-batch-update-legend",
				onClick: function () {
					jqOuter.remove();
					//jqForm.index = jqForm.index - 1;
					if (!jqForm.removeIndexArr) {
						jqForm.removeIndexArr = [];
					}
					jqForm.removeIndexArr.push(index);
				}
			});
			
			// 解析表单
			$.parser.parse(jqOuter);	
			
			$(".columnName-" + index, jqOuter).combobox("loadData", cast2combo());
			$(".columnName-" + index, jqOuter).combobox("option", "onChange", changeOfcombo);
			$(".updateMode-" + index, jqOuter).radiolist("option", "onChange", changeOfRadio);
			
			jqForm.index = index + 1;
			
			function cast2combo() {
				var data = [], col = null, i = 0;
//				data.push({value: "", text: "请选择字段", selected: true});
				for (; i < columns.length; i++) {
					col = columns[i];
					data.push({value: col.columnName, text: col.showName});
					searchColumn[col.columnName] = {dataType: col.dataType, codeTypeCode: col.codeTypeCode, inputType: col.inputType, dataTypeExtend: col.dataTypeExtend, length: col.length, inputOption: col.inputOption};
				}
				return data;
			}
			// 修改方式onchange事件
			function changeOfRadio(e, ui) {
				if ("fixed" === ui.value) {
					$(".old-value-border", jqOuter).hide();
					$(".new-value-border", jqOuter).show();
					$(".start-no-border", jqOuter).hide();
					$(".step-border", jqOuter).hide();
				} else if ("find" === ui.value) {
					$(".old-value-border", jqOuter).show();
					$(".new-value-border", jqOuter).show();
					$(".start-no-border", jqOuter).hide();
					$(".step-border", jqOuter).hide();
				} else if ("step" === ui.value) {
					$(".old-value-border", jqOuter).hide();
					$(".new-value-border", jqOuter).hide();
					$(".start-no-border", jqOuter).show();
					$(".step-border", jqOuter).show();
				}
			}
			// 字段下拉框onchange事件
			function changeOfcombo(e, ui) {
				var val = ui.newValue, i = 0,
				    um  = $(".updateMode-" + index, jqForm),
				    newcol = searchColumn[val],
				    jqNewCol = $(".newValue-" + index, jqForm),
				    jqOldCol = $(".oldValue-" + index, jqForm);
				if ($.isCoral(jqNewCol, "combobox")) {
					jqNewCol.combobox("setValue", null);
					jqNewCol.combobox("destroy");
				} else if ($.isCoral(jqNewCol, "combotree")) {
					jqNewCol.combotree("setValues", null);
					jqNewCol.combotree("destroy");
				} else if ($.isCoral(jqNewCol, "combogrid")) {
					jqNewCol.combogrid("setValue", null);
					jqNewCol.combogrid("destroy");
				} else if ($.isCoral(jqNewCol, "textbox")) {
					jqNewCol.textbox("setValue", null);
					jqNewCol.textbox("destroy");
				} else {
					jqNewCol.datepicker("setValue", null);
					jqNewCol.datepicker("destroy");
				}
				jqNewCol.addClass(".newValue-" + index);
				if ($.isCoral(jqOldCol, "combobox")) {
					jqOldCol.combobox("setValue", null);
					jqOldCol.combobox("destroy");
				} else if ($.isCoral(jqOldCol, "combotree")) {
					jqOldCol.combotree("setValues", null);
					jqOldCol.combotree("destroy");
				} else if ($.isCoral(jqOldCol, "combogrid")) {
					jqOldCol.combogrid("setValue", null);
					jqOldCol.combogrid("destroy");
				} else if ($.isCoral(jqOldCol, "textbox")) {
					jqOldCol.textbox("setValue", null);
					jqOldCol.textbox("destroy");
				} else {
					jqOldCol.datepicker("setValue", null);
					jqOldCol.datepicker("destroy");
				}
				jqOldCol.addClass(".oldValue-" + index);
				if (newcol.dataType != "n") {
					um.radiolist("disableItem", "step");
					if (um.radiolist("getValue") == "step") {
						um.radiolist("setValue", "find");
						$(".old-value-border", jqOuter).show();
						$(".new-value-border", jqOuter).show();
						$(".start-no-border", jqOuter).hide();
						$(".step-border", jqOuter).hide();
					}
					if ("combobox" === newcol.inputType || "radio" === newcol.inputType
							|| "checkbox" === newcol.inputType) {
						jqOldCol.combobox({
							width : 200,
							name : "value",
							url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&id=" + newcol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
						});
						jqNewCol.combobox({
							width : 200,
							name : "value",
							url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&id=" + newcol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
						});
					} else if ("d" === newcol.dataType) {
						jqOldCol.datepicker({
							width : 200,
							name : "value",
							dateFormat : (newcol.dataTypeExtend ? newcol.inputOption : "yyyy-MM-dd HH:mm:ss"),
							srcDateFormat : (newcol.inputOption ? newcol.dataTypeExtend : "yyyy-MM-dd HH:mm:ss"),
						});
						jqNewCol.datepicker({
							width : 200,
							name : "value",
							dateFormat : (newcol.dataTypeExtend ? newcol.inputOption : "yyyy-MM-dd HH:mm:ss"),
							srcDateFormat : (newcol.inputOption ? newcol.dataTypeExtend : "yyyy-MM-dd HH:mm:ss"),
						});
					} else if ("combotree" === newcol.inputType) {
						jqOldCol.combotree({
							width : 200,
							name : "value",
							popupDialog:true,
							url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combotree&id=" + newcol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
						});
						jqNewCol.combotree({
							width : 200,
							name : "value",
							popupDialog:true,
							url  : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combotree&id=" + newcol.codeTypeCode + "&P_menuCode=" + _this.getParamValue("menuCode")
						});
					} else if ("combogrid" === newcol.inputType) {
						jqOldCol.combogrid($.loadJson(_this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combogrid&id=" + newcol.codeTypeCode + "&P_tableId=" + _this.options.tableId + "&P_menuCode=" + _this.getParamValue("menuCode")));
						jqNewCol.combogrid($.loadJson(_this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_COMBO_TYPE=combogrid&id=" + newcol.codeTypeCode + "&P_tableId=" + _this.options.tableId + "&P_menuCode=" + _this.getParamValue("menuCode")));
					} else {
						jqOldCol.textbox({
							width : 200,
							name : "value",
							maxlength : newcol.length
						});
						jqNewCol.textbox({
							width : 200,
							name : "value",
							maxlength : newcol.length
						});
					}
				} else {
					jqOldCol.textbox({
						width : 200,
						name : "value",
						maxlength : newcol.length,
						validType : "float"
					});
					jqNewCol.textbox({
						width : 200,
						name : "value",
						maxlength : newcol.length,
						validType : "float"
					});
					um.radiolist("enableItem", "step");
				}
			}
			// 根据字段名称找字段定义信息
			function getColumn(columnName) {
				for (var i = 0; i < columns.length; i++) {
					if (columns[i].columnName === columnName) {
						return columns[i];
					}
				}
				return null;
			}
		},
		// 批量修改(弹出式)
		_createDialogBatchUpdate : function () {
			var _this = this,
		    jqGlobal = this.getDialogAppendTo(),
		    width = jqGlobal.width() - 600, 
		    height= jqGlobal.height() - 350,
		    panel;
			panel = $("<div>").appendTo(jqGlobal);
			panel.dialog({
			autoOpen : false,
			modal : true,
			appendTo : jqGlobal,
			title : "批量修改",
			width : width,
			height : height,
			resizable : true,
			position : {
				of : jqGlobal
			},
			onClose : function() {
				panel.remove();
			}
		});
		var jqTbarPanel = $("<DIV class=\"toolbarsnav clearfix\"></DIV>"),
	    jqTbar  = $("<DIV>").appendTo(jqTbarPanel),
	    jqCenter= $("<div class=\"coral-adjusted\" style=\"overflow:auto\"></div>"),
	    jqOuter = $("<DIV class=\"container-center\" style=\"width:410px;\"></DIV>"),
	    formId  = this._generateId("buf"),
        jqForm  = $("<FORM id=\"" + formId + "\" class=\"coralui-form\"></FORM>"),
	    sbarPanel = $("<div class=\"app-bottom-toolbar toolbar-center clearfix\"></div>"),
	    sbar = $("<DIV>").appendTo(sbarPanel),
	    idArr = this.getSelectedRowId();
		$("<DIV class=\"app-batch-update\"><DIV class=\"app-inputdiv12\">" +
				"<label class=\"app-input-label\">生成范围：</label>" +
				"<DIV class=\"coralui-radiolist radio-scope\" data-options=\"name:&quot;scope&quot;,data:&quot;0:所选记录;1:查询结果&quot;\"></DIV>" +
		  "</DIV></DIV>").appendTo(jqForm);
		//
		jqForm.data("columns", this._getUpdateColumns());
		
		jqTbarPanel.prependTo(panel);
		jqForm.appendTo(jqOuter);
		jqOuter.appendTo(jqCenter);
		jqCenter.appendTo(panel);
		sbarPanel.appendTo(panel);
		//顶部工具条
		jqTbar.toolbar({
			data: [{type:"button", label:"添加字段", onClick: function() { _this._appendBatchUpdateFormItem(jqForm); }},
			       {type:"button", label:"预览", onClick: function() { _this._batchUpdatePreview(jqForm);}},
			       {type:"button", label:"保存", onClick: function() { _this._batchUpdateSave(jqForm); }},
			       {type:"button", label:"返回", onClick: function() { _this._batchUpdateBack(panel); }}]
		});
		//底部工具条
		sbar.toolbar({
			data: ["->",
			       {type:"button", cls:"save", label:"保存", onClick: function() { _this._batchUpdateSave(jqForm); }},
			       {type:"button", label:"返回", onClick: function() { _this._batchUpdateBack(panel); }},
			       "->"]
		});
		// 解析表单
		$.parser.parse(jqOuter);
		if (null == idArr || 0 == idArr.length) {
			$(".radio-scope", jqForm).radiolist("setValue","1");
			$(".radio-scope", jqForm).radiolist("disableItem", "0");
		} else {
			$(".radio-scope", jqForm).radiolist("setValue","0");
			$(".radio-scope", jqForm).radiolist("enableItem", "0");
		}
		//
		$.coral.adjusted(jqCenter);
		jqForm.index = 0;
		this._appendBatchUpdateFormItem(jqForm, 0);
		// 打开表单窗体
		panel.dialog("open");
		},
		// 批量修改(嵌入式)
		_createNestedBatchUpdate : function (panel) {
			var jqTbarPanel = $("<DIV class=\"toolbarsnav clearfix\"></DIV>"),
			    jqTbar  = $("<DIV>").appendTo(jqTbarPanel),
			    jqCenter= $("<div class=\"coral-adjusted\" style=\"overflow:auto\"></div>"),
			    jqOuter = $("<DIV class=\"container-center\" style=\"width:410px;\"></DIV>"),
			    formId  = this._generateId("buf"),
		        jqForm  = $("<FORM id=\"" + formId + "\" class=\"coralui-form\"></FORM>"),
			    sbarPanel = $("<div class=\"app-bottom-toolbar toolbar-center clearfix\"></div>"),
			    sbar = $("<DIV>").appendTo(sbarPanel),
			    idArr = this.getSelectedRowId(),
			    _this = this;
			$("<DIV class=\"app-batch-update\"><DIV class=\"app-inputdiv12\">" +
					"<label class=\"app-input-label\">生成范围：</label>" +
					"<DIV class=\"coralui-radiolist radio-scope\" data-options=\"name:&quot;scope&quot;,data:&quot;0:所选记录;1:查询结果&quot;\"></DIV>" +
			  "</DIV></DIV>").appendTo(jqForm);
			//
			jqForm.data("columns", this._getUpdateColumns());
			
			jqTbarPanel.prependTo(panel);
			jqForm.appendTo(jqOuter);
			jqOuter.appendTo(jqCenter);
			jqCenter.appendTo(panel);
			sbarPanel.appendTo(panel);
			//顶部工具条
			jqTbar.toolbar({
				data: [{type:"button", label:"添加字段", onClick: function() { _this._appendBatchUpdateFormItem(jqForm); }},
				       {type:"button", label:"预览", onClick: function() { _this._batchUpdatePreview(jqForm);}},
				       {type:"button", label:"保存", onClick: function() { _this._batchUpdateSave(jqForm); }},
				       {type:"button", label:"返回", onClick: function() { _this._batchUpdateBack(panel); }}]
			});
			//底部工具条
			sbar.toolbar({
				data: ["->",
				       {type:"button", cls:"save", label:"保存", onClick: function() { _this._batchUpdateSave(jqForm); }},
				       {type:"button", label:"返回", onClick: function() { _this._batchUpdateBack(panel); }},
				       "->"]
			});
			// 解析表单
			$.parser.parse(jqOuter);
			if (null == idArr || 0 == idArr.length) {
				$(".radio-scope", jqForm).radiolist("setValue","1");
				$(".radio-scope", jqForm).radiolist("disableItem", "0");
			} else {
				$(".radio-scope", jqForm).radiolist("setValue","0");
				$(".radio-scope", jqForm).radiolist("enableItem", "0");
			}
			//
			$.coral.adjusted(jqCenter);
			jqForm.index = 0;
			this._appendBatchUpdateFormItem(jqForm, 0);
		},
		// 批量修改字段信息
		_getBatchUpdateFields : function (jqForm) {
			var _this = this,
			    formData = jqForm.form("formData", false),
			    len  = jqForm.index, 
			    i = 0,
			    scope= formData.scope,
			    fields = "[",
			    f, columnName, mode, oldValue, newValue, startNo, step, ids, inputType,
			    removeIndexArr = jqForm.removeIndexArr;
			
			if (!jqForm.form("valid")) return false;
			
			for (; i < len; i++) {
				if (removeIndexArr != null && $.inArray(i, removeIndexArr) != -1) {
					continue;
				}
				if (0 === $(".subfield-" + i, jqForm).length) continue;
				columnName = formData["columnName-" + i];
				for ( var j = i + 1; j < len; j++) {
					if (removeIndexArr != null && $.inArray(j, removeIndexArr) != -1) {
						continue;
					}
					if (columnName == formData["columnName-" + j]) {
						CFG_message("字段重复!", "warning");
						return false;
					}
				}
				if (isEmpty(columnName)) {
					CFG_message("请在所有字段中选择具体字段!", "warning");
					return false;
				}
				mode = formData["updateMode-" + i];
				inputType = getColumnDefine(columnName).inputType;
				f = "{\"columnName\":" + "\"" + columnName + "\",\"updateMode\":" + "\"" + mode + "\",\"inputType\":" + "\"" + inputType + "\"";
				if ("fixed" === mode) {
					newValue = formData["newValue-" + i];
					f += ",\"newValue\":" + "\"" + decodeURIComponent(newValue) + "\"";
				} else if ("find" === mode) {
					oldValue = formData["oldValue-" + i];
					newValue = formData["newValue-" + i];
					if (isEmpty(oldValue) && isEmpty(newValue)) {
						CFG_message("请在第 " + (i+1) + "个字段中输入查找内容或替换内容!", "warning");
						return false;
					}
					f += ",\"oldValue\":" + "\"" + decodeURIComponent(oldValue) + "\",\"newValue\":" + "\"" + decodeURIComponent(newValue) + "\"";
				} else if ("step" === mode) {
					startNo = formData["startNo-" + i];
					step = formData["step-" + i];
					if (isEmpty(startNo) || isEmpty(step)) {
						CFG_message("请在第 " + (i+1) + "个字段中输入起始号和步长!", "warning");
						return false;
					}
					f += ",\"startNo\":" + startNo + ",\"step\":" + step;
				}
				f += "},";
				
				fields += f;
			}
			fields = fields.substring(0, fields.length-1);
			fields += "]";
		    this.previewScope = parseInt(scope);
		    
		    return fields;
		    
		    /* 获取字段定义信息. */
			function getColumnDefine(columnName) {
				var cols = jqForm.data("columns"), i = 0;
				for (; i < cols.length; i++) {
					if (columnName === cols[i].columnName) return cols[i];
				}
				return null;
			};
		},
		// 保存批量修改
		_batchUpdateSave : function (jqForm) {
			var _this = this, 
			    fields= this._getBatchUpdateFields(jqForm),
			    scope = this.previewScope, ids = "";
			
			if (false === fields) return;
		    
			if (scope == "0") {
				ids = _this.getSelectedRowId().toString();
			}
			
			$.ajax({
				type : "post",
				url : _this.getAction() + "!batchUpdate.json",
				dataType : "json",
				data : ("P_tableId=" + _this.options.tableId + "&P_moduleId=" + _this.options.moduleId + 
						"&P_scope=" + scope + "&P_ids=" + ids +
						"&P_fields=" + fields + "&P_timestamp=" + _this.options.globalTimestamp + "&P_menuCode=" + _this.getParamValue("menuCode") + "&P_componentVersionId=" + this.options.componentVersionId
				          + "&P_menuId=" + this.options.menuId),
				success : function (rlt) {
					if (true === rlt.success) {
						CFG_message(rlt.message, "success");
						_this._batchUpdateBack(_this.getNestedJq());
					} else {
						CFG_message("批量修改失败!", "warning");
					}
				},
				error : function () {
					CFG_message("批量修改失败!", "error");
				}
			});
		},
		// 批量修改预览页面
		_batchUpdatePreview : function (jqForm) {
			var fields = this._getBatchUpdateFields(jqForm);
			
			if (false === fields) return;
			
			var _this = this, jqGlobal = this.getDialogAppendTo(),
				width = jqGlobal.width() - 360, 
		        height= jqGlobal.height() - 100, 
		        jqDlg = $("<div id=\"dlg_" + this.timestamp + "\"></div>").appendTo(this.options.global),
		        jqTbarPanel = $("<DIV class=\"toolbarsnav clearfix\"></DIV>"),
			    jqTbar  = $("<DIV>").appendTo(jqTbarPanel),
		        jqGrid= $("<div id=\"pvg_" + this.timestamp + "\"></div>").appendTo(jqDlg),
	            jqPbar= $("<div class=\"pvg_" + this.timestamp + "\"></div>").appendTo(jqGrid);

			jqDlg.dialog({
				autoOpen : true,
				modal : true,
				appendTo : jqGlobal,
				title : "批量修改预览",
				width : width,
				height : height,
				resizable : false,
				position : {
					of : jqGlobal,
					at : "center center"
				},
				onClose : function() {
					jqDlg.remove();
				},
				onOpen : function () {
					var setting, columns, /*datatypes, types, codetypes, urls,*/ colNames, colModel,
					    gcfg= _this.gcfg,
					    fArr= $.parseJSON(fields),
					    itArr = ["combobox","combotree","combogrid","checkbox","radio"],
					    i = 0, idx, cd,cn, cm, jq;
					if (gcfg.colNames.length > 3) {
						colNames = $.extend(true, [], gcfg.colNames).slice(0, 3);
						colModel = $.extend(true, [], gcfg.colModel).slice(0, 3);
						columns  = $.extend(true, [], gcfg.columns).slice(0, 3);
						//datatypes= $.extend(true, [], gcfg.datatypes).slice(0, 3);
						//types    = $.extend(true, [], gcfg.types).slice(0, 3);
						//codetypes= $.extend(true, [], gcfg.codetypes).slice(0, 3);
						//urls     = $.extend(true, [], gcfg.urls).slice(0, 3);
					} else {
						colNames = $.extend(true, [], gcfg.colNames);
						colModel = $.extend(true, [], gcfg.colModel);
						columns  = $.extend(true, [], gcfg.columns);
						//datatypes= $.extend(true, [], gcfg.datatypes);
						//types    = $.extend(true, [], gcfg.types);
						//codetypes= $.extend(true, [], gcfg.codetypes);
						//urls     = $.extend(true, [], gcfg.urls);
					}
					for (; i < fArr.length; i++) {
						cn = fArr[i].columnName;
						cd = getColumnDefine(cn);
						idx = $.inArray(cn, columns);
						if (idx === -1 && gcfg.colNames.length > 3) {
							idx = $.inArray(cn, gcfg.columns);
							if (idx !== -1) {
								colNames.push(gcfg.colNames[idx]);
								colModel.push(gcfg.colModel[idx]);
								columns.push(cn);
								//datatypes.push(null2empty(gcfg.datatypes[idx]));
								//codetypes.push(null2empty(gcfg.codetypes[idx]));
								//urls.push("");
								//types.push("ro");
								idx = colModel.length - 1;
							}
						}
						if (idx === -1) {
							jq = $(".columnName-" + i, jqForm);
							colNames.push(jq.combobox("getText"));
							cm = {
								fieldIndex: i,
								name : cn,
								width: (cd.width ? cd.width : 100),
								align: (cd.align ? cd.align : "left"),
								formatter: function(cv, opt, rowObj) {
									return formatter(cv, opt);
								}
							};
							if ($.inArray(cd.inputType, itArr) > -1 && isNotEmpty(cd.codeTypeCode)){
								cm.formatoptions = {data: $.loadJson(_this.getAction() + "!page.json?E_frame_name=coral&E_model_name=code&P_EMPTY=false&id=" + cd.codeTypeCode)};
							}
							colModel.push(cm);
							columns.push(cn);
							//datatypes.push(null2empty(cd.dataType));
							//codetypes.push(null2empty(cd.codeTypeCode));
							//urls.push("");
							//types.push("ro");
						} else {
							colModel[idx].fieldIndex = i;
							colModel[idx].formatter = function(cv, opt, rowObj) {
								return formatter(cv, opt);
							};//*/
						}
					}
					_this.batchUpdateGridInfo = {
							columns  : columns
							//datatypes: datatypes,
							//codetypes: codetypes,
							//types    : types,
							//urls     : urls  
					};
					setting = {
							rownumbers : true,
							height: "auto",
							width : "auto",
							url   : _this._getBaseUrl(),
							postData : _this._getSearchFilter(),
							colNames: colNames,
							colModel: colModel,
							fitStyle: "fill",
							shrinkToFit: true,
							forceFit: true
					};
					
					jqGrid.grid(setting);
					
					/* 预览数据处理.*/
					function formatter (value, opt) {
						//if (!value) return "";
						var field = fArr[opt.colModel.fieldIndex], 
						    options = opt.colModel.formatoptions, 
						    newText = field.newValue,
						    oldText = field.oldValue,
						    itArr = ["combobox","combotree","combogrid","checkbox","radio"],
						    curValue = value, curText = value, findNo = 0;
						if (options) {
							for ( var i = 0; i < options.data.length; i++) {
								if (options.data[i].value == field.newValue) {
									newText = options.data[i].text; findNo++;
								} else if (options.data[i].value == field.oldValue) {
									oldText = options.data[i].text; findNo++;
								} 
								if (options.data[i].value == value) {
									curText = options.data[i].text; findNo++;
								}
								if (3 === findNo) break;
							}
						}
						if ("fixed" === field.updateMode) {
							return "<font color=\"red\">" + newText + "</font>";
						} else if ("find" === field.updateMode) {
							var newColumn = curText.toString();
							if (isNotEmpty(oldText) && isNotEmpty(curValue)) {
								for ( var i = 0; i < fArr.length; i++) {
									if (fArr[i].columnName == field.columnName) {
										if ($.inArray(field.inputType, itArr) > -1) {
											if (curValue == fArr[i].oldValue) {
												newColumn = "<font color=\"red\">" + newText + "</font>";
											}
										} else {
											newColumn = newColumn.replace(new RegExp(fArr[i].oldValue,"gm"), "<font color=\"red\">" + fArr[i].newValue + "</font>");
										}
									}
								}
							} else if (isEmpty(oldText) && isEmpty(curText)) {
								for ( var i = 0; i < fArr.length; i++) {
									if (fArr[i].columnName == field.columnName) {
										newColumn = "<font color=\"red\">" + newText + "</font>";
									}
								}
							}
							return newColumn;
						} else if ("step" === field.updateMode) {
							var jq = $("#" + opt.gid),
							    index = jq.find("td.grid-rownum").length,
							    page  = jq.grid("option", "page"),
							    size  = jq.grid("option", "rowNum");
							return "<font color=\"red\">" + (field.startNo + ((page - 1) * size + index) * field.step) + "</font>";
						}
						return "";
					}
					/* 获取字段定义信息. */
					function getColumnDefine(columnName) {
						var cols = jqForm.data("columns"), i = 0;
						for (; i < cols.length; i++) {
							if (columnName === cols[i].columnName) return cols[i];
						}
						return null;
					}
				}
			});
		    jqTbarPanel.prependTo(jqDlg);
		    jqTbarPanel.toolbar({
				data: [{id:"close", label:"关闭", type:"button"}],
				onClick: function(e, ui) {
					if ("close" == ui.id) {
						jqDlg.dialog("close");
					}
				}
			});
			jqDlg.dialog("buttonPanel").addClass("app-bottom-toolbar");
			var sbarPanel = $("<div>").appendTo(jqDlg.dialog("buttonPanel"));
			sbarPanel.toolbar({
				data: ["->",
				       {id:"close", label:"关闭", type:"button"}],
				onClick: function(e, ui) {
					if ("close" == ui.id) {
						jqDlg.dialog("close");
					}
				}
			});
		},
		// 批量修改返回
		_batchUpdateBack : function (panel) {
			if ("previewScope" in this) delete this.previewScope;
			if ("batchUpdateGridInfo" in this) delete this.batchUpdateGridInfo;
			if (!$.isCoral(panel, "panel")) {
				// 依赖外部导航条返回
				panel.remove();
				this.load();
				if (typeof CFG_clickReturnButton === "function") {
					CFG_clickReturnButton(this.assembleData);
				}
			}
		},
	    // 容器
	    component : function () {
	    	return this.element;
	    },
	    // 刷新
	    refresh : function () {
	    	// 如果存在嵌入检索区，则刷新嵌入检索区
	    	var sUi = this.getSelfBaseSearch();
	    	if (sUi) {
	    		sUi.refresh();
	    	}
	    	CFG_emptyJQ(this.component());
	    	this.inited = false;
	    	this.isRefresh = true;
	    	this.bsearchUI = null;
	    	this._create();
	    },
	    // 刷新上层列表
	    refreshPreviousGrid : function () {
			var cgridDivId = $.config.getMasterCgridDivId(this.options.global);
			if (!cgridDivId) return;
			$("#" + cgridDivId).cgrid("refresh");
		},
		// 重新渲染对应的主列表
		refreshMasterGrid : function () {
			var cgrid = this.getMasterGrid();
			if (cgrid && !cgrid.options.disabled) {
				cgrid.refresh();
			}
		},
		// 重新渲染对应的从列表
		refreshDetailGrid : function () {
			var cgrid = this.getDetailGrid();
			if (cgrid && !cgrid.options.disabled) {
				cgrid.refresh();
			}
		},
		// 获取action		
		getAction : function () {
			var fn = window[CFG_actionName(this.options.globalTimestamp)];
			if ($.isFunction(fn)) return fn(this);
			
			return $.config.appActionPrefix + "/show-module";
		},
		// 加载列表数据（查询第一页数据）
		load : function () {
			this.uiGrid.grid("option", "datatype", "json");
			this.uiGrid.grid("option", "postData", this._getSearchFilter());
			this.uiGrid.grid("reload", {page: 1});
			// 初始化关系的表单或列表
			this.initRelation();
		},
		// 重新加载列表数据（查询当前页数据）
		reload : function (rowData) {
			if (rowData instanceof Array) {
				this.clearGridData();
				for (var i = 0, len = rowData.length; i < len; i++) {
					this.addRowData(rowData[i]);
				}
			} else {
				this.uiGrid.grid("option", "datatype", "json");
				// 列表动态条件
				this.uiGrid.grid("option", "postData", this._getSearchFilter());
				// 重新加载数据
				this.uiGrid.grid("reload");
			}
			// 初始化关系的表单或列表
			this.initRelation();
		}, 
		// 重新加载上层列表
		reloadPreviousGrid : function () {
			var cgridDivId = $.config.getMasterCgridDivId(this.options.global);
			if (!cgridDivId) return;
			$("#" + cgridDivId).cgrid("reload");
		},
		// 重新加载对应的主列表数据
		reloadMasterGrid : function () {
			var cgrid = this.getMasterGrid();
			if (cgrid && !cgrid.options.disabled) {
				cgrid.reload();
			}
		},
		// 重新加载对应的从列表数据
		reloadDetailGrid : function () {
			var cgrid = this.getDetailGrid();
			if (cgrid && !cgrid.options.disabled) {
				cgrid.reload();
			}
		},
		// 过滤条件处理
		processFilter : function(filter) {
			var fn = this.options.processFilter;
			if ($.isFunction(fn)) {
				filter = fn.apply(this.element, [filter]);
			}
			return filter;
		},
		// 二次开发 参数传递 接口
		processPostData : function (postData) {
			return postData;
		},
		// 添加返回按钮
		addReturnButton : function (callback) {
			var _this = this;
			this.uiTbar.toolbar("add", null, {
				label: "返回",
				type: "button",
				onClick: function () {
					if (typeof callback === "function") {
						callback();
					} else {
						_this.element.remove();
					}
				}
			});
		}, 
		// 初始化关系的表单或列表
		initRelation : function () {
			// 重置表单
			//var formUI = this.getSelfForm();
			//if (false !== formUI) {
			//	formUI.clickCreate();
			//} 
			// 清除相关明细列表
			this.clearDetailGridData();
		},		
		// 单击列表事件
		eSelectRow : function (e, ui) {
			var dataId = null, idArr  = this.getSelectedRowId(), 
			    master = null, detail = null, jqUI = null;
			// 工作流按钮处理
			this._processCoflowButton(idArr);
			// 相关列表或表单处理
			if (idArr.length == 0 || idArr.length > 1) {
				this.selectedRowId = null;
				this.clearDetailGridData();
				return;
			}
			dataId = idArr[0];
			//
			this.selectedRowId = dataId;
			// 列表数据是临时的，未保存
			if (dataId.indexOf("UNSAVE_") === 0) {
				jqUI = this.getSelfForm();
				if (jqUI) {
					jqUI.setFormData(this.getRowData(dataId));
				}
				return;
			}
			detail = this._getDetail();
			if (detail) {
				jqUI = detail.jqUI;
				if ($.config.contentType.isGrid(detail.type)) {
					jqUI.enableToolbar();
					jqUI.load();
				} else if ($.config.contentType.isForm(detail.type) && this.options.tableId === detail.tableId) {
					jqUI.load(dataId);
				}
			} else {
				master = this._getMaster();
				if (master) {
					jqUI = master.jqUI;
					if ($.config.contentType.isForm(master.type) && this.options.tableId === master.tableId) {
						jqUI.load(dataId);
					} 
				} 
			}
		},	
		// 双击列表事件
		eDblClickRow : function (e, ui) {
			if ("0" == this.gcfg.dblclick) return;
			var isView = ("2" == this.gcfg.dblclick), 
				isNested = this.isNestedAssembleType(CFG_common.P_UPDATE), 
			    jqForm = this.getNestedJq(isNested); // 导航 嵌入DIV
			new $.config.cform(this._formOptions(ui.rowId, isView, null, isNested), jqForm);
		},
		// 拖拽排序开始
		eOnSortableRows : $.noop,
		// 拖拽排序结束
		eAfterSortableRow : $.noop,
		// 树根节点
		getRootNode : function () {
			var data = $.config.getGlobalData(this.options.global);
			return data.rootNode;
		},
		// 获取masterDataId 或 masterTableId, 默认为masterTableId
		// type="DATA"时为 masterDataId
		getMasterId : function (type) {
			var master = this._getMaster();
			if (master && $.config.contentType.isGrid(master.type)) {
				if ("DATA" === type) return master.jqUI.selectedRowId;
				return master.tableId;
			}
			return "";
		},
		// 获取当前列表的从列表的表IDs
		getDetailTableIds : function () {
			return ids(this._getDetail(), []);
			// 获取所从列表的表ID辅助方法
			function ids (ui, idArr) {
				if (!idArr) idArr = [];
				if (null !== ui && $.config.contentType.isGrid(ui.type)) {
					idArr.push(ui.tableId);
					ids(ui.jqUI._getDetail(), idArr);
				}
				return idArr.toString();
			}
		},
		// 获取电子全文表ID
		getDocumentTableId : function () {
			return $.loadJson(this.getAction() + "!findDocumentTableId.json?P_tableId=" + this.options.tableId);
		},
		// 获取明细表表ID（从后台获取）
		getDetailTableId : function () {
			return $.loadJson(this.getAction() + "!findDetailTableId.json?P_tableId=" + this.options.tableId);
		},
		// 获取嵌入面板DIV jquery对象
		getNestedJq : function (isNested) {
			if (undefined === isNested) isNested = true;
			var jq = CFG_getEmbeddedDivNoNav(this.assembleData, isNested, $.noop) || $();
			return jq;
		},
		
		/**
		 * 删除前，二次开发接口
		 * @param idArr
		 * @param isLogicalDelete
		 * @returns {Boolean}
		 *          -- false:终止删除，true:继续删除
		 */
		beforeDelete : function (idArr, isLogicalDelete) { return true; },
		
		/**
		 * 删除后，二次开发接口
		 * @param idArr
		 * @param isLogicalDelete
		 */
		afterDelete : function (idArr, isLogicalDelete) {},
		
		/**
		 * 删除（包括物理和逻辑删除）
		 * @param idArr
		 * @param isLogicalDelete
		 *            -- 0-物理删除；1-逻辑删除
		 */
		databaseDelete : function (idArr, isLogicalDelete) {
			var _this = this,
		    url   = this.getAction() + "!destroy.json?P_tableId=" + this.options.tableId +
		    		"&P_D_tableIds=" + this.getDetailTableIds() + 
		    		"&P_isLogicalDelete=" + isLogicalDelete + 
		    		"&P_menuId=" + this.options.menuId + 
		    		"&P_menuCode=" + this.getParamValue("menuCode");
			// 删除前，业务处理
			if (false === this.beforeDelete(idArr, isLogicalDelete)) {
				return false;
			}
			
			$.confirm("所选择的记录将被删除，确定吗？", function(sure) {
				if (sure) {
					$.ajax({
						type : "post",
						url  : url,
						data : {"id": idArr.toString()},
						dataType:"json",
						success: function (msg) {
							if (msg.success === false) {
								CFG_message(msg.message, "warning");
							} else {
								// 删除后，业务处理
								_this.afterDelete(idArr, isLogicalDelete);
								_this.reload();
								CFG_message("操作成功!", "success");
							}
						}, 
						error : function () {
							CFG_message("操作失败!", "error");
						}
					});
				}
			});
		},
		
		/**
		 * 工具条按钮单击前事件处理
		 * @param  id    -- 按钮ID
		 * @return boolean 
		 *         false -- 终止事件执行
		 *         true  -- 继续执行
		 */ 
		beforeClickTbar : function(id) {
			return true;
		},
	    // 工具条事件
	    clickTbar : function (event, ui) {
	    	var id = ui.id;
	    	if (!id) return;
	    	// 
	    	if (false === this.beforeClickTbar(id)) {
	    		return;
	    	}
	    	if (CFG_common.P_CREATE === id) {
	    		this.clickCreate();
	    	} else if (CFG_common.P_UPDATE === id) {
	    		this.clickUpdate();
	    	} else if (CFG_common.P_BATCH_UPDATE === id) {
	    		this.clickBatchUpdate();
	    	} else if (CFG_common.P_MODIFY === id) {
	    		this.clickModify();
	    	} else if (CFG_common.P_VIEW === id) {
	    		this.clickUpdate(true);
	    	} else if (CFG_common.P_DELETE === id) {
	    		this.clickDelete(0);
	    	} else if (CFG_common.P_LOGICAL_DELETE === id) {
	    		this.clickDelete(1);
	    	} else if (CFG_common.P_REMOVE === id) {
	    		this.clickRemove();
	    	} else if (CFG_common.P_SEARCH === id) {
	    		this.clickSearch();
	    	} else if (CFG_common.P_REFRESH === id) {
	    		this.clickRefresh();
	    	} /*else if (CFG_common.P_NESTED_SEARCH === id) {
	    		this.clickNestedSearch();
	    	}*/ else if (CFG_common.P_BASE_SEARCH === id) {
	    		this.clickBaseSearch(event);
	    	} else if (CFG_common.P_GREAT_SEARCH === id) {
	    		this.clickGreatSearch(event);
	    	} else if (CFG_common.P_UPLOAD === id) {
	    		this.clickUpload();
	    	} else if (CFG_common.P_VIEW_DOCUMENT === id) {
	    		this.clickViewDocument();
	    	} else if (CFG_common.P_EXPORT === id) {
				this.clickExport();
			} else if (CFG_common.P_EXPORT_SETTING === id) {
				this.clickExportSetting();
			} else if (CFG_common.P_TRANSACT === id) { // 
	    		this.clickCoflowTransact();
	    	} else if (CFG_common.P_TRANSACT_AND_CHECKOUT === id) {
	    		this.clickCoflowTransactAndCheckout();
	    	} else if (CFG_common.P_CHECKOUT === id) {
	    		this.clickCoflowCheckout();
	    	} else if (CFG_common.P_REASSIGN === id) {
	    		this.clickCoflowReassign();
	    	} else if (CFG_common.P_RECALL === id) {
	    		this.clickCoflowRecall();
	    	} else if (CFG_common.P_UNTREAD === id) {
	    		this.clickCoflowUntread();
	    	} else if (CFG_common.P_SUSPEND === id) {
	    		this.clickCoflowSuspend();
	    	} else if (CFG_common.P_TERMINATION === id) {
	    		this.clickCoflowTermination();
	    	} else if (CFG_common.P_TRACK === id) {
	    		this.clickCoflowTrack();
	    	} else if (CFG_common.P_REPORT === id) {
	    		this.clickReport();
	    	} else if (id.match(/^\_subreport\_/)) {
	    		this.clickPrint(id.split(CFG_common.P_REPORT_PRE)[1]);
	    	} else if (id.match(/^\_\_\_secbtn\_/)) { // 二次开发按钮
	    		this.clickSecondDev(id);
	    	} else if (id.match(/^CD\_BUTTON\_/)) { // 组装按钮
				CFG_clickToolbar(this.assembleData, 
						CFG_getZoneName(("6" === this.assembleData.moduleType ? "" : this.options.tableId), $.config.contentType.grid, this.options.number), 
						id, 
						0, 
						this);
			} else if ("back2grid" === id) {
				this.clickBackToGrid();
	    	} 
	    },
	    // 返回或关闭
	    clickBackToGrid : function () {
	    	CFG_clickCloseButton(this.assembleData);
	    },
		// 新增
		clickCreate : function () {
			var formUI = this.getSelfForm(),
			    jqForm, isNested;
			if (false === formUI) {
				isNested = this.isNestedAssembleType(CFG_common.P_CREATE);
				jqForm = this.getNestedJq(isNested); // 导航 嵌入DIV
				formUI = new $.config.cform(this._formOptions(null, false, null, isNested), jqForm);
			} else {
				formUI.clickCreate();
			}
			return formUI;
		},
		// 添加一行列表数据进行编辑
		clickAdd : function () {
			var id = "UNSAVE_" + getTimestamp(),
			    rowData = {},
			    key = this.getKeyColumnName();
			rowData[key] = id;
			this.processEditedRowData(rowData);
			this.uiGrid.grid("addRowData", id, rowData, "last"); 
			this.uiGrid.grid("editRow", id);
		},
		// 修改
		clickUpdate : function (data) {
			var dataArr = null, formUI = null, isView = false, jqForm = null, isNested = true;
			if (typeof data === "string") {
				dataArr = [data];
			} else {
				if (typeof data === "boolean") {
					isView = data;
				}
				dataArr = this.getSelectedRowId();
			}
			if (isEmpty(dataArr)) {
				CFG_message("请先选择一条记录!", "warning");
				return;
			}
			
			if (dataArr.length > 1) {
				CFG_message("仅且只能选择一条记录!", "warning");
				return;
			}
			formUI = this.getSelfForm();
			if (false === formUI) {
				isNested = this.isNestedAssembleType(CFG_common.P_UPDATE);
				jqForm = this.getNestedJq(isNested); // 导航 嵌入DIV
				formUI = new $.config.cform(this._formOptions(dataArr[0], isView, null, isNested), jqForm);
			} else {
				formUI.load(dataArr[0]);
			}
			return formUI;
		},
		// 批量修改
		clickBatchUpdate : function () {
			var url = $.config.appActionPrefix + "/app-form!edit.json?tableId=" + this.options.tableId +
			          "&componentVersionId=" + this.options.tableId + 
			          "&menuId=" + this.options.menuId,
			    fcfg = $.loadJson(url),
			    panel= null;
			if (0 == fcfg.type) { // 弹出式
				this._createDialogBatchUpdate();
			} else {  // 嵌入式
				panel = this.getNestedJq();
				this._createNestedBatchUpdate(panel);
			}
		},
		// 前台修改（表单数据来源于列表）
		clickModify : function () {
			var idArr = null, formUI = null, formData = null, jqForm = null, isNested = true;
			idArr = this.getSelectedRowId();
			if (isEmpty(idArr)) {
				CFG_message("请先选择一条记录!", "warning");
				return;
			}
			
			if (idArr.length > 1) {
				CFG_message("仅且只能选择一条记录!", "warning");
				return;
			}
			formData = this.getRowData(idArr[0]);
			formUI = this.getSelfForm();
			if (false === formUI) {
				isNested = this.isNestedAssembleType(CFG_common.P_UPDATE);
				jqForm = this.getNestedJq(isNested); // 导航 嵌入DIV
				formUI = new $.config.cform(this._formOptions(null, false, null, isNested), jqForm);
				formUI.setFormData(formData);
			} else {
				formUI.setFormData(formData);
			}
			return formUI;
		},

		/**
		 * 删除数据
		 * @param isLogicalDelete
		 *        0--物理删除；1--逻辑删除
		 */
		clickDelete : function (isLogicalDelete) {
			var idArr = this.uiGrid.grid("option", "selarrrow");
			
			if (0 === idArr.length) {
				CFG_message( "请选择记录!", "warning");
				return;
			}
			this.databaseDelete(idArr, isLogicalDelete);
		},
		// 前台删除（不走后台）
		clickRemove : function () {
			var idArr = this.uiGrid.grid("option", "selarrrow"), 
				_this = this;
			if (0 === idArr.length) {
				CFG_message( "请选择记录！", "warning");
				return;
			}
			$.confirm("所选择的记录将被删除，确定吗？", function(sure) {
				if (!sure) {
					return;
				}
				for (var i = idArr.length; i > -1; i--) {
					_this.uiGrid.grid("delRowData", idArr[i]);
				}
				CFG_message("操作成功!", "success");
			});
		},
		// 刷新
		clickRefresh : function () {
			this.reload();
		},
		
		// 基本检索
		clickBaseSearch  : function (e) {
			this._openBS();
		},
		// 高级检索
		clickGreatSearch  : function (e) {
			this._openGS();
		},
		
		// 电子文件上传
		clickUpload : function () {
			var tableId = this.options.tableId,
			    rowIdArr= this.getSelectedRowId(),
			    _this   = this,
			    masterDataId,
			    master  = this.options.master;
			if (1 == this.options.number) {
				if (master) masterDataId = null2empty(master.dataId);
			} else {
				masterDataId = this.getMasterId("DATA");
			}
			$.ajax({
				url : this.getAction() + "!checkUpload.json?P_tableId=" + tableId + "&P_menuCode=" + this.getParamValue("menuCode"),
				dataType : "json",
				success : function (data, status, jqXHR) {
					var id, upOpt, url;
					if (!data.success) {
						CFG_message(data.message, "warning"); return;
					}
					if (tableId !== data.message) {
						if (0 == rowIdArr.length) {
							CFG_message("请选择一条记录，再上传!", "warning"); return;
						} else if (rowIdArr.length > 1) {
							CFG_message("一次只能选择一条记录进行上传!", "warning"); return;
						}
						id = rowIdArr[0];
					} else {
						id = masterDataId;
						if (master) {
							tableId = master.tableId;
						}
					}
					if (isNotEmpty(_this.options.box)) {
						id = id.split("_")[0];
					}
					url = _this.getAction() + "!upload.json;jsessionid="+jSessionId+"?id=" + id + "&P_tableId=" + tableId + "&P_docTableId=" + data.message 
					                        + "&P_componentVersionId=" + _this.options.componentVersionId 
					                        + "&P_menuId=" + _this.options.menuId
					                        + "&P_menuCode=" + _this.getParamValue("menuCode")
					                        + "&P_currentUserId=" + currentUserId;
					upOpt = {
							uploader : url,
							onUploadSuccess : function(file, data, response) {
								_this.reload();
						    }
						};
					upOpt = _this.beforeInitUpload(upOpt);
					CFG_upload(_this.options.global, upOpt, {
						appendTo : _this.getDialogAppendTo(),
						position : {
							of : _this.getDialogAppendTo()
						}});
				}, 
				error : function (req, status, err) {
					CFG_message("文件上传校验出错，请联系管理员!", "error");
				}
			});
		},
		// 电子全文上传初始化前处理
		beforeInitUpload : function (opt) {
			return opt;
		},
		// 查看电子全文
		clickViewDocument : function (id) {
			var tableId = this.options.tableId,
			    rowIdArr= null, _this   = this;
			// ID处理
			if (id) {
				rowIdArr = [id];
			} else {
				rowIdArr = this.getSelectedRowId();
			}
			// ID检查
			if (0 == rowIdArr.length) {
				CFG_message("请选择一条记录，再查看电子全文！", "warning"); 
				return;
			} else if (rowIdArr.length > 1) {
				CFG_message("一次只能选择一条记录查看电子全文！", "warning"); 
				return;
			}
			// 查看电子全文
			$.ajax({
				url : this.getAction() + "!checkUpload.json?P_tableId=" + tableId + "&P_menuCode=" + this.getParamValue("menuCode"),
				dataType : "json",
				success : function (data, status, jqXHR) {
					var id = rowIdArr[0];
					if (isNotEmpty(_this.options.box)) {
						id = id.split("_")[0];
					}
					if (!data.success) {
						CFG_message(data.message, "warning"); 
						return;
					}
					// 本列表不是电子全文列表
					if (tableId !== data.message) {
						_this.toSelectDocument(data.message, id);
					} else {
						_this.toViewDocument(tableId, id);
					}
				}, 
				error : function (req, status, err) {
					CFG_message("文件上传校验出错，请联系管理员!", "error");
				}
			});
		},
		// 弹出电子全文选择窗口
		toSelectDocument : function (docTableId, masterDataId) {
			var _this = this,
			    idArr = this.getSelectedRowId(),
			    jqGlobal = this.getDialogAppendTo(),
			    jqDlg, url, docId, html, sbarPanel;
			jqDlg = $("<div>").appendTo(jqGlobal);
			jqDlg.dialog({
				autoOpen : false,
				appendTo : jqGlobal,
				title : "选择电子全文",
				width : 400,
				maxWidth : 400,
				height : 230,
				maxHeight : 230,
				modal : true,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					if($(".uploadify", $(this)).length>0){
						$(".uploadify", $(this)).uploadify('destroy');
					}
					$(this).remove();
				}
			});
			jqDlg.dialog("buttonPanel").addClass("app-bottom-toolbar");
			// 表单信息
			url = this.getAction() + "!documentOptions.json?P_tableId=" + docTableId + "&id=" + masterDataId;
			docId = generateId("DOC_FORM");
			html = "<form class=\"coralui-form\" data-options=\"heightStyle:&quot;fill&quot;\">" +
						"<div class=\"fillwidth colspan1 clearfix\">" + 
							"<div style=\"padding-left:30px;padding-top:20px;\">" +
								"<label style=\"width:80px;\">电子全文：</label>" +
								"<input id=\"" + docId + "\" class=\"coralui-combobox\" data-options=\"width:245,name:&quot;documentId&quot;,url:&quot;"+url+"&quot;\"/>" +
							"</div>" +
						"</div>" +
					"</form>";
			jqDlg.append(html);
			// 解析表单
			$.parser.parse(jqDlg);
			// 打开表单窗体
			jqDlg.dialog("open");
			// 底部工具条
			sbarPanel = $("<div>").appendTo(jqDlg.dialog("buttonPanel"));
			sbarPanel.toolbar({
				data: ["->",
				       {id:"view", label:"查看", type:"button"},
				       {id:"close", label:"关闭", type:"button"}],
				onClick: function(e, ui) {
					if ("view" == ui.id) {
						var id = $("#" + docId).combobox("getValue");
						if (isEmpty(id)) {
							CFG_message("请选择电子全文再查看！", "warning");
							return;
						}
						_this.toViewDocument(docTableId, id);
					} else if ("close" == ui.id) {
						jqDlg.dialog("close");
					}
				}
			});
		},
		// 打开或下载文件
		toViewDocument : function (tableId, id) {
			var url = this.getAction() + "!viewDocument?id=" + id + "&P_tableId=" + tableId + "&P_menuCode=" + this.getParamValue("menuCode"),
			    left = ($(window).width() - 800)/2,
			    top  = ($(window).height() - 600)/2;
			window.open(url, "查看电子全文", 
					"left=" + left + ",top=" + top + 
					",width=800,height=600,toolbar=no,menubar=no,scrollbars=no,resizable=yes,location=no,status=no");
		},
		//导出
		clickExport : function(){
			var url = $.contextPath + "/appmanage/table-imp-export!exportExcel.json?P_tableId=" + this.options.tableId
	          + "&P_componentVersionId=" + this.options.componentVersionId + "&P_timestamp=" + this.options.globalTimestamp + "&P_ids=" + this.getSelectedRowId().join(",")
	          + "&P_menuId=" + this.options.menuId;
			$.ajax({
				url : $.contextPath + "/appmanage/app-export!isSet.json?P_tableId=" + this.options.tableId
		  	          + "&P_componentVersionId=" + this.options.componentVersionId
			          + "&P_menuId=" + this.options.menuId,
				dataType : "json",
				success : function (rlt) {
					if (rlt.success) {
						download(url);
					} else {
						CFG_message("请先配置导出信息!", "warning");
					}
				}, 
				error : function () {
					CFG_message("操作失败", "error");
				}
			});
		},
		//导出设置
		clickExportSetting : function() {
		//  if disabled, return
			if (this.options.disabled) return;
			// 关闭基本检索
			this._closeBS();
			// 打开导出设置
			var url = $.contextPath + "/cfg-resource/coral40/common/jsp/appexport/CFG_appexportsetting.jsp?tableId=" + this.options.tableId
			          + "&componentVersionId=" + this.options.componentVersionId
			          + "&menuId=" + this.options.menuId
			          + "&cgridDivId=" + this.element.attr("id")
			          + "&idSuffix=" + this.options.globalTimestamp,
			    _this = this; 
			CFG_openComponent(this.assembleData, url);
		},
		// 打印报表
		clickReport : function () {
			var _this = this,
			    idArr = this.getSelectedRowId(),
			    jqGlobal = this.getDialogAppendTo();
			var reportDlg = $("<div>").appendTo(jqGlobal);
			reportDlg.dialog({
				autoOpen : false,
				appendTo : jqGlobal,
				title : '打印报表',
				width : 400,
				maxWidth : 400,
				height : 230,
				maxHeight : 230,
				modal : true,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					reportDlg.remove();
				}
			});
			reportDlg.dialog("buttonPanel").addClass("app-bottom-toolbar");
			// 表单信息
			var reportUrl = this.getAction() + "!report.json?E_frame_name=coral&P_tableId=" + this.options.tableId + 
					"&P_moduleId=" + this.options.moduleId +
					"&P_menuId=" + this.options.menuId +
					"&P_componentVersionId=" + this.options.componentVersionId;
			var formId = generateId("FORM");
			var formCfgData = "<form class=\"coralui-form\" id=\""+formId+"\" data-options=\"heightStyle:&quot;fill&quot;\">" +
						"<div class=\"fillwidth colspan1 clearfix\">" +
						"<div style=\"padding-left:60px;padding-top:10px;\">" +	
							"<div class=\"coralui-radiolist radio-scope\" data-options=\"name:&quot;scope&quot;,data:&quot;0:所选记录&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;;1:查询结果&quot;\"></div>" +
						"</div>" +	
							"<div style=\"padding-left:60px;padding-top:20px;\">" +
								"<label style=\"width:80px;\">可用报表：</label>" +
								"<input class=\"coralui-combobox\" data-options=\"name:&quot;report&quot;,url:&quot;"+reportUrl+"&quot;\"/>" +
							"</div>" +
						"</div>" +
					"</form>";
			reportDlg.append(formCfgData);
			var reportForm = $("#" + formId);
			// 解析表单
			$.parser.parse(reportDlg);
			if (null == idArr || 0 == idArr.length) {
				$(".radio-scope", reportForm).radiolist("setValue","1");
				$(".radio-scope", reportForm).radiolist("disableItem", "0");
			} else {
				$(".radio-scope", reportForm).radiolist("setValue","0");
				$(".radio-scope", reportForm).radiolist("enableItem", "0");
			}
			// 打开表单窗体
			reportDlg.dialog("open");
			// 底部工具条
			this._createReportSbar(reportDlg, reportForm);
		},
		// 底部工具条
		_createReportSbar : function(reportDlg, reportForm) {
			var _this = this, 
			    sbarPanel = $("<div>").appendTo(reportDlg.dialog("buttonPanel"));
			sbarPanel.toolbar({
				data: ["->", {id:"save", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
				onClick: function(e, ui) {
					if ("save" == ui.id) {
						var formData = reportForm.form("formData", false);
						if (isEmpty(formData.report)) {
							CFG_message("请选择要打印的报表！", "warning");
							return;
						}
						_this.clickPrint(formData.report, formData.scope);
					} else if ("cancel" == ui.id) {
						reportDlg.dialog("close");
					}
				}
			});
		},
		// 打印
		clickPrint  : function (id, scope) {
	        var url = $.cuiFolder + "/views/config/appmanage/report/print.jsp?P_reportId=" + id +
	        		"&P_tableId=" + this.options.tableId + "&P_timestamp=" + this.options.globalTimestamp,
	            rowIdArr = this.getSelectedRowId().toString(),
	            data, filter;
	        if (!$.browser.msie) {
	        	url = $.contextPath + "/cfg-resource/cell/views/reportpreview.jsp?reportPrintUrl=" + url;
	        }
	        if (scope == "0" && rowIdArr && rowIdArr.length > 0) {
	        	url += "&P_rowIds=" + rowIdArr.toString();
	        }
	        window.open(encodeURI(url), "打印", 'left=0,top=0,width=' + (screen.availWidth - 10) + ',height='
	                + (screen.availHeight - 50)
	                + ',toolbar=yes,menubar=yes,scrollbars=yes,resizable=yes,location=yes,status=yes');
		},
		// 工作流： 办理
		clickCoflowTransact : function (rowId) {
			this.clickUpdate(rowId);
		},
		// 工作流： 办理并签收
		clickCoflowTransactAndCheckout : function () {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			var _this   = this,
			    wStatus = this._getWorkitemStatus(rowId);
			
			if (wStatus == 11) {
				$.ajax({
					url : this._getCoflowUrl(CFG_common.P_CHECKOUT, rowId),
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							var rowData = _this.uiGrid.grid("getRowData", rowId);
							rowData[CFG_common.W_STATUS] = "12";
							_this.uiGrid.grid("setRowData", rowId, rowData);
							_this.clickCoflowTransact(rowId);
						} else if (false === rlt.success) {
							CFG_message( rlt.message, "warning");
						} else {
							CFG_message("签收失败!", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败!", "error");
					}
				});
			} else {
				this.clickCoflowTransact();
			}
		},
		// 工作流： 签收
		clickCoflowCheckout : function () {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			var _this = this,
			    wStatus = this._getWorkitemStatus(rowId);
			if (wStatus == 11) {
				$.ajax({
					url : this._getCoflowUrl(CFG_common.P_CHECKOUT, rowId),
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.reload();
							CFG_message("签收成功!", "success");
						} else if (false === rlt.success) {
							CFG_message(rlt.message, "warning");
						} else {
							CFG_message("签收失败!", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败!", "error");
					}
				});
			}
		},
		// 工作流： 跟踪
		clickCoflowTrack : function () {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			var pid = this._getProccessInstanceId(rowId);
			if (false === pid) {
				$.message("列表未配置“流程实例ID”项，请联系管理员！", "warning");
				return;
			}
	        var url = this.getAction() + "!coflowTrack?P_op=graph&P_processInstanceId=" + pid + "&P_menuCode=" + this.getParamValue("menuCode");
	        var iWidth = window.screen.width;
	        var iHeight = window.screen.height;
	        var win = window.open(url, "流程跟踪", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth
	                + ',innerWidth=' + iWidth
	                + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	        return win;
		},
		// 工作流： 退回
		clickCoflowUntread : function() {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			
			var _this = this, 
			    jqGlobal = this.getDialogAppendTo(),
			    jqUC = $("<div>").appendTo(jqGlobal);
			
			// 退回意见窗体
			jqUC.dialog({
				appendTo : jqGlobal,
				modal : true,
				title : "退回审批意见",
				width : 400,
				height : 240,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					_this.close(jqUC);
				},
				onCreate : function() {
					var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
					var jq = $("<textarea id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
					jq.textbox({height: 188, maxlength: 500});
				},
				onOpen : function () {
					var jqPanel = $(this).dialog("buttonPanel"),
					    jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
					jqPanel.addClass("app-bottom-toolbar");
					jqDiv.toolbar({
						data: ["->", {id:"sure", label:"确定", type:"button"},{id:"cancel", label:"取消", type:"button"}],
						onClick: function(e, ui) {
							if ("sure" === ui.id) {
								_untread();
							} else {
								_this.close(jqUC);
							}
						}
					});
				}
			});
			
			function _untread() {
				var opinion = $("#UNTREAD_OPINION_" + _this.uuid).val(), 
			        url     = _this._getCoflowUrl(CFG_common.P_UNTREAD, rowId);
				$.ajax({
					url : url,
					dataType : "json",
					data : {P_opinion: opinion},
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.close(jqUC, true);
							_this.reload();
							CFG_message("操作成功！", "success");
						} else if (false === rlt.success) {
							CFG_message( rlt.message, "warning");
						} else {
							CFG_message("退回失败！", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败！", "error");
					}
				});
			}
		},
		// 工作流： 撤回
		clickCoflowRecall : function() {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			
			var _this = this, url = _this._getCoflowUrl(CFG_common.P_RECALL, rowId);
			$.confirm("您确定要撤回流程？", function(sure) {if (sure) _recall();});
			//
			function _recall() {
				var url = _this._getCoflowUrl(CFG_common.P_RECALL, rowId);
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.reload();
							CFG_message("操作成功！", "success");
						} else if (false === rlt.success) {
							CFG_message(rlt.message, "warning");
						} else {
							CFG_message("撤回失败！", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败！", "error");
					}
				});
			}
		},
		// 工作流： 转办
		clickCoflowReassign : function() {
			var rowId = this.checkSelectOneRowId();			
			if (false === rowId) return;
			
			var _this = this, 
			    jqGlobal = this.getDialogAppendTo(),
			    height = jqGlobal.height(), 
			    jqUC = $("<div>").appendTo(jqGlobal), 
			    jqTR = $("<ul>").appendTo(jqUC);
			
			if (height > 360) height = 360;
			// 转办窗体
			jqUC.dialog({
				appendTo : jqGlobal,
				modal : true,
				title : "转办-选择用户",
				width : 320,
				height : height,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					_this.close(jqUC);
				},
				onCreate : function() {
					var setting = {
						asyncAutoParam : "id",
						asyncUrl : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=user&P_ASYNC=true" + "&P_menuCode=" + _this.getParamValue("menuCode"),
						asyncEnable : true,
						asyncType : "post",
						beforeClick : function(treeId, treeNode) {
							return CFG_combotreeUser(treeId, treeNode);
						}
					};
					jqTR.tree(setting, [{name : "组织机构树", isParent : true, id : "-1"}]);
				},
				onOpen : function () {
					var jqPanel = $(this).dialog("buttonPanel"),
					    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
					jqPanel.addClass("app-bottom-toolbar");
					jqDiv.toolbar({
						data: ["->", {id:"sure", label:"确定", type:"button"},{id:"cancel", label:"取消", type:"button"}],
						onClick: function(e, ui) {
							if ("sure" === ui.id) {
								_reassign();
							} else {
								_this.close(jqUC);
							}
						}
					});
				}
			});
			// 转办
			function _reassign() {
				var url   = _this._getCoflowUrl(CFG_common.P_REASSIGN, rowId), 
				    sNode = jqTR.tree("getSelectedNodes");
				if (!sNode || sNode.length === 0) {
					$.alert("请选择一个用户！");
					return;
				}
				url += "&P_targetUserIds=" + sNode[0].id;
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.close(jqUC, true);
							_this.reload();
							CFG_message("操作成功！", "success");
						} else if (false === rlt.success) {
							CFG_message( rlt.message, "warning");
						} else {
							CFG_message("转办失败！", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败！", "error");
					}
				});
			}
		},
		// 工作流：中止（删除）
		clickCoflowSuspend : function() {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			var _this = this,
			    url   = this._getCoflowUrl(CFG_common.P_SUSPEND, rowId);
			$.confirm("确定要删除选中流程吗？", 
					function(sure) {
						if (sure) {
							_suspend();
						}
					}
			);
			function _suspend() {
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							CFG_message("操作成功！", "success");
							_this.reload();
						} else if (false === rlt.success) {
							CFG_message( rlt.message, "warning");
						} else {
							CFG_message("中止（删除）流程失败！", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败！", "error");
					}
				});
			}
		},
		// 工作流：终止
		clickCoflowTermination : function() {
			var rowId = this.checkSelectOneRowId();
			if (false === rowId) return;
			var _this = this,
			    url   = this._getCoflowUrl(CFG_common.P_TERMINATION, rowId);
			$.ajax({
				url : url,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						CFG_message("操作成功！", "success");
						_this.reload();
					} else if (false === rlt.success) {
						CFG_message( rlt.message, "warning");
					} else {
						CFG_message("终止流程失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 关闭窗体
		close : function(jq, isRefresh) {
			if (jq) {
				jq.dialog("close");
				jq.remove();
			}
			if (isRefresh) this.reload();
		},
		// 检查是否选中一条记录
		checkSelectOneRowId : function () {
			var rowIdArr = this.getSelectedRowId(), rowId;
			if (null == rowIdArr || 0 === rowIdArr.length) {
				CFG_message("请选择一条记录！", "warning");
				return false;
			}
			if (rowIdArr.length > 1) {
				CFG_message("一次只能选择一条记录！", "warning");
				return false;
			}
			rowId = rowIdArr[0];
			
			if (isNotEmpty(this.options.box)) {
				// 工作流视图ID是由 业务表ID + "_" + T_WF_WORKITEM.ID 组成
				rowId = rowId.split("-")[0]; 
			}
			return rowId;
		},
		// 
		getDialogAppendTo : function () {
			//CFG_getDialogParent(_this.assembleData) || $(this.options.global)
			//return CFG_getDialogParent(this.assembleData) || $("body");
			return $("body");
		},
		// 下拉框检索
		comboboxSearch : function (e, data) {
			if (this.inited) {
				this.load();
			}
		},
		// 切换列表模式
		clickSwitchGridModel : function (jq) {
		    //  if disabled, return
			if (this.options.disabled) return;
			//
			jq.toggleClass("app-toolbar-txtgrid");
			jq.toggleClass("app-toolbar-picgrid");
			var model= this.uiGrid.grid("option", "model");
			this.uiGrid.grid("option", "model", ("card" === model ? "grid" : "card"));
			this.uiGrid.grid("reload");
		},
		// 二次开发按钮事件
		clickSecondDev : function (id) {},
		// 详细页面
		viewDetailForm : function(rowId, title) {
			var isNested = this.isNestedAssembleType(CFG_common.P_UPDATE);
			var jqForm = this.getNestedJq(isNested); // 导航 嵌入DIV
			if (isEmpty(title)) title = "详细";
			return new $.config.cform(this._formOptions(rowId, true, title, isNested), jqForm);
		},
		// 明细
		viewDetailGrid : function(tableId, masterDataId, title) {
			var jq = this.getNestedJq(), _this = this,
			    options;
			// 生成自定义列表options对象
			options = {
				menuId : this.options.menuId, // 菜单ID
				componentVersionId : this.options.componentVersionId,
				moduleId: this.options.moduleId,
				tableId : tableId,
				number: 1,
				model : 1, // 1-普通列表 2-缩略图列表
				master  : {tableId: this.options.tableId, dataId: masterDataId},
				global  : this.options.global
			}; 
			// 默认标题设置
			if (isEmpty(title)) title = "明细";
			// 向导航条添加导航信息
			CFG_addNavigationItem(this.assembleData, jq, title);
			// 打开明细列表
			jq.cgrid(options);
			// 手动添加返回按钮
			jq.cgrid("addReturnButton", function() {
				CFG_removeJQ(jq);   // 删除明细列表
				_this.reload();// 重新加载父列表数据
				// 清空导航条中相应信息
				CFG_clickReturnButton(_this.assembleData);
			});
		},
		// 删除前台列表数据
		linkDeleteGD : function (id) {
			var _this = this;
			$.confirm("当前记录将从列表移除，确定吗？", function(sure) {
				if (sure) {
					_this.uiGrid.grid("delRowData", id);
				}
			});
		} ,
		// 删除(后台删除)
		linkDeleteDB : function (id) {
			var idArr = [];
			idArr.push(id);
			this.databaseDelete(idArr, 0);
		} ,
		// 删除(后台删除)
		linkDeleteLG : function (id) {
			var idArr = [];
			idArr.push(id);
			this.databaseDelete(idArr, 1);
		} ,
		// 详细
		linkViewDForm : function (id) {
			this.viewDetailForm(id);
		},
		// 明细
		linkViewDGrid : function (id) {
			var tableId = this.getDetailTableId();
			this.viewDetailGrid(tableId, id);
		},
		// 修改
		linkUpdate : function (id) {
			var isNested = this.isNestedAssembleType(CFG_common.P_UPDATE);
			var jq = this.getNestedJq(isNested); // 导航 嵌入DIV
			return new $.config.cform(this._formOptions(id, false, null, isNested), jq);
		},
		// 查看电子全文
		linkViewDocument : function (id) {
			this.clickViewDocument(id);
		},
		// 列表超链接事件总入口
		clickLink : function (btnId, dataId) {
			// 点击超链接前事件处理
			if (false === this.beforeClickLink(btnId, dataId)) return;
			// 触发超链接
			if (CFG_common.P_LINK_DELETE_GD === btnId) {
				this.linkDeleteGD(dataId);
			} else if (CFG_common.P_LINK_DELETE_DB === btnId) {
				this.linkDeleteDB(dataId);
			} else if (CFG_common.P_LINK_DELETE_LG === btnId) {
				this.linkDeleteLG(dataId);
			} else if (CFG_common.P_LINK_UPDATE === btnId) {
				this.linkUpdate(dataId);
			} else if (CFG_common.P_LINK_VIEW_DFORM === btnId) {
				this.linkViewDForm(dataId);
			} else if (CFG_common.P_LINK_VIEW_DGRID === btnId) {
				this.linkViewDGrid(dataId);
			} else if (CFG_common.P_LINK_VIEW_DOCUMENT === btnId) {
				this.linkViewDocument(dataId);
			}
		},
		
		/**
		 * 点击超链接前事件处理
		 * @return boolean
		 *         true: 继续执行；false: 停止执行
		 **/
		beforeClickLink : function (btnId, dataId) {
			return true;
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
		// 获取组件库列表DIV的JQUERY对象
		getGridJQ : function () {
			return this.uiGrid;
		},
		
		/**
		 * 校验列表数据
		 * @param rowId -- 指定行ID（为可选参数），如果不传的话，则核验整个列表数据
		 * @returns {Boolean}
		 */
		validGridData : function (rowId) {
			var rowIdArr = [], valid = false;
			if (typeof rowId === "undefined") {
				rowIdArr = this.uiGrid.grid("getDataIDs");
			} else {
				rowIdArr.push(rowId);
			}
			for (var i = 0, len = rowIdArr.length; i < len; i++) {
				valid = this.uiGrid.grid("valid",rowIdArr[i]);
				if (false === valid) return false;
			}
			return true;
		},
		
		/**
		 * 设置options中的属性值
		 * @param key
		 * @param value
		 * @returns
		 */
		setGridOption : function (key, value) {
			this.uiGrid.grid("option", key, value);
		},
		
		/**
		 * 获取options中的属性值
		 * @param key
		 * @returns
		 */
		getGridOption : function (key) {
			return this.uiGrid.grid("option", key);
		},

		/**
		 * 调用表单组件方法，
		 *    如getDataIDs方法：ui.callGridMethod("getDataIDs");
		 *     getRowData方法：ui.callGridMethod("getRowData", rowId);
		 * @param method
		 *            -- 方法名
		 * @param args
		 *            -- 为方法(method)对应的参数列表（可选参数），如果method没有参数，则不需要传入
		 */
		callGridMethod : function (method, args) {
			var jq = this.uiGrid;
			return jq.grid.apply(jq, arguments);
		},
		
		/**
		 * 判断按钮类型是否为嵌入式: 默认为嵌入式
		 */
		isNestedAssembleType : function (btnId) {
			if (btnId && this.uiTbar) {
				var btnObj = this.uiTbar.toolbar("getSubCoral", btnId);
				if (btnObj && isNotEmpty(btnObj.type)) {
					return ("1" === btnObj.$el[btnObj.type]("option", "assembleType"));
				}
			}
			return true;
		}, 
		
		// 表单options
		_formOptions : function (dataId, isView, title, isNested) {
			var masterUI = null, master = null, jqUI = null, masterDataId = null, idArr = null;
			master = this.options.master;
			// sequence > 1 是追溯项目一主表（表单）两从表（列表）时布局二次开发中使用的
			if (!master || (this.options.sequence > 1 && isEmpty(master.dataId))) {
				masterUI = this._getMaster();
				// 关联主表信息
				if (masterUI) {
					jqUI  = masterUI.jqUI;
					if ($.config.contentType.isGrid(masterUI.type)) {
						idArr = jqUI.getSelectedRowId();
						if (idArr.length == 0) {
							CFG_message("请选择一条主列表记录!", "warning");
							return;
						} else if (idArr.length > 1) {
							CFG_message("只能选择一条主列表记录!", "warning");
							return;
						}
						masterDataId = idArr[0];
					} else {
						masterDataId = jqUI.getDataId();
					}
					master = {"tableId": masterUI.tableId, "dataId": masterDataId};
				}
			}
			if (undefined === isNested)  isNested = true;
			return {
				menuId  : this.options.menuId,
				moduleId: this.options.moduleId,
				tableId : this.options.tableId,
				componentVersionId : this.options.componentVersionId,
				workflowId: null2empty(this.options.workflowId),
				box     : null2empty(this.options.box),
				columns : this.options.columns,
				number  : this.options.number,
				isNested: isNested,
				dataId  : dataId,
				gDivId  : this.gridId(),
				title   : title,
				master  : master,
				isView  : (isView ? true : false),
				global  : this.options.global
			};
		},
		// 添加回调方法 callback.name
		addCallback : function (name, fn) {
			if (!this.callback) this.callback = {};
			this.callback[name] = fn;
		},
		// 添加传递参数函数（传递给组装构件） value.name
		addOutputValue : function (name, fn) {
			if (!this.value) this.value = {};
			this.value[name] = fn;
		},
		/********************************** 预留区默认回调函数、输出参数函数(注：请勿复写)--开始 **********************************/
		_defaultOutput : function () {
			// 回调函数
			if (!this.callback) this.callback = {};
			// 重新加载数据
			this.callback["reload"] = function () {
				this.reload();
			};
			// 重新加载上层列表数据
			this.callback["reloadPreviousGrid"] = function () {
				this.reloadPreviousGrid();
			};
			// 重新加载主列表数据
			this.callback["reloadMasterGrid"] = function () {
				this.reloadMasterGrid();
			};
			// 重新加载从列表数据
			this.callback["reloadDetailGrid"] = function () {
				this.reloadDetailGrid();
			};
			// 重新渲染列表
			this.callback["refresh"] = function () {
				this.refresh();
			};
			// 重新渲染上层列表
			this.callback["refreshPreviousGrid"] = function () {
				this.refreshPreviousGrid();
			};
			// 重新渲染主列表
			this.callback["refreshMasterGrid"] = function () {
				this.refreshMasterGrid();
			};
			// 重新渲染从列表
			this.callback["refreshDetailGrid"] = function () {
				this.refreshDetailGrid();
			};


			// 输出参数函数
			if (!this.value) this.value = {};
            /* 选中一行的数据ID. */
			this.value["selectedRowId"] = function() {
                var ids = this.getSelectedRowId();
                if (isEmpty(ids) || 0 === ids.length) {
                	CFG_message("请选择一条记录!", "warning");
                    return {
                        status : false,
                        id : ""
                    };
                }
                if (ids.length > 1) {
                	CFG_message("只能选择一条记录!", "warning");
                    return {
                        status : false,
                        id : ""
                    };
                }
                return {
                    status : true,
                    id : ids[0]
                };
            };
            /* 选中行的数据ID. */
            this.value["selectedRowIds"] = function() {
                var ids = this.getSelectedRowId();
                if (isEmpty(ids) || 0 === ids.length) {
                	CFG_message("请至少选择一条记录!", "warning");
                    return {
                        status : false,
                        id : ""
                    };
                }
                return {
                    status : true,
                    ids : ids.toString()
                };
            };
            /* 选中行的数据ID，允许为空. */
            this.value["selectedRowIdsAllowBlank"] = function() {
                var ids = this.getSelectedRowId();
                return {
                    status : true,
                    ids : ids.toString()
                };
            };
            /* 所有行的数据ID. */
            this.value["allRowIds"] = function () {
            	return this._getAllRowIds();
            };
            /* 选中行的数据JSON字符串值. */
            this.value["rowValue"] = function () {
                if (isEmpty(this.selectedRowId)) return "{}";
                var rowData = this.uiGrid.grid("getRowData", this.selectedRowId);
            	return JSON.stringify(rowData);
            };
            /* 当前列表对应的表ID. */
            this.value["tableId"] = function () {
            	return this.options.tableId;
            };
            /* 当前列表所处的模块ID. */
            this.value["moduleId"] = function () {
            	return this.options.moduleId;
            },
            /* 当前列表所处的版本构件ID. */
            this.value["componentVersionId"] = function () {
            	return this.options.componentVersionId;
            };
            /* 当前列表所处的菜单ID. */
            this.value["menuId"] = function () {
            	return this.options.menuId;
            };
            /* 如果列表与树存在关系，则为列表对应的树节点ID. */
            this.value["treeNodeId"] = function () {
            	var data = $.config.getGlobalData(this.options.global);
            	return data.treeNodeId;
            };
            /* 获取物理表组ID. */
            this.value["groupId"] = function () {
            	var data = $.config.getGlobalData(this.options.global);
            	return data.groupId;
            };
			/* 上一次查询的查询条件.*/
            this.value["queryCondition"] = function () {
				var url = this.getAction() + "!getQueryCondition.json?P_tableId=" + this.options.tableId + "&P_timestamp=" + this.options.globalTimestamp + "&P_menuCode=" + this.getParamValue("menuCode");
				return $.loadJson(url);
			};
			/* (父)列表表ID.*/
			this.value["masterTableId"] = function () {
				return this.getMasterId();
			};
			/* (父)列表数据ID.*/
			this.value["masterDataId"] = function () {
				return this.getMasterId("DATA");
			};
			/* (从)列表表ID(直接在前台列表关系中获取).*/
			this.value["detailTableId"] = function () {
				var tableIds = this.getDetailTableIds();
				if (isEmpty(tableIds)) return "";
				return tableIds.split(",")[0];
			};
			/* 列表对应电子全文表ID.*/
			this.value["documentTableId"] = function () {
				return this.getDocumentTableId();
			};
			/* 时间戳 (作用：通过个时间戳可以从session获取这个列表的过滤条件与排序方式).*/
			this.value["timestamp"] = function () {
				return this.options.globalTimestamp;
			};
			/* 自定义列表构件(DIV)ID(作用可以获取自定义列表对象并可以操作这个列表).*/
			this.value["cgridDivId"] = function () {
				return this.element.attr("id");
			};
			/* 被组装的自定义列表构件(DIV)ID(作用可以获取被组装的自定义列表对象并可以操作这个列表).*/
			this.value["masterCgridDivId"] = function () {
				return $.config.getMasterCgridDivId(this.options.global);
			};
			/* 其他参数（二次开发时手动给ui.options.otherParams设值，提供给其他构件默认方法）
			 * 如果是对象，则返回这个对象的字符形式"{\"name\":\"qiucs\",\"dept\":\"{\"name\":\"dept1\"}\"}"
			 * 如果是string/number/boolean，则直接返回
			 * 如果是function，则返回函数的返回值
			 * 如果是对象，在后台可以用com.ces.config.utils.JsonUtil来解析获取指定属性的值
			 **/
			this.value["otherParams"] = function () {
				return $.config.toString(this.options.otherParams);
			};
		}
		/********************************** 预留区、回调函数、输出参数函数--结束 **********************************/
	});

})( jQuery );