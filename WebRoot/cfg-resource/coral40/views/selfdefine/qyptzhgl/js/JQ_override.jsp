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
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
	return $.contextPath + "/qyptzhgl";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	var _qymc;
	ui._init = function(){
		_qymc = ui.getItemValue("QYMC");
	}
	ui.clickSave = function(op) {
		var _this = this, formUI = $("form", this.uiForm),
				url = this.getAction() + "!save.json?P_tableId=" + this.options.tableId + "&P_op=" + op + "&P_menuCode=" + CFG_getInputParamValue(this.assembleData, "menuCode"),
				formData;

		if (!formUI.form("valid")) return;
		// 保存前回调方法
		if (_this.processBeforeSave(formUI, op) === false) return;
		// 获取表单数据
		formData = formUI.form("formData", false);

		//企业名称不变时不传入后台，后天判断没有企业名称时不做额外修改
		if (formData.QYMC == _qymc) {
			delete formData.QYMC;
		}

		$.ajax({
			type : "post",
			url : url,
			data : {E_entityJson: $.config.toString(formData)},
			dataType : 'json',
			success : function(entity) {
				if (false === entity.success || !("ID" in entity)) {
					if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
					else CFG_message(entity.message, "warning");
					return;
				}
				/*
				 * if (_this.hasGrid) { $("#" +
				 * _this.options.gDivId).grid("reload"); } else if
				 * (_this.options.model) { }
				 */
				// _this.reloadSelfGrid();
				if ("save" === op) {
					formUI.form("loadData", entity);
				} else if ("close" === op) {
					if (!_this.isNested) _this.uiForm.dialog("close");
					else _this.clickBackToGrid();
				} else if ("create" === op) {
					_this.clickCreate();
				}
				// 保存后回调方法
				_this.processAfterSave(entity, op);
				CFG_message("操作成功！", "success");
			},
			error : function() {
				CFG_message("操作失败！", "error");
			}
		});
	}
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
var _this = ui;
  	ui.beforeInitGrid = function (setting){
		$.each(setting.colModel,function(e,data){
			if(e == 5){
				data.formatter = function(value, options, rowObj) {
					var id = rowObj[ui.gcfg.keyColumn],
					cgridDivId = ui.element.attr("id");
					var buts = ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id).split("<a");
					var jbfw = "<a" + buts[1];
					var zzfw = "<a" + buts[2];
					var scsj = "<a" + buts[3];
					//debugger
					var jsonData = $.loadJson($.contextPath + "/qyptscsjgl!getScsjById.json?id=" + rowObj.ID);
					if(jsonData)
						{return jbfw + zzfw+scsj;}
					return jbfw+zzfw;
			}
			}
		});
/* 		hideToolbarItem : function(id){
			if($.isCoral(this.uiTbar,"toolbar")){
				this.uiTbar.toolbar("hise",id);
			}
		},
		showToolbarItem:function(id){
			if($.isCoral(this.uiTbar,"toolbar")){
				this.uiTbar.toolbar("show",id);
			}
		} */
		return setting;
	}

	ui.addOutputValue("setTptableIdColumns", function(o){
		var ids = ui.getSelectedRowId();//ui.selectedRowId;
		if(ids.length == 0){
			CFG_message("请选择记录", "warning");
			return;
		}
		if(ids.length > 1){
			CFG_message("只能选择一条记录", "warning");
			return;
		}
		var tableId = $.loadJson($.contextPath +'/tyttgj!getTpTableId.json?pTableId='+ui.options.tableId);
		o={
			status:true,
			P_tableId : tableId,
			P_columns : "EQ_C_ZBID≡"+ids
		};
		return o;

	})


	ui.beforeInitUpload = function(upOpt) {
		upOpt.fileTypeExts = "*.jpg;*.png;*.bmp";
		return upOpt;
	}
	ui.addOutputValue("getZhbh", function() {

		var rowId = ui.selectedRowId;
		var zhbh = $.loadJson($.contextPath + "/qyptqyzzfwxx!getZhbhById.json?uuid=" + rowId);
		var o = {
			status : true,
			zhbh : "EQ_C_ZHBH≡" + zhbh
		}
		return o;
	});
	
	//弹出式生产数据过滤(根据选中的企业获取相应的基地生产数据)
	ui.addOutputValue("setTcsCcsj",function(o){
			var rowId = ui.selectedRowId;
			var qybms = $.loadJson($.contextPath + "/qyptscsjgl!getJdbm.json?id=" + rowId);
			 var qybmstring=qybms.data;
			var ms="";
			ms += "(";
			for(var k=0;k<qybmstring.length;k++){
				if(k==qybmstring.length-1){
					ms += "(EQ_C_QYBM≡" + qybmstring[k].ZHBH + ")";
				}else{
					ms += "(EQ_C_QYBM≡" + qybmstring[k].ZHBH + ")"+"OR";
				}
			} 
			ms += ")";
				var o = {
					status : true,
					P_columns : ms
				}
				return o;
			});
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
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
