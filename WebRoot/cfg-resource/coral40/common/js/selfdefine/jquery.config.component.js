/*!
 * @author qiucs
 * @date   2014.7.17
 * 系统配置平台应用自定义核心JS
 */
(function( $, undefined ) {

	"use strict";
	
	$.component("config.router", {
		version: $.config.version,
		options: {
			menuId  : null, // 菜单ID
			menuCode: null, // 菜单编码
			componentVersionId : null, // 在生产库中对应的构件ID
			moduleId: null, // 自定义构件自身ID
			tableId : null, // 自定义构件对应的物理表ID
			groupId : null, // 自定义构件对应的物理表组ID
			columns : null, // 主列表的过滤条件
			dataId  : null, // 自定义构件要打开的记录ID
			master  : null, // {tableId: "xx", dataId: "yy"}
			appendTo: null,
			timestamp: null,
			masterCgridDivId: null // 自定义列表构件ID
		},
		/**
		 * 创建构件
		 */
	    _create: function () {
	    	var data = this._getInfo();
	    	this.info = data.data;
	    	// URL中的参数缓存
	    	$.config.setGlobalData(this.options.appendTo.get(0), this.options.paramMap);
	    	// 存储物理表组ID
    		//if (isNotEmpty(this.options.groupId)) {
    		//	$.config.setGlobalData(this.options.appendTo.get(0), {groupId: this.options.groupId});
    		//}
    		// 存储物理表ID
    		//if (isNotEmpty(this.options.tableId)) {
	    	//	$.config.setGlobalData(this.options.appendTo.get(0), {tableId: this.options.tableId});
	    	//}
    		
	    	if ("5" == this.info.type) { 
	    		// 逻辑表构件
	    		if (isEmpty(this.options.groupId)) {
	    			CFG_message("请指定具体的物理表组！", "error");
	    			return;
	    		}
	    		if (false === data.success/*this.reAssembleAreaLayout()*/) {
	    			CFG_message("您传入的物理表组与当前逻辑表构件指定的逻辑表组不匹配！", "error");
	    			return;
	    		}
	    	} else if ("6" == this.info.type && $.config.pattern._1C == this.info.templateType) {
	    		// 通用表构件
	    		if (isNotEmpty(this.options.tableId)) {
	    			this.info.table1Id = this.options.tableId;
	    		}
	    		// 二次开时，可以手动给tableId赋值
	    		/* else {
	    			CFG_message("未指定物理表，请联系管理员！", "error");
	    			return;
	    		}*/
	    	}
	    	
	    	if (isEmpty(this.options.componentVersionId)) {
	    		this.options.componentVersionId = this.info.componentVersionId;
	    	}
	    	
	    	var ltoptions = {
	    			menuId : null2empty(this.options.menuId),
	    			componentVersionId : null2empty(this.options.componentVersionId),
	    			moduleId : this.options.moduleId,
	    			tableId  : null2empty(this.options.tableId),
	    			groupId  : null2empty(this.options.groupId),
	    			columns  : null2empty(this.options.columns),
	    			dataId   : null2empty(this.options.dataId),
	    			info   : this.info,
	    			master : this.options.master,
	    			global : this.options.appendTo.get(0)
	    	};
	    	// 存储时间戳
	    	$.config.setTimestampData(ltoptions.global, this.options.timestamp);
	    	// 存储被绑定的自定义列表构件(DIV)ID
	    	if (isNotEmpty(this.options.masterCgridDivId)) {
	    		$.config.setMasterCgridDivId(ltoptions.global, this.options.masterCgridDivId);
	    	}
	    	// 创建布局
	    	this.uiBoard = new $.config.clayout(ltoptions, this.options.appendTo);
		},
		/**
		 * 获取构件布局配置信息
		 **/
		_getInfo : function () {
			var url  = $.config.appActionPrefix + "/module!toPageModule.json?id=" + this.options.moduleId + "&P_groupId=" + this.options.groupId;
			return $.loadJson(url);
		}/*,
		// 获取物理表组
		_getPhysicalGroup : function () {
			var url = $.config.appActionPrefix + "/physical-group-define!getGroupTables.json?id=" + this.options.groupId;
			return $.loadJson(url);
		},
		// 
		reAssembleAreaLayout : function () {
			var pGroup = this._getPhysicalGroup();
			if (pGroup.logicGroupCode !== this.info.logicTableGroupCode) {
				return false;
			}
			// 将传进来的表ID置空，防止被更改
			this.options.tableId = null;
			// 把逻辑表转为物理表
			if (isNotEmpty(this.info.table1Id)) this.info.table1Id = pGroup[this.info.table1Id].id;
			if (isNotEmpty(this.info.table2Id)) this.info.table2Id = pGroup[this.info.table2Id].id;
			if (isNotEmpty(this.info.table3Id)) this.info.table3Id = pGroup[this.info.table3Id].id;
			return true;
		}*/
		
	});

})( jQuery );