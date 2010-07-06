<%@page contentType="application/json;charset=UTF-8" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

{"results": [
<s:iterator value="buildingsList">
  {"id": "<s:property value="id" />", "value": "<s:if test="number != null"><s:property value="number" /></s:if> <s:if test="bulk != null"> <s:text name="ab.bulk_number.short" /><s:property value="bulk" /></s:if>"},
</s:iterator>
]}