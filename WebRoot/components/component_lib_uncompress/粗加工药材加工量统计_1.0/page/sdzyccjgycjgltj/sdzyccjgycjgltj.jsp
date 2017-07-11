<%@ page import="com.ces.config.utils.CommonUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="ces" tagdir="/WEB-INF/tags"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    request.setAttribute("idSuffix", CommonUtil.generateUIId(""));
%>
<style>
    .coralui-datepicker{
        width: 100px;
    }
    .toolbarsnav{
        background: none;
    }
</style>
<div id="max${idSuffix}" class="fill">
    <ces:form id="form${idSuffix}" name="form">
        <div class="fillwidth colspan3 clearfix ">
            <div class="app-inputdiv4">
                <label class="app-input-label">日期类型：</label>
                <input id="rqlx${idSuffix}" name="RQLX"/>
            </div>
            <div class="app-inputdiv8">
                <label class="app-input-label">交易日期：</label>
                <div style="width:35%;float: left;">
                    <input class="coralui-datepicker" id="kssj${idSuffix}" name="KSSJ"/>
                </div>
                <div style="float: left;margin-right: 7%">-</div>
                <div style="width:35%;float: left;">
                    <input class="coralui-datepicker" startDateId="kssj${idSuffix}" id="jssj${idSuffix}" name="JSSJ"/>
                </div>
            </div>
            <div class="app-inputdiv4">
                <label class="app-input-label">药品名称：</label>
                <input id="ypmc${idSuffix}" name="YPMC"/>
            </div>


        </div>
    </ces:form>
    <div class="fillwidth colspan3 clearfix ">
        <div class="toolbarsnav clearfix">
            <ces:toolbar id="toolbarId${idSuffix}" onClick="$.ns('namespaceId${idSuffix}').toolbarClick" align="center"
                         data="[{'label': '查询', 'id':'search', 'disabled': 'false','type': 'button'},{'label': '重置', 'id':'reset', 'disabled': 'false','type': 'button'}]">
            </ces:toolbar>
        </div>

    </div>

    <div id="charts${idSuffix}" style="min-width:700px;">

    </div>

</div>
<script type="text/javascript">

    $.extend($.ns("namespaceId${idSuffix}"), {
        toolbarClick : function ( e , ui ){
            var kssj = $("#kssj${idSuffix}");
            var jssj = $("#jssj${idSuffix}");
            var $form = $("#form${idSuffix}");
            if( ui.id == 'reset' ){
                var $form = $("#form${idSuffix}");
                $form.form("reset");
                kssj.datepicker("setDate","");
                jssj.datepicker("setDate","");
            }
            if( ui.id == 'search' ){
                var $formData = $form.form("formData",false);
                var jsonData = $.loadJson($.contextPath + "/hqfxjjgypcjj!searchj.json?kssj="+$formData.KSSJ+"&jssj="+$formData.JSSJ+"&ypmc="+$formData.YPMC+"&rqlx="+$formData.RQLX);
                initCharts(jsonData);
            }
        }
    });
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'sdzyccjgycjgltj.jsp',//入库量趋势分析页面
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {
                var kssj = $("#kssj${idSuffix}");
                var jssj = $("#jssj${idSuffix}");
                var rqlx = $("#rqlx${idSuffix}");
                var ypmc = $("#ypmc${idSuffix}");
                kssj.datepicker("option","onChange",function ( e ,data){
                    //设置结束日期最小日期
                    jssj.datepicker("option","minDate",data.value);
                });
                jssj.datepicker("option","onChange",function ( e ,data){
                    //设置开始时间最大日期
                    kssj.datepicker("option","maxDate",data.value);
                });

                //给日期类型添加数据和onChange事件
                rqlx.combobox({
                    data:[{value:1,text:'日',selected:true},{value:2,text:'月'},{value:3,text:'年'}],
                    onChange:function( e , data ){
                        if(data.value == 1){
                            kssj.datepicker("option","dateFormat","yyyy-MM-dd");
                            jssj.datepicker("option","dateFormat","yyyy-MM-dd");
                        }else if(data.value == 2){
                            kssj.datepicker("option","dateFormat","yyyy-MM");
                            jssj.datepicker("option","dateFormat","yyyy-MM");
                        }else{
                            kssj.datepicker("option","dateFormat","yyyy");
                            jssj.datepicker("option","dateFormat","yyyy");
                        }
                    }
                });

                var ypmcdata = $.loadJson($.contextPath + "/hqfxjjgypcjj!searchypmc.json")
                ypmc.combobox({
                    data:ypmcdata
                });


            }
        });
    });
    function initCharts(jsonData) {
        $('#charts${idSuffix}').highcharts({
            chart: {
                type: 'line'
            },
            title: {
                text: '药品价格走势图'
            },
            subtitle: {
                text: jsonData.ypmc
            },
            xAxis: {
                categories: jsonData.categories
            },
            yAxis: {
                title: {
                    text: '销售金额(元)'
                }
            },
            plotOptions: {
                line: {
                    dataLabels: {
                        enabled: true
                    },
                    enableMouseTracking: false
                }
            },
            series: jsonData.series
        });
    }




</script>
