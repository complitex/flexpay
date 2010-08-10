<%@page contentType="application/json;charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
{
    "updated": "<s:property value="updated" />",
    "status": "<s:property value="status" />",
    "actionText": "<s:text name="%{action}" />",
    "action": "<s:property value="action" />"
}