
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="beginDateFilter.readOnly">
    <s:property value="beginDateFilter.stringDate" />
</s:if><s:else>

    <%@include file="/WEB-INF/jsp/common/jquery_ui.jsp"%>

    <script type="text/javascript">
        FP.calendars("#beginDateFilter", "<s:url value="/resources/common/js/jquery/jquery-ui/images/calendar.gif" includeParams="none" />");
    </script>

    <input type="text" name="beginDateFilter.stringDate" id="beginDateFilter" value="<s:property value="beginDateFilter.stringDate" />" readonly="readonly" />
</s:else>
