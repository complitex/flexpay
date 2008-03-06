<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="buildingList">
  {"id": "<s:property value="id" />", "value": "<s:property value="name" />"},
</s:iterator>
]}