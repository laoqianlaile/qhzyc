<%@page language="java" pageEncoding="UTF-8"%>
<style>
	.red{background-color: red}
</style>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 var bsGrid
 var bjzd;
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zltpzzlb";
};

//alert('tpzz');
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
	ui.beforeInitGrid = function(setting) {
		setting.fitStyle = "width";
		return setting;
	};

	var grid;
	init=ui._init=function(){

		 grid=ui.uiGrid;
//		ui.uiGrid.grid('option','altRows',true);

		ui.uiGrid.grid({rowattr:function(o){

//			if(bjzd==undefined){
				if(o.rowData.WTBJ==1||o.rowData.CCWTBJ==1){
					return {"style":"background:#F0EFB5"};
				}else{
					return {"style":"background:white"};
				}

		}

		});
//		ui.uiGrid.grid({
//			altRows:true,
//			altClass:"color:red"
//		})
		bsGrid=ui.uiGrid;
	}
    //var data=ui.uiGrid.grid('getRowData');
	//grid.grid('getRowData');
	//alert(ui.getRowData());
//	ui.grid("getRowData");
	ui.addOutputValue('setComboGridValue_lsxzqh',function(o){
		var data=grid.grid('getRowData');


	})
//	setComboGridValue_lsxzqh:function(o){
//		return o;
//	}
    //操作列增加问题标记
	ui.beforeInitGrid = function (setting) {

		//setting.colNames.push("操作");
		//var _formatter =setting.colModel[5].formatter;
//		setting.fitStyle = "width";
		setting.colModel[ui.gcfg.linkIndex].formatter = function(cv, opt, rowData) {
			//var val = _formatter(cv, opt, rowData);
			//return val + "";


			var id = rowData[ui.gcfg.keyColumn],
					cgridDivId = ui.element.attr("id");
			var wtbj=rowData.WTBJ;
			var colid=rowData.ID;
			var ccwtbj=rowData.CCWTBJ;
			var nzwid=rowData.NZWID;
			var pch=rowData.PCH;
			var bzxs=rowData.BZXS;
			var cpzsm=rowData.CPZSM


			var val = ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id);
			var inner;


			if((wtbj=='0')&&(ccwtbj=='0')){val +=
					"<a id='bj' href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').opClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">标记问题</a>";};
			if(wtbj=='1'||ccwtbj=='1')
			{val +=
					"<a id='bj' href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').opClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">取消标记</a>";};

			return val;
		}
		return setting;
	};

	ui.executeIS=function(value){

//		var jsondata= $.loadJson($.contextPath+"/zltpzz!getJdandWd.json?cpzsm="+value);
//		var aa = ui.assembleData;
//		aa.parentConfigInfo.addMarkergrid(jsondata);
//		ui.uiGrid.grid("option", "datatype", "json");
//		ui.uiGrid.grid('option',"url", $.contextPath+"/zltpzz!reloadGrid.json?cpzsm="+value);
//		ui.uiGrid.grid('reload');


		var jsondata= $.loadJson($.contextPath+"/zltpzz!getJdandWd.json?cpzsm="+value);
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(jsondata);

		bsGrid.grid("option", "datatype", "json");
		bsGrid.grid('option',"url", $.contextPath+"/zltpzz!reloadGrid.json?cpzsm="+value);
		bsGrid.grid('reload');

	}
};
/**
 *  二次开发：复写自定义树
 */
function _override_tree (ui) {

};
/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	ui._executeBS=function(){

	}


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



//复写基本检索
function _override_bsearch (ui){
	ui.clickSearch=function(){
		var formData = $("form", ui.panelBS).form("formData", false);
		var value=formData.LIKE_C_CPZSM;
		var jsondata= $.loadJson($.contextPath+"/zltpzz!getJdandWd.json?cpzsm="+value);
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(jsondata);

		bsGrid.grid("option", "datatype", "json");
		bsGrid.grid('option',"url", $.contextPath+"/zltpzz!reloadGrid.json?cpzsm="+value);
		bsGrid.grid('reload');
	}
}
	$.extend($.ns("ns" + subffix), {
		opClick: function (e,pch,bzxs,cpzsm) {
			var value= e.innerHTML;
			var colid=$(e).attr('myAttr1');
			var jsondata= $.loadJson($.contextPath+"/gjzzlb!changeWtbj.json?pch="+pch+"&value="+value+"&bzxs="+bzxs+"&cpzsm="+cpzsm);
			if((jsondata=='1')&&(value=='标记问题')){
				e.innerHTML="取消标记";
				bjzd="取消标记";
				document.getElementById(colid).style.background="#F0EFB5";
				$("#"+colid).style.background="white";
			};
			if((jsondata=='1')&&(value=='取消标记')){e.innerHTML="标记问题";
				bjzd="标记问题";
				document.getElementById(colid).style.background="white";
			}
			bsGrid.grid("option", "datatype", "json");
			bsGrid.grid('option',"url", $.contextPath+"/zltpzz!reloadGridAtferClick.json?E_frame_name=coral&E_model_name=datagrid");
			bsGrid.grid('reload');
		}
	});



/**
 * 在此可以复写所有自定义JS类
 * @param selector
 * @returns {JQ_override}
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
//window[CFG_overrideName(subffix)] = function () {
//
//	//var startTime = new Date().getTime();
//
//	if (this instanceof $.config.cform) {
//		_override_form(this);
//	} else if (this instanceof $.config.cgrid) {
//		_override_grid(this);
//	} else if (this instanceof $.config.ctree) {
//		_override_tree(this);
//	} else if (this instanceof $.config.ctbar) {
//		_override_tbar(this);
//	} else if (this instanceof $.config.clayout) {
//		_override_layout(this);
//	}
////	else if(this instanceof $.config.cbserch){
////		_override_bsearch(this);
////	}
//
//	//console.log("over ride cost time: " + (new Date().getTime() - startTime));
//};



	
	
	
	
})("${timestamp}");
</script>
