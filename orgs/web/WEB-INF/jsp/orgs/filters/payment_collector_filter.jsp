<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{paymentCollectorFilter.readOnly}">
	<s:hidden name="paymentCollectorFilter.selectedId" />
	<s:property value="getTranslation(paymentCollectorFilter.selected.organization.names).name"/>
</s:if><s:else>
    <select name="paymentCollectorFilter.selectedId" <s:if test="paymentCollectorFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="paymentCollectorFilter.allowEmpty">
        <option value="-1"><s:text name="eirc.payment_collector"/></option>
    </s:if>
    <s:iterator value="paymentCollectorFilter.instances">
    	<option value="<s:property value="id"/>"<s:if test="%{id == paymentCollectorFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(organization.names).name"/></option>
    </s:iterator>
    </select>
</s:else>
