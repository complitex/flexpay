
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:actionmessage/>

<s:form action="generateRegistry">

<s:text name="payments.registry.generate.generate_for"/>
<select>
	<option selected="selected"><s:text name="payments.demo.data.months.mar2009"/></option>
	<option><s:text name="payments.demo.data.months.feb2009"/></option>
	<option><s:text name="payments.demo.data.months.jan2009"/></option>
	<option><s:text name="payments.demo.data.months.dec2008"/></option>
</select>

<s:submit name="submitted" cssClass="btn-exit" value="%{getText('payments.registry.generate.generate')}"/>

</s:form>