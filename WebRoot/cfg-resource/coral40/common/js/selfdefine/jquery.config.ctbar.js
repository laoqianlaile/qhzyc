/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义: 生成配置工具条JS
 * 
 * 依赖JS:
 *    
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.ctbar", {
		version: $.config.version,
		options: {
			menuId  : null,
			componentVersionId : null,
			moduleId: null,
			tableId : null,
			workflowId: null,
			box       : null,
			processInstanceId: "", // 流程实例ID
			activityId: "",        // 节点ID
			number: null,
			model : null,  // 2-缩略图工具条,其他为普通工具条
			data  : null,  // 如果data有数据，则直接根据data来渲染工具条
			type    : null,
			op      : false, //  0-新增，1-修改，2-查看
			isOverflow: true,
			defaultPos: "top", // 当上下工具条都没配置时，默认显示工具条位置: top/bottom/both
			forcePos : null,   // 强制渲染工具条位置： top/bottom/both
			dropdownOptions: {
				button: {
					text: true, // 默认不显示文字
					label: "更多" // 配置自适应时下拉按钮的文本
				}
			},
			onClick : null,
			global  : null,
			// 按钮处理 function(data, pos) {return data;}
			processData : null
		},
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	
	    	var override, data, defaultPos, forcePos;
	    	// 
	    	this.globalTimestamp = $.config.getTimestampData(this.options.global);
	    	// 构件组装数据
	    	this.assembleData = $.config.getAssembleData(this.options.global);
	    	// override current tool bar
	    	override = window[CFG_overrideName(this.globalTimestamp)]
	    	if ($.isFunction(override)) {
	    		override.apply(this);
	    	}
	    	// 
	    	this.tcfg = this._getTbarCfg();
	    	// 组装工具条按钮
	    	// 表单：当表单工具条没配置时，按系统参数默认配置的来处理
	    	// 列表：当列表工具条没配置时，默认按top的来处理
	    	defaultPos = this.options.defaultPos;
	    	forcePos = this.options.forcePos;
	    	data = this.tcfg.data;
    		if (!data.top && !data.bottom) {
    			if (defaultPos !== "top" || (forcePos && forcePos !== "top")) {
    				data.bottom = this._processTbar([], "bottom");
    			}
    			if (defaultPos !== "bottom" || (forcePos && forcePos !== "bottom")) {
    				data.top = this._processTbar([], "top");
    			}
    		} else {
    			if (data.top || (forcePos && forcePos !== "bottom")) {
    				data.top = this._processTbar(data.top, "top");
    			}
    			if (data.bottom || (forcePos && forcePos !== "top")) {
    				data.bottom = this._processTbar(data.bottom, "bottom");
    			} 
    		}
		},
		
		/**
		 * 获取工具条按钮
		 * @returns
		 */
		_getTbarCfg : function () {
			var url = this.getAction() + "!page.json?E_frame_name=coral&E_model_name=tbar" +
					"&P_tableId=" + this.options.tableId +
					"&P_moduleId=" + this.options.moduleId +
					"&P_type=" + this.options.type +
					"&P_componentVersionId=" + null2empty(this.options.componentVersionId) +
					"&P_menuId=" + null2empty(this.options.menuId) + 
					"&P_areaIndex=" + this.options.number + 
					"&P_workflowId=" + null2empty(this.options.workflowId) + 
					"&P_box=" + null2empty(this.options.box) + 
					"&P_processInstanceId=" + null2empty(this.options.processInstanceId) + 
					"&P_activityId=" + null2empty(this.options.activityId) + 
					"&P_op=" + this.options.op;
			return $.loadJson(url);
		},
		// 工作流工具条
		_processTbar : function(data, pos) {
			var fn = this.options.processData;
			data = data || [];
			// 二次开发复写按钮
			data = this.processItems(data, pos);
			if ($.isFunction(fn)) {
				data = fn.apply(this, [data, pos]);
			}
			// 
			return data;
		},
		
		/**
		 * 渲染工具条
		 * @param jq  -- 工具条位置
		 * @param pos -- top/bottom(顶部工具条、底部工具条)
		 */
		render : function (jq, pos) {
			var data  = this.tcfg.data[pos],
			    _this = this;
			if (data) {
				jq.toolbar({
		    		isOverflow : _this.options.isOverflow,
		    		dropdownOptions : _this.options.dropdownOptions,
		    		onClick: function (event, ui) {
			    			var fn = _this.options.onClick;
			    			fn = $.coral.toFunction(fn);
			    			ui.pos = ("top" === pos ? 0 : 1);
			    			if ($.isFunction(fn)) {
			    				fn(event, ui);
			    			}
			    		},
		    		data: data
		    	});
			}
			return (data || []).length > 0;
		},
		
		// 是否存在顶部工具条
		hasTop : function () {
			return (this.tcfg.data["top"] || []).length > 0;
		},
		
		// 是否存在底部工具条
		hasBottom : function () {
			return (this.tcfg.data["bottom"] || []).length > 0;
		},
		
		// 获取action		
		getAction : function () {
			var fn = window[CFG_actionName(this.globalTimestamp)];
			if ($.isFunction(fn)) return fn(this);
			
			return $.config.appActionPrefix + "/show-module";
		},
		// 对工具按钮JSON数据进行处理（如添加、删除、禁用等）
		processItems : function (data, pos) {
			return data;
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
		
		// 工具条右边按钮配置
		getRightData : function () {
			return this.rightData || {};
		}
	});

})( jQuery );
