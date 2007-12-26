<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<form id="fregions" method="post" action="">

		<tr>
			<td class="th" width="100%" colspan="4" align="center">
				<%@ include file="filters/country_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%"><input type="checkbox"
											 onchange="FP.setCheckboxes(this.checked, 'regionIds')">
			</td>
			<td class="th" width="63%"><s:text name="ab.region_name"/></td>
			<td class="th" width="35%">&nbsp;</td>
		</tr>
		<s:iterator value="%{regionNames}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right"><s:property
						value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>&nbsp;</td>
				<td class="col"><input type="checkbox" value="<s:property value="%{region.id}"/>" name="regionIds"/></td>
				<td class="col"><s:property value="%{getTranslation(translations).name}"/></td>
				<td class="col">
					<a href="<s:url value="/dicts/view_region.action?region.id=%{region.id}"/>"><s:text
							name="common.view"/></a></td>
			</tr>
		</s:iterator>
		<tr class="cols_1">
			<td class="col" width="100%" colspan="4" align="center">
				<%@ include file="filters/pager.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<input type="submit" class="btn-exit"
					   onclick="$('fregions').action='<s:url action="delete_regions"/>';$('fregions').submit()"
					   value="<s:text name="common.delete_selected"/>"/>
				<input type="button" class="btn-exit"
					   onclick="window.location='<s:url action="create_region"/>'"
					   value="<s:text name="common.new"/>"/>
				<input type="submit" class="btn-exit"
					   value="<s:text name="common.refresh"/>"/>
			</td>
		</tr>
	</form>
</table>
