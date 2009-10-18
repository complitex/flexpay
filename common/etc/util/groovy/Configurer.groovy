import java.util.regex.Pattern

def modulesDependencies = [
		common: ['common'],
		ab: ['common', 'ab'],
		ab_sync: ['common', 'an'],
		bti: ['common', 'ab', 'bti'],
		tc: ['common', 'ab', 'bti', 'tc'],
		orgs: ['common', 'orgs'],
		payments: ['common', 'ab', 'orgs', 'payments'],
		rent: ['common', 'ab', 'orgs', 'payments', 'rent'],
		eirc: ['common', 'ab', 'bti', 'orgs', 'payments', 'eirc'],
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
				'app.config.common.usersStorage': 'Users data storage type, db or ldap',
				'ldap.url': 'LDAP user storage url',
				'ldap.userDn': 'LDAP administrator distinguished name',
				'ldap.password': 'LDAP administrator password',
				'ldap.base': 'LDAP users directory root',
				'flexpay.email.host': 'SMTP server host',
				'flexpay.email.user_name': 'SMTP server login',
				'flexpay.email.user_pass': 'SMTP server password',
				'flexpay.email.default_to': 'Default email where to send notifications',
				'app.config.common.instanceId': 'Application instance unique identifier',
				'app.config.common.jms.address': 'JMS server host:port'
		],
		ab: [:],
		ab_sync: [:],
		bti: [:],
		tc: [:],
		orgs: [:],
		payments: [:],
		rent: [:],
		sz: [:],
		eirc: [
				'app.config.eirc.organizationId': 'Identifier of EIRC organization in database',
				'app.config.eirc.eircId': 'Global EIRC code, used in quittance numbers generation for example'
		]
]

class PropertiesUpdater {

	public PropertiesUpdater(Map requiredProperties, File root) {
		this.requiredProperties = requiredProperties
		this.projectRoot = root
	}

	private Map requiredProperties
	private File projectRoot

	/**
	 * Load module properties
	 *
	 * @param module Module name
	 * @return Properties set of module properties
	 */
	public Properties load(String module) {
		Properties props = Properties.newInstance()
		File config = configFile(module)
		config.withReader {Reader r ->
			props.load r
		}
		return props
	}

	public String readProp(String defaultValue, BufferedReader reader) {
		print("(Press enter for default) > ")
		String input = reader.readLine();
		if (input.trim() == "") {
			return defaultValue;
		}

		return input;
	}


	public void configure() {
		// map module name to its properties
		def properties = [:]
		requiredProperties.each {String k, v ->
			println("Adding module ${k} properties: ${configFile(k)}");
			properties.put(k, load(k))
		}

		// read new properties values to the same structure as requiredProperties [module : [prop1 : val1, prop2 : val2]]
		def newModulesProperties = [:]
		requiredProperties.each() {String module, Map props ->
			Map newProperties = [:]
			BufferedReader input = System.in.newReader()
			println("========================================================\nConfiguring module ${module}")
			props.each {String prop, String doc ->
				String defaultValue = properties[module][prop]
				println("${doc} [${defaultValue}]")
				String value = readProp(defaultValue, input)
				if (value != defaultValue) {
					newProperties.put(prop, value)
				}
			}
			newModulesProperties.put(module, newProperties)
		}

		// dump properties
		newModulesProperties.each() {String module, Map props ->
			println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++\nNew module ${module} properties:")
			props.each() {String propKey, String propValue ->
				println("\t${propKey} : ${propValue}")
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
		println("EscapedKEY: ${escapedKey}, New config:\n${text}")
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

new PropertiesUpdater(moduleRequiredProperties, root).configure();

//new PropertiesUpdater(requiredProperties).testUpdateProperty();
