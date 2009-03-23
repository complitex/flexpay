
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="endDateFilter.readOnly">
    <s:property value="endDateFilter.stringDate" />
</s:if><s:else>
    <script type="text/javascript">
        FP.calendars("#endDateFilter", true);
    </script>
    <input type="text" name="endDateFilter.stringDate" id="endDateFilter" value="<s:property value="endDateFilter.stringDate" />" readonly="readonly" />
</s:else>
