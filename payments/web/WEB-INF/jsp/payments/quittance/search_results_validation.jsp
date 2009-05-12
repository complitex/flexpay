<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	/*
		Common functions
	*/
	// replaces all the commas in string with dots
	function replaceCommaWithDot(value) {
		return value.replace(",", ".");
	}

	// Convert from big decimal format
	function dotted2Int(i) {
		i = replaceCommaWithDot(i);
		var dotpos = i.indexOf(".");
		//noinspection PointlessArithmeticExpressionJS
		return dotpos != -1 ? i.substring(0, dotpos)*100 + i.substring(dotpos + 1)*1 : i*100;
	}

	// Convert integer to big decimal format
	function int2Dotted(i) {
		var divider = 100;
		var mod = i % divider;
		return ((i-mod)/divider).toString() + "." + (mod < 10 ? "0" + mod : mod);
	}
	
	/*
		Validation functions
	*/

	// matches whether given line is valid payment value (positive number with no more than two digits after decimal point)
	function isValidPayValue(value) {
		return value.match(/^(\d)+([\.,]?\d{0,2})$/);
	}

	// validator rules initializing
	$(function() {
		$.validator.addMethod('validPayValue', function(value, element) {
			return isValidPayValue(value);
		}, '<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value"/>');
	});

	<s:iterator value="quittanceInfos" status="nQI">
	$(function() {
			<s:iterator value="%{detailses}" id="qd">
				<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
		$.validator.addMethod('payValue_<s:property value="%{#nQI.index}"/>_<s:property value="#serviceId"/>_is_not_too_big', function(value, element) {
			return dotted2Int(value) <= dotted2Int('<s:property value="%{outgoingBalance}"/>');
		}, '<s:text name="eirc.error.quittances.quittance_pay.pay_value_too_big"/>');
			</s:iterator>

		$.validator.addMethod('totalPaymentIsSumm<s:property value="%{#nQI.index}"/>', function(value, element) {
			var form = $('#quittancePayForm<s:property value="%{#nQI.index}"/>');
			var totalPaySumm = dotted2Int($('#totalToPay', form).val());
			var actualSumm = 0;

			<s:iterator value="%{detailses}" id="qd">
			actualSumm += dotted2Int($('#paymentsMap_<s:property value="%{getServiceId(serviceMasterIndex)}"/>_', form).val());
			</s:iterator>

			return totalPaySumm == actualSumm;
		}, '<s:text name="payments.quittances.quittance_pay.total_pay_is_not_summ"/>');

		$.validator.addMethod('inputIsEnough<s:property value="%{#nQI.index}"/>', function(value, element) {
			var form = $('#quittancePayForm<s:property value="%{#nQI.index}"/>');
			var totalPaySumm = dotted2Int($('#totalToPay', form).val());
			var inputSumm = dotted2Int(value);
			return totalPaySumm <= inputSumm;
		}, '<s:text name="payments.quittances.quittance_pay.input_summ_is_too_small"/>');
	});

	// validator initializing
	var validator<s:property value="%{#nQI.index}"/>;
	$(function() {
		validator<s:property value="%{#nQI.index}"/> = $("#quittancePayForm<s:property value="%{#nQI.index}"/>").validate({
			rules: {
			<s:iterator value="detailses" id="qd">
				<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
				'paymentsMap[<s:property value="#serviceId"/>]' : {
					'validPayValue': true,
					'payValue_<s:property value="%{#nQI.index}"/>_<s:property value="#serviceId"/>_is_not_too_big': true
				},
			</s:iterator>
				'inputSumm': {
					'inputIsEnough<s:property value="%{#nQI.index}"/>': true,
					'validPayValue': true
				},
				'totalToPay': 'totalPaymentIsSumm<s:property value="%{#nQI.index}"/>'				
			},
			messages: {},
			errorClass: "cols_1_error",
			errorElement: "span",
			showErrors: function(errorMap, errorList) {
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
	</s:iterator>

	/*
		Total summ division functions
	 */

	<%--// Quittance details object--%>
//	function QD(id, serviceId, title, provider, toPay, payed) {
//		this.id = id;
//		this.serviceId = serviceId;
//		this.title = title;
//		this.provider = provider;
//		this.toPay = dotted2Int(toPay);
//		this.payed = dotted2Int(payed);
//
//		this.toString = function() {
//			return "id : " + this.id + ", " +
//				   "serviceId : " + this.serviceId + ", " +
//				   "title : " + this.title + ", " +
//				   "provider : " + this.provider + ", " +
//				   "toPay : " + int2Dotted(this.toPay) + " (" + this.toPay+ "), " +
//				   "payed : " + int2Dotted(this.payed);
//		};
//	}
//
//	var DETAILS = $.protify([]);

	<%--// divides total summ between services in ascending order--%>
	<%--function divideAscending() {--%>
		<%--var totalSumm = dotted2Int($('#quittancePayForm_totalToPay').val());--%>
		<%--var sortBySumm = DETAILS.sort(function (qd1, qd2) {--%>
			<%--return qd2.toPay - qd1.toPay;--%>
		<%--});--%>

		<%--var summs = {};--%>

		<%--// set summs to zero--%>
		<%--for (var i = 0; i < sortBySumm.length; ++i) {--%>
			<%--var qd = sortBySumm[i];--%>
			<%--summs[qd.serviceId] = 0;--%>
		<%--}--%>

		<%--// divide summs--%>
		<%--while (totalSumm > 0) {--%>
			<%--for (i = 0; i < sortBySumm.length && totalSumm > 0; ++i) {--%>
				<%--qd = sortBySumm[i];--%>
				<%--var nextSumm = totalSumm >= qd.toPay ? qd.toPay : totalSumm;--%>
				<%--summs[qd.serviceId] += nextSumm;--%>
				<%--totalSumm -= nextSumm;--%>
			<%--}--%>
		<%--}--%>
		<%----%>
		<%--// set summs to their values--%>
		<%--for (var serviceId in summs) {--%>
			<%--$("#quittancePayForm_paymentsMap_" + serviceId + "_").val(int2Dotted(summs[serviceId]));--%>
		<%--}--%>

		<%--// updating validation message--%>
		<%--validator.form();--%>
	<%--}--%>

	<%--// divides total pay summ between services proportionally--%>
	<%--function divideByRatio() {--%>
		<%--var totalSumm = dotted2Int($('#quittancePayForm_totalToPay').val());--%>
		<%--var nonZeroSumms = DETAILS.findAll(function (qd) {--%>
			<%--return qd.toPay > 0;--%>
		<%--});--%>

		<%--var summs = {};--%>
		<%--// set summs to zero--%>
		<%--DETAILS.each(function (qd) {--%>
			<%--summs[qd.serviceId] = 0;--%>
		<%--});--%>

		<%--var last = nonZeroSumms.last();--%>
		<%--var summ = 0;--%>
		<%--var totalToPay = dotted2Int('<s:property value="%{getTotalPayable()}"/>');--%>
		<%--nonZeroSumms.each(function (qd) {--%>
			<%--if (qd.id != last.id) {--%>
				<%--// http://msmvps.com/blogs/rexiology/archive/2006/01/09/80628.aspx--%>
				<%--// cast float to integer trick--%>
				<%--var nextSumm = (totalSumm * qd.toPay) / totalToPay | 0;--%>
				<%--summ += nextSumm;--%>
				<%--summs[qd.serviceId] = nextSumm;--%>
			<%--}--%>
		<%--});--%>

		<%--// set last element summ--%>
		<%--summs[last.serviceId] = totalSumm - summ;--%>

		<%--// set summs to their values--%>
		<%--for (var serviceId in summs) {--%>
			<%--$("#quittancePayForm_paymentsMap_" + serviceId + "_").val(int2Dotted(summs[serviceId]));--%>
		<%--}--%>

		<%--// updating validation message--%>
		<%--validator.form();--%>
	<%--}--%>

	/*
		Change calculation change
	 */

	// change modification
	function onInputUpdate(form) {

		var totalPaySumm = dotted2Int($('#totalToPay', form).val());
		var inputSumm = dotted2Int($('#inputSumm', form).val());
		var changeSumm = inputSumm - totalPaySumm;

		$('#changeSumm', form).val(int2Dotted(changeSumm));
	}

	// total payment summ calculation
	function onPaymentUpdate(form) {

		var total = 0;
		var elements = $("input[id^=paymentsMap_]", form);

		for (var i = 0; i < elements.length; i++) {
			if (!isValidPayValue($(elements[i]).val())) {
				$('#totalToPay', form).val('<s:text name="payments.quittances.quittance_pay.unaccessible"/>');
				$('#changeSumm', form).val('<s:text name="payments.quittances.quittance_pay.unaccessible"/>');
				return;
			}

			total += dotted2Int($(elements[i]).val());
		}

		$('#totalToPay', form).val(int2Dotted(total));
		$('#inputSumm', form).val(int2Dotted(total));
		$('#changeSumm', form).val(int2Dotted(0));
	}
</script>