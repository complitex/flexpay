<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="streetDistrictEdit" method="POST">

    <s:hidden name="street.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td class="th" width="1%">&nbsp;</td>
            <td class="th" width="99%">
                <s:text name="ab.street" />:&nbsp;
                <s:property value="getTranslation(street.currentType.translations).shortName" />&nbsp;
                <s:property value="getTranslationName(street.currentName.translations)" />
            </td>
        </tr>
        <tr>
            <td class="th">&nbsp;</td>
            <td class="th"><s:text name="ab.district" /></td>
        </tr>
        <s:iterator value="districtNames" status="status">
            <tr class="cols_1">
                <td class="col_1s">
                    <input type="checkbox" name="objectIds" value="<s:property value="object.id" />"<s:if test="doesDistrictContainsStreet(object.id)"> checked</s:if> />
                </td>
                <td class="col"><s:property value="getTranslationName(translations)" /></td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="2">
                <s:submit name="submitted" cssClass="btn-exit" value="%{getText('common.save')}" />
            </td>
        </tr>
    </table>

</s:form>
