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
	return $.contextPath + "/jinchangxinxi";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	
	ui._init = function() {
		if (isEmpty(ui.options.dataId)) {//判断是否为新增
			 $.ajax({
				 type:'post',
				 url:$.contextPath +"/jinchangxinxi!getJcsfbh.json",
				 data:{Qz:5310,Zl:"JCSFBH"},
				 dataType:'json',
				 success:function(data){
					 ui.setFormData({JCSFBH:data});
				 }
			 });

			var jsonData=$.loadJson($.contextPath +'/qyda!getQyda.json?prefix=PC');
			ui.setFormData({
					PFSCBM:jsonData.pfscbm,
					PFSCMC:jsonData.pfscmc
			});
		}
		
		//设置为只读
		var jcsfbh = $("input[name='JCSFBH']",ui.uiForm);
		jcsfbh.textbox({
			readonly:true
		});
		var jsonData=$.loadJson($.contextPath +"/jyzxx!getJyzxx.json?xtlx=PC&zt=1");
		var pfsbm = ui.getItemJQ("PFSBM");
		var lssmc = ui.getItemJQ("LSSMC");
		pfsbm.combogrid("reload",jsonData.data);
		lssmc.combogrid("reload",jsonData.data);
 	}
	
	ui.addCallback("setComboGridValue_Pfsmc",function(o){
		if (null == o) return;
		var rowData = o.result;
		if (null == rowData) return;
		ui.setFormData({PFSMC:rowData.A_JYZMC,PFSBM:rowData.A_JYZBM});
	});
	ui.bindEvent = function (){
		var jqPfsbm = ui.getItemJQ("PFSBM");
		jqPfsbm.combogrid("option","onChange",function(e,data){//批发商名称下拉框事件
			ui.setFormData({PFSMC:data.text,PFSBM:data.value});
		});
	
	}
	ui.addOutputValue("setJyzxxColumns",function(o){

		return {
			status:true
		}
	});
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
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
 */
window[CFG_overrideName(subffix)] = function () {
	if (this instanceof $.config.cform) {
		_override_form(this);
	} else if (this instanceof $.config.cgrid) {
		_override_grid(this);
	} else if (this instanceof $.config.ctree) {
		_override_tree(this);
	} else if (this instanceof $.config.ctbar) {
		_override_tbar(this);
	}
};

	
	
	
	
})("${timestamp}");
</script>
