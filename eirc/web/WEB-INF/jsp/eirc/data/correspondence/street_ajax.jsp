<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


{"results": [
<s:iterator value="streetList">
  {"id": "<s:property value="id" />", "value": "<s:property value="%{getNameTranslation(top).name}" />", "info": "<s:property value="%{getTypeTranslation(top).name}" />"},
</s:iterator>
]}