<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr><td class="th"><s:text name="ab.select_correction_type" /></td></tr>

	<tr class="cols_1"><td class="col">
	<input type="radio" id="address" name="type" onclick="window.location='<s:url action="registry_record_correct_address" />'" /> <s:text name="ab.address" />
	</td></tr>
	<tr class="cols_1"><td class="col">
	<input type="radio" id="person" name="type" onclick="alert('Not implemented')" /> <s:text name="ab.person" />
</table>
