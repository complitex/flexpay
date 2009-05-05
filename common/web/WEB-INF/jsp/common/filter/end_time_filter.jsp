<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endTimeFilter.readOnly">
	<s:property value="endTimeFilter.stringDate"/>
</s:if><s:else>
	<script type="text/javascript">
		$(function() {
			$('#endTimeFilter').timepickr({
				convention: 24,
				hours: true,
				minutes: true,
				seconds: true,
				rangeMin: [0, 15, 30, 45, 59],
				rangeSec: [0, 30, 59],
				format24: "{h:02.d}:{m:02.d}:{s:02.d}"
			});
		});
	</script>

	<input type="text" name="endTimeFilter.stringDate" id="endTimeFilter"
		   value="<s:property value="endTimeFilter.stringDate" />" readonly="readonly"/>
</s:else>
