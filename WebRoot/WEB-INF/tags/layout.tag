<%@attribute name="id"%><%@ attribute name="cls" %><%@ attribute name="componentCls" %><%@ attribute name="rendered" type="java.lang.Boolean" %><%@ attribute name="authorized" %><%@attribute name="onCreate"%><%@attribute name="name"%><%@attribute name="fit" type="java.lang.Boolean"%><%@attribute name="style"%><%@include file="TagUtil.jsp" %><% 
id = tagUtil.getClientId( id );
String dataOption = tagUtil.add("id", id)
.add("cls", cls)
.add("componentCls", componentCls)
.add("rendered", rendered).add("authorized", authorized)
.add("onCreate", onCreate)
.add("name", name)
.add("fit", fit).toString();
%><div id="<%=id %>" class="<%=cls==null?"":cls%>" <%=(style == null ? "" : "style=" + style) %> data-options="<%=dataOption %>"><jsp:doBody /></div>
<script <%= "id=" + id + "_s" %>>
Coral.cc("layout", "<%= id%>");
</script>