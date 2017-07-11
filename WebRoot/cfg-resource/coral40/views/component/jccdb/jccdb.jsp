<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
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
                'page': 'jccdb.jsp',
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
                $('#zl${idSuffix}').combobox({
                    valueField:'value',
                    textField:'text',
                    data:[{
                        "value":"1",
                        "text":'蔬菜批发市场'
                    },{
                        "value":"2",
                        "text":'肉品批发市场'
                    }],
                    onChange:function(){
                        var type = $("#zl${idSuffix}").combobox("getValue");
                        var jsonData;
                        if("1"==type){
                            jsonData = $.loadJson($.contextPath+"/csgl!getAllPcsc.json");
                        }else{
                            jsonData = $.loadJson($.contextPath+"/csgl!getAllPrsc.json");
                        }
                        $('#scmc${idSuffix}').combobox("reload", jsonData);
                    }
                });
                $('#scmc${idSuffix}').combobox({
                    valueField:'PFSCBM',
                    textField:'PFSCMC',
                    enableFilter:true
                });
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

                /* var d = new Date();
                 var vYear = d.getFullYear()
                 var vMon = d.getMonth() + 1
                 var now = vYear + "-" + (vMon < 10 ? "0" + vMon : vMon);*/
                $('#search${idSuffix}').button();
                $('#cz${idSuffix}').button();
                $("#search${idSuffix}").click(function(){
                    var id = $("#scmc${idSuffix}").combobox("getValue");
                    var type = $("#zl${idSuffix}").combobox("getValue");
                    if(($.isEmptyObject(type) || type == "")|| ($.isEmptyObject(id) || id=="")){
                        CFG_message("请选择查询条件！","warning");
                        return;
                    }
                    if(type == "1"){
                        var chartData = $.loadJson($.contextPath+'/csgl!getCpjccdbChartByweek.json?id='+id);
                    }else if(type == "2"){
                        var chartData = $.loadJson($.contextPath+'/csgl!getRpjccdbChartByweek.json?id='+id);
                    }
                    var pfscmc = "";
                    if(chartData.length!=0){
                        pfscmc = chartData[0].PFSCMC;
                    }
                    var categories = new Array();
                    var jczlData = new Array();
                    var cczlData = new Array();
                    for(var i=0;i<chartData.length;i++){
                        categories.push(chartData[i].RQ);
                        jczlData.push(chartData[i].JCZL);
                        cczlData.push(chartData[i].CCZL);
                    }
                    var series = [{
                        name:'进场重量',
                        data:jczlData
                    },{
                        name:'出场重量',
                        data:cczlData
                    }];
                    $('#container${idSuffix}').highcharts({
                        chart: {
                            type: 'column'
                        },
                        credits: {//版权显示
                            enabled: false
                        },
                        title: {
                            text: pfscmc + '一周进出场对比'
                        },
                        subtitle: {
                            text: ''
                        },
                        xAxis: {
                            categories: categories
                        },
                        yAxis: {
                            min: 0,
                            title: {
                                text: '重量 (斤)'
                            }
                        },
                        tooltip: {
                            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y:.1f}斤</b></td></tr>',
                            footerFormat: '</table>',
                            shared: true,
                            useHTML: true
                        },
                        plotOptions: {
                            column: {
                                pointPadding: 0.2,
                                borderWidth: 0
                            }
                        },
                        series: series
                    });
                })
                $("#cz${idSuffix}").click(function() {
                    $("#zl${idSuffix}").combobox("setValue", "");
                    $("#scmc${idSuffix}").combobox("setValue", "");
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
        <span>批发市场种类：</span>
        <input id="zl${idSuffix}" name="zl${idSuffix}">
        <span>市场名称：</span>
        <input id="scmc${idSuffix}" name="scmc${idSuffix}">
        <button id="search${idSuffix}">查询</button>
        <button id="cz${idSuffix}">重置</button>
    </div>
    <div id="container${idSuffix}" style="width: 80%; height: 400px; margin: 0 auto"></div>
</div>
</body>
</html>