<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr class="cols_1">
		<td class="col">
            <strong><s:text name="eirc.eirc_account" />:</strong> <s:property value="eircAccount.accountNumber" />
        </td>
		<td class="col">
            <strong><s:text name="eirc.eirc_account.person" />:</strong> <s:property value="eircAccount.consumerInfo.FIO" />
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

<s:form action="eircAccountAddConsumer" method="POST">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="payments.service" />:</td>
            <td class="col"><%@include file="/WEB-INF/jsp/payments/filters/service_filter.jsp"%></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.eirc_account.erc_account" />:</td>
            <td class="col"><s:textfield name="ercAccount" /></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.eirc_account.external_account" />:</td>
            <td class="col"><s:textfield name="externalAccount" /></td>
        </tr>
        <tr valign="top" class="cols_1">
            <td class="col"><s:text name="eirc.eirc_account.consumer_fio" />:</td>
            <td class="col"><s:textfield name="consumerFio" /></td>
        </tr>
        <tr>
            <td colspan="15">
                <input type="submit" name="submitted" class="btn-exit" value="<s:text name="common.save" />" />
                <input type="button" class="btn-exit" value="<s:text name="common.back" />" onclick="backf();" />
            </td>
        </tr>
    </table>

    <s:hidden name="output" value="%{output}" />
    <s:hidden name="apartmentFilter" value="%{apartmentFilter != null ? apartmentFilter : 0}" />
    <s:hidden name="buildingFilter" value="%{buildingFilter != null ? buildingFilter : 0}" />
    <s:hidden name="streetFilter" value="%{streetFilter != null ? streetFilter : 0}" />
    <s:hidden name="townFilter" value="%{townFilter != null ? townFilter : 0}" />
    <s:hidden name="regionFilter" value="%{regionFilter != null ? regionFilter : 0}" />
    <s:hidden name="countryFilter" value="%{countryFilter != null ? countryFilter : 0}" />
    <s:hidden name="eircAccountSorterByAccountNumber.active" value="%{eircAccountSorterByAccountNumber != null ? eircAccountSorterByAccountNumber.active : 0}" />
    <s:hidden name="eircAccountSorterByAccountNumber.order" value="%{eircAccountSorterByAccountNumber.order}" />
    <s:hidden name="eircAccountSorterByAddress.active" value="%{eircAccountSorterByAddress != null ? eircAccountSorterByAddress.active : 0}" />
    <s:hidden name="eircAccountSorterByAddress.order" value="%{eircAccountSorterByAddress.order}" />
    <s:hidden name="pager.pageNumber" value="%{pager.pageNumber}" />
    <s:hidden name="pager.pageSize" value="%{pager.pageSize}" />
    <s:hidden name="returnTo" value="%{returnTo}" />
    <s:hidden name="eircAccount.id" value="%{eircAccount.id}" />

</s:form>

<script type="text/javascript">

    function backf() {

        FP.post("<s:url action="eircAccountView" />", {
            <s:if test="output != null">output: <s:property value="output" />,</s:if>
            <s:if test="apartmentFilter != null">apartmentFilter: <s:property value="apartmentFilter" />,</s:if>
            <s:if test="buildingFilter != null">buildingFilter: <s:property value="buildingFilter" />,</s:if>
            <s:if test="streetFilter != null">streetFilter: <s:property value="streetFilter" />,</s:if>
            <s:if test="townFilter != null">townFilter: <s:property value="townFilter" />,</s:if>
            <s:if test="regionFilter != null">regionFilter: <s:property value="regionFilter" />,</s:if>
            <s:if test="countryFilter != null">countryFilter: <s:property value="countryFilter" />,</s:if>
            "personSearchFilter.searchString": "<s:property value="personSearchFilter.searchString" />",
            "eircAccountSorterByAccountNumber.active": "<s:property value="eircAccountSorterByAccountNumber.active" />",
            "eircAccountSorterByAccountNumber.order": "<s:property value="eircAccountSorterByAccountNumber.order" />",
            "eircAccountSorterByAddress.active": "<s:property value="eircAccountSorterByAddress.active" />",
            "eircAccountSorterByAddress.order": "<s:property value="eircAccountSorterByAddress.order" />",
            "pager.pageNumber": "<s:property value="pager.pageNumber" />",
            "pager.pageSize": "<s:property value="pager.pageSize" />",
            "eircAccount.id": "<s:property value="eircAccount.id" />",
        });
    }

</script>
