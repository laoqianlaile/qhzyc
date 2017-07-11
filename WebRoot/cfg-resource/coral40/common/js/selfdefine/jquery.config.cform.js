/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成配置表单JS
 * 
 * 依赖JS:
 *    
 */
(function($, undefined) {

	"use strict";

	$.component("config.cform", {
		version : $.config.version,
		colspan : [ "60%", "60%", "70%", "80%" ],
		options : {
			menuId : "-1", // 菜单ID
			componentVersionId : "-1",
			moduleId: "-1",
			tableId : null,
			workflowId : null,
			box        : null,
			columns : null,
			number  : 1,   // 当前表单在构件中的区域次序
			isNested : true, // 是否为嵌入式表单(如果为false，则根据表单自身设置为显示)
			gDivId : null,
			dataId : null, // 数据ID(修改/查看记录)
			isView : false,// 是否查看
			viewModel : $.config.defaults.viewModel, // readonly-只读模式/isLabel-阅读模式（只有isView=true时才起作用）
			master : null, // 关联主表信息 {tableId:"", dataId: ""}
			isAlone: false,// 是否为单独使用
			global : null,
			sequence: 1, // 当前表单在构件中所有表单中的次序
			title  : null, // 
			backBtn : {
				position : {},// 返回或关闭按钮位置格式： {top: left/center/right/false, bottom: left/center/right/false}
				cls : "",     // 返回或关闭按钮样式
				icon: "",     // 返回或关闭按钮图样
			},
			toolbar: true,
			statusbar: true,
			/**
			 * 用new $.config.cform(opt, jq)时，复写自身
			 * function() {
			 *     var cform = this;
			 *     cform.bindEvent = function() {
			 *         ...
			 *     };
			 * }
			 * */
			override : null // function () {};
		},
		/**
		 * 创建构件
		 */
		_create : function() {
	    	var globalTimestamp, override;
			if (!this.options.global) {
				this.options.global = this.element;
			}
	    	this.element.addClass("config-form");
	    	if (this.options.isNested) {
	    		this.element.addClass("nested");
	    	} else {
	    		this.element.addClass("popup");
	    	}
	    	this.uiForm = this.element;
			// override current form
			globalTimestamp = $.config.getTimestampData(this.options.global);
	    	if (isEmpty(globalTimestamp)) {
	    		globalTimestamp = getTimestamp();
	    		$.config.setTimestampData(this.options.global, globalTimestamp);
	    	}
    	    this.options.globalTimestamp = globalTimestamp;
			// 构件组装数据
			this.assembleData = $.config.getAssembleData(this.options.global);
			// 如果工具条没指定，则指系统默认配置处理
			/*if (null === this.options.toolbar) {
				if (this.options.isNested) {
					this.options.toolbar = ("bottom" !== $.config.defaults.tbarPos.form.nested);
				} else {
					this.options.toolbar = ("bottom" !== $.config.defaults.tbarPos.form.popup);
				}
			}
			if (null === this.options.statusbar) {
				if (this.options.isNested) {
					this.options.statusbar = ("top" !== $.config.defaults.tbarPos.form.nested);
				} else {
					this.options.statusbar = ("top" !== $.config.defaults.tbarPos.form.popup);
				}
			}*/
			// override current form
    	    override = this.options.override;
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
    	    override = window[CFG_overrideName(globalTimestamp)];
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
			// 是否有列表
			this.hasGrid = isNotEmpty(this.options.gDivId);
			// 时间戳
			this.timestamp = getTimestamp();
			// 0--新增 1--修改 2--查看
			this.op = this.options.isView ? 2 : (this.options.dataId ? 1 : 0);
			// 表单配置信息
			this.fcfg = this._getFormCfg();
			if (isNotEmpty(this.options.box)) {
				this.options.tableId = this.fcfg.tableId;
			}
			// 
			if (isEmpty(this.fcfg) && isEmpty(this.fcfg.data)) {
				CFG_message("表单未配置，请检查！", "error");
				return;
			}
			
			this.isNested = this.options.isNested;
			
			if (this.isNested) { // 嵌入式表单
				CFG_emptyJQ(this.element);
			} else { // 弹出式表单
				this.element.hide();
			}
			
			this._buildTbar();

			this.fcfg.title = isNotEmpty(this.options.title) ? this.options.title
					: (null === this.options.dataId ? "新增" 
					: (this.options.isView ? "查看" 
					: (CFG_common.BOX_APPLYFOR !== this.options.box ? "修改" : "办理")));
			if (this.isNested) {
				if (this.assembleData) {
					// 添加导航
					CFG_addNavigationItem(this.assembleData, this.element, this.fcfg.title);
				}
				this._createNestedForm();
			} else {
				this._createDialogForm();
			}
			// 查看时表单设置成只读
			if (this.options.isView) {
				if ("readonly" === this.options.viewModel) {
					$("form", this.uiForm).form("setReadOnly", true);
				} else if ("isLabel" === this.options.viewModel){
					$("form", this.uiForm).form("setIsLabel", true);
				}
			}
			// 工作流按钮处理
			this.processCoflowButton();
			// 绑定事件
			this.bindEvent();
			// 设置默认值
			this._setDefaultValue();
			// 加载数据
			if (this.options.dataId) {
				if (this._hasViewRecord() && this.hasGrid) {
					this.idArr = $("#" + this.options.gDivId).grid("getDataIDs");
					this.rIndex = $.inArray(this.options.dataId, this.idArr);
					this._controlViewRecord();
				}
				// 加载表单数据
				this.load(this.options.dataId);
				//
				this._initCoflow();
			}
			// 默认回调方法与参数方法
			this._defaultOutput();
			// 自适应
			if (this.isNested) {
				$.coral.adjusted(this.uiForm);
				$("form", this.uiForm).form("refresh");
			}
			
		},
		// 默认值处理，二次开发接口
		processDefaultValue : $.noop,
		// 绑定事件
		bindEvent : $.noop,
		// 保存前回调方法当返回false时,终止保存动作
		beforeSave: function(jq/* form Jquery对象. */, op) {
			return true;
		},
		// 处理保存前
		processBeforeSave : function(jq/* form Jquery对象. */, op) {
			return (false === this._trigger("beforeSave", null, [jq, op]) ? false : this.beforeSave(jq, op));
		},
		// 保存后回调方法
		afterSave : function (entity, op) {
			
		},
		// 保存后处理
		processAfterSave : function(entity, op) {
			// 先处理options中afterSave
			this._trigger("afterSave", null, [entity, op]);
			// 再处理复写中afterSave
			this.afterSave(entity, op);
		},
		// 顶部工具条
		_createTbar : function() {
			if (!this.ctbar) return;
			this.ctbar.render(this.uiTbar, "top");
		},
		// 底部工具条
		_createSbar : function() {
			if (!this.ctbar) return;
			this.ctbar.render(this.uiSbar, "bottom");
		},
		// 创建工具条
		_buildTbar : function () {
			var _this = this, defaultPos;
			if (!this.options.toolbar && !this.options.statusbar) return;
			// 工具条位置
			//position = (this.options.toolbar && this.options.statusbar) ? "both" : (this.options.toolbar ? "top" : "bottom");
			defaultPos = this.isNested ? $.config.defaults.tbarPos.form.nested : $.config.defaults.tbarPos.form.popup;
			this.ctbar = new $.config.ctbar({
				number : this.options.number,
				menuId : this.options.menuId,
				moduleId : this.options.moduleId,
				tableId : this.options.tableId,
				componentVersionId : this.options.componentVersionId,
				workflowId : null2empty(this.options.workflowId),
				box : null2empty(this.options.box),
				processInstanceId : null2empty(this._getProccessInstanceId()),
				activityId : null2empty(this._getActivityId()),
				type : $.config.contentType.form,
				op : this.op,
				isNested : this.isNested,
				defaultPos : defaultPos,
				global : this.options.global,
				onClick : function(event, ui) {
					_this.clickTbar(event, ui);
				},
				processData : function(data, pos) {
					var assembleType = _this.getParamValue("assembleType"),
					    fn, item, idx, leftIndex, centerIndex, backBtnPos;
					// 全局默认的工具条按钮处理方法
					fn = $.fn["ctbar"].defaults.processData;
					if (typeof fn === "function") {
						data = fn.apply(this, [data, pos]);
					}
					// 表单上的工具条按钮处理方法
					fn = ("top" === pos ? _this.options.toolbar : _this.options.statusbar);
					if (typeof fn === "function") {
						data = fn.apply(_this, [data, pos]);
					}
					
					// 添加“返回/关闭”按钮情况：
					// 1. 自定义区域一是表单，同时，该构件是被组装的（isNotEmpty(assembleType) && 1 === _this.options.number）
					// 2. 通过按钮触发的来打开表单（$.config.contentType.form !== _this.options.model）。如新增、修改
					if ((isNotEmpty(assembleType) && 1 === _this.options.number) 
							|| $.config.contentType.form !== _this.options.model) {
						
						// 全局默认设置
						backBtnPos = $.config.defaults.backBtnPos.form;
						backBtnPos = (_this.isNested ? backBtnPos.nested : backBtnPos.popup);
						// 构件独立设置
						$.extend(backBtnPos, _this.options.backBtn.position);
						// 取出具体工具条“返回或关闭”位置
						backBtnPos = backBtnPos[pos];
						// 如果为false，则不添加
						if (false === backBtnPos) {
							return data;
						}
						item = {id : "back2grid", type : "button", label : (_this.isNested ? "返回" : "关闭")};
						// 样式
						if (isNotEmpty(_this.options.backBtn.cls)) {
							item.cls = _this.options.backBtn.cls;
						}
						// 图标
						if (isNotEmpty(_this.options.backBtn.icon)) {
							item.icon = _this.options.backBtn.icon;
						}
						// 按设置添加“返回或关闭”按钮
						leftIndex = $.inArray("->", data);
						if (backBtnPos === "left") {
							if (leftIndex === -1) {
								data.push(item);
							} else {
								data.splice(leftIndex, 0, item);
							}
						} else if (backBtnPos === "center") {
							if (leftIndex === -1) {
								data.push("", "->", item, "->", "");
							} else {
								centerIndex = $.inArray("->", data, leftIndex + 1);
								if (centerIndex === -1) {
									data.push(item, "->", "");
								} else {
									data.splice(centerIndex, 0, item);
								}
							}
						} else if (backBtnPos === "right") {
							if (leftIndex === -1) {
								data.push("->", item);
							} else {
								data.push(item);
							}
						}
					}
					
					return data;
				}
			});
		},
		// 工作流按钮处理
		processCoflowButton : function () {
			// 
			if (1 !== this.options.number) return;
			//
			if (!this.hasGrid || isEmpty(this.options.box) || CFG_common.BOX_TODO !== this.options.box) return;
			// 第一个节点不能退回
			if (this.fcfg.firstActivity) {
				this.removeToolbarItem(CFG_common.P_UNTREAD);
			}
			// 未签收，隐藏“提交”与“转办”按钮；已签收，移除“签收”按钮
			var wStatus = $("#" + this.options.gDivId).grid("getRowData", this.options.dataId)[CFG_common.W_STATUS];
			if (11 == wStatus) {
				this.hideToolbarItem(CFG_common.P_COMPLETE);
				this.hideToolbarItem(CFG_common.P_REASSIGN);
			} else {
				this.removeToolbarItem(CFG_common.P_CHECKOUT);
			}
		},
		// 弹出表单
		_createDialogForm : function() {
			var _this = this,
			    jqGlobal = this.getDialogAppendTo(),
			    width = jqGlobal.width() - 10, 
			    height= jqGlobal.height() - 10,
			    tbarPanel, sbarPanel;

			//this.uiForm = $("<div id=\"" + this._getFormId() + "\"></div>").appendTo(this.element);

			this.uiForm.dialog({
				autoOpen : false,
				modal : true,
				appendTo : jqGlobal,
				title : this.fcfg.title,
				width : this.colspan[this.fcfg.colspan - 1],
				maxWidth : width,
				height : "auto",
				maxHeight : height,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					_this._assembleReturn();
				}
			});
			// 顶部工具条
			if (this.options.toolbar && this.ctbar.hasTop()) {
				tbarPanel = $("<DIV class=\"toolbarsnav clearfix\"></DIV>").prependTo(this.uiForm);
				this.uiTbar = $("<DIV>").prependTo(tbarPanel);
				this._createTbar();
			}
			// 表单信息
			this.uiForm.append(this.fcfg.data);
			// 底部工具条
			if (this.options.statusbar && this.ctbar.hasBottom()) {
				sbarPanel = $("<div class=\"app-bottom-toolbar clearfix\"></div>").appendTo(this.uiForm);
				this.uiSbar = $("<DIV></DIV>").appendTo(sbarPanel);
				this._createSbar();
			}
			// 解析表单
			$.parser.parse(this.uiForm);
			// 打开表单窗体
			this.uiForm.dialog("open");
		},
		// 嵌入式表单
		_createNestedForm : function() {
			var pBody = this.element, 
			    tbarPanel = null, sbarPanel = null;
			//this.uiForm = $("<DIV id=\"" + this.fcfg.formId + "\" class=\"coral-adjusted\"></DIV>").appendTo(pBody);
			// 顶部工具条
			if (this.options.toolbar && this.ctbar.hasTop()) {
				tbarPanel = $("<DIV class=\"toolbarsnav clearfix\"></DIV>").prependTo(pBody);
				this.uiTbar = $("<DIV>").prependTo(tbarPanel);
				this._createTbar();
			}
			// 表单
			this.uiForm.append(this.fcfg.data);
			// 同一个构件多个表单时，只聚焦第一个表单
			if (this.options.sequence > 1) {
				$("form", this.uiForm).attr("data-options", "heightStyle:\"fill\",focusFirst:false");
			}
			// 底部工具条
			if (this.options.statusbar && this.ctbar.hasBottom()) {
				sbarPanel = $("<div class=\"app-bottom-toolbar clearfix\"></div>").appendTo(this.element);
				this.uiSbar = $("<DIV></DIV>").appendTo(sbarPanel);
				this._createSbar();
			}
			// 解析
			$.parser.parse(this.uiForm);
		},
		
		// 表单配置信息
		_getFormCfg : function() {
			var url = this.getAction() + "!page.json" + 
					"?E_frame_name=coral&E_model_name=form" + 
					"&P_tableId=" + this.options.tableId + 
					"&P_M_tableId=" + ((this.options.master && this.options.master.tableId) ? this.options.master.tableId : "") + 
					"&P_moduleId=" + this.options.moduleId + 
					"&P_menuId=" + this.options.menuId +
					"&P_componentVersionId=" + this.options.componentVersionId + 
					"&P_workflowId=" + null2empty(this.options.workflowId) + 
					"&P_box=" + null2empty(this.options.box) + 
					"&P_processInstanceId=" + this._getProccessInstanceId() + 
					"&P_activityId=" + this._getActivityId() + 
					"&P_readonly=" + (this.op === 2) +
					"&P_menuCode=" + this.getParamValue("menuCode") +
					"&P_timestamp=" + this.timestamp,
				fcfg;
			fcfg = $.loadJson(url);
			if (fcfg && isNotEmpty(fcfg.data)) {
				fcfg.data = fcfg.data.replace(/\{action\}/g, this.getAction());
				fcfg.data = fcfg.data.replace(/\{contextPath\}/g, $.contextPath);
				fcfg.data = fcfg.data.replace(/\{cformDivId\}/g, this.element.attr("id"));
				if (2 === this.op) {
					fcfg.data = fcfg.data.replace(/required\:true/g, "required\:false");
				}
			}
			return fcfg;
		},
		
		// 设置默认值
		_setDefaultValue : function() {
			var defaultData = this.fcfg.defaultData;
			//$("form", this.uiForm).form("loadData", this.fcfg.defaultData);
			//this._setColumnsFormData();
			//this._setMasterFormData();
			$.extend(defaultData, this._getColumnsFormData(), this._getMasterFormData());
			$("form", this.uiForm).form("loadData", defaultData);
			this.processDefaultValue();
		},
		
		// 设置外部传过来的字段值默认值（如树的字段节点）
		_setColumnsFormData : function() {
			var cFormData = this._getColumnsFormData();
			if (!$.isEmptyObject(cFormData)) {
				$("form", this.uiForm).form("loadData", cFormData);
			}
		},
		
		// 获取外部传过来的字段值
		_getColumnsFormData : function() {
			var itemArr = null, i     = 0,
			    cvArr   = null, fArr  = null, 
			    column  = null, value = null, formData = null;
			if (this.cFormData) return this.cFormData;
			// 树节点上的字段值
			this.cFormData = {};
			if (isNotEmpty(this.options.columns)) {
				itemArr = this.options.columns.split(";");
				for (; i < itemArr.length; i++) {
					cvArr = itemArr[i].split(CFG_cv_split);
					fArr  = cvArr[0].split(CFG_oc_split);
					// 只有等于(=)与包含(LIKE)字段值才设为默认值
					if ("EQ" === fArr[0] || "LIKE" === fArr[0]) {
						column = fArr[1];
						value = cvArr[1];
						this.cFormData[column.toUpperCase()] = value;
					}
				}
			}
			return this.cFormData;
		},
		
		/** 给表关系配置了关联字段设置默认值，同时获取继承字段的值. */
		_setMasterFormData : function() {
			var mFormData = this._getMasterFormData();
			if (!$.isEmptyObject(mFormData)) {
				$("form", this.uiForm).form("loadData", mFormData);
			}
		},
		
		/** 获取表关系配置了关联字段设置默认值及继承字段的值. */
		_getMasterFormData : function() {
			if (!this.options.master || !this.options.master.tableId || !this.options.master.dataId) return;
			if (!this.mFormData) {
				this.mFormData = $.loadJson(this._getMasterRelateURL());
			}
			return this.mFormData;
		},
		
		/** 与主表关系URL. */
		_getMasterRelateURL : function() {
			var url = (this.getAction() + "!relationData.json?P_tableId=" + this.options.tableId + 
					"&P_M_tableId=" + this.options.master.tableId + 
					"&P_M_dataId=" + this.options.master.dataId);
			if (isNotEmpty(this.fcfg.inheritItems)) {
				url += "&P_inheritItems=" + this.fcfg.inheritItems.toString();
			}
			url += "&P_menuCode=" + this.getParamValue("menuCode");
			return url;
		},
		// 生成ID规则
		_generateId : function(prefix) {
			return prefix + "_" + this.options.tableId + "_" + this.uuid + "_" + this.options.number;
		},

		_getFormId : function() {
			return this._generateId("fm");
		},

		_getTbarId : function() {
			return this._generateId("ft");
		},
		// 是否有首条、上一条、下一条、末条 按钮
		_hasViewRecord : function () {
			if (!this.existToolbar()) return false;
			if (this.existToolbarItem(CFG_common.P_FIRST_RECORD)) return true;
			if (this.existToolbarItem(CFG_common.P_PREVIOUS_RECORD)) return true;
			if (this.existToolbarItem(CFG_common.P_NEXT_RECORD)) return true;
			if (this.existToolbarItem(CFG_common.P_LAST_RECORD)) return true;
			return false;
		},
		// 控制首条、下一条、上一条、末条 按钮
		_controlViewRecord : function() {
			if (this.op === 0) return; // 如果是新增，则不需要控制
			if (!$.isCoral(this.uiTbar, "toolbar") && !$.isCoral(this.uiSbar, "toolbar")) {
				return ;
			}
			if (1 == this.idArr.length) {
				this.disableToolbarItem(CFG_common.P_FIRST_RECORD);
				this.disableToolbarItem(CFG_common.P_PREVIOUS_RECORD);
				this.disableToolbarItem(CFG_common.P_NEXT_RECORD);
				this.disableToolbarItem(CFG_common.P_LAST_RECORD);
			} else if (0 == this.rIndex) {
				this.disableToolbarItem(CFG_common.P_FIRST_RECORD);
				this.disableToolbarItem(CFG_common.P_PREVIOUS_RECORD);
				this.enableToolbarItem(CFG_common.P_NEXT_RECORD);
				this.enableToolbarItem(CFG_common.P_LAST_RECORD);
			} else if (this.rIndex == (this.idArr.length - 1)) {
				this.enableToolbarItem(CFG_common.P_FIRST_RECORD);
				this.enableToolbarItem(CFG_common.P_PREVIOUS_RECORD);
				this.disableToolbarItem(CFG_common.P_NEXT_RECORD);
				this.disableToolbarItem(CFG_common.P_LAST_RECORD);
			} else {
				this.enableToolbarItem(CFG_common.P_FIRST_RECORD);
				this.enableToolbarItem(CFG_common.P_PREVIOUS_RECORD);
				this.enableToolbarItem(CFG_common.P_NEXT_RECORD);
				this.enableToolbarItem(CFG_common.P_LAST_RECORD);
			}
		},
		// 调用组装返回
		_assembleReturn : function (isReload) {
			// 删除当前DIV
			this.element.remove();
			// 返回前一页面
			if (typeof CFG_clickReturnButton === "function" && this.assembleData) {
				var assembleType = this.getParamValue("assembleType");
				if ((this.options.isAlone || isNotEmpty(assembleType)) && this.assembleData.parentConfigInfo) {
					// 独立使用时，表单内容是拼接在创建的DIV中
				    CFG_clickCloseButton(this.assembleData);
				} else {
					// 内容直接嵌在创建的DIV中
					CFG_clickReturnButton(this.assembleData); 
				}
			}
			// 如果有对应的列表，则重新加载列表数据
			if (false !== isReload) {
				this.reloadSelfGrid();
			}
		},
		// 
		getDialogAppendTo : function () {
			//CFG_getDialogParent(_this.assembleData) || $(this.options.global)
			//return CFG_getDialogParent(this.assembleData) || $("body");
			return $("body");
		},
		// 判断工具条是否存在
		existToolbar : function () {
			return $.isCoral(this.uiTbar, "toolbar") || $.isCoral(this.uiSbar, "toolbar");
		},
		// 判断工具条是否存在指定ID按钮
		existToolbarItem : function (id) {
			if ($.isCoral(this.uiTbar, "toolbar")) return this.uiTbar.toolbar("isExist", id);
			if ($.isCoral(this.uiSbar, "toolbar")) return this.uiSbar.toolbar("isExist", id);
		}, 
		// 禁用指定ID工具条按钮
		disableToolbarItem : function(id) {
			if ($.isCoral(this.uiTbar, "toolbar")) this.uiTbar.toolbar("disableItem", id);
			if ($.isCoral(this.uiSbar, "toolbar")) this.uiSbar.toolbar("disableItem", id);
		},
		// 启用指定ID工具条按钮
		enableToolbarItem : function(id) {
			if ($.isCoral(this.uiTbar, "toolbar")) this.uiTbar.toolbar("enableItem", id);
			if ($.isCoral(this.uiSbar, "toolbar")) this.uiSbar.toolbar("enableItem", id);
		},
		// 隐藏指定ID工具条按钮
		hideToolbarItem : function(id) {
			if ($.isCoral(this.uiTbar, "toolbar")) this.uiTbar.toolbar("hide", id);
			if ($.isCoral(this.uiSbar, "toolbar")) this.uiSbar.toolbar("hide", id);
		},
		// 显示指定ID工具条按钮
		showToolbarItem : function(id) {
			if ($.isCoral(this.uiTbar, "toolbar")) this.uiTbar.toolbar("show", id);
			if ($.isCoral(this.uiSbar, "toolbar")) this.uiSbar.toolbar("show", id);
		},
		// 删除指定ID工具条按钮
		removeToolbarItem : function(id) {
			if ($.isCoral(this.uiTbar, "toolbar")) this.uiTbar.toolbar("remove", id);
			if ($.isCoral(this.uiSbar, "toolbar")) this.uiSbar.toolbar("remove", id);
		},
		// 获取ID值
		getDataId : function() {
			return this.getItemValue("ID");
		},
		
		/** *********************************************【工作流相关方法】(start)*********************************************** */
		
		// 工作流相关初始化信息
		_initCoflow : function () {
			this._flowDefaultValue();
			this._coflowOpinion();
		},
		
		// 工作流节点默认值（只有在待箱中，且对应字段的值为空时，才设置相应的默认值）
		_flowDefaultValue : function () {
			var defaultData = this.fcfg.flowDefaultData, val;
			if (this.options.box !== CFG_common.BOX_TODO
					|| $.isEmptyObject(defaultData)) {
				return;
			}
			for (var p in defaultData) {
				val = this.getItemValue(p);
				if (isEmpty(val)) {
					this.setItemValue(p, defaultData[p]);
				}
			}
		},
		
		// 流程审意见列
		_coflowOpinion : function() {
			if (!this.options.dataId) return;
			var jqFm = $(".fillwidth", this.uiForm), 
				_this = this,
			    label = "&nbsp;",
			    dataId = this._getCoflowId(),
			    url, opinion, jqOp, jqTemp, wfEntity, wStatus, assistData;
			if (isNotEmpty(this.options.box)
					&& CFG_common.BOX_APPLYFOR != this.options.box) {
				wfEntity = $.loadJson($.config.appActionPrefix + "/workflow-define!edit.json?id=" + this.options.workflowId);
				// 
				this.options.enableConfirmTable = wfEntity.enableConfirmTable;
				this.options.enableAssistTable  = wfEntity.enableAssistTable;
				
				if ("1" !== wfEntity.enableConfirmTable) return;				

				url = $.config.appActionPrefix + "/workflow-confirm-opinion!search.json?P_workflowId=" + this.options.workflowId +
						"&P_dataId=" + dataId; 
				opinion = $.loadJson(url);
				if (isNotEmpty(opinion)) {
					$("<hr id=\"confirmSplit_" + this.uuid + "\" class=\"app-confirm-opinion-form-split\"/>").appendTo(jqFm);
					jqOp = $("<div class=\"app-inputdiv12\"></div>").appendTo(jqFm);
					$("<label class=\"app-input-label\" style=\"float:left\">审批意见：</label>").appendTo(jqOp);
					jqOp.append(opinion);
				}
				// 第一个节点和非待办箱，不显示填写审批意见区
				if (this.fcfg.firstActivity || CFG_common.BOX_TODO != this.options.box) {
					return; 
				} 
				if (isEmpty(opinion))  {
					$("<hr id=\"confirmSplit_" + this.uuid + "\" class=\"app-confirm-opinion-form-split\"/>").appendTo(jqFm);
					label = "审批意见：";
				}
				// 审批结果
				jqOp = $("<div id=\"confirmResult_" + this.uuid + "\" class=\"app-inputdiv6\"></div>").appendTo(jqFm);
				$("<label class=\"app-input-label\">" + label + "</label>").appendTo(jqOp);
				jqTemp = $("<input id=\"CONFIRM_RESULT_" + this.uuid + "\" >").appendTo(jqOp);
				url = $.config.appActionPrefix + "/workflow-assist-opinion!combobox.json?P_workflowId=" + this.options.workflowId
				            + "&P_workitemId=" + this._getWorkitemId()
				            + "&P_activityId=" + this._getActivityId();
				jqTemp.radiolist({
					data: "1:同意;0:不同意",
					value: "1"
				});
				// 辅助意见
				if ("1" === wfEntity.enableAssistTable) {
					assistData = $.loadJson(url);
					jqOp = $("<div id=\"assistOpinion_" + this.uuid + "\" class=\"app-inputdiv6\"></div>").appendTo(jqFm);
					$("<label class=\"app-input-label\">辅助录入：</label>").appendTo(jqOp);
					jqTemp = $("<input id=\"ASSIST_OPINION_" + this.uuid + "\">").appendTo(jqOp);
					url = $.config.appActionPrefix + "/workflow-assist-opinion!combobox.json?P_workflowId=" + this.options.workflowId
					            + "&P_workitemId=" + this._getWorkitemId()
					            + "&P_activityId=" + this._getActivityId();
					assistData = $.loadJson(url);
					jqTemp.combobox({
						data: assistData,
						onChange: function(e, ui) {
							var $co = $("#CONFIRM_OPINION_" + _this.uuid);
							if (isNotEmpty(ui.newValue)) {
								$co.textbox("setValue", $(this).combobox("getText"));
							}
							return true;
						}
					});
				}
				// 审批意见
				jqOp = $("<div id=\"confirmOpinion_" + this.uuid + "\" class=\"app-inputdiv12\"></div>").appendTo(jqFm);
				$("<label class=\"app-input-label\">&nbsp;</label>").appendTo(jqOp);
				jqTemp = $("<textarea id=\"CONFIRM_OPINION_" + this.uuid + "\"></textarea>").appendTo(jqOp);
				jqTemp.textbox({height: 120, maxlength: 500});
				// 未签收
				wStatus = this._getWorkitemStatus();
				if (11 == wStatus)  this._hideConfirmOpinion();
			}
		},
		// 隐藏审批意见录入框
		_hideConfirmOpinion : function() {
			$("#confirmSplit_" + this.uuid).hide();
			if ("1" !== this.options.enableConfirmTable) return;
			$("#confirmResult_" + this.uuid).hide();
			if ("1" === this.options.enableAssistTable) {
				$("#assistOpinion_"  + this.uuid).hide();
			}
			$("#confirmOpinion_" + this.uuid).hide();
		},
		// 显示审批意见录入框
		_showConfirmOpinion : function() {
			$("#confirmSplit_" + this.uuid).show();
			if ("1" !== this.options.enableConfirmTable) return;
			$("#confirmResult_" + this.uuid).show();
			if ("1" === this.options.enableAssistTable) {
				$("#assistOpinion_"  + this.uuid).show();
			}
			$("#confirmOpinion_" + this.uuid).show();
		},
		// 工作流节点ID
		_getActivityId : function() {
			var id = "";
			if (1 === this.options.number && isNotEmpty(this.options.box)) {
				if (CFG_common.BOX_APPLYFOR === this.options.box) {
					id = CFG_common.START_ACTIVITY_ID;
				} else if (this.hasGrid) {
					id = $("#" + this.options.gDivId).grid("getRowData", this.options.dataId)[CFG_common.ACTIVITY_ID]; 
				}
			}
			return null2empty(id);
		},
		// 工作流工作项ID
		_getWorkitemId : function() {
			if (CFG_common.BOX_APPLYFOR === this.options.box) {
				if (isEmpty(this.options.dataId)) return "";
				return this.options.dataId.split("_")[1];
			}
			if (!this.hasGrid) return "";
			var id = $("#" + this.options.gDivId).grid("getRowData", this.options.dataId)[CFG_common.W_ID];
			return null2empty(id);
		},
		// 工作流工作项状态
		_getWorkitemStatus : function() {
			if (!this.hasGrid) return "";
			var id = $("#" + this.options.gDivId).grid("getRowData", this.options.dataId)[CFG_common.W_STATUS];;
			return null2empty(id);
		},
		// 工作流实例ID
		_getProccessInstanceId : function() {
			if (!this.hasGrid) return "";
			var id = $("#" + this.options.gDivId).grid("getRowData", this.options.dataId)[CFG_common.P_ID];
			return null2empty(id);
		},
		// 工作流流转地址
		_getCoflowUrl : function(op) {
			var url = this.getAction() + "!coflow.json?P_workflowId=" + null2empty(this.options.workflowId) + 
			        "&P_workitemId=" + this._getWorkitemId() + 
					"&P_op=" + op + 
					"&P_tableId=" + this.options.tableId +
					"&P_menuCode=" + this.getParamValue("menuCode");
			if (CFG_common.P_START !== op)  {
				url += ("&id=" + this._getCoflowId());
			}
			return url;
		},
		// 工作流视图ID是由 业务表ID + "_" + T_WF_WORKITEM.ID 组成
		_getCoflowId : function () {
			return this.options.dataId.split("_")[0];
		},
		// 工作流表单数据处理
		/*processFormData : function (formData) {
			if ("P_CONFIRM_RESULT" in formData) delete formData.P_CONFIRM_RESULT;
			if ("P_CONFIRM_OPINION" in formData) delete formData.P_CONFIRM_OPINION;
			if ("P_ASSIST_OPINION" in formData) delete formData.P_ASSIST_OPINION;
			return formData;
		},*/
		// 提交到下一节点
		_coflowComplete : function(performers, opinion, callback) {
			var url, formData, _this = this, jqForm = $("form", this.uiForm);
			if (!jqForm.form("valid")) 	return;
			performers= null2empty(performers);
			opinion   = null2empty(opinion);
			url  = this._getCoflowUrl(CFG_common.P_COMPLETE);
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    //formData = this.processFormData(formData);
			$.ajax({
				type : "post",
				url : url,
				data : {E_entityJson: $.config.toString(formData), 
						P_performers: performers,
						P_opinion: opinion},
				dataType : "json",
				success : function(rlt) {
					if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						CFG_message("操作成功！", "success");
						if (typeof callback === "function") {
							callback(true);
						}
						// 返回列表
						_this.clickBackToGrid();
					} else {
						CFG_message("提交失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流节点选择页面
		_coflowUserControl : function() {
			var _this = this, rNode = null, setting = null, 
			    workitemId = this._getWorkitemId(),
			    jqGlobal = this.getDialogAppendTo(),
			    jqUC  = $("<div>").appendTo(jqGlobal), 
			    h     = jqGlobal.height() > 600 ? 600 : jqGlobal.height(), 
			    jqLy  = $("<div class=\"coralui-layout\" data-options=\"fit:true\" style=\"height:100%;width:620px;" + h + "\"></div>"), 
			    jqWt  = $("<DIV data-options=\"region:'west',split:true\" style=\"width:260px;height:100%;padding-left:10px;padding-right:10px;\"></DIV>"), 
			    jqCt  = $("<DIV data-options=\"region:'center',split:true\" style=\"width:60px;height:100%;\"></DIV>"),  
			    jqAt  = $("<DIV data-options=\"region:'east',split:true\" style=\"width:300px;height:100%;padding-left:10px;padding-right:10px;\"></DIV>"), 
			    jqTs  = $("<input type=\"text\">").appendTo(jqWt), // 搜索框（树）
			    jqTr  = $("<UL class=\"coral-adjusted\" style=\"height:100%;overflow:auto;\"></UL>").appendTo(jqWt), 
				url = this.getAction() + "!coflowNextStep.json?P_workitemId=" + workitemId + "&P_menuCode=" + this.getParamValue("menuCode"), 
				nsInfo = $.loadJson(url), 
				title = "当前节点：" + nsInfo.currentActivity.name,
				boxStyle = nsInfo.currentActivity.boxStyle,
				result  = "",
				opinion = "",
				treeData = null;

			if (!_this.fcfg.firstActivity && "1" === _this.options.enableConfirmTable) {
				if (false === $("#CONFIRM_OPINION_" + _this.uuid).textbox("valid")) {
					CFG_message("审批意见过长(最多500个字符)，请修改！", "warning");
					jqUC.remove();
					return;
				}
				result  = $("#CONFIRM_RESULT_" + _this.uuid).radiolist("getValue");
				opinion = $("#CONFIRM_OPINION_" + _this.uuid).textbox("getValue");
				opinion = result + opinion;
				// 如果不同意，直接提交终止流程
				if ("0" === result) {
					$.confirm("不同意，该流程将被终止，确定要提交吗？", function (sure) {
						if (sure) _this._coflowComplete(null, opinion);
					} );	
					jqUC.remove();
					return ;
				}
			}
		    // 检查是否只有一个结束节点
		    if (nsInfo.nextActivities.length === 1 && nsInfo.nextActivities[0].ended) {
		    	// 结束节点，直接提交
		    	jqUC.remove();
		    	jqUC = null;
		    	_complete(nsInfo.nextActivities[0].id + "^_^" + nsInfo.nextActivities[0].defaultPerformers[0].id);
		    	return;
		    }
		    
			if (this.isNested) {
				h = this.uiForm.height() - 60;
			} else {
				h = this.uiForm.dialog("component").height() - 60;
			}
			
			if (h > 600) h = 600;			    

			jqUC.dialog({
				autoOpen : false,
				appendTo : jqGlobal,
				title : title,
				modal : true,
				width : 660,
				height : h,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					jqUC.remove();
				}, 
				onOpen : function () {
					var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
					    //jqFont  = $("<font color=\"red\">注：请先在右侧选中审批框，在左侧单击选择审批人；在右侧通过双击删除审批人</font>").appendTo(jqPanel),
					    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
					jqDiv.toolbar({
						data: ["", "->", 
						       {id:"sure", label:"提交", cls:"save", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
						onClick: function(e, ui) {
							if ("sure" === ui.id) {
								var success = true;
								jqDiv.toolbar("disableItem", "sure");
								success = _complete();
								if (false === success) {
									jqDiv.toolbar("enableItem", "sure");
								}
							} else {
								_this.close(jqUC);
							}
						}
					});
				}
			});

			jqWt.appendTo(jqLy);
			jqCt.appendTo(jqLy);
			jqAt.appendTo(jqLy);
			jqLy.appendTo(jqUC);
			// 按钮区
			jqCt.append("<div style=\"text-align:center;width:100%;padding-top:120px;\">" 
					+ "<p style=\"margin-top:5px;\"><button id=\"toRight_" + this.uuid + "\" class=\"coralui-button\" dataOptions=\"\">　&gt;　</button></p>"
					+ "<p style=\"margin-top:5px;\"><button id=\"toLeft_" + this.uuid + "\" class=\"coralui-button\" dataOptions=\"\">　&lt;　</button></p>"
					+ "</div>");
			// 解析布局
			$.parser.parse(jqUC);
			// 按钮事件绑定
			$("#toRight_" + this.uuid, jqUC).click(function () {
				var tns = jqTr.tree("getCheckedNodes", true);
				if (!tns || tns.length == 0) {
					CFG_message("请选择审批人！", "warning");
					return false;
				}
				var activityId = _this.coflowPerformerTarget.attr("name"), 
			        value = null;
				for (var i = 0, len = tns.length; i < len; i++) {
					value = (activityId + "^_^" + tns[i].id);
					if (_this.coflowPerformerTarget.find("option[value='" + value + "\']").length > 0) {
						continue;
					}
					$("<option value=\"" + value + "\">" + tns[i].name + "</option>").appendTo(_this.coflowPerformerTarget);
				}
			});
			$("#toLeft_" + this.uuid, jqUC).click(function () {
				if (!_this.coflowPerformerTarget ||
						_this.coflowPerformerTarget.find("option:selected").length < 1) {
					CFG_message("请选择要移除了审批人！", "warning");
					return false;
				}
				_this.coflowPerformerTarget.find("option:selected").remove();
			});
			// 
			jqTs.textbox({
				name: "treeSearch",
				componentCls : "tree-search-box",
				placeholder : "请输入关键字",
				icons: [{icon: "icon-search3", click: function(e, data) {
					jqTr.tree("filterNodesByParam", {name: data.value});
				}}],
				onKeyUp : function (e, data) {
					jqTr.tree("filterNodesByParam", {name: data.value});
				}
			});
			// 用户组织树数据
			treeData = $.loadJson(_this.getAction() + "!page.json?E_frame_name=coral&E_model_name=user&P_ASYNC=0&P_OPEN=0&P_NOCHECK=1" + "&P_menuCode=" + _this.getParamValue("menuCode"));
			// 初始化机构人员树
			setting = {checkable : true};
			jqTr.tree(setting, [{id : "-1", name : "组织机构树", nocheck : true, children: treeData, open: true}]);
			// 初始化下一步审批节点布局
			_this._coflowCreateNextStepUI(jqTr, jqAt, nsInfo, treeData);
			// 打开对话框
			jqUC.dialog("open");
			jqLy.layout("refresh");
			// 置顶
			jqUC.dialog("moveToTop");
			$(".coflow-performer-border", jqAt).each(
					function() {
						var jq = $(this)
						var h = jq.height();
						$("div", jq).height(h);
					}
			);

			/** 获取提交下一节点和参与者. */
			function getPerformers() {
				var i = 0, idArr = [], jqSel = null, jqOp = null, j = 0, performers = "";
				$(".coflow-next-activity", jqAt).each(
					function() {
						var jq = $(this);
						if (jq[boxStyle]("isChecked")) idArr.push(jq[boxStyle]("getValue"));
					}
				);
				if (nsInfo.currentActivity.minLine > idArr.length) {
					CFG_message("本节点定义至少选中" + nsInfo.currentActivity.minLine + "个节点！", "warning");
					return false;
				}
				if (nsInfo.currentActivity.maxLine < idArr.length) {
					CFG_message("本节点定义最多选中" + nsInfo.currentActivity.maxLine + "个节点！", "warning");
					return false;
				}
				for (; i < idArr.length; i++) {
					jqSel = $("select[name='" + idArr[i] + "']");
					jqOp = jqSel.find("option");
					if (0 === jqOp.length) {
						CFG_message("选中的节点必须要有参与者！", "warning");
						return false;
					}
					jqOp.each(function() {
						performers += "," + $(this).val();
					});
				}
				return performers.substring(1);
			}
			// 提交
			function _complete(performers) {
				var flag = !!performers; //是否直接提交，如果直接提交，则没有选择审批人员窗口
				performers = flag ? performers : getPerformers();
				if (false === performers) {
					return false;
				}
				_this._coflowComplete(performers, opinion, (flag ? null : function() {_this.close(jqUC);}));
				return true;
			}
		},
		// 审批节点布局
		_coflowCreateNextStepUI : function($tree, jqAt, nsInfo, treeData) {
			var i = 0, jqTr = null, jqTd = null, jqDiv = null, jqTp  = null, html = null, 
			    j = 0, defaultPerformers, optionPerformers, activity, label, 
			    boxStyle = nsInfo.currentActivity.boxStyle, 
			    nextActivities = nsInfo.nextActivities, 
			    jqTb = $("<table style=\"width:100%;height:100%;\"></table>").appendTo(jqAt),
			    suffixCkId = ("_" + this.uuid),
			    _this = this,
			    zNodes= [];
			
			this.activityMap = {asyncTree: true};

			for (; i < nextActivities.length; i++) {
				activity = nextActivities[i];
				defaultPerformers = activity.defaultPerformers;
				optionPerformers  = activity.optionPerformers;
			    zNodes= [];
				// 审核节点
				jqTr = $("<tr></tr>").appendTo(jqTb);
				jqTd = $("<td style=\"height:32px;\"></td>").appendTo(jqTr);
				label = activity.name;
				if (activity.isRollback) {
					label = "<font color=\"red\">" + label + "(回退流转节点)</font>";
				}
				this.activityMap[activity.id] = {
						isPerformerControl: activity.isPerformerControl,
						zNodes : optionPerformers,
						isRollback : activity.isRollback
				};
				jqTp = $("<input type=\"" + boxStyle + "\" id=\"" + (activity.id + suffixCkId) + "\" name=\"activity" + (suffixCkId) + "\" class=\"coflow-next-activity\">").appendTo(jqTd);
				// 节点
				jqTp[boxStyle]({
					name : "activity",
					value : activity.id,
					label : label,
					checked : activity.checked,
					disabled : activity.disabled,
					onChange : function(e, ui) {
						var jqChk = $(this),
						    activityId = jqChk[boxStyle]("option", "value"),
						    jqSel = $("select[name='" + activityId + "']", jqAt);
						changePerformerTree(jqSel, !ui.checked);
					}
				});
				// 审批人员
				jqTr = $("<tr></tr>").appendTo(jqTb);
				jqTd = $("<td valign=\"top\" class=\"coflow-performer-border\"></td>").appendTo(jqTr);
				jqDiv= $("<div class=\"coral-adjusted\"></div>").appendTo(jqTd);
				jqTp = $("<SELECT mutiple=\"multiple\" size=5 class=\"selectNormal\"></SELECT>").appendTo(jqDiv);
				jqTp.attr("name", activity.id);
				jqTp.prop("disabled", activity.ended);
				// 默认审批人员
				for (j = 0; j < defaultPerformers.length; j++) {
					$("<option>").val(activity.id + "^_^" + defaultPerformers[j].id).text(defaultPerformers[j].name).appendTo(jqTp);
				}
				
				if (activity.ended) jqTp.hide();
				
				if (activity.checked) _this.coflowPerformerTarget = jqTp;
			}
			
			$("select", jqTb).focus(function() {
				changePerformerTree($(this));
			}).dblclick(
				function() {
					var jq = $(this);
					if (!jq.prop("disabled")) {
						jq.find("option[value='" + jq.val() + "']").remove();
					}
			});
			//
			if (_this.coflowPerformerTarget) {
				changePerformerTree(_this.coflowPerformerTarget);
			}// */
			// 改变组织机构树
			function changePerformerTree(jqSel, onblur) {
				var activityId, jqChk, zNodes, isControl, isOption, asyncTree;
				if (typeof onblur === "boolean") jqSel.prop("disabled", onblur);
				if (jqSel.prop("disabled")) {
					return _resetSelected(jqSel);
				}
				activityId = jqSel.attr("name");
				jqChk = $("#" + (activityId + suffixCkId));
				if (!jqChk[boxStyle]("isChecked") || jqChk[boxStyle]("option", "disabled")) {
					return _resetSelected(jqSel);
				}
				$("select.selectActive", jqTb).removeClass("selectActive");
				jqSel.addClass("selectActive");
				_this.coflowPerformerTarget = jqSel;
				zNodes = _this.activityMap[activityId].zNodes,
			    isControl = _this.activityMap[activityId].isPerformerControl,
				asyncTree = !isControl;
				if (asyncTree && _this.activityMap.asyncTree) return;
				$tree.tree("destroy");
				var setting = {	checkable : true };
				if (asyncTree) {
					$tree.tree(setting, [{id:"-1", name:"组织机构树", nocheck: true, children: treeData, open: true}]);
					_this.activityMap.asyncTree = true;
				} else {
					$tree.tree(setting, [{id:"-1", name:"组织机构树", nocheck: true, children: zNodes, open: true}]);
					_this.activityMap.asyncTree = false;
				}
			};
			// 重置节点选择
			function _resetSelected(jqSel) {
				var rtNode = null;
				if (jqSel.hasClass("selectActive")) {
					jqSel.removeClass("selectActive")
					rtNode = $tree.tree("getNodesByParam", "id", "-1");
					$tree.tree("removeChildNodes", rtNode[0]);
					_this.activityMap.asyncTree = false;
				}
				_this.coflowPerformerTarget = null;
			}
		},
		// 选择审批人员
		_coflowTreeClick : function(e, treeId, treeNode) {
			if (!this.coflowPerformerTarget) return;
			var activityId = this.coflowPerformerTarget.attr("name"), 
			    value = (activityId + "^_^" + treeNode.id);
			if (this.coflowPerformerTarget.find("option[value='" + value + "\']").length > 0) {
				return;
			}
			$("<option value=\"" + value + "\">" + treeNode.name + "</option>").appendTo(this.coflowPerformerTarget);
		},
		/** *********************************************【工作流相关方法】(end)*********************************************** */
		// 定位controller
		getAction : function() {
			var fn = window[CFG_actionName(this.options.globalTimestamp)];
			if ($.isFunction(fn))
				return fn(this);

			return $.config.appActionPrefix + "/show-module";
		},
		// 加载表单数据
		load : function(dataId) {
			if (!dataId) return;
			if (isNotEmpty(this.options.box)) {
				// 工作流视图ID是由 业务表ID + "_" + T_WF_WORKITEM.ID 组成
				dataId = this._getCoflowId();
			}
			var url = this.getAction() + "!show.json?P_tableId=" + this.options.tableId + 
			        "&P_componentVersionId=" + this.options.componentVersionId + 
			        "&P_menuId=" + this.options.menuId + 
			        "&P_workflowId=" + null2empty(this.options.workflowId) + 
			        "&P_processInstanceId=" + this._getProccessInstanceId() + 
			        "&id=" + dataId + "&P_menuCode=" + this.getParamValue("menuCode"),
			    formData = null;
			formData = $.loadJson(url);
			if ($.isEmptyObject(formData)) {
				CFG_message("该记录已被删除，请检查！", "warning");
			} else {
				$(":coral-form", this.uiForm).form("load", formData);
			}
		},
		// 重置表单数据
		reset : function() {
			$(":coral-form", this.uiForm).form("resetData");
			this._setDefaultValue();
		},
		// 关闭窗体
		close : function(jq, relaoded) {
			if (jq) {
				jq.dialog("close");
			}
			
			if (relaoded) {
				this.reloadSelfGrid();
			}
		},
		/***********************************************************************
		 * 与当前表单相关的列表或表单
		 **********************************************************************/
		// 与当前列表的所有关系对象
		_getRelation : function () {
			var data = $.config.getGlobalData(this.options.global),
			    storeId = $.config.storeId(this.options.tableId, $.config.contentType.form),
			    relation = null, detailId = null;
			if (storeId in data) {
				return data[storeId];
			}
			// 如果通过表单没找主表列表，则通过表单对应的列表来查找主表列表
			storeId = $.config.storeId(this.options.tableId, $.config.contentType.grid);
			if (storeId in data) {
				return data[storeId];
			}
			return null;
		},
		// 从表关系对象
		getDetail : function () {
			var data = $.config.getGlobalData(this.options.global), 
			    relation = this._getRelation();
			
			if (null != relation && relation.detailId) {
				return data[relation.detailId];
			}
			
			return null;
		},
		// 主表关系对象
		getMaster : function () {
			var data = $.config.getGlobalData(this.options.global), 
			    relation = this._getRelation();
			
			if (null != relation && relation.masterId) {
				return data[relation.masterId];
			}
			
			return null;
		},
		// 获取表单对应的主列表
		getMasteGrid : function () {
			var relationUI = this.getMaster();
			if (relationUI && this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isGrid(relationUI.type)) {
					return relationUI.jqUI;
				} else {
					return relationUI.jqUI.getSelfGrid();
				}
			}
			return false;
		},
		// 获取当前表单对应的主表单
		getMasterForm : function () {
			var relationUI = this.getMaster();
			if (!relationUI) return false;
			if (this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isForm(relationUI.type)) {
					return relationUI.jqUI;
				} else {
					return relationUI.jqUI.getSelfForm();
				}
			} else if ($.config.contentType.isGrid(relationUI.type)) {
				return relationUI.jqUI.getMasterForm();
			}
			return false;
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
		// 获取表单对应的从列表
		getDetailGrid : function () {
			var relationUI = this.getDetail();
			if (relationUI && this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isGrid(relationUI.type)) {
					return relationUI.jqUI;
				} else {
					return relationUI.jqUI.getSelfGrid();
				}
			}
			return false;
		},
		// 获取表单对应的从表单
		getDetailForm : function () {
			var relationUI = this.getDetail();
			if (relationUI && this.options.tableId !== relationUI.tableId) {
				if ($.config.contentType.isForm(relationUI.type)) {
					return relationUI.jqUI;
				} else {
					return relationUI.jqUI.getSelfForm();
				}
			}
			return false;
		},
		// 刷新自身对应的列表
		reloadSelfGrid : function () {
			var cGrid = this.getSelfGrid();
			if (cGrid) {
				cGrid.reload();
			}  
			if (this.hasGrid && 
					(!cGrid || cGrid.uiGrid.attr("id") != this.options.gDivId)) {
				$("#" + this.options.gDivId).grid("reload");
			}
		},
		// 获取表单数据：格式为{字段名:值,....}，如{YEAR_CODE:2010, BEGIN_DATE:"2015-03-01",...}
		getFormData : function () {
			return $("form", this.uiForm).form("formData", false);
		},
		// 给表单设值：格式为{字段名:值,....}，如{YEAR_CODE:2010, BEGIN_DATE:"2015-03-01",...}
		setFormData : function (formData) {
			$("form", this.uiForm).form("loadData", formData);
		},
		// 设置关联关系字段值：传入主表单的数据JSON值
		setRelationData : function (masterFormData) {
			if (isEmpty(this.fcfg.relationMap)) {
				CFG_message("两表关系未找到，请联系管理员！", "error"); return;
			}
			var mcols = this.fcfg.relationMap[this.fcfg.masterTableId], 
			    cols  = this.fcfg.relationMap[this.fcfg.tableId],
			    i = 0, len = mclos.length, relationData = {};
			for (; i < len; i++) {
				if (mcols[i] in masterFormData) {
					relationData[cols[i]] = masterFormData[mcols[i]];
				} else {
					CFG_message("主表单中不存在（" + mcols[i] + "），请联系管理员查检主表单配置！", "error");
				}
			}
			this.setFormData(relationData);
		},
		
		// 获取栏位框的ID
		getItemId : function (columnName) {
			return columnName + "_" + this.timestamp;
		},
		// 获取字段栏位对应的jquery对象
		getItemJQ : function (columnName) {
			var id = this.getItemId(columnName);
			return $("#" + id);
		},
		// 获取字段栏位外围边框DIV jquery对象
		getItemBorderJQ : function (columnName) {
			var id = this.getItemId(columnName) + "_DIV";
			return $("#" + id);
		},
		
		// 获取字段栏位对应的值
		getItemValue : function (columnName) {
			var spatr = $("form", this.uiForm).form("option", "separator"),
			    items = $("[name=\"" + columnName + "\"]", this.uiForm);
			if (items.length > 0) {
				return items.map(function (i, item) { return $(this).val(); }).get().join(spatr);
			}
			return null;
		},
		// 设值
		setItemValue : function (columnName, value) {
			var jq = this.getItemJQ(columnName);
			var role = jq.attr("component-role");
			if ($.inArray(role, $.parser.plugins) > -1) {
				jq[role]("setValue", value);
			}
		},
		
		/**
		 * 获取对应字段的label
		 * @param columnName
		 * @returns
		 */
		getItemLabel : function(columnName) {
			var id, jqLabel;
			id = this.getItemId(columnName) + "_LABEL";
			return $("#" + id).text();
		},
		
		/**
		 * 设置对应字段的label
		 * @param columnName
		 * @param label
		 */
		setItemLabel : function(columnName, label) {
			var id, jqLabel;
			id = this.getItemId(columnName) + "_LABEL";
			$("#" + id).text(label);
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
		 *     如disable方法：ui.callItemMethod("NAME", "disable");
		 *      setValue方法: ui.callItemMethod("NAME", "setValue", "qiucs");
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
		
		/**
		 * ************************************ (toolbar onclick)
		 * ************************************
		 */
		clickTbar : function(e, ui) {
			var id = ui.id;
			if (CFG_common.P_CREATE === id) {
				this.clickCreate();
			} else if (CFG_common.P_SAVE === id) {
				this.clickSave("save");
			} else if (CFG_common.P_ADD === id) {
				this.clickAdd();
			} else if (CFG_common.P_ADD_AND_CLOSE === id) {
				this.clickAdd("close");
			} else if (CFG_common.P_SAVE_ALL === id) {
				this.clickSaveAll();
			} else if (CFG_common.P_SAVE_AND_CLOSE === id) {
				this.clickSave("close");
			} else if (CFG_common.P_SAVE_AND_CREATE === id) {
				this.clickSave("create");
			} else if (CFG_common.P_RESET === id) {
				this.clickReset();
			} else if (CFG_common.P_FIRST_RECORD === id
					|| CFG_common.P_NEXT_RECORD === id
					|| CFG_common.P_PREVIOUS_RECORD === id
					|| CFG_common.P_LAST_RECORD === id) {
				this.clickViewRecord(id.replace("Record", ""));
			} else if (CFG_common.P_START == id) {
				this.clickCoflowStart();
			} else if (CFG_common.P_START_AND_COMPLETE == id) {
				this.clickCoflowStartAndComplete();
			} else if (CFG_common.P_CHECKOUT == id) {
				this.clickCoflowCheckout();
			} else if (CFG_common.P_COMPLETE == id) {
				this.clickCoflowComplete();
			} else if (CFG_common.P_UNTREAD == id) {
				this.clickCoflowUntread();
			} else if (CFG_common.P_RECALL == id) {
				this.clickCoflowRecall();
			} else if (CFG_common.P_REASSIGN == id) {
				this.clickCoflowReassign();
			} else if (CFG_common.P_DELIVER == id) {
				this.clickCoflowDeliver();
			} else if (CFG_common.P_HASREAD == id) {
				this.clickCoflowHasread();
			} else if (CFG_common.P_TERMINATION == id) {
				this.clickCoflowTermination();
			} else if (CFG_common.P_TRACK == id) {
				this.clickCoflowTrack();
			} else if ("back2grid" === id) {
				this.clickBackToGrid();
			} else if (id.match(/^\_\_\_secbtn\_/)) { // 二次开发按钮
				this.clickSecondDev(id);
			} else if (id.match(/^CD\_BUTTON\_/)) { // 组装按钮
				CFG_clickToolbar(this.assembleData, 
						CFG_getZoneName(this.options.tableId, $.config.contentType.form, this.options.number, ui.pos), 
						id,
						0,
						this);
			}
		},
		// 新增
		clickCreate : function () {
			var jqForm = $("form", this.uiForm),
			    formData = jqForm.form("formData", false),
			    keptItems = this.fcfg.keptItems||[],
			    increaseItems = this.fcfg.increaseItems||[],
			    keptItemData = {}, increaseItemData = {}, item;
			if (keptItems.length > 0) {
				for (var i = 0; i < keptItems.length; i++) {
					item = keptItems[i];
					keptItemData[item] = formData[item];
				}
			}
			if (increaseItems.length > 0) {
				for (var i = 0; i < increaseItems.length; i++) {
					item = increaseItems[i];
					var v = formData[item], len;
					if (v.match(/\d+/)) {
						len = v.length;
						v = ("" + (parseInt(v) + 1));
						for (var j = v.length; j < len; j++) v = "0" + v;
						increaseItemData[item] = v;
					} else {
						increaseItemData[item] = v;
					}
				}
			}
			// 清空
			jqForm.form("clear");
			// 设置默认值
			this._setDefaultValue();
			// 连续录入项
			jqForm.form("loadData", keptItemData);
			// 递增
			jqForm.form("loadData", increaseItemData);
		},
		// 表单保存postData参数二次开发
		processPostData : function (postData, op) {
			return postData;
		},
		
		/**
		 * 保存
		 * @param op
		 *     -- save 普通保存，保存后会根据ID重新查询一下（防止业务表触发器等修改的数据）后台返回当前表单数据
		 *     -- business 直接返回保存处理后的数据
		 *     -- close 保存并关闭（返回来的数据只有当前ID的数据）
		 *     -- create 保存并新增（返回来的数据只有当前ID的数据） 
		 **/ 
		clickSave : function(op) {
			var _this = this, jqForm = $("form", this.uiForm), 
			    url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op 
                                       + "&P_componentVersionId=" + this.options.componentVersionId
                                       + "&P_menuId=" + this.options.menuId
			                           + "&P_menuCode=" + this.getParamValue("menuCode"),
			    formData, selfGrid, postData;

			if (!jqForm.form("valid")) return;
			// 保存前回调方法
			if (_this.processBeforeSave(jqForm, op) === false) return;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    postData = {E_entityJson: $.config.toString(formData)};
		    postData = this.processPostData(postData, CFG_common.P_SAVE);
			$.ajax({
				type : "post",
				url : url,
				data : postData,
				dataType : 'json',
				success : function(entity) {
					if (false === entity.success || !("ID" in entity)) {
						if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
						else CFG_message(entity.message, "warning");
						return;
					}
					// 保存后回调方法
					_this.processAfterSave(entity, op);
					CFG_message("操作成功！", "success");
					if ("save" === op) {
						jqForm.form("loadData", entity);
						if (_this.options.model === $.config.contentType.form) {
							selfGrid = _this.getSelfGrid();
							if (selfGrid) {
								selfGrid.reload();
							}
						}
					} else if ("close" === op) {
						_this.clickBackToGrid();
					} else if ("create" === op) {
						_this.clickCreate();
					} else {
						_this.doViewRecord(op);
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 直接将表单数据添加到对应的列表中
		clickAdd : function (op) {
			var cGrid = this.getSelfGrid(), // 对应列表
			    jqForm = $("form", this.uiForm),
			    formData;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    // 向列表添加数据
			cGrid.addRowData(formData);
			if ("close" === op) {
				// 关闭或返回
				this.clickBackToGrid(false);
			} else {
				// 重置表单数据
				this.clickCreate();
			}
		},
		// 将表单数据及对应从表列表数据一起保存
		clickSaveAll : function () {
			var _this = this, dGrid = this.getDetailGrid(), op = "saveAll";
			if (!dGrid) return;
			var jqForm  = $("form", this.uiForm),
			    rowData = dGrid.toFormData(), 
			    formData, url, postData;
			// 表单检验
			if (!jqForm.form("valid")) return false;
			// 检验
			if (!rowData.length) {
				CFG_message("请先添加明细列表数据再保存！", "warning");
				return false;
			}
			// 保存前回调方法
			if (_this.processBeforeSave(jqForm, op) === false) return;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    postData = {E_entityJson: $.config.toString(formData),
				    E_dEntitiesJson: $.config.toString(rowData)};
		    postData = this.processPostData(postData, CFG_common.P_SAVE_ALL);
		    // 向列表添加数据
		    // 重置表单数据
		    url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId 
                                   + "&P_componentVersionId=" + this.options.componentVersionId
                                   + "&P_menuId=" + this.options.menuId
                                   + "&P_menuCode=" + this.getParamValue("menuCode");
			// console.log("master: " + $.config.toString(formData));
			// console.log("detail: " + $.config.toString(rowData));
		    
		    $.ajax({
		    	url : url,
				type : "post",
				data : postData,
				dataType : "json",
				success : function(rlt) {
					if (rlt.success) {
						jqForm.form("loadData", rlt.data.master);
						dGrid.clearGridData();
						dGrid.addRowData(rlt.data.detail);
						CFG_message("保存成功！", "success");
						// 保存后回调方法
						_this.processAfterSave(rlt.data, op);
					} else {
						CFG_message(rlt.message, "warning");
					}
				},
				error : function() {
					CFG_message("保存主从表数据失败！", "error");
				}
		    });// */
		},
		// 重置
		clickReset : function () {
			if (this.options.dataId) {
				this.load(this.options.dataId);
			} else {
				this.clickCreate();
			}
		},
		// 查看
		clickViewRecord : function(op) {
            var arr = $("form", this.uiForm).form("modifiedData", true).split("&"),
            str = "", _this=this;
            for (var i in arr) {
                str  = str + arr[i] + "\n";
            }
            if ($.trim(str) != "" ) {
            	$.confirm('是否保存？', function(r) {
                    if (r) {
                    	_this.clickSave(op);
                    } else {
                    	_this.doViewRecord(op);
					}
                });
			} else {
				_this.doViewRecord(op);
			}
		},
		//执行查看
		doViewRecord : function(op) {
			if ("first" === op) {
        		this.rIndex = 0;
			} else if ("last" === op) {
				this.rIndex = this.idArr.length - 1;
			} else if ("next" === op) {
				this.rIndex++;
			} else if ("previous" === op) {
				this.rIndex--;
			}
        	this._controlViewRecord();
        	this.load(this.idArr[this.rIndex]);
		},
		// 工作流：启动
		clickCoflowStart : function() {
			var _this = this, 
			    jqForm = $("form", this.uiForm), 
			    url = this._getCoflowUrl(CFG_common.P_START),
			    formData, postData;

			if (!jqForm.form("valid"))
				return;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    postData = {E_entityJson: $.config.toString(formData)};
		    postData = this.processPostData(postData, CFG_common.P_START);

			$.ajax({
				type : "post",
				url : url,
				data : postData,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						// $("#" + _this.options.gDivId).grid("reload");
						jqForm.find(":hidden[name='ID']").val(rlt.ID);
						CFG_message("操作成功！", "success");
						_this.disableToolbarItem(CFG_common.P_START);
						if (_this.hasGrid) _this.clickBackToGrid();
					} else if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else {
						CFG_message("启动失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流：启动并提交
		clickCoflowStartAndComplete : function() {
			var _this = this, 
			    jqForm = $("form", this.uiForm), 
			    url = this._getCoflowUrl(CFG_common.P_START),
			    formData, postData;
			if (!jqForm.form("valid")) 	return;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    postData = {E_entityJson: $.config.toString(formData)};
		    postData = this.processPostData(postData, CFG_common.P_START_AND_COMPLETE);
			$.ajax({
				type : "post",
				url : url,
				data : postData,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						_this.setItemValue("ID", rlt.ID);
						_this.getItemJQ("PROCESS_INSTANCE_ID").remove()
						_this.options.dataId = (rlt.ID + "_" + rlt.workitemId);
						_this.clickCoflowComplete();
						_this.disableToolbarItem(CFG_common.P_START);
						_this.disableToolbarItem(CFG_common.P_START_AND_COMPLETE);
					} else if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else {
						CFG_message("启动失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流： 提交
		clickCoflowComplete : function() {
			var workitemId = this._getWorkitemId(),
			    url = this.getAction() + "!isUserControl.json?P_workitemId=" + workitemId + "&P_menuCode=" + this.getParamValue("menuCode"), 
			    //jqForm = $("form", this.uiForm), 
			    isUserControl = $.loadJson(url);

			if (true == isUserControl) {
				this._coflowUserControl();// 弹出选择下一步节点页面
			} else {
				this._coflowComplete(); // 自动提交
			}
		},
		// 工作流： 签收
		clickCoflowCheckout : function() {
			var _this = this, url = this._getCoflowUrl(CFG_common.P_CHECKOUT);

			$.ajax({
				url : url,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						_this.reloadSelfGrid();
						_this.removeToolbarItem(CFG_common.P_CHECKOUT);
						_this.showToolbarItem(CFG_common.P_COMPLETE);
						_this.showToolbarItem(CFG_common.P_REASSIGN);
						_this._showConfirmOpinion();
						CFG_message("操作成功！", "success");
					} else if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else {
						CFG_message("签收失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流： 退回
		clickCoflowUntread : function() {
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
					jqUC.remove();
				},
				onCreate : function() {
					var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
					var jq = $("<textarea id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
					jq.textbox({height: 188, maxlength: 500});
				},
				onOpen : function() {
					var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
					    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
					jqDiv.toolbar({
						data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
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
			//
			function _untread() {
				var opinion = $("#UNTREAD_OPINION_" + _this.uuid).val(), 
			        url     = _this._getCoflowUrl(CFG_common.P_UNTREAD);
				$.ajax({
					url : url,
					dataType : "json",
					data : {P_opinion: opinion},
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.close(jqUC);
							CFG_message("操作成功！", "success");
							_this.clickBackToGrid();
						} else if (false === rlt.success) {
							CFG_message(rlt.message, "warning");
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
			var _this = this, url = this._getCoflowUrl(CFG_common.P_RECALL);
			$.confirm("您确定要撤回流程？", function(sure) {if (sure) _recall();});
			//
			function _recall() {
				var url = _this._getCoflowUrl(CFG_common.P_RECALL);
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							CFG_message("操作成功！", "success");
							_this.clickBackToGrid();
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
			var _this = this, 
			    jqGlobal = this.getDialogAppendTo(),
			    height = 480, 
			    jqUC = $("<div>").appendTo(jqGlobal), 
			    jqTR = $("<ul>").appendTo(jqUC);
			// 选择用户窗体
			jqUC.dialog({
				appendTo : jqGlobal,
				modal : true,
				title : "转办-选择用户",
				width : 400,
				height : height,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					jqUC.remvoe();
				},
				onCreate : function() {
					var setting = {
						asyncAutoParam : "id",
						asyncUrl : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=user&P_ASYNC=true" + "&P_menuCode=" + _this.getParamValue("menuCode"),
						asyncEnable : true,
						asyncType : "post",
						beforeclick : function(treeId, treeNode) {
							return CFG_combotreeUser(treeId, treeNode);
						}
					};
					jqTR.tree(setting, [{name : "组织机构树", isParent : true, id : "-1"}]);
				},
				onOpen : function() {
					var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
					    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
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
			// 
			function _reassign() {
				var url   = _this._getCoflowUrl(CFG_common.P_REASSIGN), 
				    sNode = jqTR.tree("getSelectedNodes");
				if (!sNode || sNode.length === 0) {
					CFG_message("请选择用户！", "warning");
					return;
				}
				url += "&P_targetUserIds=" + sNode[0].id;
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.close(jqUC);
							CFG_message("操作成功！", "success");
							_this.clickBackToGrid();
						} else if (false === rlt.success) {
							CFG_message(rlt.message, "warning");
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
		// 工作流： 传阅
		clickCoflowDeliver : function() {
			var _this = this, 
			    jqGlobal = this.getDialogAppendTo(),
			    height = 480, 
			    jqUC = $("<div>").appendTo(jqGlobal), 
			    jqTR = $("<ul>").appendTo(jqUC);
			// 退回意见窗体
			jqUC.dialog({
				appendTo : jqGlobal,
				modal : true,
				title : "传阅-选择用户",
				width : 400,
				height : height,
				resizable : false,
				position : {
					of : jqGlobal
				},
				onClose : function() {
					jqUC.remove();
				},
				onCreate : function() {
					var setting = {
						asyncAutoParam : "id",
						asyncUrl : _this.getAction() + "!page.json?E_frame_name=coral&E_model_name=user&P_ASYNC=true&P_CHECKED=user" + "&P_menuCode=" + _this.getParamValue("menuCode"),
						asyncEnable : true,
						asyncType : "post",
						beforeclick : function(treeId, treeNode) {
							return CFG_combotreeUser(treeId, treeNode);
						}
					};
					jqTR.tree(setting, [{name : "组织机构树", isParent : true, id : "-1"}]);
				},
				onOpen : function() {
					var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
					    jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
					jqDiv.toolbar({
						data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
						onClick: function(e, ui) {
							if ("sure" === ui.id) {
								_deliver();
							} else {
								_this.close(jqUC);
							}
						}
					});
				}
			});
			// 传阅
			function _deliver () {
				var url = _this._getCoflowUrl(CFG_common.P_DELIVER), 
				    sNode = jqTR.tree("getSelectedNodes"), 
				    i = 0, userIds = "";
				if (!sNode || sNode.length === 0) {
					CFG_message("请选择一个或多个用户！", "warning");
					return;
				}
				for (; i < sNode.length; i++) {
					userIds += "," + sNode[i].id;
				}
				url += "&P_targetUserIds=" + userIds.substring(1);
				$.ajax({
					url : url,
					dataType : "json",
					success : function(rlt) {
						if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
							_this.close(jqUC);
							CFG_message("操作成功！", "success");
						} else if (false === rlt.success) {
							CFG_message(rlt.message, "warning");
						} else {
							CFG_message("传阅失败！", "warning");
						}
					},
					error : function() {
						CFG_message("操作失败！", "error");
					}
				});
			}
		},
		// 工作流： 阅毕
		clickCoflowHasread : function() {

			var _this = this, 
			    jqGlobal = this.getDialogAppendTo(),
			    jqUC = $("<div>").appendTo(jqGlobal);
			// 阅毕意见窗体
			jqUC.dialog({
				appendTo : jqGlobal,
				modal : true,
				title : "查阅意见",
				width : 400,
				height : 240,
				resizable : false,
				position : { of : jqGlobal },
				buttons : [{
					text : "阅毕",
					click : function() {
						var opinion = $("#HASREAD_OPINION_" + _this.uuid).val(), 
						    url = _this._getCoflowUrl(CFG_common.P_HASREAD);
						url += "&P_opinion=" + decodeURIComponent(opinion);
						$.ajax({
							url : url,
							dataType : "json",
							success : function(rlt) {
								if (typeof rlt.workitemId === "number" && rlt.workitemId == 0) {
									_this.close(jqUC);
									CFG_message("操作成功！", "success");
									_this.clickBackToGrid();
								} else if (false === rlt.success) {
									CFG_message(rlt.message, "warning");
								} else {
									CFG_message("阅毕失败！", "warning");
								}
							},
							error : function() {
								CFG_message("操作失败！", "error");
							}
						});
					}
				}, {
					text : "取消",
					click : function() {
						_this.close(jqUC);
					}
				} ],
				onClose : function() {
					jqUC.remove();
				},
				onCreate : function() {
					var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
					var jq = $("<textarea id=\"HASREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
					jq.textbox({height: 188, maxlength: 500});
				}
			});
		},
		// 工作流：中止（删除）
		clickCoflowSuspend : function() {
			var _this = this,
			    url   = this._getCoflowUrl(CFG_common.P_SUSPEND);
			
			$.ajax({
				url : url,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						CFG_message("操作成功！", "success");
					} else if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else {
						CFG_message("中止（删除）流程失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流：终止
		clickCoflowTermination : function() {
			var _this = this,
			    url   = this._getCoflowUrl(CFG_common.P_TERMINATION);
			
			$.ajax({
				url : url,
				dataType : "json",
				success : function(rlt) {
					if (typeof rlt.workitemId === "number" && rlt.workitemId > 0) {
						CFG_message("操作成功！", "success");
						_this.clickBackToGrid();
					} else if (false === rlt.success) {
						CFG_message(rlt.message, "warning");
					} else {
						CFG_message("终止流程失败！", "warning");
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		},
		// 工作流： 跟踪
		clickCoflowTrack : function() {
			var pid = this._getProccessInstanceId();
	        var url = this.getAction() + "!coflowTrack?P_op=graph&P_processInstanceId=" + pid + "&P_menuCode=" + this.getParamValue("menuCode");
	        var iWidth = window.screen.width;
	        var iHeight = window.screen.height;
	        var win = window.open(url, "流程跟踪", 'height=' + iHeight + ',innerHeight=' + iHeight + ',width=' + iWidth
	                + ',innerWidth=' + iWidth
	                + ',top=0,left=0,toolbar=no,menubar=no,scrollbars=yes,resizable=yes,location=no,status=no');
	        return win;
		},
		/**
		 * 返回
		 * @param isReload
		 *             --为可选参数，如果不重新加载列表数据为false，默认会重新加载列表数据
		 */
		clickBackToGrid : function(isReload) {
			//var assembleType;
			var _this = this;
			if (_this.isNested) {
				_this._assembleReturn(isReload);
			} else {
				if (false === isReload) {
					_this.uiForm.dialog("option", "onClose", function () {
						CFG_removeJQ(_this.uiForm);
						_this._assembleReturn(false);
					});
				}
				this.uiForm.dialog("close");
			}
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
		
		// 获取组件库表单form的JQUERY对象
		getFormJQ : function () {
			return $("form", this.uiForm);
		},
		
		// 二次开发按钮事件
		clickSecondDev : function(id) {

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
		//
		refresh : function () {

		},
		/**
		 * ******************************** 
		 * 预留区、回调函数、输出参数函数(注：请勿复写)--开始
		 * *********************************
		 */
		_defaultOutput : function () {
			// 回调函数
			if (!this.callback) this.callback = {};
			this.callback["reload"] = function() {
				this.load(this.options.dataId);
			};
		
			// 方法
			if (!this.value) this.value = {};
			/* 当前表单中ID值 */
			this.value["id"] = function () {return this.getDataId();};
			/* 当前表单所有栏位的值{name:value,...} */
			this.value["formData"] = function () {
				var formData = this.getFormData();
				return $.config.toString(formData);
			};
			/* 当前表单对应的表ID. */
			this.value["tableId"] = function () {return this.options.tableId;};
			/* 当前表单所处的模块ID. */
			this.value["moduleId"] = function () {return this.options.moduleId;};
		}
		/**
		 * ********************************* 
		 * 预留区、回调函数、输出参数函数--结束
		 * **********************************
		 */

	});

})(jQuery);
