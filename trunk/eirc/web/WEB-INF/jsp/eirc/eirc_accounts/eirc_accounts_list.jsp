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
                <a href="<s:url action="eircAccountView" includeParams="none"><s:param name="eircAccount.id" value="%{id}" /></s:url>">
                    <s:property value="accountNumber" />
                </a>
            </td>
            <td class="col">
                <a href="<s:url action="apartmentRegistration" namespace="/dicts" includeParams="none"><s:param name="apartment.id" value="%{apartment.id}" /></s:url>">
                    <s:property value="getAddress(apartment)" />
                </a>
            </td>
            <td class="col">
                <s:if test="person != null">
                    <a href="<s:url action="personView" namespace="/dicts" includeParams="none"><s:param name="person.id" value="%{person.id}" /></s:url>">
                        <s:property value="getFIO(person)" />
                    </a>
                </s:if><s:else>
                    (*)&nbsp;<s:property value="consumerInfo.FIO" />
                </s:else>
            </td>
            <td class="col">
                <a href="<s:url action="eircAccountEditConsumerAttributes" includeParams="none"><s:param name="eircAccount.id" value="%{id}" /></s:url>"><s:text name="common.edit" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
