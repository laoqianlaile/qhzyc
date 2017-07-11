<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 

(function(subffix) {

var isExit;
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
	//初始化参数
	ui._init = function () {
		$.each($(".homeSpan"), function(e, data){
			if(e != 0 ){
				if($(this).children().children().html() == " - 企业信息 - 新增"){
					$(this).children().children().html(" - 区域信息 - 新增")
				}
			}
		});
		var jdbh = ui.getItemJQ("JDMC");//基地编号
		//jdbh.combogrid("reload",'zzdyxx!getJdxx.json');
		if (isEmpty(ui.options.dataId)) {
			var qyxx=$.loadJson($.contextPath + '/sczzqyxx!getjdxx.json');
			ui.setFormData({QYBM:qyxx.QYBM,QYMC2:qyxx.QYMC2});
		}
		//获取当前企业基地信息
		var getQyxx = $.loadJson($.contextPath +'/qyxxjdxx!getJDXX.json');
		ui.setFormData({JDMC:getQyxx.JDMC,JDBH:getQyxx.JDBH});
	}
	ui.beforeSave = function(jq,options){
		var id = ui.getItemJQ("ID");
		var qybh = ui.getItemJQ("QYBH");
		var jdbh = ui.getItemJQ("JDBH");
		var flag = $.loadJson($.contextPath + '/sczzqyxx!isExist.json?qybh=' + qybh.val() + "&jdbh=" + jdbh.val()+"&id="+id.val());

		if (flag) {
			CFG_message("区域编号重复，请重新输入！", "error");
			return false;
		}else{

            var qymjJQ = ui.getItemJQ("QYMJ");//区域面积
            var jdbh = ui.getItemJQ("JDBH").textbox("getValue");
            var qybh = ui.getItemJQ("QYBH").textbox("getValue");
            if(jdbh.length == 0 || qybh.length == 0){
                return false;
            }
            var mj = qymjJQ.textbox("getValue");
            if(mj == 0){
                CFG_message("剩余面积不够！", "error");
                return false;
            }
            var jsonData = $.loadJson($.contextPath + "/trace!checkMj.json?mk=qy&bh="+jdbh+"&mj="+mj+"&sbh="+qybh);
            if(jsonData.result == "ERROR"){
                qymjJQ.textbox("setValue","");
                CFG_message(jsonData.msg, "error");
                return false;
            }

				return true;
		}
	}
	//
	ui.bindEvent = function () {
		//负责人名称和编号处理
		var fzr = ui.getItemJQ("FZRBH");
		fzr.combogrid("option", "onChange", function (e, data) {
			var newText = data.newText;
			var newValue = data.newValue;
			ui.setFormData({FZR: newText, FZRBH: newValue});
		});
		var jdmc = ui.getItemJQ("JDMC");//基地编号
/* 		jdmc.combogrid("option","onChange",function(e,data){
			//获取下拉列表选中行数据
			//设定值

			ui.setFormData({
				JDBH:data.newValue,
				JDMC:data.newText

			});
		}); */
//        var qymjJQ = ui.getItemJQ("QYMJ");//区域面积
//        qymjJQ.textbox("option","onChange",function(e,data){
//            var jdbh = ui.getItemJQ("JDBH").combogrid("getValue");
//            var qybh = ui.getItemJQ("QYBH").textbox("getValue");
//            if(jdbh.length == 0 || qybh.length == 0){
//                return false;
//            }
//            var mj = qymjJQ.textbox("getValue");
//            if(mj.length == 0){
//                return false;
//            }
//            var jsonData = $.loadJson($.contextPath + "/trace!checkMj.json?mk=qy&bh="+jdbh+"&mj="+mj+"&sbh="+qybh);
//            if(jsonData.result == "ERROR"){
//                qymjJQ.textbox("setValue","");
//                CFG_message(jsonData.msg, "error");
//            }
//        });
	}
	//回调函数处理使负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
	});
	ui.addOutputValue("setGzrydaColumns",function(o){
		var o = {
			status : true,
			P_columns : "EQ_C_QYZT≡1"
		}
		return o;
	});
	ui.addCallback("setComboGrid_Jdxx",function(o){
		if (null == o) return;
		var rowData = o.result;
		if (null == rowData) return;
		ui.setFormData({
			JDBH:rowData.JDBH,
			JDMC:rowData.JDMC
		});
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
