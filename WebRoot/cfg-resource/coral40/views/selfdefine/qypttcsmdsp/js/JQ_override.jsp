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
};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	var selecttedRowIds = new Array();
	ui.clickSecondDev = function(id){
		if(id==$.config.preButtonId+"confirm"){
			if (!selecttedRowIds || (selecttedRowIds && selecttedRowIds.length == 0)) {
				CFG_message("请选择记录", "warning");
				return;
			}
			ui.assembleData.CFG_outputParams.rowData = selecttedRowIds;
			CFG_clickCloseButton(ui.assembleData);
		}
	};

	ui.eSelectRow = function (e, data) {
		var selectIds = ui.getSelectedRowId();
		//加入所有当前页的行id
		for(var i = 0;i<selectIds.length;i++){
			if(selecttedRowIds.indexOf(selectIds[i])==-1){
				selecttedRowIds.push(selectIds[i]);
			}
		}
		//主要作用去除被删除的行id
		if(data.status==true){
			if(selecttedRowIds.indexOf(data.rowId)==-1){
				selecttedRowIds.push(data.rowId);
			}
		}else{
			selecttedRowIds.splice($.inArray(data.rowId,selecttedRowIds),1);
		}
		//TO-DO shift选择时存在bug 范围外的行自动未选中
	};

	ui.beforeInitGrid = function(setting) {
        setting.onSelectAll = function(e,data){
            var selectIds = data.aRowIds;
            //加入所有当前页的行id
            for(var i = 0;i<selectIds.length;i++){
                if(selecttedRowIds.indexOf(selectIds[i])==-1){
                    selecttedRowIds.push(selectIds[i]);
                }
            }
            //主要作用去除被删除的行id
            if(data.status==true){
                for(var id in data.aRowIds){
                    if(selecttedRowIds.indexOf(data.aRowIds[id])==-1){
                        selecttedRowIds.push(data.aRowIds[id]);
                    }
                }
            } else {
                for(var id in data.aRowIds){
                    selecttedRowIds.splice($.inArray(data.aRowIds[id],selecttedRowIds),1);
                }
            }
        };
		setting.onComplete = function(e,data){
			for(var i=0;i<selecttedRowIds.length;i++){
				ui.uiGrid.grid("setSelection", selecttedRowIds[i]);
			}
		};
		return setting;
	};


	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
}
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
	if(ui.options.number == 1){
		ui.processItems = function (data) {
			var btns = [];
			btns.push('->',{
				id:$.config.preButtonId + "confirm",
				label: "确认",
				type : "button"
			});
			for (var i = 2; i < data.length; i++) {//跳过'','->'
				btns.push(data[i]);
			}
			return btns;
		};
	}
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
