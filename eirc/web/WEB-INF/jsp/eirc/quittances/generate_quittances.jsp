<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_validation.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<script type="text/javascript">

	$(function() {
		$.validator.addMethod("datesCheck", function (value, element) {
			var begin = new Date($("#beginDateFilter").val());
			var end = new Date($("#endDateFilter").val());
			return this.optional(element) || begin <= end;
		});

		$.validator.addMethod("sameMonth", function (value, element) {
			var begin = new Date($("#beginDateFilter").val());
			var end = new Date($("#endDateFilter").val());
			return this.optional(element) || begin.getYear() == end.getYear() && begin.getMonth() == end.getMonth();
		});

		$("#qgform").validate({
			rules : {
				"beginDateFilter.stringDate" : {
					required : true,
					date : true,
					datesCheck : true,
					sameMonth : true
				},
				"endDateFilter.stringDate" : {
					required : true,
					date : true,
					datesCheck : true,
					sameMonth : true
				}
			},
			messages : {
				"beginDateFilter.stringDate" : {
					required : "<s:text name="eirc.error.quittance.job.no_dates" />",
					datesCheck : "<s:text name="eirc.error.quittance.job.begin_after_end" />",
					sameMonth : "<s:text name="eirc.error.quittance.job.month_only_allowed" />"
				},
				"endDateFilter.stringDate" : {
					required : "<s:text name="eirc.error.quittance.job.no_dates" />",
					datesCheck : "<s:text name="eirc.error.quittance.job.begin_after_end" />",
					sameMonth : "<s:text name="eirc.error.quittance.job.month_only_allowed" />"
				}
			},
			errorPlacement: function(error, element) {
				var row = element.parent("td").parent("tr").prev("tr");
				var cell = row.children()[0];

				error.appendTo(cell);
				row.css("display", "table-row");
			}
		});
	});
</script>

<s:form id="qgform" action="quittanceGenerate">
    <table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr class="cols_1_error" style="display:none;">
            <td colspan="4"></td>
		</tr>
		<tr valign="middle">
			<td class="col">
				<%@include file="/WEB-INF/jsp/orgs/filters/service_organization_filter.jsp"%>
			</td>
		</tr>
		<tr class="cols_1_error" style="display:none;">
            <td colspan="4"></td>
		</tr>
		<tr>
			<td class="col">
				<s:text name="common.from" />
				<%@include file="/WEB-INF/jsp/common/filter/begin_date_filter.jsp" %>
			</td>
		</tr>
		<tr class="cols_1_error" style="display:none;">
            <td colspan="4"></td>
		</tr>
		<tr>
			<td class="col">
				<s:text name="common.till" />
                <%@include file="/WEB-INF/jsp/common/filter/end_date_filter.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="center">
				<s:submit name="submitted" value="%{getText('common.upload')}" cssClass="btn-exit" />
			</td>
		</tr>
    </table>
</s:form>
