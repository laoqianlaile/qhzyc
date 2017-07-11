<%@page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.ces.component.trace.utils.SerialNumberUtil" %>
<%
    String companyCode = SerialNumberUtil.getInstance().getCompanyCode();
%>
<script type="text/javascript">
    /***************************************************!
     * @date   2016-04-15 13:23:29
     * 系统配置平台自动生成的自定义构件二次开发JS模板
     * 详细的二次开发操作，请查看文档（二次开发手册/自定义构件.docx）
     * 注：请勿修改模板结构（若需要调整结构请联系系统配置平台）
     ***************************************************/


    (function(subffix) {
        var _djcpsl = "";
        var _zhcpsl = "";
        var  resourceFolder = $.contextPath + "/cfg-resource/coral40/common";
        var hUI;
        var ImgSuffix;

        /**
         * 二次开发定位自己controller
         * 系统默认的controller: jQuery.contextPath + "/appmanage/show-module"
         * @returns {String}
         **/
        window[CFG_actionName(subffix)] = function (ui) {
            // ui.assembleData 就是 configInfo
            return jQuery.contextPath + "/sdzycypbzxx";
        };


        /**
         * 二次开发：复写自定义表单
         */
        function _override_form (ui) {
            //初始化添加企业编码
            ui._init = function(){
                hUI= ui;
                var jywjDiv = ui.getItemBorderJQ("bztp");
                jywjDiv.empty();
                jywjDiv.html(initTPdiv());
                initToolbar();
                var  qybm = ui.getItemJQ("QYBM");
                qybm.textbox("setValue","<%=companyCode%>");
                var url = ui.getAction() + "!show.json?P_tableId=" + ui.options.tableId +
                        "&P_componentVersionId=" + ui.options.componentVersionId +
                        "&P_menuId=" + ui.options.menuId +
                        "&P_workflowId=" + null2empty(ui.options.workflowId) +
                        "&P_processInstanceId=" + ui._getProccessInstanceId() +
                        "&id=" + ui.options.dataId + "&P_menuCode=" + ui.getParamValue("menuCode");
                var fromData = $.loadJson(url);
                if(ui.options.isView){
                    $("#chooseImage").removeAttr("onClick");
                }
                if(!isEmpty(fromData.BZTP))
                    $.ns("ns"+subffix+"").uploadImage($.contextPath+"/spzstpfj/"+fromData.BZTP);
            };
            ui.bindEvent = function(){
                /*生产批次号*/
                var scpch = ui.getItemJQ("QYPCH");
                //var scpch = ui.getItemJQ("SCPCH");
                scpch.combogrid("option","onChange",function(e,data){
                    var rowData = scpch.combogrid("grid").grid("getRowData",data.value);
                    ui.setFormData({YPMC:rowData.YPMC,YCDM:rowData.YCDM,SCPCH:rowData.SCPCH,QYPCH:rowData.QYPCH});
                });
                var bzge = ui.getItemJQ("BZGG");
                var bzzl = ui.getItemJQ("BZZL");
                bzge.textbox("option","onChange",function(e,data){
                    var bzgg =bzge.val();
                    var bzzls =bzzl.val();
                    /*if(bzzls < bzgg){
                     CFG_message("请保证包装规格小于包装数量","error");
                     } else if(bzgg > bzzls){*/
                    if(bzgg!=null&&bzzls!=null){
                        var bzsl=parseInt(bzzls/bzgg);
                        ui.setFormData({BZSL:bzsl})
                    };

                });

                bzzl.textbox("option","onChange",function(e,data){
                    var bzzls =bzzl.val();
                    var bzgg =bzge.val();
                    /*if(bzzls < bzgg){
                     debugger
                     CFG_message("请保证包装数量大于包装规格","error");
                     } else if(bzzls > bzgg){*/
                    if(bzgg!=null&&bzzls!=null){
                        var bzsl=parseInt(bzzls/bzgg);
                        ui.setFormData({BZSL:bzsl})
                    }

                });
                bzge.textbox("option","onChange",function(e,data){
                    var bzgg =bzge.val();
                    var bzzls =bzzl.val();
                    /*if(bzzls < bzgg){
                     CFG_message("请保证包装规格小于包装数量","error");
                     } else if(bzgg > bzzls){*/
                    if(bzgg!=null&&bzzls!=null){
                        var bzsl=parseInt(bzzls/bzgg);
                        ui.setFormData({BZSL:bzsl})
                    };

                });
            }







            ui.addCallback("setComboGridValue_scpch",function(o){
                if (null == o) return;
                var rowData = o.rowData;
                if (null == rowData) return;
                ui.setFormData({
                    SCPCH:rowData.SCPCH,
                    YPMC:rowData.YPMC,
                    QYPCH:rowData.QYPCH
                });
            });
//保存前验证生产批次号
//    ui.beforeSave = function(){
//        var scpch = ui.getItemValue("SCPCH");
//        var jsonData = $.loadJson($.contextPath+"/sdzycypbzxx!checkScpch.json?scpch="+scpch);
//        if(jsonData.length!= 0){
//            CFG_message("该批次已经被包装过，请勿重复包装","warning");
//            return false;
//        }
//    }

            ui.clickSave = function( op ){
                var _this = ui, jqForm = $("form", this.uiForm),
                        url = this.getAction() + "!save.json?",
                        formData, selfGrid, postData;
                if (!jqForm.form("valid")) return;
                // 保存前回调方法
                if (_this.processBeforeSave(jqForm, op) === false) return;
                // 获取表单数据
                postData = new FormData(jqForm);
                formData = jqForm.form("formData", false);
                var base64 = $(".thumb").attr("src");
                if(isEmpty(base64)){
                    CFG_message("请上传包装图片","warning");
                    return;
                }
                if( parseFloat(formData.BZGG) > parseFloat(formData.BZZL)){
                    CFG_message("包装重量不能小于包装规格","warning");
                    return;
                }
//                if(formData.BZZL%formData.BZGG !=0 ){
//                    CFG_message("包装有剩余,请重新输入","warning");
//                    ui.setFormData({BZZL:"",BZGG:"",BZSL:""});
//                    return;
//                }
                base64 =base64.substr(base64.indexOf(",")+1,base64.length);
                formData.BZTP=base64;
                formData.IMGSUFFIX=ImgSuffix;
                $.each(formData,function ( i, data) {
                    postData.append(i,data);
                })

                $.ajax({
                    type : "POST",
                    url : url,
                    data : postData,
                    /**
                     *必须false才会自动加上正确的Content-Type
                     */
                    contentType: false,
                    /**
                     * 必须false才会避开jQuery对 formdata 的默认处理
                     * XMLHttpRequest会对 formdata 进行正确的处理
                     */
                    processData: false,
                    timestamp: false,
                    async: false,
                    success : function(entity) {
                        if (false === entity.success || !("ID" in entity)) {
                            if (isEmpty(entity.message)) CFG_message("操作失败！", "warning");
                            else CFG_message(entity.message, "warning");
                            return;
                        }
                        // 保存后回调方法
                        _this.processAfterSave(entity, op);
                        CFG_message("操作成功！", "success");
                        if ("save" === op) {
                            jqForm.form("loadData", entity);
                            if (_this.options.model === $.config.contentType.form) {
                                selfGrid = _this.getSelfGrid();
                                if (selfGrid) {
                                    selfGrid.reload();
                                }
                            }
                        } else if ("close" === op) {
                            _this.clickBackToGrid();
                        } else if ("create" === op) {
                            _this.clickCreate();
                        } else {
                            _this.doViewRecord(op);
                        }
                    },
                    error : function() {
                        CFG_message("操作失败！", "error");
                    }
                });
            }
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
                    var url = $.contextPath + "/sdzycypbzxx!queryPch.json?pch="+rowData.BZPCH;
                    var ssqy = $.loadJson(url);
                    if((ssqy.data[0].SFRK=='0')){
                        ui.databaseDelete(idArr, isLogicalDelete);
                    }else{
                        $.alert("不能删除!该生产已打印赋码");
                        return;
                    }
                }
            }
            ui.beforeInitGrid = function(setting) {
                setting.fitStyle = "width";
                return setting;
            };

            ui.beforeInitGrid = function(setting) {
                setting.fitStyle = "width";
                return setting;
            };

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

            ui.beforeInitGrid = function(setting) {
                setting.fitStyle = "width";
                return setting;
            };

            ui.clickSecondDev = function(id){
                if (id == $.config.preButtonId + "print") {
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
                    var zsm = $.loadJson($.contextPath +"/sdzycypbzxx!demandBzpch.json?bzpch="+rowData.BZPCH);
                    var bxq = "";
                    for(var i in rowData.BZSJ.split("-")){
                        if(i==0){
                            bxq = parseInt(rowData.BZSJ.split("-")[i]) + 1 ;
                        }else{
                            bxq += "-" + rowData.BZSJ.split("-")[i];
                        }
                    }
                    var printSc = true ;
                    //showPrintDialog();

                    $.alert("<span style='font-size:20px;color:red'> 请在release中打印...</span>");
                    //判断双排打印还是单排
                    if(zsm.length%2==0) {
                        for (var i = 0; i < zsm.length / 2; i++) {
                            try {
                                var result = _print.print("bzglPrint", {
                                    "CPMC": "产品名称 ：" + rowData.YPMC,
                                    "CPDJ": "产品单价 ：1000",
                                    "BZRQ": "包装日期 ：" + rowData.BZSJ,
                                    "ZL": "重量 ：",
                                    "CPZSM": "产品追溯码：" + zsm[i].ZSM,
                                    //"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
                                    "URLL": "http://www.sdzyczs.com/zsm?" + zsm[i*2].ZSM,
                                    "URLR": "http://www.sdzyczs.com/zsm?" + zsm[i*2 + 1].ZSM
                                });
                                var resultData = JSON.parse(result);
                                if (resultData.MSG != "SUCCESS") {
                                    $.alert("打印失败");
                                    jqUC.dialog("close");
                                    return;
                                }
                            } catch (e) {
                            }
                        }
                    }else{
                        if(zsm.length<2)
                        {
                            try {
                                var result = _print.print("bzglPrint", {
                                    "CPMC": "产品名称 ：" + rowData.YPMC,
                                    "CPDJ": "产品单价 ：1000",
                                    "BZRQ": "包装日期 ：" + rowData.BZSJ,
                                    "ZL": "重量 ：",
                                    "CPZSM": "产品追溯码：" + zsm[0].ZSM,
                                    //"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
                                    "URLL": "http://www.sdzyczs.com/zsm?" + zsm[0].ZSM
                                });
                                var resultData = JSON.parse(result);
                                if (resultData.MSG != "SUCCESS") {
                                    $.alert("打印失败");
                                    jqUC.dialog("close");
                                    return;
                                }
                            } catch (e) {
                            }
                        }else{
                            for (var i = 0; i < (zsm.length / 2)-1; i++) {
                                try {
                                    var result = _print.print("bzglPrint", {
                                        "CPMC": "产品名称 ：" + rowData.YPMC,
                                        "CPDJ": "产品单价 ：1000",
                                        "BZRQ": "包装日期 ：" + rowData.BZSJ,
                                        "ZL": "重量 ：",
                                        "CPZSM": "产品追溯码：" + zsm[i].ZSM,
                                        //"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
                                        "URLL": "http://www.sdzyczs.com/zsm?" + zsm[i*2].ZSM,
                                        "URLR": "http://www.sdzyczs.com/zsm?" + zsm[i*2 + 1].ZSM
                                    });
                                    var resultData = JSON.parse(result);
                                    if (resultData.MSG != "SUCCESS") {
                                        $.alert("打印失败");
                                        jqUC.dialog("close");
                                        return;
                                    }
                                } catch (e) {
                                }
                            }
                            try {
                                var result = _print.print("bzglPrint", {
                                    "CPMC": "产品名称 ：" + rowData.YPMC,
                                    "CPDJ": "产品单价 ：1000",
                                    "BZRQ": "包装日期 ：" + rowData.BZSJ,
                                    "ZL": "重量 ：",
                                    "CPZSM": "产品追溯码：" + zsm[zsm.length-1].ZSM,
                                    //"URL":"http://www.sdzyczs.com/zsm?"+zsm[i].ZSM,
                                    "URLL": "http://www.sdzyczs.com/zsm?" + zsm[zsm.length-1].ZSM
                                });
                                var resultData = JSON.parse(result);
                                if (resultData.MSG != "SUCCESS") {
                                    $.alert("打印失败");
                                    jqUC.dialog("close");
                                    return;
                                }
                            } catch (e) {
                            }
                        }
                    }
                }else if( id == $.config.preButtonId +"download") {
                    var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
                    if($.isEmptyObject(rowData)){//判断为空
                        CFG_message("请选择一条数据","waining");
                        return false;
                    }
                    window.open($.contextPath +"/sdzycypbzxx!downLoad?bzpch="+rowData.BZPCH,"_self");
                }

            }

            /************************打印dialog bigen************************/
            function showPrintDialog(){
                var jqGlobal = $(ui.options.global);
                var jqUC = $("<div id=\"jqUC\"></div>").appendTo("body");
                jqUC.dialog({
                    appendTo: jqGlobal,
                    modal: true,
                    title: "打印",
                    width: 400,
                    height: 160,
                    resizable: false,
                    position: {
                        at: "center top+200"
                    },
                    onClose: function () {
                        jqUC.dialog("close");
                        jqUC.remove();
                        _djcpsl = "";
                        _zhcpsl = "";
                    },
                    onCreate: function () {

                        var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\">" +
                                "       <div class=\"app-inputdiv11\" style=\"float: left; width: 60px;padding-top: 8px\"  >" +
                                "           <label >打印份数：</label>" +
                                "       </div>" +
                                "       <div class=\"app-inputdiv12\" style=\"float: left;width: 250px;\" >" +
                                "           <input id='djcp' name='DJCP' />" +
                                "       </div>" +
                                "   </div>"
                        ).appendTo(this);

                        $('#djcp,#zhcp').textbox({
//                    readonly:true
                        });
                        // $("#zhcp").attr("readonly","readonly");
                        // $("#djcp").attr("readonly","readonly");
//                var jq = $("<input id=\"UNTREAD_OPINION_READCODE\" name=\"opinion\"></textarea>").appendTo(jqDiv);
//                jq.textbox({width: 200, maxlength: 22});
                    },
                    onOpen: function () {
                        var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                                jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                        jqDiv.toolbar({
                            data: ["->", {id: "sure", label: "确定", type: "button"}, {
                                id: "cancel",
                                label: "取消",
                                type: "button"
                            }],
                            onClick: function (e, data) {
                                _djcpsl =1;_zhcps1 =0;//新增内容 使得_djcpsl,_zhcps1失去区分选择单件还是组合产品的功能
                                if ("sure" === data.id) {
                                    if(_djcpsl == 1){
                                        if (/^[0-9]+$/.test( $("#djcp").val() )) {
                                        } else {
                                            CFG_message("请输入自然数！", "warning");
                                            return false;
                                        }
                                    }else if(_zhcps1 == 1){
                                        if (/^[0-9]+$/.test( $("#djcp").val() )) {//此处修改了内容 原值为#zhcp
                                        } else {
                                            CFG_message("请输入自然数！", "warning");
                                            return false;
                                        }
                                    }else{
                                        CFG_message("请输入打印份数！", "warning");//新增内容
                                        // CFG_message("请选择打印形式和打印份数！", "warning");
                                        return false;
                                    }
                                    var rowData = ui.uiGrid.grid("getRowData",ui.getSelectedRowId());
                                    var bxq = "";
                                    for(var i in rowData.BZSJ.split("-")){
                                        if(i==0){
                                            bxq = parseInt(rowData.BZSJ.split("-")[i]) + 1 ;
                                        }else{
                                            bxq += "-" + rowData.BZSJ.split("-")[i];
                                        }
                                    }

                                    if(_djcpsl == 1){
                                        for(var i = 0;i<parseInt($("#djcp").val());i++){
                                            var cpzsm = rowData.SCPCH;
                                            var data = {
                                                qymc: rowData.YPMC,
                                                cpmc: rowData.YPMC,
                                                cpdj: rowData.YPMC,
                                                bzrq: rowData.JYSJ,
                                                bxq: bxq,
                                                cpzsm: cpzsm,
                                                bzgg: rowData.YPMC
                                            };
                                            if(isSwt){
                                                var result = _window("printSczzBz", JSON.stringify(data));
                                                var resultData = JSON.parse(result);
                                                if (resultData.status == "true" || resultData.status == true) {
                                                    var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                                    if(savePrint != true ){
                                                        $.alert("打印失败：" + resultData.msg);
                                                        return false;
                                                    }
                                                } else {
                                                    $.alert("打印失败：" + resultData.msg);
                                                    return false;
                                                }
                                            }else if(isNewSwt){
                                                var result=_print.print("bzglPrint",{
                                                    "CPMC":"产品名称 ：" + rowData.YPMC,
                                                    "CPDJ":"产品等级 ：" + rowData.YPMC,
                                                    "BZRQ":"包装日期 ：" + rowData.JYSJ,
                                                    "BXQ":"保鲜期 ：" + bxq,
                                                    "ZL":"重量 ：" + rowData.YPMC,
                                                    "CPZSM": cpzsm,
                                                    "URL":"http://www.sdzyczs.com/zsm?" + cpzsm
                                                });

                                                if (result.MSG == "SUCCESS") {
                                                    var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                                    if(savePrint != true ){
                                                        $.alert("打印失败");
                                                        return false;
                                                    }
                                                } else {
                                                    $.alert("打印失败");
                                                    return false;
                                                }
                                            }
                                        }
                                        if(_zhcpsl != 1){
                                            $.alert("打印成功");
                                        }
                                    }
                                    if(_zhcpsl == 1){
                                        for(var i = 0;i<$("#zhcp").val();i++){
                                            var scpch = rowData.SCPCH;
                                            if(isSwt){
                                                var cpzsm = rowData.SCPCH;
                                                var data = {
                                                    qymc: rowData.YPMC,
                                                    cpmc: rowData.YPMC,
                                                    cpdj: rowData.YPMC,
                                                    bzrq: rowData.JYSJ,
                                                    bxq: bxq,
                                                    cpzsm: cpzsm,
                                                    bzgg: rowData.YPMC
                                                };
                                                var result = _window("printSczzBz", JSON.stringify(data));
                                                var resultData = JSON.parse(result);
                                                if (resultData.status == "true" || resultData.status == true) {
                                                    var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                                    if(savePrint != true ){
                                                        $.alert("打印失败：" + resultData.msg);
                                                        return false;
                                                    }
                                                } else {
                                                    $.alert("打印失败：" + resultData.msg);
                                                    return false;
                                                }
                                            }else if(isNewSwt){
                                                var result=_print.print("bzglPrint",{
                                                    "CPMC":"产品名称 ：" + rowData.YPMC,
                                                    "CPDJ":"产品等级 ：" + rowData.YPMC,
                                                    "BZRQ":"包装日期 ：" + rowData.JYSJ,
                                                    "BXQ":"保鲜期 ：" + bxq,
                                                    "ZL":"重量 ：" + rowData.YPMC,
                                                    "CPZSM": cpzsm,
                                                    "URL":"http://www.sdzyczs.com/zsm?" + cpzsm
                                                });

                                                if (result.MSG == "SUCCESS") {
                                                    var savePrint = $.loadJson($.contextPath + "/zzbzgl!savePrint.json?bzlsh="+rowData.CSPCH+"&bzxs=xbz&cpzsm="+cpzsm+"&id="+rowData.ID);
                                                    if(savePrint != true ){
                                                        $.alert("打印失败");
                                                        return false;
                                                    }
                                                } else {
                                                    $.alert("打印失败");
                                                    return false;
                                                }
                                            }

                                        }
                                        $.alert("打印成功");
                                    }
                                }
                                jqUC.dialog("close");
                                _djcpsl = "";
                                _zhcpsl = "";
                            }
                        });
                    }
                });
            }
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
                    if(i == 4){
                        btns.push({
                            id:$.config.preButtonId + "print",
                            icon:"icon-print",
                            label: "打印",
                            type : "button"
                        });
                        btns.push({
                            id:$.config.preButtonId + "download",
                            //icon:"icon-print",
                            label: "下载追溯码",
                            type : "button"
                        });
                    }
//            if(i==5){
//                btns.push({
//                    id:$.config.preButtonId + "chuchang",
////                    icon:"chuchang",
//                    label: "出场",
//                    type : "button"
//                });
//            }
                }

                return btns;
            };
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
            ui._init = function(){
                //var bzgg = ui.getItemJQ("BZGG");
                //bzgg.textbox("option","valid","isNumber");
                ui.setItemOption("BZGG","valid",function(){
                    //var zz = /^[0-9]+(.[9]{1,2})?0-$/;
                    //var zz =/^([1-9]+(.[0-9]{1,2})?)|0.[1-9]{1,2}$/;
                    var zz = /^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$/;
                    var errorMessage = "不符合浮点型格式或精度过大！",
                            isValid;
                    var bzgg = ui.getItemValue("BZGG")
                    var re = new RegExp(zz);
                    if(re.test(bzgg)){
                        return;
                    }else{
                        isValid = false;
                        return { isValid: isValid, errMsg: errorMessage };
                    }


                });

                ui.setItemOption("BZZL","valid",function(){
                    //var zz = /^[0-9]+(.[9]{1,2})?0-$/;
                    //var zz =/^([1-9]+(.[0-9]{1,2})?)|0.[1-9]{1,2}$/;
                    var zz = /^([1-9]+(\.[0-9]+[1-9])?)|([0]+(\.([0-9]+)?[1-9]))$/;
                    var errorMessage = "不符合浮点型格式或精度过大！",
                            isValid;
                    var bzzl = ui.getItemValue("BZZL")
                    var re = new RegExp(zz);
                    if(re.test(bzzl)){
                        return;
                    }else{
                        isValid = false;
                        return { isValid: isValid, errMsg: errorMessage };
                    }


                });

                ui.setItemOption("BZSL","valid",function(){
                    var bzsl = ui.getItemValue("BZSL");
                    var zz = /^[1-9]\d*$/;
                    var re = new RegExp(zz);
                    var errorMessage = "请输入大于0的正整数！",
                            isValid;
                    if(re.test(bzsl)){
                        return;
                    }else{
                        isValid = false;
                        return { isValid: isValid, errMsg: errorMessage };
                    }
                })

            };


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
        jQuery.extend(jQuery.ns("ns" + subffix), {
            viewImage : function (fileInput) {
                var view = $("#view${idSuffix}");
//        view.empty();
                if (window.FileReader) {
                    var p = $("#preview${idSuffix}");
                    var file = fileInput.files;
                    var f = file[file.length-1];

                    ImgSuffix = f.name.substring(f.name.lastIndexOf('.')+1);

                    //图片不能超过2M，
                    if(!(f.size<20971520 && (ImgSuffix=='jpg'||ImgSuffix=='bmp'||ImgSuffix=='jpeg'||ImgSuffix=='png'))){
                        CFG_message("图片必须小于20M，且格式必须为.png, .jpg 或 .bmp！", "error");
                        $("#imageUpload${idSuffix}").val("");
                        return false;
                    }
                    var fileReader = new FileReader();
                    fileReader.onload = (function (file) {
                        return function (e) {
                            var img = new Image();
                            img.src = this.result;
                            img.onload = function() {
                                var canvas = document.getElementById('uploadImg');
                                var cxt = canvas.getContext('2d');
                                var originWidth = img.width;
                                var originHeight = img.height;
                                if ( originWidth >  originHeight  && originWidth>1024) { //如果宽大于高在根据款进行压缩
                                    originHeight = Math.round(originHeight*1024/originWidth);
                                    originWidth = 1024;
                                }else if(originWidth < originHeight && originHeight>1024){ //如果宽大于高在根据款进行压缩
                                    originWidth = Math.round(1024*originWidth/originHeight);
                                    originHeight = 1024;
                                }
                                canvas.width = originWidth;
                                canvas.height = originHeight;
                                cxt.drawImage(img, 0, 0, originWidth, originHeight);
                                if("jpeg" == ImgSuffix || "jpg" == ImgSuffix){
                                    $.ns('ns' + subffix).uploadImage(canvas.toDataURL("image/jpeg", 0.9));
                                }else{
                                    $.ns('ns' + subffix).uploadImage(canvas.toDataURL("image/"+ImgSuffix, 0.9));
                                }
                                return;
                            }
                        }
                    })(f);
                    fileReader.readAsDataURL(f);
                }
            },
            uploadImage : function (tplj){
                var view = $("#view${idSuffix}");
                view.empty();
                var image = new Image();
                image.src = tplj;
                image.className = "thumb";
                image.onload = function (e) {

                    var div = document.createElement('div');
                    $(div).append(this);
//                    $(div).append('<span style="display: none;position: absolute; margin-top: 0px; margin-left: 6px; width: 20px; height: 20px;"><img onclick = "$.ns(\'ns'+subffix+'\').deleteImage(\'' + this.src + '\')" style = "height:20px" src = "'+resourceFolder+'/css/images/trace-image/trash_bin.png"></span>');
                    var originWidth = this.width;
                    var originHeight = this.height;
                    div.style.float = "left";
                    div.style.marginRight = "5px";
                    div.style.height = "100px";
                    if(((originWidth * 100) / originHeight) > 750){
                        div.style.width = "750px";
                    }else{
                        div.style.width = (originWidth * 100) / originHeight + "px";
                    }
                    image.style.height = div.style.height;
                    image.style.width = div.style.width;
                    this.style.position = "absolute";
                    this.style.border = "1px solid #2AC79F",
                            $(div).hover(function () {
                                $(div).find("span").toggle();
                            });
                    view.append(div);
                    if(view.css("width") != "0px"){
                        $("#chooseImage${idSuffix}").css("margin-top","75px");
                    }
                }
            },
            deleteImage : function (src) {
                $.ajax({
                    type: "POST",
                    url: $.contextPath + '/sdzycypbzxx!deleteTp.json',
                    data: {'id': hUI.options.dataId},
                    dataType: "json",
                    success: function (data) {
                        var view = $("#view${idSuffix}");
                        hUI.setFormData({"BZWJ":""});
                        view.empty();
                        $("#imageUpload${idSuffix}").val("");
                        $("#chooseImage${idSuffix}").css("margin-top","0px");
                    }
                })
            }
        });
        function  initTPdiv() {//

            var html = "<div style=\"margin-bottom: 50px;margin-left:55px\">"+
                    "<div class=\"fillwidth colspan2 clearfix\">"+
                    "<div class=\"app-inputdiv6\">"+
                    "<label class=\"app-input-label\" style=\"width:20%;\">包装图片：</label>"+
                    "<div id=\"file${idSuffix}\">"+
                    "<input class=\"inputfile\" type=\"file\" style=\"width:160px;display:none\" id=\"imageUpload${idSuffix}\" multiple=\"multiple\" lable=\"预览\"	accept=\"image/*\" name=\"imageUpload\" onchange=\"$.ns('ns"+subffix+"').viewImage(this)\"/>"+
                    "</div>"+
                    "<div style=\"margin-left: 10%\" id=\"view${idSuffix}\"></div>"+
                    "<button style=\"margin-left: 0px\" id=\"chooseImage${idSuffix}\" class='ctrl-toolbar-element ctrl-init ctrl-init-button coral-button coral-component coral-state-default coral-corner-all coral-button-text-only coral-toolbar-item-component' type='button' onclick=\"$('#imageUpload${idSuffix}').click()\">"+
                    "<span class=\"coral-button-text \">上传图片</span><canvas id=\"uploadImg\" style=\"display:none\"></canvas>"+
                    "</button>"+
                    "</div>"+
                    "</div>"+
                    "</div>";
            return html;

        }

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

                            data.unshift({
                                "type": "html",
                                "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - " + menuObj.name + poss + "</div>",
                                frozen: true
                            });
                        }
                    }

                    return data;
                }
            };
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
