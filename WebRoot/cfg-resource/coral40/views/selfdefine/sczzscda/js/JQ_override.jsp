<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/sczzscda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () { 
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	ui.beforeDelete=function (idArr, isLogicalDelete) {
		//var isExistZzdyxx = $.loadJson($.contextPath + "/trace!canDelete.json?ids="+idArr+"&mk=SCDA");

		var isExistZzdyxx = $.loadJson($.contextPath + "/sczzscda!queryPch.json?ids="+idArr);
		if(isExistZzdyxx.data[0].JYJG=='1'){
			CFG_message("已经检验，不能删除！","error");
			return false;
		}
		if(isExistZzdyxx.data[0].ZT=='0'){
			CFG_message("已经采收，不能删除！","error");
			return false;
		}
		return true;
	};
	ui.databaseDelete = function (idArr, isLogicalDelete) {
		var _this = ui,
				url   = this.getAction() + "!destroy.json?P_tableId=" + ui.options.tableId +
						"&P_D_tableIds=" + ui.getDetailTableIds() +
						"&P_isLogicalDelete=" + isLogicalDelete +
						"&P_menuId=" + ui.options.menuId +
						"&P_menuCode=" + ui.getParamValue("menuCode");
		// 删除前，业务处理
		if (false === ui.beforeDelete(idArr, isLogicalDelete)) {
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
	};
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tree!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
	//console.log("override tbar!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	//console.log("override layout!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
};








/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	
	//var startTime = new Date().getTime();
	
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof $.config.clayout) {
		_override_layout(this);
	}
	
	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
