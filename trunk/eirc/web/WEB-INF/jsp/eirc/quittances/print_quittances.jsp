<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="printQuittances">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="middle" class="cols_1">
            <td class="col">
                <%@include file="/WEB-INF/jsp/orgs/filters/service_organization_filter.jsp" %>
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col">
                <s:text name="common.from" />
                <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col">
                <s:text name="common.till" />
                <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp" %>
            </td>
        </tr>
        <tr class="cols_1">
            <td colspan="2" align="center">
                <s:submit name="submitted" value="%{getText('common.upload')}" cssClass="btn-exit"/>
            </td>
        </tr>
    </table>
</s:form>
