<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:text name="ab.district"/>&nbsp;
<select name="districtFilter.selectedId" onchange="this.form.submit()" class="form-select"><s:iterator value="districtFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == districtFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>