<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<script type="text/javascript">
    FP.switchSorter(["eircAccountSorterByAccountNumberButton", "eircAccountSorterByAddressButton"]);
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="<s:property value="eircAccountSorterByAccountNumber.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="/WEB-INF/jsp/eirc/sorters/eirc_account_sort_by_account_number_header.jsp"%>
        </td>
        <td class="<s:property value="eircAccountSorterByAddress.activated ? 'th_s' : 'th'" />" nowrap>
            <%@include file="/WEB-INF/jsp/eirc/sorters/eirc_account_sort_by_address_header.jsp"%>
        </td>
        <td class="th" width="42%"><s:text name="eirc.eirc_account.person" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="accounts" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <a href="javascript:void(0);" onclick="f('<s:url action="eircAccountView" includeParams="none" />', {'eircAccount.id':<s:property value="id" />});">
                    <s:property value="accountNumber" />
                </a>
            </td>
            <td class="col">
                <s:property value="getAddress(apartment)" />
            </td>
            <td class="col">
                <s:property value="consumerInfo.FIO" />
            </td>
            <td class="col">
                <a href="javascript:void(0);" onclick="f('<s:url action="eircAccountEditConsumerAttributes" includeParams="none" />', {'eircAccount.id':<s:property value="id" />});">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
