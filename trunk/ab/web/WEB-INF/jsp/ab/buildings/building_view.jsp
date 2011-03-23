<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td id="result"></td>
    </tr>
	<tr>
		<td colspan="2">
            <input type="button" class="btn-exit" value="<s:text name="common.delete_selected" />" onclick="deleteAjax();" />
			<input type="button" class="btn-exit"
				   onclick="window.location='<s:url action="buildingAddressEdit"><s:param name="building.id" value="building.id" /><s:param name="address.id" value="0" /></s:url>';"
				   value="<s:text name="common.new" />" />
		</td>
	</tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="buildingAddressesListAjax" namespace="/dicts" />",
            params: {
                "building.id":<s:property value="building.id" />
            }
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="buildingAddressDelete" namespace="/dicts" />",
                "objectIds", pagerAjax, {params:{"building.id":<s:property value="building.id" />}});
    }

    function setPrimaryStatus(id) {
        $.post("<s:url action="buildingAddressSetPrimaryStatus" namespace="/dicts" />",
                {
                    "address.id":id
                },
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#messagesBlock").html(data);
                    pagerAjax();
                });
    }

</script>
