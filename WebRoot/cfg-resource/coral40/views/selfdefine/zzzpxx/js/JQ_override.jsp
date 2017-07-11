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
	return $.contextPath + "/zzzpxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
//var syct = ui.getItemJQ("SYCT");
	//回调函数处理使用菜田信息
	ui.addCallback("setComboGridValue_ctda", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		//根据所选菜田信息得到菜田负责人
 		ui.setFormData({SYCT:obj.CTMC,SYCTBH:obj.CTBH});
		defalutDataBy_ctda(obj.CTBH);
 	}); 
	//回调函数处理使用菜种信息
	ui.addCallback("setComboGridValue_czda", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({SYCZ:obj.CZTYM,SYCZBH:obj.CZBH});
 		defalutDataBy_czda(obj.CZBH);
 	});
 	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
 	});  
 	ui.bindEvent = function () {
 		//使用菜田名称和编号处理
 		var syctbh = ui.getItemJQ("SYCTBH");
 		syctbh.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			//根据所选菜田信息得到菜田负责人
			ui.setFormData({SYCT:newText,SYCTBH:newValue});
			defalutDataBy_ctda(newValue);
		});
		//使用菜中名称和编号处理
 		var syczbh = ui.getItemJQ("SYCZBH");
 		syczbh.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			ui.setFormData({SYCZ:newText,SYCZBH:newValue});
			defalutDataBy_czda(newValue);
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.newText;
			var newValue = data.newValue;
			ui.setFormData({FZR:newText,FZRBH:newValue});
		});
 	}	
 	if(ui.options.number==1){
 		/**
		 * 设置菜田列表过滤条件
		 */
		ui.addOutputValue("setCtdaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1;EQ_C_ZZZT≡2"
			}
			//alert("o : =" + o.P_columns);
			return o;
		});
		/**
		 * 设置菜种列表过滤条件：菜种档案
		 */
		ui.addOutputValue("setCzdaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1"
			}
			return o;
		});
		/**
		 * 设置菜种列表过滤条件：工作人员
		 */
		ui.addOutputValue("setGzrydaColumns",function(o){
			var o = {
				status : true,
				P_columns : "EQ_C_QYZT≡1"
			}
			return o;
		});
 	
 	
 	}
 	
 	
 	//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动种植批次号生成
			var qybm=$.loadJson($.contextPath + "/zzcdda!getQybm.json");
			var zzpch=$.loadJson($.contextPath + "/zzzpxx!getZzpch.json");
			//初始化加载账号编码和种植批次号
			ui.setFormData({ZZPCH:zzpch,QYBM:qybm});
		}
 	}
 	ui.afterSave = function (){
 	//账号编号 ，菜田编号，
 		var formData=ui.getFormData();
 		//进行菜田状态修改，根据种植状态来控制对应的菜田状态改变
 		var rst=$.loadJson($.contextPath + "/zzctda!updCtzt.json?ctbh="+formData.SYCTBH+"&zzzt="+formData.QYZT);
 	}
 	
 	
 	function defalutDataBy_ctda(ctbh){
 		var obj=$.loadJson($.contextPath + "/zzctda!getCtdaFzr.json?ctbh="+ctbh);//getCtdaFzr
 		ui.setFormData({FZR:obj[0].FZR,FZRBH:obj[0].FZRBH});
 	}
 	
 	//默认进行商品名称和商品编码加载
 	function defalutDataBy_czda(czbh){
 		var obj=$.loadJson($.contextPath + "/zzzzda!getCzdaObj.json?czbh="+czbh);//getCtdaFzr
 		ui.setFormData({SPBM:obj.SPBM,SPMC:obj.SPMC});
 	}
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
//url   = this._getSearchUrl(),
	//console.log("ui.global:=" + ui._getSearchUrl());
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
