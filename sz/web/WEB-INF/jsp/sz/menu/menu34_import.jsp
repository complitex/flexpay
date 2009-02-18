<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><strong><s:text name="sz.menu2.import"/></strong>
		<ul class="docs">
			<li><a href="<s:url action="doUpload" namespace="/sz" />"><s:text name="sz.menu34.files.upload"/></a></li>
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
