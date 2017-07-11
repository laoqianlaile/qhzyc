(function($){
	$.fn['grid'].defaults = {
	    loadui: "disable"
	};
	/*
	$.fn['textbox'].defaults = {
		showStar: false
	};
	$.fn['datepicker'].defaults = {
		showStar: false
	};
	$.fn['radio'].defaults = {
		showStar: false
	};
	$.fn['checkbox'].defaults = {
		showStar: false
	};
	$.fn['radiolist'].defaults = {
		showStar: false
	};
	$.fn['checkboxlist'].defaults = {
		showStar: false
	};
	$.fn['combobox'].defaults = {
		showStar: false
	};
	$.fn['accordion'].defaults = {
		icons: "{activeHeader:'coral-icon-down', header:'coral-icon-right'}"
	};	
	$.fn['dialog'].defaults = {
		position: {
			my: "center",
			at: "center top+200",
			of: window,
			collision: "fit",
			using: function( pos ) {
				var topOffset = $( this ).css( pos ).offset().top;
				if ( topOffset < 0 ) {
					$( this ).css( "top", pos.top - topOffset );
				}
			}
		}
	};
	$.fn['combobox'].defaults = {
		enableSearch: true
	};
	$.fn['combotree'].defaults = {
		enableSearch: true
	};
	$.fn['combogrid'].defaults = {
		enableSearch: true
	};
	$.message.defaults = {
		position: {
			my: "center",
			at: "center top+200",
			of: window
		}
	};*/
	$.fn['datepicker'].defaults = {
		yearRange: "c-90:c+90"
	};
	$.fn['form'].defaults = {
		focusFirst: true
	};
	$.fn['combobox'].defaults = {
		placeholder: "请选择",
		showClose: true
	};
	$.validate.validTypeOptions = {
        "maxlength": {
            restrictInput: true,
            showTooltip: true
        },
        "number": {
            restrictInput: true
        }
    };
	$.fn['toolbar'].defaults = {
		autoDisplay : true,
		clickToDisplay : "1" //"0"鼠标移动上去展开 "1"鼠标移动上去点击展开
	};
	$.fn['tree'].defaults = {
        showLine : false,
        showIcon : false,
        selectedMulti : false,
        dblClickExpand : false,
      //左侧树样式diy
        addDiyDom : function (treeId, treeNode) {
            var spaceWidth = 15;
            var switchObj = $("#" + treeNode.tId + "_switch"), icoObj = $("#" + treeNode.tId + "_ico");
            switchObj.remove();
            icoObj.before(switchObj);
            if (treeNode.level > 0) {
                var spaceStr = "<span style='display: inline-block;width:" + (spaceWidth * treeNode.level) + "px'></span>";
                switchObj.before(spaceStr);
            }
        }
    };
	$.fn['dialog'].defaults = {
			focusInput: true
	};
	$.fn['ctbar'].defaults = {
			processData: null
	};
	$.message.defaults = {
			queue: true
	};
	/*********************** 典型页面的树处理（start） *************************/
    // 处理tree事件 begin
    $(document).on("mouseenter", ".coral-tree", function ( e ) {
        if (!$(this).hasClass("showIcon")) {
            $(this).addClass("showIcon");
        }
    }).on("mouseleave", ".coral-tree", function ( e ) {
        $(this).removeClass("showIcon");
    });
    /*********************** 典型页面的树处理（end） *************************/
})(jQuery);