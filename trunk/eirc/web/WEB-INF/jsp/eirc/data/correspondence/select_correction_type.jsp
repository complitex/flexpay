<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<div align="center" width="100%">
	<s:text name="ab.select_correction_type" />:
	<input type="radio" id="address" onchange="window.location = <s:url action="registry_record_correct_address" />" /> <s:text name="ab.address" />
	<input type="radio" id="person" onchange="window.location = <s:url action="registry_record_correct_person" />" /> <s:text name="ab.person" />
</div>
