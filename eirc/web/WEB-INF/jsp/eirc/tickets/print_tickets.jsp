<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table>

<s:form action="printTickets" method="POST">

<tr>
  <td>
    <s:text name="eirc.organisation" />
  </td>
  <td>
    <s:select name="serviceOrganisationId" list="serviceOrganizationList" listKey="id" listValue="organisation.name" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="year" />
  </td>
  <td>
    <s:select name="year" list="@org.flexpay.common.util.DateUtil@YEARS" value="year" required="true" />
  </td>
</tr>

<tr>
  <td>
    <s:text name="month" />
  </td>
  <td>
    <s:select name="month" list="@org.flexpay.common.util.DateUtil@MONTHS" value="month" required="true" />
  </td>
</tr>

<tr>
  <td colspan="2" align="center">
    &nbsp;
  </td>
</tr>

<tr>
  <td colspan="2" align="center">
    <s:submit name="submit" value="%{getText('common.upload')}" cssClass="btn-exit" />
  </td>
</tr>  

</s:form>

</table>