<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>

<s:form action="paymentPointsListMonitor">
  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td nowrap="nowrap">
        <s:text name="payments.payment_points.list.filter"/>
        <input type="text" name="filter"/>
        <input type="submit" name="submitFilter" value="<s:text name="payments.payment_points.list.filter.submit"/>"/>
      </td>
    </tr>
    <tr>
      <td nowrap="nowrap"><s:text name="payments.payment_points.list.updated"/><s:property value="updated"/></td>
      <td nowrap="nowrap" align="left">
        <input type="submit" name="update" class="" value="<s:text name="payments.payment_points.list.update"/>"/>
      </td>
      <td nowrap="nowrap" align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>
  <table cellpadding="3" cellspacing="1" border="0" width="100%" class="payment_points_list">
    <tr>
      <td class="th"><s:text name="payments.payment_points.list.payment_point"/></td>
      <td class="th"><s:text name="payments.payment_points.list.payments_count"/></td>
      <td class="th"><s:text name="payments.payment_points.list.payment_point_status"/></td>
      <td class="th"><s:text name="payments.payment_points.list.payment_point_sum"/></td>
      <td class="th"><s:text name="payments.payment_points.list.cash_box"/></td>
      <td class="th"><s:text name="payments.payment_points.list.FIO_cashier"/></td>
      <td class="th"><s:text name="payments.payment_points.list.last_payment"/></td>
    </tr>

    <s:iterator value="paymentPoints" id="paymentPointDetail">
      <td nowrap="nowrap">
        <input type="submit" name="selectedPaymentPointName" value="<s:property value="name"/>" class="btn-link"/>
        <a href="<s:url action='paymentPointDetailMonitor.action'><s:param name="processId" value="%{id}"/></s:url>">
	            	<s:property value="name" />
	            </a>
      </td>
      <td nowrap="nowrap"><s:property value="paymentsCount"/></td>
      <td nowrap="nowrap"><s:property value="status"/></td>
      <td nowrap="nowrap"><s:property value="totalSum"/></td>
      <td nowrap="nowrap"><s:property value="cashBox"/></td>
      <td nowrap="nowrap"><s:property value="cashierFIO"/></td>
      <td nowrap="nowrap"><s:property value="lastPayment"/></td>
    </s:iterator>
  </table>
  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td>
        <input type="submit" name="detail" class="" value="<s:text name="payments.payment_points.list.detail"/>"/>
      </td>
      <td align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>
</s:form>