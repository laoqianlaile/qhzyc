// 获取选中的类型ID
function getCategoryId() {
    var o = {
        status : true
    };
    o.categoryId = tree.getSelectedItemId()
    return o;
}
// 获取选中的列表数据ID
function getModuleIds() {
    var o = {
        status : true
    };
    var selectedModuleIds = dataGrid.getSelectedRowId();
    if (selectedModuleIds == undefined) {
        alert("请先选择记录");
        o.status = false;
    } else {
        o.selectedModuleIds = selectedModuleIds;
    }
    return o;
}
// 获取唯一选中的列表数据ID
function getSingleModuleId() {
    var o = {
        status : true
    };
    var selectedModuleIds = dataGrid.getSelectedRowId();
    if (selectedModuleIds == undefined) {
        alert("请先选择记录");
        o.status = false;
    } else if (selectedModuleIds.indexOf(",") != -1) {
        alert("只能选择一条记录");
        o.status = false;
    } else {
        o.selectedModuleId = selectedModuleIds;
    }
    return o;
}