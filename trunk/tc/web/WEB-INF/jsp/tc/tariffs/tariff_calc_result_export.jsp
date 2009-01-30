<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:actionerror />

<s:form action="calcResultExport" method="POST">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr valign="top" class="cols_1">
            <td class="col">
                <s:text name="tc.calc_result.calc_date"/>:
            </td>
            <td class="col">
                <s:select name="date" list="allDates" required="true" />
            </td>
        </tr>
        <tr valign="middle">
            <td colspan="2">
                <input type="submit" id="submitted" name="submitted" value="<s:text name="tc.calc_result.export" />" class="btn-exit" />
            </td>
        </tr>
    </table>

</s:form>
