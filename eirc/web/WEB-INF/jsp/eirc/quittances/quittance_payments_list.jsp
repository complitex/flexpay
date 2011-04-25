<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="common.sum" /></td>
        <td class="th"><s:text name="orgs.address" /></td>
        <td class="th"><s:text name="ab.person.fio" /></td>
        <td class="th"><s:text name="eirc.eirc_account" /></td>
        <td class="th"><s:text name="common.month" /></td>
        <td class="th"><s:text name="common.status" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="payments" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col"><s:property value="amount" /></td>
            <td class="col"><s:property value="%{getAddress(quittance)}" /></td>
            <td class="col"><s:property value="%{getFIO(quittance)}" /></td>
            <td class="col"><s:property value="quittance.eircAccount.accountNumber" /></td>
            <td class="col"><s:date format="yyyy/MM" name="quittance.dateTill" /></td>
            <td class="col"><s:text name="%{paymentStatus.i18nName}" /></td>
            <td class="col">
                <a href="<s:url action="quittancePacketEdit"><s:param name="packet.id" value="id" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
