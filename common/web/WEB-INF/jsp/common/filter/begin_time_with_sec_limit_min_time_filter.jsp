<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>

    <s:textfield name="beginTimeFilter.stringDate" id="beginTimeFilter" readonly="true" size="8" cssStyle="width:55px;" />

		<!--s:hidden value="minTime" id="minTime"/-->

	<script type="text/javascript">
        $("#beginTimeFilter").ready(function() {
            $("#beginTimeFilter").timeEntry({
                spinnerImage:FP.base + "/resources/common/js/jquery/timeentry/spinnerDefault.png",
                show24Hours:true,
                showSeconds:true,
								minTime:new Date(0, 0, 0, <s:property value="beginTimeFilter.minTime.hours" default="0" />, <s:property value="beginTimeFilter.minTime.minutes" default="0" />, <s:property value="beginTimeFilter.minTime.seconds" default="0"/>)
            });
        });
	</script>

</s:else>