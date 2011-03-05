<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
<s:if test="groupView == null">
    <%@include file="/WEB-INF/jsp/payments/registry/data/registry_record_info.jsp"%>
</s:if><s:else>
    <%@include file="/WEB-INF/jsp/payments/registry/data/registry_records_group_info.jsp"%>
</s:else>

<table cellpadding="3" cellspacing="1" border="0" width="97%">
    <tr>
        <td>
            <table width="100%">
                <tr>
                    <td class="filter"><s:text name="ab.person.fio" /></td>
                    <td colspan="5">
                        <input type="text" name="personSearchFilter.searchString" class="form-textfield"
                               value="<s:property value="personSearchFilter.searchString" />" />
                        <input type="button" onclick="pagerAjax();" value="<s:text name="common.search" />" />
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td id="result"></td>
    </tr>
</table>

<script type="text/javascript">

    $(function() {
        pagerAjax();
    });

    function pagerAjax(element) {
        FP.pagerAjax(element, {
            action:"<s:url action="personsListCorrections" includeParams="none" />",
            params:{
                "personSearchFilter.searchString": $("input[name='personSearchFilter.searchString']").get(0).value
            }
        });
    }

    function set(id) {
        $.post("<s:url action="setCorrection" includeParams="none" />", {
                    <s:if test="group != null">
                        <s:property value="group.townName != null ? '\"group.townName\":\"' + group.townName + '\",' : ''" escape="false" />
                        <s:property value="group.streetType != null ? '\"group.streetType\":\"' + group.streetType + '\",' : ''" escape="false" />
                        <s:property value="group.streetName != null ? '\"group.streetName\":\"' + group.streetName + '\",' : ''" escape="false" />
                        <s:property value="group.buildingNumber != null ? '\"group.buildingNumber\":\"' + group.buildingNumber + '\",' : ''" escape="false" />
                        <s:property value="group.buildingBulk != null ? '\"group.buildingBulk\":\"' + group.buildingBulk + '\",' : ''" escape="false" />
                        <s:property value="group.apartmentNumber != null ? '\"group.apartmentNumber\":\"' + group.apartmentNumber + '\",' : ''" escape="false" />
                        <s:property value="group.lastName != null ? '\"group.lastName\":\"' + group.lastName + '\",' : ''" escape="false" />
                        <s:property value="group.middleName != null ? '\"group.middleName\":\"' + group.middleName + '\",' : ''" escape="false" />
                        <s:property value="group.middleName != null ? '\"group.lastName\":\"' + group.middleName + '\",' : ''" escape="false" />
                        "group.errorType":<s:property value="group.errorType" />,
                        "registry.id":<s:property value="registry.id" />,
                    </s:if><s:else>
                        "record.id":<s:property value="record.id" />,
                    </s:else>
                    "object.id":id,
                    type:"person"
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
