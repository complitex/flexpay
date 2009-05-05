<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate"/>
</s:if><s:else>
	<script type="text/javascript">
		$(function() {
			$('#beginTimeFilter').timepickr({
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

	<input type="text" name="beginTimeFilter.stringDate" id="beginTimeFilter"
		   value="<s:property value="beginTimeFilter.stringDate" />" readonly="readonly"/>
</s:else>
