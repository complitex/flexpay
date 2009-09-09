<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />
<s:actionmessage />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected"/>" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="cashboxEdit"><s:param name="cashbox.id" value="0" /></s:url>';"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="eirc.cashbox.name"/></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="cashboxes" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <a href="<s:url action="cashboxView"><s:param name="cashbox.id" value="%{id}" /></s:url>">
                    <s:property value="getTranslation(names).name"/>
                </a>
            </td>
            <td class="col">
                <a href="<s:url action="cashboxEdit"><s:param name="cashbox.id" value="%{id}" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected"/>" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="cashboxEdit"><s:param name="cashbox.id" value="0" /></s:url>';"
                   value="<s:text name="common.new"/>"/>
        </td>
    </tr>
</table>
