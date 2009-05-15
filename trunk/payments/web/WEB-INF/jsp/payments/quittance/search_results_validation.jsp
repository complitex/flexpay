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
		return dotpos != -1 ? i.substring(0, dotpos) * 100 + i.substring(dotpos + 1) * 1 : i * 100;
	}

	// Convert integer to big decimal format
	function int2Dotted(i) {
		var divider = 100;
		var mod = i % divider;
		return ((i - mod) / divider).toString() + "." + (mod < 10 ? "0" + mod : mod);
	}

	/*
	 Validation functions
	 */

	// matches whether given line is valid payment value (empty string or positive number with no more than two digits after decimal point)
	function isValidPayValue(value) {
		return value == "" || value.match(/^(\d)+([\.,]?\d{0,2})$/);
	}

	// validator rules initializing
	$(function() {

		$.validator.addMethod('validPayValue', function(value, element) {
			return isValidPayValue(value);
		}, '<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value"/>');

	<%--<s:iterator value="quittanceInfos" id="qi">--%>
	<%--<s:iterator value="detailses" status="status">--%>
	<%--<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>--%>
	<%--<s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>--%>
	<%--$.validator.addMethod('payValue_<s:property value="#serviceIndx"/>_is_not_too_big', function(value, element) {--%>
	<%--return dotted2Int(value) <= dotted2Int('<s:property value="%{outgoingBalance}"/>');--%>
	<%--}, '<s:text name="eirc.error.quittances.quittance_pay.pay_value_too_big"/>');--%>
	<%--</s:iterator>--%>
	<%--</s:iterator>--%>

		$.validator.addMethod('inputIsEnough', function(value, element) {
			if (!isValidPayValue($('#totalToPay').val())) {
				return true;
			}
						
			var totalPaySumm = dotted2Int($('#totalToPay').val());
			var inputSumm = dotted2Int(value);
			return totalPaySumm <= inputSumm;
		}, '<s:text name="payments.quittances.quittance_pay.input_summ_is_too_small"/>');
	});

	// validator initializing
	var validator;
	$(function() {
		validator = $("#quittancePayForm").validate({
			rules: {
				<s:iterator value="quittanceInfos" id="qi" status="nQI">
				<s:iterator value="detailses" status="status">
				<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
				<s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>
				'payments[\'<s:property value="#serviceIndx"/>\']' : {
					'validPayValue': true<%--, 'payValue_<s:property value="#serviceIndx"/>_is_not_too_big': true--%>
				},
				</s:iterator>
				</s:iterator>
				'inputSumm': {
					'validPayValue': true,
					'inputIsEnough': true
				}
			},
			messages: {},
			errorClass: "cols_1_error",
			errorElement: "span",
			success: function(label) {
				var nextRow = label.parent('td').parent('tr.cols_1_error').next('tr.cols_1');

				// removing error label and hiding it's row
				label.parent("td").parent("tr.cols_1_error").css("display", "none");
				label.remove();

				// updating total payment summ and change
				updateTotal();
				updateChange();
				if (validator.numberOfInvalids() == 0) {
					// if the updated field is a payment we should update input
					if (nextRow.hasClass('service_payment')) {
						updateInput();
					}
				}
			},
			showErrors: function(errorMap, errorList) {
				updateTotal();
				updateChange();
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
			},
			onsubmit: false
		});
	});

	// change modification
	function updateChange() {

		if (!isValidPayValue($('#inputSumm').val()) || !isValidPayValue($('#totalToPay').val())) {
			$('#changeSumm').val('<s:text name="payments.quittances.quittance_pay.unaccessible"/>');
			return;
		}

		var totalPaySumm = dotted2Int($('#totalToPay').val());
		var inputSumm = dotted2Int($('#inputSumm').val());
		var changeSumm = inputSumm - totalPaySumm;

		$('#changeSumm').val(int2Dotted(changeSumm));
	}

	// total payment summ calculation
	function updateTotal() {

		var total = 0;
		var elements = $("input[id^=payments_]");

		for (var i = 0; i < elements.length; i++) {
			if (!isValidPayValue($(elements[i]).val())) {
				$('#totalToPay').val('<s:text name="payments.quittances.quittance_pay.unaccessible"/>');
				$('#changeSumm').val('<s:text name="payments.quittances.quittance_pay.unaccessible"/>');
				return;
			}

			total += dotted2Int($(elements[i]).val());
		}

		$('#totalToPay').val(int2Dotted(total));
	}

	// sets input summ value equal to the total summ
	function updateInput() {
		$('#inputSumm').val($('#totalToPay').val());
		updateChange();
	}

	function replaceEmptyValueWithZero(id) {
		if ($.trim($('#' + id).val()) == '') {
			$('#' + id).val('0.00');
		}
	}
</script>
