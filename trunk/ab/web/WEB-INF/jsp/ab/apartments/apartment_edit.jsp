<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="apartmentEdit" method="POST">

	<s:hidden name="apartment.id" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="2">
                <s:set name="readonly" value="%{apartment.id > 0}" />
				<%@include file="../filters/groups/country_region_town_street_building_ajax.jsp"%>
			</td>
		</tr>
		<tr>
			<td class="col"><s:text name="ab.apartment.number" />:</td>
			<td class="col">
				<s:textfield name="apartmentNumber" value="%{apartmentNumber}" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
                <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
			</td>
		</tr>
	</table>
</s:form>
