<%@page language="java" pageEncoding="UTF-8" %>
<script type="text/javascript">
    /***************************************************!
     * @author qiucs
     * @date   2014-7-15
     * 系统配置平台应用自定义二次开发JS模板
     ***************************************************/


    (function (subffix) {
        var zlJson;//理货信息
        var yForm; //主表表单
        var _syzl;
        var _syjs;
        /**
         * 二次开发定位自己controller
         * @returns {String}
         **/
        window[CFG_actionName(subffix)] = function () {
            // this.assembleData 就是 configInfo
            return $.contextPath + "/jiaoyixinxixinzeng";
        };

        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            //主表表单区域
            if (1 == ui.options.number) {

                function setDdd(lssbm) {//设置到达地
                    var detailForm = ui.getDetailForm();
                    var jqDdd = detailForm.getItemJQ("DDD");

                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getDddByLssbm.json?lssbm=" + lssbm);
                    jqDdd.combogrid("setValue", jsonData);
                }


                ui.clickSecondDev = function (id) {
                    if (id == $.config.preButtonId + "print") {//打印按钮
                     if (isSwt) {
                            var dGrid = this.getDetailGrid();
                            if (!dGrid) return;
                            var jqForm = $("form", this.uiForm),
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
                            url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
                            $.ajax({//保存数据库成功后写码
                                url: url,
                                type: "post",
                                data: {
                                    E_entityJson: $.config.toString(formData),
                                    E_dEntitiesJson: $.config.toString(rowData)
                                },
                                dataType: "json",
                                success: function (rlt) {
                                    if (rlt.success) {
                                        jqForm.form("loadData", rlt.data.master);
                                        var detailArray = [];
                                        var length = rowData.length;
                                        for (var i = 0; i < length; i++) {
                                                data = {
                                                tracecode: rowData[i].ZSM,//追溯码
                                                productName: rowData[i].SPMC,
                                                price: rowData[i].DJ,
                                                weight: rowData[i].ZL
                                            };
                                            detailArray.push(data);
                                        }
                                        var date = formData.JYRQ;
                                        var detail = {
                                            code: formData.JYTMH,
                                            marketName: formData.PFSCMC,
                                            sellerName: formData.PFSMC,
                                            buyer: formData.LSSMC,
                                            details: detailArray
                                        }
                                        var result = _window("printDetailWithBarCode", JSON.stringify(detail));
                                        dGrid.clearGridData();
                                        dGrid.addRowData(rlt.data.detail);
                                        CFG_message("保存成功！", "success");
                                    } else {
                                        CFG_message(rlt.message, "warning");
                                        return;
                                    }
                                },
                                error: function () {
                                    CFG_message("保存主从表数据失败！", "error");
                                    return;
                                }
                            });// */
                        } else {
                            $.alert("请在程序中操作");
                        }

                    }
                    if (id == $.config.preButtonId + "writeOneCodeSave" || id == $.config.preButtonId + "writeOneCardSave") {
                        if (isSwt || isNewSwt) {
                            var dGrid = this.getDetailGrid();
                            if (!dGrid) return;
                            var jqForm = $("form", this.uiForm),
                                    rowData = dGrid.toFormData(),
                                    formData, url;
                            // 表单检验
                            if (!jqForm.form("valid")) return false;
                            // 检验
                            if (!rowData.length) {
                                CFG_message("请先添加明细列表数据再保存！", "warning");
                                return false;
                            }

                            if(id == $.config.preButtonId + "writeOneCardSave"){
                                var jytmhJQ = ui.getItemJQ("JYTMH");
                                try{
                                    //错误的调用
                                    var writeResult = _card.write(jytmhJQ.textbox("getValue"));
                                }catch(e){
                                    //显示异常的详细信息
                                    $.alert("写卡出错，请检查设备！");
                                    return false;
                                }
                            }
                            // 获取表单数据
                            formData = jqForm.form("formData", false);
                            url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
                            $.ajax({//保存数据库成功后写码
                                url: url,
                                type: "post",
                                data: {
                                    E_entityJson: $.config.toString(formData),
                                    E_dEntitiesJson: $.config.toString(rowData)
                                },
                                dataType: "json",
                                success: function (rlt) {
                                    if (rlt.success) {
                                        jqForm.form("loadData", rlt.data.master);
                                        var detailArray = [];
                                        var length = rowData.length;
                                        for (var i = 0; i < length; i++) {
                                            var data = {};
                                            if (rowData[i].ZL != "" && rowData[i] != undefined && rowData[i] != null) {
                                                var totalPrice = rowData[i].DJ * rowData[i].ZL;
                                                data = {
                                                    item:i+1,
                                                    tracecode: rowData[i].ZSM,//追溯码
                                                    urltracecode:"http://www.zhuisuyun.net/" + rowData[i].ZSM,//追溯码
                                                    productName: rowData[i].SPMC,
                                                    price: rowData[i].DJ,
                                                    weight: rowData[i].ZL,
                                                    totalPrice:totalPrice
                                                };
                                            } else {
                                                var totalPrice = rowData[i].DJ * rowData[i].JS;
                                                data = {
                                                    item:i+1,
                                                    tracecode: rowData[i].ZSM,//追溯码
                                                    urltracecode:"http://www.zhuisuyun.net/" + rowData[i].ZSM,//追溯码
                                                    productName: rowData[i].SPMC,
                                                    price: rowData[i].DJ,
                                                    quantity: rowData[i].JS,
                                                    totalPrice:totalPrice
                                                };
                                            }
//                                            data = {
//                                                tracecode: rowData[i].ZSM,//追溯码
//                                                productName: rowData[i].SPMC,
//                                                price: rowData[i].DJ,
//                                                weight: rowData[i].ZL
//                                            };
                                            detailArray.push(data);
                                        }
                                        var date = formData.JYRQ;
                                        var detail = {
                                            code: formData.JYTMH,
                                            marketName: formData.PFSCMC,
                                            sellerName: formData.PFSMC,
                                            buyer: formData.LSSMC,
                                            details: detailArray
                                        }
                                        if(id == $.config.preButtonId + "writeOneCodeSave"){
                                            if(isSwt){
                                                var result = _window("printDetailWithBarCode", JSON.stringify(detail));
                                            }else if(isNewSwt){
                                                var totalWeight = 0;
                                                var totalQuantity = 0;
                                                var totalPrice = 0;
                                                $.each(detailArray,function(e,data){
                                                    if(parseFloat(data.quantity) > 0){
                                                        totalQuantity += parseFloat(data.quantity);
                                                    }else if(parseFloat(data.weight) > 0){
                                                        totalWeight += parseFloat(data.weight);
                                                    }
                                                    totalPrice += data.totalPrice;
                                                });
                                                var d = new Date();
                                                var buyDate = d.getFullYear() + "年" + (d.getMonth() + 1) + "月" + d.getDate() + "日";
                                                var _result = _print.print("pcccsfd",{
                                                    "marketName":formData.PFSCMC,
                                                    "sellerName": formData.PFSMC,
                                                    "buyer": formData.LSSMC,
                                                    "buyDate":buyDate,
                                                    "detailArray":detailArray,
                                                    "totalWeight":totalWeight,
                                                    "totalQuantity":totalQuantity,
                                                    "totalPrice":totalPrice,
                                                    "ZSM":detail.code
                                                });
                                            }
                                        }

                                        dGrid.clearGridData();
                                        dGrid.addRowData(rlt.data.detail);
                                        CFG_message("保存成功！", "success");
                                    } else {
                                        CFG_message(rlt.message, "warning");
                                        return;
                                    }
                                },
                                error: function () {
                                    CFG_message("保存主从表数据失败！", "error");
                                    return;
                                }
                            });// */
                        } else {
                            $.alert("请在程序中操作");
                        }

                    }
                    if (id == $.config.preButtonId + "readCard") {//读卡按钮
                        if (isSwt) {
                            var result = _window("readCard", "userInfo");
                            var resultData = JSON.parse(result);
                            var value = resultData.value;
                            var jqLssmcCombogrid = ui.getItemJQ("LSSMC");
                            if (resultData.status == "true" || resultData.status == true) {
                                ui.setFormData({
                                    LSSBM: value.userId,
                                    LSSMC: value.userName
                                });
                                setDdd(value.userId);
                                $.message({message: "读卡成功", cls: "info"});
                            } else {
                                $.message({message: "读卡失败，请重试", cls: "warning"});
                            }
                        }
                        else {
                            $.alert("请在程序中操作");
                        }
                    }
                };

                ui._init = function () {
                    yForm = ui;
                    $.ajax({
                        type: 'post',
                        url: $.contextPath + "/jiaoyixinxixinzeng!getLsh.json",
                        //data:{Zl:"交易编号",Qz:5310,Cd:"11"},
                        data: {Zl: "JYTMH"},
                        dataType: 'json',
                        success: function (data) {
                            ui.setFormData({JYTMH: "PC" + data});
                        }
                    });
                    var jybh = $("input[name='JYBH']", ui.uiForm);
                    jybh.textbox({
                        readonly: true
                    });
                    //加载进场理货编号数据
                    var jqJclhbh = ui.getItemJQ("JCLHBH");
                    var jclhbhData = $.loadJson($.contextPath + "/jcxx!getJclhbhByPfsbm.json?pfsbm=");
                    jqJclhbh.combogrid("reload", jclhbhData);
                    //获取批发市场名称和编码
                    var jsonData = $.loadJson($.contextPath + '/qyda!getQyda.json?prefix=PC');
                    ui.setFormData({
                        PFSCMC: jsonData.pfscmc,
                        PFSCBM: jsonData.pfscbm
                    });
                    jsonData = $.loadJson($.contextPath + "/jyzxx!getJyzxx.json?xtlx=PC&zt=1");
                    var pfsmc = ui.getItemJQ("PFSMC");
                    var jqLssmc = ui.getItemJQ("LSSMC");
                    pfsmc.combogrid("reload", jsonData.data);
                    jqLssmc.combogrid("reload", jsonData.data);
                }
                function changePfs() {
                    var dGrid = ui.getDetailGrid();
                    dGrid.clearGridData();
                    var jqDdd = $("#DDD_" + ui.timestamp);
                    jqDdd.textbox("setValue", "");
                    ui.setFormData({ZZL: "", ZJE: "", SPMC: "", SPBM: "", ZL: "", DJ: "", JE: "", DDD: ""});

                }

                ui.bindEvent = function () {//事件方法
                    var jqPfsmc = ui.getItemJQ("PFSMC"), //$("#PFSMC_" + ui.timestamp),//批发商名称
                            jqJclhbh = $("#JCLHBH_" + ui.timestamp),//进场理货编号
                            jqLssmc = $("#LSSMC_" + ui.timestamp),//零售商名称
                            jqPfsbm = $("#PFSBM_" + ui.timestamp),//批发商编码
                            jqLssbm = $("#LSSBM_" + ui.timestamp);//零售商编码
                    jqPfsmc.combogrid("option", "onChange", function (e, data) {//批发商名称下拉框事件
                        var detailForm = ui.getDetailForm();
                        var jqSpmc = detailForm.getItemJQ("SPMC");
                        var newValue = data.value;
                        var newText = data.text;
                        var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getJclhbhByPfsbm.json?pfsbm=" + newValue);
                        ui.setFormData({JCLHBH: ""});
                        jqSpmc.combogrid("setValue", "");
                        jqJclhbh.combogrid("reload", jsonData);
                        var lsUrl = $.contextPath + "/jyzxx!getJyzxx.json?xtlx=PC&zt=1&jyzbm=" + newValue;
                        jqLssmc.combogrid("reload", lsUrl);
                        ui.setFormData({PFSMC: newText, PFSBM: newValue, LSSMC: ""});
                        changePfs();
                    })
                    jqJclhbh.combogrid("option", "onChange", function (e, data) {//进场理货编号下拉框事件
                        var detailFrom = ui.getDetailForm();
                        var jqSpmc = $("#SPMC_" + detailFrom.timestamp);//商品名称
                        var newValue = data.value;
                        var newText = data.text;
                        var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getSpmcByPid.json?jclhid=" + newValue);
                        var jsonPfs = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getPfsmcByJclhbh.json?jclhbh=" + newText);
                        var pfs = jsonPfs[0];
                        jqLssmc = $("#LSSMC_" + ui.timestamp),//零售商名称
                                jqSpmc.combogrid("setValue", "");
                        jqSpmc.combogrid("reload", jsonData);
                        ui.setFormData({JCLHBH: newText, PFSMC: pfs.PFSMC, PFSBM: pfs.PFSBM});
                        var lsUrl = $.contextPath + "/jyzxx!getJyzxx.json?xtlx=PC&zt=1&jyzbm=" + pfs.PFSBM;
                        jqLssmc.combogrid("reload", lsUrl);
//                zlJson = $.loadJson($.contextPath+"/jiaoyixinxixinzeng!getJclihzl.json?jclhbh="+newText);
//				ui.setFormData({SYZL:zlJson.SYZL});
                    })
                    jqLssmc.combogrid("option", "onChange", function (e, data) {
                        var lssmc = data.text;
                        var lssbm = data.value;
                        ui.setFormData({LSSBM: lssbm, LSSMC: lssmc});
                        setDdd(lssbm);
                    })

                }
                //保存并写卡
                ui.clickSaveAll = function () {
                    var dGrid = this.getDetailGrid();
                    if (!dGrid) return;
                    var jqForm = $("form", this.uiForm),
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
                    url = this.getAction() + "!saveAll.json?P_tableId=" + this.options.tableId + "&P_D_tableIds=" + dGrid.options.tableId;
                    //追溯码list
                    var traceCodeList = new Array();
                    for (var i = 0; i < rowData.length; i++) {
                        traceCodeList.push(rowData[i].ZSM);
                    }
                    //写卡
                    if (isSwt) {
                        var length = rowData.length;
                        var detailArray = new Array();
                        var cdxx = $.loadJson($.contextPath + '/jiaoyixinxixinzeng!getCdxxByJclhbh.json?jclhbh=' + formData.JCLHBH);
                        var food = {};
                        for (var i = 0; i < length; i++) {
                            food[rowData[i].SPBM] = rowData[i].SPMC;
                            var data = {
                                tracecode: rowData[i].ZSM,//追溯码
                                batchCode: rowData[i].JHPCH,//批次号
                                sellerCode: formData.PFSBM,
                                sellerName: formData.PFSMC,
                                produceLocName: cdxx.SCJD,//生产基地名称
                                businessMarketCode: formData.PFSCBM,
                                businessMarketName: formData.PFSCMC,
                                productCode: rowData[i].SPBM,
                                productPlaceCode: cdxx.CDBM,//产地编码
                                dealCount: 0,
                                dealWeight: rowData[i].ZL,
                                dealPrice: rowData[i].DJ,
                                carNo: cdxx.YSCPH
                            };
                            detailArray.push(data);
                        }
                        var date = formData.JYRQ;
                        var bussinessInfo = {
                            bDate: date.replace(/-/g, ""),
                            bCount: String(length),
                            bType: "?"
                        }
                        var result = _window("writeBusinessInfoAndDetailAndPrint", "2", JSON.stringify(bussinessInfo), JSON.stringify(detailArray), JSON.stringify(food));
                        var resultData = JSON.parse(result);
                        if (resultData.status == "true" || resultData.status == true) {
                            $.ajax({//写卡成功后保存数据库
                                url: url,
                                type: "post",
                                data: {
                                    E_entityJson: $.config.toString(formData),
                                    E_dEntitiesJson: $.config.toString(rowData)
                                },
                                dataType: "json",
                                success: function (rlt) {
                                    if (rlt.success) {
                                        jqForm.form("loadData", rlt.data.master);
                                        dGrid.clearGridData();
                                        dGrid.addRowData(rlt.data.detail);
                                        CFG_message("保存成功！", "success");
                                    } else {
                                        CFG_message(rlt.message, "warning");
                                        _window("clearBusinessInfoAndDetail", traceCodeList);
                                        return;
                                    }
                                },
                                error: function () {
                                    CFG_message("保存主从表数据失败！", "error");
                                    _window("clearBusinessInfoAndDetail", traceCodeList);
                                    return;
                                }
                            });// */
                            $.alert("写卡成功！：" + resultData.msg);
                        } else {
                            $.alert("写卡失败：" + resultData.msg);
                        }
                    } else {
                        $.alert("请在程序环境中写卡");
                    }
                }
                //回调函数 批发商名称，零售商名称，进场理货编号 弹出框数据加载
                ui.addCallback("setComboGridValue_Pfsmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    var detailFrom = ui.getDetailForm();
                    var jqSpmc = detailFrom.getItemJQ("SPMC");
                    var jqJclhbh = ui.getItemJQ("JCLHBH");
                    ui.setFormData({PFSMC: rowData.A_JYZMC, PFSBM: rowData.A_JYZBM});
                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getJclhbhByPfsbm.json?pfsbm=" + rowData.B_JYZBM);
                    ui.setFormData({JCLHBH: ""});
                    jqSpmc.combogrid("setValue", "");
                    jqJclhbh.combogrid("reload", jsonData);
                    changePfs();

                });
                ui.addCallback("setComboGridValue_Lssmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    ui.setFormData({LSSMC: rowData.A_JYZMC, LSSBM: rowData.A_JYZBM});
                    setDdd(rowData.A_JYZBM);
                });
                ui.addCallback("setComboGridValue_Jclhbh", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    var detailForm = ui.getDetailForm();
                    var jqSpmc = detailForm.getItemJQ("SPMC");
                    var jqLssmc = ui.getItemJQ("LSSMC");
                    ui.setFormData({
                        JCLHBH: rowData.JCLHBH,
                        PFSMC: rowData.PFSMC,
                        PFSBM: rowData.PFSBM
                    });
                    //加载商品名称下拉框数据
                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getSpmcByPid.json?jclhid=" + rowData.ID);
                    jqSpmc.combogrid("setValue", "");
                    jqSpmc.combogrid("reload", jsonData);
                    //过滤批发商
                    var lsUrl = $.contextPath + "/jingyingzhedangan!getFilterJyzda.json?jyzbm=" + rowData.PFSBM;
                    jqLssmc.combogrid("reload", lsUrl);

                });

                //出参
                ui.addOutputValue("setTcsJclhbm", function (o) {//弹出式进场理货编码
                    var pfsbm = $("#PFSBM_" + ui.timestamp).textbox("getValue");
                    var o = {
                        status: true,
                        P_columns: "LIKE_C_PFSBM≡" + pfsbm
                    }
                    return o;
                });
                //出参
                ui.addOutputValue("setTcsLssmc", function (o) {//弹出式零售商编码
                    var pfsbm = $("#PFSBM_" + ui.timestamp).textbox("getValue");
                    if (pfsbm == "") {
                        var o = {
                            status: true,
                            P_columns: "EQ_C_B_ZT≡1;EQ_C_B_XTLX≡PC"
                        }
                        return o;
                    }
                    var o = {
                        status: true,
                        P_columns: "EQ_C_B_ZT≡1;EQ_C_B_XTLX≡PC;NOT_C_A_JYZBM≡" + pfsbm
                    }
                    return o;
                });

                ui.addOutputValue("setTcsJclhsp", function (o) {//弹出式进场理货商品
                    var masterForm = ui.getMasterForm();
                    var pfsbm = $("#PFSBM_" + ui.timestamp).textbox("getValue");
                    var o = {
                        status: true,
                        P_columns: "LIKE_C_PFSBM≡" + pfsbm
                    }
                    return o;
                });
            }

            //从表表单区域
            if (2 === ui.options.number) {
//                function setDdd(lssbm) {//设置到达地
//                    var detailForm = ui.getDetailForm();
//                    var jqDdd = detailForm.getItemJQ("DDD");
//
//                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getDddByLssbm.json?lssbm=" + lssbm);
//
//                    jqDdd.combogrid("setValue", jsonData);
//                }
                ui._init = function () {
                    var jqSpmc = ui.getItemJQ("SPMC");
                    var spmcData = $.loadJson($.contextPath + "/jcxx!getSpmcByPid.json?jclhid=");
                    jqSpmc.combogrid("reload", spmcData.data);
                    ui.getItemJQ("ZL").textbox({readonly: true});
                    ui.getItemJQ("JS").textbox({readonly: true});
                }
                ui.bindEvent = function () {
                    //计算金额
                    var jqPrice = $("input[name='DJ']", ui.uiForm),
                            jqWeight = $("input[name='ZL']", ui.uiForm),
                            jqQuantity = $("input[name='JS']", ui.uiForm),
                            jqAmount = $("input[name='JE']", ui.uiForm);
                    jqAmount.textbox({readonly: true});//设置只读
                    var dddJQ = ui.getItemJQ("DDD");
                    dddJQ.combogrid("option", "onChange", function (e, data) {
                        var newValue = data.value;
                        var newText = data.text;
                        ui.setFormData({DDD: newText, DDDBM: newValue});
                    });
                    jqPrice.textbox("option", "onChange", function (e, data) {
                        var weight = jqWeight.textbox("getValue"), quantity = jqQuantity.textbox("getValue"), price = data.value, amount;
                        if (!$.isNumeric(weight)) weight = 0;
                        if (!$.isNumeric(price)) price = 0;
                        if (quantity == 0 || quantity == undefined || quantity == "") {
                            amount = parseInt(weight, 10) * parseInt(price, 10);
                        } else {
                            amount = parseInt(quantity, 10) * parseInt(price, 10);
                        }
                        jqAmount.textbox("setValue", amount);
                    });
                    jqWeight.textbox("option", "onChange", function (e, data) {
                        var price = jqPrice.textbox("getValue"), weight = data.value, amount;
                        if (parseFloat(weight) > parseFloat(_syzl)) {
                            CFG_message("出场重量不得大于剩余数量(" + _syzl + ")", "warning");
                            jqWeight.textbox("setValue", _syzl);
                            weight = _syzl;
                        }
                        if (!$.isNumeric(price)) price = 0;
                        if (!$.isNumeric(weight)) weight = 0;
                        amount = parseInt(price, 10) * parseInt(weight, 10);
                        jqAmount.textbox("setValue", amount);
                    });
                    jqQuantity.textbox("option", "onChange", function (e, data) {
                        var price = jqPrice.textbox("getValue"), quantity = data.value, amount;
                        if (parseFloat(quantity) > parseFloat(_syjs)) {
                            CFG_message("出场件数不得大于剩余件数(" + _syjs + ")", "warning");
                            jqQuantity.textbox("setValue", _syjs);
                            quantity = _syjs;
                        }
                        if (!$.isNumeric(price)) price = 0;
                        if (!$.isNumeric(quantity)) quantity = 0;
                        amount = parseInt(price, 10) * parseInt(quantity, 10);
                        jqAmount.textbox("setValue", amount);
                    });

                    //商品名称onChange事件
                    //获取交易凭证号及进货批次号
                    var jqSpmc = $("#SPMC_" + ui.timestamp);
                    jqSpmc.combogrid("option", "onChange", function (e, data) {
                        var newText = data.text;
                        var newValue = data.value;
                        var masterForm = ui.getMasterForm();
                        var jqJclhbh = masterForm.getItemJQ("JCLHBH");
                        var jclhbh = jqJclhbh.combogrid("getValue");
                        var jsonId = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getIdByJclhbh.json?jclhbh=" + jclhbh);
                        //获取进货批次号及交易凭证号
                        var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getJhpch.json?jclhid=" + jsonId + "&spbm=" + newValue);
                        ui.setFormData({SPMC: newText, SPBM: newValue, JHPCH: jsonData.JHPCH, JYPZH: jsonData.JYPZH});
                        _syjs = jsonData.SYJS == null ? jsonData.JS : jsonData.SYJS;
                        _syzl = jsonData.SYZL == null ? jsonData.ZL : jsonData.SYZL
                        if ($.isNumeric(_syzl)) {
                            ui.getItemJQ("ZL").textbox({readonly: false});
                            ui.getItemJQ("JS").textbox({readonly: true});
                            ui.getItemJQ("JS").textbox("setValue", "");
                        } else if ($.isNumeric(_syjs)) {
                            ui.getItemJQ("ZL").textbox({readonly: true});
                            ui.getItemJQ("JS").textbox({readonly: false});
                            ui.getItemJQ("ZL").textbox("setValue", "");
                        }

                    });
                }
                //添加-按钮
                ui.clickAdd = function () {
                    var cGrid = ui.getSelfGrid(), // 对应列表
                            jqForm = $("form", ui.uiForm),
                            formData,
                            masterForm = ui.getMasterForm();
                    var cGridData = cGrid.getRowData();//列表所有数据
                    // 表单检验
                    if (!jqForm.form("valid")) return false;
                    // 获取表单数据
                    formData = jqForm.form("formData", false);
                    if (ui.getItemJQ("JS").textbox("option", "readonly") == true) {
                        if (formData.ZL == "" || parseFloat(formData.ZL) <= 0) {
                            CFG_message("请正确填写重量", "warning");
                            return false;
                        }
                    }
                    if (ui.getItemJQ("ZL").textbox("option", "readonly") == true) {
                        if (formData.JS == "" || parseFloat(formData.JS) <= 0) {
                            CFG_message("请正确填写件数", "warning");
                            return false;
                        }
                    }
                    var ddd = formData.DDD;
                    /////////////////////////////////////////////////////
//            var zzl = 0; todo 删除此段
//            var zje = 0;
//            if(cGridData != null && cGridData != ""){
//                $.each(cGridData,function(e,data){
//                    if(data.SPBM == formData.SPBM){
//                        zzl += parseInt(data.ZL);
//                        zje += parseInt(data.JS);
//                    }
//                });
//            }
//            var bool = false;
//			if(zlJson != null && zlJson != ""){
//				$.each(zlJson,function(e,data){
//					if(data.SPBM == formData.SPBM){
//						if((parseInt(zzl) + parseInt(formData.ZL))>parseInt(data.SYZL)){
//							$.message({message: "该商品出场重量大于进场重量!", cls: "warning"});
//							bool = true;
//						}
//					}
//				});
//			}
//
//            if(bool){
//                return;
//            }
                    /////////////////////////////////////////////////////
                    //生成追溯码
                    $.ajax({
                        type: 'post',
                        url: $.contextPath + "/jiaoyixinxixinzeng!getLsh.json",
                        data: {Zl: "ZSM"},
                        dataType: 'json',
                        success: function (data) {
                            formData.ZSM = data;
                            // 向列表添加数据
                            cGrid.addRowData(formData);
                            // 更改主表总金额及总重量
                            var rowData = cGrid.getRowData(), zl, je, js, zzl = 0, zje = 0, zjs = 0;
                            var length = rowData.length;
                            for (var i = 0; i < length; i++) {
                                zl = rowData[i].ZL;
                                je = rowData[i].JE;
                                js = rowData[i].JS;
                                if (!$.isNumeric(zl)) zl = 0;
                                if (!$.isNumeric(je)) je = 0;
                                if (!$.isNumeric(js)) js = 0;
                                zzl += parseInt(zl, 10);
                                zjs += parseInt(js, 10);
                                zje += parseInt(je, 10);
                            }
                            masterForm.setFormData({ZZL: zzl, ZJE: zje, ZJS: zjs});
//				   	var jqZzl = $("input[name='ZZL']",masterForm.uiForm);
//				   	var jqZje = $("input[name='ZJE']",masterForm.uiForm);
//				   	jqZzl.textbox({readonly:true});
//				   	jqZje.textbox({readonly:true});
                        }
                    });
                    // 重置表单数据
                    ui.clickCreate();
                    ui.setFormData({DDD: ddd});
                };
                ui.clickSecondDev = function (id) {
                    masterForm = ui.getMasterForm();

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
                        var rowData = cGrid.getRowData(), zl, je, js, zzl = 0, zje = 0, zjs = 0;
                        var length = rowData.length;
                        for (var i = 0; i < length; i++) {
                            zl = rowData[i].ZL;
                            je = rowData[i].JE;
                            js = rowData[i].JS;
                            if (!$.isNumeric(zl)) zl = 0;
                            if (!$.isNumeric(je)) je = 0;
                            if (!$.isNumeric(js)) js = 0;
                            zzl += parseInt(zl, 10);
                            zje += parseInt(je, 10);
                            zjs += parseInt(js, 10);
                        }
                        masterForm.setFormData({ZZL: zzl, ZJE: zje, ZJS: zjs});
                    }
                };

                ui.addCallback("setComboGridValue_Spmc", function (o) {
                    if (null == o) return;
                    var rowData = o.result;
                    if (null == rowData) return;
                    var masterForm = ui.getMasterForm();
                    var jqJclhbh = masterForm.getItemJQ("JCLHBH");
                    var jclhbh = jqJclhbh.combogrid("getValue");
                    var jsonId = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getIdByJclhbh.json?jclhbh=" + jclhbh);
                    //获取进货批次号
                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getJhpch.json?jclhid=" + jsonId + "&spbm=" + newValue);
                    ui.setFormData({SPMC: newText, SPBM: newValue, JHPCH: jsonData.JHPCH, JYPZH: jsonData.JYPZH});
                    //若第一次出场 取重量和件数字段 否则取剩余重量和剩余件数
                    _syjs = jsonData.SYJS == null ? jsonData.JS : jsonData.SYJS;
                    _syzl = jsonData.SYZL == null ? jsonData.ZL : jsonData.SYZL
                    if (_syjs == "" || _syjs == undefined || _syjs == 0) {
                        ui.getItemJQ("ZL").textbox({readonly: false, required: true});
                        ui.getItemJQ("JS").textbox({readonly: true, required: false});
                        ui.getItemJQ("JS").textbox("setValue", "");
                    } else {
                        ui.getItemJQ("ZL").textbox({readonly: true, required: false});
                        ui.getItemJQ("JS").textbox({readonly: false, required: true});
                        ui.getItemJQ("ZL").textbox("setValue", "");
                    }

                });
                ui.addCallback("setComboGridValue_ddd", function (o) {
                    if (null == o) return;
                    var obj = o.rowData;
                    if (null == obj) return;
                    ui.setFormData({DDD: obj.CDMC, DDDBM: obj.CDBM});
                });
                /**
                 * 设置商品列表过滤条件
                 */
                ui.addOutputValue("setTcsJclhsp", function (o) {
                    var masterForm = ui.getMasterForm();
                    var jclhbh = $("#JCLHBH_" + masterForm.timestamp).combogrid("getValue");
                    var lhbhId;
                    var jsonData = $.loadJson($.contextPath + "/jiaoyixinxixinzeng!getIdByJclhbh.json?jclhbh=" + jclhbh);
                    if (isEmpty(jsonData)) {
                        jsonData = " ";
                    }
                    var o = {
                        status: true,
                        P_columns: "LIKE_C_PID≡" + jsonData
                    }
                    return o;
                });

            }

        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
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
            /**
             * 添加自定义按钮
             * data为原来的按钮
             */
            if (1 === ui.options.number) {
                ui.processItems = function (data) {
                    var btns = [];
//                    btns.push({
//                        id: $.config.preButtonId + "readCard",
//                        label: "读卡",
//                        type: "button"
//                    });
//                    btns.push({
//                        id: $.config.preButtonId + "print",
//                        label: "打印",
//                        type: "button"
//                    });
                    btns.push('->',{
                        id: $.config.preButtonId + "writeOneCodeSave",
                        label: "写码保存",
                        type: "button"
                    });
                    btns.push({
                        id: $.config.preButtonId + "writeOneCardSave",
                        label: "写卡保存",
                        type: "button"
                    });
                    for (var i = 0; i < data.length; i++) {
                        btns.push(data[i]);
                    }
                    return btns;
                };
            } else if (2 === ui.options.number) {
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
            // ui.assembleData 就是 configInfo
            //console.log("override tbar!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {
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

            var startTime = new Date().getTime();

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

        };


    })("${timestamp}");
</script>
