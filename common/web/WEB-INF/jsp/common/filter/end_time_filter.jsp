<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endTimeFilter.readOnly">
	<s:property value="endTimeFilter.stringDate"/>
</s:if><s:else>
	<script type="text/javascript">
		$(function() {
			$('#endTimeFilter').timeEntry({
				show24Hours: true,
				showSeconds: true
			});
		});
	</script>

	<input type="text" name="endTimeFilter.stringDate" id="endTimeFilter"
		   value="<s:property value="endTimeFilter.stringDate" />" readonly="readonly"/>
</s:else>
