<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<s:actionerror />
<s:form action="paymentPointEdit">
	<s:hidden name="point.id" />
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="middle" class="cols_1">
			<td class="col"><s:text name="eirc.payments_collector"/>:</td>
			<td class="col"><%@include file="../filters/payments_collector_filter.jsp"%></td>
		</tr>
		<tr valign="top" class="cols_1">
			<td class="col"><s:text name="ab.address"/>:</td>
			<td class="col">
				<s:textfield name="point.address" />
			</td>
		</tr>
		<tr valign="middle">
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>"/></td>
		</tr>
	</table>
</s:form>
