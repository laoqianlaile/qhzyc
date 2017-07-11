<style>
    #gps-map-container{
        width:800px;
        height:550px;
    }
    #gps-map-container img{
        margin:0;
    }
</style>
<div id="max${idSuffix}" class="fill">
<div id="gps-map-container"></div>
</div>
<script>
    $(function() {
        var configInfo = CFG_initConfigInfo({
            /** 页面名称 */
            'page' : 'sdzyccjgclgpsxx.jsp',//入库量趋势分析页面
            /** 页面中的最大元素 */
            'maxEleInPage' : $('#max${idSuffix}'),
            /** 页面初始化的方法 */
            'bodyOnLoad' : function(configInfo) {

                var rowDataId = CFG_getInputParamValue(configInfo, 'inputParam'); // 获取构件输入参数
                var gpsObj = $.loadJson($.contextPath +"/sdzyccjgclsbxx!searchSbh.json?id="+rowDataId );
                var loadScript = function(url, callback){
                    var script = document.createElement( "script" )
                    script.type = "text/javascript";
                    if(script.readyState) {  //IE
                        script.onreadystatechange = function() {
                            if ( script.readyState === "loaded" || script.readyState === "complete" ) {
                                script.onreadystatechange = null;
                                callback();
                            }
                        };
                    } else {  //Others
                        script.onload = function() {
                            callback();
                        };
                    }

                    script.src = url;
                    document.getElementsByTagName( "head" )[0].appendChild( script );
                };
                var map,mark;

                var loadGpsData = function( callback ) {
                    $.getJSON("http://123.232.26.175:8080/0531yun/wsjc/Device/getDeviceData.do?userID=160801hjtzy&userPassword=160801hjtzy", function(data){
                        var id =gpsObj.SBH;
                        if (Object.prototype.toString.call(data) === '[object Array]'){
                            data.forEach(function(device){
                                if (device.DevKey && device.DevKey === id){
                                    callback([device.DevLng, device.DevLat]);
                                }
                            });
                        }
                    });
                };

                var markMap = function() {
                    loadGpsData(function(position) {
                        map.setCenter(position);
                        mark = new AMap.Marker({
                            position: position,
                            map:map
                        });
                    });
                };

                var moveMark = function() {
                    loadGpsData(function(position) {
                        mark.moveTo(position,40);
                    });
                };

                var initMap = function() {
                    loadScript("http://webapi.amap.com/maps?v=1.3&key=sdzyc_gps", function() {
                        map = new AMap.Map('gps-map-container');
                        map.setZoom(12);

                        AMap.plugin(['AMap.ToolBar','AMap.Scale'],function(){
                            var toolBar = new AMap.ToolBar();
                            var scale = new AMap.Scale();
                            map.addControl(toolBar);
                            map.addControl(scale);
                        });

                        markMap();
                        var intervalId = window.setInterval(moveMark ,1000 * 30);
                    });
                };

                initMap();

            }
        });
    });
</script>