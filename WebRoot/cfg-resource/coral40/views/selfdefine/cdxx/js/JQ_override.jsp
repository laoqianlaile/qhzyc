<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
// alert('candi')
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function () {
	// this.assembleData 就是 configInfo
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
	//腹泻一体化检索
	ui.executeIS=function(value){
//		var jsondata=$.loadJson($.contextPath+"/cdxx!searchCandi.json?value="+value);
		var val=value;
		ui.uiGrid.grid("option", "datatype", "json");
		ui.uiGrid.grid('option','url',$.contextPath+"/cdxx!searchCandi.json?E_frame_name=coral&E_model_name=datagrid&value="+val);
		ui.uiGrid.grid('reload');
//		ui.uiGrid.grid({data:jsondata});
//		aa.parentConfigInfo.addMarkergrid(jsondata);
//		ui.uiGrid.grid("option", "datatype", "json");
//		ui.uiGrid.grid('option',"url", jsondata);
//		ui.uiGrid.grid('reload');
//		ui.uiGrid.grid('rowNumMax',10);

//		ui.uiGrid.grid('clearGridData');
//		for(var i=0;i<jsondata.length;i++){
//			var id=jsondata[i].ID;
////			var griddata=ui.uiGrid.grid('getRowData');
////			var flag=true;
////			for(var k=0;k<griddata.length;k++){
////				if(griddata[k].ID==id){flag=false;break;}
////			}
//
//				ui.uiGrid.grid('addRowData',id,jsondata[i]);
//
//		}

	}
	// ui.assembleData 就是 configInfo
	//console.log("override grid!");
	//ui.getAction = function () {
	//	return $.contextPath + "/appmanage/show-module";
	//};
	ui.eSelectRow = function(e,u){
		var idArr  = this.getSelectedRowId(),jqUI=null;
		if(idArr.length==0||idArr.length>1){
			this.selectedRowId = null;
			$.message({message:"请选择一条记录!",cls:"warning"});
			return;
		}
		var rowData = this.getRowData(idArr[0]);
		this.selectedRowId = idArr[0];
		ui.assembleData.CFG_outputParams.cdxx = rowData;
		CFG_clickCloseButton(ui.assembleData);
	}
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
