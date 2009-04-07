
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

	<tr>
		<td class="th" width="1%">&nbsp;</td>
		<td class="th" width="1%">
			<input type="checkbox" disabled="1" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
		</td>
		<td class="th"><s:text name="payments.eirc_account"/></td>
		<td class="th"><s:text name="payments.eirc_account.person"/></td>
		<td class="th"><s:text name="payments.eirc_account.apartment"/></td>
		<td class="th">&nbsp;</td>
	</tr>

	<tr valign="middle" class="cols_1">
		<td class="col" align="right">1</td>
		<td class="col"><input type="checkbox" disabled="1" value="0" name="objectIds"/></td>
		<td class="col"><a href="#"><s:text name="payments.demo.data.eirc_account"/></a></td>
		<td class="col"><a href="#"><s:text name="payments.demo.data.fio"/></a></td>
		<td class="col"><a href="#"><s:text name="payments.demo.data.address"/></a></td>
		<td class="col"><a href="#"><s:text name="payments.demo.data.pay"/></a></td>
	</tr>

</table>