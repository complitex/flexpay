<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
<s:if test="groupView == null">
    <%@include file="/WEB-INF/jsp/payments/registry/data/registry_record_info.jsp"%>
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/payments/registry/data/registry_records_group_info.jsp"%>
</s:else>
<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_ajax.jsp"%>

<input id="setBut" type="button" class="btn-exit" onclick="set();" value="<s:text name="common.set" />" style="display:none;" />

<script type="text/javascript">

    $(function() {
        FF.addListener("street", function() {
            $("#setBut").show();
        });
        FF.addEraser("street", function() {
            $("#setBut").hide();
        });
    });

    function set() {
        $.post("<s:url action="setCorrection" />", {
                    <s:if test="group != null">
                        <s:if test="group.townName != null">"group.townName":"<s:property escapeHtml="false" value="group.townName" />",</s:if>
                        <s:if test="group.streetType != null">"group.streetType":"<s:property escapeHtml="false" value="group.streetType" />",</s:if>
                        <s:if test="group.streetName != null">"group.streetName":"<s:property escapeHtml="false" value="group.streetName" />",</s:if>
                        <s:if test="group.buildingNumber != null">"group.buildingNumber":"<s:property escapeHtml="false" value="group.buildingNumber" />",</s:if>
                        <s:if test="group.buildingBulk != null">"group.buildingBulk":"<s:property escapeHtml="false" value="group.buildingBulk" />",</s:if>
                        <s:if test="group.apartmentNumber != null">"group.apartmentNumber":"<s:property escapeHtml="false" value="group.apartmentNumber" />",</s:if>
                        <s:if test="group.lastName != null">"group.lastName":"<s:property escapeHtml="false" value="group.lastName" />",</s:if>
                        <s:if test="group.middleName != null">"group.middleName":"<s:property escapeHtml="false" value="group.middleName" />",</s:if>
                        <s:if test="group.firstName != null">"group.firstName":"<s:property escapeHtml="false" value="group.firstName" />",</s:if>
                        "group.errorType":<s:property escapeHtml="false" value="group.errorType" />,
                        "registry.id":<s:property value="registry.id" />,
                    </s:if><s:else>
                        "record.id":<s:property value="record.id" />,
                    </s:else>
                    "object.id":FF.filters["street"].value.val(),
                    type:"street"
                }, function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#messagesBlock").html(data);
                    if ($("#errors").text().length > 0) {
                        $("#setBut").hide();
                    } else {
                        parent.$("#messagesBlock").html(data);
                        parent.$.modal.close();
                    }
                });
    }

</script>
