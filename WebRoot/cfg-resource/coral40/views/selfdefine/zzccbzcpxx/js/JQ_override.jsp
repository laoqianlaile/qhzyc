<%@page language="java" pageEncoding="UTF-8"%>
<% String id=request.getParameter("id");
    request.setAttribute("ID",id);
    String bzid=request.getParameter("bzid");
    request.setAttribute("BZID",bzid);
%>
<script type="text/javascript">
/***************************************************!
 * @author qiucs 
 * @date   2014-7-15
 * 系统配置平台应用自定义二次开发JS模板 
 ***************************************************/
 
//alert('标签页');
(function(subffix) {

/**
 * 二次开发定位自己controller
 * @returns {String}
 **/
window[CFG_actionName(subffix)] = function (ui) {
	// ui.assembleData 就是 configInfo
	return $.contextPath + "/zzccbzcpxx";
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

    var bzxx;

    <%--if('${BZID}'!=''){bzxx = $.loadJson($.contextPath + '/zzccbzcpxx!getBzxx.json?id='+'${BZID}');--%>
        <%--ui.uiGrid.grid('addRowData',1,bzxx);--%>
    <%--}--%>

    //获取修改传入的方案id
//    var  sasa=CFG_getInputParamValue(configInfo, 'getBzid');
//    var bzid=CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "getBzid");

//    var bzid=null;
    var _bzid = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "BZID");
    var _ccglId = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "CCGLID");
    var gridData=$.loadJson($.contextPath + '/zzccgl!getGridData.json?id='+_bzid);
//    var gridData=$.loadJson($.contextPath + '/zzccgl!getGridData.json?id='+_ccglId);
//    if(gridData!=''){bzid=_ccglId;_ccglId=''};
  

    //是否为详细查看
    var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "ISVIEW");
    var isReadonly = false;
    if(isView == "YES"){
        isReadonly = true;
    }


    //列表过滤条件
//    if(_ccglId!=''){
        ui.options.columns="(EQ_C_PID≡"+_ccglId+")AND(NOT_C_IS_DELETE≡1)";
//    }

    //修改列属性
    ui.beforeInitGrid = function (setting) {
        //设置列表列下拉框数据
//        if(_ccglId!=''){
//
//        }
        var cclIndex = $.inArray("出场件数", setting.colNames);
        setting.colModel[cclIndex].formatter = 'text';
        setting.colModel[cclIndex].formatoptions = {
            'required' : true,
            'validType' : 'float',
            'readonly' : isReadonly,
            'onKeyUp' : function (e,data) {
                var rowData = ui.getRowData(e.data.rowId);
                if(parseFloat(data.value)>parseFloat(rowData.KCJS)){
                    CFG_message("出场件数不得大于库存件数",'warning');
                    $(this).textbox("setValue","");
                    return;
                }
                updateZzl();
            }
        };
        setting.afterInlineSaveRow = function(e,data){
            var rowData = ui.getRowData(data.rowId);
            if(parseFloat(rowData.CCJS)>parseFloat(rowData.KCJS)){
                CFG_message("出场件数不得大于库存件数",'warning');
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
        ui.assembleData.cGridDivId = ui.element.attr("id");

        ui.uiGrid.grid('option','onComplete',function(){
            if(_bzid!=''){ui.addRowData(gridData);}
            var $gridIDs = ui.uiGrid.grid("getDataIDs");
            $.each($gridIDs, function(e, data){
                var $gridData = ui.uiGrid.grid("getRowData", data);
                if($gridData.ID.toString().indexOf('temp_') < 0 && $gridData.CCJS != ""){
                    var newKcjs = $.loadJson($.contextPath + "/zzccbzcpxx!getBzjs.json?cpzsm=" + $gridData.CPZSM);
                    ui.uiGrid.grid("setCell", data, "KCJS", parseFloat(newKcjs) + parseFloat($gridData.CCJS));
                }
            });
        });



    };

    //自定义按钮
    ui.clickSecondDev = function(id){
        if(id == $.config.preButtonId + "inputTraceCode"){
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
            title : "请输入产品追溯码",
            width : 300,
            height : 100,
            resizable : false,
            position : {
                at: "center top+350"
            },
            onClose : function() {
                _this.close(jqUC);
                $("#UNTREAD_OPINION_" + _this.uuid).remove();
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
                            var traceCode=$("#UNTREAD_OPINION_" + _this.uuid).val();
                            addGridData(traceCode);
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

    function addGridData(traceCode){
        var rowData = ui.getRowData();
        for (var i in rowData) {
            if (traceCode == rowData[i].CPZSM) {
                CFG_message("追溯码已录入!","warning");
                return;
            }
        }

        var bzxx = $.loadJson($.contextPath + '/zzccbzcpxx!getBzcpxx.json?traceCode='+traceCode);
        if(null==bzxx.ID||""==bzxx.ID){
            CFG_message("追溯码无效!","error");
            return;
        }
        var tempId = generateId("temp");
        var data = {
            ID:tempId,
            PID:_ccglId,
            BZLSH:bzxx.BZLSH,
            CPZSM:traceCode,
            CPBH:bzxx.CPBH,
            CPMC:bzxx.CPMC,
            CPDJ:bzxx.CPDJ,
            PL:bzxx.CPPL,
            KCJS:bzxx.KCJS,


            //ZL:bzxx.ZL
        };
        ui.addRowData(data);
        updateZzl();
    }
    //-
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
    ui.processItems = function (data) {
        var isView = CFG_getInputParamValue(ui.assembleData.parentConfigInfo.parentConfigInfo, "ISVIEW");
        var btns = [];
        for (var i = 0; i < data.length; i++) {
            btns.push(data[i]);
        }
        if(isView == "NO"){
            btns.push({
                id:$.config.preButtonId + "inputTraceCode",
                label: "输入追溯码",
                type : "button"
            });
        }
        return btns;
    };
}
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
