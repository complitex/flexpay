<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="streetList">
  {"id": "<s:property value="id" />", "value": "<s:property value="%{getTranslation(top).name}" />", "info": ""},
</s:iterator>
]}