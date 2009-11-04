<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes" omit-xml-declaration="no"
				doctype-public="-//Sun Microsystems, Inc.//DTD Application Server 9.0 Servlet 2.5//EN"
				doctype-system="http://www.sun.com/software/dtd/appserver/sun-web-app_2_5-0.dtd" />

	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="/sun-web-app/security-role-mapping" />

	<xsl:template match="@error-url" />

</xsl:stylesheet>
