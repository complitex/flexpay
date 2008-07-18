<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>


<s:actionerror />
<s:form>
	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="th" width="1%">&nbsp;</td>
			<td class="th"><s:text name="ab.language"/></td>
			<td class="th"><s:text name="ab.identity_type"/></td>
		</tr>
		<s:iterator value="langToTranslation" status="rowstatus">
			<tr valign="middle" class="cols_1">
				<td class="col_1s">
					<s:property value="#rowstatus.index + 1"/>
				</td>
				<td class="col">
					<s:property value="getLangName(getLang(key))"/>
					<s:if test="getLang(key).default">
						<font color="red">*</font>
					</s:if>
				</td>
				<td class="col">
					<s:textfield name="langToTranslation[%{key}]" value="%{value}"/>
				</td>
			</tr>
		</s:iterator>
		<tr>
			<td colspan="3" height="3" bgcolor="#4a4f4f"/>
		</tr>
		<tr>
			<td colspan="3">
				<s:hidden name="identityType.id" value="%{identityType.id}"/>
				<s:submit name="submitted" value="%{getText('ab.update')}" cssClass="btn-exit"/>
			</td>
		</tr>
	</table>
</s:form>
