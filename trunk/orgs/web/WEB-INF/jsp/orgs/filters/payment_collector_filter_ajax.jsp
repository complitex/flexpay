<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="paymentCollectorFilter.readOnly">
	<s:hidden name="paymentCollectorFilter.selectedId" />
	<s:property value="getTranslationName(paymentCollectorFilter.selected.organization.names)" />
</s:if><s:else>
    <select name="paymentCollectorFilter.selectedId"<s:if test="paymentCollectorFilter.needAutoChange"> onchange="pagerAjax();"</s:if> class="form-select">
	<s:if test="paymentCollectorFilter.allowEmpty">
        <option value="-1"><s:text name="eirc.payment_collector" /></option>
    </s:if><s:iterator value="paymentCollectorFilter.instances">
    	<option value="<s:property value="id" />"<s:if test="id == paymentCollectorFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(organization.names)" /></option></s:iterator>
    </select>
</s:else>
