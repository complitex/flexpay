<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="menu2.dictionaries"/></b>
		<ul class="docs">
			<li><a href="<c:url value='/dicts/list_persons.action' />"><s:text name="menu34.dictionaries.persons"/></a></li>
			<li><a href="<c:url value='/dicts/list_countries.action' />"><s:text name="menu34.dictionaries.countries"/></a></li>
			<li><a href="<c:url value='/dicts/list_regions.action' />"><s:text name="menu34.dictionaries.regions"/></a></li>
			<li><a href="<c:url value='/dicts/list_towns.action' />"><s:text name="menu34.dictionaries.towns"/></a></li>
			<li><a href="<c:url value='/dicts/list_districts.action' />"><s:text name="menu34.dictionaries.districts"/></a></li>
			<li><a href="<c:url value='/dicts/list_streets.action' />"><s:text name="menu34.dictionaries.streets"/></a></li>
			<li><a href="<c:url value='/dicts/list_town_types.action' />"><s:text name="menu34.dictionaries.town_types"/></a></li>
			<li><a href="<c:url value='/dicts/street_type_list.action' />"><s:text name="menu34.dictionaries.street_types"/></a></li>		
			<li><a href="<c:url value='/dicts/identity_type_list.action' />"><s:text name="menu34.dictionaries.identity_types"/></a></li>
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
