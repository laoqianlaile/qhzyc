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
	return $.contextPath + "/appmanage/show-module";
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
	var objId = ui.options.dataId;
	if ( isEmpty(objId) ){}
		//新增
	
	if(1 == ui.options.number){
		
		ui.bindEvent = function(){
			var lshData = $.loadJson($.contextPath + "/xuqiugenzongjuzhengoujian!getLSH.json");
			ui.setFormData({XH:lshData});
			
			var DMJCRQ = ui.getItemJQ("DMJCRQ");
			var SJWCRQ = ui.getItemJQ("SJWCRQ");
			
			DMJCRQ.datepicker("option","onChange",function(){
				var sjwcrqData = SJWCRQ.datepicker( "getDateValue" );
				var dmjcrqData = DMJCRQ.datepicker( "getDateValue" );
				if(sjwcrqData.length != 0){
					if(dmjcrqData >= sjwcrqData){
						//alert(true);
					}else{
						DMJCRQ.datepicker( "setDate" ,"");
						CFG_message("代码检查日期不能小于实际检查日期！", "warning");
					}
				}
			});
			
			SJWCRQ.datepicker("option","onChange",function(){
				var sjwcrqData = SJWCRQ.datepicker( "getDateValue" );
				var dmjcrqData = DMJCRQ.datepicker( "getDateValue" );
				if(dmjcrqData.length != 0){
					if(dmjcrqData >= sjwcrqData){
						//alert(true);
					}else{
						SJWCRQ.datepicker( "setDate" ,"");
						CFG_message("代码检查日期不能小于实际检查日期！", "warning");
					}
				}
			});
			
		}
		
		
		
		
		
		
	}
	ui.beforeSave = function () {
		var bool = true;
			var XQMC = $("input[name='XQMC']",ui.uiForm);
			var XH = $("input[name='XH']",ui.uiForm);
			var XQSM = $("textarea[name='XQSM']",ui.uiForm);	
			
			var	xqmcValue = XQMC.textbox("getValue");
			var xhValue = XH.textbox("getValue");
			var xqsmValue = XQSM.textbox("getValue");
				if(xqmcValue.length!=0){
					var JcxqmcData = $.loadJson($.contextPath + "/xuqiugenzongjuzhengoujian!getJcxqmc.json?xqmc="+xqmcValue+"&xh="+xhValue);
					if(!JcxqmcData){
						CFG_message("需求名称重复！", "warning");
						bool = false;
					} 
				}
				
			if(bool){
				if(xqmcValue.length == 0 && xqsmValue.length == 0){
					CFG_message("需求名称与需求说明不能都为空！", "warning");
					bool = false;
				}
			}
		return bool;
	}
	
	ui.clickSave = function(op) {
			var _this = ui, jqForm = $("form", this.uiForm), 
			    url = ui.getAction() + "!save.json?P_tableId=" + ui.options.tableId + "&P_op=" + op 
                                       + "&P_componentVersionId=" + ui.options.componentVersionId
                                       + "&P_menuId=" + ui.options.menuId
			                           + "&P_menuCode=" + CFG_getInputParamValue(ui.assembleData, "menuCode"),
			    formData;

			if (!jqForm.form("valid")) return;
			// 保存前回调方法
			if (_this.processBeforeSave(jqForm, op) === false) return;
			// 获取表单数据
		    formData = jqForm.form("formData", false);
		    
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
					// 保存后回调方法
					_this.processAfterSave(entity, op);
					CFG_message("操作成功！", "success");
					if ("save" === op) {
						jqForm.form("loadData", entity);
					} else if ("close" === op) {
						if (!_this.isNested) _this.uiForm.dialog("close");
						else _this.clickBackToGrid();
					} else if ("create" === op) {
						_this.clickCreate();
						var lshData = $.loadJson($.contextPath + "/xuqiugenzongjuzhengoujian!getLSH.json");
						ui.setFormData({XH:lshData});
					}
				},
				error : function() {
					CFG_message("操作失败！", "error");
				}
			});
		}
	
	ui.afterSave = function(){
		//var lshData = $.loadJson($.contextPath + "/xuqiugenzongjuzhengoujian!getLSH.json");
		//ui.setFormData({XH:lshData});
	}
	
	
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
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
	
	if (1 === ui.options.number) {
			
				

	}
	
	
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
