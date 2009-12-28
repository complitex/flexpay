<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>

    <input type="text" name="beginTimeFilter.stringDate" id="beginTimeFilter"
           value="<s:property value="beginTimeFilter.stringDate" />" readonly="readonly" />

	<script type="text/javascript">
        $("#beginTimeFilter").ready(function() {
            $("#beginTimeFilter").timeEntry({
                spinnerImage: FP.base + "/resources/common/js/jquery/timeentry/spinnerDefault.png",
                show24Hours: true,
                showSeconds: true
            });
        });
	</script>

</s:else>
