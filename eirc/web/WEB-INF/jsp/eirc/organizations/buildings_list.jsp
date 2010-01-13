<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="eirc.add_served_buildings" />" onclick="addBuildings();" />
            <input type="button" class="btn-exit" value="<s:text name="common.back" />" onclick="backF();" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="ab.building" /></td>
    </tr>
    <s:iterator value="buildings" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property	value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <s:property value="addresses[#status.index]" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="4">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" class="btn-exit" value="<s:text name="eirc.add_served_buildings" />" onclick="addBuildings();" />
            <input type="button" class="btn-exit" value="<s:text name="common.back" />" onclick="backF();" />
        </td>
    </tr>
</table>
