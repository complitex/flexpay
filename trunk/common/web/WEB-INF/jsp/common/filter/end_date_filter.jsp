<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="endDateFilter != null && endDateFilter.readOnly">
    <s:property value="endDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

    <s:textfield name="endDateFilter.stringDate" id="endDateFilter" size="10" readonly="true" />

    <script type="text/javascript">
        FP.calendars("endDateFilter", true);
    </script>
</s:else>
