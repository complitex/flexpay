<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

	var fieldChain = new Array();
	var currentFieldIndex = 0;

	$(function() {
		createFieldChain();
	});

	/**
	 * Field chain functions
	 */	
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
		if (event.keyCode == 13 || event.keyCode == 9) {
			var nextFieldId = fieldChain[currentFieldIndex + 1];
			$('#' + nextFieldId).focus();
			$('#' + nextFieldId).select();
			event.preventDefault();
		}
	}
</script>