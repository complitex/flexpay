<%@ taglib prefix="s" uri="/struts-tags" %><s:if test="%{preRequest}"><s:property value="filterString" /></s:if><s:else>
<s:iterator value="foundObjects" status="status">
    <s:property value="name" />|<s:property value="value" />
</s:iterator></s:else>