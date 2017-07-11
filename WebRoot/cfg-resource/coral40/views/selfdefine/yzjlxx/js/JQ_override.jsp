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
	return $.contextPath + "/yzjlxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	//回调函数处理使用猪舍信息
	ui.addCallback("setComboGridValue_zsda", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		//根据所选菜田信息得到菜田负责人
		var fzr_obj=$.loadJson($.contextPath + "/yzzsda!getCsdaFzr.json?csbh="+obj.CSBH);
		//console.log(fzr_obj);
 		ui.setFormData({SYZS:obj.CSBH,FZR:fzr_obj[0].FZR,FZRBH:fzr_obj[0].FZRBH});
 	}); 
	//回调函数处理使用仔猪信息
	ui.addCallback("setComboGridValue_zzpch", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
		var zzpch=obj.ZZPCH;
		obj=$.loadJson($.contextPath + "/yzzzda!getZzdaByzzbh.json?zzpch="+zzpch);
 		ui.setFormData({ZZPCH:zzpch,PZTYM:obj.PZTYM,PZQC:obj.PZQC});
 		var sl = ui.getItemJQ("SL");
 		checkSurplusSl(zzpch,sl.val());
 	});
 	//回调函数处理负责人信息
	ui.addCallback("setComboGridValue_fzr", function(o){
		if (null == o) return;
		var obj =o.rowData;
		if (null == obj) return;
 		ui.setFormData({FZR:obj.XM,FZRBH:obj.GZRYBH});
 	});
 	ui.bindEvent = function () {
 		//处理猪舍所要带出的数据
 		var syzs = ui.getItemJQ("SYZS");
 		syzs.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			var fzr_obj=$.loadJson($.contextPath + "/yzzsda!getCsdaFzr.json?csbh="+newValue);
 			ui.setFormData({SYZS:newText,FZR:fzr_obj[0].FZR,FZRBH:fzr_obj[0].FZRBH});
		});
 		//负责人名称和编号处理
 		var fzrbh = ui.getItemJQ("FZRBH");
 		fzrbh.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			ui.setFormData({FZR:newText,FZRBH:newValue});
		});
 		//仔猪批次号处理处理
 		var zzpch = ui.getItemJQ("ZZPCH");
 		zzpch.combogrid("option","onChange",function(e,data){
			var newText = data.text;
			var newValue = data.value;
			obj=$.loadJson($.contextPath + "/yzzzda!getZzdaByzzbh.json?zzpch="+newText);
 			ui.setFormData({ZZPCH:obj.ZZPCH,PZTYM:obj.PZTYM,PZQC:obj.PZQC});
			checkSurplusSl(newText,sl.val());
		});
		var sl = ui.getItemJQ("SL");
		sl.textbox("option","onChange",function (e,data){
			var newValue=data.value;
			//判断同一仔猪批次中的数量是否有超出，超出进行提示
			var formData=ui.getFormData();
			checkSurplusSl(formData.ZZPCH,newValue);
		})
 	} 
 	function checkSurplusSl(zzpch,sl){
 		if(zzpch=="") return ;
 		if(sl=="") return ;
 		var surplusNum=$.loadJson($.contextPath + "/yzzzda!getSurplusSl.json?zzpch="+zzpch);
 		if(sl<=0){
 			$.alert({message:"进栏的数量不符合规范",modal:true});
 			ui.setFormData({SL:""});
 			return;
 		}
 		if(surplusNum>=sl) return ;
 		if(surplusNum<sl){
 			$.alert({message:"进栏的数量大于剩余养殖数量<br/>剩余数量："+surplusNum,modal:true});
 			ui.setFormData({SL:""});
 			return;
 		}
 		
 	}
 	//初始化参数
 	ui._init = function () {
		if (isEmpty(ui.options.dataId)) {//自动养殖批次号生成
			//默认添加所属账户编号
			var yzpch=$.loadJson($.contextPath + "/yzjlxx!getYzpch.json");
			//默认添加所属账户编号
			var qybm=$.loadJson($.contextPath + "/yzcdda!getQybm.json");
			ui.setFormData({YZPCH:yzpch,QYBM:qybm});
		} else {
            /********修改时加载使用畜舍，下拉列表数据必须包含本身********/
            var $syzs = ui.getItemJQ("SYZS");
            var syzs = ui.getItemValue("SYZS");
            $syzs.combogrid("option","url","yzzsda!getZsdaGrid.json?self=" + syzs);
            $syzs.combogrid("reload");
            $syzs.combogrid("setValue",syzs);
        }
 	}
 	ui.afterSave = function (){
 		//获得页面的操作对象
 		var formData=ui.getFormData();
 		//进行猪舍状态修改
 		var rst=$.loadJson($.contextPath + "/yzzsda!updataSyzt.json?zsbh="+formData.SYZS);
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
