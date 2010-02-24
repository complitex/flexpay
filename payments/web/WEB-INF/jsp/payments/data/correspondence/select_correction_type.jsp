<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/jquery_bbq.jsp"%>
<%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
        <td class="th">
            <s:text name="payments.select_correction_type" />
        </td>
    </tr>
	<tr class="cols_1">
        <td class="col">
	        <input type="radio" name="type" onclick="selectType('street');" /> <s:text name="ab.street" />
	    </td>
    </tr>
    <tr class="cols_1">
        <td class="col">
            <input type="radio" name="type" onclick="selectType('building');" /> <s:text name="ab.building" />
        </td>
    </tr>
    <tr class="cols_1">
        <td class="col">
            <input type="radio" name="type" onclick="selectType('apartment');" /> <s:text name="ab.apartment" />
        </td>
    </tr>
	<tr class="cols_1">
        <td class="col">
            <input type="radio" name="type" onclick="selectType('person');" /> <s:text name="ab.person" />
	    </td>
    </tr>
</table>

<script type="text/javascript">

    function selectType(type) {
        var params = {
            "record.id":<s:property value="record.id" />,
            type:type
        };
        location.href=$.param.querystring("<s:url action="selectCorrectionType" includeParams="none" />", params);
    }
</script>
