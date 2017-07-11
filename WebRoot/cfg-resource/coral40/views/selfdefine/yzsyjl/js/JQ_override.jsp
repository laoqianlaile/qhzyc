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
	return $.contextPath + "/yzsyjl";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//根据养殖批次号，处理相关的数据：猪舍编号、品种通用名、进栏日期、负责人、负责人编号
	ui.addCallback("setComboGridValue_jlxx", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		//根据所选养殖批次号信息得到负责人及相关的数据
		var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+obj.YZPCH);//getCtdaFzr
 		ui.setFormData({
	 		A_YZPCH:obj.YZPCH,//养殖批次号
	 		B_SYZS:fzr_obj.SYZS,//使用猪舍
	 		B_JLRQ:fzr_obj.JLRQ,//进栏日期
	 		B_PZTYM:fzr_obj.PZTYM,//品种通用名
	 		A_FZR:fzr_obj.FZR,//负责人
	 		A_FZRBH:fzr_obj.FZRBH//负责人编号
 		});
 	}); 
 	//根据饲料信息，处理相关的数据：饲料编号、饲料通用名
	ui.addCallback("setComboGridValue_slda", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({
	 		A_SYSLBH:obj.SLBH,//饲料编号
	 		A_SYSL:obj.SLTYM//饲料通用名
 		});
 	}); 
 	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({A_FZR:obj.XM,A_FZRBH:obj.GZRYBH});
 	}); 
 	
 	ui.bindEvent = function (){
 		var yzpch=ui.getItemJQ("A_YZPCH");
 		yzpch.combogrid("option","onChange",function(e,data){
			var newValue = data.value;
			//根据所选养殖批次号信息得到负责人及相关的数据
			var fzr_obj=$.loadJson($.contextPath + "/yzjlxx!getJlxxFzr.json?yzpch="+newValue);//getCtdaFzr
	 		ui.setFormData({
		 		A_YZPCH:newValue,//养殖批次号
		 		B_SYZS:fzr_obj.SYZS,//使用猪舍
		 		B_JLRQ:fzr_obj.JLRQ,//进栏日期
		 		B_PZTYM:fzr_obj.PZTYM,//品种通用名
		 		A_FZR:fzr_obj.FZR,//负责人
		 		A_FZRBH:fzr_obj.FZRBH//负责人编号
	 		});
		});
		//使用肥料名称和编号处理
 		var syslbh = ui.getItemJQ("A_SYSLBH");
 		syslbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_SYSL:newText,A_SYSLBH:newValue});
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("A_FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({A_FZR:newText,A_FZRBH:newValue});
		});
 	
 	}
 	//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {
			//自动企业编码
			var qybm=$.loadJson($.contextPath + "/yzcdda!getQybm.json");
			ui.setFormData({A_QYBM:qybm});
		} else {
            /********修改时加载使用畜舍，下拉列表数据必须包含本身********/
            var $syzs = ui.getItemJQ("B_SYZS");
            var syzs = $syzs.combogrid("getValue");
            $syzs.combogrid("option","url","yzzsda!getZsdaGrid.json?self=" + syzs);
            $syzs.combogrid("reload");
            $syzs.combogrid("setValue",syzs);

            var $sysl = ui.getItemJQ("A_SYSLBH");
            var sysl = $sysl.combogrid("getValue");
            $sysl.combogrid("option","url","yzslda!getSldaGrid.json?self=" + sysl);
            $sysl.combogrid("reload");
            $sysl.combogrid("setValue",sysl);
        }
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
