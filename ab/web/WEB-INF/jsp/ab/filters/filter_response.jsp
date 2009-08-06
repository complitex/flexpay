<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="foundObjects" status="status">
    <s:property value="name" />|<s:property value="value" />
</s:iterator>