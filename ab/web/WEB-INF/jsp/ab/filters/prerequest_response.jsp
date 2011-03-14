<%@page contentType="application/json;charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
{
    "string": "<s:property value="filterString" escapeHtml="false" escapeJavaScript="true" />",
    "value": <s:property value="filterValue" />
}