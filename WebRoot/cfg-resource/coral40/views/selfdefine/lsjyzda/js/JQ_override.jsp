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
	return $.contextPath + "/lsjyzda";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	ui._init = function(){
			//生成经营者编码
			var jyzbm = $("#JYZBM_"+ui.timestamp, this.uiForm);
			if(jyzbm.textbox("getValue")==null||jyzbm.textbox("getValue")==''){
				jQuery.ajax({
					type:'post',
					url:$.contextPath +'/lsjyzda!getJyzbm.json',
					dataType:'json',
					async:false,
					success:function(data){
						jyzbm.textbox("setValue",data.jyzbm);
					},
					error:function(data){
					}
				});
			}
			//加载表单其他默认值
			jQuery.ajax({
					type:'post',
					url:$.contextPath +'/csjyzda!initForm.json',
					dataType:'json',
					success:function(data){
						ui.setFormData({BALTJDBM:data.bajdbm,XXGXRQ:data.gxrq,BALTJDMC:data.bajdmc});
					},
					error:function(data){
					}
				});
		}
    ui.bindEvent = function (){
        var jylx = ui.getItemJQ("JYLX");
        //经营者类型
        jylx.combobox("option" , "onChange" , function( e , data ){
            var jylxValue = $.loadJson($.contextPath + "/lsjyzda!getJyzlx.json?jylxValue="+data.newValue);
            ui.setFormData({JYZLX:jylxValue});
        });
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
	
	var startTime = new Date().getTime();
	
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
	
	console.log("over ride cost time: " + (new Date().getTime() - startTime));
};

	
	
	
	
})("${timestamp}");
</script>
