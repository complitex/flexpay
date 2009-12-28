<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="endTimeFilter.readOnly">
	<s:property value="endTimeFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_timeentry.jsp"%>

    <input type="text" name="endTimeFilter.stringDate" id="endTimeFilter"
           value="<s:property value="endTimeFilter.stringDate" />" readonly="readonly" />

    <script type="text/javascript">
        $("#endTimeFilter").ready(function() {
            $("#endTimeFilter").timeEntry({
                spinnerImage: FP.base + "/resources/common/js/jquery/timeentry/spinnerDefault.png",
                show24Hours: true,
                showSeconds: true
            });
        });
    </script>

</s:else>
