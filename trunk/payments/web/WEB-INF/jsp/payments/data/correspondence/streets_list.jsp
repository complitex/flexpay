<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">
    $(function() {
        FF.updatePager("pagerAjax(this);");
    });
</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">

    <tr>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="1%">&nbsp;</td>
        <td class="th" width="98%"><s:text name="ab.street" /></td>
    </tr>
    <s:iterator value="%{streets}" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col_1s" align="right">
                <s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
            </td>
            <td class="col">
                <input type="radio" value="<s:property value="%{id}"/>" name="object.id" />
            </td>
            <td class="col">
                <s:property value="%{getTranslation(getCurrentType().translations).name}" />
                <s:property value="%{getTranslation(getCurrentName().translations).name}" />
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="3">
            <%@ include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
            <input type="hidden" id="setupType" name="setupType" value="setupType" />
            <input type="submit" onclick="$('#setupType').val('street');" class="btn-exit" value="<s:text name="common.set"/>" />
        </td>
    </tr>
</table>
