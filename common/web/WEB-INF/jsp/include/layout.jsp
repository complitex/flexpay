<%@ include file="taglibs.jsp" %>

<tiles:insertAttribute name="header"/>
<tiles:insertAttribute name="menu"/>

<div class="main">
	<div class="main-content">
		<tiles:insertAttribute name="content"/>
	</div>
</div>

<tiles:insertAttribute name="footer"/>