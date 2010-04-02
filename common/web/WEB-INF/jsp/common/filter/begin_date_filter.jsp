<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:if test="beginDateFilter.readOnly">
    <s:property value="beginDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>

    <input type="text" name="beginDateFilter.stringDate" id="beginDateFilter" size="10" value="<s:property value="beginDateFilter.stringDate" />" readonly="readonly" />

    <script type="text/javascript">
        FP.calendars("beginDateFilter", true);
    </script>

</s:else>
