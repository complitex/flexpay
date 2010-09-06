<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.eirc_account" />:</strong> <s:property value="eircAccount.accountNumber" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.eirc_account.person" />:</strong> <s:property value="eircAccount.person != null ? getFIO(eircAccount.person) : '(*) ' + eircAccount.consumerInfo.FIO" />
        </td>
	</tr>
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.eirc_account.address" />:</strong> <s:property value="getAddress(eircAccount.apartment)" />
        </td>
		<td class="col">
		</td>
	</tr>
</table>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" nowrap>
            <s:text name="eirc.service" /><br />
            (<s:text name="eirc.eirc_account.consumer_fio" />)
        </td>
        <s:iterator value="attributeTypes">
            <td class="th">
                <s:property value="getTranslationName(names)" /> <s:if test="measureUnit != null">(<s:property value="getTranslationName(measureUnit.unitNames)" />)</s:if>
            </td>
        </s:iterator>
    </tr>
    <s:iterator value="eircAccount.consumers" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right"><s:property value="#status.index + 1" /></td>
            <td class="col" nowrap>
                <s:property value="getServiceDescription(service)" /><br />
                (<s:property value="consumerInfo.lastName + ' ' + consumerInfo.firstName + ' ' + consumerInfo.middleName" />)
            </td>
            <s:iterator value="consumerAttributes.get(id)">
                <td class="col">
                    <s:if test="isNotNew()">
                        <s:property value="value()" />
                    </s:if>
                </td>
            </s:iterator>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="15">
            <s:if test="!eircAccount.consumers.isEmpty()">
                <input type="button" class="btn-exit"
                       onclick="window.location='<s:url action="eircAccountEditConsumerAttributes" includeParams="none"><s:param name="eircAccount.id" value="eircAccount.id" /></s:url>';"
                       value="<s:text name="common.edit" />" />
            </s:if>
            <input type="button" class="btn-exit" value="<s:text name="common.back" />" onclick="backf();" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    function backf() {

        FP.post("<s:url action="eircAccountsList" includeParams="none" />", {
            output: <s:property value="output" />,
            apartmentFilter: <s:property value="apartmentFilter" />,
            buildingFilter: <s:property value="buildingFilter" />,
            streetFilter: <s:property value="streetFilter" />,
            townFilter: <s:property value="townFilter" />,
            regionFilter: <s:property value="regionFilter" />,
            countryFilter: <s:property value="countryFilter" />,
            "personSearchFilter.searchString": "<s:property value="personSearchFilter.searchString" />",
            "eircAccountSorterByAccountNumber.active": <s:property value="eircAccountSorterByAccountNumber.active" />,
            "eircAccountSorterByAccountNumber.order": "<s:property value="eircAccountSorterByAccountNumber.order" />",
            "eircAccountSorterByAddress.active": <s:property value="eircAccountSorterByAddress.active" />,
            "eircAccountSorterByAddress.order": "<s:property value="eircAccountSorterByAddress.order" />",
            "pager.pageNumber":<s:property value="pager.pageNumber" />,
            "pager.pageSize":<s:property value="pager.pageSize" />
        });
    }

</script>
