
/**
* 组织机构树初始化
* @param that
*/
function initOrganizeTree(that) {
	TREE_URL = contextPath+"/appmanage/organize";	
	var moduleUrl = contextPath+"/appmanage";
	var rootId = "-1";
	var aLayout = that.aLayout;
	tree = aLayout.attachTree();
	that.tree = tree;
	tree.setImagePath(IMAGE_PATH + "csh_scbrblue/");
	tree.attachEvent("onMouseIn", function(id) {
		tree.setItemStyle(id, "background-color:#D5E8FF;");
	});
	tree.attachEvent("onMouseOut", function(id) {
		tree.setItemStyle(id, "background-color:#FFFFFF;");
	});
	var treeJson = {id:0, item:[{id: rootId,text:"组织机构", im0:"safe_close.gif", im1:"safe_open.gif", im2:"safe_close.gif",child:1}]};	
	
	tree.setDataMode("json");
	tree.enableSmartXMLParsing(true);
	var url = TREE_URL + "!treeOforg.json?E_model_name=tree&open=2";
	tree.setXMLAutoLoading(url);
	tree.loadJSONObject(treeJson);
	tree.refreshItem(rootId);
  	tree.selectItem(rootId, false, "0");
	tree.attachEvent("onClick", function(nId) {
		nodeId = nId;
		treeName = tree.getItemText(nId);
		initOrganizeGrid(that, nodeId, treeName);
	});
	that.reloadTreeItem = function() {
		tree.refreshItem(nodeId);
	};
} 