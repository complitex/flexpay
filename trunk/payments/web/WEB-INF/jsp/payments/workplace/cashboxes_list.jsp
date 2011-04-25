<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th"><s:text name="orgs.cashbox.name" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="cashboxes" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <a href="<s:url action="cashboxView"><s:param name="cashbox.id" value="id" /></s:url>">
                    <s:property value="getTranslationName(names)" />
                </a>
            </td>
            <td class="col">
                <a href="<s:url action="workplaceSetCashboxId"><s:param name="cashboxId" value="id" /></s:url>">
                    <s:text name="payments.cashbox.set" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
</table>
