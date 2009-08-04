<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
        FF.updatePager("pagerAjax(this);");
    });
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" disabled="1" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
        </td>
        <td class="th" width="14%"><s:text name="eirc.eirc_account"/></td>
        <td class="th" width="42%"><s:text name="eirc.eirc_account.person"/></td>
        <td class="th" width="42%"><s:text name="eirc.eirc_account.apartment"/></td>
    </tr>
    <s:iterator value="%{accounts}" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" align="right">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
            </td>
            <td class="col">
                <input type="checkbox" disabled="1" value="<s:property value="%{id}"/>" name="objectIds"/>
            </td>
            <td class="col">
                <a href="<s:url action="eircAccountView"><s:param name="eircAccount.id" value="%{id}"/></s:url>">
                    <s:property value="%{accountNumber}"/>
                </a>
            </td>
            <td class="col">
                <s:if test="person != null">
                    <a href="<s:url action="personView" namespace="/dicts"><s:param name="person.id" value="%{person.id}"/></s:url>">
                        <s:property value="%{getFIO(person)}"/>
                    </a>
                </s:if><s:else>
                    (*)&nbsp;<s:property value="%{consumerInfo.FIO}"/>
                </s:else>
            </td>
            <td class="col">
                <a href="<s:url action="apartmentRegistration" namespace="/dicts"><s:param name="apartment.id" value="%{apartment.id}"/></s:url>">
                    <s:property value="%{getAddress(apartment)}"/>
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="5">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="eircAccountCreateForm1" includeParams="none" />';"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
</table>