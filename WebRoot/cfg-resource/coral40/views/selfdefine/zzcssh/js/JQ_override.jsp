<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
 
(function(subffix) {
    //定义一个全局变量，存放入参ID
	var aa;
	//定义一个全局变量存放总重量dom节点
    var zzl;
	//定义一个全局变量存放主表中重量
	var formzl;
	//定义一个全局变量存放主表id
	var formid;
	//定义一个全局变量存放列表条数
	var gridrownum=0;
	//定义一个全局变量存放每次修改的rowid
	var rownumber;
	//定义一个全局变量保存列表数据条数
	var len;
	//定义一个全局变量保存grid节点
	var $grid;



/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zzcsgl";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {



	//页面初始化加载列表的数据
	aa = ui.options.dataId;
	ui.options.dataId='';

	ui._init = function (){
		//var aa = ui.options.dataId;
        zzl = ui.getItemJQ("ZZL");
		formzl=zzl.textbox("getValue");

	}


    //复写保存方法
	ui.clickSaveAll=function(){
		//alert($("form", ui.uiForm).form("valid"));
      //判断表单是否填写完整
		//if(ui.getItemJQ("ID").textbox("getValue")!=''){CFG_message("出场不能更改！", "error");return;};
		if(!$("form", ui.uiForm).form("valid")){CFG_message("请将信息填写完整！", "error");return;}
		var flag=0;
		for(var i=0;i<len;i++){
			if(parseInt($grid.grid("getRowData", "tem_"+i).CCZL)>parseInt($grid.grid("getRowData", "tem_"+i).KCZL)){flag++;}
		}
		if(flag>0){CFG_message("出场重量不能大于库存重量！", "error");return;}
		var dGrid=ui.getDetailGrid();
		var jqForm  = $("form", ui.uiForm),
				rowData = dGrid.toFormData(),
				formData, url;

		// 获取表单数据
		var jqGrid=$("grid", ui.uiGrid);
		var gridData=jqGrid.grid("gridData",false);
		formData = jqForm.form("formData", false);
		//存form中的数据
		$.ajax({
			url:$.contextPath+"/zzcsgl!saveForm.json",
			type:"post",
			data:formData,
			dataType:"json",
			success:function(res){
				for(var k=0;k<len;k++){
					$grid.grid("setCell", "tem_"+k,'PID',res.ID);
				}
				//$grid.grid("setCell", lastsel,'PID',aa);
				var cczzl=0;
				//将出场保存到数据库
				for(var m=0;m<len;m++){
					var rowData=$grid.grid("getRowData", "tem_"+m);
					if(rowData.CCZL==''){break;};
					$grid.grid("setCell", "tem_"+m,'KCZL',rowData.KCZL-rowData.CCZL);
					rowData=$grid.grid("getRowData", "tem_"+m);
					cczzl=parseInt(rowData.CCZL)+cczzl;
					$.ajax({
						url:$.contextPath+"/zzcsgl!saveGrid.json?ids="+aa,
						type:"post",
						data:rowData,
						dataType:"json",
						success:function(res){
						},
						error:function(){
							CFG_message("保存失败！", "error");
						},
					});
					//同步到采收农作物详情表中
//					$.ajax({
//						url:$.contextPath+"/zzcsgl!editCsnzwxq.json?id="+res.ID+"&cczl="+rowData.CCZL+"&pch="+rowData.PCH,
//						type:"post",
//						dataType:"json",
//						success:function(res){
//							//CFG_message("保存成功！", "success");
//						},
//						error:function(){
//							CFG_message("保存失败！", "error");
//						}
//					});
					//$grid.grid("setCell", "tem_"+m,'KCZL',rowData.KCZL-rowData.CCZL);
				}
				zzl.textbox("setValue",cczzl);

				//将出场重量同步到出场管理和采收管理两种表中

			$.ajax({
				url:$.contextPath+"/zzcsgl!editCcglandCsgl.json?id="+res.ID+"&zzl="+cczzl+"&csid="+aa+"&cczl="+cczzl,
				type:"post",
				dataType:"json",
				success:function(res){
					//CFG_message("保存成功！", "success");
				},
				error:function(){
					CFG_message("保存失败！", "error");
				}
			});

                CFG_message("保存成功！", "success");
				ui.getItemJQ("CCLSH").textbox("setValue",res.CCLSH);
				ui.getItemJQ("ID").textbox("setValue",res.ID);
				formid=res.ID;


			},
			error:function(){
                CFG_message("保存失败！", "error");
			}
		});
	}

    //绑定事件
    ui.bindEvent = function() {
        var khbh = ui.getItemJQ("KHBH");
        var xsddh = ui.getItemJQ("XSDDH");
        //客户编号下拉列表onchange事件
        khbh.combogrid("option","onChange",function(e,data){
            xsddh.combogrid("reload",'tcszzddxx!getDdxxByKhbh.json?khbh=' + data.value);
            ui.setFormData({
                KHBH:data.value,
                KHMC:data.text,
                XSDDH:""
            });
        });
    };

    //客户信息弹出框回调
    ui.addCallback("setComboGridValue_Khxx",function(o){
        if (null == o) return;
        var rowData = o.result;
        if (null == rowData) return;
        var xsddh = ui.getItemJQ("XSDDH");
        xsddh.combogrid("reload",'tcszzddxx!getDdxxByKhbh.json?khbh=' + data.value);
        ui.setFormData({
            KHBH:rowData.KHBH,
            KHMC:rowData.KHMC,
            XSDDH:""
        });
    });

    //订单信息弹出框回调
    ui.addCallback("setComboGridValue_Ddxx",function(o){
        if (null == o) return;
        var rowData = o.result;
        if (null == rowData) return;
        ui.setFormData({
            XSDDH:rowData.DDBH
        });
    });

    //订单信息弹出框过滤
    ui.addOutputValue("setTcsDdxx",function(o){
        var khbh = ui.getItemValue("KHBH");
        var o = {
            status : true,
            P_columns : "EQ_C_KHBH≡"+khbh
        };
        return o;
    });

};
/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    //将列表的总重量设置为可编辑状态
	ui.beforeInitGrid = function (setting) {
		//设置列表列下拉框数据
		//setting.editableRows = true;
        //setting.afterInlineSaveRow = afterInlineSaveRow;
		var zpfsIndex = $.inArray("出场重量", setting.colNames);
		setting.colModel[zpfsIndex].editable = true;
		setting.colModel[zpfsIndex].formatter = 'text';//栽培方式
		setting.colModel[zpfsIndex].formatoptions = {
			'required': false
		};
		return setting;
	};
    //列表初始化
	ui._init = function (){
		$grid=ui.uiGrid;
		var formmap = $.loadJson($.contextPath + '/zzcsgl!getShccCslsh.json?id='+aa);
		len=formmap.length;
		for(var k=0;k<len;k++){
				lastsel = "tem_"+k;
				ui.uiGrid.grid("addRowData", lastsel, formmap[k], "last");
		}

	}
	ui.getDataType = function() {
		return "local";
	};
	$(".coral-panel").css({"top":'300px'});
   // ui.
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
