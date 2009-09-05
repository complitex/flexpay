<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

    function validateForm() {
        var fileVal = $("input[name=upload]").val();
        if (fileVal == null || fileVal == "") {
            alert("<s:text name="tc.error.rules_file_field_cant_be_null" />");
            return false;
        }
        return true;
    }

    function submitForm() {
        if (validateForm()) {
            $("#inputForm").submit();
        }
    }

</script>

<s:actionerror />
<s:actionmessage />

<s:form id="inputForm" enctype="multipart/form-data">

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td><s:text name="bti.building.attribute.import.date" /></td>
			<td>
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		<tr>
		<tr>
			<td><s:text name="bti.building.attribute.import.file" /></td>
			<td>
				<s:file name="upload" required="true" size="75" accept="*.csv" />
				<s:hidden name="moduleName" value="tc" />
			</td>
		</tr>
		<tr>
			<td colspan="2">
                <s:hidden name="submitted" value="true" />
                <input type="button" class="btn-exit" value="<s:text name="common.import"/>" onclick="submitForm();" />
			</td>
		</tr>
	</table>
</s:form>
