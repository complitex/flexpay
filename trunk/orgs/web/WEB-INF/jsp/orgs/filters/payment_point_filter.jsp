<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="paymentPointFilter.readOnly">
	<s:hidden id="" name="paymentPointFilter.selectedId" />
	<s:property value="getTranslationName(paymentPointFilter.selected.names)" />
</s:if><s:else>
    <select name="paymentPointFilter.selectedId" class="form-select"<s:if test="paymentPointFilter.needAutoChange"> onchange="this.form.submit();"</s:if><s:if test="paymentPointFilter.disabled"> disabled="disabled"</s:if>>
	<s:if test="paymentPointFilter.allowEmpty"><option value="-1"><s:text name="eirc.payment_point" /></option></s:if>
    <s:iterator value="paymentPointFilter.points">
	    <option value="<s:property value="id" />"<s:if test="id == paymentPointFilter.selectedId"> selected</s:if>><s:property value="getTranslationName(names)" /></option></s:iterator>
    </select>
</s:else>
