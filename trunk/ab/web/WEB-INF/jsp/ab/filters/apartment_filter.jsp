<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:if test="apartmentFilter.readOnly">
	<s:hidden name="apartmentFilter.selectedId" value="%{apartmentFilter.selectedId}" />
	<s:property value="%{apartmentFilter.selected.number}" />
</s:if><s:else>
    <select name="apartmentFilter.selectedId" <s:if test="apartmentFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select">
        <s:if test="apartmentFilter.allowEmpty">
            <option value="-1">
                <s:text name="common.select" />
            </option>
        </s:if>
        <s:iterator value="apartmentFilter.apartments">
            <option value="<s:property value="id"/>"<s:if test="%{id == apartmentFilter.selectedId}"> selected</s:if>>
                <s:property value="%{number}"/>
            </option>
        </s:iterator>
    </select>
</s:else>