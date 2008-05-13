<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror />

<form id="fobjects" method="post" action="<s:url action="registry_record_correct_address" includeParams="none"/>"
	  onsubmit="return validateSubmit()">

	<%@include file="../registry_record_info.jsp" %>

	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td class="text" width="100%" colspan="4" align="center">
				<%@ include file="/WEB-INF/jsp/ab/filters/country_filter.jsp" %>
				<%@ include file="/WEB-INF/jsp/ab/filters/region_filter.jsp" %>
				<%@ include file="/WEB-INF/jsp/ab/filters/town_filter.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="text" width="100%" colspan="4" align="center">
				<%@ include file="/WEB-INF/jsp/ab/filters/district_filter.jsp" %>
				<%@ include file="/WEB-INF/jsp/ab/filters/street_filter.jsp" %>
				<%@ include file="/WEB-INF/jsp/ab/filters/buildings_filter.jsp" %>
			</td>
		</tr>

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="98%"><s:text name="ab.apartment" /></td>
		</tr>
		<s:iterator value="%{apartments}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}" />
				</td>
				<td class="col">
					<input type="radio" value="<s:property value="%{id}"/>" name="object.id" />
				</td>
				<td class="col">
					<s:property value="%{number}" />
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="3">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
			</td>
		</tr>
		<tr>
			<td colspan="3">
				<input type="hidden" id="setupType" name="setupType" value="refresh" />
				<s:hidden name="record.id" value="%{record.id}" />
				<input type="submit" onclick="$('setupType').value = 'refresh'" class="btn-exit"
					   value="<s:text name="common.refresh"/>" />
				<input type="submit" onclick="$('setupType').value = 'create-apartment'" class="btn-exit"
					   value="<s:text name="eirc.create_apartment"/>" />
				<input type="submit" onclick="$('setupType').value = 'create-building'" class="btn-exit"
					   value="<s:text name="eirc.create_building"/>" />
				<input type="submit" onclick="$('setupType').value = 'create-street'" class="btn-exit"
					   value="<s:text name="eirc.create_street"/>" />
				<input type="submit" onclick="$('setupType').value = 'apartment'" class="btn-exit"
					   value="<s:text name="common.set"/>" />
				<input type="submit" onclick="$('setupType').value = 'building'" class="btn-exit"
					   value="<s:text name="eirc.setup_with_building"/>" />
				<input type="submit" onclick="$('setupType').value = 'street'" class="btn-exit"
					   value="<s:text name="eirc.setup_with_street"/>" />
			</td>
		</tr>
	</table>
</form>

<script type="text/javascript">
	function validateSubmit() {

		var type = $('setupType').value;
		if (type == 'refresh') {
			return true;
		} else if (type == 'create-apartment' || type == 'create-building' || type == 'create-street' ) {
			return true;
		}

		var inputs = Form.getInputs('fobjects', 'radio', 'object.id');
		var radio = inputs.find(function(radio) {
			return radio.checked;
		});

		if (!radio) {
			alert('<s:text name="eirc.need_select_apartment" />');
			return false;
		}

		return true;
	}

</script>
