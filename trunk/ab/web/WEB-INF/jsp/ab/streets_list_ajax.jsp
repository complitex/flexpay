<%@page contentType="application/json;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
{"results": [<s:property value="#comma"/><s:set name="comma" value=","/><s:iterator value="streetVisList">{"id": "<s:property value="id" />", "value": "<s:property value="name" />", "info": "<s:property value="type" />"}</s:iterator>]}
