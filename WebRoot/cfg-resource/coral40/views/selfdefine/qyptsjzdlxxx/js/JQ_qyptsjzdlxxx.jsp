<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2015-12-10 10:14:55
 * 系统配置平台自动生成的自定义构件二次开发JS模板
 * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
 * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
 ***************************************************/
 
 
(function(subffix) {

/**
 * 二次开发定位自己controller
 * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return jQuery.contextPath + "/qyptsjzdlxxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};
	var lxbm = CFG_getInputParamValue(ui.assembleData, 'lxbm');
	var lxmc = CFG_getInputParamValue(ui.assembleData,'lxmc');
	ui._init = function(){
		ui.setFormData({
			FLBM:lxbm,
			FLMC:decodeURI(lxmc)
		});
	};
	ui.bindEvent = function(){
		var lxbmJQ = ui.getItemJQ("LXBM");
		lxbmJQ.textbox("option","onChange",function(e,data){
            var jsonData = $.loadJson($.contextPath + "/qyptsjlx!getIsRepeatByLxbm.json?id=" + ui.options.dataId + "&lxbm=" + lxbmJQ.textbox("getValue"));
            if(jsonData === false || jsonData === "false"){
                $.message( {message:"类型编码重复！", cls:"warning"});
                lxbmJQ.textbox("setValue","");
            }
		});
	}
	//新增后添加树子节点或修改后更改节点
	ui.afterSave = function (entity) {
		//页面ID判断表单为新增还是修改
		ymId = ui.options.dataId;
		if(ymId == null){
		//若为空  则为新增			
			ui.assembleData.parentConfigInfo.namespace.addLeftChildNode({
				id : entity.LXBM,
				name : entity.LXMC,
				diczjd : "diczjd"
			}); 
		}else{
		//若有值 为修改
			ui.assembleData.parentConfigInfo.namespace.updateLeftChildNode({
				id : entity.LXBM,
				name : entity.LXMC,
				diczjd : "diczjd"
			});
		}

	}; 
	

};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	//这里lxbm是分类编码
	var lxbm = CFG_getInputParamValue(ui.assembleData, 'lxbm');
	//var gridData=$.loadJson($.contextPath + '/qyptsjzdlxxx!getGridData.json?lxbm='+lxbm);
	ui.beforeInitGrid = function (setting){
		setting.url = $.contextPath + '/qyptsjzdlxxx!getGridData.json?lxbm='+lxbm;
		return setting;
	};
	//删除分类节点下的类型节点
	ui.afterDelete = function(idArr, isLogicalDelete){
		var lxbms = [];
		for (var i in idArr) {
			var rowData = ui.uiGrid.grid("getRowData", idArr[i]);
			lxbms.push(rowData.LXBM);
		}
		ui.assembleData.parentConfigInfo.namespace.deleteLeftTreeNode(lxbms);
	};
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写基本查询区
 */
function _override_bsearch (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	    // 添加onChange事件
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
	//};
};

/**
 *  二次开发：复写高级查询区
 */
function _override_gsearch (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {
	// ui.assembleData 就是 configInfo
};

/***
 * 当前构件全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
//jQuery.extend(jQuery.ns("ns" + subffix), {
//	name : "",
//	click: function() {}
//});









/**
 * 在此可以复写所有自定义JS类
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof jQuery.config.cform) {
		_override_form(this);
	} else if (this instanceof jQuery.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof jQuery.config.cbsearch) {
		_override_bsearch(this);
	} else if (this instanceof jQuery.config.cgsearch) {
		_override_gsearch(this);
	} else if (this instanceof jQuery.config.ctree) {
		_override_tree(this);
	} else if (this instanceof jQuery.config.ctbar) {
		_override_tbar(this);
	} else if (this instanceof jQuery.config.clayout) {
		_override_layout(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
