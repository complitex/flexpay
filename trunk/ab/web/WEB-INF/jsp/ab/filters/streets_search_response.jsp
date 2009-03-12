<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:iterator value="streetVisList">
    <s:property value="type" /> <s:property value="name" />|<s:property value="id" />
</s:iterator>