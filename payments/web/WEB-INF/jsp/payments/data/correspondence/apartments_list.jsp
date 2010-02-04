<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="3">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="98%"><s:text name="ab.apartment" /></td>
    </tr>
    <s:iterator value="apartments" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <input type="radio" value="<s:property value="id" />" name="object.id" />
            </td>
            <td class="col">
                <s:property value="number" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <s:hidden name="setupType" value="setupType" />
            <input type="submit" onclick="$('#setupType').val('apartment');" class="btn-exit" value="<s:text name="common.set" />" />
        </td>
    </tr>
</table>
