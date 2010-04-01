import java.util.regex.Pattern

def modulesDependencies = [
		common: ['common'],
		ab: ['common', 'ab'],
		ab_sync: ['common', 'ab'],
		admin: ['common', 'admin'],
		bti: ['common', 'ab', 'bti'],
		tc: ['common', 'ab', 'bti', 'tc'],
		orgs: ['common', 'orgs'],
		payments: ['common', 'ab', 'admin', 'orgs', 'payments'],
		rent: ['common', 'ab', 'orgs', 'payments', 'rent'],
		eirc: ['common', 'ab', 'admin', 'bti', 'orgs', 'payments', 'eirc'],
		sz: ['common', 'ab', 'bti', 'orgs', 'payments', 'eirc', 'sz']
]

/**
 * Structure of required properties is a following:
 * Module name : [
 * 		property name : property description
 * ]
 */
def requiredProperties = [
		common: [
				'jdbc.driverClassName': 'Jdbc driver class name',
				'jdbc.url': 'Jdbc Connection url',
				'jdbc.username': 'Database user name to use for connection',
				'jdbc.password': 'Database user password to use for connection',
				'flexpay.email.host': 'SMTP server host',
				'flexpay.email.user_name': 'SMTP server login',
				'flexpay.email.user_pass': 'SMTP server password',
				'flexpay.email.default_to': 'Default email where to send notifications',
				'app.config.common.instanceId': 'Application instance unique identifier',
				'app.config.common.disableSelfValidation': 'Whether to disable self validation of application (true/false)',
				'app.config.common.jms.address': 'JMS server host:port',
				'app.config.common.usersStorage': 'Users data storage type, db or ldap',
				'app.config.common.useOpenSSO' : 'Whether to use OpenSSO for users authentication, (true/false)'
		],
		ab: [:],
		ab_sync: [:],
		admin: [:],
		bti: [:],
		tc: [:],
		orgs: [:],
		payments: [:],
		rent: [:],
		sz: [:],
		eirc: [
				'app.config.eirc.organizationId': 'Identifier of EIRC organization in database',
				'app.config.eirc.eircId': 'Global EIRC code, used in quittance numbers generation for example'
		],
]

def dependentProperties = [
        common : [
                'app.config.common.useOpenSSO' : [
                        common : [
                                'app.config.common.opensso.url' : 'OpenSSO login url'
                        ],
						opensso : [
						        'com.iplanet.services.debug.directory' : 'Output directory where the OpenSSO debug files will be created',
								'com.iplanet.am.naming.url' : 'OpenSSO naming url',
								'com.sun.identity.agents.app.username' : 'OpenSSO user name having permissions to read security attributes',
								'com.iplanet.am.service.password' : 'OpenSSO user password',
								'com.iplanet.am.server.protocol' : 'OpenSSO server protocol',
								'com.iplanet.am.server.host' : 'OpenSSO server host',
								'com.iplanet.am.server.port' : 'OpenSSO server port',
								'com.iplanet.am.services.deploymentDescriptor' : 'OpenSSO server context path',
								'com.iplanet.am.cookie.encode' : 'Whether cookie is encoded by OpenSSO server or not',
								'com.sun.identity.liberty.interaction.wspRedirectHandler' : 'URL for WSPRedirectHandlerServlet to handle Liberty WSF WSP-resource owner interactions based on user agent redirects. This should be running in the same JVM where Liberty SP is running',
								'com.sun.identity.loginurl' : 'Login URL for WSS Liberty use cases',
								'com.sun.identity.liberty.authnsvc.url' : 'Authentication web service URL for WSS Liberty use cases'
						]
                ],
				'app.config.common.usersStorage' : [
				        common : [
								'ldap.url': 'LDAP user storage url',
								'ldap.userDn': 'LDAP administrator distinguished name',
								'ldap.admin': 'LDAP administrator Id',
								'ldap.password': 'LDAP administrator password',
								'ldap.base': 'LDAP base users directory root',
								'ldap.ou.groups': 'LDAP groups sub directory',
								'ldap.ou.people': 'LDAP users sub directory',
								'ldap.policy.names' : 'Manage LDAP policy rules in UI (| is delimiter)'
				        ]
				]
        ]
]

def isTrue = { String input ->
	return 'true' == input
}

def dependencyChecker = {String key, String value ->
	if (key == 'app.config.common.usersStorage') {
		return 'LDAP' == value.toUpperCase()
	}
	if (key == 'app.config.common.useOpenSSO') {
		return isTrue(value)
	}
	return false;
}

class PropertiesUpdater {

	private Map requiredProps
	private File projectRoot
	private Closure dependencyChecker
	private Closure inputChecker
	private Map dependencies

	/**
	 * Load module properties
	 *
	 * @param module Module name
	 * @return Properties set of module properties
	 */
	public Properties load(String module) {
		Properties props = Properties.newInstance()
		File config = configFile(module)
		assert config.exists(), "Cannot find config file for module ${module}, expected ${config.getAbsolutePath()}"
		config.withReader {Reader r ->
			props.load r
		}
		return props
	}

	public String readProp(String defaultValue, BufferedReader reader) {
		print "(Press enter for default) > "
		String input = reader.readLine();
		if (input.trim() == "") {
			return defaultValue;
		}

		return input;
	}

	private void mapModuleProperties(String module, Map props) {
		println "Adding module ${module} properties: ${configFile(module)}"
		props.put(module, load(module))
	}

	private void readNewPropertiesValues(String module, Map props, Map allProps, Map newProps) {

		Map newProperties = newProps[module] as Map ?: [:]
		BufferedReader input = System.in.newReader()
		println "========================================================\nConfiguring module ${module}"
		props.each {String prop, String doc ->
			String defaultValue = allProps[module][prop] as String
			println "${doc} [${defaultValue}]"
			String value = readProp(defaultValue, input)
			if (value != defaultValue) {
				newProperties.put(prop, value)
			}
		}
		newProps.put(module, newProperties)
	}

	/**
	 * read property value, first find in new properties, than in old
	 */
	private String prop(String module, String key, Map allProps, Map newProps) {
		return newProps[module][key] as String ?: allProps[module][key] as String
	}

	public void configure() {
		// map module name to its properties
		def allProperties = [:]
		requiredProps.each {String k, v ->
			mapModuleProperties(k, allProperties)
		}

		// read new properties values to the same structure as requiredProperties [module : [prop1 : val1, prop2 : val2]]
		def newModulesProperties = [:]
		requiredProps.each() {String module, Map props ->
			readNewPropertiesValues(module, props, allProperties, newModulesProperties)
		}

		// check dependencies, dependencies is [depModule : *dep2Props* [depProp : *perModuleProps* [ module : [prop : doc] ] ]
		dependencies.each {String depModule, Map dep2Props ->
			dep2Props.each {String depProp, Map perModuleProps ->
				println "\nChecking dependencies: ${depProp}: ${prop(depModule, depProp, allProperties, newModulesProperties)}"
				if (dependencyChecker(depProp, prop(depModule, depProp, allProperties, newModulesProperties))) {
					println "Added dependency!"
					perModuleProps.each { String module, Map reqProps ->
						// read properties of
						if (!allProperties.keySet().contains(module)) {
							mapModuleProperties(module, allProperties)
						}
						readNewPropertiesValues(module, reqProps, allProperties, newModulesProperties)
					}
				}
			}
		}

		// dump properties
		newModulesProperties.each() {String module, Map props ->
			println "++++++++++++++++++++++++++++++++++++++++++++++++++++++++\nNew module ${module} properties:"
			props.each() {String propKey, String propValue ->
				println "\t${propKey} : ${propValue}"
			}
		}

		// update configuration files
		newModulesProperties.each() {String module, Map props ->
			File configFile = configFile(module)
			String text = configFile.text
			props.each() {String propKey, String propValue ->
				// replace old property value in file text with a new one
				String escapedKey = propKey.replaceAll(/\./, '\\.')
				Pattern pattern = Pattern.compile("^${escapedKey}\\s?[=:]\\s?.*\$", Pattern.MULTILINE)
				text = text.replaceAll(pattern, "${propKey}=${propValue}")
			}

			if (!props.isEmpty()) {
				configFile.text = text
			}
		}
	}

	/**
	 * Get module configuration file
	 *
	 * @param module Module name
	 * @return module configuration file
	 */
	File configFile(String module) {
		// for opensso config file is AMConfig.properties
		if (module == 'opensso') {
			return new File(projectRoot, 'common/etc/AMConfig.properties')
		}
		return new File(projectRoot, "${module}/web/WEB-INF/${module}.properties")
	}

	// ============================================== TESTS =================================================

	void testUpdateProperty() {
		File configFile = configFile('common')
		String propKey = 'jdbc.username'
		String propValue = 'flex'
		String escapedKey = propKey.replaceAll(/\./, '\\.')
		Pattern pattern = Pattern.compile("^${escapedKey}\\s?[=:]\\s?.*\$", Pattern.MULTILINE)
		String text = configFile.text.replaceAll(pattern, "${propKey}=${propValue}")
		println "EscapedKEY: ${escapedKey}, New config:\n${text}"
	}

	private static boolean isDir(File parent, String dir) {
		File file = new File(parent, dir);
		return file.isDirectory();
	}

	public static boolean validRoot(File file) {
		return isDir(file, "common") && isDir(file, "ab") && isDir(file, "bti")  \
			 && isDir(file, "orgs") && isDir(file, "payments") && isDir(file, "eirc")  \
			 && isDir(file, "rent");
	}
}

void usage() {
	println "Project configurer utility, parameters are: [module name]"
}

assert args.length == 1, "Please, set module name as parameter"
assert modulesDependencies.containsKey(args[0]), "Know nothing about module ${args[0]}"

def moduleRequiredProperties = [:]
modulesDependencies[args[0]].each() {String module ->
	moduleRequiredProperties.put(module, requiredProperties[module])
}


File root = new File(".").getAbsoluteFile()
while (root.getParent() != null && !PropertiesUpdater.validRoot(root)) {
	root = root.parentFile
}

assert PropertiesUpdater.validRoot(root), "Invalid startup directory ${new File('').absolutePath}, please, run from project directory"

new PropertiesUpdater(
		requiredProps : moduleRequiredProperties,
		projectRoot : root,
		dependencyChecker : dependencyChecker,
		inputChecker : isTrue,
		dependencies : dependentProperties

).configure();

//new PropertiesUpdater(requiredProps).testUpdateProperty();
