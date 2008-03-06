<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="streetList">
  {"id": "<s:property value="id" />", "value": "<s:property value="name" />", "info": ""},
</s:iterator>
]}