<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.country"/>&nbsp;
<select name="countryFilter.selectedId" onchange="this.form.submit()"><s:iterator value="countryFilter.names" >
	<option  value="<s:property value="translatable.id"/>"<s:if test="%{translatable.id == countryFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>