
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="streetVisList">
    <s:property value="type" /> <s:property value="name" />|<s:property value="id" />
</s:iterator>