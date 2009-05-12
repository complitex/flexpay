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
			var form = $('#quittancePayForm');
			var totalPaySumm = dotted2Int($('#totalToPay', form).val());
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
				'payments[<s:property value="#serviceIndx"/>]' : {
					'validPayValue': true//,
					//'payValue_<s:property value="#serviceIndx"/>_is_not_too_big': true
				},
			</s:iterator>
			</s:iterator>
				'inputSumm': {
					'inputIsEnough': true,
					'validPayValue': true
				}
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
		var elements = $("input[id^=payments_]", form);

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
