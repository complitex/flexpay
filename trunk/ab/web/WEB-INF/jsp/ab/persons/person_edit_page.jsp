<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_datepicker.jsp" %>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<span id="response"></span>

<span id="editPerson">
    <s:hidden id="" name="person.id" />
</span>

<script type="text/javascript">

    <s:if test="person.notNew">
        $("#registration").ready(function() {
            $.post("<s:url action="personEditRegistrationForm" includeParams="none" />",
                    {
                        apartmentFilter:"<s:property value="person.currentRegistration.apartment.id" />",
                        beginDate:"<s:property value="format(person.currentRegistration.beginDate)" />",
                        endDate:"<s:property value="format(person.currentRegistration.endDate)" />"
                    },
                    function(data, status) {
                        if (data == "" && status == "success") {
                            window.location.href = FP.base;
                        }
                        $("#registration").html(data);
                    });
        });
    </s:if>

    var personId = $("#editPerson input[name='person.id']").get(0);

    function submitFio() {
        var params = {"person.id":personId.value};
        $("#editFio input[type='text'], #editFio input:checked").each(function() {
            params[this.name] = this.value;
        });
        $.post("<s:url action="personSaveFIO" includeParams="none" />", params, function(data, status) {
            if (data == "" && status == "success") {
                window.location.href = FP.base;
            }
            $("#response").html(data);
            if ($("#errors").html() == "") {
                personId.value = $("#responseForm input[name='person.id']").get(0).value;
                if ($("#formRegistration").get(0) == null) {
                    $.post("<s:url action="personEditRegistrationForm" includeParams="none" />", {}, function(data1, status1) {
                        if (data1 == "" && status1 == "success") {
                            window.location.href = FP.base;
                        }
                                $("#registration").html(data1);
                            });
                }
                $.post("<s:url action="personViewIdentities" includeParams="none" />",
                        {"person.id":personId.value},
                        function(data1, status1) {
                            if (data1 == "" && status1 == "success") {
                                window.location.href = FP.base;
                            }
                            $("#identities").html(data1);
                        });
            }
        });
    }

    function submitRegistration() {
        $.post("<s:url action="personSaveRegistration" includeParams="none" />",
                {
                    apartmentFilter:$("#apartment_selected").val(),
                    "person.id":personId.value,
                    beginDate:$("#beginDate").val(),
                    endDate:$("#endDate").val()
                },
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#response").html(data);
                });
    }

    FP.calendars("birthDate", true);
</script>

<form id="editFio">

    <s:set name="fio" value="%{FIOIdentity}" />

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr class="cols_1">
            <td class="col_1s" colspan="4"><strong><s:text name="ab.person.fio" /></strong></td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.last_name" /></td>
            <td class="col_1"><s:textfield id="" name="identity.lastName" value="%{#fio.lastName}" /></td>
            <td class="col_1"><s:text name="ab.person.first_name" /></td>
            <td class="col_1"><s:textfield id="" name="identity.firstName" value="%{#fio.firstName}" /></td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.middle_name" /></td>
            <td class="col_1"><s:textfield id="" name="identity.middleName" value="%{#fio.middleName}" /></td>
            <td class="col_1"><s:text name="ab.person.sex" /></td>
            <td class="col_1">
                <input type="radio" name="identity.sex"<s:if test="#fio.isMan()"> checked="checked"</s:if>
                       value="<s:property value="getSexMan()" />" />
                &nbsp;
                <s:text name="ab.person.sex.man.short" />
                <br>
                <input type="radio" name="identity.sex"<s:if test="#fio.isWoman()"> checked="checked"</s:if>
                       value="<s:property value="getSexWoman()" />" />
                &nbsp;
                <s:text name="ab.person.sex.woman.short" />
            </td>
        </tr>
        <tr class="cols_1">
            <td class="col_1"><s:text name="ab.person.birth_date" /></td>
            <td class="col_1" colspan="3">
                <input type="text" name="identity.birthDateStr" id="birthDate" value="<s:property value="format(#fio.birthDate)" />" readonly="readonly" />
            </td>
        </tr>
        <tr>
            <td colspan="4">
                <input type="button" class="btn-exit" value="<s:text name="common.save" />" onclick="submitFio();" />
            </td>
        </tr>
    </table>

</form>

<div id="registration"></div>
<div id="identities">
    <s:if test="person.notNew">
        <%@include file="person_view_identities.jsp"%>
    </s:if>
</div>
