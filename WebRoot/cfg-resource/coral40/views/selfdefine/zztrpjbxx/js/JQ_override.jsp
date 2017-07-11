<%@page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.ces.config.utils.CommonUtil" %>
<%--<%request.setAttribute("idSuffix", CommonUtil.generateUIId(""));--%>
<%--%>--%>
<%--<style>--%>
    <%--input [name=BZGG]{--%>
        <%--width: 80%;--%>
        <%--background-color:red ;--%>
    <%--}--%>
<%--</style>--%>
<script type="text/javascript">
    /***************************************************!
     * @author qiucs
     * @date   2014-7-15
     * 系统配置平台应用自定义二次开发JS模板
     ***************************************************/


    (function (subffix) {

        /**
         * 二次开发定位自己controller
         * @returns {String}
         **/
        window[CFG_actionName(subffix)] = function (ui) {
            // ui.assembleData 就是 configInfo
            return $.contextPath + "/zztrpjbxx";
        };

        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
//            ui.
            ui.beforeSave = function(jq,options){

                var id=ui.getItemValue("ID");
                var trpmc=ui.getItemJQ("TRPMC").val();
                //保证投入品名称不能重复
                var jsonData = $.loadJson($.contextPath + "/zztrpjbxx!checkTRPMC.json?trpmc="+trpmc+"&id="+id+"");

                if(jsonData.result == "ERROR"){

                    //怎么找到那些选择框，获取其中的值，往里面写入值。
                    //  ui.getItemJQ("TYM").textbox("setValue","");
                    ui.setItemValue("TRPMC","");
                    CFG_message(jsonData.msg, "error");
                    return false;
                }
                //保证通用名不能重复
                var tym=ui.getItemValue("TYM");
                var lx=ui.getItemValue("LX");
                jsonData= $.loadJson($.contextPath+"/zztrpjbxx!checkTYM.json?tym="+tym+"&lx="+lx+"&id="+id+"");
                if(jsonData.result=="ERROR"){
                    ui.setItemValue("TYM","");
                    CFG_message(jsonData.msg,"error");
                    return false;
                }

            }
            ui._init = function () {
                //var trpbh=$("#TRPBH_${timestamp}");
                var trpmc=ui.getItemJQ("TRPMC");
                trpmc.selector;
                var number=trpmc.selector.indexOf('_');
                var timstam=trpmc.selector.substring(number);
                var bzgg="BZGG"+timstam+"_DIV";
                var fllx="FLLX"+timstam+"_DIV";
                var yt="YT"+timstam+"_DIV";
                var tjyl="TJYL"+timstam+"_DIV";
                var zxbz="ZXBZ"+timstam+"_DIV";
                var yxcf="YXCF"+timstam+"_DIV";
                var aqq="AQQ"+timstam+"_DIV";
                var pp="PP"+timstam+"_DIV";
                var xh="XH"+timstam+"_DIV";
                var ml="ML"+timstam+"_DIV";
                var bz="BZ"+timstam+"_DIV";
                var lx = ui.getItemJQ("LX");
//                var s=document.getElementById(bzgg);
//                var botton1=document.createElement("button");
//                botton1.innerHTML='按钮';
//                botton1.style.display='inline-block';
//                s.children[1].children[1].parentNode.appendChild(botton1);
//                s.children[1].children[1].style.width="180px";
                <%--document.getElementById('BZGG${idSuffix}').style.width="80%"--%>

                //新增
                if(isEmpty(ui.options.dataId)){


                    //var xh="XH"+timstam+"_DIV";
                    document.getElementById(bzgg).style.display='none';
                    document.getElementById(fllx).style.display='none';
                    document.getElementById(yt).style.display='none';
                    document.getElementById(tjyl).style.display='none';
                    document.getElementById(zxbz).style.display='none';
                    document.getElementById(yxcf).style.display='none';
                    document.getElementById(aqq).style.display='none';
                    document.getElementById(pp).style.display='none';
                    document.getElementById(xh).style.display='none';
                    document.getElementById(ml).style.display='none';
                }
                //修改和详细页面
                if(!isEmpty(ui.options.dataId)){
                    //修改页面中的初始化操作
                    if(ui.getItemJQ("LX").combobox('getText')=='农药'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(yt).style.display="inline";
                        document.getElementById(zxbz).style.display="inline";
                        document.getElementById(yxcf).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                        document.getElementById(aqq).style.display="inline";
                    }else if(ui.getItemJQ("LX").combobox('getText')=='肥料'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(yt).style.display="inline";
                        document.getElementById(zxbz).style.display="inline";
                        document.getElementById(yxcf).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                        document.getElementById(aqq).style.display="inline";
                        document.getElementById(fllx).style.display="inline";
                        document.getElementById(bz).style.display="block";
                    }else if(ui.getItemJQ("LX").combobox('getText')=='种子'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                    }else if(ui.getItemJQ("LX").combobox('getText')=='农机具'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(pp).style.display='inline';
                        document.getElementById(xh).style.display='inline';
                        document.getElementById(ml).style.display='inline';
                    }

                }



                if (isEmpty(ui.options.dataId)) {
                    var zztrpbh = $.loadJson($.contextPath + '/zztrpjbxx!getZztrpbh.json');
                    var qyObj = $.loadJson($.contextPath + "/trace!getQybmAndQymc.json")
                    ui.getItemJQ("TRPBH").textbox('setValue', zztrpbh);
                    ui.setFormData({QYBM: qyObj.qybm});
                }
//                if ((ui.getItemJQ("LX").combobox('getValue')) != '2') {
//                    ui.getItemJQ("FLLX").combobox('disable');
//                }


//                var fllx = ui.getItemJQ("FLLX");
                lx.combobox('option', 'onChange', function (e, data) {
                   // ui.getItemJQ('TRBMC').textbox('setValue','');
                   // ui.getItemJQ('TYM').textbox('setValue','');
                    //档类型发生变化时，清空数据
                    ui.getItemJQ('BZGG').textbox('setValue','');
                    ui.getItemJQ('TJYL').textbox('setValue','');
                    ui.getItemJQ('YT').textbox('setValue','');
                    ui.getItemJQ('ZXBZ').textbox('setValue','');
                    ui.getItemJQ('YXCF').textbox('setValue','');
                    ui.getItemJQ('FLLX').combobox('setValue','');
                    ui.getItemJQ('AQQ').textbox('setValue','');
                    ui.getItemJQ('PP').textbox('setValue','');
                    ui.getItemJQ('XH').textbox('setValue','');
                    ui.getItemJQ('ML').textbox('setValue','');

                    var bewlx = data.text;
                    if(bewlx=='农药'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(yt).style.display="inline";
                        document.getElementById(zxbz).style.display="inline";
                        document.getElementById(yxcf).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                        document.getElementById(aqq).style.display="inline";
                    }
                    else if(bewlx=='肥料'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(yt).style.display="inline";
                        document.getElementById(zxbz).style.display="inline";
                        document.getElementById(yxcf).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                        document.getElementById(aqq).style.display="inline";
                        document.getElementById(fllx).style.display="inline";
                        document.getElementById(bz).style.display="block";
                    }else if(bewlx=='种子'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(bzgg).style.display="inline";
                        document.getElementById(tjyl).style.display="inline";
                    }else if(bewlx=='农机具'){
                        document.getElementById(bzgg).style.display='none';
                        document.getElementById(fllx).style.display='none';
                        document.getElementById(yt).style.display='none';
                        document.getElementById(tjyl).style.display='none';
                        document.getElementById(zxbz).style.display='none';
                        document.getElementById(yxcf).style.display='none';
                        document.getElementById(aqq).style.display='none';
                        document.getElementById(pp).style.display='none';
                        document.getElementById(xh).style.display='none';
                        document.getElementById(ml).style.display='none';
                        document.getElementById(pp).style.display='inline';
                        document.getElementById(xh).style.display='inline';
                        document.getElementById(ml).style.display='inline';
                        //在这个后面加一个换行符调整页面样式

                    }
//                    if (bewlx == '2') {
//                        fllx.combobox('enable');
//                    } else {
//                        fllx.combobox('disable');
//                        fllx.combobox('setValue', '');
//                    }
                });
            }
        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            ui.beforeInitGrid = function(setting) {
                setting.fitStyle = "width";
                return setting;
            };
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义树
         */
        function _override_tree(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override tree!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {

//            var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
//            var op = ui.options.op.toString();
//            var type = ui.options.type;// 0 表单 1 列表
//            var poss = "";
//            if(op == '0') {
//                poss =" - 新增";
//            }else if(op == '1') {
//                poss =" - 修改";
//            }else if(op == '2') {
//                poss =" - 详细";
//            }
//            //ui.options.backBtnPos : null
//            ui.processItems = function (data,pos) {
//                var btns = [];
//                if(type == 0){//说明是表单
//                    if(pos == 'top') {// 在顶部的时候添加当前位置
////                       return [{
////                            "type": "html",
////                            "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - " + menuObj.name + poss + "</div>",
////                            frozen: true
////                        }];
//                    }else {
//                        for (var i = 0; i < data.length; i++) {
//                            btns.push(data[i]);
//                        }
//                    }
//                }else{
////                    btns.push({
////                        "type": "html",
////                        "content": "<div class='homeSpan'><div><div style='margin-left:20px'> - " + menuObj.name + poss + "</div>",
////                        frozen: true
////                    });
//                    for (var i = 0; i < data.length; i++) {
//                        btns.push(data[i]);
//                    }
//                }
//
//                return btns;
//            };
        };
        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {

        };

        /**
         *  二次开发：复写基本查询区
         */
        function _override_bsearch (ui) {

        };
        /**
         *  二次开发：复写高级查询区
         */
        function _override_gsearch (ui) {

        };







        /**
         * 在此可以复写所有自定义JS类
         * @param selector
         * @returns {JQ_override}
         */
        window[CFG_overrideName(subffix)] = function () {
            //var startTime = new Date().getTime();

            if (this instanceof $.config.cform) {
                _override_form(this);
            } else if (this instanceof $.config.cgrid) {
                _override_grid(this);
            } else if (this instanceof $.config.cbsearch) {
                _override_bsearch(this);
            } else if (this instanceof $.config.cgsearch) {
                _override_gsearch(this);
            } else if (this instanceof $.config.ctree) {
                _override_tree(this);
            } else if (this instanceof $.config.ctbar) {
                _override_tbar(this);
            } else if (this instanceof $.config.clayout) {
                _override_layout(this);
            }

            //console.log("over ride cost time: " + (new Date().getTime() - startTime));
        };




    })("${timestamp}");
</script>
