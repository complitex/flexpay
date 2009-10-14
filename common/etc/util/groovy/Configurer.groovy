import java.util.regex.Pattern

def modulesDependencies = [
		common: ['common'],
		ab: ['common', 'ab'],
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
				'jdbc.password': 'Database user password to use for connection'
		],
		ab : [:],
		bti : [:],
		tc : [:],
		orgs : [:],
		payments : [:],
		rent : [:],
		sz : [:],
		eirc: [
				'flexpay.module.name.eirc': 'Module name'
		]
]

class PropertiesUpdater {

	public PropertiesUpdater(Map requiredProperties) {
		this.requiredProperties = requiredProperties
	}

	private Map requiredProperties

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
		System.out.print("(Press enter for default) > ")
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
			System.out.println("Adding module ${k} properties: ${configFile(k)}");
			properties.put(k, load(k))
		}

		// read new properties values to the same structure as requiredProperties [module : [prop1 : val1, prop2 : val2]]
		def newModulesProperties = [:]
		requiredProperties.each() {String module, Map props ->
			Map newProperties = [:]
			BufferedReader input = System.in.newReader()
			System.out.println("========================================================\nConfiguring module ${module}")
			props.each {String prop, String doc ->
				String defaultValue = properties[module][prop]
				System.out.println("${doc} [${defaultValue}]")
				String value = readProp(defaultValue, input)
				if (value != defaultValue) {
					newProperties.put(prop, value);
				}
			}
			newModulesProperties.put(module, newProperties)
		}

		// dump properties
		newModulesProperties.each() {String module, Map props ->
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++\nNew module ${module} properties:");
			props.each() {String propKey, String propValue ->
				System.out.println("\t${propKey} : ${propValue}");
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
		return new File("${module}/web/WEB-INF/${module}.properties");
	}

	// ============================================== TESTS =================================================
	void testUpdateProperty() {
		File configFile = configFile('common')
		String propKey = 'jdbc.username'
		String propValue = 'flex'
		String escapedKey = propKey.replaceAll(/\./, '\\.')
		Pattern pattern = Pattern.compile("^${escapedKey}\\s?[=:]\\s?.*\$", Pattern.MULTILINE)
		String text = configFile.text.replaceAll(pattern, "${propKey}=${propValue}")
		System.out.println("EscapedKEY: ${escapedKey}, New config:\n${text}")
	}
}


assert args.length == 1, "Please, set module name as parameter"
assert modulesDependencies.containsKey(args[0]), "Know nothing about module ${args[0]}"

def moduleRequiredProperties = [:]
modulesDependencies[args[0]].each() {String module ->
	moduleRequiredProperties.put(module, requiredProperties[module])
}
new PropertiesUpdater(moduleRequiredProperties).configure();







//new PropertiesUpdater(requiredProperties).testUpdateProperty();
