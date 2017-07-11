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
	return $.contextPath + "/tzgzryda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//从业所需证件号只读性控制
	ui.bindEvent = function(){
		var jqCertificateCode = $("input[name='CYZJH']",ui.uiForm),
		jqCertificate = $("input[name='CYZJ']",ui.uiForm);
		jqCertificate.textbox("option","onKeyUp",function(){
			if(!isEmpty(jqCertificate.textbox("getValue"))){
				jqCertificateCode.textbox({
					readonly : false
				});
			}
			else{
				jqCertificateCode.textbox({
					readonly : true
				});
				jqCertificateCode.textbox("setValue","");
			};
		});
	};
	
	
	//页面数据加载完执行
	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//工作人员编号生成
			$.ajax({
				type:'post',
				url:$.contextPath + "/tzgzryda!getGzrybh.json",
			 	//data:{Zl:"交易编号",Qz:5310,Cd:"11"},
			 	data:{Zl:"GZRYBH"},
				dataType:'json',
				success:function(data){
					ui.setFormData({GZRYBH:data});
				}
			})
		}
		
		//修改页面从业所需证件号只读性
		if (!isEmpty(ui.options.dataId)) {  //判断为修改
			var jqCertificate = $("input[name='CYZJ']",ui.uiForm),
			jqCertificateCode = $("input[name='CYZJH']",ui.uiForm);
			if(!isEmpty(jqCertificate.textbox("getValue"))){
				jqCertificateCode.textbox({
					readonly : false
				});
			};
		};
		
		//屠宰场名称，屠宰场编码隐藏字段赋值
		var jsonData = $.loadJson($.contextPath + '/tzqyda!getQyda.json?prefix=TZ');
		ui.setFormData({
			TZCMC:jsonData.tzcmc,
			TZCBM:jsonData.tzcbm
		});//屠宰场名称，屠宰场编码隐藏字段赋值
	};
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
