<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
            <input type="button" value="<s:text name="common.delete_selected" />" class="btn-exit" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="quittancePacketEdit" includeParams="none"><s:param name="packet.id" value="0"/></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="eirc.quittance.packet.packet_number" /></td>
        <td class="th"><s:text name="eirc.quittance.packet.creation_date" /></td>
        <td class="th"><s:text name="eirc.quittance.packet.begin_date" /></td>
        <td class="th"><s:text name="eirc.quittance.packet.close_date" /></td>
        <td class="th"><s:text name="eirc.quittance.packet.control_overall_sum" /></td>
        <td class="th"><s:text name="eirc.quittance.packet.overall_sum" /></td>
        <td class="th">&nbsp;</td>
    </tr>
    <s:iterator value="packets" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <a href="<s:url action="quittancePacketView" includeParams="none"><s:param name="packet.id" value="id" /></s:url>">
                    <s:property value="packetNumber" />
                </a>
            </td>
            <td class="col"><s:date format="yyyy/MM/dd" name="creationDate" /></td>
            <td class="col"><s:property value="%{format(beginDate)}" /></td>
            <td class="col"><s:property value="%{format(closeDate)}" /></td>
            <td class="col"><s:property value="controlOverallSumm" /></td>
            <td class="col"><s:property value="overallSumm" /></td>
            <td class="col">
                <a href="<s:url action="quittancePacketEdit" includeParams="none"><s:param name="packet.id" value="id" /></s:url>">
                    <s:text name="common.edit" />
                </a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="9">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp" %>
            <input type="button" value="<s:text name="common.delete_selected" />" class="btn-exit" onclick="deleteAjax();" />
            <input type="button" class="btn-exit"
                   onclick="window.location='<s:url action="quittancePacketEdit" includeParams="none"><s:param name="packet.id" value="0"/></s:url>';"
                   value="<s:text name="common.new" />" />
        </td>
    </tr>
</table>
