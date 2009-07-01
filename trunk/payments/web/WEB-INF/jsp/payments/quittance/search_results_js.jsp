<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	/**
	 *	Common functions
	 */
	function dotted2Int(i) {
		i = i.replace(",", ".");
		var dotpos = i.indexOf(".");
		//noinspection PointlessArithmeticExpressionJS
		return dotpos != -1 ? i.substring(0, dotpos) * 100 + i.substring(dotpos + 1) * 1 : i * 100;
	}

	function int2Dotted(i) {
		var divider = 100;
		var mod = i % divider;
		return ((i - mod) / divider).toString() + "." + (mod < 10 ? "0" + mod : mod);
	}

	/*
	 *	Replaces empty value with zero summ and comma with dot
	 */
	function processPaymentValue(fieldId) {
		var paymentValue = $.trim($('#' + fieldId).val());

		if (paymentValue == '') {
			paymentValue = '0.00';
		} else {
			paymentValue = paymentValue.replace(",", ".");
		}

		$('#' + fieldId).val(paymentValue);
	}

	/*
	 *	Validation functions
	 */
	var errorFields = new Array();

	function addErrorField(fieldId) {
		if (!containsError(fieldId)) {
			errorFields[errorFields.length] = fieldId;
			disableButtons();
		}
	}

	function removeErrorField(fieldId) {
		if (containsError(fieldId)) {
			var newErrorFields = new Array();
			for (var i = 0; i < errorFields.length; i++) {
				if (errorFields[i] != fieldId) {
					newErrorFields[newErrorFields.length] = errorFields[i];
				}
			}
			errorFields = newErrorFields;

			if (errorFields.length == 0) {
				enableButtons();
			}
		}
	}

	function containsError(fieldId) {
		for (var i = 0; i < errorFields.length; i++) {
			if (errorFields[i] == fieldId) {
				return true;
			}
		}

		return false;
	}

	function getTotalErrorsNumber() {
		return errorFields.length;
	}

	function isValidPaymentValue(value) {
		return value.match(/^(\d)+([\.,]?\d{0,2})$/);
	}

	function validatePaymentValue(fieldId) {
		var field = $('#' + fieldId);
		var paymentValue = field.val();

		var previousRow = field.parent("td").parent("tr").prev("tr");
		var errorCell = previousRow.children()[0];

		if (isValidPaymentValue(paymentValue)) {
			// hide error
			$(errorCell).text('');
			$(previousRow).css('display', 'none');
			removeErrorField(fieldId);
			return true;
		} else {
			// show error			
			$(errorCell).text('<s:text name="eirc.error.quittances.quittance_pay.invalid_pay_value"/>');
			$(previousRow).css("display", "table-row");
			addErrorField(fieldId);
			return false;
		}
	}

	function validateInputValue() {

		var inputValue = $('#inputSumm').val();
		var totalValue = $('#totalToPay').val();

		if (!isValidPaymentValue(inputValue) || !isValidPaymentValue(totalValue)) {
			return true;
		}

		var totalPaySumm = dotted2Int(totalValue);
		var inputSumm = dotted2Int(inputValue);

		var previousRow = $('#inputSumm').parent("td").parent("tr").prev("tr");
		var errorCell = previousRow.children()[0];
		if (totalPaySumm > inputSumm) {
			// show error
			$(errorCell).text('<s:text name="payments.quittances.quittance_pay.input_summ_is_too_small"/>');
			$(previousRow).css("display", "table-row");
			addErrorField('inputSumm');
			return false;
		} else {
			$(errorCell).text('');
			$(previousRow).css('display', 'none');
			removeErrorField('inputSumm');
			return true;
		}
	}

	/**
	 * Auto-update functions (total/change)
	 */
	function updateTotal() {
		var total = 0;
		var elements = $("input[id^=payments_]");

		for (var i = 0; i < elements.length; i++) {
			if (!isValidPaymentValue($(elements[i]).val())) {
				$('#totalToPay').val('');
				$('#inputSumm').val('');
				$('#changeSumm').val('');
				return;
			}

			total += dotted2Int($(elements[i]).val());
		}

		$('#totalToPay').val(int2Dotted(total));
		$('#inputSumm').val(int2Dotted(total));
		$('#changeSumm').val('0.00');
	}

	function updateChange() {
		var inputValue = $('#inputSumm').val();
		var totalValue = $('#totalToPay').val();

		if (!isValidPaymentValue(inputValue) || !isValidPaymentValue(totalValue)) {
			$('#changeSumm').val('');
			return;
		}

		var totalPaySumm = dotted2Int(totalValue);
		var inputSumm = dotted2Int(inputValue);
		var changeSumm = inputSumm - totalPaySumm;

		if (changeSumm < 0) {
			$('#changeSumm').val('');
			return;
		}

		$('#changeSumm').val(int2Dotted(changeSumm));
	}

	/**
	 * UI buttons controlling functions
	 */
	var paymentEnabled = false;

	function disableButton(button) {
		$(button).attr('disabled', 'disabled');
		$(button).removeClass('btn-exit');
        $(button).addClass('btn-search');
	}

	function enableButton(button) {
		$(button).removeAttr('disabled');
		$(button).removeClass('btn-search');
        $(button).addClass('btn-exit');
	}

	function disablePayment() {
		disableButton('#payQuittanceButton');
		paymentEnabled = false;
	}

	function enablePayment() {
		enableButton('#payQuittanceButton');
		paymentEnabled = true;
	}

	function enableButtons() {
		enableButton('#payQuittanceButton');
		enableButton('#printQuittanceButton');

		if (paymentEnabled) {
			enablePayment();
		} else {
			disablePayment();
		}
	}

	function disableButtons() {
		disableButton('#payQuittanceButton');
		disableButton('#printQuittanceButton');
	}

	function doPayQuittance() {
		$("#quittancePayForm").attr("action", "<s:url action="paymentsQuittancePay" />").submit();
		$("#quittancePayForm").removeAttr('action');
	}

	function doPrintQuittance() {
		$("#quittancePayForm").attr("action", "<s:url action="paymentOperationReportAction" />").attr("target", "_blank").submit();
		$("#quittancePayForm").removeAttr('action');
		$("#quittancePayForm").removeAttr('target');
		enablePayment();
	}

	/*
	 * Field chain functions
	 */
	var fieldChain = new Array();
	var currentFieldIndex = 0;

	function createFieldChain() {
		// selecting all the payments inputs
		var paymentInputs = $('input[id^=payments_]');
		for (var i = 0; i < paymentInputs.length ; i++) {
			fieldChain[i] = $(paymentInputs[i]).attr('id');
		}
		// adding total input summ field to field chain
		fieldChain[fieldChain.length] = 'inputSumm';
		
		// setting focus to the first payments field
		$('#' + fieldChain[0]).focus();
		$('#' + fieldChain[0]).select();
		currentFieldIndex = 0;
	}

	function setCurrentFieldIndex(event) {

		var selectedFieldId = $(event.target).attr('id');

		for (var i = 0; i < fieldChain.length; i++) {
			if (fieldChain[i] == selectedFieldId) {
				currentFieldIndex = i;
				$('#' + fieldChain[i]).select();
				return;
			}
		}
	}

	function updateCurrentFieldIndex(event) {
		var oldNumberOfErrors = getTotalErrorsNumber();
		var nextFieldId = $(event.target).attr('id');
		var currentFieldId = fieldChain[currentFieldIndex];

		if (event.keyCode == 13 || (event.keyCode == 9 && !event.shiftKey)) {
			if (currentFieldIndex < fieldChain.length - 1) {
				nextFieldId = fieldChain[currentFieldIndex + 1];
				$('#' + nextFieldId).focus();
				$('#' + nextFieldId).select();

				if (getTotalErrorsNumber() > oldNumberOfErrors) {
					$('#' + currentFieldId).focus();
					$('#' + currentFieldId).select();
				}

				event.preventDefault();
			}
		} else if (event.keyCode == 9 && event.shiftKey) {
			if (currentFieldIndex > 0) {
				nextFieldId = fieldChain[currentFieldIndex - 1];
				$('#' + nextFieldId).focus();
				$('#' + nextFieldId).select();

				if (getTotalErrorsNumber() > oldNumberOfErrors) {
					$('#' + currentFieldId).focus();
					$('#' + currentFieldId).select();
				}

				event.preventDefault();
			}
		}
	}

	// FIXME ELIMINATE THIS HACKS
	function onChangePaymentHandler(id) {
		processPaymentValue(id);
		validatePaymentValue(id);
		updateTotal();
		disablePayment();
	}

	function onChangeInputHandler() {
		processPaymentValue('inputSumm');
		validatePaymentValue('inputSumm');
		validateInputValue();
		updateChange();
		disablePayment();
	}

	/*
	 *	Event mapping
	 */
	$(function() {
		bindEvents();
		createFieldChain();
		disablePayment();
	});

	function bindEvents() {

		/*
		 *	Processing entered values binding
		 */
		<s:iterator value="quittanceInfos" id="qi" status="nQI">
		<s:iterator value="detailses" status="status">
		<s:set name="serviceId" value="%{getServiceId(serviceMasterIndex)}"/>
		<s:set name="serviceIndx" value="%{getServiceFullIndex(#nQI.index, #serviceId)}"/>
		$('#payments_<s:property value="#serviceIndx"/>').bind('focus', function(event) {
			setCurrentFieldIndex(event);
		});

		$('#payments_<s:property value="#serviceIndx"/>').bind('keypress', function(event) {
			updateCurrentFieldIndex(event);
		});

		<%-- FIXME THIS EVENT IS ACTUALLY BOUND IN HTML (WHY IT DOESN'T WORK FROM HERE?!)--%>
		<%--$('#payments_<s:property value="#serviceIndx"/>').bind('change', function(event) {--%>
			<%--processPaymentValue('payments_<s:property value="#serviceIndx"/>');--%>
			<%--validatePaymentValue('payments_<s:property value="#serviceIndx"/>');--%>
			<%--updateTotal();--%>
			<%--disablePayment();--%>
		<%--});--%>
		</s:iterator>
		</s:iterator>

//		$('#inputSumm').bind('change', function(event) {
//			processPaymentValue('inputSumm');
//			validatePaymentValue('inputSumm');
//			validateInputValue();
//			updateChange();
//			disablePayment();
//		});

		$('#inputSumm').bind('focus', function(event) {
			setCurrentFieldIndex(event);
		});
		
		$('#inputSumm').bind('keypress', function(event) {
			updateCurrentFieldIndex(event);
		});

		$('#printQuittanceButton').bind('focus', function(event) {
			setCurrentFieldIndex(event);
		});

		$('#printQuittanceButton').bind('keypress', function(event) {
			updateCurrentFieldIndex(event);
		});

		$('#printQuittanceButton').bind('click', function(event) {
			doPrintQuittance();
		});

		$('#payQuittanceButton').bind('click', function(event) {
			doPayQuittance();
		});
	};
</script>