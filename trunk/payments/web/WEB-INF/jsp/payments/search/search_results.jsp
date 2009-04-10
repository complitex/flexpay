<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

function replaceCommaWithDot(value) {
	return value.replace(",", ".");
}

// summ update
function updateTotalPay() {
	var total = 0.00;
	var elements = $("input[id^=demoQuittancePayForm_servicePayments_]");

	for (var i = 0; i < elements.length; i++) {
		var value = $(elements[i]).val();

		if (isValidPayValue(value)) {
			value = replaceCommaWithDot(value);
			total += parseFloat(value);
		} else {
			$('#demoQuittancePayForm_totalPayed').val('<s:text name="payments.error.quittances.quittance_pay.unaccessible"/>');
		}
	}

	var result = total.toFixed(2);
	$('#demoQuittancePayForm_totalPayed').val(result);
}

// validation
function isValidPayValue(value) {
	var pattern = /^(\d)+([\.,]?\d{0,2})$/;
	return value.match(pattern);
}

$(function() {
	updateTotalPay();
});

$(function() {
	$.validator.addMethod("validPayValue", function(value, element) {
		value = replaceCommaWithDot(value);
		return isValidPayValue(value);
	}, '<s:text name="payments.error.quittances.quittance_pay.invalid_pay_value"/>');


	$.validator.addMethod("payValue_1_is_not_too_big", function(value, element) {
		value = replaceCommaWithDot(value);
		return dotted2Int(value) <= dotted2Int('<s:text name="payments.demo.data.service1.payable"/>');
	}, '<s:text name="payments.error.quittances.quittance_pay.pay_value_too_big"/>');

	$.validator.addMethod("payValue_2_is_not_too_big", function(value, element) {
		value = replaceCommaWithDot(value);
		return dotted2Int(value) <= dotted2Int('<s:text name="payments.demo.data.service2.payable"/>');
	}, '<s:text name="payments.error.quittances.quittance_pay.pay_value_too_big"/>');
});

$(function() {
	var validator = $("#demoQuittancePayForm").validate({
		rules: {
			'servicePayments[1]' : {
				'validPayValue': true,
				'payValue_1_is_not_too_big': true
			},
			'servicePayments[2]' : {
				'validPayValue': true,
				'payValue_2_is_not_too_big': true
			},
			'totalPayed': 'required' // covers jQuery validation 1.5.1 bug (it doesn't allow form fields without any rules)
		},
		messages: {
			'servicePayments[1]' : {
				'validPayValue': '<s:text name="payments.error.quittances.quittance_pay.invalid_pay_value"/>',
				'payValue_1_is_not_too_big': '<s:text name="payments.error.quittances.quittance_pay.pay_value_too_big"/>'
			},
			'servicePayments[2]' : {
				'validPayValue': '<s:text name="payments.error.quittances.quittance_pay.invalid_pay_value"/>',
				'payValue_2_is_not_too_big': '<s:text name="payments.error.quittances.quittance_pay.pay_value_too_big"/>'
			}
		},
		errorClass: "cols_1_error",
		errorElement: "span",
		success: function(label) {
			label.parent("td").parent("tr.cols_1_error").css("display", "none");
			label.remove();

			if (validator.numberOfInvalids() == 0) {
				updateTotalPay();
			}
		},
		showErrors: function(errorMap, errorList) {
			if (validator.numberOfInvalids() > 0) {
			<%--$('#demoQuittancePayForm_total_pay').val('<s:text name="payments.error.quittances.quittance_pay.unaccessible"/>');--%>
			}

			this.defaultShowErrors();
		},
		errorPlacement: function(error, element) {
			var row = element.parent("td").parent("tr").prev("tr");
			var cell = row.children()[0];

			error.appendTo(cell);
			row.css("display", "table-row");
		},
		invalidHandler: function(form, validator) {
			alert('<s:text name="payments.error.quittances.quittance_pay.invalid_submit"/>');
		}
	});
});

/**
 * Quittance details object
 */
function QD(id, serviceId, title, provider, toPay, payed) {
	this.id = id;
	this.serviceId = serviceId;
	this.title = title;
	this.provider = provider;
	this.toPay = dotted2Int(toPay);
	this.payed = dotted2Int(payed);

	this.toString = function() {
		return "id : " + this.id + ", " +
			   "serviceId : " + this.serviceId + ", " +
			   "title : " + this.title + ", " +
			   "provider : " + this.provider + ", " +
			   "toPay : " + int2Dotted(this.toPay) + " (" + this.toPay + "), " +
			   "payed : " + int2Dotted(this.payed);
	};
}

/**
 * Convert from big decimal format
 * @param i
 */
function dotted2Int(i) {
	var dotpos = i.indexOf(".");
	//noinspection PointlessArithmeticExpressionJS
	return dotpos != -1 ? i.substring(0, dotpos) * 100 + i.substring(dotpos + 1) * 1 : i * 100;
}

/**
 * Convert integer to big decimal format
 *
 * @param i Integer to convert
 */
function int2Dotted(i) {
	var divider = 100;
	var mod = i % divider;
	return ((i - mod) / divider).toString() + "." + (mod < 10 ? "0" + mod : mod);
}

var DETAILS = $.protify([]);

function divideAscending() {
	var totalSumm = dotted2Int($("#demoQuittancePayForm_totalPayed").val());
	var sortBySumm = DETAILS.sort(function (qd1, qd2) {
		return qd2.toPay - qd1.toPay;
	});

	var summs = {};

	// set summs to zero
	for (var i = 0; i < sortBySumm.length; ++i) {
		var qd = sortBySumm[i];
		summs[qd.serviceId] = 0;
	}

	// divide summs
	while (totalSumm > 0) {
		for (i = 0; i < sortBySumm.length && totalSumm > 0; ++i) {
			qd = sortBySumm[i];
			var nextSumm = totalSumm >= qd.toPay ? qd.toPay : totalSumm;
			summs[qd.serviceId] += nextSumm;
			totalSumm -= nextSumm;
		}
	}

	// set summs to their values
	for (var serviceId in summs) {
		$("#demoQuittancePayForm_servicePayments_" + serviceId + "_").val(int2Dotted(summs[serviceId]));
	}
}
function divideByRatio() {
	var totalSumm = dotted2Int($("#demoQuittancePayForm_totalPayed").val());
	var nonZeroSumms = DETAILS.findAll(function (qd) {
		return qd.toPay > 0;
	});

	var summs = {};
	// set summs to zero
	DETAILS.each(function (qd) {
		summs[qd.serviceId] = 0;
	});

	var last = nonZeroSumms.last();
	var summ = 0;
	var totalToPay = dotted2Int('<s:text name="payments.demo.data.total_payable"/>');
	nonZeroSumms.each(function (qd) {
		if (qd.id != last.id) {
			// http://msmvps.com/blogs/rexiology/archive/2006/01/09/80628.aspx
			// cast float to integer trick
			var nextSumm = (totalSumm * qd.toPay) / totalToPay | 0;
			summ += nextSumm;
			summs[qd.serviceId] = nextSumm;
		}
	});

	// set last element summ
	summs[last.serviceId] = totalSumm - summ;

	// set summs to their values
	for (var serviceId in summs) {
		$("#demoQuittancePayForm_servicePayments_" + serviceId + "_").val(int2Dotted(summs[serviceId]));
	}
}
</script>

<s:form id="demoQuittancePayForm">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service"/></td>
			<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.service_supplier"/></td>
			<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.payable"/></td>
			<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.payed"/></td>
			<td class="th" nowrap="nowrap"><s:text name="payments.quittances.quittance_pay.pay"/></td>
		</tr>

		<tr class="cols_1_error" style="display:none;">
			<td colspan="6"/>
		</tr>
		<tr class="cols_1">
			<!-- Add quittance details to array -->
			<script type="text/javascript">				
				DETAILS.push(new QD("1", "1",
						"<s:text name="payments.demo.data.service1.name"/>",
						"<s:text name="payments.demo.data.service1.provider"/>",
						"<s:text name="payments.demo.data.service1.outgoing"/>",
						"<s:text name="payments.demo.data.service1.payed_summ"/>"));
			</script>

			<%-- render details --%>
			<td class="col" align="right">1</td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.service1.name"/></td>
			<td class="col"><s:text name="payments.demo.data.service1.provider"/></td>
			<td class="col" id="paySumm_1"><s:text name="payments.demo.data.service1.outgoing"/></td>
			<td class="col"><s:text name="payments.demo.data.service1.payed_summ"/></td>
			<td class="col">
				<input name="servicePayments[1]" id="demoQuittancePayForm_servicePayments_1_"
					   type="text" value="<s:text name="payments.demo.data.service1.payable"/>" style="width: 100%; text-align: right;"/>
			</td>
		</tr>

		<tr class="cols_1_error" style="display:none;">
			<td colspan="6"/>
		</tr>
		<tr class="cols_1">
			<script type="text/javascript">
				DETAILS.push(new QD("2", "2",
						"<s:text name="payments.demo.data.service2.name"/>",
						"<s:text name="payments.demo.data.service2.provider"/>",
						"<s:text name="payments.demo.data.service2.outgoing"/>",
						"<s:text name="payments.demo.data.service2.payed_summ"/>"));
			</script>

			<%-- render details --%>
			<td class="col" align="right">2</td>
			<td class="col" nowrap="nowrap"><s:text name="payments.demo.data.service2.name"/></td>
			<td class="col"><s:text name="payments.demo.data.service2.provider"/></td>
			<td class="col" id="paySumm_2"><s:text name="payments.demo.data.service2.outgoing"/></td>
			<td class="col"><s:text name="payments.demo.data.service2.payed_summ"/></td>
			<td class="col">
				<input name="servicePayments[2]" id="demoQuittancePayForm_servicePayments_2_"
						type="text" value="<s:text name="payments.demo.data.service2.payable"/>" style="width: 100%; text-align: right;"/>
			</td>
		</tr>


		<tr class="cols_1">
			<td class="col" colspan="3" style="text-align:right;font-weight:bold;"><s:text name="payments.quittances.quittance_pay.total_payable"/></td>
			<td class="col" style="font-weight:bold;"><s:text name="payments.demo.data.total_payable"/></td>
			<td class="col" style="font-weight:bold;"><s:text name="payments.demo.data.total_payed_before"/></td>
			<td class="col"><input type="text" id="demoQuittancePayForm_totalPayed" name="totalPayed" value="<s:text name="payments.demo.data.total_pay"/>" style="width: 100%; text-align: right;" readonly="readonly"/></td>
		</tr>

		<tr>
			<td colspan="2" style="text-align:left;">
				<input type="submit" name="submitted" value="<s:text name="payments.quittances.quittance_pay.pay"/>" class="btn-exit" style="width: 100%;"/>				
			</td>
			<td colspan="4" style="text-align:right;">
				<input type="button" value="<s:text name="payments.quittance.payment.pay_asc"/>" class="btn-exit" onclick="divideAscending();"/>
				<input type="button" value="<s:text name="payments.quittance.payment.pay_by_ratio"/>" class="btn-exit" onclick="divideByRatio();"/>
			</td>
		</tr>

	</table>
</s:form>