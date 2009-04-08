import java.text.*

assert args.size() >= 2, 'Please specify version in YYYY-MM-DD[-N] format and module_name'

def versionStr = args[0]
def module = args[1]

assert versionStr =~ /\d\d\d\d-\d\d-\d\d(-\d+)?/, 'Need to specify version as parameter in YYYY-MM-DD[-N] format'

class Version implements Comparable {
	Date date
	int number

	int compareTo(Version v) {
		return date <=> v.date ?: number <=> v.number
	}

	int compareTo(Object v) {
		if (v as Version) {
			return compareTo(v as Version);
		}
		assert false, 'Only compare to Version is supported'
	}

	String toString() {
		return "Date: ${date.format('yyyy-MM-dd')}, number: ${number}"
	}
}

class UpdatePath implements Comparable {
	Version version
	File path

	int compareTo(UpdatePath up) {
		return version <=> up.version
	}

	int compareTo(Object v) {
		if (v as UpdatePath) {
			return compareTo((UpdatePath) v);
		}
		assert false, 'Only compare to UpdatePath is supported'
	}

	String toString() {
		return "Version: ${version}, path: ${path}"
	}
}

def version = new Version(number : 0)
if (versionStr.count("-") == 3) {
	version.number = versionStr.substring("yyyy-MM-dd-".size()) as int
	versionStr = versionStr.substring(0, "yyyy-MM-dd".size())
}
version.date = new SimpleDateFormat("yyyy-MM-dd").parse(versionStr)

def modulesDependencies = [
        common : ['common'],
        ab : ['common', 'ab'],
        bti : ['common', 'ab', 'bti'],
        tc : ['common', 'ab', 'bti', 'tc'],
        orgs : ['common', 'orgs'],
        payments : ['common', 'ab', 'orgs', 'payments'],
        eirc : ['common', 'ab', 'orgs', 'payments', 'eirc'],
        sz : ['common', 'ab', 'orgs', 'payments', 'eirc', 'sz']
]

assert modulesDependencies[module] != null, "Unknown module name ${module}"

// find new updates
def paths = []
modulesDependencies[module].each { moduleName ->
	def dir = new File("${moduleName}/etc/sql/update")
	dir.eachFile { file ->
		if (file.directory) {
			return
		}
		def name = file.name
		if (! (name =~ /\d\d\d\d_\d\d_\d\d(_\d+)?\.sql/)) {
			println "Unsupported file ${file}"
			return
		}
		def sqlPos = name.lastIndexOf(".sql");
		name = name.substring(0, sqlPos)

		def ver = new Version(number : 0)
		if (name.count("_") == 3) {
			def num = name.substring('yyyy_MM_dd_'.size())

			ver.number = num as int
			name = name.substring(0, 'yyyy_MM_dd'.size())
		}
		ver.date = new SimpleDateFormat('yyyy_MM_dd').parse(name)

		if (version < ver) {
			paths.push new UpdatePath(version : ver, path : file)
		}
	}
}

paths.sort()

println "Updates for ${version}"
paths.each { up ->
	println up.path
}
