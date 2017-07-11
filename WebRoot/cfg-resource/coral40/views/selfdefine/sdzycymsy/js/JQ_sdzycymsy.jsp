<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-04-22 14:59:19
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
	return jQuery.contextPath + "/sdzycymsy";
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
	ui._init = function(){
		var qybm = ui.getItemJQ("QYBM");
		var dataid = ui.options.dataId;
		if(isEmpty(dataid)){
			var qyxx = $.loadJson($.contextPath+'/trace!getQybm.json');
			qybm.textbox("setValue",qyxx);
		}
	}
    //保存后执行  更新试验田状态为种植  对应编码为1
    ui.afterSave = function (){
        var formData=ui.getFormData();
        //进行试验田状态修改，根据种植状态来控制对应的菜田状态改变
        var result = $.loadJson($.contextPath+"/sdzycymsy!processDkzt.json?dkbh="+formData.DKBH);
    }

	ui.bindEvent = function (){
		ui.setItemOption("YMBH", "onChange", function(e,data){
			var rowData = ui.getItemJQ("YMBH").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({
				YMBH:rowData.YMBH,
				YMPZ:rowData.YMPZ
			});
		})

		ui.setItemOption("DKBH", "onChange", function(e,data){
			var rowData = ui.getItemJQ("DKBH").combogrid("grid").grid("getRowData",data.value);
			ui.setFormData({
				DKBH:rowData.DKBH,
				YMMJ:rowData.DKMJ
			});
		})

	}



	ui.addCallback("setComboGridValue_ymbh",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			YMBH:rowData.YMBH,
			YMPZ:rowData.YMPZ
		});
	});

	ui.addCallback("setComboGridValue_dkbh",function(o){
		if (null == o) return;
		var rowData = o.rowData;
		if (null == rowData) return;
		ui.setFormData({
			DKBH:rowData.DKBH,
			YMMJ:rowData.DKMJ
		});
	});
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//ui.beforeInitGrid = function (setting) {
	//	return setting;
	//};
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
    //添加回调事件
    ui.addCallback("setComboGridValue_ymbh",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setItemValue("YMBH",rowData.YMBH);

    });

    ui.addCallback("setComboGridValue_dkbh",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setItemValue("DKBH",rowData.DKBH);

    });
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
