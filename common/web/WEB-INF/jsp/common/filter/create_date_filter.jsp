<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="createDateFilter != null && createDateFilter.readOnly">
    <s:property value="createDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

    <s:textfield name="createDateFilter.stringDate" id="createDateFilter" size="10" readonly="true" />

    <script type="text/javascript">
        FP.calendars("createDateFilter", true);
    </script>
</s:else>
