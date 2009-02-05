<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<s:form action="personEditFIO" method="post">

		<s:set name="fio" value="%{FIOIdentity}"/>
		<tr class="cols_1">
			<td class="col_1s" colspan="4"><b><s:text name="ab.person.fio"/></b></td>
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
				<input type="text" name="identity.birthDateStr" id="fio.birthDate"
						value="<s:property value="format(#fio.birthDate)"/>" />
				<img src="<s:url value="/resources/common/js/jscalendar/img.gif" includeParams="none"/>" alt=""
					 id="trigger_fio.birthDate"
					 style="cursor: pointer; border: 1px solid red;"
					 title="<s:text name="common.calendar"/>"
					 onmouseover="this.style.background='red';"
					 onmouseout="this.style.background='';"/>
				<script type="text/javascript">
				Calendar.setup({
					inputField	 : "fio.birthDate",
					ifFormat	 : "%Y/%m/%d",
					button		 : "trigger_fio.birthDate",
					align		 : "Tl"
				});
				</script>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit" name="submitted" value="<s:text name="common.save"/>"/>
			</td>
		</tr>
		<s:hidden name="person.id" value="%{person.id}"/>
	</s:form>
</table>
