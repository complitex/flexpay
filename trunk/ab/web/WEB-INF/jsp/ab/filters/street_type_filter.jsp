<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.town_type"/>&nbsp;
<select name="streetTypeFilter.selectedId" class="form-select"><s:iterator value="streetTypeFilter.names">
	<option value="<s:property value="translatable.id"/>"<s:if test="%{translatable.id == streetTypeFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>