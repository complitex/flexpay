<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="common.language" /></td>
        <td class="th"><s:text name="payments.service.name" /></td>
    </tr>
    <s:iterator value="service.descriptions" status="rowstatus">
        <tr valign="middle" class="cols_1">
            <td class="col_1s">
                <s:property value="#rowstatus.index + 1" />
            </td>
            <td class="col">
                <s:property value="getLangName(lang)" /><s:if test="lang.default"> (default)</s:if>
            </td>
            <td class="col">
                <s:property value="name" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service.can_return" />:</td>
        <td class="col" colspan="2">
            <s:if test="service.canReturn"><s:text name="common.yes" /></s:if><s:else><s:text name="common.no" /></s:else>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service.external_code" />:</td>
        <td class="col" colspan="2">
            <s:property value="service.externalCode" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service.begin_date" />:</td>
        <td class="col" colspan="2">
            <s:date name="service.beginDate" format="yyyy/MM/dd" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service.end_date" />:</td>
        <td class="col" colspan="2">
            <s:date name="service.endDate" format="yyyy/MM/dd" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service_type" />:</td>
        <td class="col" colspan="2">
            <s:property value="getTranslationName(service.serviceType.typeNames)" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service_type.code" />:</td>
        <td class="col" colspan="2">
            <s:property value="service.serviceType.code" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="orgs.service_provider" />:</td>
        <td class="col" colspan="2">
            <s:property value="getTranslationName(service.serviceProvider.organization.names)" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="common.measure_unit" />:</td>
        <td class="col" colspan="2">
            <s:if test="service.measureUnit != null">
                <s:property value="getTranslationName(service.measureUnit.unitNames)" />
            </s:if><s:else>
                -
            </s:else>
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col_1s" colspan="2"><s:text name="payments.service.parent_service" />:</td>
        <td class="col" colspan="2">
            <s:if test="service.parentService != null">
                <s:property value="getTranslationName(service.parentService.descriptions)" />
            </s:if><s:else>
                -
            </s:else>
        </td>
    </tr>
    <tr>
        <td colspan="4" height="3" bgcolor="#4a4f4f"></td>
    </tr>
    <tr>
        <td colspan="4">
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="serviceEdit"><s:param name="service.id" value="service.id" /></s:url>';"
                   value="<s:text name="common.edit" />" />
        </td>
    </tr>
</table>
