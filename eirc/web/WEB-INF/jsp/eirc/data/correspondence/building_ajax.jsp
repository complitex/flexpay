<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="buildingsList">
  {"id": "<s:property value="id" />", "value": "<s:property value="buildingAttributes[1].value" />"},
</s:iterator>
]}