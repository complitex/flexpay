<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="regionFilter.selectedId" onchange="this.form.submit()" class="form-select"><s:iterator value="regionFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == regionFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>