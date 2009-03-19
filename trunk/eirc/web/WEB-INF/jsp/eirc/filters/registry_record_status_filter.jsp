<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<select name="recordStatusFilter.selectedStatus" class="form-select"><s:iterator value="recordStatusFilter.statusTypes">
	<option value="<s:property value="key"/>"<s:if test="%{key == recordStatusFilter.selectedStatus}"> selected</s:if>><s:text name="%{value}"/></option></s:iterator>
</select>
