<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>
<s:actionmessage />

<s:form action="registryDeliveryHistory">
  <s:hidden name="beginDate" />
  <s:hidden name="endDate" />

  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td class="col"><s:text name="payments.registry.delivery_history.date_from"/></td>
      <td class="col">
          <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
      </td>

      <td class="col"><s:text name="payments.registry.delivery_history.date_till"/></td>
      <td class="col">
          <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
      </td>
      <td>
        <input type="submit" name="filterSubmitted" class="btn-exit"
                                       value="<s:text name="payments.registry.delivery_history.filter"/>"/>
      </td>
    </tr>
    <tr>
      <td colspan="5" align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>

  <table cellpadding="3" cellspacing="1" border="0" width="100%">
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
        <td class="col">
          <a href="<s:url action="registryView"><s:param name="registry.id" value="%{registryId}"/></s:url>">
            <s:property value="registryId"/>
          </a>
        </td>
        <td class="col"><s:property value="dateFrom"/></td>
        <td class="col"><s:property value="dateTo"/></td>
        <td class="col"><s:property value="dateDelivery"/></td>
        <td class="col"><s:property value="typeRegistry"/></td>
        <td class="col"><s:property value="recipient"/></td>
        <td class="col"><s:property value="serviceProvider"/></td>
      </tr>
    </s:iterator>
  </table>
  
  <table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
      <td colspan="4">
				<input type="submit" name="submitted" class="btn-exit" value="<s:text name="payments.registry.delivery_history.repeat_send" />"/>
			</td>
      <td colspan="4" align="right">
        <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
      </td>
    </tr>
  </table>
</s:form>