<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<select name="registryTypeFilter.selectedId" class="form-select">
	<option value="-1"><s:text name="eirc.registry_type"/></option><s:iterator value="registryTypeFilter.registryTypes">
	<option value="<s:property value="id"/>"<s:if test="%{id == registryTypeFilter.selectedId}"> selected</s:if>><s:text name="%{i18nName}"/></option></s:iterator>
</select>
