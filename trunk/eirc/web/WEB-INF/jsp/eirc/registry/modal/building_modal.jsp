<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<input id="selectedGroupIndex" type="hidden" />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr valign="top" class="cols_1">
        <td class="col" colspan="2"><s:text name="ab.building" /></td>
    </tr>
    <tr valign="top" class="cols_1">
        <td class="col"><s:text name="ab.district.name" />:</td>
        <td class="col">
            <%@include file="/WEB-INF/jsp/ab/filters/district_filter.jsp"%>
        </td>
    </tr>
    <tr valign="middle" class="cols_1">
        <td id="address" class="col" colspan="2"></td>
    </tr>
    <tr valign="middle">
        <td colspan="2">
            <input type="button" onclick="createItem($('#selectedGroupIndex').val());" class="btn-exit" value="<s:text name="common.add" />" />
        </td>
    </tr>
</table>
