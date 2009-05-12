<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endTimeFilter.readOnly">
	<s:property value="endTimeFilter.stringDate"/>
</s:if><s:else>
	<style type="text/css">@import "<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none"/>";</style>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.js" includeParams="none"/>"></script>
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
