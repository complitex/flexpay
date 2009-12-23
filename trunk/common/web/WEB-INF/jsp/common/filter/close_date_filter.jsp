<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="closeDateFilter.readOnly">
    <s:property value="closeDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

    <script type="text/javascript">
        FP.calendars("closeDateFilter", true);
    </script>
    <input type="text" name="closeDateFilter.stringDate" id="closeDateFilter" value="<s:property value="closeDateFilter.stringDate" />" readonly="readonly" />
</s:else>
