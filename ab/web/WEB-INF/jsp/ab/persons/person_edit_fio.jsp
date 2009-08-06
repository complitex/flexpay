<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<script type="text/javascript">
    FP.calendars("#birthDate", true);
</script>

<s:form action="personEditFIO">

    <s:set name="fio" value="%{FIOIdentity}"/>

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr class="cols_1">
            <td class="col_1s" colspan="4"><strong><s:text name="ab.person.fio"/></strong></td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.last_name"/></td>
            <td class="col_1"><s:textfield name="identity.lastName" value="%{#fio.lastName}"/></td>
            <td class="col_1"><s:text name="ab.person.first_name"/></td>
            <td class="col_1"><s:textfield name="identity.firstName" value="%{#fio.firstName}"/></td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.middle_name"/></td>
            <td class="col_1"><s:textfield name="identity.middleName" value="%{#fio.middleName}"/></td>
            <td class="col_1"><s:text name="ab.person.sex"/></td>
            <td class="col_1">
                <input type="radio" name="identity.sex"
                       <s:if test="%{#fio.isMan()}">checked="checked"</s:if>
                       value="<s:property value="@org.flexpay.ab.persistence.PersonIdentity@SEX_MAN" />"/>
                &nbsp;
                <s:text name="ab.person.sex.man.short"/>
                <br/>
                <input type="radio" name="identity.sex"
                       <s:if test="%{#fio.isWoman()}">checked="checked"</s:if>
                       value="<s:property value="@org.flexpay.ab.persistence.PersonIdentity@SEX_WOMAN" />"/>
                &nbsp;
                <s:text name="ab.person.sex.woman.short"/>
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.birth_date"/></td>
            <td class="col_1" colspan="3">
                <input type="text" name="identity.birthDateStr" id="birthDate" value="<s:property value="format(#fio.birthDate)"/>" readonly="readonly" />
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save"/>"/>
            </td>
        </tr>
    </table>
    <s:hidden name="person.id" value="%{person.id}"/>

</s:form>
