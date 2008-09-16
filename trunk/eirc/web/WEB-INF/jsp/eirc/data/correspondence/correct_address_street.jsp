<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionerror/>

<form id="fobjects" method="post"
	  action="<s:url action="registry_record_correct_address_street" includeParams="none"/>"
	  onsubmit="return validateSubmit()">

	<%@include file="../registry_record_info.jsp" %>

	<%@ include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_streetname.jsp" %>
	<table cellpadding="3" cellspacing="1" border="0" width="100%">

		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th" width="98%"><s:text name="ab.street"/></td>
		</tr>
		<s:iterator value="%{objectNames}" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col_1s" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col">
					<input type="radio" value="<s:property value="%{id}"/>" name="object.id"/>
				</td>
				<td class="col">
					<s:property value="%{getTranslation(getCurrentType().translations).name}"/>
					<s:property value="%{getTranslation(getCurrentName().translations).name}"/>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="3">
				<%@ include file="/WEB-INF/jsp/ab/filters/pager.jsp" %>
				<input type="hidden" id="setupType" name="setupType" value="setupType" />
				<s:hidden name="record.id" value="%{record.id}" />
				<input type="submit" onclick="$('setupType').value = 'street'" class="btn-exit"
					   value="<s:text name="common.set"/>" />
			</td>
		</tr>
	</table>
</form>

<script type="text/javascript">
	function validateSubmit() {

		if ($('setupType').value == 'setupType') {
			return true;
		}

		var inputs = Form.getInputs('fobjects', 'radio', 'object.id');
		var radio = inputs.find(function(radio) {
			return radio.checked;
		});

		if (!radio) {
			alert('<s:text name="eirc.need_select_street" />');
			$('setupType').value = 'setupType';
			return false;
		}

		return true;
	}

</script>
