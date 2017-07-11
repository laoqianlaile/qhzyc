(function($) {
    // 获取表单工具条系统参数值
    var ftbar = $.loadJson($.contextPath + "/parameter/system-parameter!formToolBarPosition.json");
    $.extend($.config.defaults, {

        tbarPos:{
            form:{
                nested: "top",
                popup: "bottom"

            }

        },
        /**
         * 返回或关闭按钮位置: left/center/right/false
         * 如果设置成false则表示不加返回或关闭按钮
         */
        backBtnPos : {
            form : {
                nested : {
                    top : "right",
                    bottom: "center"
                },
                popup : {
                    top : "center",
                    bottom: "center"
                }
            }
        }
    });
    $.fn['cform'].defaults = {
        backBtn: {cls:"return_tb"}
    };


    $.fn['ctbar'].defaults = {
        processData: function(data, pos) {
            var ui =  this;
            if ("top" === pos) {
                var op = ui.options.op.toString();
                var poss = "";
                // 表单
                if ($.config.contentType.isForm(ui.options.type)) {
                    if (op == '0') {
                        poss =" - 新增";
                    } else if (op == '1') {
                        poss =" - 修改";
                    } else if (op == '2') {
                        poss =" - 详情";
                    }
                    if(data.length == 0){
                        poss =" - 详请";
                    }
                }

                var menuObj = $.loadJson($.contextPath + "/trace!getMenuById.json?id="+ui.options.menuId);
                if(menuObj.name != undefined){
                    if("企业信息" == menuObj.name){
                        menuObj.name = $(".coral-state-active").children().html();
                    }


                    if(ui.options.number ==1 || (ui.options.number === 2 && ui.options.type !== $.config.contentType.form)
                        || $.config.contentType.form !== ui.options.model 
                    ) {
                        data.unshift({
                            "type": "html",
                            "content": "<div class='homeSpan'><div><div style='margin-left:27px;font-size:16px'> - " + menuObj.name + poss + "</div>",
                            frozen: true
                        });
                    }
                }
            }

            return data;
        }
    };
    $('.fancybox-buttons').fancybox({
        'transitionIn'	: 'elastic',
        'transitionOut'	: 'elastic',
        'titlePosition' : 'inside'
    });
})(jQuery);