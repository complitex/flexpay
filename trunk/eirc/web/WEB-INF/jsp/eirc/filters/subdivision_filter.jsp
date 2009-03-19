<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{subdivisionFilter.isReadOnly()}">
	<s:hidden name="subdivisionFilter.selectedId" />
	<s:property value="getTranslation(subdivisionFilter.selected.names).name"/>
</s:if><s:else><select name="subdivisionFilter.selectedId" class="form-select">
	<s:if test="subdivisionFilter.allowEmpty"><option value="-1"><s:text name="eirc.subdivision" /></option></s:if>
	<s:iterator value="subdivisionFilter.subdivisions">
	<option value="<s:property value="id"/>"<s:if test="%{id == subdivisionFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option></s:iterator>
</select></s:else>
