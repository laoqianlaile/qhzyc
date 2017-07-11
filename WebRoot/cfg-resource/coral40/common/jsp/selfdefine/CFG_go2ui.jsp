<script type="text/javascript">
//var start = new Date().getTime();
new $.config.router({
		menuId : "${menuId}",
		componentVersionId : "${componentVersionId}",
		moduleId: "${moduleId}",
		tableId : "${tableId}",
		groupId : "${groupId}",
		columns : "${columns}",
		dataId  : "${dataId}",
		master  : {tableId:"${masterTableId}", dataId: "${masterDataId}"},
		appendTo: $("#mtc_${timestamp}"),
		timestamp    : "${timestamp}",
		masterCgridDivId: "${masterCgridDivId}",
		paramMap: ${paramMap}
});
//console.log("selfdefine total: " + (new Date().getTime() - start));
</script>
