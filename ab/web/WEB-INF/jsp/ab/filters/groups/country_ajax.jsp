<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bgiframe.jsp"%>
<%@include file="/WEB-INF/jsp/ab/includes/flexpay_filter.jsp"%>

<s:if test="#readonly == null">
    <s:set name="readonly" value="false" />
</s:if>

<script type="text/javascript">

    $(function() {
        FF.createFilter("country", {
            action: "<s:url action="countryFilterAjax" namespace="/dicts" />",
            <s:if test="#readonly">display:"input-readonly",</s:if>
            defaultValue: "<s:property value="countryFilter != null ? countryFilter : userPreferences.countryFilter" />"
        });
    });

</script>

<table width="100%">
    <tr>
        <td class="filter"><s:text name="ab.country" /></td>
        <td id="country_raw"></td>
        <td colspan="4">&nbsp;</td>
    </tr>
</table>
