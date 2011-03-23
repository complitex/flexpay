<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<s:set name="checkOldPassword" value="%{checkOldPassword}" />
<s:set name="passwordRequired" value="%{passwordRequired}" />

<table cellpadding="3" cellspacing="1" border="0" width="100%">
	<tr>
		<td class="col"><s:text name="admin.user.name" />*:</td>
		<td class="col">
			<s:if test="#readonly">
				<s:textfield name="model.userName" value="%{currentUserPreferences.username}" maxlength="255" readonly="true" />
			</s:if><s:else>
				<s:textfield name="model.userName" value="%{currentUserPreferences.username}" maxlength="255" readonly="false" />
			</s:else>
		</td>
	</tr>
	<tr>
		<td class="col"><s:text name="admin.user.lastname" />*:</td>
		<td class="col">
			<s:textfield name="model.lastName" value="%{currentUserPreferences.lastName}" maxlength="255" />
		</td>
	</tr>
	<tr>
		<td class="col"><s:text name="admin.user.firstname" />*:</td>
		<td class="col">
			<s:textfield name="model.firstName" value="%{currentUserPreferences.firstName}" maxlength="255" />
		</td>
	</tr>
	<tr>
		<td class="col"><s:text name="admin.user.role" />:</td>
		<td class="col">
			<select name="model.roleId" class="form-select">
					<option value="-1"></option>
				<s:iterator value="userRoles">
					<option value="<s:property value="id" />"<s:if test="currentUserPreferences.userRole != null && id == currentUserPreferences.userRole.id"> selected</s:if>>
						<s:property value="getTranslationName(translations)" />
					</option>
				</s:iterator>
			</select>
		</td>
	</tr>
	<s:if test="#checkOldPassword">
		<tr>
			<td class="col"><s:text name="admin.user.old_password" />:</td>
			<td class="col">
				<s:password name="model.oldPassword" maxlength="12" />
			</td>
		</tr>
	</s:if>
	<tr>
		<td class="col">
			<s:text name="admin.user.password" />
			<s:if test="#passwordRequired">*</s:if>:
		</td>
		<td class="col">
			<s:password name="model.password" maxlength="12" />
		</td>
	</tr>
	<tr>
		<td class="col">
			<s:text name="admin.user.re_enter_password" />
			<s:if test="#passwordRequired">*</s:if>:
		</td>
		<td class="col">
			<s:password name="model.reEnterPassword" maxlength="12" />
		</td>
	</tr>
	<tr>
		<td colspan="2">
            <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
			<s:if test="#readonly">
						<input type="button" class="btn-exit"
									 onclick="window.location='<s:url action="certificateEdit"><s:param name="alias" value="%{currentUserPreferences.username}" /></s:url>'"
									 value="<s:text name="admin.certificate.edit" />" />
				<%@include file="user_extend.jsp"%>
			</s:if>
		</td>
	</tr>
</table>
