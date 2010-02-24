<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_ui_core.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_simplemodal.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
    <tr>
        <td colspan="12">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
        </td>
    </tr>
    <tr>
        <td class="th">&nbsp;</td>
        <td class="th">
            <input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');" />
        </td>
        <td class="th"><s:text name="eirc.registry.record.service" /></td>
        <td class="th"><s:text name="eirc.registry.record.account" /></td>
        <td class="th"><s:text name="eirc.registry.record.address" /></td>
        <td class="th"><s:text name="eirc.registry.record.fio" /></td>
        <td class="th"><s:text name="eirc.date" /></td>
        <td class="th"><s:text name="eirc.registry.record.amount" /></td>
        <td class="th"><s:text name="eirc.registry.record.containers" /></td>
        <td class="th"><s:text name="eirc.registry.record.error" /></td>
        <td class="th"><s:text name="eirc.status" /></td>
        <td class="th"><s:text name="eirc.correspondence" /></td>
    </tr>
    <s:iterator value="records" status="status">
        <tr valign="middle" class="cols_1">
            <td class="col" width="1%" align="right">
                <s:property value="#status.index + pager.thisPageFirstElementNumber + 1" />
            </td>
            <td class="col" width="1%">
                <input type="checkbox" name="objectIds" value="<s:property value="id" />" />
            </td>
            <td class="col">
                <s:if test="service != null">
                    <a href="<s:url action="serviceEdit" namespace="/eirc" includeParams="none"><s:param name="service.id" value="service.id" /></s:url>">
                        <s:property value="serviceCode" />
                    </a>
                </s:if><s:else>
                    <s:property value="serviceCode" />
                </s:else>
            </td>
            <td class="col"><s:property value="personalAccountExt" /></td>
            <td class="col" nowrap="nowrap">
                <s:if test="streetType != null && streetName != null && (buildingNum != null || buildingBulkNum != null) && apartmentNum != null">
                    <s:if test="buildingNum != null && buildingBulkNum != null">
                        <s:set name="addressVal" value="%{streetType + ', ' + streetName + ', ' + buildingNum + ' ' + buildingBulkNum + ', ' + apartmentNum}" />
                    </s:if><s:elseif test="buildingNum != null">
                        <s:set name="addressVal" value="%{streetType + ', ' + streetName + ', ' + buildingNum + ', ' + apartmentNum}" />
                    </s:elseif><s:else>
                        <s:set name="addressVal" value="%{streetType + ', ' + streetName + ', ' + buildingBulkNum + ', ' + apartmentNum}" />
                    </s:else>
                    <s:if test="apartment != null">
                        <a href="<s:url action="apartmentRegistration" namespace="/dicts" includeParams="none"><s:param name="apartment.id" value="apartment.id" /></s:url>">
                            <s:property value="#addressVal" />
                        </a>
                    </s:if><s:else>
                        <s:property value="#addressVal" />
                    </s:else>
                </s:if>
            </td>
            <td class="col">
                <s:if test="firstName != null || middleName != null || lastName != null">
                    <s:set name="fioVal" value="%{(lastName != null ? lastName : '') + (firstName != null ? ' ' + firstName : '') + (middleName != null ? ' ' + middleName : '')}" />
                    <s:if test="person != null">
                        <a href="<s:url action="personView" namespace="/dicts" includeParams="none"><s:param name="person.id" value="person.id" /></s:url>">
                            <s:property value="#fioVal" />
                        </a>
                    </s:if><s:else>
                        <s:property value="#fioVal" />
                    </s:else>
                </s:if>
            <td class="col"><s:date name="operationDate" format="yyyy/MM/dd" /></td>
            <td class="col"><s:property value="amount" /></td>
            <%--<td class="col"><s:property value="containers" /></td>--%>
            <td class="col">N/A</td>
            <td class="col"><s:text name="%{importError.errorId}" /></td>
            <td class="col"><s:text name="%{recordStatus.i18nName}" /></td>
            <td class="col">
                <a href="#" onclick="createDialog(<s:property value="id" />);"><s:text name="common.edit" /></a>
            </td>
        </tr>
    </s:iterator>
    <tr>
        <td colspan="12">
            <%@include file="/WEB-INF/jsp/common/filter/pager/pager_ajax.jsp"%>
            <input type="button" value="<s:text name="eirc.process_selected" />" class="btn-exit" onclick="process();" />
        </td>
    </tr>
</table>

<script type="text/javascript">

    function createDialog(recordId) {

        var src = $.param.querystring("<s:url action="selectCorrectionType" namespace="/payments" includeParams="none" />", {"record.id":recordId});
        $.modal('<iframe src="' + src + '" height="550" width="800" style="border:0" />', {
            containerCss:{
                backgroundColor:"#fff",
                borderColor:"#0063dc",
                height:550,
                padding:0,
                width:800
            },
            escClose:true,
            overlayClose:true,
            onClose: function() {
                $.modal.close();
                pagerAjax();
            }
        });

    }

    function process() {
        var ids = [];
        $("input[name=objectIds]:checked").each(function() {
            ids[ids.length] = this.value;
        });
        if (ids.length == 0) {
            return;
        }
        $.post("<s:url action="registryRecordsProcess" includeParams="none" />",
                {
                    objectIds:ids,
                    "registry.id":<s:property value="registry.id" />
                },
                function(data) {
                    $("#messagesBlock").html(data);
                });
    }
</script>
