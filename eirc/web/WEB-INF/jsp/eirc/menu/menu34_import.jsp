<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.import"/></b>
		<ul class="docs">
			<li><a href="<s:url action='spFileCreate' namespace="/eirc" />"><s:text name="eirc.menu34.files.upload"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<s:url action='spFileList' namespace="/eirc" />"><s:text name="eirc.menu34.file_list"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<s:url action='registriesList' namespace="/eirc" />"><s:text name="eirc.menu34.registries"/></a></li>
		</ul>
	</li>
</ul>
