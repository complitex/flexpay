
<%@include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<%@include file="/WEB-INF/jsp/common/includes/jquery_ui.jsp"%>

<s:actionerror />

<s:form id="frecords">
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td colspan="11">
				<%@include file="/WEB-INF/jsp/ab/filters/import_error_type_filter.jsp" %>
				<%@include file="../filters/registry_record_status_filter.jsp" %>
				<input type="submit" value="<s:text name="eirc.filter" />" class="btn-exit"/>

				<%@include file="../data/registry_info.jsp" %>
			</td>
		</tr>
		<tr>
			<td class="th">&nbsp;</td>
			<td class="th"><input type="checkbox" onchange="FP.setCheckboxes(this.checked, 'objectIds');"></td>
			<td class="th"><s:text name="eirc.registry.record.service"/></td>
			<td class="th"><s:text name="eirc.registry.record.account"/></td>
			<td class="th"><s:text name="eirc.registry.record.address"/></td>
			<td class="th"><s:text name="eirc.registry.record.fio"/></td>
			<td class="th"><s:text name="eirc.date"/></td>
			<td class="th"><s:text name="eirc.registry.record.amount"/></td>
			<td class="th"><s:text name="eirc.registry.record.containers"/></td>
			<td class="th"><s:text name="eirc.registry.record.error"/></td>
			<td class="th"><s:text name="eirc.status"/></td>
			<td class="th"><s:text name="eirc.correspondence"/></td>

		</tr>
		<s:iterator value="records" status="status">
			<tr valign="middle" class="cols_1">
				<td class="col" width="1%" align="right">
					<s:property value="%{#status.index + pager.thisPageFirstElementNumber + 1}"/>
				</td>
				<td class="col" width="1%"><input type="checkbox" name="objectIds" value="<s:property value="%{id}"/>"/></td>
				<td class="col">
					<s:if test="service != null">
						<a href="<s:url action="serviceEdit" namespace="/eirc" includeParams="none"><s:param name="service.id" value="service.id" /></s:url>">
							<s:property value="serviceCode"/>
						</a>
					</s:if><s:else>
					<s:property value="serviceCode"/>
				</s:else>

				</td>
				<td class="col"><s:property value="%{personalAccountExt}"/></td>
				<td class="col" nowrap="nowrap">
					<s:if test="streetType != null && streetName != null && buildingNum != null || buildingBulkNum != null || apartmentNum != null">
						<s:set name="addressVal"
							   value="%{streetType + ', ' + streetName + ', ' + buildingNum + ' ' + buildingBulkNum + ', ' + apartmentNum}"/>
						<s:if test="apartment != null">
							<a href="<s:url action="apartmentRegistration" namespace="/dicts" includeParams="none"><s:param name="apartment.id" value="apartment.id" /></s:url>">
								<s:property value="addressVal"/>
							</a>
						</s:if>
						<s:else>
							<s:property value="addressVal"/>
						</s:else>
					</s:if>
				</td>
				<td class="col">
					<s:if test="firstName != null || middleName != null || lastName != null">
						<s:set name="fioVal" value="%{firstName + ' ' + middleName + ' ' + lastName}" />
					<s:if test="person != null">
					<a href="<s:url action="personView" namespace="/dicts"><s:param name="person.id" value="person.id" /></s:url>">
						<s:property value="#fioVal"/>
					</a>
					</s:if>
					<s:else>
						<s:property value="#fioVal" />
					</s:else>
					</s:if>
				<td class="col"><s:date name="operationDate" format="yyyy/MM/dd"/></td>
				<td class="col"><s:property value="%{amount}"/></td>
					<%--<td class="col"><s:property value="%{containers}" /></td>--%>
				<td class="col">N/A</td>
				<!-- TODO uncomment as fix for LocalizedTextUtil NPE available (see https://xwork.dev.java.net/issues/show_bug.cgi?id=6)-->
				<%--<td class="col"><s:text name="%{importError.errorId}"/></td>--%>
				<td class="col"><s:text name="%{importError.errorId}"/></td>
				<td class="col"><s:text name="%{recordStatus.i18nName}"/></td>

				<td class="col">
                    <a href="javascript:createDialog(<s:property value="%{id}" />)">
					    <s:text name="common.edit"/>
                    </a>
				</td>

			</tr>
		</s:iterator>
		<tr>
			<td colspan="11">
				<%@include file="/WEB-INF/jsp/common/filter/pager/pager.jsp" %>
				<input type="submit" value="<s:text name="eirc.process_selected" />" class="btn-exit"
					   onclick="$('#frecords').attr('action', '<s:url action="registryRecordsProcess" includeParams="none"/>');"/>
		</tr>
	</table>
	<s:hidden name="registry.id"/>
</s:form>

<script type="text/javascript">

    function createDialog(recordId) {

        $("#dialog").append(
                $('<iframe name="frame" src="<s:url action="selectCorrectionType" namespace="/payments" includeParams="none"/>?record.id=' + recordId + '" width="600" height="320"></iframe>').
                        css({"border" : "none"})
        );

        $("#dialog").dialog({
            bgiframe: true,
            modal: true,
            width: 640,
            height: 400,
            closeOnEscape: true,
            title: "<s:text name="eirc.registry.view.dialog_title" />",
            autoOpen: false,
            buttons: {
                '<s:text name="common.cancel" />' : function() {
                    $(this).html("").dialog("destroy");
                }
            }
        });

        $("#dialog").dialog("open");
    }

</script>

<div id="dialog" style="display:none;"></div>
