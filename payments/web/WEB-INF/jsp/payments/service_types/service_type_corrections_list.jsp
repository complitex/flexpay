<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="3">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceTypeCorrectionCreate"><s:param name="serviceType.id" value="serviceType.id" /><s:param name="dataCorrection.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="payments.service_type.data_source" /></td>
        <td class="th"><s:text name="payments.service_type.extrenal_id" /></td>
    </tr>
    <s:iterator value="dataCorrections" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col"><s:property value="dataSourceDescription.description" /></td>
            <td class="col"><s:property value="externalId" /></td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceTypeCorrectionCreate"><s:param name="serviceType.id" value="serviceType.id" /><s:param name="dataCorrection.id" value="0" /></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
