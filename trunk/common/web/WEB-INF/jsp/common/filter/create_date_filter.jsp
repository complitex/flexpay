<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="createDateFilter.readOnly">
    <s:property value="createDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

    <script type="text/javascript">
        FP.calendars("createDateFilter", true);
    </script>
    <input type="text" name="createDateFilter.stringDate" id="createDateFilter" value="<s:property value="createDateFilter.stringDate" />" readonly="readonly" />

</s:else>
