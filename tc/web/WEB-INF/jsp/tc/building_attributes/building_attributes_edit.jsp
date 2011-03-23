<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">
    FP.calendars("attributeDate", true);

    function showAttributesBlock() {
        FP.showShadow("shadow", "attributesBlock");
        $.post("<s:url action="buildingAttributesEditBlock" />",
                {
                    attributeDate:$("#attributeDate").val(),
                    "building.id":<s:property value="building.id" />
                },
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#attributesBlock").html(data);
                    FP.hideShadow("shadow");
                });
    }

</script>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <s:iterator value="alternateAddresses">
        <tr valign="middle" class="cols_1">
            <td class="col" colspan="2">
                <s:property value="getAddress(id)" /><s:if test="primaryStatus"> (<s:text name="tc.edit_building_attributes.primary_status" />)</s:if>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td class="th" colspan="2">
            <s:property value="getAddress(building.id)" /><s:if test="building.primaryStatus"> (<s:text name="tc.edit_building_attributes.primary_status" />)</s:if>
        </td>
    </tr>
    <tr>
        <td>
            <s:text name="tc.edit_building_attributes.date" />
            <s:textfield id="attributeDate" name="attributeDate" readonly="true" />
            <input type="button" value="<s:text name="tc.show_attribute_values" />" class="btn-exit" onclick="showAttributesBlock();" />
        </td>
    </tr>
</table>
<div id="attributesBlock"></div>