<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<s:form action="eircAccountCreate" method="post">

    <table cellpadding="3" cellspacing="1" border="0" width="100%">
        <tr>
            <td colspan="2">
                <%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp"%>
            </td>
        </tr>
        <tr>
            <td class="col"><s:text name="eirc.eirc_account.consumer_fio" />:</td>
            <td class="col">
                <s:textfield name="consumerFio" value="%{consumerFio}" maxlength="255" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <s:submit id="submitButton" cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" disabled="true" />
            </td>
        </tr>
    </table>

</s:form>

<script type="text/javascript">

    $(function() {

        FF.addListener("apartment", function() {
            $("#submitButton").removeAttr("disabled");
        });

        FF.addEraser("apartment", function() {
            $("#submitButton").attr("disabled", true);
        });

    });

</script>
