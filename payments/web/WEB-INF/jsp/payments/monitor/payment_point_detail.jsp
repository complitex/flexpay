<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>

<s:form action="paymentPointDetailMonitor">
  <s:hidden name="status" />
  <s:hidden name="processId" />
  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td nowrap="nowrap"><s:text name="payments.payment_points.list.updated"/><s:property value="updated"/></td>
      <td nowrap="nowrap" align="left">
        <input type="submit" name="update" class="" value="<s:text name="payments.payment_points.list.update"/>"/>
      </td>
    </tr>
    <tr>
      <td nowrap="nowrap"><s:text name="payments.payment_point"/>:<s:property value="name"/></td>
    </tr>
    <tr>
      <td nowrap="nowrap">
        <s:text name="payments.payment_point.detail.payments_count"/>:<s:property value="paymentsCount"/>&nbsp;&nbsp;&nbsp;
        <s:text name="payments.payment_point.detail.sum"/>:<s:property value="totalSum"/>
      </td>
    </tr>
    <tr>
      <td nowrap="nowrap"><s:text name="payments.payment_point.detail.status"/>:<s:property value="status"/></td>
    </tr>
    <tr>
      <td>
        <s:iterator value="buttons" id="button">
          <input type="submit" name="activity" class="" value="<s:property value="button"/>"/>
        </s:iterator>
      </td>
    </tr>
  </table>
  <table cellpadding="3" cellspacing="1" border="0" width="100%" class="cash_boxes_list">
    <tr>
      <td class="th"><s:text name="payments.payment_point.detail.cash_boxes.list.cash_box"/></td>
      <td class="th"><s:text name="payments.payment_point.detail.cash_boxes.list.sum"/></td>
      <td class="th"><s:text name="payments.payment_point.detail.cash_boxes.list.FIO_cashier"/></td>
      <td class="th"><s:text name="payments.payment_point.detail.cash_boxes.list.last_payment"/></td>
      <td class="th"><s:text name="payments.payment_point.detail.cash_boxes.list.payments_count"/></td>
    </tr>

    <s:iterator value="cashboxes" id="paymentPointDetail">
      <tr>
        <td nowrap="nowrap"><s:property value="cashBox"/></td>
        <td nowrap="nowrap"><s:property value="sum"/></td>
        <td nowrap="nowrap"><s:property value="cashierFIO"/></td>
        <td nowrap="nowrap"><s:property value="lastPayment"/></td>
        <td nowrap="nowrap"><s:property value="paymentsCount"/></td>
      </tr>
    </s:iterator>
  </table>
</s:form>