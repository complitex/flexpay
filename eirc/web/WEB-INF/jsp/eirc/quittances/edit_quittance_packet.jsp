
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="quittancePacketEdit">
	<s:hidden name="packet.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.packet_number"/>:</td>
			<td class="col"><s:textfield name="packet.packetNumber" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.creation_date"/>:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/create_date_filter.jsp" %></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.begin_date"/>:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.close_date"/>:</td>
			<td class="col"><%@include file="/WEB-INF/jsp/common/filter/close_date_filter.jsp" %></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.payment_point"/>:</td>
			<td class="col"><%@include file="../filters/payment_points_filter.jsp" %></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.control_quittances_number"/>:</td>
			<td class="col"><s:textfield name="packet.controlQuittanciesNumber" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.control_overall_summ"/>:</td>
			<td class="col"><s:textfield name="packet.controlOverallSumm" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.quittances_number"/>:</td>
			<td class="col">
				<s:hidden name="packet.quittanciesNumber" />
				<s:property value="packet.quittanciesNumber" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.overall_summ"/>:</td>
			<td class="col">
				<s:hidden name="packet.overallSumm" />
				<s:property value="packet.overallSumm" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.opener_user"/>:</td>
			<td class="col">
				<s:hidden name="packet.creatorUserName" />
				<s:property value="packet.creatorUserName" /></td>
		</tr>
		<tr valign="middle" class="col">
			<td class="col"><s:text name="eirc.quittance.packet.closer_user"/>:</td>
			<td class="col">
				<s:hidden name="packet.closerUserName" />
				<s:property value="packet.closerUserName" /></td>
		</tr>
		<tr valign="middle">
			<td colspan="2"><input type="submit" class="btn-exit" name="submitted"
								   value="<s:text name="common.save"/>"/></td>
		</tr>
	</table>
</s:form>
