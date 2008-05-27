<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="streetFilter.selectedId" <s:if test="streetFilter.disabled">disabled="1"</s:if> onchange="this.form.submit()" class="form-select"><s:iterator value="streetFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == streetFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>