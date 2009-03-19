<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="importErrorTypeFilter.selectedType" class="form-select"><s:iterator value="importErrorTypeFilter.errorTypes">
	<option value="<s:property value="key"/>"<s:if test="%{key == importErrorTypeFilter.selectedType}"> selected</s:if>><s:text name="%{value}"/></option></s:iterator>
</select>
