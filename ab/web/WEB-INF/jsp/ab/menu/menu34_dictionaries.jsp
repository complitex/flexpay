<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="menu2.dictionaries"/></b>
		<ul class="docs">
			<li><a href="<s:url namespace="/dicts" action='list_persons' />"><s:text name="menu34.dictionaries.persons"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_countries' />"><s:text name="menu34.dictionaries.countries"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_regions' />"><s:text name="menu34.dictionaries.regions"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_towns' />"><s:text name="menu34.dictionaries.towns"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_districts' />"><s:text name="menu34.dictionaries.districts"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_streets' />"><s:text name="menu34.dictionaries.streets"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_buildings' />"><s:text name="menu34.dictionaries.buildings"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='list_town_types' />"><s:text name="menu34.dictionaries.town_types"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='street_type_list' />"><s:text name="menu34.dictionaries.street_types"/></a></li>
			<li><a href="<s:url namespace="/dicts" action='identity_type_listn' />"><s:text name="menu34.dictionaries.identity_types"/></a></li>
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
