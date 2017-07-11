<%@ page import="com.ces.config.utils.CommonUtil" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags" %>
<%
    String path = request.getContextPath();
    String resourceFolder = path + "/cfg-resource/coral40/common";
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
    request.setAttribute("menuId", request.getParameter("menuId"));
    request.setAttribute("basePath", basePath);
    request.setAttribute("path", path);
%>

<div id="maxDiv${idSuffix}" class="fill">
    <div style="margin: 0 auto;margin-top:20px;text-align:center">
        <span>生产企业：</span>
        <input id="scqy${idSuffix}" name="scqy${idSuffix}">
        <span>产品名称：</span>
        <input id="cpmc${idSuffix}" name="cpmc${idSuffix}">
        <span>出场年月：</span>
        <input id="startDatepicker${idSuffix}" name="startDatepicker">
        <span>到</span>
        <input id="endDatepicker${idSuffix}" name="endDatepicker">
        <button id="search${idSuffix}">查询</button>
        <button id="cz${idSuffix}">重置</button>
    </div>
    <div id="container${idSuffix}" style="width: 80%; height: 400px; margin: 0 auto"></div>
</div>
<script type="text/javascript">
    var seriesData = [{name:"安全率",data:[]}];
    $.extend($.ns("namespaceId${idSuffix}"), {
        initCharts:function(){
            Highcharts.setOptions({
                lang: {
//                    months: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
//                    shortMonths: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
//                    weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"]
                }
            });
            $('#container${idSuffix}').highcharts({
                title: {
                    text: '安全率',
                    x: -20 //center
                },
                credits: {//版权显示
                    enabled: false
                },
//                subtitle: {
//                    text: '批发市场',
//                    x: -20
//                },
                xAxis: {
                    type: 'datetime',
                    dateTimeLabelFormats: {
                        month: '%y年%b月'
                    }
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '产品安全率'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }]
                },
                tooltip: {
                    valueSuffix: '%',
                    dateTimeLabelFormats: {
                        month: '%y年%b月'
                    },
                    crosshairs: {
                        width: 2,
                        color: 'gray',
                        dashStyle: 'shortdot'
                    }
                },
                colors: ['#2f7ed8'],
                legend: {//图标显示
//                enabled:false,
                    layout: 'vertical',
                    align: 'right',
                    verticalAlign: 'middle',
                    borderWidth: 0
                },
                series: seriesData
            });
            var qymc = $.loadJson($.contextPath + '/zlzzzlbg!getCompanyName.json');
            $('#scqy${idSuffix}').textbox({
//                required:true
                'value' : qymc,
                'readonly' : true
            });
            var pzxxJson = $.loadJson($.contextPath + '/zlzzzlbg!getPzxx.json');
            $('#cpmc${idSuffix}').combobox({
                valueField:'CPBH',
                textField:'CPMC',
                enableFilter:true,
                data:pzxxJson
            });
            $('#startDatepicker${idSuffix}').datepicker({
                dateFormat: 'yyyy-MM'/*,
                 value: now*/
            });
            $('#endDatepicker${idSuffix}').datepicker({
                dateFormat: 'yyyy-MM'/*,
                 value: now*/
            });
            $('#search${idSuffix}').button();
            $('#cz${idSuffix}').button();
            $("#search${idSuffix}").click(function(){
                var startDate = $('#startDatepicker${idSuffix}').datepicker("getDateValue");
                var endDate = $('#endDatepicker${idSuffix}').datepicker("getDateValue");
                var cpbh = $("#cpmc${idSuffix}").combobox("getValue");
                var scqy = $('#scqy${idSuffix}').textbox('getValue');
                if (startDate == "" || endDate == "" || cpbh == "") {
                    CFG_message("请完善查询信息","warning");
                    return;
                }
                startDate = startDate.replace("-","/");
                endDate = endDate.replace("-","/");
                var startD = new Date(Date.parse(startDate));
                var endD = new Date(Date.parse(endDate));
                if (startD > endD) {
                    CFG_message("起始年月必须小于终止年月!","warning");
                    return;
                }
                $.ajax({
                    type: "post",
                    url: $.contextPath + "/zlzzzlbg!getAql.json",
                    data:{"startDate":startDate.replace("/","-"),
                        "endDate":endDate.replace("/","-"),
                        "cpbh":cpbh,
                        "scqy":scqy
                    },
                    async: false,
                    success: function (data) {
                        if(data.length!=0){seriesData = toHighchartsData(data);
                        }
                        else{CFG_message("没有查到数据!","warning");}

                    }
                });
                var chart = $('#container${idSuffix}').highcharts();
                var seriesList = chart.series; //获得图表的所有序列
                //通过for循环删除序列数据
                for(var i = 0;i<seriesList.length;)
                {
                    chart.series[0].remove();
                }
                if($.isEmptyObject(seriesData)){
                    return ;
                }
                chart.addSeries(seriesData);
            });
            $("#cz${idSuffix}").click(function() {
                $("#scqy${idSuffix}").textbox("setValue", "");
                $("#cpmc${idSuffix}").combobox("setValue", "");
                $('#startDatepicker${idSuffix}').datepicker("setDate", "");
                $('#endDatepicker${idSuffix}').datepicker("setDate", "");
            })
        }
    });
    var toHighchartsData=function(jsonData){
        var result = {};
        var data = [];
        result.name = "安全率";
        $.each(jsonData, function () {
            var date = this.date;
            var rate = this.rate;
            var year = date.substr(0, 4);
            var month = date.substr(5, 2);
            data.push([Date.UTC(year,month-1),rate]);
        });
        result.data = data;
        return result;
    };
    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'zlzzzlbg.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#maxDiv${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad': function (configInfo) {
                $.ns('namespaceId${idSuffix}').initCharts();
            }
        });
    });
</script>
