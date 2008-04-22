<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

<tr valign="middle" class="cols_1">
  <td>
    Number
  </td>
  <td>
    <s:property value="ticketInfo.ticketNumber" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    creationDate
  </td>
  <td>
    <s:property value="@org.flexpay.common.util.DateUtil@format(ticketInfo.creationDate,'dd.MM.yyyy')" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    dateFrom
  </td>
  <td>
    <s:property value="@org.flexpay.common.util.DateUtil@format(ticketInfo.dateFrom,'dd.MM.yyyy')" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    dateTill
  </td>
  <td>
    <s:property value="@org.flexpay.common.util.DateUtil@format(ticketInfo.dateTill,'dd.MM.yyyy')" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    payer
  </td>
  <td>
    <s:property value="ticketInfo.payer" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    address
  </td>
  <td>
    <s:property value="ticketInfo.address" />
  </td>
</tr>

</table>

<table>

<tr>
  <td class="th">
    service
  </td>
  <td class="th">
    dateFrom
  </td>
  <td class="th">
    dateTill
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    kvartplata
  </td>
  <td>
    <s:property value="ticketInfo.kvartplataDateFromSum" />
  </td>
  <td>
    <s:property value="ticketInfo.kvartplataDateTillSum" />
  </td>
</tr>

<s:if test="ticketInfo.serviceAmountInfoMap.get(2) != null">
<tr valign="middle" class="cols_1">
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(2).name" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(2).dateFromAmount" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(2).dateTillAmount" />
  </td>
</tr>
</s:if>

<s:if test="ticketInfo.serviceAmountInfoMap.get(3) != null">
<tr valign="middle" class="cols_1">
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(3).name" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(3).dateFromAmount" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(3).dateTillAmount" />
  </td>
</tr>
</s:if>

<s:if test="ticketInfo.serviceAmountInfoMap.get(4) != null">
<tr valign="middle" class="cols_1">
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(4).name" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(4).dateFromAmount" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(4).dateTillAmount" />
  </td>
</tr>
</s:if>

<s:if test="ticketInfo.serviceAmountInfoMap.get(5) != null">
<tr valign="middle" class="cols_1">
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(5).name" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(5).dateFromAmount" />
  </td>
  <td>
    <s:property value="ticketInfo.serviceAmountInfoMap.get(5).dateTillAmount" />
  </td>
</tr>
</s:if>

<tr valign="middle" class="cols_1">
  <td>
    waterin
  </td>
  <td>
    <s:property value="ticketInfo.waterinDateFromSum" />
  </td>
  <td>
    <s:property value="ticketInfo.waterinDateTillSum" />
  </td>
</tr>

<tr valign="middle" class="cols_1">
  <td>
    sum
  </td>
  <td>
    <s:property value="ticketInfo.dateFromSum" />
  </td>
  <td>
    <s:property value="ticketInfo.dateTillSum" />
  </td>
</tr>

</table>



<s:form action="ticketPay" method="POST">
  <s:hidden name="ticketId" value="%{ticketInfo.ticketNumber}" />
    <s:submit name="submit" value="%{getText('common.view')}" cssClass="btn-exit" />
</s:form>


</table>