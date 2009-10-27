<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:form action="personEdit" method="POST">

    <s:hidden name="person.id" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">

        <s:iterator value="identities" status="status">
            <s:set name="pi" value="%{value}" />
            <tr class="cols_1">
                <td class="col_1s"><s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
                    &nbsp;<s:text name="ab.person.identity_type" /></td>
                <td class="col_1"><s:property value="getTranslationName(pi.identityType.translations)" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.last_name" /></td>
                <td class="col"><s:textfield name="identities[%{key}].lastName" value="%{pi.lastName}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.first_name" /></td>
                <td class="col"><s:textfield name="identities[%{key}].firstName" value="%{pi.firstName}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.middle_name" /></td>
                <td class="col"><s:textfield name="identities[%{key}].middleName" value="%{pi.middleName}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.birth_date" /></td>
                <td class="col"><s:textfield name="identities[%{key}].birthDate" value="%{format(pi.bidthDate)}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.organization" /></td>
                <td class="col"><s:textfield name="identities[%{key}].organization" value="%{pi.organization}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.begin_date" /></td>
                <td class="col"><s:textfield name="identities[%{key}].beginDate" value="%{format(pi.beginDate)}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.end_date" /></td>
                <td class="col"><s:textfield name="identities[%{key}].endDate" value="%{format(pi.endDate)}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.serial_number" /></td>
                <td class="col"><s:textfield name="identities[%{key}].serialNumber" value="%{pi.serialNumber}" /></td>
            </tr>
            <tr class="cols_1">
                <td class="col_1s"><s:text name="ab.person.document_number" /></td>
                <td class="col"><s:textfield name="identities[%{key}].documentNumber" value="%{pi.documentNumber}" /></td>
            </tr>
            <tr>
                <td colspan="2" height="3" bgcolor="#4a4f4f"></td>
            </tr>
        </s:iterator>
        <tr>
            <td colspan="3" height="3" bgcolor="#4a4f4f"></td>
        </tr>
        <tr>
            <td colspan="3">
                <s:submit name="submitted" cssClass="btn-exit" value="%{getText('common.save')}" />
            </td>
        </tr>
    </table>

</s:form>
