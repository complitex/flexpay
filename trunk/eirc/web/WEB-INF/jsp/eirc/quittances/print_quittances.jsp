<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form action="printQuittances">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="middle" class="cols_1">
            <td class="col">
                <%@include file="../filters/service_organization_filter.jsp" %>
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col">
                <s:text name="ab.from" />
                <%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col">
                <s:text name="ab.till" />
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
