<%@page contentType="application/json;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
{"results": [<s:iterator value="streetVisList"><s:property value="#comma"/><s:set name="comma" value="%{','}"/>{"id": "<s:property value="id" />", "value": "<s:property value="type" /> <s:property value="name" />", "info": ""}</s:iterator>]}
