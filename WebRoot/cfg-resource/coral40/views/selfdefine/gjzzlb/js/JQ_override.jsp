<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/

var thisGrid;
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/gjzzlb";
};
	var grid;

	function hover(e){
		alert(e.innerHTML);
	}
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
	ui._init=function(){
		thisGrid=ui.uiGrid;
		ui.uiGrid.grid({rowattr:function(o){
//			if(bjzd==undefined){
			if(o.rowData.WTBJ==1||o.rowData.CCWTBJ==1){
				return {"style":"background:#F0EFB5"};
			}else{
				return {"style":"background:white"};
			}
		}

		});
		//列表传值到地图
 		var Mapdata= $.loadJson($.contextPath+"/gjzzlb!SearchKhxx.json");
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(Mapdata); 
		bsGrid=ui.uiGrid;
	}



	ui.beforeInitGrid = function (setting) {
        //修改翻页的默认显示行数
        setting.rowNum = 4;
		//setting.colNames.push("操作");
		//var _formatter =setting.colModel[5].formatter;
		setting.colModel[ui.gcfg.linkIndex].formatter = function(cv, opt, rowData) {

			//var val = _formatter(cv, opt, rowData);
			//return val + "";
			var id = rowData[ui.gcfg.keyColumn],
					cgridDivId = ui.element.attr("id");
//			var wtbj=rowData.WTBJ;
//			var ccwtbj=rowData.CCWTBJ;
//			var nzwid=rowData.NZWID;
			
			var wtbj=rowData.WTBJ;
			var ccwtbj=rowData.CCWTBJ;
			var nzwid=rowData.NZWID;
			var pch=rowData.PCH;
			var bzxs=rowData.BZXS;
			var cpzsm=rowData.CPZSM
			var colid=rowData.ID
			//var inner;
			//预警状态
			var yjzt = rowData.YJZT;
			var id=rowData.ID
			var dh=rowData.DH;
			var data=rowData;
			//var cpzsm=rowData.CPZSM;
			var xsqx=rowData.XSQX;
			var lxr=rowData.LXR;
			var qymc=rowData.QYMC;
			var pch=rowData.PCH;
			//debugger
			//无标记时
			if((wtbj=='0')&&(ccwtbj=='0')){
					//无标记。但有预警
				if((yjzt == '1')){
				return ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id)+
					"<a href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').ooClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">标记问题</a>"+
					"<a href='javascript:void(0);' style = 'color:rgb(51,190,239);' onclick='$.ns(\"ms" + subffix + "\").xqClick(\""+cpzsm+"\")'> 预警详情</a>";
			}
			//无标记  无预警
			else{
				return ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id)+
					"<a href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').ooClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">标记问题</a>";
			}		
			}
			else{
			//有标记时，但无预警
				if (yjzt == '0') {
                return ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id)+
					"<a href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').ooClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">取消标记</a>"+
					"<a href='javascript:void(0);' style = 'color:rgb(51,190,239);' myAttr1="+id+" onclick='$.ns(\"ns" + subffix + "\").opClick(this)'> 发送预警信息</a>";
            	} else {
            	//有标记 ，且有预警
                return ui.gcfg.linkContent.replace(/\{1\}/g, cgridDivId).replace(/\{2\}/g, id)+
					"<a href='javascript:void(0);' myAttr1="+colid+" style = 'color:rgb(51,190,239);'  onclick=\"$.ns('ns" + subffix + "').ooClick(this,'"+pch+"','"+bzxs+"','"+cpzsm+"')\">取消标记</a>"+
					"<a href='javascript:void(0);' myAttr1="+id+" style = 'color:rgb(51,190,239);' onclick='$.ns(\"ns" + subffix + "\").opClick(this)'> 发送预警信息</a>"+
					"<a href='javascript:void(0);' style = 'color:rgb(51,190,239);' onclick='$.ns(\"ms" + subffix + "\").xqClick(\""+cpzsm+"\")'> 预警详情</a>";
            	}	
			};
		}
		return setting;
	};
	//复写一体化检索
	ui.executeIS=function(value){


		var jsondata=$.loadJson($.contextPath+"/gjzzlb!getJdandWdByIs.json?value="+value);

//		var jsondata=[{"JD":121.4,"DZ":"松江","WD":31.2,"MDMC":"一号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"},
//			{"JD":121.1,"WD":31.4,"DZ":"松江","MDMC":"二号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"},
//			{"JD":121.7,"WD":31.8,"DZ":"松江","MDMC":"三号号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"}];
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(jsondata);
		ui.uiGrid.grid("option", "datatype", "json");
		ui.uiGrid.grid('option',"url", $.contextPath+"/gjzzlb!reloadGridIs.json?value="+value);
		ui.uiGrid.grid('reload');
	}
	//复写基本检索
	ui._executeBS=function(){


		var data=this;
		var formData = $("form", ui.panelBS).form("formData", false);

		var zsm=formData.LIKE_C_CPZSM;
		var cppch=formData.LIKE_C_CPPCH;
		var lowcssj=formData.GTE_C_JDRQ;
		var highcssj=formData.LTE_C_JDRQ;
		var csqy=formData.LIKE_C_SCQY;


		var jsondata= $.loadJson($.contextPath+"/gjzzlb!getJdandWd.json?cpzsm="+zsm+"&cppch="+cppch+"&lowcssj="+lowcssj+"&highcssj="+highcssj+"&csqy="+csqy);
//		var jsondata=[{"JD":121.4,"DZ":"松江","WD":31.2,"MDMC":"一号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"},
//			{"JD":121.1,"WD":31.4,"DZ":"松江","MDMC":"二号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"},
//			{"JD":121.7,"WD":31.8,"DZ":"松江","MDMC":"三号号门店","LXR":"小王","LXDH":"110","MDTP":"http://i6.topit.me/6/5d/45/1131907198420455d6o.jpg"}];
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(jsondata);
		ui.uiGrid.grid("option", "datatype", "json");
		ui.uiGrid.grid('option',"url", $.contextPath+"/gjzzlb!reloadGrid.json?cpzsm="+zsm+"&cppch="+cppch+"&lowcssj="+lowcssj+"&highcssj="+highcssj+"&csqy="+csqy);
		ui.uiGrid.grid('reload');
		//var jsondata= $.loadJson($.contextPath+"/gjzzlb!getJdandWd.json?cpzsm="+zsm+"&cppch="+cppch+"&lowcssj="+lowcssj+"&highcssj="+highcssj+"&csqy="+csqy);

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

//	function _override_bsearch (ui){
//		ui.clickSearch=function(){
//			var formData = $("form", ui.panelBS).form("formData", false);
//			var value=formData.LIKE_C_CPZSM;
//			var jsondata= $.loadJson($.contextPath+"/zltpzz!getJdandWd.json?cpzsm="+value);
//			var aa = ui.assembleData;
//			aa.parentConfigInfo.addMarkergrid(jsondata);
//
//			bsGrid.grid("option", "datatype", "json");
//			bsGrid.grid('option',"url", $.contextPath+"/zltpzz!reloadGrid.json?cpzsm="+value);
//			bsGrid.grid('reload');
//		}
//	}

function _override_bsearch (ui){
	ui.clickSearch=function(){

		var data=this;
		var formData = $("form", ui.panelBS).form("formData", false);
		var zsm=formData.EQ_C_CPZSM;
		var pch=formData.EQ_C_PCH;
		var qymc='';
		var khmc=formData.LIKE_C_KHMC;
		var cpmc=formData.LIKE_C_CPMC;
		var aftdate=formData.LTE_C_CCSJ;
		var befdate=formData.GTE_C_CCSJ;

		var Mapdata= $.loadJson($.contextPath+"/gjzzlb!SearchXsqx.json?zsm="+zsm+"&pch="+pch+"&qymc="+qymc+"&khmc="+khmc+"&cpmc="+cpmc+"&aftdate="+aftdate+"&befdate="+befdate);
		var aa = ui.assembleData;
		aa.parentConfigInfo.addMarkergrid(Mapdata);
		thisGrid.grid("option", "datatype", "json");
		thisGrid.grid('option','url',$.contextPath+"/gjzzlb!BaseSearch.json?E_frame_name=coral&E_model_name=datagrid&zsm="+zsm+"&pch="+pch+"&qymc="+qymc+"&khmc="+khmc+"&cpmc="+cpmc+"&aftdate="+aftdate+"&befdate="+befdate);
		thisGrid.grid('reload');


	}
}

	$.extend($.ns("ns" + subffix), {
		ooClick: function (e,pch,bzxs,cpzsm) {
			var value= e.innerHTML;
			var colid=$(e).attr('myAttr1');
			var jsondata= $.loadJson($.contextPath+"/gjzzlb!changeWtbj.json?pch="+pch+"&value="+value+"&bzxs="+bzxs+"&cpzsm="+cpzsm);
			if((jsondata=='1')&&(value=='标记问题')){
				e.innerHTML="取消标记";
				document.getElementById(colid).style.background="#F0EFB5";
			};
			if((jsondata=='1')&&(value=='取消标记')){e.innerHTML="标记问题";
				document.getElementById(colid).style.background="white";
			}
			thisGrid.grid("option", "datatype", "json");
			thisGrid.grid('option','url',$.contextPath+"/gjzzlb!BaseSearch.json?E_frame_name=coral&E_model_name=datagrid&zsm="+''+"&pch="+''+"&qymc="+''+"&khmc="+''+"&cpmc="+''+"&aftdate="+''+"&befdate="+'');
			thisGrid.grid('reload');		
		}
	});

/***
 * 当前全局函数实现位置
 * 如果有需要的，可打开以下实现体
 * 使用方法： 与开发构件一致
 **/
$.extend($.ns("ns" + subffix), {
    opClick: function (e) {
    //预警信息发送状态提示
		var id=$(e).attr('myAttr1');
		var Mapdata= $.loadJson($.contextPath+"/zlaqyj!sendoneMessage.json?id="+id);
		if(Mapdata!=null){
			alert("预警信息已发送成功");
			thisGrid.grid("option", "datatype", "json");
			thisGrid.grid('option','url',$.contextPath+"/gjzzlb!BaseSearch.json?E_frame_name=coral&E_model_name=datagrid&zsm="+''+"&pch="+''+"&qymc="+''+"&khmc="+''+"&cpmc="+''+"&aftdate="+''+"&befdate="+'');
			thisGrid.grid('reload');
		}else{
			alert("业务繁忙，请重试")
		}
    }

});
/**
*预警详情弹出框
*/
 $.extend($.ns("ms" + subffix), {
    xqClick: function (cpzsm) {
    //debugger	
    //jqGlobal = $(ui.options.global);	            
		$("body").append("<div id='yjxqq${idSuffix}'><div id='yjxqGrid${idSuffix}' ><div></div></div></div>");
        
         $("#yjxqq${idSuffix}").dialog({
                modal: true,
                width: 550,
                height: "auto",
                title: "预警详情",
                minHeight:400,
                reloadOnOpen:true,
                onOpen: function () {
                	var $grid = $("#yjxqGrid${idSuffix}"),
                    _colModel = [
                        {name: "LXR", sortable: true, width: 200,align : "center"},
                        {name: "YJSJ", sortable: true, formatter: "date", width: 200, fixed: true,align : "center"}
                    ],
                    _colNames = ["被预警人", "预警时间"],
                    _setting = {
                        width: "550",
                        height: "600",
                        colModel: _colModel,
                        colNames: _colNames,
                        url: $.contextPath+"/zlaqyj!viewEarlyWarning.json?CPZSM="+cpzsm
                    };
         			$grid.grid(_setting);
/*                     var gridData = $.loadJson($.contextPath+"/zlaqyj!viewEarlyWarning.json?id="+id);
                    $.each(gridData,function(index,data){
                        var tempid = generateId("temp");
                    	$grid.grid("addRowData",tempid,{LXR:data.LXR,YJSJ:data.YJSJ},'last');
                    }); */
                    /*
                    $("#qsnsxdialogDate${idSuffix}").datepicker({
                        required: true
                    });
                    $("#dialogConfirm${idSuffix}").button({
                        label: "确定",
                        width: 80,
                        onClick: function () {
                            if (!$("#qsnsxdialogDate${idSuffix}").datepicker("valid")) {
                                CFG_message("请先填写时间", "warning");
                                return;
                            }
                            $.ns('namespaceId${idSuffix}').clearFirst();
                            $("#" + gridId).grid("setRowData", rowId, {
                                QSNSX: "1"
                            });
                            //TODO 高亮行
                            var $grids = $("#scbzGrid${idSuffix},#scggGrid${idSuffix},#scsfGrid${idSuffix},#scyyGrid${idSuffix},#sccsGrid${idSuffix},#scccGrid${idSuffix},#scqtGrid${idSuffix}");
                            $grids.grid("option", "rowattr", $.ns('namespaceId${idSuffix}').rowAttr);
                            //设置toolbar文本框
                            $.ns('namespaceId${idSuffix}').getToolBarQsnsxsjComponent(gridId).textbox("setValue", $("#qsnsxdialogDate${idSuffix}").datepicker("getValue"));
                            $("#yjxqq${idSuffix}").remove();
                        }
                    });
                    $("#dialogCancel${idSuffix}").button({
                        label: "取消",
                        width: 80,
                        onClick: function () {
                            $("#yjxqq${idSuffix}").remove();
                        }
                    });
                    */
                },
                onClose: function () {
                    $("#yjxqq${idSuffix}").remove();
                } 
           });
          //$("#yjxqq${idSuffix}").dialog("open"); 		
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
	
	
	
	
})("${timestamp}");
</script>
