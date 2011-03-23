<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.switchSorter(["registrySorterByCreationDateButton"]);
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="14" align="right">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="<s:property value="registrySorterByCreationDate.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="/WEB-INF/jsp/payments/sorters/registry_sort_by_creation_date_header.jsp"%>
        </td>
        <td class="th"><s:text name="eirc.registry.number" /></td>
        <td class="th"><s:text name="eirc.sender" /></td>
        <td class="th"><s:text name="eirc.recipient" /></td>
        <td class="th"><s:text name="eirc.registry_type" /></td>
        <td class="th"><s:text name="eirc.load_date" /></td>
        <td class="th"><s:text name="eirc.records_number" /></td>
        <td class="th"><s:text name="eirc.status" /></td>
        <td class="th"><s:text name="payments.registry.commentary.edit" /></td>
        <td class="th">&nbsp;</td>
        <td class="th"><s:text name="eirc.registry.mb.file_download" /></td>
        <td class="th"><s:text name="eirc.registry.fp.file_download" /></td>
    </tr>
    <s:set name="fromDate" value="fromDate" />
    <s:set name="tillDate" value="tillDate" />
    <s:iterator value="registries" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:date name="creationDate" format="yyyy/MM/dd" /></td>
            <td class="col"><s:property value="id" /></td>
            <td class="col"><s:property value="getTranslationName(orgs.get(senderCode).names)" /></td>
            <td class="col"><s:property value="getTranslationName(orgs.get(recipientCode).names)" /></td>
            <td class="col"><s:text name="%{registryType.i18nName}" /></td>
            <td class="col"><s:date name="files.get(mbType).creationDate" format="yyyy/MM/dd HH:mm:ss" /></td>
            <td class="col"><s:property value="recordsNumber" /></td>
            <td class="col"><s:text name="%{registryStatus.i18nName}" /></td>
            <td class="col">
                <s:if test="registryType.code == @org.flexpay.common.persistence.registry.RegistryType@TYPE_CASH_PAYMENTS">
                    <a href="<s:url action="registryCommentaryEdit">
                                <s:param name="registry.id" value="id" />
                                <s:param name="senderOrganizationFilter.selectedId" value="senderOrganizationFilter.selectedId" />
                                <s:param name="recipientOrganizationFilter.selectedId" value="recipientOrganizationFilter.selectedId" />
                                <s:param name="registryTypeFilter.selectedId" value="registryTypeFilter.selectedId" />
                                <s:param name="serviceProviderFilter.selectedId" value="serviceProviderFilter.selectedId" />
                                <s:param name="fromDate" value="#fromDate" />
                                <s:param name="tillDate" value="#tillDate" />
                            </s:url>">
                        <s:text name="common.edit" />
                    </a>
                </s:if>
            </td>
            <td class="col">
                <a href="<s:url action="registryView" namespace="/payments"><s:param name="registry.id" value="id" /></s:url>">
                    <s:text name="common.view" />
                </a>
            </td>
            <td class="col">
                <a href="<s:url value="/download/%{files.get(mbType).id}.registry" />">
                    <s:property value="files.get(mbType).originalName" />
                </a>
            </td>
            <td class="col">
                <a href="<s:url value="/download/%{files.get(fpType).id}.registry" />">
                    <s:property value="files.get(fpType).originalName" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="14">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <%--<input type="button" value="<s:text name="eirc.process" />" class="btn-exit" onclick="process();" />--%>
        </td>
    </tr>
</table>
