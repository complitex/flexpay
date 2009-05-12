<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate" />
</s:if><s:else>
	<style type="text/css">@import "<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.css" includeParams="none"/>";</style>
	<script type="text/javascript" src="<s:url value="/resources/common/js/jquery/timeentry/jquery.timeentry.js" includeParams="none"/>"></script>
	<script type="text/javascript">
		$(function() {
			$('#beginTimeFilter').timeEntry({
				show24Hours: true,
				showSeconds: true
			});
		});
	</script>

	<input type="text" name="beginTimeFilter.stringDate" id="beginTimeFilter"
		   value="<s:property value="beginTimeFilter.stringDate" />" readonly="readonly" />
</s:else>
