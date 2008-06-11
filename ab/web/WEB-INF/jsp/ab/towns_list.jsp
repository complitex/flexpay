<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fobjects" method="post" action="<s:url value="/dicts/list_towns.action" includeParams="none" />">

		<tr>
			<td colspan="4">
				<%@ include file="filters/groups/country_region.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds')"></td>
			<td class="th" width="63%"><s:text name="ab.town"/></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<s:iterator value="%{objectNames}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>&nbsp;</td>
				<td class="col"><input type="checkbox" value="<s:property value="%{object.id}"/>" name="objectIds"/></td>
				<td class="col">
					<a href="<s:url value="/dicts/list_streets.action?countryFilter.selectedId=%{countryFilter.selectedId}&regionFilter.selectedId=%{regionFilter.selectedId}&townFilter.selectedId=%{object.id}"/>">
						<s:property value="%{getTranslation(translations).name}"/>
					</a>
				</td>
				<td class="col">
					<a href="<s:url value="/dicts/view_town.action?object.id=%{object.id}"/>">
						<!-- <img src="<s:url value="/resources/common/img/i_view.gif" />" alt="<s:text name="common.view"/>"
						 title="<s:text name="common.view"/>" /> -->
						<s:text name="common.view"/> 
					</a></td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="4">
				<%@ include file="filters/pager.jsp" %>
				<input type="submit" class="btn-exit"
					   onclick="$('fobjects').action='<s:url action="delete_towns"/>';$('fobjects').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="create_town"/>'"
					   value="<s:text name="common.new"/>"/>
			</td>
		</tr>
	</form>
</table>
