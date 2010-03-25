<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form id="fobjects" action="eircAccountCreateForm2" method="post">

    <s:hidden name="apartmentFilter" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="6">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            </td>
        </tr>
		<tr>
			<td colspan="6">
				<s:textfield name="personSearchString" />
                <s:submit cssClass="btn-exit" value="%{getText('common.search')}" />
			</td>
		</tr>
		<tr>
			<td>
				&nbsp;
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="20%"><s:text name="ab.person.last_name" /></td>
			<td class="th" width="20%"><s:text name="ab.person.first_name" /></td>
			<td class="th" width="20%"><s:text name="ab.person.middle_name" /></td>
			<td class="th" width="20%"><s:text name="ab.person.birth_date" /></td>
		</tr>
		<s:iterator value="persons" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
                    <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />&nbsp;
                </td>
				<td class="col">
                    <input type="radio" name="personId" value="<s:property value="id" />" />
                </td>
				<td class="col"><s:property value="defaultIdentity.lastName" /></td>
				<td class="col"><s:property value="defaultIdentity.firstName" /></td>
				<td class="col"><s:property value="defaultIdentity.middleName" /></td>
				<td class="col"><s:property value="format(defaultIdentity.birthDate)" /></td>

			</tr>
		</s:iterator>
		<tr>
			<td colspan="6">
                <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
				<input type="submit" class="btn-exit"
					   onclick="$('#fobjects').attr('action', '<s:url action="eircAccountCreate" includeParams="none" />');"
					   value="<s:text name="common.create" />" />
			</td>
		</tr>
    </table>

</s:form>
