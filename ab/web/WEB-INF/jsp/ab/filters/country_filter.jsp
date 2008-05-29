<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="countryFilter.selectedId" <s:if test="countryFilter.readOnly">disabled="1"</s:if>  onchange="this.form.submit()" class="form-select"><s:iterator value="countryFilter.names" >
	<option  value="<s:property value="translatable.id"/>"<s:if test="%{translatable.id == countryFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>