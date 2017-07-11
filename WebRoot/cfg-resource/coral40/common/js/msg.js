(function($) {
    $.msg = {
        defaults : {
            emptyRecords : "无数据显示",
            loadText : "数据加载中...",
            selectRow : "请选择需要修改的记录",
            onlyOneRow : "只能选择一条数据！"
        },
        search : {
            nowSearch : "搜索中..."
        },
        save : {
            saveData : "数据已改变，是否保存？",
            saveSuccess : "保存成功!",
            saveFail : "保存失败!"
        },
        del : {
            deleteData : "删除所选记录？",
            selectRow : "请选择需要删除的记录！",
            deleteSuccess : "删除成功!",
            deleteFail : "删除失败!"
        },
        errors : {
            errMsg : "处理过程错误"
        },
        checkUnique : {
            notUnique : "已存在，请重新输入！",
            errMsg : "检查出错，请联系管理员！"
        },
        archiverage : {
            notUnique : "分类代码已存在，请重新输入代码！",
            questionDelete : "所选择的记录将被删除，其下子分类将会同时删除，确定吗？",
            yearNotUnique : "存在同全宗同年度的节点，请修改！"

        }
    };
})(jQuery);
