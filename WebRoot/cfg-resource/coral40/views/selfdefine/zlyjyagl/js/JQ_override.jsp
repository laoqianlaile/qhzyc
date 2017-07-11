<%@page language="java" pageEncoding="UTF-8" %>
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
            return $.contextPath + "/zlyjyagl";
        };


        /**
         * 二次开发：复写自定义表单
         */
        function _override_form(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        };
        /**
         *  二次开发：复写自定义列表
         */
        function _override_grid(ui) {
            // ui.assembleData 就是 configInfo
            //console.log("override grid!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            ui.linkDeleteGD = function (id) {
                var _this = ui;
                $.confirm("当前记录将从列表移除，确定吗？", function (sure) {
                    if (sure) {
                        $.ajax({
                            type: 'POST',
                            dataType: 'JSON',
                            url: $.contextPath + '/zlyjyagl!deleteYjya.json',
                            data: {yjyaId: id},
                            success: function (data) {
                                CFG_message("操作成功", "success");
                                _this.uiGrid.grid("delRowData", id);
                            },
                            error: function () {
                                CFG_message("操作失败", "error");
                            }
                        });
                    }
                });
            };

            ui._init = function () {
                var sasa = document.getElementById('back2grid');
                sasa.remove();
                $("div[class=homeSpan]").children().children().html(" - 预案管理");
            };


            ui.clickSecondDev = function (id) {
                if (id == $.config.preButtonId + "fanhui") {
                    menuClick(
                            '自定义预案统计',
                            '/cfg-resource/coral40/views/component/zlyjya/zlyjya.jsp?menuId=408a96265243af8a015243bca3fc0000&constructId=402881f24fde174c014fde7a0304001c&componentVersionId=402881f24fde174c014fde7a02f5001b&___t=1453100670108');
                }
                if(id == $.config.preButtonId+"delete"){
                    var rowId = ui.getSelectedRowId();//获取选中ID
                    if(rowId.length >0){
                        $.confirm("当前记录将从列表移除，确定吗？", function (sure){
                            if(sure){
                                $.ajax({
                                    type:'POST',
                                    dataType:'JSON',
                                    url: $.contextPath + '/zlyjyagl!deleteYjya.json',
                                    data:{yjyaId:rowId.toString()},
                                    success:function(data){
                                        CFG_message("操作成功","success");
                                        //重新加载数据
                                        ui.uiGrid.grid("reload");

                                    },
                                    error:function(data){
                                        CFG_message("操作失败","error");
                                    }
                                });
                            }
                        });
                    }else{
                        CFG_message("请选择一条记录删除");
                    }
                }
            };

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
        }
        ;
        /**
         *  二次开发：复写自定义工具条
         */
        function _override_tbar(ui) {

            // ui.assembleData 就是 configInfo
            //console.log("override tbar!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
            ui.processItems = function (data) {
                var btns = [];
                btns.push('->');
                btns.push(data[2]);
                btns.push(data[3]);
                btns.push({
                    id: $.config.preButtonId + "delete",
                    label:"删除",
                    type:"button"
                });
                btns.push({
                    id: $.config.preButtonId + "fanhui",
                    label: "返回",
                    type: "button"
                });
                return btns;
            }
        }
        ;
        /**
         *  二次开发：复写自定义布局
         */
        function _override_layout(ui) {
            //console.log("override layout!");
            //ui.getAction = function () {
            //	return $.contextPath + "/appmanage/show-module";
            //};
        }
        ;

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
