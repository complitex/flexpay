<%@taglib prefix="s" uri="/struts-tags"%>
<s:iterator value="foundObjects"><s:property value="name + '|' + value" />
</s:iterator>