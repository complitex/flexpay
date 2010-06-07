<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="closeDateFilter.readOnly">
    <s:property value="closeDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

    <s:textfield name="closeDateFilter.stringDate" id="closeDateFilter" size="10" readonly="true" />

    <script type="text/javascript">
        FP.calendars("closeDateFilter", true);
    </script>
</s:else>
