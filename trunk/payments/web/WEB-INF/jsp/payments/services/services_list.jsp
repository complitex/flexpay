<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceEdit" includeParams="none"><s:param name="service.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="payments.service_type.name" /></td>
        <td class="th"><s:text name="eirc.service.description" /></td>
        <td class="th"><s:text name="eirc.service.service_provider" /></td>
        <td class="th"><s:text name="payments.service_type.code" /></td>
        <td class="th"><s:text name="eirc.service.external_code" /></td>
        <td class="th"><s:text name="eirc.service.begin_date" /></td>
        <td class="th"><s:text name="eirc.service.end_date" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="services" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col" nowrap>
                <s:if test="isSubService()">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</s:if>
                <a href="<s:url action="serviceView" includeParams="none"><s:param name="service.id" value="id" /></s:url>">
                    <s:property value="getTranslationName(serviceType.typeNames)" />
                </a>
            </td>
            <td class="col"><s:property value="getTranslationName(descriptions)" /></td>
            <td class="col"><s:property value="getTranslationName(serviceProvider.organization.names)" /></td>
            <td class="col"><s:property value="serviceType.code" /></td>
            <td class="col"><s:property value="externalCode" /></td>
            <td class="col"><s:date name="beginDate" format="yyyy/MM/dd" /></td>
            <td class="col"><s:date name="endDate" format="yyyy/MM/dd" /></td>
            <td class="col">
                <a href="<s:url action="serviceEdit" includeParams="none"><s:param name="service.id" value="id" /></s:url>"><s:text name="common.edit" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="10">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceEdit" includeParams="none"><s:param name="service.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
