<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="eirc.menu2.import"/></b>
		<ul class="docs">
			<li><a href="<c:url value='/eirc/spFileCreate.action' />"><s:text name="eirc.menu34.files.upload"/></a></li>
		</ul>
		<ul class="docs">
			<li><a href="<c:url value='/eirc/spFileList.action' />"><s:text name="eirc.menu34.sp_file_list"/></a></li>
		</ul>
	</li>
	

	<!--
		<li><a href="123123">Some folder</a>
			<ul class="docs">
				<li><a href="123">Some document</a></li>
			</ul>
		</li>
		-->

</ul>
