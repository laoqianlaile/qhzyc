<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
    <script type="text/javascript" src="<%= path%>/cfg-resource/coral40/_cui_library/jquery.coral.min.js"></script>
    <script type="text/javascript" src="<%= path%>/cfg-resource/coral40/common/js/highcharts.js"></script>
    <!--
    <link rel="stylesheet" type="text/css" href="styles.css">
    -->
    <script type="text/javascript">
    $(function () {
        var highData = [[Date.UTC(2015, 9, 27), 18.25], [Date.UTC(2015, 9, 28), 18.25], [Date.UTC(2015, 9, 30), 13.58]];
//        var data = $.loadJSON($.contextPath+"/csgl!queryCcjgxx.json");
        /*function toHighchartsData(data){
            $.each(data,function(){
            })
        }
        $.ajax({
            type:"post",
            url: $.contextPath+"/csgl!queryCcjgxx.json",
            async:false,
            success:function(data){
                highData = toHighchartsData(data);
            }
        })*/
        $('#container').highcharts({
            title: {
                text: '出场价格统计',
                x: -20 //center
            },
            credits:{//版权显示
                enabled:false
            },
            subtitle: {
                text: '批发市场',
                x: -20
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%e'
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
                valueSuffix: '元'
            },
            legend: {//图标显示
//                enabled:false,
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: '批菜',
                data: highData
            }]
        });
    });
</script>
</head>
<body>
<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>
</html>