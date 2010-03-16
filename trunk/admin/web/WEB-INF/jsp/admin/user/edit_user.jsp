<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<div id="response">
    <%@include file="/WEB-INF/jsp/common/includes/errors_messages.jsp"%>
</div>

<s:form action="editUser" method="POST">

	<s:set name="readonly" value="%{currentUserPreferences.username != null && currentUserPreferences.username != ''}" />
	<s:set name="checkOldPassword" value="%{checkOldPassword}" />

	<table cellpadding="3" cellspacing="1" border="0" width="100%">
		<tr>
			<td class="col"><s:text name="admin.user.name" />*:</td>
			<td class="col">
				<s:if test="#readonly">
					<s:textfield name="userName" value="%{currentUserPreferences.username}" maxlength="255" readonly="true" />
				</s:if>
				<s:else>
					<s:textfield name="userName" value="%{currentUserPreferences.username}" maxlength="255" readonly="false" />
				</s:else>
			</td>
		</tr>
		<tr>
			<td class="col"><s:text name="admin.user.lastname" />*:</td>
			<td class="col">
				<s:textfield name="lastName" value="%{currentUserPreferences.lastName}" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td class="col"><s:text name="admin.user.firstname" />*:</td>
			<td class="col">
				<s:textfield name="firstName" value="%{currentUserPreferences.firstName}" maxlength="255" />
			</td>
		</tr>
		<tr>
			<td class="col"><s:text name="admin.user.password" />:</td>
			<td class="col">
				<s:password name="password" maxlength="12" />
			</td>
		</tr>
		<s:if test="#checkOldPassword">
			<tr>
				<td class="col"><s:text name="admin.user.old_password" />:</td>
				<td class="col">
					<s:password name="oldPassword" maxlength="12" />
				</td>
			</tr>
		</s:if>
		<tr>
			<td colspan="2">
                <s:submit cssClass="btn-exit" name="submitted" value="%{getText('common.save')}" />
			</td>
		</tr>
	</table>
</s:form>