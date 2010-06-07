<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="beginTimeFilter.readOnly">
	<s:property value="beginTimeFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>

    <s:textfield name="beginTimeFilter.stringDate" id="beginTimeFilter" readonly="true" size="5" cssStyle="width:40px;" />

	<script type="text/javascript">
        $("#beginTimeFilter").ready(function() {
            $("#beginTimeFilter").timeEntry({
                spinnerImage:FP.base + "/resources/common/js/jquery/timeentry/spinnerDefault.png",
                show24Hours:true
            });
        });
	</script>

</s:else>
