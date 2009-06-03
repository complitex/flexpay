<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{paymentPointsFilter.readOnly}">
	<s:hidden name="paymentPointsFilter.selectedId" />
	<s:property value="getTranslation(paymentPointsFilter.selected.names).name"/>
</s:if><s:else>
    <select name="paymentPointsFilter.selectedId" <s:if test="paymentPointsFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="paymentPointsFilter.allowEmpty">
        <option value="-1"><s:text name="eirc.payment_point"/></option>
    </s:if>
    <s:iterator value="paymentPointsFilter.points">
	    <option value="<s:property value="id"/>"<s:if test="%{id == paymentPointsFilter.selectedId}"> selected</s:if>><s:property value="getTranslation(names).name"/></option>
    </s:iterator>
    </select>
</s:else>
