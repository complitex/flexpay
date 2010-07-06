<%@page contentType="application/json;charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
{
    "errorsNumber": "<s:property value="registry.errorsNumber" />",
    "errorMessage": "<s:actionerror />"
}