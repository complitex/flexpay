<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="buildingAttributeGroupFilter.selectedId" class="form-select"><s:iterator value="buildingAttributeGroupFilter.groups">
	<option value="<s:property value="id"/>"<s:if test="%{id == buildingAttributeGroupFilter.selectedId}"> selected</s:if>><s:property value="%{getTranslation(translations).name}"/></option></s:iterator>
</select>