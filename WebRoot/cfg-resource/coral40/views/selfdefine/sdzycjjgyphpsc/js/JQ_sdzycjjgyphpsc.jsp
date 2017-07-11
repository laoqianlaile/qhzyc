<%@page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript">
/***************************************************!
 * @date   2016-11-06 13:44:21
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
	return jQuery.contextPath + "/sdzycjjgyphpsc";
};
	

/**
 * 二次开发：复写自定义表单
 */
function _override_form (ui) {
	// ui.assembleData 就是 configInfo
	//ui.bindEvent = function () {
	//	  ui.setItemOption("NAME", "onChange", function(e, data) {});// 添加onChange事件
	//    ui.callItemMethod("USER_ID", "disable");// 禁用USER_ID
	//};
    ui._init = function(){
        initToolbar();
        ui.getItemJQ("SCJL").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");
        ui.getItemJQ("GYY").combogrid("reload",$.contextPath+"/zzgzryda!getGzrydaGrid.json?dwlx=JJGQY");
        if(isEmpty(ui.options.dataId) && ui.options.number == 1) {
            ui.setItemValue("QYBM",$.loadJson($.contextPath + "/trace!getQybm.json"));
        }
    }
    ui.bindEvent = function(){
        _this = this;
        var scfabh = ui.getItemJQ("SCFABH");
        scfabh.combogrid("option","onChange",function(e,data) {
            var rowData = scfabh.combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({SCFABH:data.text,YPMC:rowData.YPMC,JGGY:rowData.JGGY,YCDM:rowData.YCDM,YPJS:rowData.YPJS});
        });
        /*领料单号下拉数据*/
        var lldh = ui.getItemJQ("LLDH");
        lldh.combogrid("option","onChange",function(e,data){
            //根据领料单号初始化原料批次号数据
            var jsonData = $.loadJson( $.contextPath +"/sdzycypscxx!searchLlycxxData.json?lldh="+data.value);
           // ui.getItemJQ("QYPCH").combogrid("reload",jsonData);
            ui.getItemJQ("YYCPCH").combogrid("reload",jsonData);
        });
        ui.setItemOption("YYCPCH","onChange" , function( e ,data){
            var rowData = ui.getItemJQ("YYCPCH").combogrid("grid").grid("getRowData",data.value);
            ui.setFormData({YYCPCH:rowData.PCH,JGZZL:rowData.LLZZL,YCDM:rowData.YCDM,CSPCH:rowData.CSPCH,QYPCH:rowData.QYPCH,YCMC:rowData.LLMC,CD:rowData.CD});
        });
    };

    //计算饮片重量
    ui.clickAdd = function(){
        var cGrid = this.getSelfGrid(), // 对应列表
                jqForm = $("form", this.uiForm),
                formData;
        // 表单检验
        if (!jqForm.form("valid")) return false;
        // 获取表单数据
        formData = jqForm.form("formData", false);
        // 向列表添加数据
        cGrid.addRowData(formData);
        if (!cGrid) return;
       var rowData = cGrid.toFormData();//获取从列表的数据
        _this.getMasterForm().setItemValue("JGZZL",processJgzzl(rowData));
        // 重置表单数据
        //this.clickCreate();
    }
    //保存返回主菜单
    ui.afterSave = function (){
        ui._assembleReturn(true);
        var ltc_id = $("#mainContent div div")[0].getAttribute("id");
        var gd_id = $("#LTC_"+ltc_id.substr(5,ltc_id.length)+" div ")[0].getAttribute("id");
        $("#gd_" + gd_id.substr(3,gd_id.length)).grid("reload");
    }
    ui.addCallback("setCombogridValue_scfabh",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        var jsonData = $.loadJson( $.contextPath + "/sdzycypscxx!getJggymc.json?jggy="+rowData.JGGY);
        ui.setFormData({
            SCFABH:rowData.SCFABH,
            YPMC:rowData.YPMC,
            YCDM:rowData.YCDM,
            YPJS:rowData.YPJS,
            JGGY:jsonData
        });
    });

    ui.addCallback("setCombogridValue_lldh",function(o){
        if (null == o) return;
        var rowData = o.rowData;
        if (null == rowData) return;
        ui.setFormData({
            LLDH:rowData.LLDH,
            YYCPCH:rowData.PCH,
            JGZZL:rowData.LLZZL
        });
    });
};

/**
 *  二次开发：复写自定义列表
 */
function _override_grid (ui) {
	// ui.assembleData 就是 configInfo
	//ui.beforeInitGrid = function (setting) {
	//	return setting;
	//};
    //改写超链接前台删除方法
    ui.linkDeleteGD =function(id){
        var _this = this;
        $.confirm("当前记录将从列表移除，确定吗？", function(sure) {
            if (sure) {
                _this.uiGrid.grid("delRowData", id);
                var rowData = _this.getRowData();
                _this.getMasterForm().setItemValue("JGZZL",processJgzzl(rowData));
            }
        });
    }
};

/**
 *  二次开发：复写自定义工具条
 */
function _override_tbar (ui) {
	// ui.assembleData 就是 configInfo
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
    var _this ;
    function initToolbar(){
        $.fn['ctbar'].defaults = {
            processData: function(data, pos) {
                var ui =  this;
                if ("top" === pos) {
                    var op = ui.options.op.toString();
                    var poss = "";
                    // 表单
                    if ($.config.contentType.isForm(ui.options.type)) {
                        if (op == '0') {
                            poss =" - 新增";
                        } else if (op == '1') {
                            poss =" - 修改";
                        } else if (op == '2') {
                            poss =" - 详情";
                        }
                        if(data.length == 0){
                            poss =" - 详请";
                        }
                    }

                    var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
                    if(menuObj.name != undefined){
                        if(ui.options.number ==1 || (ui.options.number === 2 && ui.options.type !== $.config.contentType.form)
                        ) {
                            data.unshift({
                                "type": "html",
                                "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - " + menuObj.name + poss + "</div>",
                                frozen: true
                            });
                        }
                    }
                }

                return data;
            }
        };
    }
    //计算加工总重量
    function processJgzzl(rowData){
        var jgzzl = 0;
        for(var i=0;i<rowData.length;i++){
            jgzzl += parseFloat(rowData[i].JGZZL);
        }
        return jgzzl;
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
