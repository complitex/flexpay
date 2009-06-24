<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>
<html>
<head><title>Build info page</title></head>
<body>
<table>
	<tr>
		<td><s:text name="common.build.version" /></td>
		<td>@@@VERSION@@@</td>
	</tr>
	<tr>
		<td><s:text name="common.build.time" /></td>
		<td>@@@TIME@@@</td>
	</tr>
	<tr>
		<td colspan="2"><a href="<s:url action="diff" namespace="/common" includeParams="none"/>"><s:text
				name="common.build.changes" /></a></td>
	</tr>
	<tr>
		<td colspan="2"><s:text name="common.build.changed_files" /></td>
	</tr>
	<tr>
		<td colspan="2">
			<pre>@@@FILESLIST@@@</pre>
		</td>
	</tr>
</table>
</body>
</html>
