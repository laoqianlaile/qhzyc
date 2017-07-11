<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>出场价格统计</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <%-- <script type="text/javascript" src="<%= path%>/cfg-resource/coral40/_cui_library/jquery.coral.min.js"></script>
     <script type="text/javascript" src="<%= path%>/cfg-resource/coral40/common/js/highcharts.js"></script>--%>
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script type="text/javascript">
        var seriesData;
        $(function () {
            var configInfo = CFG_initConfigInfo({
                /** 页面名称 */
                'page': 'ccjgtj.jsp',
                /** 页面中的最大元素 */
                'maxEleInPage': $('#maxDiv${idSuffix}'),
                /** 获取构件嵌入的区域 */
                'getEmbeddedZone': function () {
                    return $('#layout${idSuffix}').layout('panel', 'center');
                    //return $("#layout${idSuffix}");
                },
                /** 初始化预留区 */
                'initReserveZones': function (configInfo) {
                    CFG_addToolbarButtons(configInfo, $('#toolbarId${idSuffix}'), 'toolBarReserve', 0);
                },
                /** 获取返回按钮添加的位置 */
                'setReturnButton': function (configInfo) {
                    CFG_setReturnButton(configInfo, $('#toolbarId${idSuffix}'));
                },
                /** 页面初始化的方法 */
                'bodyOnLoad': function (configInfo) {
                    $.ns('namespaceId${idSuffix}').initCharts();
                }
            });
            if (configInfo) {
                //alert("系统参数：\t" + "关联的系统参数=" + CFG_getSystemParamValue(configInfo, 'systemParam1')
                //		+ "\n构件自身参数：\t" + "selfParam1=" + CFG_getSelfParamValue(configInfo, 'selfParam1')
                //		+ "\n构件入参：\t" + "inputParamName_1=" + CFG_getInputParamValue(configInfo, 'inputParamName_1'));
            }
        });
        $.extend($.ns("namespaceId${idSuffix}"), {
            initCharts:function(){
                $.ajax({
                    type: "post",
                    url: $.contextPath + "/csgl!queryCcjgxx.json",
                    async: false,
                    success: function (data) {
                        seriesData = toHighchartsData(data);
                    }
                })
                Highcharts.setOptions({
                    lang: {
                        months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
                        shortMonths: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
                        weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"]
                    }
                });
                $('#container${idSuffix}').highcharts({
                    title: {
                        text: '出场价格统计',
                        x: -20 //center
                    },
                    credits: {//版权显示
                        enabled: false
                    },
                    subtitle: {
                        text: '批发市场',
                        x: -20
                    },
                    xAxis: {
                        type: 'datetime',
                        dateTimeLabelFormats: {
                            day: '%B %e日'
                        }
                    },
                    yAxis: {
                        title: {
                            text: '平均价格（元）'
                        },
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }]
                    },
                    tooltip: {
                        valueSuffix: '元/斤',
                        dateTimeLabelFormats: {
                            day: "%A, %B%e日, %Y年"
                        },
                        crosshairs: {
                            width: 2,
                            color: 'gray',
                            dashStyle: 'shortdot'
                        }
                    },
                    colors: ['#2f7ed8', '#0d233a'],
                    legend: {//图标显示
//                enabled:false,
                        layout: 'vertical',
                        align: 'right',
                        verticalAlign: 'middle',
                        borderWidth: 0
                    },
                    series: seriesData
                });
                $('#zl${idSuffix}').combobox({
                    valueField:'value',
                    textField:'text',
                    data:[{
                        "value":"1",
                        "text":'蔬菜'
                    },{
                        "value":"2",
                        "text":'肉类'
                    }],
                    onChange:function(){
                        var type = $("#zl${idSuffix}").combobox("getValue");
                        if("1"==type){
                            $('#spbm${idSuffix}').combobox("reload", $.contextPath+"/csgl!getAllScFoods.json");
                        }else{
                            $('#spbm${idSuffix}').combobox("reload", $.contextPath+"/csgl!getAllRpFoods.json");
                        }
                    }
                });
                $('#spbm${idSuffix}').combobox({
                    valueField:'value',
                    textField:'text',
                    enableFilter:true
                })
                /* var d = new Date();
                 var vYear = d.getFullYear()
                 var vMon = d.getMonth() + 1
                 var now = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon);*/
                $('#datepicker${idSuffix}').datepicker({
                    dateFormat: 'yyyy-MM'/*,
                     value: now*/
                });
                $('#search${idSuffix}').button();
                $('#cz${idSuffix}').button();
                $("#search${idSuffix}").click(function(){
                    var date = $('#datepicker${idSuffix}').datepicker("getDateValue");
                    var year = date.substr(0,4);
                    var month = date.substr(5,2);
                    var zl = $("#zl${idSuffix}").combobox("getValue");
                    var spbm = $("#spbm${idSuffix}").combobox("getValue");
                    if(!$.isEmptyObject(zl)&& $.isEmptyObject(spbm)){
                        CFG_message("请选择商品名称！","warning");
                        return;
                    }
                    if($.isEmptyObject(zl)&& !$.isEmptyObject(spbm)){
                        CFG_message("请选择种类！","warning");
                        return;
                    }
                    $.ajax({
                        type: "post",
                        url: $.contextPath + "/csgl!queryCcjgxx.json",
                        data:{"year":year,"month":month,"zl":zl,"spbm":spbm},
                        async: false,
                        success: function (data) {
                            seriesData = toHighchartsData(data);
                        }
                    })
                    var chart = $('#container').highcharts();
                    var seriesList = chart.series; //获得图表的所有序列
                    //通过for循环删除序列数据
                    for(var i = 0;i<seriesList.length;)
                    {
                        chart.series[0].remove();
                    }
                    if($.isEmptyObject(seriesData)){
                        return ;
                    }
                    var color = ['#2f7ed8', '#0d233a'];
                    $.each(seriesData,function(index){
                        var data = {
                            name:this.name,
                            data:this.data,
                            color:color[index]
                        }
                        chart.addSeries(data);
                    })
                })
                $("#cz${idSuffix}").click(function() {
                    $("#zl${idSuffix}").combobox("setValue", "");
                    $("#spbm${idSuffix}").combobox("setValue", "");
                    $('#datepicker${idSuffix}').datepicker("setDate", "");
                })
            }
        })
        var toHighchartsData=function(data){
            var result = []
            $.each(data, function (key, value) {
                var seriesData = {};
                var highData = [];
                seriesData.name = key;
                $.each(value, function () {
                    var rq = this.RQ;
                    var pjdj = this.PJDJ;
                    var year = rq.substr(0, 4);
                    var month = rq.substr(5, 2);
                    var day = rq.substr(8, 2);
                    highData.push([Date.UTC(year, month-1, day), pjdj]);
                })
                seriesData.data = eval(highData);
                result.push(seriesData);
            })
            return result;
        }
    </script>
</head>
<body>
<div id="maxDiv${idSuffix}" class="fill">
    <div style="margin: 0 auto;margin-top:20px;text-align:center">
        <span>种类：</span>
        <input id="zl${idSuffix}" name="zl${idSuffix}">
        <span>商品名称：</span>
        <input id="spbm${idSuffix}" name="spbm${idSuffix}">
        <span>年月：</span>
        <input id="datepicker${idSuffix}" name="datepicker">
        <button id="search${idSuffix}">查询</button>
        <button id="cz${idSuffix}">重置</button>
    </div>
    <div id="container${idSuffix}" style="width: 80%; height: 400px; margin: 0 auto"></div>
</div>
</body>
</html>