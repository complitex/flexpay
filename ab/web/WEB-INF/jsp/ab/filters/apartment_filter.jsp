<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="apartmentFilter.selectedId" <s:if test="apartmentFilter.readOnly">disabled="1"</s:if> onchange="this.form.submit()" class="form-select"><s:iterator value="apartmentFilter.apartments">
	<option value="<s:property value="id"/>"<s:if test="%{id == apartmentFilter.selectedId}"> selected</s:if>><s:property value="%{number}"/></option></s:iterator>
</select>