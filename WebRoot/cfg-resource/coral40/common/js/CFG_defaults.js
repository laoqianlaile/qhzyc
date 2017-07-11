(function($) {
    $.config = $.config || {};
    $.config.defaults = $.config.defaults || {};
    
    // 获取表单工具条系统参数值
    var ftbar = $.loadJson($.contextPath + "/parameter/system-parameter!formToolBarPosition.json");
    
    $.extend($.config.defaults, {
    	/**
         * 系统配置平台默认，提示窗口（可以根据项目自身需求进行复写）
         * @param message
         * @param type --success 成功
         *             --warning 警告/失败
         *             --error   错误
         * */
        message : function (message, type) {
        	if (!type) {
        		type = "info";
        	} else if ("error" === type) {
        		type = "danger";
        	}
        	$.message(message, type);
        },
        
        /**
         * 自定义表单查看详细方式：
         *   readonly -- 只读模式
         *   isLabel  -- 阅读模式
         */
        viewModel : "readonly",
        
        /**
         * 工具条默认位置：top/bottom/both
         */
        tbarPos: {
        	//grid: "top",
        	form: {
        		// 嵌入
        		nested: ftbar.nested,
        		// 弹出
        		popup : ftbar.popup
        	}
        },
        
        /**
         * 返回或关闭按钮位置: left/center/right/false
         * 如果设置成false则表示不加返回或关闭按钮
         */
        backBtnPos : {
        	form : {
        		// 嵌入式表单
        		nested : {
            		top : "left",
            		bottom: "center"        			
            	},
            	// 弹出式表单
    	    	popup : {
    	    		top : "left",
    	    		bottom: "right"
    	    	}
        	}
        }
    });
    
    
})(jQuery);