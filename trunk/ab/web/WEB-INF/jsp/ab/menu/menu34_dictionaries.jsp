<%@ include file="/WEB-INF/jsp/common/taglibs.jsp" %>

<ul class="folders">
	<li class="open"><b><s:text name="menu2.adress_dictionaries"/></b>
		<ul class="docs">
			<li>
				<a href="<s:url namespace="/dicts" action='list_countries' includeParams="none" />"><s:text
						name="menu34.dictionaries.countries"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_regions' includeParams="none" />"><s:text
						name="menu34.dictionaries.regions"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_towns' includeParams="none" />"><s:text
						name="menu34.dictionaries.towns"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_districts' includeParams="none" />"><s:text
						name="menu34.dictionaries.districts"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_streets' includeParams="none" />"><s:text
						name="menu34.dictionaries.streets"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_buildings' includeParams="none"/>"><s:text
						name="menu34.dictionaries.buildings"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='list_apartments' includeParams="none"/>"><s:text
						name="menu34.dictionaries.apartments"/></a></li>
            </ul>
    </li>
    <li class="open"><b><s:text name="menu2.person_dictionaries"/> </b>
        <ul class="docs">
            <li>
                <a href="<s:url namespace="/dicts" action='list_persons' includeParams="none" />"><s:text
                        name="menu34.dictionaries.persons"/></a></li>
            </ul>
    </li>
    <li class="open"><b><s:text name="menu2.type_dictionaries"/> </b>
        <ul class="docs">
            <li>
				<a href="<s:url namespace="/dicts" action='town_type_list' includeParams="none" />"><s:text
						name="menu34.dictionaries.town_types"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='street_type_list' includeParams="none" />"><s:text
						name="menu34.dictionaries.street_types"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='identity_type_list' includeParams="none" />"><s:text
						name="menu34.dictionaries.identity_types"/></a></li>
			<li>
				<a href="<s:url namespace="/dicts" action='buildingAttributeTypeListAction' includeParams="none" />"><s:text
						name="ab.buildings.attribute_types"/></a></li>
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
