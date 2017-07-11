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
	return $.contextPath + "/zzdkzztj";
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

    ui._init = function(){
		console.log();
		var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/zzdkzztj!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
        var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/zzdkzztj" + "\" enctype=\"multipart/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
        $(ui.options.global).append(importDiv);
    }


	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};


	ui.addOutputValue("setComboGridValue_Dkbh",function (o){
		var ids = ui.selectedRowId;
		var dkbh = $.loadJson($.contextPath+'/zzdkzztj!getDkbhById.json?id='+ids);
		return {
			status:true,
			P_columns:'EQ_C_DKBH≡'+dkbh
		};
	});
	ui.beforeDelete=function (idArr, isLogicalDelete) {
		var isExistZzdyxx = $.loadJson($.contextPath + "/zzdkxx!isExistZzdyxx.json?ids="+idArr);
		if(isExistZzdyxx>0){
			CFG_message("删除的地块信息中,包含种植单元的信息","error");
			return false;
		}
		return true;
	}

    ui.clickSecondDev = function(id){
        if (id == $.config.preButtonId + "import") {
            $("#imageUpload").click();
        }else if(id == $.config.preButtonId + "export"){
           window.location.href=$.contextPath + "/zzdkzztj!downLoad";
        }else if(id == $.config.preButtonId + "print"){
            if(isSwt || isNewSwt){
                if(ui.getSelectedRowId().length != 1){
                    CFG_message("请选择一条记录", "warning");
                    return;
                }
            }else{
                $.alert("请在程序环境中写卡");
                return;
            }

			var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
			if(isSwt){
				var data = {
					QYBH:rowData.QYBH,
					DKBH:rowData.DKBH
				}
				var result = _window("printSczzDkbq", JSON.stringify(data));
			}else if(isNewSwt){
				var result = _print.print("dkbqPrint",{"QYBHDKBH":rowData.QYBH + "/" + rowData.DKBH,"DKBH":rowData.DKBH});
				if(result.MSG = "SUCCESS")
					$.alert("打印成功");
//				_print.print("template",{"variable":{"QYBH":rowData.QYBH,"DKBH":rowData.DKBH},"url":"http://www.zhuisuyun.net/01234567890123456789","barcode":"12345678900123456789"})
			}
		}
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
	ui.processItems = function (data) {
		var btns = [];
		for (var i = 0; i < data.length; i++) {
			btns.push(data[i]);
		}
		btns.push({
			id: $.config.preButtonId + "import",
			icon: "icon-print",
			label: "导入地块信息",
			type: "button"
		});
		btns.push({
			id: $.config.preButtonId + "export",
			icon: "icon-print",
			label: "导入模板下载",
			type: "button"
		});
		btns.push({
			id: $.config.preButtonId + "print",
			icon: "icon-print",
			label: "打印地块标签",
			type: "button"
		});
		return btns;
	};
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
	 *  二次开发：复写基本查询区
	 */
	function _override_bsearch (ui) {
		// ui.assembleData 就是 configInfo
		//console.log("override search!");
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
		//console.log("override search!");
		//ui.bindEvent = function () {
		// 添加onChange事件
		//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
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
		} else if (this instanceof $.config.cbsearch) {
			_override_bsearch(this);
		} else if (this instanceof $.config.cgsearch) {
			_override_gsearch(this);
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
