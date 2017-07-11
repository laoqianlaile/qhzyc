<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String card = request.getParameter("card");
%>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>触摸屏Demo</title>
    <link rel="stylesheet" href="css/reset.css">
    <link rel="stylesheet" href="jquery-cui/4.0.2/css/jquery.coral.min.css">
    <link rel="stylesheet" href="css/hdty-operation.css">
    <link rel="stylesheet" href="css/minimal.css">
    <link rel="stylesheet" href="css/tinyscrollbar.css" media="screen"/>
</head>

<body>
<div class="wrapper">
    <!--头部-->
    <div class="head">
        <img src="images/opt-out.png">
    </div>

    <!--内容部分，主要分为两列，左边为内容区，右边为导航区-->
    <div id="tabs1">
        <ul>
            <li>
                <a href="#fragment-1" id="first-tab">
                    <div class="tab-info">
                        <div class="img"></div>
                        <div class="tab-name">任务</div>
                        <div class="tips" id="tasks"></div>
                    </div>
                </a>
            </li>
            <li>
                <a href="#fragment-1" id="second-tab">
                    <div class="tab-info">
                        <div class="img"></div>
                        <div class="tab-name tab-name1">完成</div>
                        <div class="tips tips1" id="complete"></div>
                    </div>
                </a>
            </li>
        </ul>
        <div id="fragment-1" class="clearfix">
            <!--这是批量处理的按钮-->
            <div class="buttons clearfix">
                <div class="button addButton" id="add-button">
                    <img src="images/add-icon.png">
                    <span>新增</span>
                </div>
                <div class="button" id="back-button">
                    <img src="images/back-icon.png" style="margin-left:-6%;">
                    <span>回退</span>
                </div>
                <div class="button" id="submit-button">
                    <img src="images/gou.png">
                    <span>完成</span>
                </div>
                <div class="button" id="start-button">
                    <img src="images/start-icon.png">
                    <span>开始</span>
                </div>
            </div>
            <table class="table-head">
                <thead>
                <tr>
                    <td width="8.37%" class="left-td">
                        <div class="all-select">全选</div>
                    </td>
                    <td width="13.55%" class="relative"><div class="relative"><input id="department"/>
                    </div>
                    </td>
                    <td width="14.62%" class="relative">
                        <div class="relative"><div class="bg"></div>
                            <input id="name"/>
                        </div>
                    </td>
                    <td width="14.95%" class="relative">
                        <div class="relative"><div class="bg"></div>
                            <input id="combobox1" name="combobox" value="1">
                        </div>
                    </td>
                    <td width="18.28%">作业名称</td>
                    <td width="26.46%">状态</td>
                    <td width="3.77%" class="right-td"></td>
                </tr>
                </thead>
            </table>
            <div id="scrollbar1">
                <div class="scrollbar">
                    <div class="track">
                        <div class="thumb">
                            <div class="end"></div>
                        </div>
                    </div>
                </div>
                <div class="viewport">
                    <div class="overview" flag="farmingList">
                        <table class="table-body">
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
        <div id="fragment-2">

        </div>
    </div>

</div>
<div class="alertBox" style="z-index:100000;">
    <div class="alert-head">
        警告
    </div>
    <div class="alert-body">
        <div class="text"></div>
        <div class="selection">
            <input type="button" value="取消" class="cancel"/>
            <input type="button" value="确定" class="confirm"/>
        </div>
    </div>
</div>

<div class="addBox">
    <div class="addBox-head clearfix">
        <span class="text1">请选择</span>
        <img src="images/close.png">
    </div>
    <div class="addBox-body">
        <table class="table-head1">
            <thead>
            <tr>
                <td width="25%" class="relative">
                    <div class="relative"><div class="bg"></div>
                        <input id="department2"/>
                    </div>
                </td>
                <td width="25%" class="relative">
                    <div class="relative"><div class="bg"></div>
                        <input id="name2"/>
                    </div>
                </td>
                <td width="28%" class="relative">
                    <div class="relative"><div class="bg"></div>
                        <input id="combobox3"  name="combobox" value="1">
                    </div>
                </td>
                <td width="22%">作业名称</td>
            </tr>
            </thead>
        </table>
        <div id="scrollbar3">
            <div class="scrollbar">
                <div class="track">
                    <div class="thumb">
                        <div class="end"></div>
                    </div>
                </div>
            </div>
            <div class="viewport">
                <div class="overview" flag="overviewIncrease">
                    <table class="table-body1">
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="jquery-cui/4.0.2/js/jquery.coral.min.js"></script>
<script src="js/jquery.tinyscrollbar.js"></script>
<script src="js/icheck.min.js"></script>
<script src="js/hdty-operation.js"></script>
<script>

    /*********************************自定义全局变量 begin****************************************************/
    var _$data = {
        data: {},
        company: "",
        is_end: "0"
    };
    var _url = "";
    var _data = {};
    var baseUrl = "/config1.0/services/jaxrs/farmOperationService";
    /*********************************自定义全局变量 end****************************************************/


    $(function () {
        $('#first-tab').click(function () {
            <%--window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-operation.jsp?card=<%=card%>";--%>
            _$data.is_end = "0";
            queryFarming();
        });

        $("#second-tab").click(function () {
            <%--window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-operation1.jsp?card=<%=card%>";--%>
            _$data.is_end = "1";
            queryCompleteFarming();
            var first=$('#first-tab');
            var second=$('#second-tab');

            $(this).find('.img').css('background-image','url(images/complete-icon1.png)');
            $(this).find('.tab-name').removeClass('tab-name1').css("color","#114100");
            $(this).find('.tips').removeClass('tips1');
            flag=1;

            first.find('.tips').addClass('tips1');
            first.find('.img').css('background-image','url(images/task-icon2.png)');
            first.find('.tab-name').addClass('tab-name1');
            first.find('.tab-name').css('color','#67bb80');

            var winWidth=$(window).width();
            var winheight=$(window).height();
            if(winWidth<1280){
                if(winheight<768){
                    $(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
                }
                else if(winheight>=768&&winheight<=880){
                    $(this).children('.tab-info').css('margin-top',(349-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(256-second.children('.tab-info').height())/2);
                }
                else if(winheight>=880){
                    $(this).children('.tab-info').css('margin-top',(372-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(274-second.children('.tab-info').height())/2);
                }

            }else if(winWidth>=1280&&winWidth<1440){
                if(winheight<768){
                    $(this).children('.tab-info').css('margin-top',(292-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(216-second.children('.tab-info').height())/2);
                }
                else if(winheight>=768&&winheight<=880){
                    $(this).children('.tab-info').css('margin-top',(339-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(250-second.children('.tab-info').height())/2);
                }
                else if(winheight>=880){
                    $(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(277-second.children('.tab-info').height())/2);
                }
            }
            else if(winWidth>=1440&&winWidth<1920){
                if(winheight<768){
                    $(this).children('.tab-info').css('margin-top',(305-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(222-first.children('.tab-info').height())/2);
                }else if(winheight>=768&&winheight<=880){
                    $(this).children('.tab-info').css('margin-top',(332-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(243-first.children('.tab-info').height())/2);
                }
                else if(winheight>=860&&winheight<880){
                    $(this).children('.tab-info').css('margin-top',(360-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(264-first.children('.tab-info').height())/2);
                }
                else if(winheight>=880&&winheight<920){
                    $(this).children('.tab-info').css('margin-top',(379-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(277-first.children('.tab-info').height())/2);
                }
                else if(winheight>=920){
                    $(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
                    first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
                }
            }else if(winWidth>=1920){
                $(this).children('.tab-info').css('margin-top',(394-$(this).children('.tab-info').height())/2);
                first.children('.tab-info').css('margin-top',(289-first.children('.tab-info').height())/2);
            }
            flag1=0;
        });

        $("input[class=confirm]").click(function () {
            if($(".text").html() == "是否退出系统")
            window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-login.jsp";
        });


        /**********************批量开始操作 begin*******************************/
        $("#start-button").click(function () {
            var sid = "";
            var farmingType = "";
            $.each($("input[flag=farmingCheckbox]"), function (e, data) {
                if ($(this).parents("div").attr("class").indexOf("checked") >= 0) {
                    sid += $(this).attr("sid") + ",";
                    farmingType += $(this).attr("farmingType") + ",";
                }
            });
            if(sid == ""){
                bgBlur();
                $(".text").html("请先选择需要操作的农事项！");
                $(".alertBox").show();
            }
            _url = baseUrl + "/beginOperation";
            _data = {
                sid: sid,
                ickbh: "<%=card%>",
                farmingType: farmingType
            }
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                async: false,
                success: function (data) {
                    if (data.RESULT != "ERROR") {
                        if (_$data.is_end == "0") {
                            queryFarming();
                        } else if (_$data.is_end == "1") {
                            queryCompleteFarming();
                        }
                        if ($('.all-select').css("color") == "rgb(255, 255, 255)")
                            $('.all-select').click();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });


        $("#submit-button").click(function () {
            var sid = "";
            var farmingType = "";
            $.each($("input[flag=farmingCheckbox]"), function (e) {
                if ($(this).parents("div").attr("class").indexOf("checked") >= 0) {
                    sid += $(this).attr("sid") + ",";
                    farmingType += $(this).attr("farmingType") + ",";
                }
            });
            if(sid == ""){
                bgBlur();
                $(".text").html("请先选择需要操作的农事项！");
                $(".alertBox").show();
            }
            _url = baseUrl + "/endOperation";
            _data = {
                sid: sid,
                ickbh: "<%=card%>",
                farmingType: farmingType
            }
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                async: false,
                success: function (data) {
                    if (data.RESULT != "ERROR") {
                        if (_$data.is_end == "0") {
                            queryFarming();
                        } else if (_$data.is_end == "1") {
                            queryCompleteFarming();
                        }
                        if ($('.all-select').css("color") == "rgb(255, 255, 255)")
                            $('.all-select').click();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });

        $("#back-button").click(function () {
            var sid = "";
            var farmingType = "";
            $.each($("input[flag=farmingCheckbox]"), function (e) {
                if ($(this).parents("div").attr("class").indexOf("checked") >= 0) {
                    sid += $(this).attr("sid") + ",";
                    farmingType += $(this).attr("farmingType") + ",";
                }
            });
            if(sid == ""){
                bgBlur();
                $(".text").html("请先选择需要操作的农事项！");
                $(".alertBox").show();
            }
            _url = baseUrl + "/rollBack";
            _data = {
                sid: sid,
                ickbh: "<%=card%>",
                farmingType: farmingType
            }
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                async: false,
                success: function (data) {
                    if (data.RESULT != "ERROR") {
                        if (_$data.is_end == "0") {
                            queryFarming();
                        } else if (_$data.is_end == "1") {
                            queryCompleteFarming();
                        }
                        if ($('.all-select').css("color") == "rgb(255, 255, 255)")
                            $('.all-select').click();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });

        /**********************批量操作 end*******************************/
    })


    //初始化复选框CSS
    function initCheck() {
        $('.skin-minimal input').iCheck({
            checkboxClass: 'icheckbox_minimal',
            increaseArea: '20%'
        });
    }


    $("#combobox_i1_0,#combobox_i2_0").parents(".coral-combo-wrapper").css({
        "-webkit-border-radius": "0 25px 25px 25px",
        "-moz-border-radius": "0 25px 25px 25px",
        "-ms-border-radius": "0 25px 25px 25px",
        "-o-border-radius": "0 25px 25px 25px",
        "border-radius": "0 25px 25px 25px",
    });

    var combo_data7;
    var land_Data;
    function queryArea() {
        _url = baseUrl + "/queryArea";
        _data = {
            operatorIcCode: "<%=card%>"
        };
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                combo_data7 = data;
            },
            error: function () {
                alert("出错了！");
            }
        });
        return combo_data7;
    }
    queryArea();
    //地块初始化
    function queryLand(areaCode) {
        _url = baseUrl + "/queryLand";
        _data = {
            operatorIcCode: "<%=card%>",
            areaCode: areaCode
        };
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                land_Data = data;
            },
            error: function () {
                alert("出错了！");
            }
        });
        return land_Data;
    }

    var $name = $("#name");
    $.parseDone(function () {
        $("#department").combobox({
            valueField: 'QYBH',
            textField: 'QYMC',
            onChange: "onChange",//只有选择的值改变时才能触发
            onShowPanel: "styleChange",
            onHidePanel: "styleChange1",
            data: combo_data7,
            value: "all",
            panelComponentCls: "areaBlock"
        });
        queryLand("all");
        $("#name").combobox({
            valueField: 'DKBH',
            textField: 'DKMC',
            onShowPanel: "styleChange",
            onHidePanel: "styleChange1",
            onChange: "onChange2",
            data: land_Data,
            value: "all",
            panelWidth: "800px",
            panelComponentCls: "landBlock"//给面板添加类，这样就可以任意定义面板的样式了！！！！
        });
        if (_$data.is_end == "0") {
            queryFarming();
        } else if (_$data.is_end == "1") {
            queryCompleteFarming();
        }
    });

    function onChange(e, ui) {
        $("#name").combobox("reload", queryLand(ui.value));
        $("#name").combobox("setValue", "all");
        if (_$data.is_end == "0") {
            queryFarming();
        } else if (_$data.is_end == "1") {
            queryCompleteFarming();
        }
    }

    function onChange2(e, ui) {
        if (_$data.is_end == "0") {
            queryFarming();
        } else if (_$data.is_end == "1") {
            queryCompleteFarming();
        }
    }

    $('#combobox1').combobox({
        valueField: 'value',
        textField: 'text',
        data: [{
            "value": "all",
            "text": "作业类型（全部）",
            "selected": true,
            icon: "opeicon1"
        }, {
            "value": "bz",
            "text": "播种",
            icon: "opeicon1"
        }, {
            "value": "sf",
            "text": "施肥",
            icon: "opeicon2"
        }, {
            "value": "gg",
            "text": "灌溉",
            icon: "opeicon3"
        }, {
            "value": "cc",
            "text": "锄草",
            icon: "opeicon4"
        }, {
            "value": "yy",
            "text": "用药",
            icon: "opeicon5"
        }, {
            "value": "qt",
            "text": "其它",
            icon: "opeicon6"
        }, {
            "value": "cs",
            "text": "采收",
            icon: "opeicon7"
        }],
        onShowPanel: "styleChange",
        onHidePanel: "styleChange1",
        onSelect: "getData3",
        onChange: "onChange3",
        formatter: "formatter",
        panelWidth: "364px",
        panelComponentCls: "workBlock"
    });

    function onChange3(e, ui) {
        if (_$data.is_end == "0") {
            queryFarming();
        } else if (_$data.is_end == "1") {
            queryCompleteFarming();
        }
    }


    var $name2 = $("#name2");
    $.parseDone(function () {
        $("#department2").combobox({
            valueField: 'QYBH',
            textField: 'QYMC',
            onShowPanel: "styleChange",
            onHidePanel: "styleChange1",
            data: combo_data7,
            value: "all",
            panelComponentCls: "areaBlockAdd",
            onChange: "onChangeAdd"
        });
        $("#name2").combobox({
            valueField: 'DKBH',
            textField: 'DKMC',
            onShowPanel: "styleChange",
            onHidePanel: "styleChange1",
            data: land_Data,
            value: "all",
            panelWidth: "460px",
            panelComponentCls: "landBlockAdd",
            onChange: "onChangeAdd2"
        });
    });
    function onChangeAdd(e, ui) {
        $("#name2").combobox("reload", queryLand(ui.value));
        $("#name2").combobox("setValue", "all");
        increase();
    }
    function onChangeAdd2(e, ui) {
        increase();
    }
    function onChangeAdd3(e, ui) {
        increase();
    }

    $('#combobox3').combobox({
        valueField: 'value',
        textField: 'text',
        data: [{
            "value": "all",
            "text": "作业类型（全部）",
            "selected": true,
            icon: "opeicon0"
        }, {
            "value": "bz",
            "text": "播种",
            icon: "opeicon1"
        }, {
            "value": "sf",
            "text": "施肥",
            icon: "opeicon2"
        }, {
            "value": "gg",
            "text": "灌溉",
            icon: "opeicon3"
        }, {
            "value": "cc",
            "text": "锄草",
            icon: "opeicon4"
        }, {
            "value": "yy",
            "text": "用药",
            icon: "opeicon5"
        }, {
            "value": "qt",
            "text": "其它",
            icon: "opeicon6"
        }, {
            "value": "cs",
            "text": "采收",
            icon: "opeicon7"
        }],
        onShowPanel: "styleChange",
        onHidePanel: "styleChange1",
        formatter: "formatter",
        panelWidth: "364px",
        panelComponentCls: "workBlockAdd",
        onChange: "onChangeAdd3",
    });

    //点击新增按钮
    $("#add-button").click(function () {
        bgBlur();
        $(".addBox").show();
        setTimeout(function () {
//            $('#scrollbar3').tinyscrollbar();
            var cWidth = $(".cancelAdd").width();
            $(".cancelAdd, .confirmAdd").css("height", cWidth * 0.5);
            $(".cancelAdd, .confirmAdd").css("line-height", cWidth * 0.5 + "px");
            $(".cancelAdd, .confirmAdd").css("margin-top", cWidth * 0.1 + "px");
            $(".cancelAdd, .confirmAdd").css("margin-bottom", cWidth * 0.1 + "px");
        }, 1000);
        increase();
    });

    //实现遮罩
    var bgObj;
    function bgBlur() {
        var sWidth,sHeight;
        sWidth=document.body.scrollWidth;//获得的body正文全文宽
        sHeight=document.body.scrollHeight ;//获得的body正文全文高
        bgObj=document.createElement("div");
        bgObj.setAttribute('id','bgDiv');
        bgObj.style.position="absolute";
        bgObj.style.top="0";
        bgObj.style.background="#000";
        bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
        bgObj.style.opacity="0.8";
        bgObj.style.left="0";
        bgObj.style.width=sWidth + "px";
        bgObj.style.height=sHeight + "px";
        bgObj.style.zIndex = "10010";
        document.body.appendChild(bgObj);
        blur1=1;
    }

    $(".addBox-head img").click(function () {
        if (_$data.is_end == "0") {
            queryFarming();
        } else if (_$data.is_end == "1") {
            queryCompleteFarming();
        }
        document.body.removeChild(bgObj);
        $(".wrapper").css("webkitFilter", "none");
        $(".wrapper").css("mozFilter", "none");
        $(".wrapper").css("msFilter", "none");
        $(".wrapper").css("oFilter", "none");
        $(".wrapper").css("filter", "none");
        $(".addBox").css("display", "none");
        blur1 = 0;
    });

    /****************************获取企业编码 begin************************/
    _url = baseUrl + "/touchIn";
    _data = {
        card: "<%=card%>"
    };
    $.ajax({
        url: _url,
        data: _data,
        type: "GET",
        dataType: "json",
        async: false,
        success: function (data) {
            if (data.RESULT == "ERROR") {
                window.location.href = "<%=path%>/cfg-resource/coral40/views/syncpsczyxxglxt/v2.0/hdty-login.jsp?card=" + card;
            } else if (data.RESULT == "SUCCESS") {
                _$data.data = data.DATA;
                if (_$data.is_end == "0") {
                    setTimeout(queryFarming, 1000);
                } else if (_$data.is_end == "1") {
                    queryCompleteFarming
                    setTimeout(queryCompleteFarming, 1000);
                }

            }
        },
        error: function () {
            alert("出错了！");
        }
    });
    /****************************获取企业编码 end************************/

    /*********************************查询符合新增条件的农事项 begin*************************************************************/
    function increase() {
        _data = {
            company: _$data.data.QYBM,
            area: $("#department2").combobox("getValue"),
            land: $("#name2").combobox("getValue"),
            farmingType: $("#combobox3").combobox("getValue"),
            is_end: "1",
            operatorIcCode: "<%=card%>"
        };
        _url = baseUrl + "/queryFarmingItem";
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                loadIncreaseFarmingtItem(data.DATA);
            },
            error: function () {
                alert("出错了！");
            }
        });
    }

    function loadIncreaseFarmingtItem(data) {
        var table_bodyJQ = $(".table-body1 tbody");
        table_bodyJQ.empty();
        //循环加载每一条需操作农事项
        $.each(data, function (e, data) {
            var farmingType = ""
            var farmingTypeImg = "";
            if (data.TYPE == "bz") {
                farmingType = "播种";
                farmingTypeImg = "images/plant.png";
            } else if (data.TYPE == "gg") {
                farmingType = "灌溉";
                farmingTypeImg = "images/watering.png";
            } else if (data.TYPE == "sf") {
                farmingType = "施肥";
                farmingTypeImg = "images/fertilize.png";
            } else if (data.TYPE == "yy") {
                farmingType = "用药";
                farmingTypeImg = "images/drug.png";
            } else if (data.TYPE == "cs") {
                farmingType = "采收";
                farmingTypeImg = "images/recovery.png";
            } else if (data.TYPE == "cc") {
                farmingType = "锄草";
                farmingTypeImg = "images/weeding.png";
            } else if (data.TYPE == "qt") {
                farmingType = "其他";
                farmingTypeImg = "images/detection.png";
            }
            //是否正在操作
            var operationImg = "0";
            if (data.BEGINTIME != "" && data.ENDTIME == "") {
                operationImg = "1";
            }

            var Html = "<tr class=\"showTr1\">" +
                    "<td width=\"25%\" class=\"left-td2\">" + data.SSQYMC + "</td>" +
                    "<td width=\"25%\">" + data.DKMC + "</td>" +
                    "<td width=\"25%\">" +
                    "<img src=\"" + farmingTypeImg + "\" class=\"icon\"><span>" + farmingType + "</span>" +
                    "</td>" +
                    "<td width=\"25\" class=\"right-td2\">" + data.NSZYXMC + "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fold2\">" +
                    "<span class=\"cancelAdd\">" +
                    "<img src=\"images/back-icon.png\">取消" +
                    "</span>" +
                    "<span class=\"confirmAdd\"  sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                    "<img src=\"images/gou.png\">确定" +
                    "</span>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fx\"></td>" +
                    "</tr>";

            table_bodyJQ.append(Html);
        });
        $(".operation").css("width", $(".button").eq(0).width());
        $('.operation').css({
            'height': $('.operation').width() * 0.45,
            'line-height': $('.operation').width() * 0.45 + 'px'
        });
        $(".state").css("width", $(".button").eq(0).width());
        $('.state').css({'height': $('.state').width() * 0.45, 'line-height': $('.state').width() * 0.45 + 'px'});
        //初始化复选框样式
        initCheck();
        //刷新滚动条
        $("div[flag=overviewIncrease]").height((data.length * 72 + 72));
        setTimeout(function () {
            var $scrollbar = $("#scrollbar3");
            $scrollbar.tinyscrollbar();
            var scrollbar = $scrollbar.data("plugin_tinyscrollbar");
            scrollbar.update();
            var cWidth = $(".cancelAdd").width();
            $(".cancelAdd, .confirmAdd").css("height", cWidth * 0.5);
            $(".cancelAdd, .confirmAdd").css("line-height", cWidth * 0.5 + "px");
            $(".cancelAdd, .confirmAdd").css("margin-top", cWidth * 0.1 + "px");
            $(".cancelAdd, .confirmAdd").css("margin-bottom", cWidth * 0.1 + "px");
        }, 1000);
        //添加关闭点击事件
        $(".showTr1").click(function () {
            $(".showTr1").children("td").css("background-color", "#114100");
            $(".showTr1").next("tr").children(".fold2").css("background-color", "#24710c").children(".cancelAdd, .confirmAdd").css("display", "none");
            $(this).children("td").css("background-color", "#f08300");
            $(this).next("tr").children(".fold2").css("background-color", "#fff").children(".cancelAdd, .confirmAdd").css("display", "inline-block");
        });
        //添加确定点击事件
        $(".cancelAdd, .confirmAdd").click(function () {
            _data = {
                farmingType: $(this).attr("farmingType"),
                sid: $(this).attr("sid")
            };
            _url = baseUrl + "/addFarmingItem";
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                async: false,
                success: function (data) {
                },
                error: function () {
                    alert("出错了！");
                }
            });
            $index = $(this).parents("tr").prev(".showTr1");
            $index.children("td").css("background-color", "#114100");
            $index.next("tr").children(".fold2").css("background-color", "#24710c").children(".cancelAdd, .confirmAdd").css("display", "none");
        });

    }


    /*********************************查询符合新增条件的农事项 end*************************************************************/


    /******************************加载农事项 begin**********************/
    function queryFarming() {
        _data = {
            company: _$data.data.QYBM,
            area: $("#department").combobox("getValue"),
            land: $("#name").combobox("getValue"),
            farmingType: $("#combobox1").combobox("getValue"),
            is_end: _$data.is_end,
            operatorIcCode: "<%=card%>"
        };
        _url = baseUrl + "/queryFarmingItem";
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                $("#complete").html(data.COUNT);
                addFarmingtItem(data.DATA);
            },
            error: function () {
                alert("出错了！");
            }
        });
    }


    function addFarmingtItem(data) {
        var table_bodyJQ = $(".table-body tbody");
        table_bodyJQ.empty();
        //设置需要操作农事项条数
        $("#tasks").html(data.length);
        //循环加载每一条需操作农事项
        $.each(data, function (e, data) {
            var farmingType = ""
            var farmingTypeImg = "";
            if (data.TYPE == "bz") {
                farmingType = "播种";
                farmingTypeImg = "images/plant.png";
            } else if (data.TYPE == "gg") {
                farmingType = "灌溉";
                farmingTypeImg = "images/watering.png";
            } else if (data.TYPE == "sf") {
                farmingType = "施肥";
                farmingTypeImg = "images/fertilize.png";
            } else if (data.TYPE == "yy") {
                farmingType = "用药";
                farmingTypeImg = "images/drug.png";
            } else if (data.TYPE == "cs") {
                farmingType = "采收";
                farmingTypeImg = "images/recovery.png";
            } else if (data.TYPE == "cc") {
                farmingType = "锄草";
                farmingTypeImg = "images/weeding.png";
            } else if (data.TYPE == "qt") {
                farmingType = "其他";
                farmingTypeImg = "images/detection.png";
            }
            //是否正在操作
            var operationImg = "0";
            if (data.BEGINTIME != "" && data.ENDTIME == "") {
                operationImg = "1";
            }


            var topHtml = "<tr class=\"showTr\">" +
                    "<td width=\"7.66%\" class=\"left-td1\">" +
                    "<div class=\"skin skin-minimal\" sid=\"" + data.SID + "\">" +
                    "<input type=\"checkbox\" id=\"minimal-checkbox-1\" name=\"checkbox\" flag=\"farmingCheckbox\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                    "</div>" +
                    "</td>" +
                    "<td width=\"13.17%\" class=\"department\">" + data.SSQYMC + "</td>" +
                    "<td width=\"16.87%\" class=\"name\">" + data.DKMC + "</td>" +
                    "<td width=\"11.79%\" class=\"work\">" +
                    "<img src=\"" + farmingTypeImg + "\" class=\"icon\"><span>" + farmingType + "</span>" +
                    "</td>" +
                    "<td width=\"24.71%\">" + data.NSZYXMC + "</td>" +
                    "<td width=\"22.80%\">" +
                    "<div class=\"state\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\" operationImg=\"" + operationImg + "\">" +
                    "<img src=\"images/start-icon.png\">" +
                    "<span class=\"state-text\">未开始</span>" +
                    "</div>" +
                    "<div class=\"operations hidden\">" +
                    "<div class=\"operation complete\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                    "<img src=\"images/gou.png\">" +
                    "完成" +
                    "</div>" +
                    "<div class=\"operation back\" flag=\"rowRollBack\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                    "<img src=\"images/back-icon.png\">" +
                    "回退" +
                    "</div>" +
                    "<div class=\"access\">" +
                    "进行中<br />" +
                    "<div class=\"showPart\">" +
                    "<div class=\"proBar\"></div>" +
                    "</div>" +
                    "</div>" +
                    "</div>" +
                    "</td>";
            var centerHtem = "<td width=\"3%\">" +
                    "</td>" +
                    "</tr>";

            if (data.IS_INCREASE == 1) {
                centerHtem = "<td width=\"3%\" class=\"right-td1 delete-able\" sid=\"" + data.SID + "\" operationImg=\"" + operationImg + "\" farmingType=\"" + data.TYPE + "\">" +
                        "X" +
                        "</td>" +
                        "</tr>";
            }
            var bottonHtml = "<tr>" +
                    "<td colspan=\"7\" class=\"fold\">" +
                    "<span class=\"record\">操作记录</span>" +
                    "<span class=\"start-time\">开始时间：<span class=\"start-record\">" + data.BEGINTIME + "</span></span>" +
                    "<span class=\"end-time\">结束时间：<span class=\"end-record\">" + data.ENDTIME + "</span></span>" +
                    "<span class=\"people\">责任人:<span class=\"people-record\">" + data.OPERATOR + "</span></span>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fx\"></td>" +
                    "</tr>";

            table_bodyJQ.append(topHtml + centerHtem + bottonHtml);
        });
        $(".operation").css("width", $(".button").eq(0).width());
        $('.operation').css({
            'height': $('.operation').width() * 0.45,
            'line-height': $('.operation').width() * 0.45 + 'px'
        });
        $(".state").css("width", $(".button").eq(0).width());
        $('.state').css({'height': $('.state').width() * 0.45, 'line-height': $('.state').width() * 0.45 + 'px'});
        //初始化复选框样式
        initCheck();
        //刷新滚动条
        $("div[flag=farmingList]").height((data.length * 65 + 65));
        var $scrollbar = $("#scrollbar1");
        $scrollbar.tinyscrollbar();
        var scrollbar = $scrollbar.data("plugin_tinyscrollbar");
        scrollbar.update();
        /**********************处理正在操作的农事项样式 begin******************************/
        $.each($(".state"), function (e, data) {
            if ($(this).attr("operationImg") == "1") {
                $(this).addClass("hidden").next(".operations").removeClass("hidden");
            }
        });
        /**********************处理正在操作的农事项样式 end******************************/

        /*******************************开始操作点击事件 begin****************************************/
        $(".state").click(function (event) {
            event.stopPropagation();
            _url = baseUrl + "/operationFarmingItem";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            }
            _this = $(this);
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (_$data.is_end == "0") {
                        queryFarming();
                    } else if (_$data.is_end == "1") {
                        queryCompleteFarming();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });
        /*******************************开始操作点击事件 end****************************************/

        /*******************************完成操作点击事件 begin****************************************/
        $(".complete").click(function (event) {
            event.stopPropagation();
            _url = baseUrl + "/operationFarmingItem";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            }
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (_$data.is_end == "0") {
                        queryFarming();
                    } else if (_$data.is_end == "1") {
                        queryCompleteFarming();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });
        /*******************************完成操作点击事件 end****************************************/
        /***********************************删除 begin*******************************************************/
        $(".delete-able").click(function (event) {
            if ($(this).attr("operationImg") == "1") {
                bgBlur();
                $(".text").html("正在操作的农事项不能删除！");
                $(".alertBox").show();
                return false;
            }
            event.stopPropagation();
            _url = baseUrl + "/deleteFarmingItem";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            }
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (_$data.is_end == "0") {
                        queryFarming();
                    } else if (_$data.is_end == "1") {
                        queryCompleteFarming();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
//            $(this).parents("tr").addClass("hidden").next("tr").addClass("hidden").next("tr").addClass("hidden");
        });
        /***********************************删除 end*******************************************************/

        /*******************************查看操作人 begin*********************************************************/
        $(".showTr").click(function () {
            $(".showTr").children("td").removeClass("activeTd");
            $(".showTr").find(".proBar").removeClass("activeBar");
            $(".showTr").next("tr").children(".fold").css("background-color", "#24710c");
            $(".showTr").next("tr").find(".record, .start-time, .end-time, .people").css("display", "none");
            $(this).children("td").addClass("activeTd");
            $(this).find("[name='checkbox']").iCheck("check");
            $(this).find(".proBar").addClass("activeBar");
            $(this).next("tr").children(".fold").css("background-color", "#f8f8f8");
            $(this).next("tr").find(".record, .start-time, .end-time, .people").css("display", "inline-block");
        });
        /*******************************查看操作人 end*********************************************************/


        /*******************************回退操作点击事件 begin****************************************/
        $("div[flag=rowRollBack]").click(function (event) {
            event.stopPropagation();
            _url = baseUrl + "/rollBack";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            };
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if (_$data.is_end == "0") {
                        queryFarming();
                    } else if (_$data.is_end == "1") {
                        queryCompleteFarming();
                    }
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });
        /*******************************回退操作点击事件 end****************************************/
    }
    /******************************加载农事项 end**********************/








    /********************************************************************完成   未完成  分割线**********************************************/


    /******************************加载完成农事项 begin**********************/
    function queryCompleteFarming() {
        _data = {
            company: _$data.data.QYBM,
            area: $("#department").combobox("getValue"),
            land: $("#name").combobox("getValue"),
            farmingType: $("#combobox1").combobox("getValue"),
            is_end: _$data.is_end,
            operatorIcCode: "<%=card%>"
        };
        _url = baseUrl + "/queryFarmingItem";
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                $("#tasks").html(data.COUNT);
                addCompleteFarmingtItem(data.DATA);
            },
            error: function () {
                alert("出错了！");
            }
        });
    }

    function formatDate(date) {
        return date.getFullYear() + "/" + (date.getMonth() + 1) + "/" + date.getDate();
    }


    function addCompleteFarmingtItem(data) {
        var table_bodyJQ = $(".table-body tbody");
        table_bodyJQ.empty();
        //设置需要操作农事项条数
        $("#complete").html(data.length);
        //循环加载每一条需操作农事项
        $.each(data, function (e, data) {
            var farmingType = ""
            var farmingTypeImg = "";
            if (data.TYPE == "bz") {
                farmingType = "播种";
                farmingTypeImg = "images/plant.png";
            } else if (data.TYPE == "gg") {
                farmingType = "灌溉";
                farmingTypeImg = "images/watering.png";
            } else if (data.TYPE == "sf") {
                farmingType = "施肥";
                farmingTypeImg = "images/fertilize.png";
            } else if (data.TYPE == "yy") {
                farmingType = "用药";
                farmingTypeImg = "images/drug.png";
            } else if (data.TYPE == "cs") {
                farmingType = "采收";
                farmingTypeImg = "images/recovery.png";
            } else if (data.TYPE == "cc") {
                farmingType = "锄草";
                farmingTypeImg = "images/weeding.png";
            } else if (data.TYPE == "qt") {
                farmingType = "其他";
                farmingTypeImg = "images/detection.png";
            }
            var is_rollback = 1;
            var endOperationTime = formatDate(new Date(data.YGSJ2.replace(/-/g, "/")));
            var nowTime = formatDate(new Date());
            if (Date.parse(endOperationTime) < Date.parse(nowTime)) {
                is_rollback = 0
            }
            var html1 = html1 = "<tr class=\"showTr1\">" +
                    "<td width=\"7.66%\" class=\"left-td1\">" +
                    "<div class=\"skin skin-minimal\">";
            if (is_rollback == 1) {
                html1 += "<input type=\"checkbox\" id=\"minimal-checkbox-1\" name=\"checkbox\" flag=\"farmingCheckbox\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">";
            }
            html1 += "</div>" +
                    "</td>" +
                    "<td width=\"13.17%\">" + data.SSQYMC + "</td>" +
                    "<td width=\"16.87%\">" + data.DKMC + "</td>" +
                    "<td width=\"14.79%\">" +
                    "<img src=\"" + farmingTypeImg + "\" class=\"icon1\"><span>" + farmingType + "</span>" +
                    "</td>" +
                    "<td width=\"21.71%\">" + data.NSZYXMC + "</td>" +
                    "<td width=\"14.15%\" style=\"text-align: right;\">" +
                    "已完成" +
                    "</td>" +
                    "<td width=\"11.65%\" class=\"right-td1\" style=\"padding-left:4.3%;\">";
            var html2 = "";
            if (is_rollback == 1) {
                html2 = "<div class=\"backOpr\"  flag=\"rowRollBack\" sid=\"" + data.SID + "\" farmingType=\"" + data.TYPE + "\">" +
                        "<img src=\"images/back-icon.png\">" +
                        "<span class=\"textTip\">回退</span>" +
                        "</div>";
            }
            var html3 = "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fold1\">" +
                    "<span class=\"record1\">操作记录</span>" +
                    "<span class=\"start-time1\">开始时间：<span class=\"start-record\">" + data.BEGINTIME + "</span></span>" +
                    "<span class=\"end-time1\">结束时间：<span class=\"end-record\">" + data.ENDTIME + "</span></span>" +
                    "<span class=\"people1\">责任人:<span class=\"people-record\">" + data.OPERATOR + "</span></span>" +
                    "</td>" +
                    "</tr>" +
                    "<tr>" +
                    "<td colspan=\"7\" class=\"fx\"></td>" +
                    "</tr>";
            table_bodyJQ.append(html1 + html2 + html3);
        });
        $(".backOpr").css("width", $(".button").eq(0).width());
        $('.backOpr').css({'height': $('.backOpr').width() * 0.45, 'line-height': $('.backOpr').width() * 0.45 + 'px'});
        //初始化复选框样式
        initCheck();
        //刷新滚动条
//        $(".overview").height(($(".table-body").height() + 50));
//        $('#scrollbar2').tinyscrollbar();
        $("div[flag=farmingList]").height((data.length * 65 + 65));
        var $scrollbar = $("#scrollbar1");
        $scrollbar.tinyscrollbar();
        var scrollbar = $scrollbar.data("plugin_tinyscrollbar");
        scrollbar.update();

        /*******************************查看操作人 begin*********************************************************/
        $(".showTr1").click(function () {
            $(".showTr1").children("td").removeClass("activeTd1");
            $(".showTr1").next("tr").children(".fold1").css("background-color", "#114100");
            $(".showTr1").next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display", "none");
            $(this).children("td").addClass("activeTd1");
            $(this).find("[name='checkbox']").iCheck("check");
            $(this).next("tr").children(".fold1").css("background-color", "#fff");
            $(this).next("tr").find(".record1, .start-time1, .end-time1, .people1").css("display", "inline-block");
//            var $scrollbar = $("#scrollbar1");
//            $scrollbar.tinyscrollbar();
//            var scrollbar = $scrollbar.data("plugin_tinyscrollbar");
//            scrollbar.update();
        });
        /*******************************查看操作人 end*********************************************************/


        /*******************************回退操作点击事件 begin****************************************/
        $("div[flag=rowRollBack]").click(function (event) {
            event.stopPropagation();
            _this = $(this);
            _url = baseUrl + "/rollBack";
            _data = {
                sid: $(this).attr("sid"),
                ickbh: "<%=card%>",
                farmingType: $(this).attr("farmingType")
            }
            _this = $(this);
            $.ajax({
                url: _url,
                data: _data,
                type: "GET",
                dataType: "json",
                success: function (data) {
                    queryCompleteFarming();
                },
                error: function () {
                    alert("出错了！");
                }
            });
        });
        /*******************************回退操作点击事件 end****************************************/

    }
    /******************************加载完成农事项 end**********************/

    /*************************批量回退 begin*************************************************/
    $("#back-button1").click(function () {
        var sid = "";
        var farmingType = "";
        $.each($("input[flag=farmingCheckbox]"), function (e) {
            if ($(this).parents("div").attr("class").indexOf("checked") >= 0) {
                sid += $(this).attr("sid") + ",";
                farmingType += $(this).attr("farmingType") + ",";
            }
        });
        _url = baseUrl + "/rollBack";
        _data = {
            sid: sid,
            ickbh: "<%=card%>",
            farmingType: farmingType
        }
        $.ajax({
            url: _url,
            data: _data,
            type: "GET",
            dataType: "json",
            async: false,
            success: function (data) {
                if (data.RESULT != "ERROR") {
                    queryCompleteFarming();
                    if ($('.all-select').css("color") == "rgb(255, 255, 255)")
                        $('.all-select').click();
                }
            },
            error: function () {
                alert("出错了！");
            }
        });
    });
    /*************************批量回退 end*************************************************/


</script>
</body>
</html>
