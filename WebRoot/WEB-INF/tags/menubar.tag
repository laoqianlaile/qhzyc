<%@attribute name="id"%><%@ attribute name="cls" %><%@ attribute name="componentCls" %><%@ attribute name="rendered" type="java.lang.Boolean" %><%@ attribute name="authorized" %><%@attribute name="name"%><%@attribute name="data"%><%@attribute name="disabled"%><%@attribute name="url"%><%@attribute name="method"%><%@attribute name="onclick"%><%@attribute name="onClick"%><%@attribute name="onCreate"%><%@include file="TagUtil.jsp" %><% 
id = tagUtil.getClientId( id );
String dataOption = tagUtil.add("disabled", disabled)
.add("id", id)
.add("cls", cls)
.add("componentCls", componentCls)
.add("rendered", rendered).add("authorized", authorized)
.add("method", method)
.add("url", url)
.add("id", id)
.add("data", data)
.add("name", name)
.add("onClick", onClick)
.add("onCreate", onCreate).toString();
%><ul <%=(id == null ? "" : "id="+id) %> class="<%=cls==null?"":cls%>" data-options="<%=dataOption %>">
<jsp:doBody />
</ul>
<script <%= "id=" + id + "_s" %>>
Coral.cc("menubar", "<%= id%>");
</script>