<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
<%@include file="/WEB-INF/jsp/payments/data/registry_record_info.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
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
                    "record.id":<s:property value="record.id" />,
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
