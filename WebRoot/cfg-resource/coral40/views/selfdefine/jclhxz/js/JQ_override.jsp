<%@page language="java" import="com.ces.config.utils.*" pageEncoding="UTF-8" %>
<% %>
<script type="text/javascript">
    /***************************************************!
     * @author qiucs
     * @date   2014-7-15
     * 系统配置平台应用自定义二次开发JS模板
     ***************************************************/


    (function (subffix) {

        /**
         * 二次开发定位自己controller
         * @returns {String}
         **/
        window[CFG_actionName(subffix)] = function () {
            // this.assembleData 就是 configInfo
            return $.contextPath + "/jclhxz";
        };


        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            var barCode;
            var prefix;
            if (ui.options.number == 1) {
                ui.clickSecondDev = function (id) {
                    if (id == $.config.preButtonId + "dm") {
                        var ccxx = $.loadJson($.contextPath + "/jclhxz!getZzccxx.json?ccpch=PC531000001000009");
                    }
                    if (id == $.config.preButtonId + "dk") {
                        /**
                         * 从卡内读取理货卡信息在表单上显示
                         */
                        if (isSwt) {
                            //var result = _window("readAllRecords", "businessDetail");
                            //var userInfo = _window("readCard","userInfo");
                            var result = _window("readUserInfoAndBusinessDetail");
                            var userInfoAndBusinessDetail = JSON.parse(result);
                            if (userInfoAndBusinessDetail.status == false || userInfoAndBusinessDetail.status == "false") {
                                $.message({message: userInfoAndBusinessDetail.msg, cls: "warning"});
                                return;
                            }
                            var userValue = userInfoAndBusinessDetail.user;
                            var value = userInfoAndBusinessDetail.records;

                            if (userInfoAndBusinessDetail.status == "true" || userInfoAndBusinessDetail.status == true) {
                                if (userValue.cardType === "2") {//判断卡类型
                                    ui.setFormData({PFSMC: userValue.userName, PFSBM: userValue.userId});//填入卡内用户信息
                                    var cGrid = ui.getDetailGrid();
                                    var cForm = ui.getDetailForm();
                                    for (var i = 0; i < value.length; i++) {
                                        var detailData = value[i];
                                        var spmc = $.loadJson($.contextPath + "/cpfl!getSpmc.json?spbm=" + detailData.productCode);
                                        cForm.SPMC = spmc;
                                        cForm.ZL = detailData.dealWeight;
                                        cForm.DJ = detailData.dealPrice;
                                        cForm.JHPCH = detailData.batchCode;//批次号
                                        cForm.SPBM = detailData.productCode;
                                        cForm.GHPCSCBM = detailData.sellerCode;
                                        cForm.GHPCSCMC = detailData.sellerName;
                                        cForm.JYPZH = detailData.tracecode;
                                        cForm.GHPFSCMC = detailData.businessMarketName;
                                        cForm.GHPFSCBM = detailData.businessMarketCode;
                                        //计算批次金额
                                        if (!$.isNumeric(detailData.dealWeight)) cForm.ZL = 0;
                                        if (!$.isNumeric(detailData.dealPrice)) cForm.DJ = 0;
                                        cForm.JE = parseFloat(cForm.ZL, 10) * parseFloat(cForm.DJ, 10);
                                        //向列表中添加数据
                                        cGrid.addRowData(cForm);
                                    }
                                    //更新主表单理货总重量和总金额
                                    var rowData = cGrid.getRowData(), zl, je, lhzzl = 0, lhzje = 0;
                                    for (var i = 0, len = rowData.length; i < len; i++) {
                                        zl = rowData[i].ZL;
                                        je = rowData[i].JE;
                                        if (!$.isNumeric(zl)) zl = 0;
                                        if (!$.isNumeric(je)) je = 0;
                                        lhzzl += parseInt(zl, 10);
                                        lhzje += parseInt(je, 10);
                                    }
                                    $(this.uiForm).form();
                                    this.uiForm.form("loadData", {LHZZL: lhzzl, LHZJE: lhzje, JHPCH: ""});//读卡清除主表进货批次号
                                    $.message({message: "读卡成功", cls: "info"});
                                } else {
                                    $.message({message: "请使用蔬菜类流通卡！", cls: "warning"});
                                }
                            }
                        } else {
                            $.alert("请在程序中操作");
                        }
                    }
                    if (id == $.config.preButtonId + "inputBarCode") {
                        showDialog();
                    }
                }

                ui.bindEvent = function () {
                    //批发商名称下拉列表
                    var jqPfsmc = $("#PFSMC_" + ui.timestamp);
                    var jqCdmc = $("#CDMC_" + ui.timestamp);
                    jqPfsmc.combogrid("option", "onChange", function (e, data) {
                        var text = data.text;
                        var value = data.value;
                        ui.setFormData({PFSMC: text, PFSBM: value});
                    });
                    //产地名称下拉列表
                    jqCdmc.combogrid("option", "onChange", function (e, data) {
                        var text = data.text;
                        var value = data.value;
                        ui.setFormData({CDMC: text, CDBM: value});
                    });
                }

                ui.addCallback("getJcxx", function (res) {
                    if (null == res) return;
                    var rowData = res.jcxxId;
                    if (null == rowData) return;
                    //初始化form表单
                    $(ui.uiForm).form();
                    var data = {
                        "PFSMC": rowData.PFSMC,
                        "YSCPH": rowData.YSCPH,
                        "JCRQ": rowData.JCRQ,
                        "JCHWZL": rowData.JCHWZL
                    };
                    ui.uiForm.form("loadData", data);
                });
                ui._init = function () {
                    var lhbhData = $.loadJson($.contextPath + '/jclhxz!getJclhbh.json');//生成进场理货编号
                    var jsonData = $.loadJson($.contextPath + '/qyda!getQyda.json?prefix=PC');
                    ui.setFormData({
                        PFSCBM: jsonData.pfscbm,
                        PFSCMC: jsonData.pfscmc,
                        JCLHBH: lhbhData
                    });
                    var jsonData = $.loadJson($.contextPath + "/jyzxx!getJyzxx.json?xtlx=PC&zt=1");
                    var pfsmc = ui.getItemJQ("PFSMC");
                    pfsmc.combogrid("reload", jsonData.data);
                }
                ui.addOutputValue("setJyzxxColumns", function (o) {
                    return {
                        status: true
//			status:true,
//			P_columns:"EQ_C_B_ZT≡1;EQ_C_B_XTLX≡PC"
                    }
                });
                //保存
                ui.clickSaveAll = function () {
                    //校验理货重量和进场重量
                    var jchwzl = $("input[name='JCHWZL']", this.uiForm).textbox("getValue");
                    var lhzzl = $("input[name='LHZZL']", this.uiForm).textbox("getValue");
                    if (!$.isNumeric(jchwzl)) jchwzl = 0;
                    if (!$.isNumeric(lhzzl)) lhzzl = 0;
                    if (parseFloat(jchwzl, 10) < parseFloat(lhzzl, 10)) {
                        $.message({message: "理货总重量超出进场货物重量，请核查后重新填写!", cls: "warning"});
                        return false;
                    }
                    var dGrid = ui.getDetailGrid();
                    if (!dGrid) return;
                    var jqForm = $("form", ui.uiForm),
                            rowData = dGrid.toFormData(),
                            formData, url;
                    // 表单检验
                    if (!jqForm.form("valid")) return false;
                    // 检验
                    if (!rowData.length) {
                        CFG_message("请先添加明细列表数据再保存！", "warning");
                        return false;
                    }
                    // 获取表单数据
                    formData = jqForm.form("formData", false);
                    // 向列表添加数据
                    // 重置表单数据
                    url = ui.getAction() + "!saveAll.json?P_tableId=" + ui.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
                    //console.log("master: " + $.config.toString(formData));
                    //console.log("detail: " + $.config.toString(rowData));

                    $.ajax({
                        url: url,
                        type: "post",
                        data: {
                            E_entityJson: $.config.toString(formData),
                            E_dEntitiesJson: $.config.toString(rowData),
                            barCode: barCode == null ? "" : barCode
                        },
                        dataType: "json",
                        async: false,
                        success: function (rlt) {
                            if (rlt.success) {
                                barCode = null;
                                jqForm.form("loadData", rlt.data.master);
                                dGrid.clearGridData();
                                dGrid.addRowData(rlt.data.detail);
                                CFG_message("保存成功！", "success");
                                //清除卡交易信息
                                if (isSwt) {
                                    var traceCodeList = new Array();
                                    for (var i = 0; i < rowData.length; i++) {
                                        traceCodeList.push(rowData[i].JYPZH);
                                    }
                                    _window("clearBusinessDetail", JSON.stringify(traceCodeList));
                                }
                            } else {
                                CFG_message(rlt.message, "warning");
                            }
                        },
                        error: function () {
                            CFG_message("保存主从表数据失败！", "error");
                        }
                    });//*/
                }

                ui.addCallback("setComboGridValue_Pfsmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    ui.setFormData({PFSMC: rowData.A_JYZMC, PFSBM: rowData.B_JYZBM});
                });

                ui.addCallback("setComboGridValue_Cdmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    ui.setFormData({CDMC: rowData.CDMC, CDBM: rowData.CDBM});
                });

                function setPfsxx(jyzbm, barCode) {//根据读码信息获取本市场内该经营者信息
                    var pfscbm = ui.getItemJQ("PFSCBM").textbox("getValue");
                    var jyzxx = $.loadJson($.contextPath + '/jyzxx!getJyzxxByBmAndQybm.json?jyzbm=' + jyzbm);
                    if ("FATAL" == jyzxx.result) {
                        CFG_message("批发商信息错误，请录入！", "warning");
                    } else if ("SUCCESS" == jyzxx.result) {
                        ui.setFormData({
                            PFSMC: jyzxx.JYZMC,
                            PFSBM: jyzxx.JYZBM
                        });
                    } else {
                        var id = jyzxx.ID;
                        var createJyz = $.loadJson($.contextPath + '/jyzxx!createJyz.json?id=' + id + "&qybm=" + pfscbm + "&xltx=PC&barCode=" + barCode);
                        ui.setFormData({
                            PFSMC: jyzxx.JYZMC,
                            PFSBM: jyzxx.JYZBM
                        });
                        CFG_message({
                            message: "已添加经营者" + jyzxx.JYZMC + "！",
                            position: {at: "center top+210"}
                        }, "success");
                    }
                }

                function checkSpxx(spxxArray) {
                    var spbmArray = new Array();
                    var addedSpmcArray = new Array();
                    for (var i = 0; i < spxxArray.length; i++) {
                        spbmArray.push(spxxArray[i].SPBM);
                    }
                    var exsitSp = $.loadJson($.contextPath + '/qyptmdspgl!checkSpid.json?spbm=' + JSON.stringify(spbmArray));
                    if (exsitSp != null && exsitSp.length != 0) {
                        for (var i = 0; i < spxxArray.length; i++) {
                            for (var j = 0; j < exsitSp.length; j++) {
                                if (spxxArray[i].SPBM != exsitSp[j].SPBM) {
                                    if (j == exsitSp.length - 1) {
                                        addedSpmcArray.push(spxxArray[i].SPMC);
                                    }
                                    continue;
                                }
                                break;
                            }
                        }
                    } else {
                        for (var i = 0; i < spxxArray.length; i++) {
                            addedSpmcArray.push(spxxArray[i].SPMC);
                        }
                    }

                    if (addedSpmcArray.length > 0) {
                        CFG_message("已添加商品" + JSON.stringify(addedSpmcArray) + "至系统", "success");
                    }
                }

            } else if (ui.options.number == 2) {
                var masterForm = ui.getMasterForm();
                var masterFormData = $('form', masterForm.uiForm).form('formData', false);
                /**
                 *页面初始化
                 */
                ui._init = function () {
                    var jqSpmc = ui.getItemJQ("SPMC");
                    var jsonData = $.loadJson($.contextPath + "/cpfl!getCpflGrid.json");
                    console.log(jsonData);
                    jqSpmc.combogrid("reload", jsonData.data);
                    var jhpchData = $.loadJson($.contextPath + '/jclhxz!getJhpch.json');
                    ui.setFormData({
                        JHPCH: jhpchData
                    });
                }
                ui.bindEvent = function () {
                    //商品名称的下拉列表
                    var jqSpbm = $("#SPMC_" + ui.timestamp);
                    var je = $("input[name='JE']", this.uiForm),
                            zl = ui.getItemJQ("ZL"),
                            dj = ui.getItemJQ("DJ"),
                            js = ui.getItemJQ("JS");
                    jqSpbm.combogrid("option", "onChange", function (e, data) {
                        var text = data.text;
                        var value = data.value;
                        ui.setFormData({SPMC: text, SPBM: value});
                    });
                    zl.textbox("option", "onChange", function (e, data) {
                        var djValue = dj.textbox("getValue"),
                                zlValue = data.value,
                                jeValue,
                                jsValue = js.textbox("getValue");
                        if (jsValue != "") {
                            CFG_message("重量与件数只能输入一项","warning");
                            js.textbox("setValue","");
                        }
                        if (!$.isNumeric(djValue)) djValue = 0;
                        if (!$.isNumeric(zlValue)) zlValue = 0;
                        jeValue = parseFloat(djValue, 10) * parseFloat(zlValue, 10);
                        je.textbox("setValue", Math.round(jeValue * 100) / 100);
                    });
                    js.textbox("option", "onChange", function (e, data) {
                        var djValue = dj.textbox("getValue"),
                                jsValue = data.value,
                                jeValue,
                                zlValue = zl.textbox("getValue");
                        if (zlValue != "") {
                            CFG_message("重量与件数只能输入一项","warning");
                            zl.textbox("setValue","");
                        }
                        if (!$.isNumeric(djValue)) djValue = 0;
                        if (!$.isNumeric(jsValue)) jsValue = 0;
                        jeValue = parseFloat(djValue, 10) * parseFloat(jsValue, 10);
                        je.textbox("setValue", Math.round(jeValue * 100) / 100);
                    });
                    dj.textbox("option", "onChange", function (e, data) {
                        var zlValue = zl.textbox("getValue"),
                                djValue = data.value,
                                jeValue,
                                jsValue = js.textbox("getValue");
                        if (zlValue != "") {
                            if (!$.isNumeric(djValue)) djValue = 0;
                            if (!$.isNumeric(zlValue)) zlValue = 0;
                            jeValue = parseFloat(djValue, 10) * parseFloat(zlValue, 10);
                        } else if (jsValue != "") {
                            if (!$.isNumeric(djValue)) djValue = 0;
                            if (!$.isNumeric(jsValue)) jsValue = 0;
                            jeValue = parseFloat(djValue, 10) * parseFloat(jsValue, 10);
                        } else {
                            jeValue = 0;
                        }
                        je.textbox("setValue", Math.round(jeValue * 100) / 100);
                    });
                }

                ui.clickAdd = function () {
                    var masterForm = ui.getMasterForm();
                    var masterFormData = $('form', masterForm.uiForm).form('formData', false);
                    var cGrid = this.getSelfGrid(), // 对应列表
                            jqForm = $("form", this.uiForm),
                            formData;
                    // 表单检验
                    if (!jqForm.form("valid")) return false;
                    // 获取表单数据
                    formData = jqForm.form("formData", false);
                    //进货批次号
                    var jhpch = formData.JHPCH;
                    //写入进货批次号
                    //formData.JHPCH = masterFormData.JHPCH;
                    // 向列表添加数据
                    cGrid.addRowData(formData);
                    // 重置表单数据
                    ui.clickCreate();
                    //重新添加进货批次号
                    ui.setFormData({JHPCH: jhpch});
                    //更新主表单理货总重量和总金额
                    var rowData = cGrid.getRowData(), zl, je,js, lhzzl = 0, lhzje = 0,lhzjs = 0;
                    for (var i = 0, len = rowData.length; i < len; i++) {
                        zl = rowData[i].ZL;
                        je = rowData[i].JE;
                        js = rowData[i].JS;
                        if (!$.isNumeric(zl)) zl = 0;
                        if (!$.isNumeric(je)) je = 0;
                        if (!$.isNumeric(js)) js = 0;
                        lhzzl += parseInt(zl, 10);
                        lhzje += parseInt(je, 10);
                        lhzjs += parseInt(js, 10);
                    }
                    masterForm.setFormData({LHZZL: lhzzl, LHZJE: lhzje,LHZJS: lhzjs});
                };
                //删除功能
                ui.clickSecondDev = function (id) {
                    if (id == $.config.preButtonId + "del") {
                        var cGrid = this.getSelfGrid();
                        var rowIdArr = cGrid.getSelectedRowId();
                        if (rowIdArr === null || 0 === rowIdArr.length) {
                            $.message({message: "请选择记录!", cls: "warning"});
                            return;
                        }
                        for (var i = rowIdArr.length - 1; i > -1; i--) {
                            cGrid.uiGrid.grid("delRowData", rowIdArr[i]);
                        }
                        //更新主表单理货总重量和总金额
                        var rowData = cGrid.getRowData(), zl, je, lhzzl = 0, lhzje = 0;
                        for (var i = 0, len = rowData.length; i < len; i++) {
                            zl = rowData[i].ZL;
                            je = rowData[i].JE;
                            if (!$.isNumeric(zl)) zl = 0;
                            if (!$.isNumeric(je)) je = 0;
                            lhzzl += parseInt(zl, 10);
                            lhzje += parseInt(je, 10);
                        }
                        masterForm.setFormData({LHZZL: lhzzl, LHZJE: lhzje});
                    }
                };

                ui.addCallback("setComboGridValue_Spmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    ui.setFormData({SPMC: rowData.SPMC, SPBM: rowData.SPBM});
                });
            }

            function showDialog() {
                var _this = ui;
                var jqGlobal = $(ui.options.global);
                var jqUC = $("<div id=\"jqUC\"></div>").appendTo(jqGlobal);
                jqUC.dialog({
                    appendTo: jqGlobal,
                    modal: true,
                    title: "请输入条码",
                    width: 240,
                    height: 80,
                    resizable: false,
                    position: {
                        at: "center top+200"
                    },
                    onClose: function () {
                        jqUC.remove();
                        jqUC.remove();
                    },
                    onCreate: function () {
                        var jqDiv = $("<div class=\"app-inputdiv-full\" style=\"padding:10px 20px;\"></div>").appendTo(this);
                        var jq = $("<input id=\"UNTREAD_OPINION_" + _this.uuid + "\" name=\"opinion\"></textarea>").appendTo(jqDiv);
                        jq.textbox({width: 200});
                        jq.textbox("option", "onKeyDown", function (e, data) {
                            if (e.keyCode == '13') {//添加回车事件
                                $('#sure').trigger("click");
                            }
                        });
                    },
                    onOpen: function () {
                        _this.close(false);
                        var jqPanel = $(this).dialog("buttonPanel").addClass("app-bottom-toolbar"),
                                jqDiv = $("<div class=\"dialog-toolbar\">").appendTo(jqPanel);
                        jqDiv.toolbar({
                            data: ["->", {id: "sure", label: "确定", type: "button"}, {
                                id: "cancel",
                                label: "取消",
                                type: "button"
                            }],
                            onClick: function (e, ui) {
                                if ("sure" === ui.id) {
                                    barCode = $("#UNTREAD_OPINION_" + _this.uuid).val();
                                    setFormData(barCode);
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

            function setFormData(barCode) {
                prefix = barCode.substring(0, 2);
                var detailForm = ui.getDetailForm();
                var detailGrid = ui.getDetailGrid();
                if ("ZZ" === prefix) {
                    var jsonData = $.loadJson($.contextPath + '/jclhxz!getZzccxx.json?zztmh=' + barCode);
                    if ("error" === jsonData.result) {
                        CFG_message("请输入有效的条形码!", "warning");
                    } else {
                        var ccxx = jsonData.ccData;
                        var qyxx = jsonData.qyData;
                        //加载主表数据
                        ui.setFormData({
                            LHZZL: ccxx.ZZL,
                            CDBM: qyxx.LSXZQHDM,
                            CDMC: qyxx.LSXZQ,
                        });
                        setPfsxx(qyxx.QYBM, barCode);
                        //添加散货明细
                        var shxx = jsonData.shData;
                        detailGrid.clearGridData();
                        if (shxx != undefined) {
                            for (var i = 0; i < shxx.length; i++) {
                                var sh = shxx[i];
                                //加载从表列表数据
                                var detailForm = {
                                    SPMC: sh.PZ,
                                    SPBM: sh.PLBH,
                                    ZL: sh.CCZL,
                                    JYPZH: sh.CPZSM,
                                    JHPCH: sh.ZSPCH
                                }
                                detailGrid.addRowData(detailForm);
                            }
                        }
                        //添加产品出场明细
                        var cpxx = jsonData.cpData;
                        if (cpxx != undefined) {
                            for (var i = 0; i < cpxx.length; i++) {
                                var cp = cpxx[i];
                                //加载从表列表数据
                                var detailForm = {
                                    SPMC: cp.CPMC,
                                    SPBM: cp.CPBH,
                                    JS: cp.CCJS,
                                    JHPCH: cp.ZSPCH,
                                    JYPZH: cp.ZSM,
                                }
                                detailGrid.addRowData(detailForm);
                            }
                        }
                        //更新主表理货重量 金额
                        var rowData = detailGrid.getRowData(), zl, je, js, lhzzl = 0, lhzje = 0, lhzjs = 0;
                        for (var i = 0, len = rowData.length; i < len; i++) {
                            zl = rowData[i].ZL;
                            je = rowData[i].JE;
                            js = rowData[i].JS;
                            if (!$.isNumeric(zl)) zl = 0;
                            if (!$.isNumeric(je)) je = 0;
                            if (!$.isNumeric(js)) js = 0;
                            lhzzl += parseInt(zl, 10);
                            lhzje += parseInt(je, 10);
                            lhzjs += parseInt(js, 10);
                        }
                        ui.setFormData({
                            LHZZL: lhzzl,
                            LHZJE: lhzje,
                            LHZJS: lhzjs,
                            JHPCH: ""//to be deleted
                        });
                    }
                } else if ("PC" === prefix) {
                    var jsonData = $.loadJson($.contextPath + '/jclhxz!getPcjyxx.json?pctmh=' + barCode);
                    //根据批发市场出场条码进行 经营者信息判断
                    if ("ERROR" === jsonData) {
                        CFG_message("请输入有效的条形码!", "warning");
                    } else {

                        var lhxx = jsonData.lhxx;
                        var jymxxx = jsonData.jymxxx;
                        var pfsbm = lhxx.LSSBM;
                        if (jymxxx != null && jymxxx.length > 0) {
                            var spxxArray = new Array();
                            for (var i = 0; i < jymxxx.length; i++) {
                                var spxx = {};
                                spxx.SPMC = jymxxx[i].SPMC;
                                spxx.SPBM = jymxxx[i].SPBM;
                                spxxArray.push(spxx);
                            }
//					checkSpxx(spxxArray);//todo 海岛田园的包装产品目前无法进行商品同步
                        }
                        setPfsxx(pfsbm, barCode);
                        //加载主表数据
                        ui.setFormData({
                            CDZMH: lhxx.CDZMH,
                            YSCPH: lhxx.YSCPH,
                            SCJD: lhxx.SCJD,//
                            CDMC: lhxx.CDMC,
                            CDBM: lhxx.CDBM
                        });
                        detailGrid.clearGridData();
                        //加载从表列表数据
                        for (var i = 0; i < jymxxx.length; i++) {
                            detailForm.SPMC = jymxxx[i].SPMC;
                            detailForm.SPBM = jymxxx[i].SPBM;
                            if (jymxxx[i].ZL != "" && jymxxx[i].ZL != null && jymxxx[i].ZL != undefined) {
                                detailForm.ZL = jymxxx[i].ZL;
                                detailForm.JS = "";
                            }
                            if (jymxxx[i].JS != "" && jymxxx[i].JS != null && jymxxx[i].JS != undefined) {
                                detailForm.JS = jymxxx[i].JS;
                                detailForm.ZL = "";
                            }
                            detailForm.DJ = jymxxx[i].DJ;
                            detailForm.JE = jymxxx[i].JE;
                            detailForm.JHPCH = jymxxx[i].JHPCH;
                            detailForm.JYPZH = jymxxx[i].ZSM;
                            detailForm.GHPFSCBM = lhxx.PFSCBM;
                            detailForm.GHPFSCMC = lhxx.PFSCMC;
                            detailGrid.addRowData(detailForm);
                        }
                        //更新主表理货重量 金额
                        var rowData = detailGrid.getRowData(), zl, je, js, lhzzl = 0, lhzje = 0, lhzjs = 0;
                        for (var i = 0, len = rowData.length; i < len; i++) {
                            zl = rowData[i].ZL;
                            je = rowData[i].JE;
                            js = rowData[i].JS;
                            if (!$.isNumeric(zl)) zl = 0;
                            if (!$.isNumeric(je)) je = 0;
                            if (!$.isNumeric(js)) js = 0;
                            lhzzl += parseInt(zl, 10);
                            lhzje += parseInt(je, 10);
                            lhzjs += parseInt(js, 10);
                        }
                        ui.setFormData({
                            LHZZL: lhzzl,
                            LHZJE: lhzje,
                            LHZJS: lhzjs,
                            JHPCH: ""//to be deleted
                        });
                    }
                } else {
                    CFG_message("请输入有效的条形码!", "warning");
                    return;
                }
            }
        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            var masterForm = ui.getMasterForm();
            ui.eSelectRow = function (e, data) {
                return null;
            };
        };
        /**
         *  二次开发：复写自定义树
         */
        function _override_tree(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tree!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tbar!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            if (ui.options.number == 1) {
                ui.processItems = function (data) {
                    var btns = [];
//                    btns.push({
//                        id: $.config.preButtonId + "dk",
//                        label: "读卡",
//                        type: "button"
//                    });
                    btns.push('->',{
                        id: $.config.preButtonId + "inputBarCode",
                        label: "输入条码",
                        type: "button"
                    });
                    for (var i = 2; i < data.length; i++) {//忽略'','->'
                        btns.push(data[i]);
                    }
                    return btns;
                };
            } else if (ui.options.number == 2) {
                ui.processItems = function (data) {
                    var btns = [];
                    for (var i = 0; i < data.length; i++) {
                        btns.push(data[i]);
                    }
                    btns.push({
                        id: $.config.preButtonId + "del",
                        label: "删除",
                        type: "button"
                    });
                    return btns;
                };
            }
        };


        /**
         * 在此可以复写所有自定义JS类
         * @param selector
         * @returns {JQ_override}
         */
        window[CFG_overrideName(subffix)] = function () {
            if (this instanceof $.config.cform) {
                _override_form(this);
            } else if (this instanceof $.config.cgrid) {
                _override_grid(this);
            } else if (this instanceof $.config.ctree) {
                _override_tree(this);
            } else if (this instanceof $.config.ctbar) {
                _override_tbar(this);
            }
        };


    })("${timestamp}");
</script>
