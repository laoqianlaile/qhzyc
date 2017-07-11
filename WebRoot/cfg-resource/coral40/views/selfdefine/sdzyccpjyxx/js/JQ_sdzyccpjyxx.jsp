<%@page language="java" pageEncoding="UTF-8"%>

<script type="text/javascript">
/***************************************************!
 * @date   2016-04-15 10:15:10
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
	return jQuery.contextPath + "/sdzyccpjyxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	ui.clickSecondDev = function(id){
		if (id == $.config.preButtonId + "print") {
			if(true){
				if(ui.getSelectedRowId().length != 1){
					CFG_message("请选择一条记录", "warning");
					return;
				}
			}else{
				$.alert("请在程序环境中写卡");
				return;
			}
			var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());

			var printSc = true ;
			//showPrintDialog();
			//$.alert(rowData.QYXSDDH);
			$.alert("<span style='font-size:20px;color:red'> 正在打印请稍后...</span>");

				try{
					var result = _print.print("xsddhlPrint",{
						"CPMC":"销售订单号 ："+rowData.QYXSDDH,
						"CPDJ":"产品单价 ：",
						"BZRQ":"交易日期 ：" +rowData.JYSJ,
						"ZL"  :"重量 ：",
						"CPZSM":"产品追溯码：",
						"URL":rowData.QYXSDDH
					});
					var resultData = JSON.parse(result);
					if(resultData.MSG != "SUCCESS"){
						$.alert("打印失败");
						jqUC.dialog("close");
						return;
					}
				}catch(e){
				}
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
			if(i == 4){
				btns.push({
					id:$.config.preButtonId + "print",
					icon:"icon-print",
					label: "打印",
					type : "button"
				});
			}
		}
		return btns;
	};
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
