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
<style type="text/css">
    /*<![CDATA[*/
    .thumb {
        height: 75px; /*给缩略图指定单个维度，浏览器自动等比缩放*/
        border: 1px solid #000;
        /*margin: 10px 5px 0 0;*/
        position: absolute;
    }


    #allmap{
        margin:50px;
        height:60%;
    }

    /*li {color: red;}*/
    /*]]>*/
</style>
<html>
<head>
    <base href="<%=basePath%>">

    <title>同批追踪</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">

    <style type="text/css">
        .colspan3 .app-inputdiv4 .inputfile {
            width: 62.5%;
        }

        .top0form {
            padding-top: 0px;
        }
        #coral-tabs-nav {position:absolute;top:100px}

    </style>
</head>
<body>
<div id="max${idSuffix}"  style="overflow: scroll;height:550px ">

        <div id="grid${idSuffix}" style="height:550px">

            </div>
        <div id="allmap" style="position:absolute;margin-top:10px;left:0%;right:0%;weight:100%;height: 32%;display: block;z-index:12"></div>


</div>

<script>

    $.getScript("http://api.map.baidu.com/api?v=15&ak=5bf855579378e7f83002e94a5b62c806");
    //显示地图功能
    function showmap(datamap){
        $.getScript("http://api.map.baidu.com/getscript?v=2.0&ak=5bf855579378e7f83002e94a5b62c806&services=&t=20150901171226"
                ,function aa () {
                        function createmap(){
                            //创建一个地图容器
                            var map = new BMap.Map("allmap");
                            //定义坐标
                            //var point = new BMap.Point("湖南省");
                            //设置地图初始界面中心点
                            map.centerAndZoom("上海");
                            map.enableScrollWheelZoom();//开启鼠标滚动
                            return map;
                        }

                        function addControl(zmap){
                            zmap.addControl(new BMap.NavigationControl());
                            zmap.addControl(new BMap.NavigationControl());
                            zmap.addControl(new BMap.ScaleControl());
                            zmap.addControl(new BMap.OverviewMapControl());
                            zmap.addControl(new BMap.MapTypeControl());
                        }

                        var map = createmap();
                        //添加控件
                        addControl(map);

                        function addMarker(point, index,map,data){


                            var myIcon = new BMap.Icon("http://app.baidu.com/map/images/us_mk_icon.png", new BMap.Size(23, 22), {
                                offset: new BMap.Size(10, 25),

                                imageOffset: new BMap.Size(0, 0 - index * 25) // 设置图片偏移
                            });

                            var marker = new BMap.Marker(point, {icon: myIcon});
                            //var lable = new BMap.Label("这是一个提示",{offset:new BMap.Size(20, 5)});
                            //marker.setLabel(lable);
                            marker.addEventListener("click", function(){
                                var img = document.createElement("img");
                                img.src ="D:/idea/config1.0/WebRoot/spzstpfj/"+ data.TPBCMC;
                                img.style.width = "220px";
                                img.style.height = "100px";
                                var opts = {
                                    width : 150, //弹出框宽度
                                    height: 250, // 弹出框高度
                                    title :img //标题
                                }
                                var infoWindow = new BMap.InfoWindow("<span style='font-size: 14px'>"+"门店名称:"+data.KHMC+"</span><br><span style='font-size: 14px'>"+"联系人："+data.LXR+"</span><br><span style='font-size: 14px'>"+"联系电话:"+data.DH+"</span><br><span style='font-size: 14px'>"+"地址："+data.DZ+"</span>", opts); // 设置标注弹出标题和内容
                                map.openInfoWindow(infoWindow,point);
                            });


                            map.addOverlay(marker);
                        }

                        function what(){
                            alert('say something');
                        }
//                var point = new BMap.Point(121.4, 31.2);
//                addMarker(point, 0,map,null);

                        if(datamap!=null){
                            for(var i=0;i<datamap.length;i++){
                                var jd=datamap[i].JD;
                                var wd=datamap[i].WD;
                                var point = new BMap.Point(jd, wd);
                                addMarker(point, 0,map,datamap[i]);
                            }
                        }


                    }
        )
    }

    //


    $(function () {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page': 'tpzzlb.jsp',
            /** 页面中的最大元素 */
            'maxEleInPage': $('#max${idSuffix}'),
            /** 获取构件嵌入的区域 */
            'getEmbeddedZone': function () {
                //return $('#layoutId${idSuffix}').layout('panel', 'north');
                return $('#grid${idSuffix}');
            },

            'bodyOnLoad': function (configInfo) {

                var lbConstructDetails = CFG_getReserveZoneInfo(configInfo, "tpzzlb", '2');
                configInfo.addMarkergrid = function(datamap){
                    showmap(datamap);
                   // addMarker(point, index,map,data);
                }
                if (lbConstructDetails && lbConstructDetails.length > 0) {
                    var lbConstructDetail = lbConstructDetails[0];
                    CFG_openComponent(configInfo, lbConstructDetail.bindingComponentUrl, "", false);

//                    configInfo.lbChildConfigInfo = lbChildConfigInfo.childConfigInfo;
//                  lbChildConfigInfo.parentConfigInfo = configInfo;
                }
                //页面初始化加载一个空的页面
                var cc = [{}];
                showmap();


                if (configInfo.notAuthorityComponentButtons) {
                    $.each(configInfo.notAuthorityComponentButtons, function (i, v) {
                        if (v == 'add') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'add');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'add');
                        } else if (v == 'update') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'update');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'update');
                        } else if (v == 'delete') {
                            //$('#toolbarId${idSuffix}').toolbar('disableItem', 'delete');
                            $('#toolbarId${idSuffix}').toolbar('hide', 'delete');
                        }
                    });
                }
            }
        });
    });
</script>
</body>
</html>