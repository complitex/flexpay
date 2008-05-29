<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="townFilter.selectedId" <s:if test="townFilter.readOnly">disabled="1"</s:if> onchange="this.form.submit()" class="form-select"><s:iterator value="townFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == townFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>