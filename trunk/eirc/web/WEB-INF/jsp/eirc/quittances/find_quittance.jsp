<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

<s:form method="POST">

<tr>
  <td>
    <s:text name="eirc.ticket_number" />
  </td>
  <td>
    <s:textfield key="ticketId" />
  </td>
</tr>

<tr>
  <td colspan="2" align="center">
    &nbsp;
  </td>
</tr>

<tr>
  <td colspan="2" align="center">
    <s:submit name="submitted" value="%{getText('common.view')}" cssClass="btn-exit" />
  </td>
</tr>  

</s:form>


</table>