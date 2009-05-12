<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate" />
</s:if><s:else>
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
