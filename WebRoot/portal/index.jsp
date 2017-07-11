<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/portal/jsp/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<utility:base target="easyui" themes="bootstrap"  packages="portal" />
	<style type="text/css">
		#portal {
			position: relative;
			overflow: hidden;
			padding: 0 0.5% 0.5% 0.5%;
		}
		
		.portal-item{height: 100%; padding: 0;}
		.portal-item-both {width: 30%;}
		.portal-item-center {width: 40%;}
		.portal-item-split {width: 0.5%;background-color: #fff;}
		
		.portal-panel {margin: 8px 0 0 0!important;}
		.portal-content {width: 100%;height: 100%;}
		
		.panel-title {
			font-size: 12px !important;
			font-weight: normal !important;
			margin-left: 5px !important;
		}
		
		.panel-heading {
			display: inline;
			padding: 8px 15px;
			border-top-left-radius: 2px;
			border-top-right-radius: 2px;
		}
	</style>
</head>
<body>

	<div id="portal">
		<div class="portal-item portal-item-both">
			<div title="运维人员动态" iconCls="icon-zico05">
				<table style="width: 100%;height: 100%;" >
					<tr align="center">
						<td>地址</td>
						<td>额定人员</td>
						<td>在岗人员</td>
					</tr>
					<tr align="center">
						<td>张江</td>
						<td>10</td>
						<td>9</td>
					</tr>
					<tr align="center">
						<td>华阳路</td>
						<td>7</td>
						<td>7</td>
					</tr>
					<tr align="center">
						<td>300号</td>
						<td>6</td>
						<td>6</td>
					</tr>
				
				</table>
			</div>
			<div title="故障工单统计" iconCls="icon-zico08" class="maximizable is-tabs">
			</div>
		</div>
		<div class="portal-item portal-item-split"></div>
		<div class="portal-item portal-item-center">
			<div iconCls="icon-zico01">
			</div>
			<div title="设备统计" iconCls="icon-zico04" class="maximizable">
			</div>
		</div>
		<div class="portal-item portal-item-split"></div>
		<div class="portal-item portal-item-both"  >
			<div title="用户节点统计" iconCls="icon-zico05" class="maximizable">
			<div id="userNode" class="portal-content" type="force" style="width:100%;height: 100%;float: left;"></div>
			</div>
			<div title="告警统计" iconCls="icon-zico07" class="maximizable" >
			</div>
			
		</div>
	</div>
	
	<div id="dialog"></div>
	
    
    <!-- 为适应面板最大化时重绘图表，对下列方法进行改造 -->
    
    <script type="text/javascript">
	
		// 面板最大化事件
	
		
		$(function(){
			$('#portal').portal({
				fit: true,
				border: false,
				fixheight: 9
			});
			
               // 用户节点统计
	    	require(['echarts','echarts/chart/bar'],userNode);
		});
		
		// 为面板添加最大化按钮
			$(".maximizable").each(function(i,n){
				var node = $(n).find("div.portal-content");
				var param = '"'+node.attr("id")+'","'+node.attr("type")+'"';
				$(n).prev("div.panel-header")
					.find("div.panel-tool")
					.append('<a class="panel-tool-max" href="javascript:void(0);" onclick=maxPanel('+param+');/>');
			});
	</script>
   <script src="${ctx}/portal/js/echarts/build/dist/echarts.js"></script>
	<script type="text/javascript">
		require.config({
            paths: {
                echarts: '${ctx}/portal/js/echarts/build/dist/'
            }
        });
        var areaName = "all";
     	var areaTitle = "";
    </script>
    <script type="text/javascript">
   		//用户节点统计
		var userNodeChart;
        function userNode(ec) {
        	userNodeChart = ec.init(document.getElementById('userNode'));
			userNodeChart.showLoading({
			    text : '努力加载数据中...',
			    textStyle : {
			        fontSize : 20
			    }
			});
			
			option = {
    title : {
        text: '某地区蒸发量和降水量',
        subtext: '纯属虚构'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['蒸发量','降水量']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
            type : 'value'
        }
    ],
    series : [
        {
            name:'蒸发量',
            type:'bar',
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'},
                    {type : 'min', name: '最小值'}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name: '平均值'}
                ]
            }
        },
        {
            name:'降水量',
            type:'bar',
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
            markPoint : {
                data : [
                    {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
                    {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
                ]
            },
            markLine : {
                data : [
                    {type : 'average', name : '平均值'}
                ]
            }
        }
    ]
};
           
			
            userNodeChart.setOption(option);
            userNodeChart.hideLoading();//取消loading
        }
   </script>
</body>
</html>