<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
<%@include file="/WEB-INF/jsp/payments/data/registry_record_info.jsp"%>
<%@include file="/WEB-INF/jsp/ab/filters/groups/country_region_town_street_building_apartment_ajax.jsp"%>

<input id="setBut" type="button" class="btn-exit" onclick="set();" value="<s:text name="common.set" />" style="display:none;" />

<script type="text/javascript">

    $(function() {
        FF.addListener("apartment", function() {
            $("#setBut").show();
        });
        FF.addEraser("apartment", function() {
            $("#setBut").hide();
        });
    });

    function set() {
        $.post("<s:url action="setCorrection" includeParams="none" />", {
                    "record.id":<s:property value="record.id" />,
                    "object.id":FF.filters["apartment"].value.val(),
                    type:"apartment"
                }, function(data) {
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
