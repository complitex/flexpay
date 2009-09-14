<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<%@include file="/WEB-INF/jsp/payments/include/stylesheet.jsp" %>

<script type="text/javascript">
	$(function() {
		updateControls();
	});

	function updateControls() {
		if ($('#details').val() == 1) {
			disableCashboxSelect();
		} else {
			enableCashboxSelect();
		}
	}

	function disableCashboxSelect() {
		$('#cashboxFilter\\.selectedId').attr('disabled', 'disabled');
	}

	function enableCashboxSelect() {
		$('#cashboxFilter\\.selectedId').removeAttr('disabled');
	}

	function printReport() {

		var paymentCollectorId = "<s:property value="userPreferences.paymentCollectorId"/>";
		if (paymentCollectorId == '') {
			alert('<s:text name="payments.error.payment_collector_not_found"/>');
			return;
		}

		var url = '<s:url action="accReturnedPaymentsReport" includeParams="none" />';
		url += '?beginDateFilter.stringDate=' + $('#beginDateFilter').val() +
			   '&beginTimeFilter.stringDate=' + $('#beginTimeFilter').val() +
			   '&endDateFilter.stringDate=' + $('#endDateFilter').val() +
			   '&endTimeFilter.stringDate=' + $('#endTimeFilter').val() +
			   '&details=' + $('#details').val() +
			   '&submitted=submitted';

		var paymentPointId = $('#paymentPointsFilter\\.selectedId').val();
		if (paymentPointId > 0) {
			url += '&paymentPointsFilter.selectedId=' + paymentPointId;
		}

		var cashboxId = $('#cashboxFilter\\.selectedId').val();
		if (cashboxId > 0) {
			url += '&cashboxFilter.selectedId=' + cashboxId;
		}

		window.open(url, "_blank");
	}
</script>

<s:actionerror/>
<s:actionmessage/>

<form action="<s:url action="accReturnedPaymentsReport"/>">
	<table>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_from"/>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/begin_time_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td nowrap="nowrap">
				<s:text name="payments.report.generate.date_till"/>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
			</td>
			<td nowrap="nowrap">
				<%@ include file="/WEB-INF/jsp/common/filter/end_time_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<s:text name="payments.report.acc.returned.details_for"/>
				<select id="details" name="details" class="form-select" onchange="updateControls();">
					<option value="1"<s:if test="%{details == 1}"> selected</s:if>>
						<s:text name="payments.report.acc.returned.details_payment_point"/>
					</option>
					<option value="2"<s:if test="%{details == 2}"> selected</s:if>>
						<s:text name="payments.report.acc.returned.details_cashbox"/>
					</option>
					<option value="3"<s:if test="%{details == 3}"> selected</s:if>>
						<s:text name="payments.report.acc.returned.details_payments"/>
					</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<s:text name="payments.report.acc.returned.payment_point"/>
				<%@include file="/WEB-INF/jsp/orgs/filters/payment_points_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<s:text name="payments.report.acc.returned.cashbox"/>
				<%@include file="/WEB-INF/jsp/orgs/filters/cashbox_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="button" name="submitted" class="btn-exit" onclick="printReport();"
					   value="<s:text name="payments.reports.generate.generate"/>"/>
			</td>
		</tr>
	</table>
</form>