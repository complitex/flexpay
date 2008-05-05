<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<s:if test="status == @org.flexpay.ab.actions.apartment.ApartmentEditAction@STATUS_BLANC_NUMBER">
	<font color="red">typed blanc apartment number</font>
</s:if>
<s:if test="status == @org.flexpay.ab.actions.apartment.ApartmentEditAction@STATUS_NUMBER_ALREDY_EXIST">
	<font color="red">typed apartment number alredy exist</font>
</s:if>

<s:form>
<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="col">
				<s:text name="ab.apartment.number" />
			</td>
			<td class="col">
				<s:textfield name="apartmentNumber" value="%{apartment.number}" />
			</td>
		</tr>
		<tr>
			<td colspan="4" height="3" bgcolor="#4a4f4f"/>
		</tr>	
		<tr>
		  <td colspan="4">
		    <s:hidden name="apartment.id" value="%{apartment.id}" />
		    <s:submit name="submit" value="%{getText('common.save')}" cssClass="btn-exit" />
		  </td>
		</tr>
</table>
</s:form>