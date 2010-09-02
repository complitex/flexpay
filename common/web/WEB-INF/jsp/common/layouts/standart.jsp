<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>
<%@taglib prefix="menu" uri="http://struts-menu.sourceforge.net/tag"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<tiles:importAttribute ignore="true" name="title" scope="request" />
	<title><s:text name="%{#attr.title}" /></title>

	<%@include file="scripts.jsp"%>

</head>
<body>

<tiles:insertAttribute name="header" />

<table cellpadding="0" cellspacing="0" border="0" width="100%">
	<tr>
		<menu:useMenuDisplayer name="fpDisplayer">
			<menu:displayMenu name="FPMenu" levelBegin="1" levelEnd="1" />
		</menu:useMenuDisplayer>

		<tiles:insertAttribute name="language" />

		<!--<form name="search">-->
		<td class="topmenu_form_search" nowrap>&nbsp;
			<!--<input type="text" class="form-search">&nbsp;<input type="button" value="Найти" class="btn-search" />-->
		</td>
		<!--</form>-->
	</tr>
</table>

<menu:useMenuDisplayer name="fpDisplayer">
	<menu:displayMenu name="FPMenu" levelBegin="2" levelEnd="2" />
</menu:useMenuDisplayer>


<div class="crumbs">
	<div class="crumbs-padding">
		<tiles:insertAttribute name="breadCrumbs" />
	</div>
</div>

<div class="columns">

	<menu:useMenuDisplayer name="fpDisplayer">
		<menu:displayMenu name="FPMenu" levelBegin="3" />
	</menu:useMenuDisplayer>

	<div class="main">
		<div class="main-content">
			<tiles:insertAttribute name="body" ignore="true" />
		</div>

		<!--
				<div class="main-tip">
					<div class="main-tip-padding">
						<tiles:insertAttribute name="tip" />
					</div>
				</div>
				-->
	</div>

</div>

<tiles:insertAttribute name="footer" />

</body>
</html>
