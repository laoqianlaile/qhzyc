<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2015-11-23 12:06:38
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
	return jQuery.contextPath + "/sczzpccc";
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
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.clickSecondDev = function(id){
		if (id == $.config.preButtonId + "print") {
			if(isSwt || isNewSwt){
				if(ui.getSelectedRowId().length != 1){
					CFG_message("请选择一条记录", "warning");
					return;
				}
			}else{
				$.alert("请在程序环境中写卡");
				return;
			}
			var $printDatas = $.loadJson($.contextPath + "/sczzpccc!getPrint.json?id=" + ui.getSelectedRowId()[0]);
			if(isSwt){
				$.each($printDatas,function(e, data){
					var data = {
						code:data.SFDH,
						buyer:data.KHMC
					}
					var result = _window("printAttach",JSON.stringify(data))
				});
			}else if(isNewSwt){
				$.each($printDatas,function(e, data){
					var result = _print.print("zzccsfdPrint",{
						"XSDDH":"销售订单号 ：" + data.XSDDH,
						"KHMC":"客户名称 ：" + data.KHMC,
						"CCSJ":"出场时间 ：" + data.CCSJ,
						"PSDZ":"配送地址 ：" + data.PSDZ,
						"SFDH": data.SFDH
					});
				});
			}

		}else if(id == $.config.preButtonId + "delete"){
			if(ui.getSelectedRowId().length < 1){
				CFG_message("请至少选择一条记录", "warning");
				return;
			}
			var id = "";
			$.each(ui.getSelectedRowId(), function(e, data){
				id += data + ",";
			});
			$.confirm("所选择的记录将被删除，确定吗？", function(sure) {
            	if (!sure) {
                	return;
            	}
				var dataJson = $.loadJson($.contextPath + "/zzcc!deletePcccxx.json?id=" + id);
				if(dataJson == "1"){
					CFG_message("删除成功", "success");
					ui.reload();
				}else{
					CFG_message("删除失败", "error");
				}
        	});



		}
	}
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	ui.processItems = function (data) {
		var btns = [];
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
		}
		btns.splice(4,0,{
			id: $.config.preButtonId + "delete",
			icon: "icon-delete",
			label: "删除",
			type: "button"
		});
		btns.push({
			id: $.config.preButtonId + "print",
			icon: "icon-print",
			label: "打印随附单",
			type: "button"
		});
		return btns;
	}

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
