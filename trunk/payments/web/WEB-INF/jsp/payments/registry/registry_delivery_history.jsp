<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<s:actionerror/>
<s:actionmessage />

<s:form action="registryDeliveryHistory">
  <s:hidden name="beginDate" />
  <s:hidden name="endDate" />

  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td nowrap="nowrap"><s:text name="payments.registry.delivery_history.date_from"/></td>
      <td nowrap="nowrap">
          <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
      </td>

      <td nowrap="nowrap"><s:text name="payments.registry.delivery_history.date_till"/></td>
      <td nowrap="nowrap">
          <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
      </td>
      <td nowrap="nowrap">
        <input type="submit" name="filterSubmitted" class="btn-exit"
                                       value="<s:text name="payments.registry.delivery_history.filter"/>"/>
      </td>
      <td nowrap="nowrap" align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>

  <table cellpadding="3" cellspacing="1" border="0" width="100%" class="payment_points_list">
    <tr>
      <td class="th"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
      <td class="th"><s:text name="payments.registry.delivery_history.registry_id"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.date_from"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.date_to"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.delivery_date"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.type_registry"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.recipient"/></td>
      <td class="th"><s:text name="payments.registry.delivery_history.service_provider"/></td>
    </tr>

    <s:iterator value="deliveryHistory">
      <tr>
        <td class="col" width="1%">
          <input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/>
        </td>
        <td nowrap="nowrap"><s:property value="dateFrom"/></td>
        <td nowrap="nowrap"><s:property value="dateTo"/></td>
        <td nowrap="nowrap"><s:property value="dateDelivery"/></td>
        <td nowrap="nowrap"><s:property value="typeRegistry"/></td>
        <td nowrap="nowrap"><s:property value="recipient"/></td>
        <td nowrap="nowrap"><s:property value="serviceProvider"/></td>
      </tr>
    </s:iterator>
  </table>
  
  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td colspan="5">
				<input type="submit" name="submitted" class="" value="<s:text name="payments.registry.delivery_history.repeat_send" />"/>
			</td>
      <td align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>
</s:form>