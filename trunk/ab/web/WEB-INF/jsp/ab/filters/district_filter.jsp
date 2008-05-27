<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="districtFilter.selectedId" <s:if test="districtFilter.disabled">disabled="1"</s:if> onchange="this.form.submit()" class="form-select"><s:iterator value="districtFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == districtFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>