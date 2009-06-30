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

	function replaceEmptyValueWithZero(id) {
		if ($.trim($('#' + id).val()) == '') {
			$('#' + id).val('0.00');
		}
	}
</script>