<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="%{paymentPointsFilter.readOnly}">
	<s:hidden name="paymentPointsFilter.selectedId" />
	<s:property value="getPaymentPointDescription(paymentPointsFilter.selected)"/>
</s:if><s:else><select name="paymentPointsFilter.selectedId" <s:if test="paymentPointsFilter.needAutoChange">onchange="this.form.submit();"</s:if> class="form-select">
	<s:if test="paymentPointsFilter.allowEmpty"><option value="-1"><s:text name="eirc.payment_point"/></option></s:if><s:iterator value="paymentPointsFilter.points">
	<option value="<s:property value="id"/>"<s:if test="%{id == paymentsCollectorFilter.selectedId}"> selected</s:if>><s:property value="getPaymentPointDescription(id)"/></option></s:iterator>
</select></s:else>
