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
	//return $.contextPath + "/appmanage/show-module";
    return $.contextPath + "/zzccshxx";
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
    var shgrid;

    //获取分拣传入的ID
    var _fjglid=CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "FJGLID");



    //获取修改传入的方案id
    var _ccglId = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "CCGLID");
    //是否为详细查看
    var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "ISVIEW");
    var isReadOnly = false;
    if(isView == "YES")
    {
        isReadOnly = true;
    }
    //列表过滤条件
    ui.options.columns="(EQ_C_PID≡"+_ccglId+")AND(NOT_C_IS_DELETE≡1)";
    //修改列属性
    ui.beforeInitGrid = function (setting) {
        //设置列表列下拉框数据
        var cclIndex = $.inArray("出场重量", setting.colNames);
        setting.colModel[cclIndex].formatter = 'text';
        setting.colModel[cclIndex].formatoptions = {
            'required' : true,
            'validType' : 'float',
            'readonly' : isReadOnly,
            'onKeyUp' : function (e,data) {
                var rowData = ui.getRowData(e.data.rowId);
                if (parseFloat(rowData.CCZL)<=0) {
                    CFG_message("出场重量无效",'warning');
                    $(this).textbox("setValue","");
//                    ui.uiGrid.grid('editRow', rowData.ID);
                    return;
                }
                if(parseFloat(data.value)>parseFloat(rowData.KCZL)){
                    CFG_message("出场重量不得大于库存重量",'warning');
                    $(this).textbox("setValue","");
//                    ui.uiGrid.grid('editRow', rowData.ID);
                    return;
                }
                updateZzl();
            }
        };
        setting.afterInlineSaveRow = function(e,data){
            var rowData = ui.getRowData(data.rowId);
            if (parseFloat(rowData.CCZL)<=0) {
                CFG_message("出场重量无效",'warning');
                $(this).textbox("setValue","");
                ui.uiGrid.grid('editRow', rowData.ID);
                return;
            }
            if(parseFloat(rowData.CCZL)>parseFloat(rowData.KCZL)){
                CFG_message("出场重量不得大于库存重量",'warning');
                $(this).textbox("setValue","");
                ui.uiGrid.grid('editRow', rowData.ID);
                return;
            }
            updateZzl();
        };
        if(isView == "YES"){
            var delIndex = $.inArray("操作", setting.colNames);
            setting.colNames.splice(delIndex,1);
            setting.colModel.splice(delIndex,1);
        }
        return setting;
    };
    ui._init = function () {
         shgrid=ui.uiGrid;

        ui.assembleData.cGridDivId = ui.element.attr("id");
//        var shgriddata= $.loadJson($.contextPath + "/zzccshxx!getShccByFj.json?csid=" + _fjglid);
        if(_fjglid!=''){

            var flag=0;
            shgrid.grid('option','onComplete',function(){
                if(flag>0){
                    return false;
                }
                shgrid.grid("option", "datatype", "json");
                shgrid.grid("option", "url", $.contextPath + "/zzccshxx!getShccByFj.json?csid=" + _fjglid);
                shgrid.grid("reload");
                flag++;
                return;

            });
        }else{
            shgrid.grid("option", "onComplete", function(){
                var $gridIDs = shgrid.grid("getDataIDs");
                $.each($gridIDs, function(e, data){
                    var $gridData = shgrid.grid("getRowData", data);
                    if($gridData.ID.toString().indexOf('temp_') < 0){
                        var newKczl = $.loadJson($.contextPath + "/zzccshxx!getShccKczl.json?pch=" + $gridData.PCH + "&cczl=" + $gridData.CCZL);
                        shgrid.grid("setCell", data, "KCZL", newKczl);
                    }
                });
            });
        }


    };


    // 前台删除（不走后台）
//    ui.linkDeleteGD = function () {
//        var idArr = this.uiGrid.grid("option", "selarrrow"),
//                _this = this;
//        if (0 === idArr.length) {
//            CFG_message( "请选择记录！", "warning");
//            return;
//        }
//        $.confirm("所选择的记录将被删除，确定吗？", function(sure) {
//            if (!sure) {
//                return;
//            }
//
//            for (var i = idArr.length; i > -1; i--) {
//                _this.uiGrid.grid("delRowData", idArr[i]);
//            }
//            CFG_message("操作成功!", "success");
//        });
//    }


    //自定义按钮
    ui.clickSecondDev = function(id){
        if(id == $.config.preButtonId + "inputBatchCode"){
            showDialog();
        }
    };

    function showDialog(){
        var _this = ui;
        var jqGlobal = $(ui.assembleData.parentConfigInfo.parentConfigInfo.parentConfigInfo.dialogParent);
        var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
        jqUC.dialog({
            appendTo : jqGlobal,
            modal : true,
            title : "请输入采收批次号",
            width : 300,
            height : 100,
            resizable : false,
            position : {
                at: "center top+350"
            },
            onClose : function() {
                jqUC.remove();
            },
            onCreate : function() {
                var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
                var jq = $("<input id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></input>").appendTo(jqDiv);
                jq.textbox({width: 200});
                jq.textbox("option","onKeyDown",function( e,data){
                    if(e.keyCode =='13'){//添加回车事件
                        $('#sure').trigger("click");
                    }
                });
            },
            onOpen : function() {
                _this.close(false);
                var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                        jqDiv   = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                jqDiv.toolbar({
                    data: ["->", {id:"sure", label:"确定", type:"button"}, {id:"cancel", label:"取消", type:"button"}],
                    onClick: function(e, ui) {
                        if ("sure" === ui.id) {
                            var batchCode=$("#UNTREAD_OPINION_" + _this.uuid).val();
                            addGridData(batchCode);
                            _this.close(jqUC);
                            $("#UNTREAD_OPINION_" + _this.uuid).remove();
                        } else {
                            _this.close(jqUC);
                            $("#UNTREAD_OPINION_" + _this.uuid).remove();
                        }
                    }
                });
            }
        });
    }

    function addGridData(batchCode){
        var pcxx = $.loadJson($.contextPath + '/zzccshxx!getCspcxx.json?batchcode='+batchCode);
        if (null == pcxx.ID||"" == pcxx.ID) {
            CFG_message("采收批次号无效!","error");
            return;
        }
        var griddata = ui.getRowData();
        for(var i in griddata){
            if(griddata[i].PCH == pcxx.PCH){
                CFG_message("相同批次号已存在！","warning");
                return;
            }
        }
        var tempId = generateId("temp");
        var data = {
            ID:tempId,
            PID:_ccglId,
            PCH:pcxx.PCH,
            PZ:pcxx.PZ,
            PZBH:pcxx.PZBH,
            CSZZL:pcxx.ZL,
            KCZL:pcxx.KCZL
//            CPZSM:cpzsm
        };
        ui.addRowData(data);
    }
    //更新主表总重量字段
    function updateZzl(){
        var gridConfigInfos = ui.assembleData.parentConfigInfo.parentConfigInfo.parentConfigInfo.bottomChildConfigInfo.childConfigInfos;
        //主表表单id
        var formId = ui.assembleData.parentConfigInfo.parentConfigInfo.parentConfigInfo.topChildConfigInfo.cFormDivId;
        //主表表单对象
        var jqForm = $("#" + formId);
        var zzl = 0;
        for(var i in gridConfigInfos){
            var jqGrid = $("#" + gridConfigInfos[i].cGridDivId);
            var rowDatas = jqGrid.cgrid("getRowData");
            for (var j in rowDatas) {
                var rowData = rowDatas[j];
                if (rowData.hasOwnProperty("CCZL")) {
                    if (!$.isNumeric(rowData.CCZL)) rowData.CCZL = 0;
                    zzl += parseFloat(rowData.CCZL);
                }
                if (rowData.hasOwnProperty("ZL")) {
                    if (!$.isNumeric(rowData.ZL)) rowData.ZL = 0;
                    zzl += parseFloat(rowData.ZL);
                }
            }
        }
        jqForm.cform("setFormData",{ZZL:zzl});
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
        var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "ISVIEW");
        var btns = [];
        for (var i = 0; i < data.length; i++) {
            btns.push(data[i]);
        }
        if(isView == "NO"){
            btns.push({
                id:$.config.preButtonId + "inputBatchCode",
                label: "输入采收批次号",
                type : "button"
            });
        }
        return btns;
    };
};
/**
 *  二次开发：复写自定义布局
 */
function _override_layout (ui) {
//    ui._init=function(){
//        var tabs=document.getElementsByClassName('a');
//    }
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
