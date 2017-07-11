<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
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
        var chartData = $.loadJson($.contextPath+'/trace!getQysqtj.json?csrq=month');
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

                <%--//日期初始化--%>
                <%--$('#csdatepicker${idSuffix}').datepicker({--%>
                    <%--dateFormat: 'yyyy-MM'/*,--%>
                     <%--value: now*/--%>
                <%--});--%>
                <%--$('#jzdatepicker${idSuffix}').datepicker({--%>
                    <%--dateFormat: 'yyyy-MM'/*,--%>
                     <%--value: now*/--%>
                <%--});--%>

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
                $('#zjqt${idSuffix}').button();
                $('#zjsst${idSuffix}').button();
                $('#zjqt${idSuffix}').click(function(){
                    chartData = $.loadJson($.contextPath+'/trace!getQysqtj.json?csrq=week');
                    query();
                });
                $('#zjsst${idSuffix}').click(function(){
                    chartData = $.loadJson($.contextPath+'/trace!getQysqtj.json?csrq=month');
                    query();
                });
                $('#search${idSuffix}').click(function(){
                    var csrq = $('#csdatepicker${idSuffix}').datepicker("getDateValue");
                    var jzrq = $('#jzdatepicker${idSuffix}').datepicker("getDateValue");
                    if(($.isEmptyObject(csrq) || csrq == "")|| ($.isEmptyObject(jzrq) || jzrq=="")){
                        CFG_message("请选择日期范围！","warning");
                        return;
                    }
                    chartData = $.loadJson($.contextPath+'/trace!getQysqtj.json?csrq='+csrq+'&jzrq='+jzrq);
                    query();
                });

                function query(){
                    var categories = new Array();
                    var sqsl = new Array();
                    for(var i=0;i<chartData.length;i++){
                        categories.push(chartData[i].RQ);
                        sqsl.push(chartData[i].SQSL);
                    }
                    $('#container${idSuffix}').highcharts({
                        chart: {
                            type: 'column'
                        },
                        credits: {//版权显示
                            enabled: false
                        },
                        title: {
                            text: '企业申请数量统计'
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
                                text: '数量（家）'
                            },
                            allowDecimals:false
                        },
                        tooltip: {
                            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                            '<td style="padding:0"><b>{point.y}家</b></td></tr>',
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
                        series: [{
                            name:'企业申请数量',
                            data:sqsl
                        }]
                    });
                }
            //初始显示30天数据
            query();

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
<div id="maxDiv${idSuffix}" class="fill" style="background: #fff">
    <div style="margin: 0 auto;margin-top:20px;text-align:center">
        <span>选择时间：</span>
        <%--<input id="csdatepicker${idSuffix}" name="datepicker">--%>
        <%--<span>~</span>--%>
        <%--<input id="jzdatepicker${idSuffix}" name="datepicker">--%>
        <ces:datepicker id="csdatepicker${idSuffix}" name="datepicker" dateFormat="yyyy-MM-dd"></ces:datepicker>~
        <ces:datepicker id="jzdatepicker${idSuffix}" name="datepicker" startDateId = "csdatepicker${idSuffix}" dateFormat="yyyy-MM-dd"></ces:datepicker>
        <button id="search${idSuffix}">查询</button>
        <button id="zjqt${idSuffix}">  最近7天  </button>
        <button id="zjsst${idSuffix}"> 最近30天 </button>
    </div>
    <div id="container${idSuffix}" style="width: 80%; height: 400px; margin: 0 auto"></div>
</div>
</body>
</html>