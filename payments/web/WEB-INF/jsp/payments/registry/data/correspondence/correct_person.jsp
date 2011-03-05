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
    <tr>
        <td>
            <input id="setBut" type="button" class="btn-exit" onclick="set();" value="<s:text name="common.set" />" style="display:none;" />
        </td>
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

    function showBut() {
        $("#setBut").show();
    }

    function set() {
        var id = $("input[type='radio'][name='objectIds']:checked").val();
        $.post("<s:url action="setCorrection" includeParams="none" />", {
                    <s:if test="group != null">
                        <s:if test="group.townName != null">"group.townName":"<s:property value="group.townName" />",</s:if>
                        <s:if test="group.streetType != null">"group.streetType":"<s:property value="group.streetType" />",</s:if>
                        <s:if test="group.streetName != null">"group.streetName":"<s:property value="group.streetName" />",</s:if>
                        <s:if test="group.buildingNumber != null">"group.buildingNumber":"<s:property value="group.buildingNumber" />",</s:if>
                        <s:if test="group.buildingBulk != null">"group.buildingBulk":"<s:property value="group.buildingBulk" />",</s:if>
                        <s:if test="group.apartmentNumber != null">"group.apartmentNumber":"<s:property value="group.apartmentNumber" />",</s:if>
                        <s:if test="group.lastName != null">"group.lastName":"<s:property value="group.lastName" />",</s:if>
                        <s:if test="group.middleName != null">"group.middleName":"<s:property value="group.middleName" />",</s:if>
                        <s:if test="group.firstName != null">"group.firstName":"<s:property value="group.firstName" />",</s:if>
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
