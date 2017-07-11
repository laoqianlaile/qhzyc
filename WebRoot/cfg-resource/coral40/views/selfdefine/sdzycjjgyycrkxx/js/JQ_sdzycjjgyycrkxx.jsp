<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>

<script type="text/javascript">
/***************************************************!
 * @date   2016-04-15 09:38:21
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
	return jQuery.contextPath + "/sdzycjjgyycrkxx";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
    //初始化默认添加企业编码
    ui._init = function(){
        hUI = ui;
        //初始化图片删除区域
        var cdzmtpDiv = ui.getItemBorderJQ("cdzmtp");
        cdzmtpDiv.empty();
       // var qybm = ui.getItemJQ("QYBM");
        var qybm="<%=companyCode%>";
        //获取企业名称
        var jsondata = $.loadJson($.contextPath +"/sdzyccjgylrkxx!searchQybm.json?qybm="+qybm+"&dwlx=CJGQY");
        var qymc=jsondata.data[0].QYMC;
        console.log(qymc);
        ui.getItemJQ("GYS").combogrid("destroy");
        ui.getItemJQ("GYS").textbox();
        ui.getItemJQ("GYS").textbox("setValue",qymc);

        var ycmc =  ui.getItemJQ("YCMC");
        ui.getItemJQ("QYBM").textbox("setValue","<%=companyCode%>");
        ui.getItemJQ("FZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");
        if((!isEmpty(ui.options.dataId))||ui.options.isView){//判断为修改,详细
            var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
                    "&P_componentVersionId=" + ui.options.componentVersionId +
                    "&P_menuId=" + ui.options.menuId +
                    "&P_workflowId=" + null2empty(ui.options.workflowId) +
                    "&P_processInstanceId=" + ui._getProccessInstanceId() +
                    "&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode");
            var formData = $.loadJson(url);
            $("form", ui.uiForm).form("loadData",formData);
            var jsondata = $.loadJson($.contextPath +"/sdzyccjgcjxx!searchcdzmxxData.json?sctpmc="+formData.CDZM);
            if(ui.options.isView){
                ui.getItemBorderJQ("CDZM").remove();
            }
            //判断包装图片是否存在
            if(!isEmpty(jsondata.data[0])){
                cdzmtpDiv.html(initTPdiv(jsondata.data[0].TPBCMC));
            }
            ycmc.combogrid("destroy");
            ycmc.textbox();
            ycmc.textbox("setValue",formData.YCMC);
            var cgdh = formData.CGDH;
            if( isEmpty(cgdh)){
                ui.getItemJQ("CGDH").combobox({disabled:true});
               /* ui.getItemJQ("PCH").combogrid("setValue",formData.PCH);
                ui.getItemJQ("PCH").combogrid("setText",formData.QYPCH);
                //ui.getItemJQ("PCH").hide();
               $("#PCH_"+ui.timestamp+"_DIV span").hide();
              //  ui.getItemJQ("PCH").combogrid("option","readonly","true");
                ui.getItemBorderJQ("PCH").append("<input name='QYPCH' class='coralui-textbox' id='QYPCH'>");
                $("#QYPCH").textbox();
                $("#QYPCH").textbox("setValue",formData.QYPCH);
                ui.getItemJQ("QYPCH").textbox("setValue","");*/
            }
        }
    }
    ui.bindEvent  = function(){
        //产地证明下拉框
        var cdzm = ui.getItemJQ("CDZM");
        var rowData = "";
        cdzm.combogrid("option", "onChange", function (e, data) {
            rowData = cdzm.combogrid("grid").grid("getRowData", data.value);
            var tpmc = rowData.TPBCMC;
            ui.getItemBorderJQ("cdzmtp").html(initTPdiv(tpmc));
        });
        //批次号下拉框联动数据
        var cgdh = ui.getItemJQ("CGDH");
        cgdh.combobox("option","onChange",function(e,data){
            var jsonData = $.loadJson($.contextPath +"/sdzyccjgycrkxx!searchPchGridData.json?cgdh="+data.value);
            ui.getItemJQ("QYPCH").combogrid("reload",jsonData);
            //ui.getItemJQ("PCH").combogrid("reload",jsonData);
            ui.getItemJQ("CSPCH").textbox("setValue", jsonData[0].CSPCH);
            ui.getItemJQ("YPTZSM").textbox("setValue",jsonData[0].YPTZSM);
        });

        ui.setItemOption("QYPCH","onChange", function ( e, data){
            var rowData =  ui.getItemJQ("QYPCH").combogrid("grid").grid("getRowData",data.newValue);
            ui.setFormData({YCMC:rowData.YCMC,LX:rowData.LYZT,CD:rowData.CDMC,PCH:rowData.PCH,RKZL:rowData.JYZL,YCDM:rowData.YCDM,QYPCH:rowData.QYPCH});
        });
        //入库重量数值检验
        ui.getItemJQ("RKZL").textbox("option","onChange",function(e,data){
            var pch = ui.getItemValue("PCH");
            var jsonData =  $.loadJson($.contextPath +"/sdzycjjgyycrkxx!searchDataByPch.json?pch="+pch);
            var rkzl = data.value;
            if(rkzl>jsonData.data[0].RKZL){
                CFG_message("所填重量超出库存"+jsonData.data[0].RKZL+"，请重新输入", "warning");
                ui.getItemJQ("RKZL").val("");//入库重量数据清空
            }
        });

        ui.getItemJQ("GYS").combogrid("option", "onChange", function (e, data){
            var gys_value = data.value;
            var gys_data = $.loadJson($.contextPath +"/sdzycgysxx!searchYcgysxxByGysbh.json?gysbh="+gys_value);
            ui.getItemJQ("CDZM").combogrid("reload", gys_data);
            var cdzm = ui.getItemJQ("CDZM");
            cdzm.combogrid("option", "onChange", function (e, data) {
                var rowData = cdzm.combogrid("grid").grid("getRowData", data.value);
                var tpmc = rowData.TPBCMC;
                ui.getItemBorderJQ("cdzmtp").html(initTPdiv(tpmc));
            });
        });

    }

    /*负责人的回调函数*/
    ui.addCallback("setComboGridValue_fzr",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({FZR:obj.XM});
    });
    /*批次号的回调函数*/
    ui.addCallback("setComboGridValue_pch",function(o){
        if(null == o){
            return;
        }
        var obj = o.rowData;
        if(null == obj) return;
        ui.setFormData({YCMC:obj.YCMC,LX:obj.LYZT,CD:obj.CDMC,PCH:obj.PCH});
    });



    ui.addOutputValue("setCombogridValue_fzr", function (o){

        return {
            status : true ,
            P_columns: 'EQ_C_DWLX≡JJGQY'
        }
    });
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
    ui.clickDelete = function (isLogicalDelete) {
        var idArr = ui.uiGrid.grid("option", "selarrrow");
        if (0 == idArr.length) {
            CFG_message( "请选择记录!", "warning");
            return;
        }
        for(var i in idArr) {
            var rowData = ui.uiGrid.grid("getRowData",idArr[i]);
            var url = $.contextPath + "/sdzycscllxx!queryPch.json?pch="+rowData.PCH;
            var ssqy = $.loadJson(url);
            if(isEmpty(ssqy.data[0])){
                ui.databaseDelete(idArr, isLogicalDelete);
            }else{
                $.alert("不能删除!已生产领料");
                return;
            }
        }
    }
    ui._init = function(){
        console.log();
        var onChangeFunction = "if (window.FileReader) {var file = this.files; var f = file[file.length-1]; var ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);if(ImgSuffix != 'xls'){CFG_message('导入文件格式不正确', 'error');return false;} var formData = new FormData(document.forms.namedItem('fileinfo'));$.ajax({type: 'POST',url: '" + $.contextPath + "/sdzycjjgyycrkxx!ImportXls.json',data: formData,contentType: false,processData: false,timestamp: false,async: false,success: function (data) {$('#imageUpload').val('');if(data.RESULT == 'SUCCESS'){CFG_message(data.MSG, 'success');}else if(data.RESULT == 'ERROR'){CFG_message(data.MSG, 'error');}$('#" + ui.uiGrid.attr("id") + "').grid('reload');},error: function () {$('#imageUpload').val('');CFG_message('操作失败！', 'error');}});}";
        var importDiv = "<div id=\"importDiv\" style=\"display:none\"><form id=\"importDiv\" name=\"fileinfo\" action=\"" + $.contextPath + "/sdzycjjgyycrkxx" + "\" enctype=\"multipart	/form-data\" method=\"post\"><input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload\" lable=\"预览\" accept=\"application/vnd.ms-excel\" name=\"imageUpload\" onchange=\"" + onChangeFunction + "\"/></form></div>";
        $(ui.options.global).append(importDiv);
    }
    ui.beforeInitGrid = function(setting) {
        setting.fitStyle = "width";
        return setting;
    };

    ui.clickSecondDev = function(id){
        if (id == $.config.preButtonId + "import") {
            $("#imageUpload").click();
        }else if(id == $.config.preButtonId + "export"){
            window.location.href=$.contextPath + "/sdzycjjgyycrkxx!downLoad";
            /*}else if(id == $.config.preButtonId + "print"){*/
            /*if(isSwt || isNewSwt){
             if(ui.getSelectedRowId().length != 1){
             CFG_message("请选择一条记录", "warning");
             return;
             }
             /!*}else{
             $.alert("请在程序环境中写卡");
             return;
             }*!/

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
             }*/
        }
    };
    // ui.assembleData 就是 configInfo
    //ui.beforeInitGrid = function (setting) {
    //	return setting;
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
        if(ui.options.type  == 1){  //grid(type =1 ) form(type =2 )
            btns.push({
                id: $.config.preButtonId + "import",
                icon: "icon-print",
                label: "导入原料信息",
                type: "button"
            });
            btns.push({
                id: $.config.preButtonId + "export",
                icon: "icon-print",
                label: "模板下载",
                type: "button"
            });
        }
        return btns;
    };
	// ui.assembleData 就是 configInfo
};

/**
 *  二次开发：复写基本查询区
 */
function _override_bsearch (ui) {
    ui._init = function(){
        ui.getItemJQ("FZR").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");

    }
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	    // 添加onChange事件
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {})
	//};
    /*负责人的回调函数*/
    ui.addCallback("setComboGridValue_fzr",function(o){
        if(null == o) return;
        var obj = o.rowData;
        if(null == obj) return;
        ui.setItemValue("FZR",obj.XM);

    });
    /*批次号的回调函数*/
    ui.addCallback("setComboGridValue_pch",function(o){
        if(null == o){
            return;
        }
        var obj = o.rowData;
        if(null == obj) return;
        ui.setItemValue("PCH",obj.PCH);
    });
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
function initTPdiv(tpmc) {
    //var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
    //var path = "spzstpfj/thumb/1476780291968.jpeg";//+tpbcmc;
    var path = "spzstpfj/thumb/"+tpmc;
    var html = "<div style=\"margin-bottom: 50px;margin-left:55px;display:none\">" +
            "<div class=\"fillwidth colspan2 clearfix\">" +
            "<div class=\"app-inputdiv6\">" +
            "<label class=\"app-input-label\" style=\"width:55%;\">产地证明图片显示：</label>" +
            "<div id=\"file${idSuffix}\">" +
            "<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns" + subffix + "').viewImage(this)\"/>" +
            "</div>" +
            "<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>" +
            "<img id=\"data_list\" src='"+path+"'>" +
            "</div>" +
            "</div>" +
            "</div>";
    return html;
}









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
