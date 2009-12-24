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
                show24Hours: true,
                showSeconds: true
            });
        });
    </script>

</s:else>
