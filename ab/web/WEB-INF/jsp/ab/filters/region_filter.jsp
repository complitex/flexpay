<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="regionFilter.selectedId" <s:if test="regionFilter.readOnly">disabled="1"</s:if> <s:if test="regionFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select"><s:iterator value="regionFilter.names">
	<option value="<s:property value="translatable.object.id"/>"<s:if test="%{translatable.object.id == regionFilter.selectedId}"> selected</s:if>><s:property value="name"/></option></s:iterator>
</select>