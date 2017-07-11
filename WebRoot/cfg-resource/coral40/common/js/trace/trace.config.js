/**
 * 食品追溯saas组件配置文件
 * Created by Synge on 2015/10/29.
 */
(function($){
    //下拉框可搜索
    $.fn['combobox'].defaults = {
        enableFilter: true,
        readonlyInput: false,
        enablePinyin: true

    };
    //取消文本框隐藏值，使得js的.val()赋值方法可用
    $.fn['textbox'].defaults = {
        useHiddenInput: false
    };
    //下拉列表可搜索
    $.fn['combogrid'].defaults = {
        enableFilter: true,
        readonlyInput: false
    };
    //取消所有输入框tooltip:还可输入n个文字
    $.validate.validTypeOptions = {
        "maxlength": {
            restrictInput: true,
            showTooltip: true
        },
        "number": {
            restrictInput: true
        }
    };
})(jQuery);
