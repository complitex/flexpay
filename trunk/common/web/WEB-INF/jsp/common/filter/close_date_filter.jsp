
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="closeDateFilter.readOnly">
    <s:property value="closeDateFilter.stringDate" />
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

    <script type="text/javascript">
        FP.calendars("#closeDateFilter", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    </script>

    <input type="text" name="closeDateFilter.stringDate" id="closeDateFilter" value="<s:property value="closeDateFilter.stringDate" />" readonly="readonly" />

</s:else>
