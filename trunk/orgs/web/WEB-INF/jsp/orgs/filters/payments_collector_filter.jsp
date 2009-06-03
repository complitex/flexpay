<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{paymentsCollectorFilter.readOnly}">
	<s:hidden name="paymentsCollectorFilter.selectedId" />
	<s:property value="getTranslation(paymentsCollectorFilter.selected.organization.names).name"/>
</s:if><s:else>
    <select name="paymentsCollectorFilter.selectedId" <s:if test="paymentsCollectorFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="paymentsCollectorFilter.allowEmpty">
        <option value="-1"><s:text name="eirc.payments_collector"/></option>
    </s:if>
    <s:iterator value="paymentsCollectorFilter.instances">
    	<option value="<s:property value="id"/>"<s:if test="%{id == paymentsCollectorFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(organization.names).name"/></option>
    </s:iterator>
    </select>
</s:else>
