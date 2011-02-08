<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<br clear="all" />

<p></p>

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<td align="center">
			<span class="text-small">&#xA9; 2011 Flexpay project</span>
		</td>
	</tr>
	<sec:authorize ifAllGranted="ROLE_COMMON_DEVELOPER">
		<tr>
			<td align="center">
				<a href="<s:url action="about" namespace="/common" includeParams="none" />"><s:text name="common.build.about" /></a>
			</td>
		</tr>
	</sec:authorize>
</table>
