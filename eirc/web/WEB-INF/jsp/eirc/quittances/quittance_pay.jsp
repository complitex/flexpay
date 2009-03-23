<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	function replaceCommaWithDot(value) {
		return value.replace(",", ".");
	}

	// summ update
	function updateTotalPay() {
		var total = 0.00;
		var elements = $("input[id^=demoQuittancePayForm_servicePayValue_]");

		for (var i = 0; i < elements.length; i++) {
			var value = $(elements[i]).val();

			if (isValidPayValue(value)) {
				value = replaceCommaWithDot(value);
				total += parseFloat(value);
			} else {
				<%--$('#demoQuittancePayForm_total_pay').val('<s:text name="eirc.error.quittances.quittance_pay.unaccessible"/>');--%>
			}
		}

		//		var result = total.toFixed(2) + "";
		//		$('#demoQuittancePayForm_total_pay').val(result.replace(".", ","));
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
		}, '<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value"/>');


	<s:iterator value="%{quittance.quittanceDetails}" id="qd">
		$.validator.addMethod("payValue_<s:property value="%{#qd.id}"/>_is_not_too_big", function(value, element) {
			value = replaceCommaWithDot(value);
			return parseFloat(value) <= <s:property value="%{getPayable(#qd)}"/>;
		}, '<s:text name="eirc.error.quittances.quittance_pay.pay_value_too_big"/>');
	</s:iterator>
	});

	$(function() {
		var validator = $("#demoQuittancePayForm").validate({
			rules: {
				<s:iterator value="%{quittance.quittanceDetails}" id="qd">
				'servicePayValue[<s:property value="%{#qd.id}"/>]' : {
					'validPayValue': true,
					'payValue_<s:property value="%{#qd.id}"/>_is_not_too_big': true
				},
				</s:iterator>
				'total_pay': 'required' // covers $ validation 1.5.1 bug (it doesn't allow form fields without any rules)
			},
			messages: {
				<s:iterator value="%{quittance.quittanceDetails}" id="qd" status="status">
				'servicePayValue[<s:property value="%{#qd.id}"/>]' : {
					'validPayValue': '<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value"/>',
					'payValue_<s:property value="%{#qd.id}"/>_is_not_too_big': '<s:text name="eirc.error.quittances.quittance_pay.pay_value_too_big"/>'
				}<s:if test="!#status.last">, </s:if>
				</s:iterator>
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
					<%--$('#demoQuittancePayForm_total_pay').val('<s:text name="eirc.error.quittances.quittance_pay.unaccessible"/>');--%>
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
				alert('<s:text name="eirc.error.quittances.quittance_pay.invalid_submit"/>');
			}
		});
	});

	/**
	 * Quittance details object
	 */
	function QD(id, title, provider, toPay, payed) {
		this.id = id;
		this.title = title;
		this.provider = provider;
		this.toPay = dotted2Int(toPay);
		this.payed = dotted2Int(payed);

		this.toString = function() {
			return "id : " + this.id + ", " +
				   "title : " + this.title + ", " +
				   "provider : " + this.provider + ", " +
				   "toPay : " + int2Dotted(this.toPay) + " (" + this.toPay+ "), " +
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
		return dotpos != -1 ? i.substring(0, dotpos)*100 + i.substring(dotpos + 1)*1 : i*100; 
	}

	/**
	 * Convert integer to big decimal format
	 * 
	 * @param i Integer to convert
	 */
	function int2Dotted(i) {
		var divider = 100;
		var mod = i % divider;
		return ((i-mod)/divider).toString() + "." + (mod < 10 ? "0" + mod : mod);
	}

	var DETAILS = $.protify([]);

	function divideAscending() {
		var totalSumm = dotted2Int($("#demoQuittancePayForm_total_pay").val());
		var sortBySumm = DETAILS.sort(function (qd1, qd2) {
			return qd2.toPay - qd1.toPay;
		});

		var summs = {};

		// set summs to zero
		for (var i = 0; i < sortBySumm.length; ++i) {
			var qd = sortBySumm[i];
			summs[qd.id] = 0;
		}

		// divide summs
		while (totalSumm > 0) {
			for (i = 0; i < sortBySumm.length && totalSumm > 0; ++i) {
				qd = sortBySumm[i];
				var nextSumm = totalSumm >= qd.toPay ? qd.toPay : totalSumm;
				summs[qd.id] += nextSumm;
				totalSumm -= nextSumm;
			}
		}

		// set summs to their values
		for (var id in summs) {
			$("#demoQuittancePayForm_servicePayValue_" + id + "_").val(int2Dotted(summs[id]));
		}
	}
	function divideByRatio() {
		var totalSumm = dotted2Int($("#demoQuittancePayForm_total_pay").val());
		var nonZeroSumms = DETAILS.findAll(function (qd) {
			return qd.toPay > 0;
		});

		var summs = {};
		// set summs to zero
		DETAILS.each(function (qd) {
			summs[qd.id] = 0;
		});

		var last = nonZeroSumms.last();
		var summ = 0;
		var totalToPay = dotted2Int('<s:property value="%{getTotalPayable()}" />');
		nonZeroSumms.each(function (qd) {
			if (qd.id != last.id) {
				// http://msmvps.com/blogs/rexiology/archive/2006/01/09/80628.aspx
				// cast float to integer trick
				var nextSumm = (totalSumm * qd.toPay) / totalToPay | 0;
				summ += nextSumm;
				summs[qd.id] = nextSumm;
			}
		});

		// set last element summ
		summs[last.id] = totalSumm - summ;

		// set summs to their values
		for (var id in summs) {
			$("#demoQuittancePayForm_servicePayValue_" + id + "_").val(int2Dotted(summs[id]));
		}
	}
</script>

<s:actionerror />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td><s:text name="eirc.quittances.quittance_pay.quittance_number" />:</td>
		<td><s:property value="%{quittanceNumber}" /></td>
	</tr>
	<tr>
		<td><s:text name="month" />:</td>
		<td><s:date format="MM/yyyy" name="quittance.dateFrom" /></td>
	</tr>
	<tr>
		<td><s:text name="eirc.quittances.quittance_pay.fio" />:</td>
		<td><s:property value="%{getFIO()}" /></td>
	</tr>
	<tr>
		<td><s:text name="eirc.quittances.quittance_pay.address" />:</td>
		<td><s:property value="%{getAddress()}" /></td>
	</tr>
	<tr>
		<td><s:text name="eirc.eirc_account" />:</td>
		<td><s:property value="quittance.eircAccount.accountNumber" /></td>
	</tr>
</table>

<br />

<s:form id="demoQuittancePayForm" action="quittancePay">

	<s:hidden name="quittanceNumber" value="%{quittanceNumber}" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" nowrap="nowrap"><s:text name="eirc.quittances.quittance_pay.service" /></td>
			<td class="th"><s:text name="eirc.quittances.quittance_pay.service_supplier" /></td>
			<td class="th"><s:text name="eirc.quittances.quittance_pay.payable" /></td>
			<td class="th"><s:text name="eirc.quittances.quittance_pay.payed" /></td>
			<td class="th"><s:text name="eirc.quittances.quittance_pay.pay" /></td>
		</tr>

		<s:iterator value="%{quittance.orderedQuittanceDetails}" id="qd">
			<tr class="cols_1_error" style="display:none;">
				<td colspan="4" />
			</tr>
			<tr class="cols_1">
				<td class="col" nowrap="nowrap">
					<s:if test="%{#qd.consumer.service.isSubService()}">&nbsp;&nbsp;&nbsp;<i><s:property
							value="%{getServiceName(#qd)}" /></i></s:if>
					<s:else><s:property value="%{getServiceName(#qd)}" />
						<!-- Add quittance details to array -->
						<script type="text/javascript">
							DETAILS.push(new QD(
									"<s:property value="%{#qd.id}" />",
									"<s:property value="%{getServiceName(#qd)}" />",
									"<s:property value="%{getServiceProviderName(#qd)}" />",
									"<s:property value="%{#qd.outgoingBalance}" />",
									"<s:property value="%{getPayedSumm(#qd)}" />"));
						</script>
					</s:else>
				</td>
				<td class="col"><s:property value="%{getServiceProviderName(#qd)}" /></td>
				<td class="col" id="paySumm_<s:property value="%{#qd.id}" />"><s:property
						value="%{getPayable(#qd)}" /></td>
				<td class="col"><s:property value="%{getPayedSumm(#qd)}" /></td>
				<td class="col">
					<s:if test="%{!#qd.consumer.service.isSubService()}">
						<s:textfield name="servicePayValue[%{#qd.id}]" value="%{getPayable(#qd)}"
									 cssStyle="width: 100%; text-align: right;" /></s:if></td>
			</tr>
		</s:iterator>

		<tr class="cols_1">
			<td class="col" colspan="2" style="text-align:right;font-weight:bold;"><s:text
					name="eirc.quittances.quittance_pay.total_payable" /></td>
			<td class="col" style="font-weight:bold;"><s:property value="%{getTotalPayable()}" /></td>
			<td class="col" style="font-weight:bold;"><s:property value="%{getTotalPayed()}" /></td>
			<td class="col"><s:textfield name="total_pay" value="%{getTotalPayable()}"
										 cssStyle="width: 100%; text-align: right;" /></td>
		</tr>

		<tr>
			<td colspan="4" style="text-align:left;">
				<input type="button" value="<s:text name="eirc.quittance.payment.pay_by_ratio"/>"
					   class="btn-exit" onclick="divideByRatio();" />
				<input type="button" value="<s:text name="eirc.quittance.payment.pay_asc"/>"
					   class="btn-exit" onclick="divideAscending();" />
			</td>
			<td style="text-align:right;">
				<input type="submit" name="submitted" value="<s:text name="eirc.quittances.quittance_pay.pay"/>"
					   class="btn-exit" style="width: 100%;" />
			</td>
		</tr>

	</table>

</s:form>
