<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

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
            action:"<s:url action="personsListAjax" namespace="/dicts" includeParams="none" />",
            params:{
                "personSearchFilter.searchString": $("input[name='personSearchFilter.searchString']").get(0).value
            }
        });
    }

    function deleteAjax() {
        FP.serviceElements("<s:url action="personDelete" namespace="/dicts" includeParams="none" />", "objectIds", pagerAjax);
    }

</script>
