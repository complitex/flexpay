<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<script type="text/javascript">

    function toggleGroup(id) {
        $("#toggle" + id).val($("#group" + id).toggle().is(":visible") ? "<s:text name="common.hide" />" : "<s:text name="common.show" />");
    }

    function saveAttributes() {

        var params = {
            <s:iterator value="groups"><s:set name="types" value="typesMap[id]" /><s:iterator value="types" id="type">"attrs[<s:property value="#type.id" />]":"",</s:iterator></s:iterator>
            "building.id":<s:property value="building.id" />,
            attributeDate:"<s:property value="attributeDate" />"
        };

        $("#attrs :selected").each(function() {
            params[$(this).parent().get(0).name] = this.value;
        });
        $("#attrs input[id^=attrs]").each(function() {
            params[this.name] = this.value;
        });

        $.post("<s:url action="buildingAttributesSave" includeParams="none" />", params,
                function(data, status) {
                    if (data == "" && status == "success") {
                        window.location.href = FP.base;
                    }
                    $("#response").html(data);
                });
    }

</script>

<style type="text/css">

    .attrGroup {
        width:100%;
        font-size:100%;
        font-weight:bold;
    }

</style>

<div id="response"></div>

<form id="attrs">
<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td class="th_s"><s:text name="tc.building_attributes" /></td>
        <td class="th_s">
            <input type="button" value="<s:text name="common.save" />" class="btn-exit" onclick="saveAttributes();" />
        </td>
    </tr>
    <s:iterator value="groups" status="groupStatus">
        <s:set name="types" value="typesMap[id]" />

        <s:if test="!#types.isEmpty()">
            <tr>
                <td class="th" colspan="2" style="padding:0;">
                    <table class="attrGroup">
                        <tr>
                            <td><s:property value="getTranslationName(translations)" /></td>
                            <td style="text-align:right;">
                                <input type="button" class="btn-exit" id="toggle<s:property value="id" />"
                                       onclick="toggleGroup(<s:property value="id" />);"
                                       value="<s:property value="#groupStatus.first ? getText('common.hide') : getText('common.show')" />" />
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr id="group<s:property value="id" />" class="cols_1"<s:if test="!#groupStatus.first"> style="display:none;"</s:if>>
                <td colspan="2">
                    <table>
                        <s:iterator value="types" id="type">
                            <tr class="cols_1">
                                <td class="col" style="width:80%;">
                                    <s:property value="getTranslationName(#type.translations)" />
                                </td>
                                <td class="col">
                                    <nobr>
                                        <s:if test="isBuildingAttributeTypeSimple(#type)">
                                            <s:textfield id="attrs%{#type.id}" name="attrs[%{#type.id}]" value="%{attrs[#type.id]}" cssStyle="width:140px;" />
                                        </s:if><s:elseif test="isBuildingAttributeTypeEnum(#type)">
                                            <s:select id="attrs%{#type.id}" name="attrs[%{#type.id}]" value="%{attrs[#type.id]}" list="#type.sortedValues" listKey="value" listValue="value" emptyOption="true" cssStyle="width:140px;" />
                                        </s:elseif>
                                        <s:if test="#type.isTemp()"><img src="<s:url value="/resources/common/img/i_clock.gif" includeParams="none" />" alt="<s:text name="tc.temp_attribute" />" style="vertical-align:middle;" /></s:if>
                                    </nobr>
                                </td>
                            </tr>
                        </s:iterator>
                    </table>
                </td>
            </tr>
        </s:if>

    </s:iterator>
</table>
</form>
