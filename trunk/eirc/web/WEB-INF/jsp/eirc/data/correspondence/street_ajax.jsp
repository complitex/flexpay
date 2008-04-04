<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="streetVisList">
  {"id": "<s:property value="id" />", "value": "<s:property value="name" />", "info": "<s:property value="type" />"},
</s:iterator>
]}