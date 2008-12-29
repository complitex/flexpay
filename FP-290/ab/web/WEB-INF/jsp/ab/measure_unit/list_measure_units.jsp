<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<s:form id="fobjects" action="measureUnitsList" namespace="/dicts">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">
				<input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');">
			</td>
			<td class="th"><s:text name="common.measure_unit" /></td>
			<td class="th">&nbsp;</td>
		</tr>
		<s:iterator value="units" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s"><s:property value="#rowstatus.index + 1" /></td>
				<td class="col"><s:checkbox name="objectIds" fieldValue="%{id}" /></td>
				<td class="col">
					<s:property value="getTranslation(unitNames).name" />
				</td>
				<td class="col">
					<a href="<s:url action='measureUnitEdit'><s:param name="measureUnit.id" value="%{id}"/></s:url>">
						<s:text name="ab.edit" />
					</a>
				</td>
			</tr>
		</s:iterator>

		<tr>
			<td colspan="4">

				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="measureUnitDelete"/>';"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action='measureUnitEdit'><s:param name="measureUnit.id" value="0" /></s:url>';"
					   value="<s:text name="common.new"/>" />
			</td>
		</tr>
	</table>
</s:form>
